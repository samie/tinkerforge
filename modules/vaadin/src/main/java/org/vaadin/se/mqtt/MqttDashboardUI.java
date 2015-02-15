package org.vaadin.se.mqtt;

import com.vaadin.annotations.Push;

import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.communication.PushMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import org.vaadin.alump.masonry.MasonryLayout;
import org.vaadin.se.mqtt.displays.MqttDisplay;

@Theme("valo")
@SuppressWarnings("serial")
@Push(value = PushMode.AUTOMATIC)
public abstract class MqttDashboardUI extends UI {

    private Label title;
    private MasonryLayout columnLayout;

    @Override
    protected void init(VaadinRequest request) {

        VerticalLayout baseLayout = new VerticalLayout();
        baseLayout.setMargin(true);
        baseLayout.setSpacing(true);
        baseLayout.setSizeFull();
        setContent(baseLayout);

        title = new Label("MQTT Data Sources");
        title.setStyleName(ValoTheme.LABEL_H1);
        baseLayout.addComponent(title);

        columnLayout = new MasonryLayout(300);
        baseLayout.addComponent(columnLayout);
        baseLayout.setComponentAlignment(columnLayout, Alignment.TOP_LEFT);
        baseLayout.setExpandRatio(columnLayout, 1f);

        MqttDashboard dash = getDashboardSpec();
        title.setValue(dash.getTitle());
        for (MqttDisplay display : dash.getDisplays()) {
            columnLayout.addComponent(display);
        }

    }

    /**
     * Get a dashboard specification.
     *
     * @return
     */
    public abstract MqttDashboard getDashboardSpec();

}
