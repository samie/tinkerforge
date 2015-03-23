package org.vaadin.mqtt.ui;

/**
 * MQTT topics that can be subscribed and displayed using MqttDisplay.
 *
 * @author Sami Ekblad
 */
public class MqttTopic {

    private final String topicId;
    private final String title;
    private final String unit;
    private final Number min;
    private final Number max;

    public MqttTopic(String topicId, String title, String unit, Number min, Number max) {
        this.topicId = topicId;
        this.title = title;
        this.unit = unit;
        this.min = min;
        this.max = max;
    }

    public MqttTopic(String topicId, String title) {
        this.topicId = topicId;
        this.title = title;
        this.unit = "";
        this.min = null;
        this.max = null;
    }

    public String getId() {
        return this.topicId;
    }

    public String getTitle() {
        return this.title;
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
