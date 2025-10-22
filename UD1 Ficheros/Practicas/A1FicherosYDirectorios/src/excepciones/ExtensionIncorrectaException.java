package excepciones;

public class ExtensionIncorrectaException extends Exception {
    public ExtensionIncorrectaException(String extension) {
        super("La cadena " + extension + " no es una extensión");
    }
}
