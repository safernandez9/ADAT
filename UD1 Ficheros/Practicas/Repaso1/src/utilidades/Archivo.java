package utilidades;

import excepciones.ArchivoNoExisteException;

import java.io.File;

public abstract class Archivo {

    protected File file;

    public Archivo(String ruta) {
        this.file = new File(ruta);
    }

    public abstract void abrirArchivo() throws ArchivoNoExisteException;
    public abstract void cerrarArchivo();

    public boolean archivoExiste(){
        return this.file.exists();
    }
    public boolean borrarArchivo(){
        return this.file.delete();
    }

    //NO SE SI ESTA BIEN
    public boolean renombrarArchivo(String nombreNuevo){
        return this.file.renameTo(new File(nombreNuevo));
    }

    public boolean crearRutaSiNoExiste() {
        return this.file.getParentFile().mkdirs();
    }
}
