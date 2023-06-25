package nl.sanderhautvast.json.ser.collections;


import nl.sanderhautvast.json.ser.Mapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ArrayTest {
    @Test
    public void testValue() {
        assertEquals("[\"value1\",\"value2\"]", Mapper.json(new String[]{"value1", "value2"}));
    }

    @Test
    public void testEmpty() {
        assertEquals("[]", Mapper.json(new String[]{}));
    }

    @Test
    public void testPropertyValue() {
        Bean object = new Bean();
        object.setArray(new String[]{"value1", "value2"});
        assertEquals("{\"array\":[\"value1\",\"value2\"]}", Mapper.json(object));
    }

    public static class Bean {
        private String[] array;

        @SuppressWarnings("unused")
        public String[] getArray() {
            return array;
        }

        public void setArray(String[] array) {
            this.array = array;
        }
    }

}
