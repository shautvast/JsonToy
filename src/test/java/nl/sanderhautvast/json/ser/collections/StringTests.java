package nl.sanderhautvast.json.ser.collections;

import com.fasterxml.jackson.core.JsonProcessingException;
import nl.sanderhautvast.json.ser.Mapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StringTests {

    @Test
    void testCharEscapes() {
        assertEquals("\"\\t\"", Mapper.json('\t'));
        assertEquals("\"\\b\"", Mapper.json('\b'));
        assertEquals("\"\\r\"", Mapper.json('\r'));
        assertEquals("\"\\n\"", Mapper.json('\n'));
        assertEquals("\"\\\"\"", Mapper.json('\"'));
        assertEquals("\"\\\\\"", Mapper.json('\\'));
        assertEquals("\"\\f\"", Mapper.json('\f'));
        assertEquals("\"\\/\"", Mapper.json('/'));
        assertEquals("\"'\"", Mapper.json('\''));
    }

    @Test
    void testStringEscapes() {
        assertEquals("\"bla\\tbla\"", Mapper.json("bla\tbla"));
        assertEquals("\"\\b\\b\"", Mapper.json("\b\b"));
        assertEquals("\"\\r\\n\"", Mapper.json("\r\n"));
        assertEquals("\"\\n\"", Mapper.json("\n"));
        assertEquals("\"\\\"\"", Mapper.json("\""));
        assertEquals("\"\\\\\"", Mapper.json("\\"));
        assertEquals("\"\\f\"", Mapper.json("\f"));
        assertEquals("\"\\/\"", Mapper.json("/"));
        assertEquals("\"'\"", Mapper.json("'"));
    }

    @Test
    void unicodeTests() {
        assertEquals("\"angry=😠\"", Mapper.json("angry=😠"));
        assertEquals("\"\\u20A7dings\"", Mapper.json("₧dings"));
        assertEquals("\"狗=dog\"", Mapper.json("狗=dog")); // dog
    }

}
