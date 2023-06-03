package nl.jssl.jsontoy.serialize;

import java.util.Formatter;

public abstract class JSONSerializer<T> {
    protected abstract String handle(T object);

    protected Formatter formatter = new Formatter();

    public String toJSONString(T object) {
        if (object == null) {
            return "";
        } else if (object instanceof Number || object instanceof Boolean) {
            return "" + object;
        } else if (object instanceof CharSequence || object instanceof Character) {
            return "\"" + object + "\"";
        } else {
            return handle(object);
        }
    }
}
