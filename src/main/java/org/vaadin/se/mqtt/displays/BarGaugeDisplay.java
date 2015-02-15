package org.vaadin.se.mqtt.displays;

import com.vaadin.addon.charts.Chart;
import com.vaadin.addon.charts.model.ChartType;
import com.vaadin.addon.charts.model.DataSeries;
import com.vaadin.addon.charts.model.YAxis;
import com.vaadin.addon.charts.model.style.GradientColor;
import com.vaadin.addon.charts.model.style.SolidColor;
import org.vaadin.se.mqtt.MqttDataSource;
import org.vaadin.se.mqtt.MqttMessageConverter;

/**
 * Linear gauge display for MQTT client.
 *
 * @author Sami Ekblad
 */
public class BarGaugeDisplay extends MqttDisplay {

    public BarGaugeDisplay(MqttDataSource source, MqttMessageConverter converter) {
        super(source, 0, converter);
    }

    @Override
    protected Chart createChart(String name, String unit, Number min, Number max) {
        final DataSeries series = new DataSeries(name + " (" + unit + ")");

        final ChartUtils cu = ChartUtils.build(null, ChartType.COLUMN, false).
                size(SIZE_X_PX, SIZE_Y_PX).series(series);

        // Axis configuration
        cu.xAxis(null);
        cu.columnOptions(series);
        cu.yAxis(null, min, max, Math.ceil((max.doubleValue() - min.doubleValue()) / 10));

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
