package nl.sanderhautvast.json.ser.primitives;

import nl.sanderhautvast.json.ser.Mapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LongPropertyTest {
    @Test
    public void testPrimitive() {
        assertEquals("-55", Mapper.json(-55L));
    }

    @Test
    public void testWrapper() {
        assertEquals("55", Mapper.json(Long.valueOf("55")));
    }

    @Test
    public void testProperty() {
        Bean object = new Bean();
        object.setData(1L);
        assertEquals("{\"data\":1}", Mapper.json(object));
    }

    public static class Bean {
        private long data;

        public long getData() {
            return data;
        }

        public void setData(long data) {
            this.data = data;
        }
    }
}
