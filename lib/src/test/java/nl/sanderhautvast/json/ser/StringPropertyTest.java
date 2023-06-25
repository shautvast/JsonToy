package nl.sanderhautvast.json.ser;

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

    @SuppressWarnings("unused")
    public static class Bean {
        private String data1;
        private String data2;

        public String getData1() {
            return data1;
        }

        public void setData1(String data1) {
            this.data1 = data1;
        }

        public String getData2() {
            return data2;
        }

        public void setData2(String data2) {
            this.data2 = data2;
        }
    }
}
