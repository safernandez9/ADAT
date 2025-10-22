package persistencia;

import modelo.Corredor;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;

public class CorredorRead extends Archivo {

    ObjectInputStream ois;

    public CorredorRead(String ruta) {
        super(ruta);
    }


    @Override
    public void abrirArchivo() {
        if (!this.archivoExiste()) {
            throw new RuntimeException("Archivo no encontrado" + ruta.getPath());
        }
        try {
            ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(ruta));
        } catch (IOException e) {
            ois = null;
            throw new RuntimeException("Error al abrir el archivo de corredores" + e.getMessage());
        }
    }

    //Ready
    @Override
    public void cerrarArchivo() {
        if (ois != null) {
            try {
                ois.close();
            } catch (IOException e) {
                throw new RuntimeException("Error al cerrar el archivo de corredores" + e.getMessage());
            }
        }

    }

    /**
     * Lee un corredor desde la posición actual del stream
     *
     * @return
     */
    public Corredor leer() {
        try {
            return (Corredor) ois.readObject();
        } catch (EOFException e) {
            return null;
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error al leer el corredor");
            return null;
        }
    }

    /**
     * Devuelve el dorsal mas alto encontrado en el fichero
     *
     * @return
     */
    public int obtenerUltimoDorsal() {
        int ultimoDorsal = 0;

        try {
            abrirArchivo();
            if (ois == null)  return 0;
            Corredor c;
            while (((c = leer()) != null)) {
                ultimoDorsal = c.getDorsal();
            }
        } finally {
            this.cerrarArchivo();

        }
        return ultimoDorsal;
    }

    /**
     * Busca un corredor por su dorsal
     * @param dorsal
     * @return
     */
    public Corredor buscarPorDorsal(int dorsal) {
        Corredor encontrado = null;

        try{
            abrirArchivo();
            if (ois == null)  return null;
            Corredor c;
            while (((c = leer()) != null) && encontrado == null) {
                if (c.getDorsal() == dorsal) {
                    encontrado = c;
                }
            }
        } finally {
            this.cerrarArchivo();
        }
        return encontrado;
    }


    /* PARA RECORRER COMO UNA LISTA, lo descubrio hace nada no lo usa hay que ver como funciona
    public Iterator<Corredor> leerIterativo() {
        this.abrirArchivo();
        if (in == null) {
            return new ArrayList<>();
        }

        return () -> new Iterator<Corredor>() {
            Corredor siguiente = leer();
            boolean fin = (siguiente == null);

            @Override
            public boolean hasNext() {
                return !fin;
            }

            @Override
            public Corredor next() {
                Corredor actual = siguiente;
                siguiente = leer();
                if (siguiente == null) {
                    fin = true;
                    cerrarArchivo();
                }
                return actual;
            }
        };
    }
    */

    public void iniciarLectura() {
        abrirArchivo();
    }

    public void finalizarLectura() {
        cerrarArchivo();
    }
}
