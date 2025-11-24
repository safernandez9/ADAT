package persistencia;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;


/**
 * Clase que extiende ObjectOutputStream para permitir la
 * escritura de objetos en un archivo existente sin sobrescribir
 * la cabecera del archivo.
 */
public class AppendObjectOutputStream extends ObjectOutputStream {

    /**
     * Constructor que recibe OutputStream
     * @param out
     * @throws IOException
     */
    public AppendObjectOutputStream(OutputStream out) throws IOException {
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
