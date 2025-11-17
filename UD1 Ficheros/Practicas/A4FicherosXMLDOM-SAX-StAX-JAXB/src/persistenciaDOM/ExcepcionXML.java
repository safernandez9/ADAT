package persistenciaDOM;

/**
 * Lanzo en lugar de runtimeExceptions, facilita la legibilidad del codigo. Dos constructores según como la quiera usar.
 */
public class ExcepcionXML extends RuntimeException {

    /**
     * Recibe un mensaje escrito por el programador y la excepcion generada.
     * @param mensaje Tipo String
     * @param causa
     */
    public ExcepcionXML(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }

    /**
     * Solo recibe un mensaje.
     * @param mensaje Tipo String
     */
    public ExcepcionXML(String mensaje) {
        super(mensaje);
    }
}
