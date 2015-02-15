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

package org.vaadin.tinkerforge.modules.communication.mqtt;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Supplier;

/**
 * Created by Sven Ruppert on 04.05.2014.
 */
public class MqttBuffer {

  static final ExecutorService fixedThreadPool = Executors.newFixedThreadPool(4); //TODO dynamisch

  private String topic;
  private MqttClient client;
  private int qos = 1;
  private boolean retained = true;

  public MqttBuffer topic(String s) {
    this.topic = s;
    return this;
  }

  public MqttBuffer client(MqttClient c) {
    this.client = c;
    return this;
  }

  public MqttBuffer qos(int q) {
    this.qos = q;
    return this;
  }
  public MqttBuffer retained(boolean b) {
    this.retained = b;
    return this;
  }

  public void sendAsync(String msg) {
    Supplier<String> task = () -> {
      try {
        client.publish(topic, (msg).getBytes("UTF-8"), qos, retained);
      } catch (MqttException
          | UnsupportedEncodingException e) {
        e.printStackTrace();
      }
      return "Done - " + msg;
    };
    CompletableFuture.supplyAsync(task, fixedThreadPool)
        .thenAccept(System.out::println);
  }
}
