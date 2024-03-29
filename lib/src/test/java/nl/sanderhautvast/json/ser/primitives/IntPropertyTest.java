package nl.sanderhautvast.json.ser.primitives;

import nl.sanderhautvast.json.ser.Mapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class IntPropertyTest {

    @Test
    public void testPrimitive() {
        assertEquals("-55", Mapper.json(-55));
    }

    @Test
    public void testWrapper() {
        assertEquals("55", Mapper.json(Integer.valueOf("55")));
    }

    @Test
    public void testPropertyValue() {
        Bean object = new Bean();
        object.setData(1);
        assertEquals("{\"data\":1}", Mapper.json(object));
    }

    @SuppressWarnings("unused")
    public static class Bean {
        int data;

        public int getData() {
            return data;
        }

        public void setData(int data) {
            this.data = data;
        }
    }
}
