/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persistencia;

import logica.EmpresaDAO;
import modelo.Departamento;
import modelo.Proxecto;
import modelo.VehiculoPropio;
import util.GestorConexiones;
import util.TipoSGBD;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Especificar un metodo para metadatos de tablas a pedir
 * @author usuario
 */
public class GestorEmpresa {

    private Connection conexion;
    private EmpresaDAO dao;

    /**
     * Constructor de GestorEmpresa
     * @param conexion conexión a la base de datos
     * @throws SQLException
     */
    public GestorEmpresa(Connection conexion) throws SQLException {
        this.conexion = conexion;
        this.dao = new EmpresaDAO(conexion);
    }

    /**
     * Mostrar metadatos de la base de datos
     */
    public void mostrarMetadatos() {
        try {
            GestorConexiones.obtenerMetadatos(conexion);
        } catch (Exception e) {
            System.out.println("Error al obtener metadatos: " + e.getMessage());
        }
    }

    // EJERCICIO 6: Mostrar departamentos e insertar proyecto

    /**
     * Mostrar departamentos de la base de datos
     */
    public void mostrarDepartamentos() {
        try {
            List<Departamento> departamentos = dao.mostrarDepartamentos();
            for (Departamento d : departamentos) {
                System.out.println(d);
            }
        } catch (Exception e) {
            System.out.println("Error al mostrar departamentos: " + e.getMessage());
        }
    }

    /**
     * Insertar un nuevo proyecto si no existe otro con el mismo nombre
     * @param p proyecto a insertar
     */
    public void insertarProyecto(Proxecto p) {
        try{
            if(!dao.existeProyecto(p.getNomeProxecto())) {
                dao.insertarProyecto(p);
                System.out.println("Proyecto insertado: " + p.getNomeProxecto());
            } else {
                System.out.println("Ya existe un proyecto con ese nombre: " + p.getNomeProxecto());
            }
        } catch(Exception e){
            System.out.println("Error al insertar proyecto: " + e.getMessage());
        }
    }

    // Ejercicio 7: Crear tablas Familiares y Vehiculos

    public void crearTablasFamiliaresYVehiculos() {
        try {
            GestorConexiones.borrarTablas(conexion, "Familiar", "VEHICULO_PROPIO",  "VEHICULO_RENTING", "Vehiculo");
            dao.crearTablaFamiliar();
            dao.crearTablaVehiculo();
            System.out.println("Tablas Familiares y Vehiculos creadas correctamente.");
        } catch (Exception e) {
            System.out.println("Error al crear tablas: " + e.getMessage());
            e.printStackTrace();

        }
    }
}
