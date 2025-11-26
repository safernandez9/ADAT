package persistenciaArchivos.persistenciaObj;

import excepciones.ArchivoNoExisteException;
import excepciones.ExcepcionFicheros;
import utilidades.Archivo;

import java.io.*;


/**
 * Clase para la lectura de corredores desde un archivo binario de manera secuencial
 * Ver p48
 *
 */
public class LectorObj extends Archivo {

    private ObjectInputStream ois;

    public LectorObj(String ruta) {
        super(ruta);
    }

    /**
     * Abre un archivo para lectura
     *
     * @throws ArchivoNoExisteException Si el archivo no existe
     */
    @Override
    public void abrirArchivo() throws ArchivoNoExisteException {
        if (!this.archivoExiste()) {
            throw new ArchivoNoExisteException("Archivo no encontrado" + file.getPath());
        }
        try {
            ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(file)));
        } catch (IOException e) {
            ois = null;
            throw new ExcepcionFicheros("Error al abrir el archivo " + file.getPath() + e.getMessage(), e);
        }
    }

    /**
     * Cierra un archivo
     */
    @Override
    public void cerrarArchivo() {
        if (ois != null) {
            try {
                ois.close();
            } catch (IOException e) {
                throw new RuntimeException("Error al cerrar el archivo " + file.getPath() + e.getMessage(), e);
            }
        }

    }

    /**
     * Lee un objeto desde la posición actual del stream
     *
     * @return El objeto leído
     * @throws EOFException      Si se llega al final del archivo
     * @throws ExcepcionFicheros Si ocurre un error de lectura
     */
    public Object leerObjeto() throws EOFException, ExcepcionFicheros {
        try {
            return ois.readObject();
        } catch (EOFException e) {
            throw e;
        } catch (IOException | ClassNotFoundException e) {
            throw new ExcepcionFicheros("Error al leer el objeto del archivo: " + e.getMessage(), e);
        }
    }

    /**
     * Inicia la lectura del archivo
     *
     * @throws ArchivoNoExisteException Si el archivo no existe
     */
    public void iniciarLectura() throws ArchivoNoExisteException {
        abrirArchivo();
    }

    /**
     * Finaliza la lectura del archivo
     */
    public void finalizarLectura() {
        cerrarArchivo();
    }

}
