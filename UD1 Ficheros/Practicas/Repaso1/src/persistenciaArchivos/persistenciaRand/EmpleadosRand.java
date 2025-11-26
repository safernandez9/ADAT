package persistenciaArchivos.persistenciaRand;

import excepciones.ArchivoNoExisteException;
import excepciones.ExcepcionFicheros;
import modelo.EmpleadoCompleto;
import persistenciaArchivos.persistenciaTexto.EmpleadosTxt;
import utilidades.Archivo;

import java.io.EOFException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashSet;
import java.util.Set;

public class EmpleadosRand extends Archivo {

    private RandomAccessFile raf;
    public static final int TAM_REGISTRO = 200;  // Tamaño fijo del registro (en bytes)

    public EmpleadosRand (String ruta){
        super(ruta);
    }

    @Override
    public void abrirArchivo() {
        try{
            raf = new RandomAccessFile(file,"rw");
        }catch(FileNotFoundException ex){
            throw new RuntimeException("Fichero " + file + " no encontrado", ex);
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
     * @param empleado El objeto Equipo a escribir
     */
    public void escribirEmpleado(EmpleadoCompleto empleado, String log) throws ArchivoNoExisteException, ExcepcionFicheros {

        EmpleadosTxt empleadosTxt = new EmpleadosTxt();

        int edadMIN, edadMAX;
        try{
            edadMAX = empleadosTxt.leerEdadMaxima(file.getPath());
            edadMIN = empleadosTxt.leerEdadMinima(file.getPath());
        } catch (ExcepcionFicheros | ArchivoNoExisteException e){
            throw new ExcepcionFicheros("Error al leer edades desde el archivo de texto: " + e.getMessage(), e);
        }


        try {
            if (raf == null) {
                abrirArchivo();
            }

            // Calcular posición del registro
            // -1L porque los equipos se numeran desde 1, pero las posiciones en bytes empiezan desde 0. La L
            // es para forzar que la operación se haga en long y no en int ya que los bytes se guardan en long.
            long posicion = (empleado.getId() - 1L) * TAM_REGISTRO;
            raf.seek(posicion);

            // Escribir campos básicos

            /* Colocar el flag de borrado como primer campo permite detectar inmediatamente si
             * un registro es libre sin tener que leerCorredor y deserializar el resto del contenido, lo
             * que reduce I/O
             */
            raf.writeBoolean(empleado.isBorrado());
            raf.writeInt(empleado.getId());
            raf.writeUTF(empleado.getNombre());
            if(empleado.getEdad() > edadMAX || empleado.getEdad() < edadMIN){
                empleadosTxt.escribirLogError("Edad inválida para empleado ID " + empleado.getId() +)
                        ": " + empleado.getEdad() + ". Debe estar entre " + edadMIN + " y " + edadMAX, log);
                return;
            }
            raf.writeInt(empleado.getEdad());
            raf.writeUTF(empleado.getDepartamento());
            raf.writeDouble(empleado.getSalario());


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

    /**
     * Lee un objeto Equipo desde el fichero aleatorio en la posición calculada
     * a partir de su idEquipo.
     *
     * @param idEquipo Identificador del equipo a leer
     * @return El objeto Equipo leído, o null si no existe
     */
    public Equipo leerEquipoPorID(int idEquipo) {
        if(idEquipo <= 0){
            throw new IllegalArgumentException("ID de equipo inválido: " + idEquipo);
        }
        Equipo equipo = null;

        try{
            if(raf == null){
                abrirArchivo();
            }
            // Calcular posición del registro
            long posicion = (idEquipo - 1L) * TAM_REGISTRO;

            // Verificar que la posición está dentro del archivo
            if(posicion >= raf.length()){
                return null; // Registro no existe
            }

            // Mover el puntero a la posición del registro
            raf.seek(posicion);

            // Leer campos básicos en el mismo orden que se escribieron
            boolean borrado = raf.readBoolean();
            if(borrado){
                return null; // Registro marcado como borrado
            }

            int idLeido = raf.readInt();
            String nombre = raf.readUTF();
            int numPatrocinadores = raf.readInt();

            // Crear objeto Equipo y su lista de patrocinadores
            equipo = new Equipo(idLeido, nombre);
            equipo.setBorrado(borrado);
            equipo.setNumPatrocinadores(numPatrocinadores);
            Set<Patrocinador> patrocinadores = new HashSet<>();

            // Crear y añadir patrocinadores a la vez que se leen
            for(int i = 0; i < numPatrocinadores; i++){
                patrocinadores.add(new Patrocinador(raf.readUTF(),
                        raf.readFloat(), UtilidadesFechas.fromLongFecha(raf.readLong())));
            }

            // Asignar el conjunto de patrocinadores al equipo
            equipo.setPatrocinadores(patrocinadores);

        } catch(EOFException e){
            // LLega al final del archivo antes de completar la lectura o registro incompleto
            throw new RuntimeException("Registro incompleto al leer equipo con ID " + idEquipo, e);
        } catch (IOException e) {
            throw new RuntimeException("Error al leer equipo con ID " + idEquipo + ": " + e.getMessage(), e);
        }
        return equipo;
    }

    /**
     * Calcula un nuevo ID para un equipo, basado en el número de registros en el archivo.
     * Incluye los registros marcados como borrados para evitar reutilizar IDs.
     *
     * @return Nuevo ID para el equipo
     */
    public int calcularNuevoID(){
        if(raf == null){
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
        try{
            return (int) Math.ceil((double) raf.length() / TAM_REGISTRO);
        } catch (IOException e){
            System.out.println("Error al calcular el número de registros: " + e.getMessage());
            return 0;
        }
    }

    /**
     * Calcula un nuevo ID reutilizando IDs de equipos borrados.
     * Recorre el archivo buscando el primer registro marcado como borrado
     * y devuelve su ID (posición + 1).
     * Si no hay registros borrados, devuelve el siguiente ID secuencial.
     */
    public int calcularNuevoIDReutilizado(){

        if(raf == null){
            abrirArchivo();
        }

        try{
            long totalRegistros = numeroRegistrosConBorrados();

            for(int i = 0; i < totalRegistros; i++){
                long posicion = i * TAM_REGISTRO;
                raf.seek(posicion);
                boolean borrado = raf.readBoolean();
                if(borrado){
                    int id = raf.readInt(); // El ID está justo después del flag de borrado
                    return id; // Devolver el ID del registro borrado encontrado
                }
            }

            // No se encontraron registros borrados, devolver siguiente ID secuencial
            return (int) totalRegistros + 1;

        } catch (IOException e){
            throw new RuntimeException("Error al calcular nuevo ID reutilizado: " + e.getMessage(), e);
        }
    }
}
