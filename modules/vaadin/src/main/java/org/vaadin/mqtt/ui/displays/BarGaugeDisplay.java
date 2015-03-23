package org.vaadin.mqtt.ui.displays;

import com.vaadin.addon.charts.Chart;
import com.vaadin.addon.charts.model.ChartType;
import com.vaadin.addon.charts.model.DataSeries;
import com.vaadin.addon.charts.model.PlotOptionsColumn;
import com.vaadin.addon.charts.model.YAxis;
import com.vaadin.addon.charts.model.style.GradientColor;
import com.vaadin.addon.charts.model.style.SolidColor;
import org.vaadin.mqtt.ui.MqttDataSource;
import org.vaadin.mqtt.ui.MqttMessageConverter;

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
        PlotOptionsColumn opt = cu.columnOptions(series);        
        opt.setThreshold(min);
        cu.yAxis(null, min, max, Math.ceil((max.doubleValue() - min.doubleValue()) / 10));

        // Apply colors 
        cu.colors(getColors());

        return cu.draw();
    }
}
