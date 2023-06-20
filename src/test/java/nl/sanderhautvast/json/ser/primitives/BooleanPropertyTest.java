package nl.sanderhautvast.json.ser.primitives;

import lombok.Data;
import nl.sanderhautvast.json.ser.Mapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BooleanPropertyTest {

    @Test
    public void testPrimitive() {
        assertEquals("true", Mapper.json(true));
    }

    @Test
    public void testWrapper() {
        assertEquals("true", Mapper.json(Boolean.TRUE));
    }

    @Test
    public void testProperty() {
        Bean object = new Bean();
        object.setData(true);
        assertEquals("{\"data\":true}", Mapper.json(object));
    }

    @Data
    public static class Bean {
        boolean data;
    }
}
