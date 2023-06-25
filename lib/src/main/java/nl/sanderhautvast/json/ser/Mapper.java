package nl.sanderhautvast.json.ser;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


public class Mapper {
    private static final Map<Class<?>, BaseMapper<?>> mappers = new ConcurrentHashMap<>();

    private static final ByteClassLoader generatedClassesLoader = new ByteClassLoader();
    private static final StringMapper stringMapper = new StringMapper();
    private static final BooleanMapper booleanMapper = new BooleanMapper();

    private static final IntegerMapper integerMapper = new IntegerMapper();
    private static final LongMapper longMapper = new LongMapper();
    private static final ShortMapper shortMapper = new ShortMapper();
    private static final ByteMapper byteMapper = new ByteMapper();
    private static final CharMapper charMapper = new CharMapper();
    private static final FloatMapper floatMapper = new FloatMapper();
    private static final DoubleMapper doubleMapper = new DoubleMapper();


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

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static void json(StringBuilder b, Object value) {
        if (value == null) {
            b.append("null");
        } else {
            Class<?> type = value.getClass();
            if (type.isArray()) {
                if (value instanceof byte[]) {
                    array(b, (byte[]) value);
                } else if (value instanceof int[]) {
                    array(b, (int[]) value);
                } else if (value instanceof short[]) {
                    array(b, (short[]) value);
                } else if (value instanceof boolean[]) {
                    array(b, (boolean[]) value);
                } else if (value instanceof char[]) {
                    array(b, (char[]) value);
                } else if (value instanceof long[]) {
                    array(b, (long[]) value);
                } else if (value instanceof float[]) {
                    array(b, (float[]) value);
                } else if (value instanceof double[]) {
                    array(b, (double[]) value);
                } else {
                    array(b, (Object[]) value);
                }
            } else if (value instanceof Collection) {
                list(b, (Collection) value);
            } else if (value instanceof Map) {
                object(b, (Map) value);
            } else {
                if (type.equals(String.class)) {
                    stringMapper.json(b, (String) value);
                } else if (type.equals(Boolean.class)) {

                    booleanMapper.json(b, (Boolean) value);
                } else if (type.equals(Integer.class)) {
                    integerMapper.json(b, (Integer) value);
                } else if (type.equals(Long.class)) {
                    longMapper.json(b, (Long) value);
                } else if (type.equals(Short.class)) {
                    shortMapper.json(b, (Short) value);
                } else if (type.equals(Byte.class)) {
                    byteMapper.json(b, (Byte) value);
                } else if (type.equals(Character.class)) {
                    charMapper.json(b, (Character) value);
                } else if (type.equals(Float.class)) {
                    floatMapper.json(b, (Float) value);
                } else if (type.equals(Double.class)) {
                    doubleMapper.json(b, (Double) value);
                } else {
                    BaseMapper mapper = mappers.computeIfAbsent(type, key -> createObjectMapper(type));
                    mapper.json(b, value);
                }
            }
        }
    }

    private static void array(StringBuilder b, Object[] array) {
        if (array.length == 0) {
            b.append("[]");
        } else {
            Object first = array[0];
            b.append("[");
            Mapper.json(b, first);
            Arrays.stream(array).skip(1)
                    .forEach(element -> {
                        b.append(",");
                        Mapper.json(b, element);
                    });
            b.append("]");
        }
    }

    private static void array(StringBuilder b, byte[] array) {
        if (array.length == 0) {
            b.append("[]");
        } else {
            byte first = array[0];
            b.append("[");
            json(b, first);
            for (int i = 1; i < array.length; i++) {
                b.append(",");
                json(b, array[i]);
            }
            b.append("]");
        }
    }

    private static void array(StringBuilder b, short[] array) {
        if (array.length == 0) {
            b.append("[]");
        } else {
            short first = array[0];
            b.append("[");
            json(b, first);
            for (int i = 1; i < array.length; i++) {
                b.append(",");
                json(b, array[i]);
            }
            b.append("]");
        }
    }

    private static void array(StringBuilder b, long[] array) {
        if (array.length == 0) {
            b.append("[]");
        } else {
            long first = array[0];
            b.append("[");
            json(b, first);
            for (int i = 1; i < array.length; i++) {
                b.append(",");
                json(b, array[i]);
            }
            b.append("]");
        }
    }

    private static void array(StringBuilder b, boolean[] array) {
        if (array.length == 0) {
            b.append("[]");
        } else {
            boolean first = array[0];
            b.append("[");
            json(b, first);
            for (int i = 1; i < array.length; i++) {
                b.append(",");
                json(b, array[i]);
            }
            b.append("]");
        }
    }

    private static void array(StringBuilder b, double[] array) {
        if (array.length == 0) {
            b.append("[]");
        } else {
            double first = array[0];
            b.append("[");
            json(b, first);
            for (int i = 1; i < array.length; i++) {
                b.append(",");
                json(b, array[i]);
            }
            b.append("]");
        }
    }

    private static void array(StringBuilder b, char[] array) {
        if (array.length == 0) {
            b.append("[]");
        } else {
            char first = array[0];
            b.append("[");
            json(b, first);
            for (int i = 1; i < array.length; i++) {
                b.append(",");
                json(b, array[i]);
            }
            b.append("]");
        }
    }

    private static void array(StringBuilder b, float[] array) {
        if (array.length == 0) {
            b.append("[]");
        } else {
            float first = array[0];
            b.append("[");
            json(b, first);
            for (int i = 1; i < array.length; i++) {
                b.append(",");
                json(b, array[i]);
            }
            b.append("]");
        }
    }

    private static void array(StringBuilder b, int[] array) {
        if (array.length == 0) {
            b.append("[]");
        } else {
            int first = array[0];
            b.append("[");
            json(b, first);
            for (int i = 1; i < array.length; i++) {
                b.append(",");
                json(b, array[i]);
            }
            b.append("]");
        }
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

    static void escape(StringBuilder b, String value) {
        int offset = b.length();
        b.append(value);
        int i = offset;
        while (i < b.length()) {
            char c = b.charAt(i);
            switch (c) {
                case '\t':
                    b.replace(i, i + 1, "\\");
                    b.insert(i + 1, "t");
                    break;
                case '\"':
                    b.replace(i, i + 1, "\\");
                    b.insert(++i, "\"");
                    break;
                case '/':
                    b.replace(i, i + 1, "\\");
                    b.insert(++i, "/");
                    break;
                case '\r':
                    b.replace(i, i + 1, "\\");
                    b.insert(++i, "r");
                    break;
                case '\n':
                    b.replace(i, i + 1, "\\");
                    b.insert(++i, "n");
                    break;
                case '\b':
                    b.replace(i, i + 1, "\\");
                    b.insert(++i, "b");
                    break;
                case '\f':
                    b.replace(i, i + 1, "\\");
                    b.insert(++i, "f");
                    break;
                case '\\':
                    b.replace(i, i + 1, "\\");
                    b.insert(++i, "\\");
                    break;
                case '\'':
                    break;
                default:
                    if ((c <= '\u001F') || (c >= '\u007F' && c <= '\u009F') || (c >= '\u2000' && c <= '\u20FF')) {
                        String ss = Integer.toHexString(c);
                        b.replace(i, i + 1, "\\");
                        b.insert(++i, "u");
                        for (int k = 0; k < 4 - ss.length(); k++) {
                            b.insert(++i, '0');
                        }
                        b.insert(++i, ss.toUpperCase());
                    }
            }
            i++;
        }
    }
}

class BooleanMapper extends BaseMapper<Boolean> {

    @Override
    public void json(StringBuilder b, Boolean value) {
        b.append(value);
    }
}

class ShortMapper extends BaseMapper<Short> {

    @Override
    public void json(StringBuilder b, Short value) {
        b.append(value);
    }
}

class StringMapper extends BaseMapper<String> {
    @Override
    public void json(StringBuilder b, String value) {
        b.append("\"");
        Mapper.escape(b, value);
        b.append("\"");
    }
}

class IntegerMapper extends BaseMapper<Integer> {

    @Override
    public void json(StringBuilder b, Integer value) {
        b.append(value);
    }
}

class LongMapper extends BaseMapper<Long> {

    @Override
    public void json(StringBuilder b, Long value) {
        b.append(value);
    }
}

class ByteMapper extends BaseMapper<Byte> {

    @Override
    protected void json(StringBuilder b, Byte value) {
        b.append(value);
    }
}

class CharMapper extends BaseMapper<Character> {

    @Override
    protected void json(StringBuilder b, Character value) {
        b.append("\"");
        Mapper.escape(b, value);
        b.append("\"");
    }
}

class FloatMapper extends BaseMapper<Float> {

    @Override
    protected void json(StringBuilder b, Float value) {
        b.append(value);
    }
}

class DoubleMapper extends BaseMapper<Double> {

    @Override
    protected void json(StringBuilder b, Double value) {
        b.append(value);
    }
}