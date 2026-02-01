package tarea3actualizacion;

import logica.GestorEmpresa;
import modelo.Familiar;
import modelo.VehiculoPropio;
import modelo.VehiculoRenting;
import utilidades.GestorConexiones;
import utilidades.TipoSGBD;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class Tarea3Actualizacion {

    static Connection mysql = null;
    static Connection sqlServer = null;
    static Connection sqlite = null;

    public static void main(String[] args) {

        try {
            // Abro conexiones a las tres bases de datos

            sqlServer = GestorConexiones.getConnection(TipoSGBD.SQLSERVER, "BDEmpresa25", "sa", "abc123.");
            mysql = GestorConexiones.getConnection(TipoSGBD.MYSQL, "BDEmpresa25", "root", "abc123.");
            sqlite = GestorConexiones.getConnection(TipoSGBD.SQLITE, "C://sqlite3//bdempresa25.db", null, null);

            // Creo gestores para cada base de datos
            GestorEmpresa gestorSqlServer = new GestorEmpresa(sqlServer);
            GestorEmpresa gestorMysql = new GestorEmpresa(mysql);
            GestorEmpresa gestorSqlite = new GestorEmpresa(sqlite);

            // EJERCICIO 1: InsertarFamiliar. Un familiar válido y uno inválido (empleado no existe)

            System.out.println("EJERCICIO 1: Insertar Familiar");
            Familiar f = new Familiar("1111111", "99999999A", "Ana", "Lopez", Date.valueOf("1990-05-15"), "Hermana", 'M');
            Familiar f2 = new Familiar("0000000", "88888888B", "Luis", "Garcia", Date.valueOf("1985-03-20"), "Hermano", 'H');
            System.out.println("Sql Server:");
            gestorSqlServer.insertarFamiliar(f);
            gestorSqlServer.insertarFamiliar(f2);
            System.out.println("MySQL:");
            gestorMysql.insertarFamiliar(f);
            gestorMysql.insertarFamiliar(f2);
            System.out.println("SQLite:");
            gestorSqlite.insertarFamiliar(f);
            gestorSqlite.insertarFamiliar(f2);


            // EJERCICIO 2: Insertar Vehiculo.

            System.out.println("EJERCICIO 2: Insertar Vehículo");
            VehiculoPropio v1 = new VehiculoPropio("1234ABC", "Toyota",
                    "Corolla", "G", Date.valueOf("2023-05-15"), 15000);
            VehiculoRenting v2 = new VehiculoRenting("5678XYZ", "Ford",
                    "Fiesta", "D", Date.valueOf("2023-07-01"), 200, 24);
            System.out.println("Sql Server:");
            gestorSqlServer.insertarVehiculo(v1);
            gestorSqlServer.insertarVehiculo(v2);
            System.out.println("MySQL:");
            gestorMysql.insertarVehiculo(v1);
            gestorMysql.insertarVehiculo(v2);
            System.out.println("SQLite:");
            gestorSqlite.insertarVehiculo(v1);
            gestorSqlite.insertarVehiculo(v2);


            // EJERCICIO 3: Cambiar el departamento que controla un proyecto.

            System.out.println("EJERCICIO 3: Cambiar departamento que controla un proyecto");
            String nombreProyecto = "PROXECTO X";
            String nombreNuevoDepartamento = "CONTABILIDAD";
            System.out.println("Sql Server:");
            gestorSqlServer.cambiarDepartamentoProyecto(nombreProyecto, nombreNuevoDepartamento);
            System.out.println("MySQL:");
            gestorMysql.cambiarDepartamentoProyecto(nombreProyecto, nombreNuevoDepartamento);
            System.out.println("SQLite:");
            gestorSqlite.cambiarDepartamentoProyecto(nombreProyecto, nombreNuevoDepartamento);


            // EJERCICIO 4: Eliminar un proyecto.

            int numProxecto = 3;
            System.out.println("EJERCICIO 4: Eliminar un proyecto");
            System.out.println("Sql Server:");
            gestorSqlServer.eliminarProyecto(numProxecto);
            System.out.println("MySQL:");
            gestorMysql.eliminarProyecto(numProxecto);
            System.out.println("SQLite:");
            gestorSqlite.eliminarProyecto(numProxecto);

            // EJERCICIO 5: Incrementar salario de una lista de empleados fijos.

            Double incremento = 500.0;
            List<String> empleadosAfectados = new ArrayList<>();
            empleadosAfectados.add("7777777");
            empleadosAfectados.add("1111111");
            System.out.println("EJERCICIO 5: Incrementar salario de dos empleados de un departamento");
            System.out.println("Sql Server:");
            gestorSqlServer.incrementarSalarioEmpleadosFijos(incremento, empleadosAfectados);
            System.out.println("MySQL:");
            gestorMysql.incrementarSalarioEmpleadosFijos(incremento, empleadosAfectados);
            System.out.println("SQLite:");
            gestorSqlite.incrementarSalarioEmpleadosFijos(incremento, empleadosAfectados);



        } catch (SQLException e) {
            System.out.println("Error en la base de datos: " + e.getMessage());
        } finally {

            // Cierro todas las conexiones al final
            try {
                GestorConexiones.cerrarConexion(mysql);
                GestorConexiones.cerrarConexion(sqlServer);
                GestorConexiones.cerrarConexion(sqlite);
            } catch (Exception e) {
                System.out.println("Error al cerrar conexiones: " + e.getMessage());
            }

        }
    }

}
