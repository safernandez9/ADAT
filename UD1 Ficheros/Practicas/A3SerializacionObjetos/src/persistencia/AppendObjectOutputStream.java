package persistencia;

import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 *  LA GESTION DE ERRORES: CAPTURO EN PERSISTENCIA LA EXCEPCION Y LANZO UN RUNTIMEEXCEPTION
 *  EN EL GESTOR LO CAPTURO Y LANZO EL MENSAJE CORRESPONDIENTE
 *
 *  O LANZO UN THROW HASTA EL GESTOR DIRECTAMENTE
 */

public class AppendObjectOutputStream extends ObjectOutputStream {

    /**
     * Constructor que recibe OutputStream
     * @param out
     * @throws IOException
     */
    public AppendObjectOutputStream(ObjectOutputStream out) throws IOException {
        super(out);
    }

    /**
     * Constructor sin parametros
     * @throws IOException
     * @throws SecurityException
     */
    protected AppendObjectOutputStream() throws IOException, SecurityException {
        super();
    }

    /**
     * Redefinimos el metodo de escribir la cabecera para que no haga nada
     * @throws IOException
     */
    @Override
    protected void writeStreamHeader() throws IOException {

    }
}
