package junit.org.vaadin.model.converter.json;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
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
        LocalDateTime now = LocalDateTime.now();

        SensorValue value = SensorValue.newBuilder()
                .localDateTime(now)
                .deviceType("type")
                .rawValue(0)
                .build();
        check(converter, value);
    }
    @Test
    public void testFromToJson02() throws Exception {
        final SensorValueJsonConverter converter = new SensorValueJsonConverter();
        LocalDateTime now = LocalDateTime.now();

        SensorValue value = SensorValue.newBuilder()
                .localDateTime(now)
                .deviceType("type")
                .rawValue(-1)
                .build();
        check(converter, value);
    }
    @Test
    public void testFromToJson03() throws Exception {
        final SensorValueJsonConverter converter = new SensorValueJsonConverter();
        LocalDateTime now = LocalDateTime.now();

        SensorValue value = SensorValue.newBuilder()
                .localDateTime(now)
                .deviceType("type")
                .rawValue(Integer.MIN_VALUE)
                .build();
        check(converter, value);
    }
    @Test
    public void testFromToJson04() throws Exception {
        final SensorValueJsonConverter converter = new SensorValueJsonConverter();
        LocalDateTime now = LocalDateTime.now();

        SensorValue value = SensorValue.newBuilder()
                .localDateTime(now)
                .deviceType("")
                .rawValue(Integer.MIN_VALUE)
                .build();
        check(converter, value);
    }


    private void check(SensorValueJsonConverter converter, SensorValue value) {
        String s = converter.toJson(value);
        assertNotNull(s);
        assertFalse(s.isEmpty());
        System.out.println("s = " + s);
        SensorValue fromJson = converter.fromJson(s);
        assertNotNull(fromJson);
        assertTrue(fromJson.equals(value));
    }
}