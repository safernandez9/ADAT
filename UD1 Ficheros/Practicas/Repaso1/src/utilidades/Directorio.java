package utilidades;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class Directorio {

    // Path del directorio
    private Path rutaPath;

    // Constructor que inicializa la ruta del directorio
    public Directorio(String ruta) {
        this.rutaPath = Paths.get(ruta);
    }

    public Directorio() {
    }

    /**
     * Verifica si el directorio existe en la ruta especificada
     *
     * @return
     */
    public boolean existe() {
        return Files.exists(this.rutaPath);
    }

    /**
     * Crea los directorios en la ruta especificada
     *
     * @return true si se crearon los directorios, false si ya existían
     * @throws IOException
     */
    public boolean crearDirectorios() throws IOException {
        if (!Files.exists(this.rutaPath)) {
            Files.createDirectories(this.rutaPath);
            return true;
        }
        return false;
    }

    /**
     * Obtiene el nombre del directorio
     * @return
     */
    public String getNombre() {
        return rutaPath.getFileName().toString();
    }

    /**
     * Borra el directorio en la ruta especificada
     *
     * @return true si se borró el directorio, false si no existía
     * @throws IOException
     */
    public boolean borrar() throws IOException {
        if (!Files.exists(this.rutaPath)) {
            throw new IOException("El directorio no existe: " + this.rutaPath);
        }
        return Files.deleteIfExists(this.rutaPath);
    }

    /**
     * Renombra el directorio en la ruta especificada
     *
     * @param nombreNuevo
     * @return true si se renombró el directorio
     * @throws IOException
     */
    public boolean renombrar(String nombreNuevo) throws IOException {
        if (!Files.exists(this.rutaPath)) {
            throw new IOException("El directorio no existe: " + this.rutaPath);
        }
        Path destino = this.rutaPath.getParent().resolve(nombreNuevo);
        Files.move(this.rutaPath, destino, StandardCopyOption.REPLACE_EXISTING);
        this.rutaPath = destino;
        return true;
    }
}

