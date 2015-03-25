package junit.org.vaadin.model.converter.json;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.rapidpm.module.iot.tinkerforge.data.SensorDataElement;
import org.vaadin.model.SensorValue;
import org.vaadin.model.converter.json.SensorValueJsonConverter;

import java.time.LocalDateTime;

import static org.junit.Assert.*;

public class SensorValueJsonConverterTest {

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testFromToJson01() throws Exception {
        final SensorValueJsonConverter converter = new SensorValueJsonConverter();

        SensorDataElement value = new SensorDataElement();
        value.setSensorValue(1.0);
        value.setMasterUID("ARARA");
        value.setDate(LocalDateTime.now());
        check(converter, value);
    }



    private void check(SensorValueJsonConverter converter, SensorDataElement value) {
        String s = converter.toJson(value);
        assertNotNull(s);
        assertFalse(s.isEmpty());
        System.out.println("s = " + s);
        SensorDataElement fromJson = converter.fromJson(s);
        assertNotNull(fromJson);
        assertTrue(fromJson.equals(value));
    }
}