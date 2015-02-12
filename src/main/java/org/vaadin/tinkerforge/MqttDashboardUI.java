package org.vaadin.tinkerforge;

import org.vaadin.tinkerforge.displays.GaugeDisplay;
import com.vaadin.annotations.Push;
import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.shared.communication.PushMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import java.util.HashMap;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.vaadin.alump.masonry.MasonryLayout;

@Theme("valo")
@SuppressWarnings("serial")
@Push(value = PushMode.AUTOMATIC)
public class MqttDashboardUI extends UI {

    public static final String LOCAL_SERVER = "tcp://cdn.virit.in:1883"; //TODO: Change this
    private HashMap<Topic, MqttClient> clients = new HashMap<Topic, MqttClient>();

    @WebServlet(value = {"/*"}, asyncSupported = true)
    @VaadinServletConfiguration(productionMode = false, ui = MqttDashboardUI.class)
    public static class Servlet extends VaadinServlet {
    }

    @Override
    protected void init(VaadinRequest request) {

        VerticalLayout baseLayout = new VerticalLayout();
        baseLayout.setMargin(true);
        baseLayout.setSpacing(true);
        baseLayout.setSizeFull();
        setContent(baseLayout);

        Label title = new Label("MQTT Channels");
        title.setStyleName(ValoTheme.LABEL_H1);
        baseLayout.addComponent(title);

        MasonryLayout columnLayout = new MasonryLayout(300);
        baseLayout.addComponent(columnLayout);
        baseLayout.setComponentAlignment(columnLayout, Alignment.TOP_LEFT);
        baseLayout.setExpandRatio(columnLayout, 1f);

        // Receive all topics using a separate client
        // Hopefully clients are not too expensive objects. :)
        Topic[] topics = Topic.values();
        for (Topic topic : topics) {
            String id = "CLIENT-" + topic.name();
            GaugeDisplay gauge = new GaugeDisplay(LOCAL_SERVER, topic, id, 0, 100);
            columnLayout.addComponent(gauge);
        }

    }

}
