package nl.sanderhautvast.json.ser.primitives;

import nl.sanderhautvast.json.ser.Mapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class NullPropertyTest {

    @Test
    public void testProperty() {
        Bean object = new Bean();
        assertEquals("{\"data\":null}", Mapper.json(object));
    }

    @SuppressWarnings("unused")
    public static class Bean {
        private String data;

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }
    }
}
