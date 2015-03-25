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

package org.rapidpm.module.iot.tinkerforge.sensor.multivalue;

import com.tinkerforge.Device;
import com.tinkerforge.IPConnection;
import org.rapidpm.module.iot.tinkerforge.sensor.TinkerForgeBaseSensor;

/**
 * Created by Sven Ruppert on 14.09.2014.
 */
public abstract class TinkerForgeSensorMultiValue<T extends Device> extends TinkerForgeBaseSensor<T> {
    public TinkerForgeSensorMultiValue(String UID, int callbackPeriod, IPConnection ipcon) {
        super(UID, callbackPeriod, ipcon);
    }


    //evtl mal mehr...


}
