package org.vaadin.tinkerforge;

import java.nio.charset.Charset;
import org.vaadin.mqtt.ui.MqttDashboard;
import org.vaadin.mqtt.ui.MqttDashboardUI;
import org.vaadin.mqtt.ui.MqttTopic;

import org.vaadin.mqtt.ui.converters.WetterstationDataParser;
import org.vaadin.mqtt.ui.displays.GaugeDisplay;
import org.vaadin.mqtt.ui.displays.BarGaugeDisplay;
import org.vaadin.mqtt.ui.displays.SparklineDisplay;

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
}
