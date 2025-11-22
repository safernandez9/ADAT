import excepciones.ArchivoNoExisteException;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class LecturaTexto extends Archivo implements AutoCloseable {
    // Objeto BufferedReader para la lectura del archivo
    private BufferedReader br;

    public LecturaTexto(String ruta) {
        super(ruta);
    }

    //Abre un archivo en la ruta del objeto LecturaTexto en modo lectura
    @Override
    public void abrirArchivo() throws ArchivoNoExisteException {
        if (!this.archivoExiste()){
            throw new ArchivoNoExisteException(this.ruta);
        }
        else{
            try {
                this.br = new BufferedReader(new FileReader(this.ruta));
            } catch (IOException e) {
                System.err.println("Error abriendo archivo: " + this.ruta);
                System.out.println(e.getMessage());
            }
        }
    }

    @Override
    public void cerrarArchivo() {
        if(this.br != null){
            try {
                this.br.close();
            } catch (IOException e) {
                System.out.println("Error al cerrar el archivo " + this.ruta + "\n" + e.getMessage());
            }
        }
    }

    /**
     * Lee una línea del archivo abierto
     * @return
     * @throws IOException
     */
    public String leerLinea() throws IOException {
        if (this.br == null) {
            throw new IOException("Archivo no abierto: " + this.ruta);
        }
        return this.br.readLine();
    }

    @Override
    public void close() throws Exception {
        cerrarArchivo();
    }
}
