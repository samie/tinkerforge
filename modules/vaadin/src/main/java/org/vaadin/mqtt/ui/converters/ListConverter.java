/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.vaadin.mqtt.ui.converters;

import java.nio.charset.Charset;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.vaadin.mqtt.ui.MqttComponent;
import org.vaadin.mqtt.ui.MqttMessageConverter;
import org.vaadin.mqtt.ui.displays.ListDisplay;
import org.vaadin.mqtt.ui.displays.MqttDisplay;

/**
 * Converter for ListDisplay.
 *
 * @author Sami Ekblad
 */
public class ListConverter implements MqttMessageConverter {

    @Override
    public void convert(final MqttDisplay display, final String id, final MqttMessage message) {
        if (display instanceof ListDisplay) {
            ListDisplay d = (ListDisplay) display;
            d.add(id, new String(message.getPayload(), Charset.forName(MqttComponent.MQTT_MESSAGE_CHARSET)));
        }
    }
}
