/*
 * Copyright [2014] [www.rapidpm.org / Sven Ruppert (sven.ruppert@rapidpm.org)]
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package org.rapidpm.module.iot.tinkerforge;


import com.tinkerforge.IPConnection;
import com.tinkerforge.NotConnectedException;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.rapidpm.module.iot.tinkerforge.data.SensorDataElement;
import org.rapidpm.module.iot.tinkerforge.sensor.TinkerForgeBaseSensor;
import org.rapidpm.module.iot.tinkerforge.sensor.singlevalue.Barometer;
import org.rapidpm.module.iot.tinkerforge.sensor.singlevalue.Humidity;
import org.rapidpm.module.iot.tinkerforge.sensor.singlevalue.Light;
import org.rapidpm.module.iot.tinkerforge.sensor.singlevalue.Temperature;
import org.vaadin.model.SensorValue;
import org.vaadin.model.converter.json.SensorValueJsonConverter;
import org.vaadin.tinkerforge.modules._utils.WaitForQ;
import org.vaadin.tinkerforge.modules.communication.mqtt.MqttBuffer;
import org.vaadin.tinkerforge.modules.communication.mqtt.MqttClientBuilder;
import org.vaadin.tinkerforge.modules.communication.mqtt.MqttTopic;

import java.time.LocalDateTime;

/**
 * Created by Sven Ruppert on 15.02.14.
 */
public class WeatherStationRemote {

    //TODO wrong place and duplicated
    private static final MqttTopic topicLight = new MqttTopic("TinkerForge/Wetterstation/Light", "Ambient Light", "Lux", 1, 1000);
    private static final MqttTopic topicTemp = new MqttTopic("TinkerForge/Wetterstation/Temp", "Temperature", "C", -40, +40);
    private static final MqttTopic topicHum = new MqttTopic("TinkerForge/Wetterstation/Hum", "Humidity", "%RH", 0, 100);
    private static final MqttTopic topicAir = new MqttTopic("TinkerForge/Wetterstation/Air", "Air Pressure", "mBar", 500, 1500);

    public static final String HOST = "127.0.0.1";  //wetterstation
    public static final int PORT = 4223;
    private static int callbackPeriod = 5000;

    private static IPConnection ipcon = new IPConnection();
    public static final int QUALITY = 1;
    public static final String BROKER = "127.0.0.1";  //MQTT broker

    public static void main(String args[]) throws Exception {

        ipcon.connect(HOST, PORT);

        MqttClientBuilder builder = new MqttClientBuilder();
        MqttClient sender = builder
                .uri("tcp://" + BROKER + ":1883")
                .clientUIDGenerated()
                .build();
        sender.connect();

        MqttBuffer.Builder bufferBuilder = MqttBuffer.newBuilder().withClient(sender).withQos(QUALITY).withRetained(true);

        final MqttBuffer mqttBufferTemp = bufferBuilder.withTopic(topicTemp.getTopic()).build();
        final MqttBuffer mqttBufferAir = bufferBuilder.withTopic(topicAir.getTopic()).build();
        final MqttBuffer mqttBufferHum = bufferBuilder.withTopic(topicHum.getTopic()).build();
        final MqttBuffer mqttBufferLight = bufferBuilder.withTopic(topicLight.getTopic()).build();

        final Temperature temperature = new Temperature("dXj", callbackPeriod, ipcon);
        temperature.bricklet.addTemperatureListener(sensorvalue -> {
            final String text = LocalDateTime.now() + " - Temp  : " + sensorvalue + " °C";
            System.out.println("text = " + text);
        });

        temperature.addSensorDataAction(sensorValue -> {
            String json = sensorValue2JSON(temperature, sensorValue);
            mqttBufferTemp.sendAsync(json);
        });

        temperature.run();


        final Barometer barometer = new Barometer("jY4", callbackPeriod, ipcon);
        barometer.bricklet.addAirPressureListener(sensorvalue -> {
            final String text = LocalDateTime.now() + " - Air   : " + sensorvalue / 1000.0 + " mbar";
            System.out.println("text = " + text);

        });
        barometer.addSensorDataAction(v -> {
            String json = sensorValue2JSON(barometer, v);
            mqttBufferAir.sendAsync(json);
        });
        barometer.run();

        final Light light = new Light("jy2", callbackPeriod, ipcon);
        light.bricklet.addIlluminanceListener(sensorvalue -> {
            final double lux = sensorvalue / 10.0;
            final String text = LocalDateTime.now() + " - Lux   : " + lux + " Lux";
            System.out.println("text = " + text);
        });
        light.addSensorDataAction(v -> {
            String json = sensorValue2JSON(light, v);
            mqttBufferLight.sendAsync(json);
        });
        light.run();

        final Humidity humidity = new Humidity("kfd", callbackPeriod, ipcon);
        humidity.bricklet.addHumidityListener(sensorvalue -> {
            final double tempNorm = sensorvalue / 10.0;
            final String text = LocalDateTime.now() + " - RelHum: " + tempNorm + " %RH";
            System.out.println("text = " + text);
        });
        humidity.addSensorDataAction(v -> {
            String json = sensorValue2JSON(humidity, v);
            mqttBufferHum.sendAsync(json);
        });



        WaitForQ waitForQ = new WaitForQ();

        waitForQ.addShutDownAction(() -> {
            try {
                ipcon.disconnect();
            } catch (NotConnectedException e) {
                e.printStackTrace();
            }
        });
        waitForQ.addShutDownAction(() -> System.exit(0));

        waitForQ.waitForQ();
    }

    private static String sensorValue2JSON(TinkerForgeBaseSensor device, double sensorValue) {
        SensorDataElement nextSensorDataElement = device.getNextSensorDataElement();
        nextSensorDataElement.setValueType(String.valueOf(sensorValue));
        return new SensorValueJsonConverter().toJson(nextSensorDataElement);
    }


}

