package nl.sanderhautvast.json.ser.primitives;

import lombok.Data;
import nl.sanderhautvast.json.ser.Mapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DoublePropertyTest {
    @Test
    public void testPrimitive() {
        assertEquals("-55.6", Mapper.json(-55.6D));
    }

    @Test
    public void testWrapper() {
        assertEquals("55.0", Mapper.json(Double.valueOf("55.0")));
    }

    @Test
    public void testProperty() {
        Bean object = new Bean();
        object.setData(326.2D);
        assertEquals("{\"data\":326.2}", Mapper.json(object));
    }

    @Data
    public static class Bean {
        private double data;
    }
}
