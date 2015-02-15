package org.vaadin.se.mqtt;

import com.vaadin.addon.charts.model.AbstractSeries;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * Conversion interface for MQTT Payload to value a Display chart needs.
 *
 * @author Sami Ekblad
 */
public interface MqttMessageParser {

    void convert(AbstractSeries series, final MqttTopic topic, final MqttMessage message);

}
