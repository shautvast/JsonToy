package nl.sanderhautvast.json.ser.collections;


import nl.sanderhautvast.json.ser.Mapper;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class MapTest {
    @Test
    public void testValue() {
        String jsonString = Mapper.json(Map.of("key1", "value1", "key2", "value2"));
        assertTrue("{\"key1\":\"value1\",\"key2\":\"value2\"}".equals(jsonString)
                || "{\"key2\":\"value2\",\"key1\":\"value1\"}".equals(jsonString), jsonString); //order doesn't matter
    }

    @Test
    public void testIntegerKeys() {
        String jsonString = Mapper.json(Map.of(1, "value1", 2, "value2"));
        assertTrue("{\"1\":\"value1\",\"2\":\"value2\"}".equals(jsonString)
                || "{\"2\":\"value2\",\"1\":\"value1\"}".equals(jsonString), jsonString); //order doesn't matter
    }

    @Test
    void testEmpty(){
        assertEquals("{}", Mapper.json(Map.of()));
    }

    @Test
    public void testPropertyValue() {
        Bean object = new Bean();
        object.setMap(Map.of("key1", "value1", "key2", "value2"));
        String jsonString = Mapper.json(object);
        assertTrue("{\"map\":{\"key1\":\"value1\",\"key2\":\"value2\"}}".equals(jsonString)
                || "{\"map\":{\"key2\":\"value2\",\"key1\":\"value1\"}}".equals(jsonString), jsonString);
    }

    public static class Bean {
        private Map<String, String> map;

        @SuppressWarnings("unused")
        public Map<String, String> getMap() {
            return map;
        }

        public void setMap(Map<String, String> map) {
            this.map = map;
        }
    }
}
