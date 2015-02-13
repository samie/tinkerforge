package org.vaadin.tingerforge;

import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.shared.communication.PushMode;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.vaadin.tinkerforge.Topic;
import org.vaadin.tinkerforge.displays.GaugeDisplay;

import javax.servlet.annotation.WebServlet;
import java.util.HashMap;

@Theme("valo")
@SuppressWarnings("serial")
@Push(value = PushMode.AUTOMATIC)
public class MqttDashboardUI extends UI {

    public static final String LOCAL_SERVER = "tcp://127.0.0.1:1883";
    private HashMap<Topic, MqttClient> clients = new HashMap<>();

    @WebServlet(value = {"/*"}, asyncSupported = true)
    @VaadinServletConfiguration(productionMode = false, ui = MqttDashboardUI.class)
    public static class Servlet extends VaadinServlet {
    }

    @Override
    protected void init(VaadinRequest request) {
        final VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);
        layout.setSpacing(true);
        setContent(layout);
        Label title = new Label("MQTT Channels");
        title.setStyleName(ValoTheme.LABEL_H1);
        layout.addComponent(title);

        // Receive all topics using a separate client
        // Hopefully clients are not too expensive objects. :)
        Topic[] topics = Topic.values();
        for (Topic topic : topics) {
            String id = "CLIENT-" + topic.name();
            GaugeDisplay gauge = new GaugeDisplay(LOCAL_SERVER, topic, id, 0, 100);
            layout.addComponent(gauge);

        }

    }

}
