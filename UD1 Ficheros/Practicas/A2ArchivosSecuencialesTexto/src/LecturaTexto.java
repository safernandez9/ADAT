import excepciones.ArchivoNoExisteException;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class LecturaTexto extends Archivo {
    private BufferedReader br;

    public LecturaTexto(String ruta) {
        super(ruta);
    }

    //Abre un archivo en la ruta del objeto LecturaTexto en modo lectura
    @Override
    public void abrirArchivo() throws ArchivoNoExisteException {
        //Metodo encontrado, preguntar br = Files.newBufferedReader(rutaPath);
        if (!this.archivoExiste()){
            throw new ArchivoNoExisteException(this.ruta);
        }
        else{
             //TODO
            //Estos try-catch para tratar e archivo con las funciones del enunciado? Podria hacerlo como en los apuntes y ya estaría
            //Usar el metodo propio de archivoExiste me obilga a hacer un try-Catch que nunca se va a ejecutar. Por que no hacerlo
            //simple como en cerrar archivo.
            //Vamos a tener Ejercicios o documentación en los examenes?
            try {
                this.br = new BufferedReader(new FileReader(this.ruta));
            } catch (FileNotFoundException ignored) {;
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

    public String leerLinea() throws IOException {
        try {
           return this.br.readLine();
        } catch (IOException e) {
            throw new IOException(e);
        }
    }

}
