/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.vaadin.tingerforge.displays;

import com.vaadin.addon.charts.Chart;
import com.vaadin.addon.charts.model.ChartType;
import com.vaadin.addon.charts.model.Configuration;
import com.vaadin.addon.charts.model.Labels;
import com.vaadin.addon.charts.model.ListSeries;
import com.vaadin.addon.charts.model.PlotOptionsGauge;
import com.vaadin.addon.charts.model.YAxis;
import com.vaadin.addon.charts.model.style.GradientColor;
import com.vaadin.addon.charts.model.style.SolidColor;
import java.nio.charset.Charset;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.vaadin.tingerforge.Topic;

/**
 * Gauge display for MQTT client.
 *
 * @author Sami Ekblad
 */
public class GaugeDisplay extends MqttDisplay {

    protected Chart gauge;

    protected static int GAUGE_MIN = 0;
    protected static int GAUGE_MAX = 100;

    public GaugeDisplay(String serverUrl, Topic topic, String id, Number min, Number max) {
        super(serverUrl, id, topic);
    }

    @Override
    public void messageArrived(String string, MqttMessage mm) {

        if (gauge == null) {
            gauge = createGaugeChart(getTopic().getName(), getTopic().getUnit(), GAUGE_MIN, GAUGE_MAX);
            showChart(gauge);
        }

        String lastMessage = payloadToString(mm);
        gauge.getConfiguration()
                .getSeries()
                .forEach(s -> {
                    Double newValue = Double.valueOf(lastMessage.split(":")[3]);
                    ((ListSeries) s).updatePoint(0, newValue);
                });
    }

    protected Chart createGaugeChart(String name, String unit, int min, int max) {
        final Chart chart = new Chart();

        final Configuration configuration = new Configuration();
        configuration.getChart().setType(ChartType.GAUGE);
        configuration.getChart().setAlignTicks(false);
        configuration.getChart().setPlotBackgroundColor(null);
        configuration.getChart().setPlotBackgroundImage(null);
        configuration.getChart().setPlotBorderWidth(0);
        configuration.getChart().setPlotShadow(false);

        configuration.getPane().setStartAngle(-150);
        configuration.getPane().setEndAngle(150);

        YAxis yAxis = new YAxis();
        yAxis.setMin(min);
        yAxis.setMax(max);
        yAxis.setLineColor(new SolidColor(COLOR_PRIMARY));
        yAxis.setTickColor(new SolidColor(COLOR_PRIMARY));
        yAxis.setMinorTickColor(new SolidColor(COLOR_PRIMARY));
        yAxis.setOffset(-25);
        yAxis.setLineWidth(2);
        yAxis.setLabels(new Labels());
        yAxis.getLabels().setDistance(-20);
        yAxis.getLabels().setRotationPerpendicular();
        yAxis.setTickLength(5);
        yAxis.setMinorTickLength(5);
        yAxis.setEndOnTick(false);
        configuration.addyAxis(yAxis);

        final ListSeries series = new ListSeries(name + " (" + unit + ")", 12);

        PlotOptionsGauge plotOptionsGauge = new PlotOptionsGauge();
        plotOptionsGauge.setDataLabels(new Labels());
        plotOptionsGauge.getDataLabels().setFormatter("function() {return '' + this.y +  ' " + unit + "';}");
        plotOptionsGauge.getTooltip().setValueSuffix(unit);

        plotOptionsGauge.getDataLabels().setBackgroundColor(new SolidColor(COLOR_SECONDARY));

        series.setPlotOptions(plotOptionsGauge);
        configuration.setSeries(series);
        chart.drawChart(configuration);

        return chart;
    }

    protected GradientColor getLinearGradient(String from, String toColor) {
        GradientColor gradient = GradientColor.createLinear(0, 0, 0, 1);
        gradient.addColorStop(0, new SolidColor(from));
        gradient.addColorStop(1, new SolidColor(from));
        return gradient;
    }

    private String payloadToString(MqttMessage mm) {
        byte[] payload = mm.getPayload();
        return new String(payload, Charset.forName(MQTT_CHARSET));
    }
}
