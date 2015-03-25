package junit.org.vaadin.tinkerforge.modules.communication.mqtt.publisher;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Created by sven on 14.02.15.
 */
public class DemoDataFunctionGenerator {

    private int valueMin = 0;
    private int valueMax = 40;
    private int maxCount = 5;
    private int stepSize = 10;

    private int lastValue = 0;
    private List<Double> doubleList = Collections.EMPTY_LIST;

    private DemoDataFunctionGenerator(Builder builder) {
        valueMin = builder.valueMin;
        valueMax = builder.valueMax;
        maxCount = builder.maxCount;
        stepSize = builder.stepSize;
    }

    public Double nextValue() {

        if (doubleList.isEmpty()) {
            System.out.println("generating = ");
            List<Integer> integerList = new Random()
                    .ints(valueMin, valueMax)
                    .limit(maxCount)
                    .boxed()
                    .collect(Collectors.toList());

            //add last value if present
            integerList.add(0, lastValue);
            lastValue = integerList.get(integerList.size()-1);

            doubleList = new WorkLoadGenerator()
                    .generate(stepSize, integerList);
        }
        return doubleList.remove(0);
    }


    public static final class Builder {
        private int valueMin;
        private int valueMax;
        private int maxCount;
        private int stepSize;

        public Builder() {
        }

        public Builder valueMin(int valueMin) {
            this.valueMin = valueMin;
            return this;
        }

        public Builder valueMax(int valueMax) {
            this.valueMax = valueMax;
            return this;
        }

        public Builder maxCount(int maxCount) {
            this.maxCount = maxCount;
            return this;
        }

        public Builder stepSize(int stepSize) {
            this.stepSize = stepSize;
            return this;
        }

        public DemoDataFunctionGenerator build() {
            return new DemoDataFunctionGenerator(this);
        }
    }
}
