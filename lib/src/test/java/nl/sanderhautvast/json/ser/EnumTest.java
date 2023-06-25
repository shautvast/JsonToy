package nl.sanderhautvast.json.ser;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EnumTest {

    enum Answer {
        YES, NO
    }

    @Test
    void testEnums() {
        assertEquals("\"YES\"", Mapper.json(Answer.YES));
    }
}
