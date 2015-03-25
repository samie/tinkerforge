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

package org.rapidpm.module.iot.tinkerforge.sensor.multivalue.color;

import com.tinkerforge.BrickletColor;
import com.tinkerforge.IPConnection;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;
import org.rapidpm.module.iot.tinkerforge.sensor.multivalue.TinkerForgeSensorMultiValue;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sven Ruppert on 14.09.2014.
 */
public class Color extends TinkerForgeSensorMultiValue<BrickletColor> {


    public Color(String UID, int callbackPeriod, IPConnection ipcon) {
        super(UID, callbackPeriod, ipcon);
    }

    @Override
  public void initBricklet() {
    try {
      bricklet.setColorCallbackPeriod(callbackPeriod);
      bricklet.setColorTemperatureCallbackPeriod(callbackPeriod);
      bricklet.setIlluminanceCallbackPeriod(callbackPeriod);

      bricklet.addColorListener(this::executeColor);
      bricklet.addColorTemperatureListener(this::executeColorTemp);
      bricklet.addIlluminanceListener(this::executeIlluminance);

    } catch (TimeoutException | NotConnectedException e) {
      e.printStackTrace();
    }
  }

  private void executeIlluminance(long l) {
    illuminanceActionList.forEach(v -> v.execute(l));
  }

  private void executeColorTemp(int i) {
    colorTempActionList.forEach(v -> v.execute(i));
  }

  private void executeColor(int r, int g, int b, int c) {
    colorActionList.forEach(v -> v.execute(r, g, b, c));
  }

  @Override
  protected void connectBricklet() {
    bricklet = new BrickletColor(UID, ipcon);
  }


  private List<IlluminanceAction> illuminanceActionList = new ArrayList<>();
  private List<ColorTempAction> colorTempActionList = new ArrayList<>();
  private List<ColorAction> colorActionList = new ArrayList<>();


  public void addColorAction(ColorAction action) {
    colorActionList.add(action);
  }
  public void clearColorActionList() {
    colorActionList.clear();
  }


  public void addColorTempAction(ColorTempAction action) {
    colorTempActionList.add(action);
  }
  public void clearColorTempActionList() {
    colorTempActionList.clear();
  }


  public void addIlluminanceAction(IlluminanceAction action) {
    illuminanceActionList.add(action);
  }
  public void clearIlluminanceActionList() {
    illuminanceActionList.clear();
  }


}
