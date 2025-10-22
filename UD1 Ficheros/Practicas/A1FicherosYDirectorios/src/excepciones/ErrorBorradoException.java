package excepciones;

public class ErrorBorradoException extends Exception {
    public ErrorBorradoException(String path) {
        super("Error al borrar en " + path);
    }
}
