// Saul Fernandez Salgado 77013586H

import modelo.Fotografia;
import modelo.FotografiaArtistica;
import modelo.FotografiaDocumental;
import persistencia.GestorExposicion;
import utilidades.GestorConexiones;
import utilidades.TipoSGBD;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Main {

    static Connection sqlServer = null;

    public static void main(String[] args) {

            try {

                // Creo conexion y gestor

                sqlServer = GestorConexiones.getConnection(TipoSGBD.SQLSERVER, "BDEXPOSICION26", "sa", "abc123.");
                GestorExposicion gestorExposicion = new GestorExposicion(sqlServer);

                // Ejercicio 1

                System.out.println("EJERCICIO 1");
                gestorExposicion.crearLaboratoriosFotograficos();
                System.out.println();

                // Ejercicio 2

                System.out.println("EJERCICIO 2");
                List<Fotografia> listaFotos = new ArrayList<>();
                listaFotos.add(new FotografiaArtistica("FOTOARTMI", "GRANDE", LocalDate.of(2020,12, 20) ,
                        'S', "Encuadrado", "Compleja"));
                listaFotos.add(new FotografiaDocumental("FOTODOCMI", "MEDIANO", LocalDate.of(2020,11, 24),
                        'N', "Tipo123456"));

                System.out.println("\nInsercion correcta: ");
                gestorExposicion.insertarFotografias("AMELIE", "FASCINUS", listaFotos);

                System.out.println("\nFotógrafo no existente: ");
                gestorExposicion.insertarFotografias("SAUL", "FASCINUS", listaFotos);

                System.out.println("\nExposición no existente: ");
                gestorExposicion.insertarFotografias("AMELIE", "CHANDOMONTE", listaFotos);

                // Meto mas caracteres en tipo de los que sooporta el campo para que falles
                System.out.println("\nInsercion que corte la transacción: ");
                listaFotos.add(new FotografiaDocumental("NOSEINSERTA", "MEDIANO", LocalDate.of(2020,11, 24),
                        'N', "Tipo12345632"));
                gestorExposicion.insertarFotografias("AMELIE", "FASCINUS", listaFotos);
                System.out.println();


                // Ejercicio 3

                System.out.println("EJERCICIO 3");
                System.out.println("\nCaso Correcto");
                gestorExposicion.trasladarFotografias("Invisible", "Fascinus");
                System.out.println("\nCaso primero no existe");
                gestorExposicion.trasladarFotografias("SAUL", "Fascinus");
                gestorExposicion.trasladarFotografias("Invisible", "SAUL");



            } catch (SQLException e) {
                System.out.println("Error en la base de datos: " + e.getMessage());
            } finally {

                // Cierro todas las conexiones al final
                try {
                    GestorConexiones.cerrarConexion(sqlServer);
                } catch (Exception e) {
                    System.out.println("Error al cerrar conexiones: " + e.getMessage());
                }

            }
    }
}
