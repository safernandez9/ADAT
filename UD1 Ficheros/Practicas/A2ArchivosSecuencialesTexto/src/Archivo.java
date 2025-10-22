//Repaso: Las clases abstractas se declaran con abstract. Pueden tener uno o mas metodos abstractos y no se
//pueden instanciar debido a su modificador.
//Puedo poner un constructor con datos comunes para que lo usen las clases que hereden de la abstracta

import excepciones.ArchivoNoExisteException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public abstract class Archivo {

    String ruta;
    Path rutaPath;

    public Archivo(String ruta) {
        this.ruta = ruta;
        rutaPath = Paths.get(ruta);
    }

    public abstract void abrirArchivo() throws ArchivoNoExisteException, IOException;
    public abstract void cerrarArchivo() throws IOException;

    public boolean archivoExiste(){
        return Files.exists(this.rutaPath);
    }

    public void borrarArchivo() throws IOException {
        Files.delete(this.rutaPath);
    }

    public void renombrarArchivo(String nuevoNombre) throws IOException {

        //Concatenar la String es mala idea por el distinto uso de las /. Para unir rutas tengo el metodo replace.
        Path destino = this.rutaPath.getParent().resolve(nuevoNombre);
        Files.move(this.rutaPath, destino, StandardCopyOption.REPLACE_EXISTING);
    }

}
