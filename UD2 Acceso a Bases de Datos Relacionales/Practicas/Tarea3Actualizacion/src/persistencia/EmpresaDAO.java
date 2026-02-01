/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persistencia;

import dtos.ProyectoEmpleado;
import modelo.*;
import utilidades.GestorConexiones;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author usuario
 */

/**
 * Clase DAO para gestionar las operaciones de la base de datos relacionadas con la empresa.
 * Apunte:
 * Los datos char y varchar de SQL, en las sentencias parametrizadas se utiliza setString.
 * Debemos tener cuidado si en la BD tenemos datos char(1), y  en java, en la clase tenemos un campo como char,
 * tenemos que convertir el campo char a String para utilizarlo en la sentencia parametrizada y darle valor
 * con setString. Ejemplo; si color es char,
 * sentencia.setString(7, String.valueOf(foto.getColor()));
 */
public class EmpresaDAO {

    Connection conexion;

    public EmpresaDAO(Connection conexion) {
        this.conexion = conexion;
    }

    // Ejercicio 1: Insertar Familiar

    /**
     * Inserta un familiar en la base de datos.
     *
     * @param familiar El objeto Familiar a insertar.
     * @throws SQLException Si ocurre un error durante la operación de inserción.
     */
    public void insertarFamiliar(Familiar familiar) throws SQLException {

        try {
            // Obtener el máximo número de familiar para el empleado dado

            int nuevoNumero = 0;

            String sqlMax = """
                    SELECT COALESCE(MAX(NUM_FAMILIAR), 0)
                    FROM FAMILIAR
                    WHERE NSS_EMPLEADO = ?
                    """;

            try (ResultSet rs = GestorConexiones.ejecutarConsulta(conexion, sqlMax, familiar.getNssEmpregado())) {
                if (rs.next()) {
                    nuevoNumero = rs.getInt(1) + 1;
                }
            } catch (SQLException e) {
                throw new SQLException("Error al obtener el máximo número de familiar: " + e.getMessage());
            }

            // Insertar el nuevo familiar con el nuevo número


            String sqlInsert = """
                        INSERT INTO FAMILIAR (NSS_EMPLEADO, NUM_FAMILIAR, NSS_FAMILIAR, NOME, APELIDOS, DATA_NACEMENTO, PARENTESCO, SEXO)
                        VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                    """;

            int filas = GestorConexiones.ejecutarSentencia(conexion, sqlInsert,
                    familiar.getNssEmpregado(),
                    nuevoNumero,
                    familiar.getNssFamiliar(),
                    familiar.getNome(),
                    familiar.getApelidos(),
                    familiar.getDataNacemento(),
                    familiar.getParentesco(),
                    String.valueOf(familiar.getSexo())
            );
            if (filas == 0) {
                throw new SQLException("No se pudo insertar el familiar.");
            }
        } catch (SQLException e) {
            throw new SQLException("Error al insertar el familiar: " + e.getMessage());
        }
    }

    /**
     * Comprueba si un empleado con el NSS dado existe en la base de datos.
     *
     * @param nssEmpregado El NSS del empleado a comprobar.
     * @return true si el empleado existe, false en caso contrario.
     * @throws SQLException Si ocurre un error durante la operación de consulta.
     */
    public boolean comprobarExistenciaEmpleado(String nssEmpregado) throws SQLException {

        String sql = "SELECT COUNT(*) AS TOTAL FROM empregado WHERE NSS = ?";

        try (ResultSet rs = GestorConexiones.ejecutarConsulta(conexion, sql, nssEmpregado)) {
            if (rs.next()) {
                int count = rs.getInt("TOTAL");
                return true;
            }
        } catch (SQLException e) {
            throw new SQLException("Error al comprobar la existencia del empleado: " + e.getMessage());
        }

        return false;
    }

    // Ejercicio 2: Insertar Vehículo

    /**
     * Comprueba si un vehículo con la matrícula dada existe en la base de datos.
     *
     * @param matricula La matrícula del vehículo a comprobar.
     * @return true si el vehículo existe, false en caso contrario.
     * @throws SQLException Si ocurre un error durante la operación de consulta.
     */
    public boolean vehiculoExiste(String matricula) throws SQLException {

        String sql = "SELECT COUNT(*) AS TOTAL FROM VEHICULO WHERE Matricula = ?";

        try (ResultSet rs = GestorConexiones.ejecutarConsulta(conexion, sql, matricula)) {
            if (rs.next()) {
                int count = rs.getInt("TOTAL");
                if (count > 0) {
                    return true;
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Error al comprobar la existencia del vehículo: " + e.getMessage());
        }

        return false;
    }

    /**
     * Inserta un vehículo en la base de datos, manejando tanto vehículos propios como de renting.
     * Recupera la clave primaria generada para el vehículo insertado.
     *
     * @param vehiculo
     * @return
     * @throws SQLException
     */
    public void insertarVehiculo(Vehiculo vehiculo) throws SQLException {

        String sqlVehiculo = """
                    INSERT INTO vehiculo (Matricula, Marca, Modelo, TipoCombustible)
                    VALUES (?, ?, ?, ?)
                """;

        String sqlVehiculoPropio = """
                    INSERT INTO VEHICULO_PROPIO (CodVehiculo, DataCompra, Precio)
                    VALUES (?, ?, ?)
                """;

        String sqlVehiculoRenting = """
                    INSERT INTO VEHICULO_RENTING (CodVehiculo, FechaInicio, PrecioMensual, MesesContratados)
                    VALUES (?, ?, ?, ?)
                """;

        try {
            conexion.setAutoCommit(false); // para asegurar consistencia si hay varias inserciones

            // Insertamos en la tabla VEHICULO y recuperamos la PK generada

            int claveGenerada = GestorConexiones.ejecutarSentenciaRecuperandoClave(conexion, sqlVehiculo,
                    vehiculo.getMatricula(),
                    vehiculo.getMarca(),
                    vehiculo.getModelo(),
                    vehiculo.getCombustible()
            );

            // Segun el tipo de vehiculo, insertamos en la tabla correspondiente

            if (vehiculo instanceof VehiculoPropio) {

                VehiculoPropio vp = (VehiculoPropio) vehiculo;

                GestorConexiones.ejecutarSentencia(conexion, sqlVehiculoPropio,
                        claveGenerada,
                        vp.getDataCompra(),
                        vp.getPrezoPagado()
                );

            } else if (vehiculo instanceof VehiculoRenting) {
                VehiculoRenting vr = (VehiculoRenting) vehiculo;

                GestorConexiones.ejecutarSentencia(conexion, sqlVehiculoRenting,
                        claveGenerada,
                        vr.getDataInicio(),
                        vr.getPrezoMensual(),
                        vr.getMesesContratados()
                );
            }

            conexion.commit();

        } catch (Exception e) {
            conexion.rollback();
            throw new SQLException("Error al insertar vehículo: " + e.getMessage(), e);
        } finally {
            conexion.setAutoCommit(true); // restauramos estado original
        }
    }

    // Ejercicio 3: Cambiar Departamento que controla un Proyecto

    /**
     * Comprueba si un proyecto con el nombre dado existe en la base de datos.
     *
     * @param nombreProyecto El nombre del proyecto a comprobar.
     * @return true si el proyecto existe, false en caso contrario.
     */
    public boolean proyectoExisteNombre(String nombreProyecto) {

        String sql = "SELECT COUNT(*) AS TOTAL FROM proxecto WHERE NomeProxecto = ?";

        try{
            try (ResultSet rs = GestorConexiones.ejecutarConsulta(conexion, sql, nombreProyecto)) {
                if (rs.next()) {
                    int count = rs.getInt("TOTAL");
                    if (count > 0) {
                        return true;
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al comprobar la existencia del proyecto: " + e.getMessage());
        }
        return false;
    }

    /**
     * Comprueba si un departamento con el nombre dado existe en la base de datos.
     *
     * @param nombreNuevoDepartamento El nombre del departamento a comprobar.
     * @return true si el departamento existe, false en caso contrario.
     */
    public boolean departamentoExiste(String nombreNuevoDepartamento) {
        String sql = "SELECT COUNT(*) AS TOTAL FROM departamento WHERE NomeDepartamento = ?";

        try {
            try (ResultSet rs = GestorConexiones.ejecutarConsulta(conexion, sql, nombreNuevoDepartamento)) {
                if (rs.next()) {
                    int count = rs.getInt("TOTAL");
                    if (count > 0) {
                        return true;
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al comprobar la existencia del departamento: " + e.getMessage());
        }
        return false;
    }

    /**
     * Cambia el departamento que controla un proyecto.
     *
     * @param nombreProyecto         El nombre del proyecto.
     * @param nombreNuevoDepartamento El nombre del nuevo departamento.
     */
    public void cambiarDepartamentoProyecto(String nombreProyecto, String nombreNuevoDepartamento) throws SQLException {

        String sql = """
                UPDATE proxecto
                SET NumDepartControla = (Select NumDepartamento FROM departamento WHERE NomeDepartamento= ?)
                WHERE NomeProxecto = ?
                """;

        try {
            int filas = GestorConexiones.ejecutarSentencia(conexion, sql, nombreNuevoDepartamento, nombreProyecto);
            if (filas == 0) {
                throw new SQLException("No se pudo cambiar el departamento del proyecto.");
            }
        } catch (SQLException e) {
            throw new SQLException("Error al cambiar el departamento del proyecto: " + e.getMessage());
        }

    }

    // Ejercicio 4: Eliminar Proyecto

    /**
     * Comprueba si un proyecto con el número dado existe en la base de datos.
     *
     * @param numProxecto El número del proyecto a comprobar.
     * @return true si el proyecto existe, false en caso contrario.
     * @throws SQLException Si ocurre un error durante la operación de consulta.
     */
    public boolean proyectoExisteNum(int numProxecto) throws SQLException {

        String sql = "SELECT COUNT(*) AS TOTAL FROM proxecto WHERE NumProxecto = ?";

        try {
            try (ResultSet rs = GestorConexiones.ejecutarConsulta(conexion, sql, numProxecto)) {
                if (rs.next()) {
                    int count = rs.getInt("TOTAL");
                    if (count > 0) {
                        return true;
                    }
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Error al comprobar la existencia del proyecto: " + e.getMessage());
        }
        return false;
    }

    /**
     * Elimina un proyecto de la base de datos de forma segura.
     * Primero comprueba si el proyecto existe, imprime sus datos y los empleados asignados,
     * y luego elimina el proyecto utilizando una transacción para asegurar la integridad de los datos.
     * @param numProxecto
     * @throws SQLException
     */
    public ProyectoEmpleado eliminarProyecto(int numProxecto) throws SQLException {

        Proxecto p = null;
        Empregado em = null;
        List<Empregado> empleados = new ArrayList<>();

        //  CONSULTAS

        String sqlDatos = "SELECT * FROM PROXECTO WHERE NumProxecto = ?";

        String sqlDatosEmpleados = """
                    SELECT e.NSS, e.Nome, e.Apelido1, e.Apelido2
                    FROM empregado e
                    JOIN empregado_proxecto ep ON e.NSS = ep.NSSEmpregado 
                    JOIN proxecto p on ep.NumProxecto = p.NumProxecto
                    WHERE p.NumProxecto = ?
                """;


        // SACO DATOS PARA IMPRIMIR

        try (ResultSet rs = GestorConexiones.ejecutarConsulta(conexion, sqlDatos, numProxecto)) {

            while(rs.next()){
                p = new Proxecto(rs.getInt("NumProxecto"),
                        rs.getString("NomeProxecto"),
                        rs.getString("Lugar"),
                        rs.getInt("NumDepartControla"));
            }


            try (ResultSet rsEmps = GestorConexiones.ejecutarConsulta(conexion, sqlDatosEmpleados, numProxecto)) {

                while(rsEmps.next()){
                    em = new Empregado(rsEmps.getString("NSS"),
                            rsEmps.getString("Nome"),
                            rsEmps.getString("Apelido1"),
                            rsEmps.getString("Apelido2"));

                    empleados.add(em);
                }


            } catch (SQLException e) {
                throw new SQLException("Error al obtener los empleados asignados al proyecto: " + e.getMessage());
            }
        } catch (SQLException e) {
            throw new SQLException("Error al obtener los empleados asignados al proyecto: " + e.getMessage());
        }

        // Elimino el proyecto de forma segura con transacción.
        // Hay que eliminar también las relaciones en la tabla intermedia EMPREGADO_PROXECTO
        try {
            conexion.setAutoCommit(false);

            String sqlBorrarTablaIntermedia = "DELETE FROM empregado_proxecto WHERE NumProxecto = ?";
            GestorConexiones.ejecutarSentencia(conexion, sqlBorrarTablaIntermedia, numProxecto);

            String sqlBorrarProyecto = "DELETE FROM PROXECTO WHERE NumProxecto = ?";
            int filas = GestorConexiones.ejecutarSentencia(conexion, sqlBorrarProyecto, numProxecto);

            System.out.println("Proyecto eliminado correctamente.");
            conexion.commit();

            return new ProyectoEmpleado(p, empleados);

        } catch (SQLException e) {
            conexion.rollback();
            throw new SQLException("Error al eliminar el proyecto: " + e.getMessage());
        } finally {
            conexion.setAutoCommit(true);
        }
    }

    // Ejercicio 5: Incrementar Salarios Empleados Fijos

    /**
     * Comprueba si un empleado con el NSS dado es un empleado fijo.
     *
     * @param nss El NSS del empleado a comprobar.
     * @return true si el empleado es fijo, false en caso contrario.
     * @throws SQLException Si ocurre un error durante la operación de consulta.
     */
    public boolean esEmpleadoFijo(String nss) throws SQLException {

        String sql = "SELECT COUNT(*) AS TOTAL FROM empregadofixo WHERE NSS = ?";

        try {
            try (ResultSet rs = GestorConexiones.ejecutarConsulta(conexion, sql, nss)) {
                if (rs.next()) {
                    int count = rs.getInt("TOTAL");
                    if (count > 0) {
                        return true;
                    }
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Error al comprobar si el empleado es fijo: " + e.getMessage());
        }
        return false;
    }


    public void incrementarSalarioEmpleadosFijos(Double incremento, List<String> empleadosAfectados) {

        String sql = "UPDATE empregadofixo SET Salario = Salario + ? WHERE NSS = ?";
        List<String> lotesSql = new ArrayList<>();
        lotesSql.add(sql);

        try {
            conexion.setAutoCommit(false); // para asegurar consistencia si hay varias actualizaciones

            List<Object[]> parametros = new ArrayList<>();
            List<List<Object[]>> lotesParametros = new ArrayList<>();

            for (String nss : empleadosAfectados) {
                parametros.add(new Object[]{incremento, nss});
            }

            lotesParametros.add(parametros);
            GestorConexiones.ejecutarLoteTransaccionalPreparedStatement(conexion, lotesSql, lotesParametros);

        } catch (Exception e) {
            try {
                conexion.rollback();
            } catch (SQLException ex) {
                System.out.println("Error al hacer rollback: " + ex.getMessage());
            }
            System.out.println("Error al incrementar salarios: " + e.getMessage());
        } finally {
            try {
                conexion.setAutoCommit(true); // restauramos estado original
            } catch (SQLException e) {
                System.out.println("Error al restaurar autoCommit: " + e.getMessage());
            }
        }
    }
}


//
//
//
//    public boolean insertarProxecto (Proxecto p) throws SQLException {
//        String sql ="Select * FROM PROXECTO";
//
//        try(ResultSet rs = UtilidadesBD.crearResultSetActualizable(con, sql)){
//            rs.movetoinsertrow();
//
//            rs.updatecosas
//
//            rs.insertrow();
//            rs.movetocurrentrow();
//            returntrue
//        }
//    }
//
//
//    public int incrementarSalariosDepartamento(int incremento, int numdepartamento) throws sqlexception{
//        Sql
//
//                int afectados = 0;
//                try{
//                    conexion.setAutocomit(false);
//                    try(Resultset rs = utilide.crearactualizable(con, sql, numDEp)){
//while rs.next
//salario = getint
//updateINt
//updateROW
//adectados++
//commit
//return afectados
//}
//
//                    }
//                }
//    }
//
//
//    public int cambiarDepartamentoProyecto(String  nombreDep, String nombreProd){
//        try{
//            String sql = """
//                         """;
//
//            //IMPORTANTE ASEGURARME DE QUE EXECUTARSENTENCIA DEVULVA EL NUM DE FILAS AFECTADAS
//            int filas = UtilidadesBD.executarSentencia(conexion, sql, nombreDep, nombreProd);
//            if(filas == 0){
//
//
//            }
//
//
//
//            // VER IMPORTEKEY Y EXPORTEKEY
//
//             /**
//             * EmpresaDAO(TIPOSGBD) borrarProxecto cambiarDepartamentoProxecto
//             * cerrar() close() crearDTO(ResultSet rs) existeDepartamento
//             * existeEmpregadoFixo existeProxecto incrementarSalarioEmpr
//             * incrementarSalarioEmpr incrementarSalarioDep insertarFamili
//             * insertarProxecto insertarVehiculo
//                                                                                    obterEmpregadosConMas*
//             */
//
//
//             existeEmpregadoFixo(){
//                     1 Validacion previa si un nss no existe abortamos
//                             2 PReparo BAtvtch
//                                     ps
//                                      3 ejecutoBatch esto se hace con varias dll o de actualizacion
//                                                     4 contar resultados usando logica universal
//        }
//    }
//
//        ResultSet dinamico no se que es pero lo usa en insertar proyecto
//                se usa ResultSet.TYP_SCROL_SENSITIVE
//                        RESULTSET.CONCURUPDATE
//
//}



