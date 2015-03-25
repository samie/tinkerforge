package org.vaadin.mqtt.ui;

import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.vaadin.tinkerforge.modules.communication.mqtt.MqttTopic;

/**
 * Data source specifying the server, MQTT topic.
 *
 * @author Sami Ekblad
 */
public class MqttDataSource {

    private MqttTopic topic;
    private String url;
    private static long counter;
    private MqttConnectOptions options;

    public MqttDataSource(String url, String clientId, MqttConnectOptions opts, MqttTopic topic) {
        this.topic = topic;
        this.url = url;
        this.options = opts;
        this.clientId = clientId;
    }

    public MqttDataSource(String url, String clientId, MqttTopic topic) {
        this(url, clientId, null, topic);
    }

    /**
     * Creates new data source with generated id.
     *
     * @param url
     * @param topic
     */
    public MqttDataSource(String url, MqttTopic topic) {
        this(url, "" + topic.hashCode() + "-" + (counter++), topic);
    }

    /**
     * Get the value of url
     *
     * @return the value of url
     */
    public String getUrl() {
        return url;
    }

    /**
     * Set the value of url
     *
     * @param url new value of url
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * Get the value of topic
     *
     * @return the value of topic
     */
    public MqttTopic getTopic() {
        return topic;
    }

    /**
     * Set the value of topic
     *
     * @param topic new value of topic
     */
    public void setTopic(MqttTopic topic) {
        this.topic = topic;
    }

    private String clientId;

    /**
     * Get the value of clientId
     *
     * @return the value of clientId
     */
    public String getClientId() {
        return clientId;
    }

    /**
     * Set the value of clientId
     *
     * @param clientId new value of clientId
     */
    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public MqttConnectOptions getOptions() {
        return options;
    }

    public void setOptions(MqttConnectOptions options) {
        this.options = options;
    }

}
