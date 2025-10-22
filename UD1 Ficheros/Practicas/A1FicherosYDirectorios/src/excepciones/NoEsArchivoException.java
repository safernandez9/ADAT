package excepciones;

public class NoEsArchivoException extends Exception{
    public NoEsArchivoException() {
        super("La ruta no es un archivo");
    }
}
