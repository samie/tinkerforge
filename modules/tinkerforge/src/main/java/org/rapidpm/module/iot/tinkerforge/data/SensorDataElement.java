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

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Created by Sven Ruppert on 16.02.14.
 */
public class SensorDataElement  {

  private String masterUID;      //master abc
  private String brickletUID;    // sensor xyz
  private String brickletType;   // Temperatur
  private LocalDateTime date;
  private String valueType;  //for multiValue Elements
  private double sensorValue;

  public String getMasterUID() {
    return masterUID;
  }

  public void setMasterUID(String masterUID) {
    this.masterUID = masterUID;
  }

  public String getBrickletUID() {
    return brickletUID;
  }

  public void setBrickletUID(String brickletUID) {
    this.brickletUID = brickletUID;
  }

  public String getBrickletType() {
    return brickletType;
  }

  public void setBrickletType(String brickletType) {
    this.brickletType = brickletType;
  }

  public LocalDateTime getDate() {
    return date;
  }

  public void setDate(LocalDateTime date) {
    this.date = date;
  }

  public double getSensorValue() {
    return sensorValue;
  }

  public void setSensorValue(double sensorValue) {
    this.sensorValue = sensorValue;
  }

  public String getValueType() {
    return valueType;
  }

  public void setValueType(String valueType) {
    this.valueType = valueType;
  }

  @Override
  public int hashCode() {
    return Objects.hash(masterUID, brickletUID, brickletType, date, valueType, sensorValue);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    final SensorDataElement other = (SensorDataElement) obj;
    return Objects.equals(this.masterUID, other.masterUID)
        && Objects.equals(this.brickletUID, other.brickletUID)
        && Objects.equals(this.brickletType, other.brickletType)
        && Objects.equals(this.date, other.date)
        && Objects.equals(this.valueType, other.valueType)
        && Objects.equals(this.sensorValue, other.sensorValue);
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("SensorDataElement{");
    sb.append("masterUID='").append(masterUID).append('\'');
    sb.append(", brickletUID='").append(brickletUID).append('\'');
    sb.append(", brickletType='").append(brickletType).append('\'');
    sb.append(", date=").append(date);
    sb.append(", valueType=").append(valueType);
    sb.append(", sensorValue=").append(sensorValue);
    sb.append('}');
    return sb.toString();
  }
}
