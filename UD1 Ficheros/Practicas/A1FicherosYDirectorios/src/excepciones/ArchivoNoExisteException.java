package excepciones;

public class ArchivoNoExisteException extends Exception {
    public ArchivoNoExisteException(String path) {
        super("La ruta del archivo " + path + " no existe");
    }
}
