package org.vaadin.mqtt.ui.displays;

import com.vaadin.addon.charts.Chart;
import com.vaadin.addon.charts.model.DataSeries;
import com.vaadin.addon.charts.model.DataSeriesItem;
import com.vaadin.addon.charts.model.Series;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.IntStream;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.vaadin.alump.masonry.MasonryLayout;
import org.vaadin.mqtt.ui.MqttDataSource;
import org.vaadin.mqtt.ui.MqttMessageConverter;
import org.vaadin.mqtt.ui.MqttComponent;

/**
 * Abstract parent class for displays with single MQTT client / topic.
 *
 * @author Sami Ekblad
 */
public abstract class MqttDisplay extends MqttComponent {

    protected static String COLOR_PRIMARY = "#339";
    protected static String COLOR_SECONDARY = "#EEE";
    protected static String COLOR_BACKGROUND = "#FFF";

    protected Chart chart;
    private MqttMessageConverter converter;
    int historyLength;
    private String[] colors;

    MqttDisplay(MqttDataSource source, int historyLength, MqttMessageConverter converter) {
        super(source, source.getTopic().getTitle());
        this.historyLength = historyLength;
        this.converter = converter;
    }

    @Override
    public void connect() {
        MqttClient client = getClient();
        client.setCallback(new DisplayCallback());
        try {
            if (getSource().getOptions() != null) {
                client.connect(getSource().getOptions());
            } else {
                client.connect();
            }
            client.subscribe(this.getSource().getTopic().getId(), 1);
            showUserMessage(STR_WAITING_READING, true);
        } catch (MqttException ex) {
            Logger.getLogger(MqttDisplay.class.getName()).log(Level.SEVERE, null, ex);
            showUserMessage(STR_CONNECTION_ERROR + " id='" + this.getSource().getClientId() + "': " + ex.getMessage(), false);
        }
    }

    @Override
    public void disconnect() {
        try {
            MqttClient client = getClient();
            if (client != null) {
                client.unsubscribe(getSource().getTopic().getId());
                client.close();
            }
        } catch (MqttException ex) {
            Logger.getLogger(MqttDisplay.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Show the chart.
     *
     */
    public void showChart() {
        removeAllComponents();
        addComponents(chart);
    }

    public void messageArrived(String id, MqttMessage message) {

        if (chart == null) {
            chart = createChart(getSource().getTopic().getTitle(), getSource().getTopic().getUnit(), getSource().getTopic().getMin(), getSource().getTopic().getMax());
            showChart();
            ((MasonryLayout) getParent()).markAsDirty();
        }

        // Update the display values
        converter.convert(this, id, message);

    }

    public List<Series> getSeries() {
        return chart.getConfiguration().getSeries();
    }

    public void deliveryComplete(IMqttDeliveryToken imdt) {
        // Not needed
    }

    public void connectionLost(String message) {
        showUserMessage(STR_CONNECTION_LOST, false);
    }

    /**
     * Create a suitable chart for this display.
     *
     * @param name
     * @param unit
     * @param min
     * @param max
     * @return
     */
    protected abstract Chart createChart(String name, String unit, Number min, Number max);

    /**
     * Updates all series with the given values.
     *
     * @param values
     */
    public void updateValue(Number... values) {
        getSeries().forEach(s -> {
            IntStream.range(0, values.length).forEach(idx
                    -> updateSeries((DataSeries) s, idx, values[idx], 0 == historyLength));
        });
    }

    /**
     * Updates the new value in the given series.
     *
     * @param series
     * @param index
     * @param newValue
     * @param singleValueDisplay
     */
    protected void updateSeries(DataSeries series, int index, Number newValue, boolean singleValueDisplay) {
        if (singleValueDisplay) {
            // Animated update of single value
            if (series.getData().size() == 1) {
                DataSeriesItem item = series.get(index);
                item.setY(newValue);
                series.update(item);
            } else {
                DataSeriesItem dataItem = new DataSeriesItem(index, newValue);
                series.add(dataItem);
            }
        } else {
            // add to dataset and rotate if needed
            DataSeriesItem dataItem = new DataSeriesItem(new Date(), newValue);
            series.add(dataItem, true, series.getData().size() > historyLength);
        }
    }

    public void setColors(String... colors) {
        this.colors = colors;
    }

    public String[] getColors() {
        return this.colors;
    }

    /* MQTT callback for wiring MQTT events to UI updates.    
     */
    public class DisplayCallback implements MqttCallback {

        @Override
        public void connectionLost(Throwable t) {
            getUI().access(() -> {
                MqttDisplay.this.connectionLost(t.getMessage());
            });
        }

        @Override
        public void messageArrived(String string, MqttMessage mm) throws Exception {
            getUI().access(() -> {
                MqttDisplay.this.messageArrived(string, mm);
            });
        }

        @Override
        public void deliveryComplete(IMqttDeliveryToken imdt) {
            getUI().access(() -> {
                MqttDisplay.this.deliveryComplete(imdt);
            });
        }
    }

    /**
     * Get the value of converter
     *
     * @return the value of converter
     */
    public MqttMessageConverter getConverter() {
        return converter;
    }

    /**
     * Set the value of converter
     *
     * @param converter new value of converter
     */
    public void setConverter(MqttMessageConverter converter) {
        this.converter = converter;
    }
}
