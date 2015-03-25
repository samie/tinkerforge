/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.vaadin.mqtt.ui;

import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;
import com.vaadin.ui.themes.ValoTheme;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.vaadin.mqtt.ui.displays.MqttDisplay;
import org.vaadin.tinkerforge.modules.communication.mqtt.MqttClientBuilder;

/**
 * A MQTT UI component to be presented in dashboard.
 *
 * @author Sami Ekblad
 */
public abstract class MqttComponent extends CustomComponent {

    /* Available content sizes */
    protected static final String SIZE_X_PX = "265px";
    protected static final String SIZE_Y_PX = "265px";
    protected static final String SIZE_Y_025_PX = "60px";
    public static final String MQTT_MESSAGE_CHARSET = "UTF-8";

    protected final String STR_NOT_CONNECTED = "Not connected";
    protected final String STR_WAITING_READING = "Waiting for reading...";
    protected final String STR_CONNECTION_ERROR = "Connection failed to";
    protected final String STR_CONNECTION_LOST = "No connection";

    private final CssLayout layout = new CssLayout();
    private final Label title = new Label("");

    private final MqttDataSource source;
    private MqttClient client;

    public MqttComponent(MqttDataSource source, String title) {
        this.source = source;
        this.title.setStyleName(ValoTheme.LABEL_H3);
        this.title.setValue(title);
        setCompositionRoot(layout);
        showUserMessage(STR_NOT_CONNECTED, true);
    }

    public MqttDataSource getSource() {
        return source;
    }

    public MqttClient getClient() {
        return client;
    }

    protected void removeAllComponents() {
        layout.removeAllComponents();
        addComponents(title);
    }

    protected void addComponents(Component... components) {
        layout.addComponents(components);
    }

    /**
     * Show a message instead of the content.
     *
     * @param message
     * @param isSucsess Error messages are formatted differently from success
     * messages.
     */
    public final void showUserMessage(final String message, boolean isSucsess) {
        removeAllComponents();
        Label l = new Label(message);
        l.setStyleName(isSucsess ? ValoTheme.LABEL_SUCCESS : ValoTheme.LABEL_FAILURE);
        addComponents(title, l);
    }

    @Override
    public void attach() {
        super.attach();
        // Lazy initialization of MQTT client
        if (client == null) {
            try {
                this.client = MqttClientBuilder.builder()
                        .uri(this.source.getUrl())
                        .clientUIDGenerated()
                        .memoryPersistence(true)
                        .build();
//                this.client = new MqttClient(this.source.getUrl(), this.source.getClientId(), new MemoryPersistence());
            } catch (Exception ex) {
                Logger.getLogger(MqttDisplay.class.getName()).log(Level.SEVERE, null, ex);
                showUserMessage(STR_CONNECTION_ERROR + this.source.getUrl() +" clientId='" + this.source.getClientId() + "': " + ex.getMessage(), false);
            }
        }
        connect();
    }

    @Override
    public void detach() {
        disconnect();
        super.detach();
    }

    /**
     * Initialize MQTT UI when shown.
     *
     */
    public abstract void connect();

    /**
     * Disconnect MQTT UI when removed.
     *
     */
    public abstract void disconnect();
}
