package nl.sanderhautvast.json.ser.primitives;

import nl.sanderhautvast.json.ser.Mapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BytePropertyTest {
    @Test
    public void testPrimitive() {
        assertEquals("-55", Mapper.json((byte) -55));
    }

    @Test
    public void testWrapper() {
        assertEquals("55", Mapper.json(Byte.valueOf((byte) 55)));
    }

    @Test
    public void testProperty() {
        Bean object = new Bean();
        object.setData((byte) 1);
        assertEquals("{\"data\":1}", Mapper.json(object));
    }

    public static class Bean {
        private byte data;

        public byte getData() {
            return data;
        }

        public void setData(byte data) {
            this.data = data;
        }
    }
}
