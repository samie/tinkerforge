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
import org.vaadin.mqtt.ui.displays.MqttDisplay;

/**
 * Convert a double value in MQTT message to Chart value.
 *
 */
public class DoubleConverter implements MqttMessageConverter {

    private String separator;
    private int[] fields;

    public DoubleConverter() {
        this.separator = null;
        this.fields = null;
    }

    public DoubleConverter(String separator, int... messageField) {
        this.separator = separator;
        this.fields = messageField;
    }

    @Override
    public void convert(final MqttDisplay display, final String id, final MqttMessage message) {
        String stringValue = new String(message.getPayload(), Charset.forName(MqttComponent.MQTT_MESSAGE_CHARSET));
        if (fields == null) {
            // Only value in message
            double value = Double.parseDouble(stringValue);
            display.updateValue(value);
        } else if (separator != null && fields != null) {
            // Get the speciefied double values
            String[] splitted;
            try {
                splitted = stringValue.split(separator);
            } catch (Exception e) {
                System.err.println("Failed to split message. Separator='" + separator + "', topic=" + id + ", payload='" + stringValue + "'");
                splitted = null;
            }

            if (splitted == null) {
                return; // Got nothing meaningful                
            }

            // Pick & parse only the requested fields
            Number[] values = new Number[fields.length];
            for (int i = 0; i < fields.length; i++) {
                if (fields[i] >= values.length && fields[i] < 0) {
                    System.err.println("Skipping a missing field in received message field=" + fields[i] + ", topic=" + id);
                } else {
                    try {
                        values[i] = Double.parseDouble(splitted[fields[i]]);
                    } catch (Exception e) {
                        System.err.println("Failed to parse message field=" + i + ", topic=" + id + ", payload='" + stringValue + "'");
                    }
                }
            }

            // Update the display char data
            display.updateValue(values);

        }

    }

}
