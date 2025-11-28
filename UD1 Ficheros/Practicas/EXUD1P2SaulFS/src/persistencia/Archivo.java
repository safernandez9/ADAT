// Saúl Fernández Salgado
package persistencia;

import utilidades.ArchivoNoExisteException;

import java.io.File;

public abstract class Archivo {

    protected File file;

    public Archivo(String ruta) {
        this.file = new File(ruta);
    }

    public abstract void abrirArchivo() throws ArchivoNoExisteException;
    public abstract void cerrarArchivo();


}
