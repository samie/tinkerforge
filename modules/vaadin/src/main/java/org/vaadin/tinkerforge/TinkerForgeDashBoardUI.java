package org.vaadin.tinkerforge;

import java.nio.charset.Charset;

import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.vaadin.model.SensorValue;
import org.vaadin.model.converter.json.SensorValueJsonConverter;
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
    public static final Charset CHARSET = Charset.forName(MQTT_MESSAGE_CHARSET);

    // Messages published by the TinkerFoge Wetterstation
    private static final MqttTopic LIGHT = new MqttTopic("TinkerForge/Wetterstation/Light", "Ambient Light", "Lux", 1, 1000);
    private static final MqttTopic TEMP = new MqttTopic("TinkerForge/Wetterstation/Temp", "Temperature", "C", -40, +40);
    private static final MqttTopic HUMIDITY = new MqttTopic("TinkerForge/Wetterstation/Hum", "Humidity", "%RH", 0, 100);
    private static final MqttTopic AIR_PRESSURE = new MqttTopic("TinkerForge/Wetterstation/Air", "Air Pressure", "mBar", 500, 1500);

    // Special data parsers for the above messages
    private WetterstationDataParser CONVERTER = new WetterstationDataParser();

    // Dashboard specification
    private final MqttDashboard dashboardSpec = new MqttDashboard("TinkerForge Wetterstation") {
        {
            add(GaugeDisplay.class, MQTT_BROKER, LIGHT, CONVERTER, "#BBBBBB", "#FF9900", "#FFFF00");
            add(SparklineDisplay.class, MQTT_BROKER, LIGHT, CONVERTER, "#0066FF");
            add(BarGaugeDisplay.class, MQTT_BROKER, TEMP, CONVERTER, "#000099", "#66CCFF", "#FF3300");
            add(SparklineDisplay.class, MQTT_BROKER, TEMP, CONVERTER, "#000099");
            add(BarGaugeDisplay.class, MQTT_BROKER, HUMIDITY, CONVERTER, "#000099", "#66CCFF");
            add(SparklineDisplay.class, MQTT_BROKER, AIR_PRESSURE, CONVERTER, "#000099", "#66CCFF");
        }

    };

    @Override
    public MqttDashboard getDashboardSpec() {
        return this.dashboardSpec;
    }

    /**
     * Convert MQTT messages to Chart values.
     */
    public static class WetterstationDataParser implements MqttMessageConverter {

        private final SensorValueJsonConverter converter = new SensorValueJsonConverter();

        public WetterstationDataParser() { /** variable ?? **/ }

        @Override
        public void convert(final MqttDisplay display, final MqttTopic topic, final MqttMessage message) {
            byte[] payload = message.getPayload();
            SensorValue sensorValue = converter.fromJson(new String(payload, CHARSET));
//            final LocalDateTime localDateTime = sensorValue.getLocalDateTime();
//            double v = Math.round(100.0 * sensorValue.getRawValue()) / 100.0; //TODO depends on Sensor if /10 or /100 or /1000 ..
            double v = sensorValue.getRawValue() / 100.0;
            display.updateValue(v);
        }
    }

}
