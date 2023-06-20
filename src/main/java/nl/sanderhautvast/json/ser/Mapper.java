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

    static {
        addMapper(String.class, new StringMapper());
        addMapper(Boolean.class, new BooleanMapper());
        addMapper(Integer.class, new IntegerMapper());
        addMapper(Long.class, new LongMapper());
        addMapper(Short.class, new ShortMapper());
        addMapper(Byte.class, new ByteMapper());
        addMapper(Character.class, new CharMapper());
        addMapper(Float.class, new FloatMapper());
        addMapper(Double.class, new DoubleMapper());
    }

    /**
     * Add a new (custom) mapper implementation for the specified type
     *
     * @param type   The class to serialize to json
     * @param mapper the Mapper implementation
     */
    public static <T> void addMapper(Class<T> type, BaseMapper<T> mapper) {
        mappers.put(type, mapper);
    }

    /**
     * returns the json representation of the value as a String
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static <J> String json(Object value) {
        if (value == null) {
            return "null";
        }
        Class<J> type = (Class<J>) value.getClass();
        if (type.isArray()) {
            return array((Object[]) value);
        }
        if (value instanceof Collection) {
            return list((Collection) value);
        }
        if (value instanceof Map) {
            return object((Map) value);
        }
        BaseMapper mapper = mappers.computeIfAbsent(type, key -> createObjectMapper(type));
        return mapper.json(value);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static String array(Object[] array) {
        if (array.length == 0) {
            return "[]";
        }
        Object first = array[0];
        Class<?> elementType = first.getClass();
        BaseMapper mapper = mappers.computeIfAbsent(elementType, key -> createObjectMapper(elementType));
        StringBuilder builder = new StringBuilder("[");
        builder.append(mapper.json(first));
        Arrays.stream(array).skip(1)
                .forEach(element -> {
                    builder.append(",");
                    builder.append(mapper.json(element));
                });
        builder.append("]");
        return builder.toString();
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static String list(Collection list) {
        if (list.isEmpty()) {
            return "[]";
        }
        Object first = list.iterator().next();
        Class<?> elementType = first.getClass();
        BaseMapper mapper = mappers.computeIfAbsent(elementType, key -> createObjectMapper(elementType));
        StringBuilder builder = new StringBuilder("[");
        builder.append(mapper.json(first));
        list.stream().skip(1)
                .forEach(element -> {
                    builder.append(",");
                    builder.append(mapper.json(element));
                });
        builder.append("]");
        return builder.toString();
    }


    @SuppressWarnings({"rawtypes", "unchecked"})
    private static String object(Map map) {
        if (map.isEmpty()) {
            return "{}";
        }
        Set<Map.Entry> entries = map.entrySet();
        Map.Entry first = entries.iterator().next();
        Class<?> valueType = first.getValue().getClass();

        BaseMapper mapper = mappers.computeIfAbsent(valueType, key -> createObjectMapper(valueType));
        StringBuilder builder = new StringBuilder("{");
        builder.append("\"").append(first.getKey()).append("\":").append(mapper.json(first.getValue()));
        entries.stream().skip(1)
                .forEach(entry -> {
                    builder.append(",\"");
                    builder.append(entry.getKey()).append("\":");
                    builder.append(mapper.json(entry.getValue()));
                });
        builder.append("}");
        return builder.toString();
    }

    public static String json(byte value) {
        return Byte.toString(value);
    }

    public static String json(boolean value) {
        return Boolean.toString(value);
    }

    public static String json(short value) {
        return Short.toString(value);
    }

    public static String json(int value) {
        return Integer.toString(value);
    }

    public static String json(long value) {
        return Long.toString(value);
    }

    public static String json(char value) {
        return "\"" + escape(value) + "\"";
    }

    public static String json(float value) {
        return Float.toString(value);
    }

    public static String json(double value) {
        return Double.toString(value);
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

    private static String escape(char c) {
        return escape(String.valueOf(c));
    }

    static String escape(String value) {
        StringBuilder b = new StringBuilder(value);
        int i = 0;
        while (i < b.length()) {
            char c = b.charAt(i);
            switch (c) {
                case '\t':
                    b.replace(i,i+1, "\\");
                    b.insert(i+1, "t");
                    break;
                case '\"':
                    b.replace(i,i+1, "\\");
                    b.insert(++i, "\"");
                    break;
                case '/':
                    b.replace(i,i+1, "\\");
                    b.insert(++i, "/");
                    break;
                case '\r':
                    b.replace(i,i+1, "\\");
                    b.insert(++i, "r");
                    break;
                case '\n':
                    b.replace(i,i+1, "\\");
                    b.insert(++i, "n");
                    break;
                case '\b':
                    b.replace(i,i+1, "\\");
                    b.insert(++i, "b");
                    break;
                case '\f':
                    b.replace(i,i+1, "\\");
                    b.insert(++i, "f");
                    break;
                case '\\':
                    b.replace(i,i+1, "\\");
                    b.insert(++i, "\\");
                    break;
                case '\'':
                    break;
                default:
                    if ((c <= '\u001F') || (c >= '\u007F' && c <= '\u009F') || (c >= '\u2000' && c <= '\u20FF')) {
                        String ss = Integer.toHexString(c);
                        b.replace(i,i+1,"\\");
                        b.insert(++i, "u");
                        for (int k = 0; k < 4 - ss.length(); k++) {
                            b.insert(++i,'0');
                        }
                        b.insert(++i,ss.toUpperCase());
                    }
            }
            i++;
        }
        return b.toString();
    }
}

class BooleanMapper extends BaseMapper<Boolean> {

    @Override
    public String json(Boolean b) {
        return Boolean.toString(b);
    }
}

class ShortMapper extends BaseMapper<Short> {

    @Override
    public String json(Short value) {
        return Short.toString(value);
    }
}

class StringMapper extends BaseMapper<String> {
    @Override
    public String json(String value) {
        return "\"" + Mapper.escape(value) + "\"";
    }
}

class IntegerMapper extends BaseMapper<Integer> {

    @Override
    public String json(Integer value) {
        return Integer.toString(value);
    }
}

class LongMapper extends BaseMapper<Long> {

    @Override
    public String json(Long value) {
        return Long.toString(value);
    }
}

class ByteMapper extends BaseMapper<Byte> {

    @Override
    protected String json(Byte value) {
        return Byte.toString(value);
    }
}

class CharMapper extends BaseMapper<Character> {

    @Override
    protected String json(Character value) {
        return "\"" + value + "\"";
    }
}

class FloatMapper extends BaseMapper<Float> {

    @Override
    protected String json(Float value) {
        return Float.toString(value);
    }
}

class DoubleMapper extends BaseMapper<Double> {

    @Override
    protected String json(Double value) {
        return Double.toString(value);
    }
}