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

package org.rapidpm.module.iot.tinkerforge.sensor;

import com.tinkerforge.*;
import org.rapidpm.module.iot.tinkerforge.data.SensorDataElement;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * Created by Sven Ruppert on 14.09.2014.
 */
public abstract class TinkerForgeBaseSensor<T extends Device> implements Runnable {


  protected String UID;
  protected int callbackPeriod;
  protected int port;
  protected String host;
  protected IPConnection ipcon;
  public T bricklet;

  public String masterUID;
  public String brickletUID;
  public String brickletType;

  public TinkerForgeBaseSensor(final String UID, int callbackPeriod, IPConnection ipcon) {
    this.UID = UID;
    this.callbackPeriod = callbackPeriod;
      this.ipcon = ipcon;
    connectBricklet();
  }

  public abstract void initBricklet();

  protected abstract void connectBricklet();

  @Override
  public void run() {
    try {
      masterUID = bricklet.getIdentity().connectedUid;
      brickletUID = bricklet.getIdentity().uid;
      brickletType = bricklet.getIdentity().deviceIdentifier + "";
      initBricklet();
    } catch (TimeoutException
        | NotConnectedException e) {
      e.printStackTrace();
    }
  }

  public SensorDataElement getNextSensorDataElement() {
    final SensorDataElement data = new SensorDataElement();
    data.setMasterUID(masterUID);
    data.setBrickletUID(brickletUID);
    data.setBrickletType(brickletType);
    data.setDate(LocalDateTime.now());
    return data;
  }


  public void disconnect() {
    try {
      ipcon.setAutoReconnect(false);
      ipcon.disconnect();
    } catch (NotConnectedException e) {
      e.printStackTrace();
    }
  }

  public void connect() {
    try {
      ipcon.setAutoReconnect(true);
      ipcon.connect(host, port);
    } catch (IOException | AlreadyConnectedException e) {
      e.printStackTrace();
    }
  }

  public void setIpcon(IPConnection ipcon) {
    this.ipcon = ipcon;
  }
}
