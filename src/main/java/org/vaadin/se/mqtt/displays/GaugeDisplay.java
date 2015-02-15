/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.vaadin.se.mqtt.displays;

import com.vaadin.addon.charts.Chart;
import com.vaadin.addon.charts.model.ChartType;
import com.vaadin.addon.charts.model.DataSeries;
import com.vaadin.addon.charts.model.YAxis;
import com.vaadin.addon.charts.model.style.GradientColor;
import com.vaadin.addon.charts.model.style.SolidColor;
import org.vaadin.se.mqtt.MqttDataSource;
import org.vaadin.se.mqtt.MqttMessageParser;

/**
 * Gauge display for MQTT client.
 *
 * @author Sami Ekblad
 */
public class GaugeDisplay extends MqttDisplay {

    public GaugeDisplay(MqttDataSource source, MqttMessageParser converter) {
        super(source, converter);
    }

    @Override
    protected Chart createChart(String name, String unit, Number min, Number max) {
        final DataSeries series = new DataSeries(name + " (" + unit + ")");

        final ChartUtils cu = ChartUtils.build(null, ChartType.SOLIDGAUGE, false).
                size(SIZE_X_PX, SIZE_Y_PX).series(series);

        cu.gauge(-150, 150).background("60%", "100%", "arc", 0);
        cu.gaugeOptions(series);
        cu.dataLabels(series,
                "function() {return '<div style=\"text-align:center;\"><span style=\"font-size: 2em;\">' + this.y +  '</span><br /><span style=\"font-size: 1em;\">" + unit + "</span></div>';}", true, 0, -20, 0);

        // Axis configuration
        cu.yAxis(null, min, max, -1);

        // Colors 
        YAxis.Stop stop1 = new YAxis.Stop(0.1f, SolidColor.DARKGRAY);
        YAxis.Stop stop2 = new YAxis.Stop(0.5f, SolidColor.ORANGE);
        YAxis.Stop stop3 = new YAxis.Stop(0.9f, SolidColor.YELLOW);
        cu.stops(stop1, stop2, stop3);
                
        return cu.draw();
    }

    protected GradientColor getLinearGradient(String from, String toColor) {
        GradientColor gradient = GradientColor.createLinear(0, 0, 0, 1);
        gradient.addColorStop(0, new SolidColor(from));
        gradient.addColorStop(1, new SolidColor(from));
        return gradient;
    }

}
