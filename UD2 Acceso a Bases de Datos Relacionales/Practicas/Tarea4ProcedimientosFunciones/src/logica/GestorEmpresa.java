package logica;

import persistencia.EmpresaDAO;
import utilidades.GestorConexiones;
import utilidades.ResultadoProcedimiento;

import java.sql.Connection;
import java.sql.ResultSet;

public class GestorEmpresa {

    private Connection conexion;
    private EmpresaDAO empresaDAO;

    public GestorEmpresa(Connection conexion) {
        this.conexion = conexion;
        this.empresaDAO = new EmpresaDAO(conexion);
    }

    /**
     * Ejercicio 1
     * @param nss
     * @param rua
     * @param numero
     * @param piso
     * @param cp
     * @param localidade
     */
    public void cambioDomicilio(String nss, String rua, int numero, String piso, String cp, String localidade) {
        try {
            ResultadoProcedimiento res = empresaDAO.cambioDomicilio(nss, rua, numero, piso, cp, localidade);
            System.out.println("Domicilio cambiado correctamente.");
            System.out.println("Filas afectadas: " + res.getUpdateCount());
            System.out.println("Nuevos datos del empleado:");

            String sqlSelect = "SELECT Rua, Numero_Calle, Piso, CP, Localidade FROM EMPREGADO WHERE NSS = ?";
            try (ResultSet rs = GestorConexiones.ejecutarConsulta(conexion, sqlSelect, nss)) {
                if (rs.next()) {
                    System.out.println("Calle: " + rs.getString("Rua"));
                    System.out.println("Número: " + rs.getInt("Numero_Calle"));
                    System.out.println("Piso: " + rs.getString("Piso"));
                    System.out.println("Código Postal: " + rs.getString("CP"));
                    System.out.println("Localidad: " + rs.getString("Localidade"));
                }


            } catch (Exception e) {
                System.out.println("Error al cambiar el domicilio: " + e.getMessage());
            }
        } catch (Exception e) {
            System.out.println("Error al cambiar el domicilio: " + e.getMessage());
        }
    }

    /**
     * Ejercicio 2
     * @param numProxecto
     */
    public void datosProxecto(int numProxecto) {
        try {
            ResultadoProcedimiento res = empresaDAO.datosProxecto(numProxecto);
            System.out.println("Datos del proyecto obtenidos correctamente.");
            System.out.println("Nombre del proyecto: " + res.getParametrosOut()[0]);
            System.out.println("Lugar del proyecto: " + res.getParametrosOut()[1]);
            System.out.println("Departamento del proyecto: " + res.getParametrosOut()[2]);
        } catch (Exception e) {
            System.out.println("Error al obtener los datos del proyecto: " + e.getMessage());
        }
    }

    /**
     * Ejercicio 3
     * @param numProyectos
     */
    public void cambiarDepartamentoPorNumeroProyectos(int numProyectos) {
        try {
            ResultadoProcedimiento res = empresaDAO.cambiarDepartamentoPorNumeroProyectos(numProyectos);
            System.out.println("Departamentos actualizados correctamente.");
            System.out.println("Filas afectadas: " + res.getUpdateCount());
        } catch (Exception e) {
            System.out.println("Error al listar los empleados del departamento: " + e.getMessage());
        }
    }

    /**
     * Ejercicio 4
     * @param nombreDepartamento
     */
    public void numeroEmpregadosDepartamento(String nombreDepartamento) {
        try {
            int num = empresaDAO.numeroEmpregadosDepartamento(nombreDepartamento);
            System.out.println("Número de empleados en el departamento " + nombreDepartamento + ": " + num);
        } catch (Exception e) {
            System.out.println("Error al obtener el número de empleados del departamento: " + e.getMessage());
        }

    }
}
