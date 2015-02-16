/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.vaadin.se.mqtt.displays;

import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.VerticalLayout;
import java.util.Arrays;
import java.util.List;
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.fields.TypedSelect;

/**
 * Special Display to add other displays.
 *
 * @author Sami Ekblad
 */
public class AddNewDataSource extends CustomComponent {

    List<Class<? extends MqttDisplay>> displayTypes = Arrays.asList(BarGaugeDisplay.class,
            GaugeDisplay.class,
            SparklineDisplay.class,
            PolarDisplay.class);
    VerticalLayout baseLayout = new VerticalLayout();
    MTextField url = new MTextField("MQTT server URL", "tcp://mqtt.virit.in:1883");
    MTextField topic = new MTextField("Message topic", "TinkerForge/Wetterstation/Light");
    MTextField name = new MTextField("Title","Ambient light");
    MTextField unit = new MTextField("Unit","Lux");
    MTextField min = new MTextField("Min value","0");
    MTextField max = new MTextField("Max value", "1600");

    public AddNewDataSource() {
        TypedSelect<Class<? extends MqttDisplay>> type = new TypedSelect<>();
        type.setBeans(displayTypes);
        baseLayout.addComponent(url);
        baseLayout.addComponent(topic);
        baseLayout.addComponent(name);
        baseLayout.addComponent(unit);
        baseLayout.addComponent(min);
        baseLayout.addComponent(max);
    }

}
