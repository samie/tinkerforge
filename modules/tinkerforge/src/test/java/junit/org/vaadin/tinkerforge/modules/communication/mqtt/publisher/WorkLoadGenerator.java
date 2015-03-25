package junit.org.vaadin.tinkerforge.modules.communication.mqtt.publisher;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.interpolation.SplineInterpolator;
import org.apache.commons.math3.analysis.interpolation.UnivariateInterpolator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sven Ruppert on 12.11.13.
 */
public class WorkLoadGenerator {



    private UnivariateFunction createInterpolateFunction(int stepSize, final List<Integer> values) {
        final double[] valueArrayX = new double[values.size()];
        for (int i = 0; i < valueArrayX.length; i++) {
            valueArrayX[i] = (double) i * stepSize;
        }

        final double[] valueArrayY = new double[values.size()];
        int i = 0;
        for (final Integer value : values) {
            valueArrayY[i] = (double) value;
            i = i + 1;
        }

        final UnivariateInterpolator interpolator = new SplineInterpolator();
        return interpolator.interpolate(valueArrayX, valueArrayY);
    }

    public List<Double> generate(int stepSize, final List<Integer> v) {
        final UnivariateFunction interpolateFunction = createInterpolateFunction(stepSize, v);
        //baue Kurve auf
        final int anzahlValuesInterpolated = (v.size() - 1) * stepSize;
        final List<Double> result = new ArrayList<>();
        for (int i = 0; i < anzahlValuesInterpolated - 1; i++) {
            final double valueForY = interpolateFunction.value(i);
            result.add(valueForY);
        }
        return result;
    }

}
