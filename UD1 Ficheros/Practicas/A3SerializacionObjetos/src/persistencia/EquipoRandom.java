package persistencia;

import modelo.Equipo;
import modelo.Patrocinador;
import utilidades.UtilidadesFechas;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class EquipoRandom extends Archivo {

    private RandomAccessFile raf;
    private static final int TAM_REGISTRO = 200;  // Tamaño fijo del registro (en bytes)

    public EquipoRandom(String ruta){
        super(ruta);
    }

    @Override
    public void abrirArchivo() {
        try{
            raf = new RandomAccessFile(ruta,"rw");
        }catch(FileNotFoundException ex){
            throw new RuntimeException("Fichero " + ruta + " no encontrado", ex);
        }
    }

    @Override
    public void cerrarArchivo() {
        try{
            if(raf != null){
                raf.close();
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al cerrar el archivo: " + e.getMessage(), e);
        }
    }

    /**
     * Escribe un objeto Equipo en el fichero aleatorio, usando su idEquipo para calcular la
     * posición. Cada registro ocupa 200 bytes.
     *
     * @param equipo
     */
    public void escribirEquipo(Equipo equipo) {
        try {
            if (raf == null) {
                abrirArchivo();
            }

            // Calcular posición del registro
            long posicion = (equipo.getIdEquipo() - 1L) * TAM_REGISTRO;
            raf.seek(posicion);

            // Escribir campos básicos

            /* Colocar el flag de borrado como primer campo permite detectar inmediatamente si
            * un registro es libre sin tener que leer y deserializar el resto del contenido, lo
            * que reduce I/O
             */
            raf.writeBoolean(equipo.isBorrado());
            raf.writeInt(equipo.getIdEquipo());
            raf.writeUTF(equipo.getNombre());
            raf.writeInt(equipo.getPatrocinadores().size());

            // Escribir patrocinadores
            for (Patrocinador p : equipo.getPatrocinadores()) {
                raf.writeUTF(p.getNombre());
                raf.writeFloat(p.getDonacion());
                raf.writeLong(UtilidadesFechas.toLongFecha(p.getFechaInicio()));
            }

            /*
             * Se podría rellenar el espacio que queda con 0 en vez de basura
             * // Rellenar el resto del registro hasta 200 bytes
             * long posFinal = (equipo.getIdEquipo() * TAM_REGISTRO);
             * if (raf.getFilePointer() < posFinal) {
             *     int bytesRestantes = (int) (posFinal - raf.getFilePointer());
             *     for (int i = 0; i < bytesRestantes; i++) {
             *         raf.writeByte(0);
             *     }
             * }
             */

        } catch (IOException e) {
            throw new RuntimeException("Error al escribir equipo: " + e.getMessage(), e);
        }
    }

}
