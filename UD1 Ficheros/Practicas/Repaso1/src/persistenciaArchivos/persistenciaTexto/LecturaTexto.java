package persistenciaArchivos.persistenciaTexto;


import excepciones.ArchivoNoExisteException;
import utilidades.Archivo;

import java.io.BufferedReader;
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
            throw new ArchivoNoExisteException(this.file.getPath());
        }
        else{
            try {
                this.br = new BufferedReader(new FileReader(this.file));
            } catch (IOException e) {
                System.err.println("Error abriendo archivo: " + this.file);
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
                System.out.println("Error al cerrar el archivo " + this.file + "\n" + e.getMessage());
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
            throw new IOException("Archivo no abierto: " + this.file);
        }
        return this.br.readLine();
    }

    @Override
    public void close() throws Exception {
        cerrarArchivo();
    }
}
