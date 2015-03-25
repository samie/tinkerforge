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

package org.rapidpm.module.iot.tinkerforge.data;

/**
 * Created by Sven Ruppert on 16.04.2014.
 */
public class DeviceIdentity {


  private String uid;
  private String connectedUid;
  private char position;
  private short[] hardwareVersion;
  private short[] firmwareVersion;
  private int deviceIdentifier;

  public DeviceIdentity(String uid, String connectedUid, char position, short[] hardwareVersion, short[] firmwareVersion, int deviceIdentifier) {

    this.uid = uid;
    this.connectedUid = connectedUid;
    this.position = position;
    this.hardwareVersion = hardwareVersion;
    this.firmwareVersion = firmwareVersion;
    this.deviceIdentifier = deviceIdentifier;
  }

  public String getUid() {
    return uid;
  }

  public void setUid(String uid) {
    this.uid = uid;
  }

  public String getConnectedUid() {
    return connectedUid;
  }

  public void setConnectedUid(String connectedUid) {
    this.connectedUid = connectedUid;
  }

  public char getPosition() {
    return position;
  }

  public void setPosition(char position) {
    this.position = position;
  }

  public short[] getHardwareVersion() {
    return hardwareVersion;
  }

  public void setHardwareVersion(short[] hardwareVersion) {
    this.hardwareVersion = hardwareVersion;
  }

  public short[] getFirmwareVersion() {
    return firmwareVersion;
  }

  public void setFirmwareVersion(short[] firmwareVersion) {
    this.firmwareVersion = firmwareVersion;
  }

  public int getDeviceIdentifier() {
    return deviceIdentifier;
  }

  public void setDeviceIdentifier(int deviceIdentifier) {
    this.deviceIdentifier = deviceIdentifier;
  }
}
