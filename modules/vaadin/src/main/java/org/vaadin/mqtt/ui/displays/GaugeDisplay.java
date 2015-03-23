/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.vaadin.mqtt.ui.displays;

import com.vaadin.addon.charts.Chart;
import com.vaadin.addon.charts.model.ChartType;
import com.vaadin.addon.charts.model.DataSeries;
import org.vaadin.mqtt.ui.MqttDataSource;
import org.vaadin.mqtt.ui.MqttMessageConverter;

/**
 * Gauge display for MQTT client.
 *
 * @author Sami Ekblad
 */
public class GaugeDisplay extends MqttDisplay {

    public GaugeDisplay(MqttDataSource source, MqttMessageConverter converter) {
        super(source, 0, converter);
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

        // Apply colors 
        cu.colors(getColors());

        return cu.draw();
    }


}
