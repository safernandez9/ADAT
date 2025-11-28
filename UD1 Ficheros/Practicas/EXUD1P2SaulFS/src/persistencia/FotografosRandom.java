// Saúl Fernández Salgado
package persistencia;

import clases.Fotografia;
import clases.Fotografo;
import utilidades.UtilidadesFechas;

import java.io.EOFException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class FotografosRandom extends Archivo {

    private RandomAccessFile raf;
    public static final int TAM_REGISTRO = 1024;  // Tamaño fijo del registro (en bytes)

    public FotografosRandom(String ruta) {
        super(ruta);
    }

    @Override
    public void abrirArchivo() {
        try {
            raf = new RandomAccessFile(file, "rw");
        } catch (FileNotFoundException ex) {
            throw new RuntimeException("Fichero " + file + " no encontrado", ex);
        }
    }

    @Override
    public void cerrarArchivo() {
        try {
            if (raf != null) {
                raf.close();
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al cerrar el archivo: " + e.getMessage(), e);
        }
    }


    /**
     * Lee un fotografo por su id del fichero binario
     *
     * @param idFotografo
     * @return
     */
    public Fotografo leerFotografoPorID(int idFotografo) {
        if (idFotografo <= 0) {
            throw new IllegalArgumentException("ID de fotografo inválido: " + idFotografo);
        }
        Fotografo fotografo = null;

        try {
            if (raf == null) {
                abrirArchivo();
            }
            // Calcular posición del registro
            long posicion = (idFotografo - 1L) * TAM_REGISTRO;

            // Verificar que la posición está dentro del archivo
            if (posicion >= raf.length()) {
                return null; // Registro no existe
            }

            // Mover el puntero a la posición del registro
            raf.seek(posicion);

            // Leer campos básicos en el mismo orden que se escribieron
            boolean borrado = raf.readBoolean();

            int codigo = raf.readInt();
            String numLicencia = raf.readUTF();
            String nombre = raf.readUTF();
            String estudio = raf.readUTF();
            int numFotografia = raf.readInt();

            // Crear objeto Equipo y su lista de patrocinadores
            fotografo = new Fotografo(borrado, codigo, numLicencia, nombre, estudio, numFotografia);
            List<Fotografia> fotografias = new ArrayList<Fotografia>();

            // Crear y añadir patrocinadores a la vez que se leen
            for (int i = 0; i < numFotografia; i++) {
                fotografias.add(new Fotografia(raf.readUTF(), UtilidadesFechas.fromLongFecha(raf.readLong()),
                        raf.readUTF(), raf.readDouble()));
            }

            // Asignar el conjunto de patrocinadores al fotografo
            fotografo.setFotografias(fotografias);

        } catch (EOFException e) {
            // LLega al final del archivo antes de completar la lectura o registro incompleto
            throw new RuntimeException("Registro incompleto al leer fotografo con ID " + idFotografo, e);
        } catch (IOException e) {
            throw new RuntimeException("Error al leer fotografo con ID " + idFotografo + ": " + e.getMessage(), e);
        }
        return fotografo;
    }

    public boolean buscarLicenciaEnArchivo(String licencia) throws IOException {
        try {
            if (raf == null) {
                abrirArchivo();
            }

            for (int id = 1; id <= numeroRegistrosConBorrados(); id++) {
                try {
                    boolean borrado = raf.readBoolean();
                    int codigo = raf.readInt();
                    String numLicencia = raf.readUTF();
                    if (numLicencia.equals(licencia)) {
                        return true;
                    }
                } catch (RuntimeException e) {
                    System.out.println("Error de persistencia al leer el equipo con ID " + id + ": " + e.getMessage());
                }
            }
            return false;


        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    /**
     * Escribe un objeto Fotografo en el fichero aleatorio, usando su idEquipo para calcular la
     * posición. Cada registro ocupa 1024 bytes.
     *
     * @param fotografo El objeto Equipo a escribir
     */
    public void escribirFotografo(Fotografo fotografo) {
        try {
            if (raf == null) {
                abrirArchivo();
            }

            long posicion = (long) (calcularNuevoID()) * TAM_REGISTRO;
            raf.seek(posicion);

            // Escribir campos básicos

            raf.writeBoolean(fotografo.isBorrado());
            raf.writeInt(fotografo.getCodigo());
            raf.writeUTF(fotografo.getNumLicencia());
            raf.writeUTF(fotografo.getNombre());
            raf.writeUTF(fotografo.getEstudio());
            raf.writeInt(fotografo.getFotografias().size());

            // Escribir fotografias
            for (Fotografia f : fotografo.getFotografias()) {
                raf.writeUTF(f.getTitulo());
                raf.writeLong(UtilidadesFechas.toLongFecha(f.getFechaToma()));
                raf.writeUTF(f.getTitulo());
                raf.writeDouble(f.getTamanoMB());
            }

        } catch (IOException e) {
            throw new RuntimeException("Error al escribir fotografo: " + e.getMessage(), e);
        }
    }

    /**
     * Calcula un nuevo ID para un fotografo, basado en el número de registros en el archivo.
     * Incluye los registros marcados como borrados para evitar reutilizar IDs repetidos
     *
     * @return Nuevo ID para el equipo
     */
    public int calcularNuevoID() {
        if (raf == null) {
            abrirArchivo();
        }

        return numeroRegistrosConBorrados() + 1;
    }

    /**
     * Calcula el número de registros en el archivo, incluyendo los marcados como borrados.
     * Como no se repiten IDs, esto permite asignar nuevos IDs secuenciales y evitar
     * acceder a registros ya ocupados pero marcados como borrados.
     *
     * @return Número de registros en el archivo
     */
    public int numeroRegistrosConBorrados() {
        try {
            return (int) Math.ceil((double) raf.length() / TAM_REGISTRO);
        } catch (IOException e) {
            System.out.println("Error al calcular el número de registros: " + e.getMessage());
            return 0;
        }
    }

}
