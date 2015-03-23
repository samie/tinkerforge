/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.vaadin.mqtt.ui;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.vaadin.mqtt.ui.displays.MqttDisplay;
import org.vaadin.mqtt.ui.inputs.MqttInput;

/**
 * Dashboard definition linking DataSource to specific Display.
 *
 * @author Sami Ekblad
 */
public class MqttDashboard {

    private final List<MqttComponent> displays = new ArrayList<>();

    private String title;

    private boolean addPanelVisible;

    /**
     * Get the value of addPanelVisible
     *
     * @return the value of addPanelVisible
     */
    public boolean isAddPanelVisible() {
        return addPanelVisible;
    }

    /**
     * Set the value of addPanelVisible
     *
     * @param addPanelVisible new value of addPanelVisible
     */
    public void setAddPanelVisible(boolean addPanelVisible) {
        this.addPanelVisible = addPanelVisible;
    }

    /**
     * Get the value of title
     *
     * @return the value of title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Set the value of title
     *
     * @param title new value of title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    public MqttDashboard(String title) {
    }

    public void add(MqttDisplay display) {
        displays.add(display);
    }

    public void add(MqttInput input) {
        displays.add(input);
    }

    /**
     * Add a display by class using url, topic and suitable message parser.
     *
     * This looks for constructor with arguments MqttDataSource and Mqtt
     *
     * @param displayClass
     * @param ds
     * @param converter
     * @param colors
     */
    public void add(Class<? extends MqttDisplay> displayClass, MqttDataSource ds, MqttMessageConverter converter, String... colors) {
        try {
            Constructor<? extends MqttDisplay> constructor = ((Class<MqttDisplay>) displayClass).getConstructor(MqttDataSource.class, MqttMessageConverter.class);
            MqttDisplay instance = constructor.newInstance(ds, converter);
            instance.setColors(colors);
            add(instance);

        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException ex) {
            Logger.getLogger(MqttDashboard.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Add a display by class using url, topic and suitable message parser.
     *
     * This looks for constructor with arguments MqttDataSource and Mqtt
     *
     * @param displayClass
     * @param url
     * @param topic
     * @param converter
     * @param colors
     */
    public void add(Class<? extends MqttDisplay> displayClass, String url, MqttTopic topic, MqttMessageConverter converter, String... colors) {
        add(displayClass, new MqttDataSource(url, topic), converter, colors);
    }

    public void add(Class<? extends MqttInput> inputClass, String url, MqttTopic topic) {
        add(inputClass, url, topic, null);
    }

    public void add(Class<? extends MqttInput> inputClass, String url, MqttTopic topic, String messageFormat) {
        add(inputClass, new MqttDataSource(url, topic), messageFormat);
    }

    public void add(Class<? extends MqttInput> inputClass, MqttDataSource ds, String messageFormat) {
        try {

            Constructor<? extends MqttInput> constructor = ((Class<MqttInput>) inputClass).getConstructor(MqttDataSource.class, String.class);
            MqttInput instance = constructor.newInstance(ds, messageFormat);
            add(instance);
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException ex) {
            Logger.getLogger(MqttDashboard.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<MqttComponent> getComponents() {
        return this.displays;
    }

}
