package junit.org.vaadin.tinkerforge.modules.communication.mqtt.publisher;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.vaadin.model.SensorValue;
import org.vaadin.model.converter.json.SensorValueJsonConverter;
import org.vaadin.tinkerforge.modules._utils.WaitForQ;
import org.vaadin.tinkerforge.modules.communication.mqtt.MqttBuffer;
import org.vaadin.tinkerforge.modules.communication.mqtt.MqttClientBuilder;
import org.vaadin.tinkerforge.modules.communication.mqtt.MqttTopic;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Created by sven on 13.02.15.
 */
public class DemoDataPublisherMain {


    public static final String BROKER = "127.0.0.1";  //wetterstation
    public static final int QUALITY = 1;

    public static void main(String[] args) throws MqttException {

        MqttClientBuilder builder = new MqttClientBuilder();
        MqttClient sender = builder
                .uri("tcp://" + BROKER + ":1883")
                .clientUIDGenerated()
                .build();
        sender.connect();

        MqttTopic[] topics = MqttTopic.WETTERSTATION_ALL;

        Map<MqttTopic, MqttBuffer> mqttBufferMap = new HashMap<>();

        for (MqttTopic topic : topics) {
            MqttBuffer buffer = new MqttBuffer()
                    .client(sender).topic(topic.getTopic())
                    .qos(QUALITY)
                    .retained(true);
            mqttBufferMap.put(topic, buffer);
        }


        final Timer timer = new Timer();
        mqttBufferMap.forEach((topic, mqttBuffer) -> timer.scheduleAtFixedRate(new TimerTask() {
            final DemoDataFunctionGenerator generator = new DemoDataFunctionGenerator.Builder()
                    .valueMin(topic.getMin().intValue())
                    .valueMax(topic.getMax().intValue())
                    .maxCount(100)
                    .stepSize(20)
                    .build();
            private  SensorValueJsonConverter converter = new SensorValueJsonConverter();
            final SensorValue.Builder sensorValueBuilder = SensorValue.newBuilder()
                    .deviceType(topic.getName());
            @Override
            public void run() {
                double v = Math.round(100.0 * generator.nextValue()) / 100.0;
                int rawValue = (int) (v * 100);
                SensorValue sensorValue = sensorValueBuilder.localDateTime(LocalDateTime.now()).rawValue(rawValue).build();
                mqttBuffer.sendAsync(converter.toJson(sensorValue));
            }
        }, 0, 2_000));



        WaitForQ waitForQ = new WaitForQ();
        waitForQ.addShutDownAction(timer::cancel);
        waitForQ.addShutDownAction(() -> {
            try {
                sender.close();
            } catch (MqttException e) {
                e.printStackTrace();
            }
        });
        waitForQ.addShutDownAction(() -> System.exit(0));
        waitForQ.waitForQ();

    }

}
