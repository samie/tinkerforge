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

import com.tinkerforge.AlreadyConnectedException;
import com.tinkerforge.IPConnection;
import com.tinkerforge.NotConnectedException;
import org.rapidpm.module.iot.tinkerforge.data.DeviceIdentity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sven Ruppert on 16.04.2014.
 */
public class BrickletReader {
  private final static int DEFAULT_PORT = 4223;

  public List<DeviceIdentity> findBricklets(String host) {
    final List<DeviceIdentity> bricklets = new ArrayList<>();

    IPConnection ipcon = new IPConnection();
    try {
      ipcon.connect(host, DEFAULT_PORT);

      // Register enumerate listener and print incoming information
      ipcon.addEnumerateListener((uid, connectedUid, position, hardwareVersion, firmwareVersion, deviceIdentifier, enumerationType) -> {

            System.out.println("UID:               " + uid);
            System.out.println("Enumeration Type:  " + enumerationType);

            if (enumerationType == IPConnection.ENUMERATION_TYPE_DISCONNECTED) {
              System.out.println("");
              return;
            }

            DeviceIdentity identity = new DeviceIdentity(uid, connectedUid, position,
                hardwareVersion, firmwareVersion, deviceIdentifier);
            bricklets.add(identity);

            System.out.println("Connected UID:     " + connectedUid);
            System.out.println("Position:          " + position);
            short nameA = hardwareVersion[0];
            short nameB = hardwareVersion[1];
            short nameC = hardwareVersion[2];
            System.out.println("Hardware Version:  " + nameA + "." + nameB + "." + nameC);
            System.out.println("Firmware Version:  " + firmwareVersion[0] + "." + firmwareVersion[1] + "." + firmwareVersion[2]);
            System.out.println("Device Identifier: " + deviceIdentifier);
            System.out.println("");
          }
      );

      ipcon.enumerate();
      Thread.sleep(2000l);
      ipcon.disconnect();
    } catch (IOException
        | AlreadyConnectedException
        | NotConnectedException
        | InterruptedException e) {
      e.printStackTrace();
    }

    return bricklets;
  }
}
