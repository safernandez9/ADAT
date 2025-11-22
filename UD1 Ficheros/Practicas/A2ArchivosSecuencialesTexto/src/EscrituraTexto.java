import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class EscrituraTexto extends Archivo implements AutoCloseable {

    //BufferedWriter para escribir en el archivo
    private BufferedWriter bWriter;

    public EscrituraTexto(String ruta) {
        super(ruta);
    }

    /**
     * Abre un archivo en la ruta del objeto EscrituraTexto en modo escritura (append)
     */
    @Override
    public void abrirArchivo() {
        try {
            bWriter = new BufferedWriter(new FileWriter(ruta, true)); // modo append
        } catch (IOException e) {
            System.err.println("error abriendo archivo para escritura " + e.getMessage());
        }
    }

    /**
     * Cierra el archivo abierto para escritura
     */
    @Override
    public void cerrarArchivo() {
        try {
            if (bWriter != null) {
                bWriter.close();
            }
        } catch (IOException e) {
            System.err.println("error cerrando archivo de escritura " + e.getMessage());
        }
    }

    /**
     * Escribe una línea en el archivo abierto
     *
     * @param texto
     */
    public void escribirLinea(String texto) {
        try {
            if (bWriter != null) {
                bWriter.write(texto);
                bWriter.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error escribiendo en archivo: " + e.getMessage());
        }
    }

    /**
     * Cierra el archivo al finalizar el uso del objeto (AutoCloseable)
     */
    @Override
    public void close() throws Exception {
        cerrarArchivo();
    }
}

