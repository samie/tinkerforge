package org.vaadin.tinkerforge;

import com.vaadin.addon.charts.model.AbstractSeries;
import com.vaadin.addon.charts.model.DataSeries;
import com.vaadin.addon.charts.model.DataSeriesItem;
import com.vaadin.addon.charts.model.ListSeries;
import java.nio.charset.Charset;
import java.util.Date;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.vaadin.se.mqtt.MqttDashboard;
import org.vaadin.se.mqtt.MqttDashboardUI;
import org.vaadin.se.mqtt.MqttMessageParser;
import org.vaadin.se.mqtt.MqttTopic;
import org.vaadin.se.mqtt.displays.GaugeDisplay;
import org.vaadin.se.mqtt.displays.BarGaugeDisplay;
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
    private WetterstationDataParser singleValue = new WetterstationDataParser(0, 3);
    private WetterstationDataParser historyValues = new WetterstationDataParser(30, 3);

    // Dashboard specification
    private final MqttDashboard dashboardSpec = new MqttDashboard("TinkerForge Wetterstation") {
        {
            add(GaugeDisplay.class, MQTT_BROKER, LIGHT, singleValue);
            add(SparklineDisplay.class, MQTT_BROKER, LIGHT, historyValues);
            add(BarGaugeDisplay.class, MQTT_BROKER, TEMP, singleValue);
            add(BarGaugeDisplay.class, MQTT_BROKER, HUMIDITY, singleValue);
            add(SparklineDisplay.class, MQTT_BROKER, AIR_PRESSURE, historyValues);
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
    public static class WetterstationDataParser implements MqttMessageParser {

        private final int[] fields;
        private final int historySize;

        public WetterstationDataParser(int historySize, int... messageField) {
            this.historySize = historySize;
            this.fields = messageField;
        }

        @Override
        public void convert(AbstractSeries series, MqttTopic topic, MqttMessage message) {
            String[] splitted = payloadToString(message);
            if (splitted == null) {
                return;
            }

            double[] values = new double[splitted.length];
            for (int i = 0; i < splitted.length; i++) {
                try {
                    values[i] = Double.parseDouble(splitted[i]);
                } catch (Exception e) {
                    System.err.println("Failed to parse message field=" + i + ", topic=" + topic.getTopic());
                }
            }
            for (int i = 0; i < fields.length; i++) {
                DataSeries s = (DataSeries) series;
                if (fields[i] >= values.length && fields[i] < 0) {
                    System.err.println("Missing field in received message field=" + fields[i] + ", topic=" + topic.getTopic());
                    return;
                }

                if (historySize == 0) {
                    // Animated update of single value
                    ListSeries l;
                    if (s.getData().size() == 1) {
                        DataSeriesItem item = s.get(0);
                        item.setY(values[fields[i]]);
                        s.update(item);
                        System.err.println("update value=" + values[fields[i]] + ", topic=" + topic.getTopic());
                    } else {
                        DataSeriesItem dataItem = new DataSeriesItem(0, values[fields[i]]);
                        s.add(dataItem);
                        System.err.println("add value=" + values[fields[i]] + ", topic=" + topic.getTopic());

                    }
                } else {
                    // add to dataset and rotate if needed
                    System.err.println("history value=" + values[fields[i]] + ", topic=" + topic.getTopic());
                    DataSeriesItem dataItem = new DataSeriesItem(new Date(), values[fields[i]]);
                    s.add(dataItem, true, s.getData().size() > historySize);
                }
            }
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
