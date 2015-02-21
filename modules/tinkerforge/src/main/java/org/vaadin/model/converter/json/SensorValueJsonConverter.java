package org.vaadin.model.converter.json;

import com.google.gson.Gson;
import org.vaadin.model.SensorValue;

/**
 * Created by sven on 19.02.15.
 */
public class SensorValueJsonConverter {


    public SensorValue fromJson(String json) {
        final Gson gson = new Gson();
        return gson.fromJson(json, SensorValue.class);
    }

    public String toJson(SensorValue sensorValue) {
        Gson gson = new Gson();
        return gson.toJson(sensorValue);
    }


}
