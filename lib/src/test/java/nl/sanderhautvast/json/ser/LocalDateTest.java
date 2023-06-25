package nl.sanderhautvast.json.ser;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LocalDateTest {

    @Test
    void testLocalDate() {
        assertEquals("\"2023-10-11\"", Mapper.json(LocalDate.of(2023, 10, 11)));
    }

    @Test
    void testLocalDateTime() {
        assertEquals("\"2023-10-11T13:15:27\"", Mapper.json(LocalDateTime.of(2023, 10, 11,13,15,27)));
    }
}
