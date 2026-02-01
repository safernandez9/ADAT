import logica.GestorEmpresa;
import utiles.GestorConexiones;
import utiles.TipoSGBD;

import java.sql.Connection;
import java.util.function.Consumer;

public class Main {

    public static void main(String[] args) {

        // Como funcionan estas lambda?
        ejecutarPorSGBD(con -> GestorEmpresa.ejercicio1(con));  //OK
        ejecutarPorSGBD(con -> GestorEmpresa.ejercicio2(con)); //OK
        ejecutarPorSGBD(con -> GestorEmpresa.ejercicio3(con)); //OK
        ejecutarPorSGBD(con -> GestorEmpresa.ejercicio4(con, "TÉCNICO")); //OK
        ejecutarPorSGBD(con -> GestorEmpresa.ejercicio5(con, "PORTAL", "SANTIAGO")); //OK
        ejecutarPorSGBD(con -> GestorEmpresa.ejercicio6(con)); //OK
        ejecutarPorSGBD(con -> GestorEmpresa.ejercicio7(con, 5)); //OK
        ejecutarPorSGBD(con -> GestorEmpresa.ejercicio8(con, 2000)); //OK
        ejecutarPorSGBD(con -> GestorEmpresa.ejercicio9(con)); //OK
        ejecutarPorSGBD(con -> GestorEmpresa.ejercicio10(con)); //OK

    }

    /**
     * 
     * @param accion 
     */
    private static void ejecutarPorSGBD(Consumer<Connection> accion) {
       ejecutar(TipoSGBD.SQLSERVER, "BDEmpresa25", "sa", "abc123.", accion);
       ejecutar(TipoSGBD.MYSQL, "BDEmpresa25", "root", "abc123.", accion);
       ejecutar(TipoSGBD.SQLITE, "C://sqlite3//bdempresa25.db", null, null, accion);
    }

    /**
     * 
     * @param tipo
     * @param ruta
     * @param user
     * @param pass
     * @param accion 
     */
    private static void ejecutar(TipoSGBD tipo, String ruta, String user, String pass, Consumer<Connection> accion) {
        
        System.out.println("\nSGBD: " + tipo);
        try (Connection con = GestorConexiones.getConnection(tipo, ruta, user, pass)) {
            accion.accept(con);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
