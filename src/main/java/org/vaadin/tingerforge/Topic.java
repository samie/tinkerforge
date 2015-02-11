/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.vaadin.tingerforge;

/**
 *
 * @author Sami Ekblad
 */
public enum Topic {

    LIGHT("TinkerForge/Wetterstation/Light"),
    TEMP("TinkerForge/Wetterstation/Temp"),
    HUMIDITY("TinkerForge/Wetterstation/Hum"),
    AIR_PRESSURE("TinkerForge/Wetterstation/Air");

    private String topicId;

    private Topic(String topicId) {
        this.topicId = topicId;
    }

    public String getTopic() {
        return this.topicId;
    }
}
