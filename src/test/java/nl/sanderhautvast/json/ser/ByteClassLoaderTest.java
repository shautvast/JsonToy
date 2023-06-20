package nl.sanderhautvast.json.ser;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class ByteClassLoaderTest {

    @Test
    public void foo() {
        assertThrows(ClassNotFoundException.class, () -> new ByteClassLoader().findClass("WrongName"));
    }
}