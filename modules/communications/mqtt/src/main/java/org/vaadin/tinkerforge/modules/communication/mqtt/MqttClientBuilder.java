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
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.eclipse.paho.client.mqttv3.persist.MqttDefaultFilePersistence;

import java.util.UUID;

/**
 * Created by Sven Ruppert on 03.05.2014.
 */
public class MqttClientBuilder {

  private String uri;
  private String clientUID;
  private boolean memoryPersistence = true;
  private boolean filePersistence = false;

  public MqttClient build(){
    MqttClient client;
    try {
      if(memoryPersistence){
        client = new MqttClient( uri,clientUID, new MemoryPersistence() );
      } else{
        client = new MqttClient( uri,clientUID, new MqttDefaultFilePersistence());
      }
    } catch (MqttException e) {
      e.printStackTrace();
      client = null;
    }
    return client;
  }

  public MqttClientBuilder uri(String s) {
    this.uri = s;
    return this;
  }

  public MqttClientBuilder clientUID(String s) {
    this.clientUID = s;
    return this;
  }
  public MqttClientBuilder clientUIDGenerated() {
      this.clientUID = UUID.randomUUID().toString().replace("-", "").substring(0, 22);
    System.out.println("clientUID = " + clientUID);
    return this;
  }

  public MqttClientBuilder memoryPersistence(boolean b) {
    this.memoryPersistence = b;
    this.filePersistence = !this.memoryPersistence;
    return this;
  }

  public MqttClientBuilder filePersistence(boolean b) {
    this.filePersistence = b;
    this.memoryPersistence = !this.filePersistence;
    return this;
  }
}
