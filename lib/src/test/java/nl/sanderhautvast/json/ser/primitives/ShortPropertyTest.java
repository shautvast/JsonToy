package nl.sanderhautvast.json.ser.primitives;

import nl.sanderhautvast.json.ser.Mapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ShortPropertyTest {
    @Test
    public void testPrimitive() {
        assertEquals("-55", Mapper.json((short) -55));
    }

    @Test
    public void testWrapper() {
        assertEquals("5", Mapper.json(Short.valueOf("5")));
    }

    @Test
    public void testProperty() {
        Bean object = new Bean();
        object.setData((short) 3);
        assertEquals("{\"data\":3}", Mapper.json(object));
    }

    public static class Bean {
        private short data;
        @SuppressWarnings("unused")
        public short getData() {
            return data;
        }

        public void setData(short data) {
            this.data = data;
        }
    }
}
