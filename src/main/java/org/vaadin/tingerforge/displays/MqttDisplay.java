/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.vaadin.tingerforge.displays;

import com.vaadin.addon.charts.Chart;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;
import com.vaadin.ui.themes.ValoTheme;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.vaadin.tingerforge.Topic;

/**
 * Abstract parent class for displays with single MQTT client / topic.
 *
 * @author Sami Ekblad
 */
public abstract class MqttDisplay extends CustomComponent {

    protected String MQTT_CHARSET = "UTF-8";
    protected static String COLOR_PRIMARY = "#339";
    protected static String COLOR_SECONDARY = "#DDD";

    private MqttClient client;
    private final Topic topic;

    private final CssLayout layout = new CssLayout();
    private final Label title = new Label("");

    private final String STR_NOT_CONNECTED = "Not connected";
    private final String STR_WAITING_READING = "Waiting for reading...";
    private final String STR_CONNECTION_ERROR = "Connection failed to";
    private final String STR_CONNECTION_LOST = "No connection";
    private final String clientId;
    private final String serverUrl;

    MqttDisplay(String serverUrl, String id, Topic topic) {
        this.serverUrl = serverUrl;
        this.clientId = id;
        this.topic = topic;
        layout.setStyleName(topic.name().toLowerCase());
        title.setStyleName(ValoTheme.LABEL_H3);
        setCompositionRoot(layout);
        showUserMessage(STR_NOT_CONNECTED, true);
    }

    public Topic getTopic() {
        return topic;
    }

    public MqttClient getClient() {
        return client;
    }

    @Override
    public void detach() {
        super.detach();
        try {
            if (client != null) {
                client.unsubscribe(topic.getTopic());
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
                this.client = new MqttClient(this.serverUrl, this.clientId, new MemoryPersistence());
            } catch (MqttException ex) {
                Logger.getLogger(MqttDisplay.class.getName()).log(Level.SEVERE, null, ex);
                showUserMessage(STR_CONNECTION_ERROR + " id='" + clientId + "': " + ex.getMessage(), false);
            }
        }

        client.setCallback(new DisplayCallback());
        try {
            client.connect();
            client.subscribe(topic.getTopic(), 1);
            showUserMessage(STR_WAITING_READING, true);
        } catch (MqttException ex) {
            Logger.getLogger(MqttDisplay.class.getName()).log(Level.SEVERE, null, ex);
            showUserMessage(STR_CONNECTION_ERROR + " id='" + clientId + "': " + ex.getMessage(), false);
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

    public abstract void messageArrived(String string, MqttMessage mm);

    public void deliveryComplete(IMqttDeliveryToken imdt) {
        // Not needed?
    }

    public void connectionLost(String message) {
        showUserMessage(STR_CONNECTION_LOST, false);
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
}
