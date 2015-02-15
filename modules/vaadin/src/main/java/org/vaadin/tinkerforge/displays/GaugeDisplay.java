/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.vaadin.tinkerforge.displays;

import com.vaadin.addon.charts.Chart;
import com.vaadin.addon.charts.model.ChartModel;
import com.vaadin.addon.charts.model.ChartType;
import com.vaadin.addon.charts.model.Configuration;
import com.vaadin.addon.charts.model.Labels;
import com.vaadin.addon.charts.model.ListSeries;
import com.vaadin.addon.charts.model.PlotOptionsGauge;
import com.vaadin.addon.charts.model.YAxis;
import com.vaadin.addon.charts.model.style.GradientColor;
import com.vaadin.addon.charts.model.style.SolidColor;
import java.nio.charset.Charset;

import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.vaadin.tinkerforge.modules.communication.mqtt.Topic;

/**
 * Gauge display for MQTT client.
 *
 * @author Sami Ekblad
 */
public class GaugeDisplay extends MqttDisplay {

    protected Chart gauge;

    public GaugeDisplay(String serverUrl, Topic topic, String id, Number min, Number max) {
        super(serverUrl, id, topic);
    }

    @Override
    public void messageArrived(String string, MqttMessage mm) {

        if (gauge == null) {
            gauge = createGaugeChart(getTopic().getName(), getTopic().getUnit(), getTopic().getMin(), getTopic().getMax());
            showChart(gauge);
        }

        String lastMessage = payloadToString(mm);
        if (lastMessage != null) {
            gauge.getConfiguration()
                    .getSeries()
                    .forEach(s -> {
                        Double newValue = Double.valueOf(lastMessage.split(":")[3]);
                        ((ListSeries) s).updatePoint(0, newValue);
                    });
        }
    }

    protected Chart createGaugeChart(String name, String unit, Number min, Number max) {
        final Chart chart = new Chart();
        chart.setWidth(GAUGE_SIZE_PX);
        chart.setHeight(GAUGE_SIZE_PX);
        
        final Configuration conf = new Configuration();
        conf.setTitle((String) null);

        ChartModel c = conf.getChart();
        c.setType(ChartType.GAUGE);
        c.setAlignTicks(false);
        c.setPlotBackgroundColor(null);
        c.setPlotBackgroundImage(null);
        c.setPlotBorderWidth(0);
        c.setPlotShadow(false);
       

        conf.getPane().setStartAngle(-150);
        conf.getPane().setEndAngle(150);
        c.setBackgroundColor(new SolidColor(COLOR_BACKGROUND));

        YAxis yAxis = new YAxis();
        yAxis.setMin(min);
        yAxis.setMax(max);
        yAxis.setLineColor(new SolidColor(COLOR_BACKGROUND));
        yAxis.setTickColor(new SolidColor(COLOR_PRIMARY));
        yAxis.setMinorTickColor(new SolidColor(COLOR_PRIMARY));
        yAxis.setOffset(-25);
        yAxis.setLineWidth(0);
        yAxis.setLabels(new Labels());
        yAxis.getLabels().setDistance(-20);
        yAxis.getLabels().setRotationPerpendicular();
        yAxis.setTickLength(5);
        yAxis.setMinorTickLength(5);
        yAxis.setEndOnTick(false);
        conf.addyAxis(yAxis);

        final ListSeries series = new ListSeries(name + " (" + unit + ")", 12);

        PlotOptionsGauge go = new PlotOptionsGauge();
        go.setDataLabels(new Labels());
        go.getDataLabels().setFormatter("function() {return '' + this.y +  ' " + unit + "';}");
        go.getTooltip().setValueSuffix(unit);
        go.getDataLabels().setBackgroundColor(new SolidColor(COLOR_BACKGROUND));
        go.setColor(new SolidColor(COLOR_BACKGROUND));

        series.setPlotOptions(go);
        conf.setSeries(series);
        chart.drawChart(conf);

        return chart;
    }
    private static final String GAUGE_SIZE_PX = "275px";

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
