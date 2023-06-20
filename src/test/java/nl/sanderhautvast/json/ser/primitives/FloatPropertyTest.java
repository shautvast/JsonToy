package nl.sanderhautvast.json.ser.primitives;

import lombok.Data;
import nl.sanderhautvast.json.ser.Mapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FloatPropertyTest {
    @Test
    public void testPrimitive() {
        assertEquals("-55.6", Mapper.json(-55.6F));
    }

    @Test
    public void testWrapper() {
        assertEquals("55.0", Mapper.json(Float.valueOf("55.0")));
    }

    @Test
    public void testProperty() {
        Bean object = new Bean();
        object.setData(1F);
        assertEquals("{\"data\":1.0}", Mapper.json(object));
    }

    @Data
    public static class Bean {
        private float data;
    }
}
