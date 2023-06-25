package nl.sanderhautvast.json.ser;

public abstract class BaseMapper<T> {

    protected abstract void json(StringBuilder b, T value);

}
