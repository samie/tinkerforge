package junit.org.vaadin.tinkerforge.modules.communication.mqtt.publisher;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
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


        Timer timer = new Timer();

        mqttBufferMap.forEach((topic, mqttBuffer) -> {
            final DemoDataFunctionGenerator generator = new DemoDataFunctionGenerator.Builder()
                    .valueMin(topic.getMin().intValue())
                    .valueMax(topic.getMax().intValue())
                    .maxCount(100)
                    .stepSize(20)
                    .build();
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    double v = Math.round(100.0 * generator.nextValue()) / 100.0;
                    mqttBuffer.sendAsync(LocalDateTime.now() + ":" + v + "");
                }
            }, 0, 2_000);
        });



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
