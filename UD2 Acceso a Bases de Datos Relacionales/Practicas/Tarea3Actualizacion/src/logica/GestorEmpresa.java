/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package logica;

import dtos.ProyectoEmpleado;
import modelo.Empregado;
import modelo.Familiar;
import modelo.Vehiculo;
import persistencia.EmpresaDAO;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author usuario
 */
public class GestorEmpresa {

    Connection conexion;
    EmpresaDAO empresaDAO;

    public GestorEmpresa(Connection conexion) {
        this.conexion = conexion;
        this. empresaDAO = new EmpresaDAO(conexion);

    }

    /**
     * Inserta un familiar en la base de datos si el empleado asociado existe.
     *
     * @param familiar El objeto Familiar a insertar.
     */
    public void insertarFamiliar(Familiar familiar) {
        try {
            if (!empresaDAO.comprobarExistenciaEmpleado(familiar.getNssEmpregado())) {
                System.out.println("El empleado con NSS" + familiar.getNssEmpregado() + " no existe.");
                return;
            }

            empresaDAO.insertarFamiliar(familiar);
            System.out.println("Familiar insertado correctamente.");

        } catch (SQLException e) {
            System.out.println("Error al insertar familiar: " + e.getMessage());
        }
    }

    /**
     * Inserta un vehículo en la base de datos si no existe ya uno con la misma matrícula.
     *
     * @param vehiculo El objeto Vehiculo a insertar.
     * @throws SQLException Si ocurre un error durante la operación de base de datos.
     */
    public void insertarVehiculo(Vehiculo vehiculo) throws SQLException {

        try {
            if (empresaDAO.vehiculoExiste(vehiculo.getMatricula())) {
                System.out.println("El vehículo ya existe.");
                return;
            }
            empresaDAO.insertarVehiculo(vehiculo);
            System.out.println("Vehículo insertado correctamente.");
        } catch (SQLException e) {
            System.out.println("Error al insertar vehículo: " + e.getMessage());
            return;
        }
    }

    /**
     * Cambia el departamento que controla un proyecto.
     *
     * @param nombreProyecto         El nombre del proyecto.
     * @param nombreNuevoDepartamento El nombre del nuevo departamento.
     */
    public void cambiarDepartamentoProyecto(String nombreProyecto, String nombreNuevoDepartamento) {

        try {
            if (!empresaDAO.proyectoExisteNombre(nombreProyecto)) {
                System.out.println("El proyecto " + nombreProyecto + " no existe.");
                return;
            }

            if (!empresaDAO.departamentoExiste(nombreNuevoDepartamento)) {
                System.out.println("El departamento " + nombreNuevoDepartamento + " no existe.");
                return;
            }

            empresaDAO.cambiarDepartamentoProyecto(nombreProyecto, nombreNuevoDepartamento);
            System.out.println("Departamento del proyecto cambiado correctamente.");

        } catch (SQLException e) {
            System.out.println("Error al cambiar departamento del proyecto: " + e.getMessage());
        }
    }

    /**
     * Elimina un proyecto de la base de datos.
     *
     * @param numProxecto El número del proyecto a eliminar.
     */
    public void eliminarProyecto(int numProxecto) {
        try {
            if (!empresaDAO.proyectoExisteNum(numProxecto)) {
                System.out.println("El proyecto con número " + numProxecto + " no existe.");
                return;
            }

            ProyectoEmpleado p = empresaDAO.eliminarProyecto(numProxecto);
            System.out.println("Empleados afectados por la eliminación del proyecto:");
            for(Empregado e: p.getEmpregadosProxecto()){
                System.out.println("- " + e.getNss() + " " + e.getNombre() + " " + e.getApellido1() + " " + e.getApellido2());
            }
            System.out.println("Datos del proyecto eliminado:");
            System.out.println(p.getP().toString());

            System.out.println("Proyecto eliminado correctamente.");

        } catch (SQLException e) {
            System.out.println("Error al eliminar proyecto: " + e.getMessage());
        }
    }

    public void incrementarSalarioEmpleadosFijos(Double incremento, List<String> empleadosAfectados) {

        try{
            // Borro de la lista los empleados que no existen o no son fijos
            for(String nss : empleadosAfectados){
                if(!empresaDAO.esEmpleadoFijo(nss)){
                    System.out.println("El empleado con NSS " + nss + " no existe.");
                    empleadosAfectados.remove(nss);
                }
            }

            if(empleadosAfectados.isEmpty()){
                System.out.println("No hay empleados fijos para incrementar el salario.");
                return;
            }

            empresaDAO.incrementarSalarioEmpleadosFijos(incremento, empleadosAfectados);
            System.out.println("Salarios incrementados correctamente.");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }


//                    incrementarSalarios
//                            incrementarSalariosDEP
//                            insertarFamiliar
//                                    insertarVehiculo
//
//
//
//    insertarProxecto
                                            
}
