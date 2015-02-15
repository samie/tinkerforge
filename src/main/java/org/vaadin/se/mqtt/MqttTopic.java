package org.vaadin.se.mqtt;

/**
 * MQTT topics that can be subscribed and displayed using MqttDisplay.
 *
 * @author Sami Ekblad
 */
public class MqttTopic {

    private final String topicId;
    private final String name;
    private final String unit;
    private final Number min;
    private final Number max;

    public MqttTopic(String topicId, String name, String unit, Number min, Number max) {
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
