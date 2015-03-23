package org.vaadin.mqtt.ui.displays;

import com.vaadin.addon.charts.Chart;
import com.vaadin.addon.charts.model.AbstractSeries;
import com.vaadin.addon.charts.model.Background;
import com.vaadin.addon.charts.model.ChartModel;
import com.vaadin.addon.charts.model.ChartType;
import com.vaadin.addon.charts.model.Configuration;
import com.vaadin.addon.charts.model.DataSeries;
import com.vaadin.addon.charts.model.Labels;
import com.vaadin.addon.charts.model.Marker;
import com.vaadin.addon.charts.model.Pane;
import com.vaadin.addon.charts.model.PlotOptionsColumn;
import com.vaadin.addon.charts.model.PlotOptionsLine;
import com.vaadin.addon.charts.model.PlotOptionsSolidGauge;
import com.vaadin.addon.charts.model.PlotOptionsSpline;
import com.vaadin.addon.charts.model.Series;
import com.vaadin.addon.charts.model.Title;
import com.vaadin.addon.charts.model.XAxis;
import com.vaadin.addon.charts.model.YAxis;
import com.vaadin.addon.charts.model.style.Color;
import com.vaadin.addon.charts.model.style.GradientColor;
import com.vaadin.addon.charts.model.style.SolidColor;
import java.util.ArrayList;
import java.util.List;

/**
 * Some helpers to configure charts.
 *
 * @author Sami Ekblad
 */
public class ChartUtils {

    private final Chart chart;
    private final Configuration conf;
    private final ChartModel model;

    private ChartUtils(Chart chart) {
        this.chart = chart;
        this.conf = chart.getConfiguration();
        this.model = conf.getChart();
    }

    public static ChartUtils build(String title, ChartType type, boolean showLegend) {
        ChartUtils cu = new ChartUtils(new Chart(type)).title(title).legend(false);
        return cu;
    }

    public ChartUtils legend(boolean showLegend) {
        conf.getLegend().setEnabled(showLegend);
        return this;
    }

    public ChartUtils size(String x, String y) {
        chart.setWidth(x);
        chart.setHeight(y);
        return this;
    }

    public Chart draw() {
        chart.drawChart();
        return chart;
    }

    public ChartUtils title(String title) {
        conf.setTitle((String) null);
        return this;
    }

    public ChartUtils series(Series... series) {
        conf.setSeries(series);
        return this;
    }

    public ChartModel getModel() {
        return this.model;
    }

    public Configuration getConf() {
        return this.conf;
    }

    public ChartUtils xAxis(String title) {
        XAxis xAxis = new XAxis();
        xAxis.setTitle(title != null ? new Title(title) : new Title(""));
        xAxis.setLabels(new Labels(false));
        Color transparent = new SolidColor(0, 0, 0, 0);
        xAxis.setTickColor(transparent);
        xAxis.setMinorTickColor(transparent);
        xAxis.setTickLength(0);
        xAxis.setTickWidth(0);
        xAxis.setMinorTickLength(0);
        xAxis.setMinorTickWidth(0);

        conf.addxAxis(xAxis);
        return this;
    }

    public ChartUtils yAxis(String title, Number min, Number max, Number tickInterval) {
        YAxis yAxis = new YAxis();
        yAxis.setTitle(title != null ? new Title(title) : new Title(""));
        yAxis.setExtremes(min, max);
        if (tickInterval.intValue() <= 0 || tickInterval.doubleValue() > max.doubleValue()) {
            Color transparent = new SolidColor(0, 0, 0, 0);
            yAxis.setTickColor(transparent);
            yAxis.setMinorTickColor(transparent);
            yAxis.setLineColor(transparent);
            yAxis.setGridLineWidth(0);
            yAxis.setLabels(new Labels(false));
        } else {
            yAxis.setLabels(new Labels(true));
            yAxis.setTickInterval(tickInterval);
        }
        conf.addyAxis(yAxis);
        return this;
    }

    public YAxis getYAxis() {
        return conf.getyAxis();
    }

    public XAxis getXAxis() {
        return conf.getxAxis();
    }

    public ChartUtils dataLabels(AbstractSeries forSeries, String format, boolean html, int x, int y, int borderWidth) {
        Labels labels = new Labels();
        labels.setFormatter(format);
        labels.setBorderWidth(borderWidth);
        labels.setUseHTML(html);
        labels.setX(x);
        labels.setY(y);
        forSeries.getPlotOptions().setDataLabels(labels);
        return this;
    }

    public PlotOptionsColumn columnOptions(AbstractSeries series) {
        PlotOptionsColumn go = new PlotOptionsColumn();
        go.setPointWidth(null);
        go.getTooltip().setEnabled(false);
        series.setPlotOptions(go);
        return go;
    }

    public PlotOptionsLine lineOptions(AbstractSeries series) {
        PlotOptionsLine go = new PlotOptionsLine();
        go.setPointWidth(null);
        go.setMarker(new Marker(false));
        go.getTooltip().setEnabled(false);
        go.setTurboThreshold(0);
        series.setPlotOptions(go);
        return go;
    }

    public PlotOptionsSpline splineOptions(AbstractSeries series) {
        PlotOptionsSpline go = new PlotOptionsSpline();
        go.setPointWidth(null);
        go.setMarker(new Marker(false));
        go.getTooltip().setEnabled(false);
        series.setPlotOptions(go);
        return go;
    }

    public PlotOptionsSolidGauge gaugeOptions(AbstractSeries series) {
        PlotOptionsSolidGauge go = new PlotOptionsSolidGauge();
        series.setPlotOptions(go);
        return go;
    }

    public ChartUtils gauge(int start, int end) {
        Pane pane = conf.getPane();
        pane.setStartAngle(start);
        pane.setEndAngle(end);
        return this;
    }

    public ChartUtils background(String innerRadius, String outerRadius, String shape, int borderWidth) {
        Background bkg = new Background();
        bkg.setInnerRadius(innerRadius);
        bkg.setOuterRadius(outerRadius);
        bkg.setShape(shape);
        bkg.setBorderWidth(borderWidth);
        conf.getPane().setBackground(bkg);
        return this;
    }

    public void stops(YAxis.Stop... stops) {
        conf.getyAxis().setStops(stops);
    }

    public ChartUtils polar() {
        conf.getChart().setPolar(true);
        return this;
    }

    public ChartUtils tooltip(boolean enabled) {
        conf.getTooltip().setEnabled(enabled);
        return this;
    }

    public ChartUtils pane(int x, int y) {
        Pane pane = new Pane(x, y);
        pane.setBackground(new Background[]{});
        conf.addPane(pane);
        return this;
    }

    public ChartUtils xAxis(String title, String... categories) {
        return xAxis(title).categories(categories);
    }

    public ChartUtils categories(String... categories) {
        conf.getxAxis().setCategories(categories);
        return this;
    }

    public ChartUtils colors(String[] colors) {
        if (colors != null && colors.length > 1) {

            if (model.getType() == ChartType.SOLIDGAUGE) {
                // Stops are only supported in SOLIDGAUGE.                       
                List<YAxis.Stop> stops = convertToColors(colors);
                stops(stops.toArray(new YAxis.Stop[]{}));
            } else {
                gradient(colors[0], colors[colors.length - 1]);
            }
        }
        if (colors != null && colors.length == 1) {
            // Single color spec
            color(colors[0]);
        }
        return this;
    }

    /**
     * Calculate stops for linear gradient based on colors from 10% to 90%.
     *
     * @param colors
     * @return
     */
    private List<YAxis.Stop> convertToColors(String[] colors) {
        List<YAxis.Stop> stops = new ArrayList<>(colors.length);
        float stepSize = colors.length > 1 ? 0.8f / (float) (colors.length - 1) : 1f;
        for (int i = 0; i < colors.length; i++) {
            stops.add(new YAxis.Stop(0.1f + i * stepSize, new SolidColor(colors[i])));
        }
        ;
        return stops;
    }

    /**
     * Create linear gradient.
     *
     * @param from
     * @param toColor
     * @return
     */
    public GradientColor getLinearGradient(String from, String toColor) {
        GradientColor gradient = GradientColor.createLinear(0, 0, 0, 1);
        gradient.addColorStop(0, new SolidColor(from));
        gradient.addColorStop(1, new SolidColor(from));
        return gradient;
    }

    void color(Series series, String color) {
        ((DataSeries) series).getPlotOptions().setColor(new SolidColor(color));
    }

    void color(String color) {
        conf.getSeries().forEach(s -> color(s, color));
    }

    void gradient(Series series, String min, String max) {
        ((DataSeries) series).getPlotOptions().setColor(getLinearGradient(min, max));
    }

    void gradient(String min, String max) {
        conf.getSeries().forEach(s -> gradient(s, min, max));
    }

}
