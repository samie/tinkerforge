package org.vaadin.mqtt.ui.displays;

import com.vaadin.addon.charts.Chart;
import com.vaadin.addon.charts.model.ChartType;
import com.vaadin.addon.charts.model.DataSeries;
import com.vaadin.data.util.converter.StringToDateConverter;
import com.vaadin.ui.Grid;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import org.vaadin.mqtt.ui.MqttDataSource;
import org.vaadin.mqtt.ui.MqttMessageConverter;
import org.vaadin.viritin.ListContainer;

/**
 * Gauge display for MQTT client.
 *
 * @author Sami Ekblad
 */
public class ListDisplay extends MqttDisplay {

    private final List<Message> list = new ArrayList<>();
    private final SimpleDateFormat df = new SimpleDateFormat("yyyy-mm-dd HH:MM:ss");
    private final Grid grid = new Grid();

    public ListDisplay(MqttDataSource source, MqttMessageConverter converter) {
        super(source, 10, converter);
    }

    /**
     * Show the grid instead of chart.
     *
     */
    @Override
    public void showChart() {
        removeAllComponents();
        addComponents(grid);
    }

    @Override
    protected Chart createChart(String name, String unit, Number min, Number max) {
        // Init Grid instead
        ListContainer<Message> listContainer = new ListContainer<>(Message.class);
        listContainer.setCollection(list);
        grid.setContainerDataSource(listContainer);
        grid.setWidth(SIZE_X_PX);
        grid.setHeight(SIZE_Y_PX);
        grid.setColumnOrder("time", "payload", "topic");
        grid.getColumn("time").setConverter(new StringToDateConverter() {
            @Override
            public String convertToPresentation(Date value,
                    Class<? extends String> targetType, Locale locale)
                    throws com.vaadin.data.util.converter.Converter.ConversionException {
                if (value == null) {
                    return null;
                }

                return df.format(value);
            }

        });

        // Dummy chart
        final DataSeries series = new DataSeries(name + " (" + unit + ")");
        ChartUtils cb = ChartUtils.build(null, ChartType.LINE, false)
                .size(SIZE_X_PX, SIZE_Y_025_PX)
                .series(series);
        return cb.draw();
    }

    public long size() {
        return list.size();
    }

    public void add(String topic, String payload) {
        Message msg = new Message(new Date(), topic, payload);
        ListContainer<Message> c = (ListContainer<Message>) grid.getContainerDataSource();
        c.addItemAt(0, msg);
        if (c.size() > historyLength) {
            c.removeItem(c.getIdByIndex(historyLength));
        }
    }

    public static class Message {

        private final Date time;
        private final String topic;
        private final String payload;

        private Message(Date time, String topic, String payload) {
            this.time = time;
            this.topic = topic;
            this.payload = payload;
        }

        public Date getTime() {
            return time;
        }

        public String getTopic() {
            return topic;
        }

        public String getPayload() {
            return payload;
        }

    }

}
