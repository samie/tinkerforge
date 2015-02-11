/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.vaadin.tingerforge;

import com.vaadin.addon.charts.Chart;
import com.vaadin.addon.charts.model.ChartType;
import com.vaadin.addon.charts.model.Configuration;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * Gauge display for MQTT client.
 *
 * @author Sami Ekblad
 */
public class GaugeDisplay extends MqttDisplay {

    Chart chart = createGaugeChart();

    public GaugeDisplay(Topic topic, MqttClient client, Number min, Number max) {
        super(topic, client);

        setCompositionRoot(chart);
    }

    @Override
    public void messageArrived(String string, MqttMessage mm) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private Chart createGaugeChart() {
        Chart chrt = new Chart(ChartType.GAUGE);
        chrt.setSizeFull();
        Configuration c = chrt.getConfiguration();

        return chrt;
    }

}
