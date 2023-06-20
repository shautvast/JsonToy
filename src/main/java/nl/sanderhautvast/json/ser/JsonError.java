package nl.sanderhautvast.json.ser;

public class JsonError extends RuntimeException{
    public JsonError(Throwable cause) {
        super(cause);
    }
}
