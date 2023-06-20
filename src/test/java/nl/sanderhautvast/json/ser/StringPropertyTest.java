package nl.sanderhautvast.json.ser;

import lombok.Data;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class StringPropertyTest {

    @Test
    public void stringValue() {
        assertEquals("\"value\"", Mapper.json("value"));
    }

    @Test
    public void stringProperty() {
        Bean object = new Bean();
        object.setData1("value1");
        object.setData2("value2");
        assertEquals("{\"data1\":\"value1\",\"data2\":\"value2\"}", Mapper.json(object));
    }

    @Test
    public void stringPropertyNull() {
        Bean object = new Bean();
        object.setData1("value1");
        object.setData2(null);
        assertEquals("{\"data1\":\"value1\",\"data2\":null}", Mapper.json(object));
    }

    @Data
    public static class Bean {
        private String data1;
        private String data2;
    }
}
