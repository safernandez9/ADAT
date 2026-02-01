// Saul Fernandez Salgado 77013586H
package persistencia;

import logica.ExposicionDAO;
import modelo.Fotografia;
import utilidades.GestorConexiones;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class GestorExposicion {

    private Connection conexion;
    private ExposicionDAO exposicionDAO;

    public GestorExposicion(Connection conexion){
        this.conexion = conexion;
        this.exposicionDAO = new ExposicionDAO(conexion);
    }

    // Ejercicio 1: Crear LaboratoriosFotograficos

    /**
     * Crea las tablas Laboratorio y FotografoColabora.
     * Falta borrar constraints por lo que solo las crea la primera vez
     */
    public void crearLaboratoriosFotograficos() {
        try {

            // Borramos tablas si ya existen
            GestorConexiones.borrarTablas(conexion, "LABORATORIO", "FOTOGRAFO_COLABORA");

            // Creamos las nuevas

            exposicionDAO.crearTablaLaboratorioYFotografoColabora();
            System.out.println("Tablas Laboratorio_Fotografico y Fotografo_Colabora creadas correctamente.");
            GestorConexiones.obtenerMetadatosTabla(conexion, "LABORATORIO");
            GestorConexiones.obtenerMetadatosTabla(conexion, "FOTOGRAFO_COLABORA");
        } catch (Exception e) {
            System.out.println("Error al crear tablas: " + e.getMessage());
        }
    }

    // Ejercicio 2: Insertar fotos

    /**
     * Comprueba Existencia de nombreFotografo y nombreExposicion e inserta la fotografia en la base de datos
     * para ese fotografo en esa exposicion
     * @param nombreFotografo
     * @param nombreExposicion
     * @param fotos
     */
    public void insertarFotografias(String nombreFotografo, String nombreExposicion, List<Fotografia> fotos){

        int codExpo;
        int codFotografo;

        try {
            if((codFotografo = exposicionDAO.existeFotografo(nombreFotografo)) < 0){
                System.out.println("Fotógrafo " + nombreFotografo + " no existe en la base de datos.");
                return;
            }

            if((codExpo = exposicionDAO.existeExposicion(nombreExposicion)) < 0){
                System.out.println("Exposición " + nombreExposicion + " no existe en la base de datos.");
                return;
            }

            for(Fotografia f: fotos){
                f.setCodFotografo(codFotografo);
                f.setCodExposicion(codExpo);
            }

            exposicionDAO.insertarFotografias(fotos);
            System.out.println("Fotografia insertada correctamente.");

        } catch (SQLException e) {
            System.out.println("Error al insertar fotografias: " + e.getMessage());
        }
    }

    // Ejercicio 3: Trasladar fotografias

    /**
     * Comprueba la existencia de las exposiciones, obtiene la provincia y localidad de la primera
     * y lo imprime por pantalla
     * @param expoOrigen
     * @param expoDestino
     */
    public void trasladarFotografias(String expoOrigen, String expoDestino){

        int codExpoOrigen, codExpoDestino;
        StringBuilder sb = new StringBuilder();

        try {
            if ((codExpoOrigen = exposicionDAO.existeExposicion(expoOrigen)) < 0) {
                System.out.println("Exposición " + expoOrigen + " no existe en la base de datos.");
                return;
            }

            if ((codExpoDestino = exposicionDAO.existeExposicion(expoDestino)) < 0) {
                System.out.println("Exposición " + expoDestino + " no existe en la base de datos.");
                return;
            }

            sb.append("NOMBRE EXPOSICIÖN: " + expoOrigen + " ");

            String localidadExpo = exposicionDAO.obtenerLocalidadExpo(expoOrigen);
            sb.append(localidadExpo.toUpperCase());




            System.out.println(sb.toString() + "\nFOTOGRAFÍAS: ");

        }catch(SQLException e){
            System.out.println("Error en el traslado de fotografias: " + e.getMessage());
        }

    }



}
