/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.vaadin.tinkerforge;

/**
 * MQTT topics that can be subscribed and displayed using MqttDisplay.
 *
 * @author Sami Ekblad
 */
public enum Topic {

    LIGHT("TinkerForge/Wetterstation/Light", "Ambient Light", "Lux", 1, 1000),
    TEMP("TinkerForge/Wetterstation/Temp", "Temperature", "C", -40, +40),
    HUMIDITY("TinkerForge/Wetterstation/Hum", "Humidity", "%RH", 0, 100),
    AIR_PRESSURE("TinkerForge/Wetterstation/Air", "Air Pressure", "mBar", 500, 1500);

    private final String topicId;
    private final String name;
    private final String unit;
    private final Number min;
    private final Number max;

    private Topic(String topicId, String name, String unit, Number min, Number max) {
        this.topicId = topicId;
        this.name = name;
        this.unit = unit;
        this.min = min;
        this.max = max;
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

    public Number getMin() {
        return min;
    }

    public Number getMax() {
        return max;
    }
    
    

}
