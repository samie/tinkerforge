package junit.org.vaadin.tinkerforge.modules.communication.mqtt;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.junit.Test;
import org.vaadin.tinkerforge.modules.communication.mqtt.MqttBuffer;
import org.vaadin.tinkerforge.modules.communication.mqtt.MqttClientBuilder;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MqttClientBuilderTest {



    @org.junit.Before
    public void setUp() throws Exception {

    }

    @org.junit.After
    public void tearDown() throws Exception {

    }
    public static final int QUALITY = 1;
    public static final String TOPIC = "TinkerForge/Wetterstation/";
    public static final String BROKER = "127.0.0.1";  //wetterstation

    @Test
    public void test001() throws Exception {

        final String TEST_TOPIC = TOPIC + System.nanoTime();

        MqttClientBuilder builder = new MqttClientBuilder();
        MqttClient sender = builder
                .uri("tcp://" + BROKER + ":1883")
                .clientUIDGenerated()
                .build();
        sender.connect();
        MqttBuffer buffer = MqttBuffer.newBuilder()
                .withClient(sender).withTopic(TEST_TOPIC)
                .withQos(QUALITY)
                .withRetained(true)
                .build();

        MqttClient consumer = builder
                .uri("tcp://" + BROKER + ":1883")
                .clientUIDGenerated()
                .build();
        consumer.connect();
        consumer.subscribe(TEST_TOPIC);

        LongAdder longAdder = new LongAdder();

        consumer.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable throwable) {
                //
            }

            @Override
            public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
                longAdder.increment();
            }
            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
            }
        });

        Timer consumerTimer = new Timer();
        consumerTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                System.out.println("resultSet = " + longAdder.intValue());
            }
        }, 0, 1_000);

        IntStream.range(0,100).forEach(v -> buffer.sendAsync(v+""));

        Thread.sleep(3_000);
        assertTrue(longAdder.intValue() > 0);
        assertEquals(100, longAdder.intValue());
        System.out.println("resultSet = " + longAdder.intValue());
    }


    @Test
    public void testBuild() throws Exception {

    }

    @Test
    public void testUri() throws Exception {

    }

    @Test
    public void testClientUID() throws Exception {

    }

    @Test
    public void testClientUIDGenerated() throws Exception {

    }

    @Test
    public void testMemoryPersistence() throws Exception {

    }

    @Test
    public void testFilePersistence() throws Exception {

    }
}