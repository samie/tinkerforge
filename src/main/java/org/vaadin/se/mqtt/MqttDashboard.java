/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.vaadin.se.mqtt;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.vaadin.se.mqtt.displays.MqttDisplay;

/**
 * Dashboard definition linking DataSource to specific Display.
 *
 * @author Sami Ekblad
 */
public class MqttDashboard {

    private final List<MqttDisplay> displays = new ArrayList<>();

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

    /**
     * Add a display by class using url, topic and suitable message parser.
     *
     * This looks for constructor with arguments MqttDataSource and Mqtt
     *
     * @param displayClass
     * @param url
     * @param topic
     * @param parser
     * @param colors
     */
    public void add(Class<? extends MqttDisplay> displayClass, String url, MqttTopic topic, MqttMessageConverter parser, String... colors) {
        try {
            Constructor<? extends MqttDisplay> constructor = displayClass.getConstructor(MqttDataSource.class, MqttMessageConverter.class);
            MqttDisplay instance = constructor.newInstance(new MqttDataSource(url, topic), parser);
            instance.setColors(colors);
            add(instance);
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException ex) {
            Logger.getLogger(MqttDashboard.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<MqttDisplay> getDisplays() {
        return this.displays;
    }

}
