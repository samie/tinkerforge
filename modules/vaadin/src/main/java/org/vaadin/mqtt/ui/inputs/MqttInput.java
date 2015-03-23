/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.vaadin.mqtt.ui.inputs;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.vaadin.mqtt.ui.MqttComponent;
import org.vaadin.mqtt.ui.MqttDataSource;

/**
 * Base class for MQTT input components.
 *
 * @author Sami Ekblad
 */
public abstract class MqttInput extends MqttComponent {

    public MqttInput(MqttDataSource source, String title) {
        super(source, title);
    }

    @Override
    public void connect() {
        try {
            MqttClient client = getClient();
            if (client != null) {
                if (getSource().getOptions() != null) {
                    client.connect(getSource().getOptions());
                } else {
                    client.connect();
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(SliderInput.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void disconnect() {
        if (getClient() != null) {
            try {
                MqttClient c = getClient();
                if (c != null) {
                    c.disconnect();
                }
            } catch (Exception ex) {
                Logger.getLogger(SliderInput.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
