package org.vaadin.se.mqtt;

import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.vaadin.se.mqtt.displays.MqttDisplay;

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
     * @param topic
     * @param message
     */
    void convert(final MqttDisplay display, final MqttTopic topic, final MqttMessage message);

}
