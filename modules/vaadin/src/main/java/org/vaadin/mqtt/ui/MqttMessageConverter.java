package org.vaadin.mqtt.ui;

import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.vaadin.mqtt.ui.displays.MqttDisplay;

/**
 * Conversion interface for MQTT Payload to value a Display chart needs.
 *
 * @author Sami Ekblad
 */
public interface MqttMessageConverter {

    /**
     * Convert the received value to presentation Display.
     *
     * @param display
     * @param id
     * @param message
     */
    void convert(final MqttDisplay display, String id, final MqttMessage message);

}
