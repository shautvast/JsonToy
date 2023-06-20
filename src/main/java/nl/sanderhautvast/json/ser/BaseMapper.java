package nl.sanderhautvast.json.ser;

public abstract class BaseMapper<T> {

    protected abstract String json(T value);

}
