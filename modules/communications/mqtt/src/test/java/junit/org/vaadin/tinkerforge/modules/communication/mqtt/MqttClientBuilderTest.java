package junit.org.vaadin.tinkerforge.modules.communication.mqtt;

import junit.framework.Assert;
import org.eclipse.paho.client.mqttv3.*;
import org.junit.Test;
import org.vaadin.tinkerforge.modules._utils.WaitForQ;
import org.vaadin.tinkerforge.modules.communication.mqtt.MqttBuffer;
import org.vaadin.tinkerforge.modules.communication.mqtt.MqttClientBuilder;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.IntStream;

import static org.junit.Assert.*;

public class MqttClientBuilderTest {

    public static final int QUALITY = 1;

    @org.junit.Before
    public void setUp() throws Exception {

    }

    @org.junit.After
    public void tearDown() throws Exception {

    }


    static final ExecutorService fixedThreadPool = Executors.newFixedThreadPool(4); //TODO dynamisch
    public static final String TOPIC = "TinkerForge/Wetterstation/";
    public static final String HOST = "127.0.0.1";  //wetterstation
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
        MqttBuffer buffer = new MqttBuffer()
                .client(sender).topic(TEST_TOPIC)
                .qos(QUALITY)
                .retained(true);

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

        //to fast for broker -- messages lost
        IntStream.range(0,100).forEach(v -> buffer.sendAsync(v+""));

        Thread.sleep(3_000);
        assertTrue(longAdder.intValue() > 0);
        assertEquals(100, longAdder.intValue());
        System.out.println("resultSet = " + longAdder.intValue());

//        WaitForQ waitForQ = new WaitForQ();
//        waitForQ.addShutDownAction(() -> {
//            try {
//                sender.close();
//            } catch (MqttException e) {
//                e.printStackTrace();
//            }
//        });
//        waitForQ.addShutDownAction(() -> {
//            try {
//                consumer.close();
//            } catch (MqttException e) {
//                e.printStackTrace();
//            }
//        });
//        waitForQ.addShutDownAction(() -> System.exit(0));
//        waitForQ.waitForQ();

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