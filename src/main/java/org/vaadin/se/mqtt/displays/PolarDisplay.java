package org.vaadin.se.mqtt.displays;

import com.vaadin.addon.charts.Chart;
import com.vaadin.addon.charts.model.ChartType;
import com.vaadin.addon.charts.model.DataSeries;
import com.vaadin.addon.charts.model.PlotOptionsColumn;
import com.vaadin.addon.charts.model.XAxis;
import org.vaadin.se.mqtt.MqttDataSource;
import org.vaadin.se.mqtt.MqttMessageParser;

/**
 * Gauge display for MQTT client.
 *
 * @author Sami Ekblad
 */
public class PolarDisplay extends MqttDisplay {

    public PolarDisplay(MqttDataSource source, MqttMessageParser converter) {
        super(source, converter);
    }

    @Override
    protected Chart createChart(String name, String unit, Number min, Number max) {
        final DataSeries series = new DataSeries(name + " (" + unit + ")");

        ChartUtils cb = ChartUtils.build(null, ChartType.AREA, false)
                .size(SIZE_X_PX, SIZE_Y_PX)
                .series(series)
                .polar()
                .pane(-45, 315);

        XAxis axis = new XAxis();
        cb.xAxis(null);
        cb.yAxis(null, min, max, -1);

        PlotOptionsColumn opts = cb.columnOptions(series);
        return cb.draw();
    }

}
