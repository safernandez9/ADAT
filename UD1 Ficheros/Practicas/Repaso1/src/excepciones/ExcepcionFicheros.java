package excepciones;

public class ExcepcionFicheros extends RuntimeException {
    /**
     * Recibe un mensaje escrito por el programador y la excepcion generada.
     * @param mensaje Tipo String
     * @param causa Tipo Throwable
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
