package nl.sanderhautvast.json.ser.collections;


import nl.sanderhautvast.json.ser.Mapper;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ListTest {

    @Test
    void testEmpty() {
        assertEquals("[]", Mapper.json(List.of()));
    }

    @Test
    void testValue() {
        assertEquals("[\"value1\",\"value2\"]", Mapper.json(List.of("value1", "value2")));
    }

    @Test
    void testPropertyValue() {
        Bean object = new Bean();
        object.setList(List.of("value1", "value2"));
        assertEquals("{\"list\":[\"value1\",\"value2\"]}", Mapper.json((object)));
    }

    public static class Bean {
        private List<String> list;

        public List<String> getList() {
            return list;
        }

        public void setList(List<String> list) {
            this.list = list;
        }
    }

}
