package org.vaadin.tinkerforge;

import java.nio.charset.Charset;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.vaadin.se.mqtt.MqttDashboard;
import org.vaadin.se.mqtt.MqttDashboardUI;
import org.vaadin.se.mqtt.MqttMessageConverter;
import org.vaadin.se.mqtt.MqttTopic;
import org.vaadin.se.mqtt.displays.GaugeDisplay;
import org.vaadin.se.mqtt.displays.BarGaugeDisplay;
import org.vaadin.se.mqtt.displays.MqttDisplay;
import org.vaadin.se.mqtt.displays.SparklineDisplay;

/**
 * Specialized MQTT Dashboard for TinkerForge sensors.
 *
 * @author Sami Ekblad
 */
public class TinkerForgeDashBoardUI extends MqttDashboardUI {

    private static final String MQTT_BROKER = "tcp://mqtt.virit.in:1883";

    protected static final String MQTT_MESSAGE_CHARSET = "UTF-8";

    // Messages published by the TinkerFoge Wetterstation
    private static final MqttTopic LIGHT = new MqttTopic("TinkerForge/Wetterstation/Light", "Ambient Light", "Lux", 1, 1000);
    private static final MqttTopic TEMP = new MqttTopic("TinkerForge/Wetterstation/Temp", "Temperature", "C", -40, +40);
    private static final MqttTopic HUMIDITY = new MqttTopic("TinkerForge/Wetterstation/Hum", "Humidity", "%RH", 0, 100);
    private static final MqttTopic AIR_PRESSURE = new MqttTopic("TinkerForge/Wetterstation/Air", "Air Pressure", "mBar", 500, 1500);

    // Special data parsers for the above messages
    private WetterstationDataParser CONVERTER = new WetterstationDataParser(3);

    // Dashboard specification
    private final MqttDashboard dashboardSpec = new MqttDashboard("TinkerForge Wetterstation") {
        {
            add(GaugeDisplay.class, MQTT_BROKER, LIGHT, CONVERTER, "#BBBBBB", "#FF9900","#FFFF00");
            add(SparklineDisplay.class, MQTT_BROKER, LIGHT, CONVERTER, "#0066FF");
            add(BarGaugeDisplay.class, MQTT_BROKER, TEMP, CONVERTER, "#000099", "#66CCFF", "#FF3300");
            add(SparklineDisplay.class, MQTT_BROKER, TEMP, CONVERTER, "#000099");
            add(BarGaugeDisplay.class, MQTT_BROKER, HUMIDITY, CONVERTER, "#000099", "#66CCFF");
            add(SparklineDisplay.class, MQTT_BROKER, AIR_PRESSURE, CONVERTER,"#000099", "#66CCFF");
        }

    };

    @Override
    public MqttDashboard getDashboardSpec() {
        return this.dashboardSpec;
    }

    /**
     * Convert MQTT messages to Chart values.
     *
     */
    public static class WetterstationDataParser implements MqttMessageConverter {

        private final int[] fields;

        public WetterstationDataParser(int... messageField) {
            this.fields = messageField;
        }

        @Override
        public void convert(final MqttDisplay display, final MqttTopic topic, final MqttMessage message) {
            String[] splitted = payloadToString(message);
            if (splitted == null) {
                return; // Got nothing
            }

            // Parse a
            Number[] numbers = new Number[splitted.length];
            for (int i = 0; i < splitted.length; i++) {
                try {
                    numbers[i] = Double.parseDouble(splitted[i]);
                } catch (Exception e) {
                    System.err.println("Failed to parse message field=" + i + ", topic=" + topic.getTopic());
                }
            }

            // Pick only requested fields
            Number[] values = new Number[fields.length];
            for (int i = 0; i < fields.length; i++) {
                if (fields[i] >= values.length && fields[i] < 0) {
                    System.err.println("Missing field in received message field=" + fields[i] + ", topic=" + topic.getTopic());
                    return;
                } else {
                    values[i] = numbers[fields[i]];
                }
            }

            // Update the display char data
            display.updateValue(values);

        }
    }

    private static String[] payloadToString(MqttMessage mm) {
        byte[] payload = mm.getPayload();
        try {
            return new String(payload, Charset.forName(MQTT_MESSAGE_CHARSET)).split(":");
        } catch (Exception e) {
        }
        return null;
    }

}
