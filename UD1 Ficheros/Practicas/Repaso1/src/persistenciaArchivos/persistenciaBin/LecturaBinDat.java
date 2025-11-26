package persistenciaArchivos.persistenciaBin;

import excepciones.ExcepcionFicheros;
import utilidades.Archivo;

import java.io.*;

public class LecturaBinDat extends Archivo {

    private DataInputStream dis;

    public LecturaBinDat(String ruta) {
        super(ruta);
    }

    @Override
    public void abrirArchivo() {
        if (!this.archivoExiste()) {
            throw new RuntimeException("Archivo no encontrado" + file.getPath());
        }
        try {
            dis = new DataInputStream(new BufferedInputStream(new FileInputStream(file)));
        } catch (IOException e) {
            dis = null;
            throw new RuntimeException("Error al abrir el archivo de corredores" + e.getMessage(), e);
        }
    }

    @Override
    public void cerrarArchivo() {
        if (dis != null) {
            try {
                dis.close();
            } catch (IOException e) {
                throw new RuntimeException("Error al cerrar el archivo de corredores" + e.getMessage(), e);
            }
        }

    }

    public int leerInt() throws ExcepcionFicheros, EOFException {
        try {
            return dis.readInt();
        } catch (EOFException e){
            throw e;
        } catch (IOException e) {
            throw new ExcepcionFicheros("Error leyendo ID del archivo binario: " + e.getMessage(), e);
        }
    }

    public double leerDouble() throws ExcepcionFicheros, EOFException {
        try {
            return dis.readDouble();
        } catch (EOFException e){
            throw e;
        }catch (IOException e) {
            throw new ExcepcionFicheros("Error leyendo salario del archivo binario: " + e.getMessage(), e);
        }
    }


}
