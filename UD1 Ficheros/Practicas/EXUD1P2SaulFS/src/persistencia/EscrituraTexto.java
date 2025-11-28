// Saúl Fernández Salgado
package persistencia;

import utilidades.ExcepcionFicheros;
import utilidades.ExcepcionXML;

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
            bWriter = new BufferedWriter(new FileWriter(file.getPath()));
        } catch (IOException e) {
            throw new ExcepcionFicheros("Error al abrir archivo: " + e.getMessage());
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
            throw new ExcepcionFicheros("Error al cerrar archivo: " + e.getMessage());
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
            throw new ExcepcionFicheros("Error al escribir linea: " + e.getMessage());
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

