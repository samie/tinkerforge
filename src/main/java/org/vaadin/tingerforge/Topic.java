/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.vaadin.tingerforge;

/**
 * MQTT topics that can be subscribed and displayed using MqttDisplay.
 *
 * @author Sami Ekblad
 */
public enum Topic {

    LIGHT("TinkerForge/Wetterstation/Light", "Ambient Light", "Lux"),
    TEMP("TinkerForge/Wetterstation/Temp", "Temperature", "C"),
    HUMIDITY("TinkerForge/Wetterstation/Hum", "Humidity", "%RH"),
    AIR_PRESSURE("TinkerForge/Wetterstation/Air", "Air Pressure", "mBar");

    private final String topicId;
    private final String name;
    private final String unit;

    private Topic(String topicId, String name, String unit) {
        this.topicId = topicId;
        this.name = name;
        this.unit = unit;
    }

    public String getTopic() {
        return this.topicId;
    }

    public String getName() {
        return this.name;
    }

    public String getUnit() {
        return this.unit;
    }

}
