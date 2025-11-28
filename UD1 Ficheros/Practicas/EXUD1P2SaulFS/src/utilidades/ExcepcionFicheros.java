// Saúl Fernández Salgado
package utilidades;

/**
 * Lanzo en lugar de runtimeExceptions, facilita la legibilidad del codigo. Dos constructores según como la quiera usar.
 */
public class ExcepcionFicheros extends RuntimeException {

    /**
     * Recibe un mensaje escrito por el programador y la excepcion generada.
     * @param mensaje Tipo String
     * @param causa
     */
    public ExcepcionFicheros(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }

    /**
     * Solo recibe un mensaje.
     * @param mensaje Tipo String
     */
    public ExcepcionFicheros(String mensaje) {
        super(mensaje);
    }
}
