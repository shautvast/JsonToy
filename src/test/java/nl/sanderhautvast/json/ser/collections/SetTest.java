package nl.sanderhautvast.json.ser.collections;

import lombok.Data;
import nl.sanderhautvast.json.ser.Mapper;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SetTest {

    @Test
    void testEmpty(){
        assertEquals("[]", Mapper.json(Set.of()));
    }
    @Test
    public void testValue() {
        String jsonString = Mapper.json(Set.of("value1", "value2"));
        assertTrue("[\"value2\",\"value1\"]".equals(jsonString)
                || "[\"value1\",\"value2\"]".equals(jsonString), jsonString);
    }

    @Test
    public void testPropertyValue() {
        Bean object = new Bean();
        object.setSet(Set.of("value1", "value2"));
        String jsonString = Mapper.json(object);
        assertTrue("{\"set\":[\"value2\",\"value1\"]}".equals(jsonString)
                || "{\"set\":[\"value1\",\"value2\"]}".equals(jsonString));
    }

    @Data
    public static class Bean {
        private Set<String> set;
    }
}
