package excepciones;

public class DirectorioNoExisteException extends Exception {
    public DirectorioNoExisteException(String path) {
        super("La ruta del directorio " + path + " no existe");
    }
}
