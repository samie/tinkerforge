/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.vaadin.tingerforge;

import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * Abstract parent class for displays with single MQTT client / topic.
 *
 * @author Sami Ekblad
 */
public abstract class MqttDisplay extends CustomComponent {
    
    private final MqttClient client;
    private final Topic topic;
    
    MqttDisplay(Topic topic, MqttClient client) {
        this.client = client;
        this.topic = topic;
    }
    
    @Override
    public void detach() {
        super.detach();
        try {
            client.unsubscribe(topic.getTopic());
            client.close();
        } catch (MqttException ex) {
            Logger.getLogger(MqttDisplay.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public void attach() {
        super.attach();
        client.setCallback(new DisplayCallback());
        try {
            client.connect();
            client.subscribe(topic.getTopic(), 1);
        } catch (MqttException ex) {
            Logger.getLogger(MqttDisplay.class.getName()).log(Level.SEVERE, null, ex);
            showPermanentMessage("Connection failed with '" + client.getClientId() + "': " + ex.getMessage());
        }
    }
    
    public void showPermanentMessage(String message) {
        setCompositionRoot(new Label(message));
    }
    
    public abstract void messageArrived(String string, MqttMessage mm);
    
    public void deliveryComplete(IMqttDeliveryToken imdt) {
        
    }
    
    public void connectionLost(String message) {
        showPermanentMessage("Connection lost");
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
