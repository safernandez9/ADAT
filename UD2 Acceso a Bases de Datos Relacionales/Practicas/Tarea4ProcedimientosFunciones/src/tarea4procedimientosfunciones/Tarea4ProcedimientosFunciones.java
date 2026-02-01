/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package tarea4procedimientosfunciones;

import logica.GestorEmpresa;
import utilidades.GestorConexiones;
import utilidades.TipoSGBD;

import java.sql.Connection;
import java.sql.SQLException;

/**
 *
 * @author usuario
 */
public class Tarea4ProcedimientosFunciones {

    static Connection mysql = null;
    static Connection sqlServer = null;

    public static void main(String[] args) {

        try {
            // Abro conexiones a las tres bases de datos

            sqlServer = GestorConexiones.getConnection(TipoSGBD.SQLSERVER, "BDEmpresa25", "sa", "abc123.");
            mysql = GestorConexiones.getConnection(TipoSGBD.MYSQL, "BDEmpresa25", "root", "abc123.");

            // Creo gestores para cada base de datos
            GestorEmpresa gestorSqlServer = new GestorEmpresa(sqlServer);
            GestorEmpresa gestorMysql = new GestorEmpresa(mysql);

            // EJERCICIO 1: CambioDomicilio
            System.out.println("EJERCICIO 1: Cambio Domicilio");
            System.out.println("Sql Server:");
            gestorSqlServer.cambioDomicilio("0010010", "Calle Nueva", 45, "2B", "28080", "Madrid");
            System.out.println("MySQL:");
            gestorMysql.cambioDomicilio("0010010", "Calle Nueva", 45, "2B", "28080", "Madrid");

            // EJERCICIO 2: DatosProxecto
            System.out.println();
            System.out.println("EJERCICIO 2: Datos Proxecto");
            System.out.println("Sql Server:");
            gestorSqlServer.datosProxecto(1);
            System.out.println("MySQL:");
            gestorMysql.datosProxecto(1);

            // EJERCICIO 3: CambiarDepartamentoPorNumeroProyectos
            System.out.println();
            System.out.println("EJERCICIO 3: Cambiar Departamento Por Número Proyecto");
            System.out.println("Sql Server:");
            gestorSqlServer.cambiarDepartamentoPorNumeroProyectos(3);
            System.out.println("MySQL:");
            gestorMysql.cambiarDepartamentoPorNumeroProyectos(3);

            // EJERCICIO 4: NumeroEmpregadosDepartamento
            System.out.println();
            System.out.println("EJERCICIO 4: Numero de Empleados en un Departaamento");
            System.out.println("Sql Server:");
            gestorSqlServer.numeroEmpregadosDepartamento("PERSOAL");
            System.out.println("MySQL:");
            gestorMysql.numeroEmpregadosDepartamento("PERSOAL");


        } catch (SQLException e) {
            System.out.println("Error en la base de datos: " + e.getMessage());
        } finally {

            // Cierro todas las conexiones al final
            try {
                GestorConexiones.cerrarConexion(mysql);
                GestorConexiones.cerrarConexion(sqlServer);
            } catch (Exception e) {
                System.out.println("Error al cerrar conexiones: " + e.getMessage());
            }

        }
    }
}
