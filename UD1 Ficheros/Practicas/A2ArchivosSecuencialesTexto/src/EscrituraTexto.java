import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class EscrituraTexto extends Archivo {

    private BufferedWriter bw;

    EscrituraTexto(String archivo) {
        super(archivo);
    }

    @Override
    public void abrirArchivo() throws IOException {
        this.bw = new BufferedWriter(new FileWriter(this.ruta));

    }

    public void cerrarArchivo() throws IOException {
        if(this.bw != null){
            bw.close();
        }
    }

    public void escribirLinea(String linea) throws IOException {
        this.bw.write(linea);
    }


}
