package org.vaadin.mqtt.ui.converters;

import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.rapidpm.module.iot.tinkerforge.data.SensorDataElement;
import org.vaadin.model.SensorValue;
import org.vaadin.model.converter.json.SensorValueJsonConverter;
import org.vaadin.mqtt.ui.MqttMessageConverter;
import org.vaadin.mqtt.ui.displays.MqttDisplay;

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
    public void convert(MqttDisplay display, String id, MqttMessage message) {
        byte[] payload = message.getPayload();
        SensorDataElement sensorValue = converter.fromJson(new String(payload, CHARSET));
        double v = sensorValue.getSensorValue();
        display.updateValue(v);
    }

}
