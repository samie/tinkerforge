/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.vaadin.mqtt.ui.inputs;

import com.vaadin.data.Property;
import com.vaadin.ui.Slider;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.vaadin.mqtt.ui.MqttDataSource;
import org.vaadin.mqtt.ui.MqttTopic;

/**
 *
 * @author Sami Ekblad
 */
public class SliderInput extends MqttInput {

    private static final int DEFAULT_QOS = 0;
    private static final String VALUE_PLACEHOLDER = "${VALUE}";
    private final Slider slider;
    private final MqttTopic topic;
    private final String messageFormat;

    public SliderInput(MqttDataSource source, String messageformat) {
        super(source, source.getTopic().getTitle());
        this.messageFormat = messageformat != null ? messageformat : VALUE_PLACEHOLDER;
        this.topic = source.getTopic();
        double min = topic.getMin().doubleValue();
        double max = topic.getMax().doubleValue();
        slider = new Slider();
        slider.setWidth(SIZE_X_PX);
        slider.setMax(max);
        slider.setMin(min);
        slider.setResolution(1);
        slider.addValueChangeListener(new ValueListener());
    }

    @Override
    public void connect() {
        super.connect();
        removeAllComponents();
        addComponents(slider);
    }

    private class ValueListener implements Property.ValueChangeListener {

        public ValueListener() {
        }

        @Override
        public void valueChange(Property.ValueChangeEvent event) {
            MqttMessage msg = new MqttMessage((SliderInput.this.messageFormat.replace(VALUE_PLACEHOLDER, "" + slider.getValue())).getBytes());
            msg.setQos(DEFAULT_QOS);
            try {
                getClient().publish(topic.getId(), msg);
            } catch (Exception ex) {
                Logger.getLogger(SliderInput.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
