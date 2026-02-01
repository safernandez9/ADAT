package persistencia;

import utilidades.GestorConexiones;
import utilidades.ResultadoProcedimiento;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;

public class EmpresaDAO {

    private Connection conexion;

    public EmpresaDAO(Connection conexion) {
        this.conexion = conexion;
    }

    // Con mi método de ejecutar procedimientos almacenados

    /**
     * Ejercicio 1
     * IN, NO OUT
     * @param nss
     * @param rua
     * @param numero
     * @param piso
     * @param cp
     * @param localidade
     * @return
     * @throws SQLException
     */
    public ResultadoProcedimiento cambioDomicilio(String nss, String rua, int numero, String piso, String cp, String localidade) throws SQLException {

        try {
            String sql = "{call pr_CambioDomicilio(?,?,?,?,?,?)}";

            ResultadoProcedimiento res = GestorConexiones.ejecutarProcedimiento(conexion, sql, new Object[]{nss, rua, numero, piso, cp, localidade}, null);
            return res;

        } catch (SQLException e) {
            throw new SQLException("Error al cambiar el domicilio del empleado: " + e.getMessage());
        }
    }

    /**
     * Ejercicio 2
     * IN, OUT
     * @param numProxecto
     * @throws SQLException
     */
    public ResultadoProcedimiento datosProxecto(int numProxecto) throws SQLException {

        try {

            String sql = "{call pr_DatosProxectos(?,?,?,?)}";

            ResultadoProcedimiento res = GestorConexiones.ejecutarProcedimiento(conexion, sql, new Object[]{numProxecto},
                    new int[]{Types.VARCHAR, Types.VARCHAR, Types.VARCHAR});
            return res;

        } catch (SQLException e) {
            throw new SQLException("Error al obtener los datos del proyecto: " + e.getMessage());
        }
    }

    /**
     * Ejercicio 3
     * IN, NO OUT
     */
    public ResultadoProcedimiento cambiarDepartamentoPorNumeroProyectos(int num) throws SQLException {
        try {
            String sql = "{call pr_DepartControlaProxec(?)}";

            ResultadoProcedimiento res = GestorConexiones.ejecutarProcedimiento(conexion, sql, new Object[]{num}, null);
            return res;

        } catch (SQLException e) {
            throw new SQLException("Error al cambiar el departamento que controla el proyecto" + num + e.getMessage());
        }
    }

    /**
     * Ejercicio 4
     * Función
     */
    public int numeroEmpregadosDepartamento(String nomeDept) throws SQLException {
        try {
            String sql = "{? = call fn_nEmpDepart(?)}";

            Object resultado = GestorConexiones.ejecutarFuncionEscalar(conexion, sql, Types.INTEGER, nomeDept);

            // Convertimos a int antes de devolver
            return (resultado != null) ? (Integer) resultado : 0;

        } catch (SQLException e) {
            throw new SQLException("Error al obtener el número de empleados del departamento " + nomeDept + ": " + e.getMessage());
        }
    }

    // Copiado de clase, sin usar métodos genéricos

    /*

    public static void cambioDomicilio(Connection con, String nss, String rua, int numero, String piso, String cp, String localidade) throws SQLException {

        CallableStatement cs = con.prepareCall("{ call pr_CambioDomicilio(?,?,?,?,?,?) }");

        cs.setString(1, nss);
        cs.setString(2, rua);
        cs.setInt(3, numero);
        cs.setString(4, piso);
        cs.setString(5, cp);
        cs.setString(6, localidade);

        cs.execute();
        cs.close();
    }

    public static String[] datosProxecto(Connection con, int numProxecto) throws SQLException {

        CallableStatement cs = con.prepareCall("{ call pr_DatosProxectos(?,?,?,?) }");

        cs.setInt(1, numProxecto);

        cs.registerOutParameter(2, Types.VARCHAR);
        cs.registerOutParameter(3, Types.VARCHAR);
        cs.registerOutParameter(4, Types.VARCHAR);

        cs.execute();

        String[] datos = {cs.getString(2), cs.getString(3), cs.getString(4)};

        cs.close();
        return datos;
    }

    public static ResultSet departamentosPorProxectos(Connection con, int n) throws SQLException {

        CallableStatement cs = con.prepareCall("{ call pr_DepartControlaProxec(?) }");

        cs.setInt(1, n);
        boolean tieneRS = cs.execute();

        return tieneRS ? cs.getResultSet() : null;
    }

    public static int numeroEmpregados(Connection con, String dep) throws SQLException {

        CallableStatement cs = con.prepareCall("{ ? = call fn_nEmpDepart(?) }");

        cs.registerOutParameter(1, Types.INTEGER);
        cs.setString(2, dep);

        cs.execute();

        int total = cs.getInt(1);
        cs.close();
        return total;
    }

    // obtiene empleados asociados a un proyecto y los devuelve como dto
    public static List<EmpregadoInfoProxectoDTO> obtenerEmpleadosProxecto(Connection con, int numProxecto) throws SQLException {

        List<EmpregadoInfoProxectoDTO> lista = new ArrayList<>();

        String sql = "{ call sp_empleados_por_proyecto(?) }";

        try (CallableStatement cs = con.prepareCall(sql)) {

            cs.setInt(1, numProxecto);

            try (ResultSet rs = cs.executeQuery()) {

                while (rs.next()) {

                    EmpregadoInfoProxectoDTO dto =
                            new EmpregadoInfoProxectoDTO(
                                    rs.getString("NSS"),
                                    rs.getString("NomeCompleto"),
                                    rs.getString("Lugar"),
                                    rs.getInt("NumDepartControla")
                            );

                    lista.add(dto);
                }
            }
        }

        return lista;
    }
    //endregion
     */

}
