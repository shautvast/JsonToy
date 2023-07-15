package nl.sanderhautvast.json.ser;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.temporal.Temporal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


/**
 * Mapper.json(StringBuilder b, ...)
 * TODO write to outputstream
 */
public class Mapper {
    private static final Map<Class<?>, BaseMapper<?>> mappers = new ConcurrentHashMap<>();

    private static final ByteClassLoader generatedClassesLoader = new ByteClassLoader();
    public static final char[] TAB = {'\\', 't'};
    public static final char[] DOUBLEQUOTE = {'\\', '\"'};
    public static final char[] SLASH = {'\\', '/'};
    public static final char[] RETURN = {'\\', 'r'};
    public static final char[] BACKSLASH = {'\\', '\\'};
    public static final char[] NEWLINE = {'\\', 'n'};
    public static final char[] BELL = {'\\', 'b'};
    public static final char[] FORMFEED = {'\\', 'f'};
    private static final char[][] MAP = createEscapeMap();


    /**
     * Add a new (custom) mapper implementation for the specified type
     *
     * @param type   The class to serialize to json
     * @param mapper the Mapper implementation
     */
    @SuppressWarnings("unused")
    public static <T> void addMapper(Class<T> type, BaseMapper<T> mapper) {
        mappers.put(type, mapper);
    }

    /**
     * returns the json representation of the value as a String
     */
    public static String json(Object value) {
        StringBuilder b = new StringBuilder(128);
        json(b, value);
        return b.toString();
    }

    @SuppressWarnings({"unchecked", "rawtypes", "UnnecessaryToStringCall"})
    public static void json(StringBuilder b, Object value) {
        if (value == null) {
            b.append("null");
        } else {
            Class<?> type = value.getClass();
            if (type.isArray()) {
                array(b, value);
            } else if (value instanceof Collection) {
                list(b, (Collection) value);
            } else if (value instanceof Map) {
                object(b, (Map) value);
            } else {
                if (type == String.class) {
                    b.append("\"");
                    Mapper.escape(b, (String) value);
                    b.append("\"");
                } else if (type == Character.class) {
                    b.append("\"");
                    Mapper.escape(b, (Character) value);
                    b.append("\"");
                } else if (type == UUID.class || value instanceof Temporal || type.isEnum()) {
                    b.append("\"");
                    b.append(value.toString());
                    b.append("\"");
                } else if (type == Boolean.class
                        || type == Integer.class
                        || type == Long.class
                        || type == Float.class
                        || type == Double.class
                        || type == Byte.class
                        || type == Short.class
                        || type == BigInteger.class
                        || type == BigDecimal.class
                ) {
                    b.append(value.toString()); // prevents another nullcheck
                } else {
                    BaseMapper mapper = mappers.computeIfAbsent(type, key -> createObjectMapper(type));
                    mapper.json(b, value);
                }
            }
        }
    }

    //TODO make this more performant
    private static void array(StringBuilder b, Object value) {
        b.append("[");
        StringJoiner joiner = new StringJoiner(",");
        for (int i = 0; i < Array.getLength(value); i++) {
            Object arrayElement = Array.get(value, i);
            joiner.add(Mapper.json(arrayElement)); // recursie
        }
        b.append(joiner);
        b.append("]");
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static void list(StringBuilder b, Collection list) {
        if (list.isEmpty()) {
            b.append("[]");
        } else {
            Object first = list.iterator().next();
            b.append("[");
            Mapper.json(b, first);
            list.stream().skip(1)
                    .forEach(element -> {
                        b.append(",");
                        Mapper.json(b, element);
                    });
            b.append("]");
        }
    }


    @SuppressWarnings({"rawtypes", "unchecked"})
    private static void object(StringBuilder b, Map map) {
        if (map.isEmpty()) {
            b.append("{}");
        } else {
            b.append("{\"");
            Set<Map.Entry> entries = map.entrySet();
            Map.Entry first = entries.iterator().next();
            Object key = first.getKey();
            if (key instanceof String) {
                escape(b, (String) key);
            } else if (key instanceof Character) {
                escape(b, (Character) key);
            } else {
                b.append(key);
            }
            b.append("\":");
            Mapper.json(b, first.getValue());
            entries.stream().skip(1)
                    .forEach(entry -> {
                        b.append(",\"");
                        b.append(entry.getKey()).append("\":");
                        Mapper.json(b, entry.getValue());
                    });
            b.append("}");
        }
    }

    public static void json(StringBuilder b, byte value) {
        b.append(value);
    }

    public static void json(StringBuilder b, boolean value) {
        b.append(value);
    }

    public static void json(StringBuilder b, short value) {
        b.append(value);
    }

    public static void json(StringBuilder b, int value) {
        b.append(value);
    }

    public static void json(StringBuilder b, long value) {
        b.append(value);
    }

    public static void json(StringBuilder b, char value) {
        b.append("\"");
        escape(b, value);
        b.append("\"");
    }

    public static void json(StringBuilder b, float value) {
        b.append(value);
    }

    public static void json(StringBuilder b, double value) {
        b.append(value);
    }

    @SuppressWarnings("unchecked")
    private static <T> BaseMapper<T> createObjectMapper(Class<T> forType) {
        try {
            ClassReader cr = new ClassReader(forType.getName());
            MapperFactory mapperFactory = new MapperFactory();
            cr.accept(mapperFactory, 0);
            ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES);

            mapperFactory.classNode.accept(classWriter);
            byte[] byteArray = classWriter.toByteArray();
            generatedClassesLoader.addClass(mapperFactory.classNode.name, byteArray);
            return (BaseMapper<T>) generatedClassesLoader.loadClass(mapperFactory.classNode.name).getConstructor().newInstance();
        } catch (IOException | ClassNotFoundException | NoSuchMethodException | InstantiationException |
                 IllegalAccessException | InvocationTargetException e) {
            throw new JsonError(e);
        }
    }

    static void escape(StringBuilder b, char c) {
        escape(b, String.valueOf(c));
    }

    private static char[][] createEscapeMap() {
        char[][] charmap = new char[0x2100][];
        for (int i = 0; i < 0x2100; i++) {
            char c = (char) i;
            char[] replacement;
            if (c == '\t') {
                replacement = TAB;
            } else if (c == '\"') {
                replacement = DOUBLEQUOTE;
            } else if (c == '/') {
                replacement = SLASH;
            } else if (c == '\r') {
                replacement = RETURN;
            } else if (c == '\\') {
                replacement = BACKSLASH;
            } else if (c == '\n') {
                replacement = NEWLINE;
            } else if (c == '\b') {
                replacement = BELL;
            } else if (c == '\f') {
                replacement = FORMFEED;
            } else if ((c <= '\u001F') || (c >= '\u007F' && c <= '\u009F') || (c >= '\u2000')) {
                replacement = new char[6];
                replacement[0] = '\\';
                replacement[1] = 'u';

                String hex = Integer.toHexString(c).toUpperCase();
                int hexlen = hex.length();
                for (int k = 0; k < 4 - hexlen; k++) {
                    replacement[k + 2] = '0';
                }
                for (int k = 0; k < hexlen; k++) {
                    replacement[6 - hexlen + k] = hex.charAt(k);
                }
            } else {
                replacement = new char[]{c};
            }
            charmap[i] = replacement;
        }

        return charmap;
    }

    //both methods are equally slow it seems
    //mainly because we can't do a batch copy into the stringbuilder and have to map every character individually
    static void escape(StringBuilder b, String value) {
        for (int i = 0; i < value.length(); i++) {
            int c = value.charAt(i);

            if (c < 0x2100) {
                b.append(MAP[c]);
            } else {
                b.append((char) c);
            }
        }
    }

}



