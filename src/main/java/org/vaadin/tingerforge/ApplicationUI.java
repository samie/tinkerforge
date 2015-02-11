package org.vaadin.tingerforge;

import com.vaadin.annotations.Push;
import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.shared.communication.PushMode;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

@Theme("valo")
@SuppressWarnings("serial")
@Push(value = PushMode.AUTOMATIC)
public class ApplicationUI extends UI {

    public static final String LOCAL_SERVER = "tcp://127.0.0.1:1883";
    private HashMap<Topic, MqttClient> clients = new HashMap<Topic, MqttClient>();

    @WebServlet(value = {"/*"}, asyncSupported = true)
    @VaadinServletConfiguration(productionMode = false, ui = ApplicationUI.class)
    public static class Servlet extends VaadinServlet {
    }

    @Override
    protected void init(VaadinRequest request) {
        final VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);
        layout.setSpacing(true);
        setContent(layout);
        layout.addComponent(new Label("MQTT Channels"));

        // Receive all topics using a separate client
        // Hopefully clients are not expensive. :)
        Topic[] topics = Topic.values();
        for (Topic topic : topics) {
            try {
                MqttClient client = new MqttClient(LOCAL_SERVER, "CLIENT-" + topic.name(), new MemoryPersistence());
                layout.addComponent(new GaugeDisplay(topic, client, 0, 100));
                
            } catch (MqttException ex) {
                Logger.getLogger(ApplicationUI.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }

}
