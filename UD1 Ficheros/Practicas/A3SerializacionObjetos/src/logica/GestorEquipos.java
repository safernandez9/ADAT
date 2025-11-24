package logica;

import modelo.Equipo;
import modelo.Patrocinador;
import persistencia.EquipoRandom;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class GestorEquipos {

    private final EquipoRandom archivo;

    public GestorEquipos(String rutaArchivo) {
        archivo = new EquipoRandom(rutaArchivo);
        // Abrir el archivo al crear el gestor puede lanzar una excepcion
        try{
            archivo.abrirArchivo();
        } catch (RuntimeException e) {
            System.out.println("Error al abrir el archivo de equipos: " + e.getMessage());
        }
    }

    /**
     * Busca un equipo por su nombre en el archivo.
     *
     * @param nombre El nombre del equipo a buscar.
     * @return El objeto Equipo si se encuentra, o null si no se encuentra.
     */
    public Equipo buscarEquipoPorNombre(String nombre) {
            Equipo equipo;
            int idEquipo = 1;
            // Leer equipos secuencialmente hasta encontrar el nombre o llegar al final
            // Leo el equipo entero y luego hago comprobaciones y return
            while ((equipo = archivo.leerEquipoPorID(idEquipo)) != null) {
                if (!equipo.isBorrado() && equipo.getNombre().equalsIgnoreCase(nombre)) {
                    return equipo;
                }
                idEquipo++;
            }

        return null; // No se encontró el equipo
    }

    /**
     * Guarda un nuevo equipo en el archivo.
     *
     * @param equipo El objeto Equipo a guardar.
     * @return true si se guardó correctamente, false en caso de error.
     */
    public boolean guardarEquipo(Equipo equipo) {
        try{
            if(buscarEquipoPorNombre(equipo.getNombre()) != null){
                System.out.println("El equipo con nombre " + equipo.getNombre() + " ya existe.");
                return false;
            }

            // Asignar ID autoincremental
            int ultimoId = archivo.calcularNuevoID();
            // También puedo calcular el ID del primer registro borrado
            // int id = archivo.calcularNuevoIDRetilizando();

            equipo.setIdEquipo(ultimoId);

            // Actualizo el número de patrocinadores justo antes de añadir por si hay inconsistencias
            equipo.setNumPatrocinadores(equipo.getPatrocinadores().size());

            // Validar tamaño maximo del registro
            if(equipo.bytesSerializacionEquipo() > EquipoRandom.TAM_REGISTRO){
                System.out.println("El equipo excede el tamaño máximo permitido de "
                        + EquipoRandom.TAM_REGISTRO + " bytes.");
                return false;
            }

            archivo.escribirEquipo(equipo);
            return true;
        } catch (IllegalArgumentException e){
            System.out.println("Datos inválidos: " + e.getMessage());
            return false;
        } catch (RuntimeException e){
            System.out.println("Error de persistencia al guardar el equipo: " + e.getMessage());
            return false;
        }
    }

    /**
     * Busca un equipo por su ID en el archivo.
     * No compruba null, eso lo delega al llamador.
     *
     * @param idEquipo El ID del equipo a buscar.
     * @return El objeto Equipo si se encuentra, o null si no se encuentra.
     */
    public Equipo buscarEquipoPorID(int idEquipo){
        if(idEquipo <= 0){
            System.out.println("ID de equipo inválido: " + idEquipo);
            return null;
        }
        try{
            return archivo.leerEquipoPorID(idEquipo);
        } catch (RuntimeException e){
            System.out.println("Error de persistencia al buscar el equipo: " + e.getMessage());
            return null;
        }
    }

    /**
     * Muestra la información de un equipo por su ID.
     *
     * @param idEquipo El ID del equipo a mostrar.
     */
    public void mostrarEquipoPorID(int idEquipo) {
        if(idEquipo <= 0){
            System.out.println("ID de equipo inválido: " + idEquipo);
            return;
        }
        Equipo equipo = buscarEquipoPorID(idEquipo);
        if(equipo == null){
            System.out.println("Error: El equipo con ID " + idEquipo + " no existe o está marcado como borrado.");
        }
        System.out.println(equipo);
    }

    /**
     * Lista todos los equipos no borrados en el archivo.
     */
    public void listarEquipos() {
        Equipo equipo;

        for (int id = 1; id <= archivo.numeroRegistrosConBorrados(); id++) {
            try {
                equipo = archivo.leerEquipoPorID(id);
                if (equipo != null && !equipo.isBorrado()) {
                    System.out.println(equipo);
                }
            } catch (RuntimeException e) {
                System.out.println("Error de persistencia al leer el equipo con ID " + id + ": " + e.getMessage());
            }
        }
    }

    /**
     * Elimina (marca como borrado) un equipo por su ID.
     *
     * @param idEquipo El ID del equipo a eliminar.
     */
    public void eliminarEquipoPorID(int idEquipo) {

        // Valido parametro de entrada
        if(idEquipo <= 0){
            System.out.println("ID de equipo inválido: " + idEquipo);
            return;
        }
        try{

            Equipo equipo = buscarEquipoPorID(idEquipo);

            // Valido que el equipo existe y no está borrado
            if(equipo == null){
                System.out.println("Error: El equipo con ID " + idEquipo + " no existe o ya está marcado como borrado.");
                return;
            }

            System.out.println("EQUIPO A BORRAR ");
            System.out.println(equipo);
            equipo.setBorrado(true);

            // Reescribo el equipo marcado como borrado en su posición
            archivo.escribirEquipo(equipo);
            System.out.println("Equipo con ID " + idEquipo + "  borrado correctamente.");

        } catch (RuntimeException e){
            System.out.println("Error de persistencia al eliminar el equipo: " + e.getMessage());
        }
    }

    /**
     * Agrega o actualiza un patrocinador en un equipo.
     * Si el patrocinador ya existe (mismo nombre), se actualizan sus datos.
     * Si no existe, se añade al conjunto de patrocinadores del equipo.
     *
     * @param idEquipo        El ID del equipo al que se agrega o actualiza el patrocinador.
     * @param nuevoPatrocinador El objeto Patrocinador a agregar o actualizar.
     */
    public void agregarOActualizarPatrocinador(int idEquipo, Patrocinador nuevoPatrocinador) {

        boolean encontrado = false;


        if(idEquipo <= 0){
            System.out.println("ID de equipo inválido: " + idEquipo);
            return;
        }
        try{
            Equipo equipo = buscarEquipoPorID(idEquipo);

            // Valido que el equipo existe y no está borrado
            if(equipo == null){
                System.out.println("Error: El equipo con ID " + idEquipo + " no existe o está marcado como borrado.");
                return;
            }

            // Recorro los patrocinadores del equipo para ver si ya existe

            Set<Patrocinador > patrocinadores = equipo.getPatrocinadores();
            if(patrocinadores == null){
            patrocinadores = new HashSet<>();
            }
            for(Patrocinador p: patrocinadores){
                if(p.getNombre().equalsIgnoreCase(nuevoPatrocinador.getNombre())){
                    p.setDonacion(nuevoPatrocinador.getDonacion());
                    p.setFechaInicio(nuevoPatrocinador.getFechaInicio());
                    encontrado = true;
                    System.out.println("Patrocinador " + p.getNombre() + " actualizado en el equipo " + equipo.getNombre());
                    break;
                }
            }

            // Si no se encontró, se añade
            if(!encontrado){
                patrocinadores.add(nuevoPatrocinador);
                System.out.println("Patrocinador " + nuevoPatrocinador.getNombre() + " agregado al equipo " + equipo.getNombre());
            }

            // Guardo los cambios en el fichero
            equipo.setPatrocinadores(patrocinadores);
            equipo.setNumPatrocinadores(patrocinadores.size());

            // Validar tamaño maximo del registro
            if(equipo.bytesSerializacionEquipo() > EquipoRandom.TAM_REGISTRO){
                System.out.println("El equipo excede el tamaño máximo permitido de "
                        + EquipoRandom.TAM_REGISTRO + " bytes después de agregar/actualizar el patrocinador.");
                return;
            }
            archivo.escribirEquipo(equipo);

        } catch (RuntimeException e){
            System.out.println("Error de persistencia al agregar/actualizar el patrocinador: " + e.getMessage());
        }
    }

    public void eliminarPatrocinador(int idEquipo, String nombrePatrocinador) {
        if(idEquipo <= 0){
            System.out.println("ID de equipo inválido: " + idEquipo);
            return;
        }
        try{
            Equipo equipo = buscarEquipoPorID(idEquipo);

            // Valido que el equipo existe y no está borrado
            if(equipo == null){
                System.out.println("Error: El equipo con ID " + idEquipo + " no existe o está marcado como borrado.");
                return;
            }

            Set<Patrocinador> patrocinadores = equipo.getPatrocinadores();
            if(patrocinadores == null || patrocinadores.isEmpty()){
                System.out.println("El equipo no tiene patrocinadores para eliminar.");
                return;
            }

            // Intento buscar y eliminar el patrocinador por nombre

            boolean eliminado = patrocinadores.removeIf(p -> p.getNombre().equalsIgnoreCase(nombrePatrocinador));

            // removeIf devuelve true si se eliminó al menos un elemento. Es un método que elimina todos los elementos de
            // una colección que cumplen una condición dada.

            if(eliminado){
                System.out.println("Patrocinador " + nombrePatrocinador + " eliminado del equipo " + equipo.getNombre());
                // Actualizo el número de patrocinadores
                equipo.setNumPatrocinadores(patrocinadores.size());
                // Guardo los cambios en el fichero
                archivo.escribirEquipo(equipo);;
            } else {
                System.out.println("Patrocinador " + nombrePatrocinador + " no encontrado en el equipo " + equipo.getNombre());
            }

        } catch (RuntimeException e){
            System.out.println("Error de persistencia al eliminar el patrocinador: " + e.getMessage());
        }
    }


}
