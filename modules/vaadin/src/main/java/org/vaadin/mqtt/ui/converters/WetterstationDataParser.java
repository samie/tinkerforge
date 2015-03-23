package org.vaadin.mqtt.ui.converters;

import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.vaadin.model.SensorValue;
import org.vaadin.model.converter.json.SensorValueJsonConverter;
import org.vaadin.mqtt.ui.MqttMessageConverter;
import static org.vaadin.tinkerforge.TinkerForgeDashBoardUI.CHARSET;

/**
 *
 * @author Sami Ekblad
 */
public class WetterstationDataParser implements MqttMessageConverter {

    private final SensorValueJsonConverter converter = new SensorValueJsonConverter();

    public WetterstationDataParser() {
    }

    @Override
    public void convert(org.vaadin.mqtt.ui.displays.MqttDisplay display, String id, MqttMessage message) {
        byte[] payload = message.getPayload();
        SensorValue sensorValue = converter.fromJson(new String(payload, CHARSET));
//            final LocalDateTime localDateTime = sensorValue.getLocalDateTime();
//            double v = Math.round(100.0 * sensorValue.getRawValue()) / 100.0; //TODO depends on Sensor if /10 or /100 or /1000 ..
        double v = sensorValue.getRawValue() / 100.0;
        display.updateValue(v);
    }

}
