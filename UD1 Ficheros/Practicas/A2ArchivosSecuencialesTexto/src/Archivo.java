//Repaso: Las clases abstractas se declaran con abstract. Pueden tener uno o mas metodos abstractos y no se
//pueden instanciar debido a su modificador.
//Puedo poner un constructor con datos comunes para que lo usen las clases que hereden de la abstracta

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import excepciones.ArchivoNoExisteException;

public abstract class Archivo {

    // Ruta del archivo
    protected String ruta;
    // Path del archivo
    protected Path rutaPath;

    public Archivo(String ruta) {
        this.ruta = ruta;
        this.rutaPath = Paths.get(ruta);
    }

    // Métodos abstractos para abrir y cerrar archivos que deben implementar las subclases
    public abstract void abrirArchivo() throws ArchivoNoExisteException, IOException;
    public abstract void cerrarArchivo() throws IOException;

    /**
     * Verifica si el archivo existe en la ruta especificada
     * @return
     */
    public boolean archivoExiste() {
        return Files.exists(this.rutaPath);
    }

    /**
     * Borra el archivo en la ruta especificada
     *
     * @throws IOException
     */
    public void borrarArchivo() throws IOException {
        if (!archivoExiste()) {
            throw new IOException("Archivo no existe: " + this.ruta);
        }
        Files.delete(this.rutaPath);
    }

    /**
     * Renombra el archivo en la ruta especificada
     *
     * @param nuevoNombre
     * @throws IOException
     */
    public void renombrarArchivo(String nuevoNombre) throws IOException {
        if (!archivoExiste()) {
            throw new IOException("Archivo no existe: " + this.ruta);
        }
        Path destino = this.rutaPath.getParent().resolve(nuevoNombre);
        Files.move(this.rutaPath, destino, StandardCopyOption.REPLACE_EXISTING);
        this.ruta = destino.toString();
        this.rutaPath = destino;
    }
}

