package org.vaadin.se.mqtt.displays;

import com.vaadin.addon.charts.Chart;
import com.vaadin.addon.charts.model.ChartType;
import com.vaadin.addon.charts.model.DataSeries;
import org.vaadin.se.mqtt.MqttDataSource;
import org.vaadin.se.mqtt.MqttMessageConverter;

/**
 * Gauge display for MQTT client.
 *
 * @author Sami Ekblad
 */
public class SparklineDisplay extends MqttDisplay {

    public SparklineDisplay(MqttDataSource source, MqttMessageConverter converter) {
        super(source, 30, converter);
    }

    @Override
    protected Chart createChart(String name, String unit, Number min, Number max) {
        final DataSeries series = new DataSeries(name + " (" + unit + ")");

        ChartUtils cb = ChartUtils.build(null, ChartType.LINE, false)
                .size(SIZE_X_PX, SIZE_Y_025_PX)
                .series(series);
        cb.lineOptions(series);

        cb.xAxis(null);
        cb.yAxis(null, min, max, -1);

        return cb.draw();
    }

}
