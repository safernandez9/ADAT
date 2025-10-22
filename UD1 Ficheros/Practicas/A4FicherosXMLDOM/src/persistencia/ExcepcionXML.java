package persistencia;

/**
 * Lanzo en lugar de runtimeExceptions, facilita la legibilidad del codigo. Dos constructores según como la quiera usar
 */
public class ExcepcionXML extends RuntimeException {

    public ExcepcionXML(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }

    public ExcepcionXML(String mensaje) {
        super(mensaje);
    }
}
