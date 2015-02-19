package org.vaadin.se.mqtt.displays;

import com.vaadin.addon.charts.Chart;
import com.vaadin.addon.charts.model.DataSeries;
import com.vaadin.addon.charts.model.DataSeriesItem;
import com.vaadin.addon.charts.model.Series;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;
import com.vaadin.ui.themes.ValoTheme;
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
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.vaadin.alump.masonry.MasonryLayout;
import org.vaadin.se.mqtt.MqttDataSource;
import org.vaadin.se.mqtt.MqttMessageConverter;

/**
 * Abstract parent class for displays with single MQTT client / topic.
 *
 * @author Sami Ekblad
 */
public abstract class MqttDisplay extends CustomComponent {

    protected static final String SIZE_X_PX = "275px";
    protected static final String SIZE_Y_PX = "275px";
    protected static final String SIZE_Y_025_PX = "65px";

    protected static String COLOR_PRIMARY = "#339";
    protected static String COLOR_SECONDARY = "#EEE";
    protected static String COLOR_BACKGROUND = "#FFF";

    private MqttClient client;

    private final CssLayout layout = new CssLayout();
    private final Label title = new Label("");

    private final String STR_NOT_CONNECTED = "Not connected";
    private final String STR_WAITING_READING = "Waiting for reading...";
    private final String STR_CONNECTION_ERROR = "Connection failed to";
    private final String STR_CONNECTION_LOST = "No connection";
    private final MqttDataSource source;

    protected Chart chart;
    private MqttMessageConverter converter;
    private int historyLength;
    private String[] colors;

    MqttDisplay(MqttDataSource source, int historyLength, MqttMessageConverter converter) {
        this.source = source;
        this.historyLength = historyLength;
        this.converter = converter;
        layout.setStyleName(source.getTopic().getName().toLowerCase());
        title.setStyleName(ValoTheme.LABEL_H3);
        title.setValue(source.getTopic().getName());
        setCompositionRoot(layout);
        showUserMessage(STR_NOT_CONNECTED, true);
    }

    public MqttDataSource getSource() {
        return source;
    }

    public MqttClient getClient() {
        return client;
    }

    @Override
    public void detach() {
        super.detach();
        try {
            if (client != null) {
                client.unsubscribe(source.getTopic().getTopic());
                client.close();
            }
        } catch (MqttException ex) {
            Logger.getLogger(MqttDisplay.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void attach() {
        super.attach();

        // Lazy initialization of MQTT client
        if (client == null) {
            try {
                this.client = new MqttClient(this.source.getUrl(), this.source.getClientId(), new MemoryPersistence());
            } catch (MqttException ex) {
                Logger.getLogger(MqttDisplay.class.getName()).log(Level.SEVERE, null, ex);
                showUserMessage(STR_CONNECTION_ERROR + " id='" + this.source.getClientId() + "': " + ex.getMessage(), false);
            }
        }

        client.setCallback(new DisplayCallback());
        try {
            client.connect();
            client.subscribe(this.source.getTopic().getTopic(), 1);
            showUserMessage(STR_WAITING_READING, true);
        } catch (MqttException ex) {
            Logger.getLogger(MqttDisplay.class.getName()).log(Level.SEVERE, null, ex);
            showUserMessage(STR_CONNECTION_ERROR + " id='" + this.source.getClientId() + "': " + ex.getMessage(), false);
        }
    }

    /**
     * Show a message instead of the graph.
     *
     * @param message
     * @param isSucsess Error messages are formatted differently from success
     * messages.
     */
    public final void showUserMessage(final String message, boolean isSucsess) {
        layout.removeAllComponents();
        Label l = new Label(message);
        l.setStyleName(isSucsess ? ValoTheme.LABEL_SUCCESS : ValoTheme.LABEL_FAILURE);
        layout.addComponents(title, l);
    }

    /**
     * Show the chart.
     *
     * @param chart
     */
    public void showChart(Chart chart) {
        layout.removeAllComponents();
        layout.addComponents(title, chart);
    }

    public void messageArrived(String id, MqttMessage message) {

        if (chart == null) {
            chart = createChart(getSource().getTopic().getName(), getSource().getTopic().getUnit(), getSource().getTopic().getMin(), getSource().getTopic().getMax());
            showChart(chart);
            getParent().markAsDirty();
        }

        // Update the display values
        converter.convert(this, source.getTopic(), message);

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
        getSeries()
                .forEach(s -> IntStream.range(0, values.length)
                        .forEach(idx -> updateSeries((DataSeries) s, idx, values[idx], 0 == historyLength)));

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
            getUI().access(() -> MqttDisplay.this.connectionLost(t.getMessage()));
        }

        @Override
        public void messageArrived(String string, MqttMessage mm) throws Exception {
            getUI().access(() -> MqttDisplay.this.messageArrived(string, mm));
        }

        @Override
        public void deliveryComplete(IMqttDeliveryToken imdt) {
            getUI().access(() -> MqttDisplay.this.deliveryComplete(imdt));
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
