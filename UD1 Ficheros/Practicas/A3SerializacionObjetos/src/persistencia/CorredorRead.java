package persistencia;

import modelo.Corredor;

import java.io.*;

/**
 * Clase para la lectura de corredores desde un archivo binario de manera secuencial
 */
public class CorredorRead extends Archivo {

    private ObjectInputStream ois;

    public CorredorRead(String ruta) {
        super(ruta);
    }

    @Override
    public void abrirArchivo() {
        if (!this.archivoExiste()) {
            throw new RuntimeException("Archivo no encontrado" + file.getPath());
        }
        try {
            ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(file)));
        } catch (IOException e) {
            ois = null;
            throw new RuntimeException("Error al abrir el archivo de corredores" + e.getMessage(), e);
        }
    }

    @Override
    public void cerrarArchivo() {
        if (ois != null) {
            try {
                ois.close();
            } catch (IOException e) {
                throw new RuntimeException("Error al cerrar el archivo de corredores" + e.getMessage(), e);
            }
        }

    }

    /**
     * Lee un corredor desde la posición actual del stream
     *
     * @return El corredor leído, o null si se llega al final del archivo
     */
    public Corredor leerCorredor() {
        try {
            return (Corredor) ois.readObject();
        } catch (EOFException e) {
            return null;
        } catch (IOException | ClassNotFoundException e) {
            return null;
        }
    }

    /**
     * Devuelve el dorsal mas alto encontrado en el fichero
     *
     * @return Ultimo dorsal o 0 si está vacío o no se puede leerCorredor
     */
    public int obtenerUltimoDorsal() {

        int ultimoDorsal = 0;

        try {
            abrirArchivo();
            if (ois == null)  return 0;
            Corredor c;
            while (((c = leerCorredor()) != null)) {
                ultimoDorsal = c.getDorsal();
            }
        } finally {
            this.cerrarArchivo();

        }
        return ultimoDorsal;
    }

    /**
     * Busca un corredor por su dorsal
     * @param dorsal Dorsal a buscar
     * @return El corredor encontrado o null si no existe
     */
    public Corredor buscarPorDorsal(int dorsal) {
        Corredor encontrado = null;

        try{
            abrirArchivo();
            if (ois == null)  return null;
            Corredor c;
            while (((c = leerCorredor()) != null) && encontrado == null) {
                if (c.getDorsal() == dorsal) {
                    encontrado = c;
                }
            }
        } finally {
            this.cerrarArchivo();
        }
        return encontrado;
    }

    public void iniciarLectura() {
        abrirArchivo();
    }

    public void finalizarLectura() {
        cerrarArchivo();
    }
}
