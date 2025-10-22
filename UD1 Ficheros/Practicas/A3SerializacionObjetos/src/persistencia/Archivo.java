package persistencia;

import java.io.File;

public abstract class Archivo {

    protected File ruta;

    public Archivo(String ruta) {
        this.ruta = new File(ruta);
    }

    public abstract void abrirArchivo();
    public abstract void cerrarArchivo();

    public boolean archivoExiste(){
        return this.ruta.exists();
    }
    public boolean borrarArchivo(){
        return this.ruta.delete();
    }

    //NO SE SI ESTA BIEN
    public boolean renombrarArchivo(String nombreNuevo){
        return this.ruta.renameTo(new File(nombreNuevo));
    }
}
