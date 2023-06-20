package nl.sanderhautvast.json.ser.primitives;

import lombok.Data;
import nl.sanderhautvast.json.ser.Mapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CharPropertyTest {
    @Test
    public void testPrimitive() {
        assertEquals("\"d\"", Mapper.json('d'));
    }

    @Test
    public void testWrapper() {
        assertEquals("\"s\"", Mapper.json(Character.valueOf('s')));
    }

    @Test
    public void testProperty() {
        Bean object = new Bean();
        object.setData('a');
        assertEquals("{\"data\":\"a\"}", Mapper.json(object));
    }

    @Data
    public static class Bean {
        private char data;
    }
}
