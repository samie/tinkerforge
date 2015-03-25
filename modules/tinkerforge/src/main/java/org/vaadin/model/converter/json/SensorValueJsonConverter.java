package org.vaadin.model.converter.json;

import com.google.gson.Gson;
import org.rapidpm.module.iot.tinkerforge.data.SensorDataElement;

/**
 * Created by sven on 19.02.15.
 */
public class SensorValueJsonConverter {


    public SensorDataElement fromJson(String json) {
        final Gson gson = new Gson();
        return gson.fromJson(json, SensorDataElement.class);
    }

    public String toJson(SensorDataElement sensorValue) {
        Gson gson = new Gson();
        return gson.toJson(sensorValue);
    }


}
