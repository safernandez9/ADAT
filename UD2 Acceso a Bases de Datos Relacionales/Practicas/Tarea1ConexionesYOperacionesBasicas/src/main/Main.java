package main;

import modelo.Proxecto;
import persistencia.GestorEmpresa;
import util.GestorConexiones;
import util.TipoSGBD;

import java.sql.Connection;
import java.sql.SQLException;

public class Main {

    // REPASAR SQLITE

    public static void main(String[] args) {

        // Declaro fuera para que las coja el finally

        Connection mysql = null;
        Connection sqlServer = null;
        Connection sqlite = null;

        try {

            // Abro conexiones a las tres bases de datos

            sqlServer = GestorConexiones.getConnection(TipoSGBD.SQLSERVER, "BDEmpresa25", "sa", "abc123.");
            mysql = GestorConexiones.getConnection(TipoSGBD.MYSQL, "BDEmpresa25", "root", "abc123.");
            sqlite = GestorConexiones.getConnection(TipoSGBD.SQLITE, "C://sqlite3//bdempresa25.db", null, null);

            // Creo gestores para cada base de datos
            GestorEmpresa gestorSqlServer = new GestorEmpresa(sqlServer);
            GestorEmpresa gestorMysql = new GestorEmpresa(mysql);
            GestorEmpresa gestorSqlite = new GestorEmpresa(sqlite);

            // Mostrar metadatos de cada base de datos
            System.out.println("Metadatos SQL Server:");
            gestorSqlServer.mostrarMetadatos();
            System.out.println("\nMetadatos MySQL:");
            gestorMysql.mostrarMetadatos();
            System.out.println("\nMetadatos SQLite:");
            gestorSqlite.mostrarMetadatos();

            // Ejercicio 6A: Mostrar departamentos de cada base de datos
            System.out.println("\nDepartamentos SQL Server:");
            gestorSqlServer.mostrarDepartamentos();
            System.out.println("\nDepartamentos MySQL:");
            gestorMysql.mostrarDepartamentos();
            System.out.println("\nDepartamentos SQLite:");
            gestorSqlite.mostrarDepartamentos();

            // Ejercicio 6B: Insertar un nuevo proyecto en cada base de datos

            Proxecto nuevoProyecto = new Proxecto(999, "ProxectoSaul", "Cangas", 4);

            System.out.println("\nInsertando proyecto en SQL Server:");
            gestorSqlServer.insertarProyecto(nuevoProyecto);
            System.out.println("\nInsertando proyecto en MySQL:");
            gestorMysql.insertarProyecto(nuevoProyecto);
            System.out.println("\nInsertando proyecto en SQLite:");
            gestorSqlite.insertarProyecto(nuevoProyecto);

            // Ejercicio 7: DDL. Crear tablas Familiares y Vehiculos en cada base de datos

            System.out.println("\nCreando tablas en SQL Server:");
            gestorSqlServer.crearTablasFamiliaresYVehiculos();
            System.out.println("\nCreando tablas en MySQL:");
            gestorMysql.crearTablasFamiliaresYVehiculos();
            System.out.println("\nCreando tablas en SQLite:");
            gestorSqlite.crearTablasFamiliaresYVehiculos();

            // Comprobamos que las tablas se han creado correctamente mostrando los metadatos de nuevo

            System.out.println("\nMetadatos SQL Server tras crear tablas:");
            gestorSqlServer.mostrarMetadatos();
            System.out.println("\nMetadatos MySQL tras crear tablas:");
            gestorMysql.mostrarMetadatos();
            System.out.println("\nMetadatos SQLite tras crear tablas:");
            gestorSqlite.mostrarMetadatos();


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
