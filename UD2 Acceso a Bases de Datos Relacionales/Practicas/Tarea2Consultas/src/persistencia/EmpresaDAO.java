package persistencia;

import dto.*;
import utiles.GestorConexiones;

import java.sql.*;
import java.util.*;

public class EmpresaDAO {

    // LOCALDETE SE CONVIERTE CON JAVA SQL DATE VALUE OF, HAY QUE CONVERTIR LOCALDATE A DATE PARA uno de los SQBD Y EL CHAR A STRInG
    
    // EJERCICIO 1
    
    /**
     * Visualizar número e nome dos departamentos que teñen proxectos asignados.
     * 
     * @param con
     * @return List<DepartamentoDTO> Es un tipo de objeto que creo para guardar solo los datos que me interesan en esta consulta
     */
    public static List<DepartamentoDTO> ejercicio1(Connection con) {
        String sql = """
            SELECT DISTINCT d.NumDepartamento, d.NomeDepartamento
            FROM DEPARTAMENTO d
            JOIN PROXECTO p ON d.NumDepartamento = p.NumDepartControla
        """;

        List<DepartamentoDTO> lista = new ArrayList<>();
        
        // Ejecuto consulta y guardo resultado en ResultSet y recorro añadiendo a la lista mientras no haya una excepcion
        try (ResultSet rs = GestorConexiones.ejecutarConsulta(con, sql)) {
            while (rs.next())
                lista.add(new DepartamentoDTO(rs.getInt(1), rs.getString(2)));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return lista;
    }

    
    // EJERCICIO 2
    
    /**
     * Visualizar o número e nome, nombre e apelidos do director dos departamentos que teñen proxectos asignados.
     * Este método se usará para MySQL y SQLServer ya que CONCAT no funciona en SQLite
     * @param con
     * @return 
     */
    public static List<DepartamentoDirectorDTO> ejercicio2_mysql_sqlserver(Connection con) {

        String sql = """
            SELECT DISTINCT
                   d.NumDepartamento,
                   d.NomeDepartamento,
                   e.Nome AS NomeDirector,
                   CONCAT(e.Apelido1, ' ', COALESCE(e.Apelido2, '')) AS ApelidosDirector
            FROM DEPARTAMENTO d
            JOIN EMPREGADO e ON d.NSSDirector = e.NSS
            JOIN PROXECTO p ON d.NumDepartamento = p.NumDepartControla
        """;

        return ejecutarEjercicio2(con, sql);
    }

    /**
     * Visualizar o número e nome, nombre e apelidos do director dos departamentos que teñen proxectos asignados.
     * Este método se usará para SQLite ya que debe ser sin CONCAT
     * @param con
     * @return 
     */
    public static List<DepartamentoDirectorDTO> ejercicio2_sqlite(Connection con) {

        String sql = """
            SELECT DISTINCT
                   d.NumDepartamento,
                   d.NomeDepartamento,
                   e.Nome AS NomeDirector,
                   e.Apelido1 || ' ' || COALESCE(e.Apelido2, '') AS ApelidosDirector
            FROM DEPARTAMENTO d
            JOIN EMPREGADO e ON d.NSSDirector = e.NSS
            JOIN PROXECTO p ON d.NumDepartamento = p.NumDepartControla
        """;

        return ejecutarEjercicio2(con, sql);
    }

    /**
     * Método auxiliar del ejercicio 2. Ejecuta la consulta independientemente de la conexion que reciba.
     * @param con
     * @param sql
     * @return 
     */
    private static List<DepartamentoDirectorDTO> ejecutarEjercicio2(Connection con, String sql) {
        
        List<DepartamentoDirectorDTO> lista = new ArrayList<>();
                
        try (ResultSet rs = GestorConexiones.ejecutarConsulta(con, sql)) {
            while (rs.next())
                lista.add(new DepartamentoDirectorDTO(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4)));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return lista;
        
    }


    // EJERCICIO 3
   
    // fechas
    //MySQL
    /**
     * Visualizar o NSS, o nome e apelidos e a idade dos empregados da empresa.
     * En MySQL usamos TIMESTAMPDIFF para calcular la edad
     * @param con
     * @return 
     */
    public static List<EmpregadoIdadeDTO> ejercicio3_mysql(Connection con) {

        String sql = """
            SELECT
                NSS,
                CONCAT(Nome, ' ', Apelido1),
                TIMESTAMPDIFF(YEAR, DataNacemento, CURDATE())
            FROM EMPREGADO
        """;

        return empleadosEdad(con, sql);
    }

    // SQLite
    /**
     * Visualizar o NSS, o nome e apelidos e a idade dos empregados da empresa.
     * En SQLite no usamos ni TIMESTAMPDIFF ni CONCAT para calcular la edad
     * @param con
     * @return 
     */
    public static List<EmpregadoIdadeDTO> ejercicio3_sqlite(Connection con) {

        String sql = """
            SELECT
                NSS,
                Nome || ' ' || Apelido1,
                CAST((strftime('%Y','now') - strftime('%Y', DataNacemento)) AS INTEGER)
            FROM EMPREGADO
        """;

        return empleadosEdad(con, sql);
    }

    // SQL Server
    /**
     * Visualizar o NSS, o nome e apelidos e a idade dos empregados da empresa.
     * En SQLServer usamos DATEDIFF para calcular la edad
     * @param con
     * @return 
     */
    public static List<EmpregadoIdadeDTO> ejercicio3_sqlserver(Connection con) {

        String sql = """
            SELECT
                NSS,
                CONCAT(Nome, ' ', Apelido1),
                DATEDIFF(YEAR, DataNacemento, GETDATE())
            FROM EMPREGADO
        """;

        return empleadosEdad(con, sql);
    }

    /**
     * Ejecuta la parte común del ejercicio
     * @param con
     * @param sql
     * @return 
     */
    private static List<EmpregadoIdadeDTO> empleadosEdad(Connection con, String sql) {

        List<EmpregadoIdadeDTO> lista = new ArrayList<>();

        try (ResultSet rs = GestorConexiones.ejecutarConsulta(con, sql)) {

            while (rs.next()) {
                lista.add(new EmpregadoIdadeDTO(
                        rs.getString(1),
                        rs.getString(2),
                        rs.getInt(3)
                ));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return lista;
    }

    
    // EJERCICIO 4   

    // MySQL
    /**
     * Dado o nome dun departamento, visualizar os empregados que traballan nese departamento especificando se se trata dun empregado fixo ou temporal.
     * En MySQL usamos if
     * @param con
     * @param departamento
     * @return 
     */
    public static List<EmpregadoResumoDTO> ejercicio4_mysql(Connection con, String departamento) {

        String sql = """
            SELECT e.NSS, CONCAT(e.Nome,' ',e.Apelido1),
                   IF(f.NSS IS NOT NULL,'FIXO','TEMPORAL')
            FROM EMPREGADO e
            JOIN DEPARTAMENTO d ON e.NumDepartamentoPertenece = d.NumDepartamento
            LEFT JOIN EMPREGADOFIXO f ON e.NSS = f.NSS
            WHERE d.NomeDepartamento = ?
        """;

        return empleadosConParametro(con, sql, departamento);
    }

    // SQLite y SQL Server
    /**
     * Dado o nome dun departamento, visualizar os empregados que traballan nese departamento especificando se se trata dun empregado fixo ou temporal.
     * En SQLite y MySQL usamos CASE
     * @param con
     * @param departamento
     * @return 
     */
    public static List<EmpregadoResumoDTO> ejercicio4_sqlite_sqlserver(Connection con, String departamento) {

        // NomeDepartamento = ? por que usaré la variable recibida por código
        String sql = """
            SELECT e.NSS,
                   e.Nome || ' ' || e.Apelido1,
                   CASE WHEN f.NSS IS NOT NULL THEN 'FIXO' ELSE 'TEMPORAL' END
            FROM EMPREGADO e
            JOIN DEPARTAMENTO d ON e.NumDepartamentoPertenece = d.NumDepartamento
            LEFT JOIN EMPREGADOFIXO f ON e.NSS = f.NSS
            WHERE d.NomeDepartamento = ?
        """;

        return empleadosConParametro(con, sql, departamento);
    }

    // EJERCICIO 5
    //concat

    // MySQL y SQL Server
    public static List<EmpregadoResumoDTO> ejercicio5_mysql_sqlserver(Connection con, String proxecto, String lugar) {

        String sql = """
            SELECT e.NSS, CONCAT(e.Nome,' ',e.Apelido1),
                   f.Salario, d.NomeDepartamento
            FROM EMPREGADO e
            JOIN EMPREGADOFIXO f ON e.NSS = f.NSS
            JOIN EMPREGADO_PROXECTO ep ON e.NSS = ep.NSSEmpregado
            JOIN PROXECTO p ON ep.NumProxecto = p.NumProxecto
            JOIN DEPARTAMENTO d ON e.NumDepartamentoPertenece = d.NumDepartamento
            WHERE p.NomeProxecto = ? AND p.Lugar = ?
        """;

        return empleadosConParametro(con, sql, proxecto, lugar);
    }

    // SQLite
    public static List<EmpregadoResumoDTO> ejercicio5_sqlite(Connection con, String proxecto, String lugar) {

        String sql = """
            SELECT e.NSS,
                   e.Nome || ' ' || e.Apelido1,
                   f.Salario,
                   d.NomeDepartamento
            FROM EMPREGADO e
            JOIN EMPREGADOFIXO f ON e.NSS = f.NSS
            JOIN EMPREGADO_PROXECTO ep ON e.NSS = ep.NSSEmpregado
            JOIN PROXECTO p ON ep.NumProxecto = p.NumProxecto
            JOIN DEPARTAMENTO d ON e.NumDepartamentoPertenece = d.NumDepartamento
            WHERE p.NomeProxecto = ? AND p.Lugar = ?
        """;

        return empleadosConParametro(con, sql, proxecto, lugar);
    }

    // EJERCICIO 6: Visualizar, para cada departamento, o número de empregados fixos e temporais
    // SUM(x) mysql / CASE WHEN para SQLite y SQL Server

    // MySQL
    public static List<DepartamentoEmpleadosDTO> ejercicio6_mysql(Connection con) {

        String sql = """
            SELECT d.NomeDepartamento,
                   SUM(f.NSS IS NOT NULL),
                   SUM(t.NSS IS NOT NULL)
            FROM DEPARTAMENTO d
            LEFT JOIN EMPREGADO e ON d.NumDepartamento = e.NumDepartamentoPertenece
            LEFT JOIN EMPREGADOFIXO f ON e.NSS = f.NSS
            LEFT JOIN EMPREGADOTEMPORAL t ON e.NSS = t.NSS
            GROUP BY d.NomeDepartamento
        """;

        return departamentosEmpleados(con, sql);
    }

    // SQLite y SQL Server
    public static List<DepartamentoEmpleadosDTO> ejercicio6_sqlite_sqlserver(Connection con) {

        String sql = """
            SELECT d.NomeDepartamento,
                   SUM(CASE WHEN f.NSS IS NOT NULL THEN 1 ELSE 0 END),
                   SUM(CASE WHEN t.NSS IS NOT NULL THEN 1 ELSE 0 END)
            FROM DEPARTAMENTO d
            LEFT JOIN EMPREGADO e ON d.NumDepartamento = e.NumDepartamentoPertenece
            LEFT JOIN EMPREGADOFIXO f ON e.NSS = f.NSS
            LEFT JOIN EMPREGADOTEMPORAL t ON e.NSS = t.NSS
            GROUP BY d.NomeDepartamento
        """;

        return departamentosEmpleados(con, sql);
    }

    // auxiliar ejercicio 6
    private static List<DepartamentoEmpleadosDTO> departamentosEmpleados(Connection con, String sql) {

        List<DepartamentoEmpleadosDTO> lista = new ArrayList<>();

        try (ResultSet rs = GestorConexiones.ejecutarConsulta(con, sql)) {

            while (rs.next()) {
                lista.add(new DepartamentoEmpleadosDTO(
                        rs.getString(1),
                        rs.getInt(2),
                        rs.getInt(3)
                ));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return lista;
    }

    // EJERCICIO 7
    public static List<DepartamentoDTO> ejercicio7(Connection con, int n) {
        String sql = """
            SELECT d.NumDepartamento, d.NomeDepartamento
            FROM DEPARTAMENTO d
            JOIN EMPREGADO e ON d.NumDepartamento = e.NumDepartamentoPertenece
            GROUP BY d.NumDepartamento
            HAVING COUNT(*) > ?
        """;
        return obtenerDepartamentos(con, sql, n);
    }

    // EJERCICIO 8
    // concat
    // MySQL y SQL Server
    public static List<EmpregadoResumoDTO> ejercicio8_mysql_sqlserver(Connection con, double salario) {

        String sql = """
            SELECT e.NSS, CONCAT(e.Nome,' ',e.Apelido1), f.Salario
            FROM EMPREGADO e
            JOIN EMPREGADOFIXO f ON e.NSS = f.NSS
            WHERE f.Salario > ?
        """;
        return empleadosConParametro(con, sql, salario);
    }

    // SQLite
    public static List<EmpregadoResumoDTO> ejercicio8_sqlite(Connection con, double salario) {

        String sql = """
            SELECT e.NSS, e.Nome || ' ' || e.Apelido1, f.Salario
            FROM EMPREGADO e
            JOIN EMPREGADOFIXO f ON e.NSS = f.NSS
            WHERE f.Salario > ?
        """;
        return empleadosConParametro(con, sql, salario);
    }

    // EJERCICIO 9
    // Scroll ResultSet no funciona correctamente en SQLite, Solo MySQL y SQL Server

    public static void ejercicio9_mysql_sqlserver(Connection con) throws SQLException {

        String sql = """
            SELECT d.NomeDepartamento, CONCAT(e.Nome,' ',e.Apelido1)
            FROM EMPREGADOFIXO f
            JOIN EMPREGADO e ON f.NSS = e.NSS
            JOIN DEPARTAMENTO d ON e.NumDepartamentoPertenece = d.NumDepartamento
            WHERE f.Salario = (
                SELECT MAX(f2.Salario)
                FROM EMPREGADOFIXO f2
                JOIN EMPREGADO e2 ON f2.NSS = e2.NSS
                WHERE e2.NumDepartamentoPertenece = d.NumDepartamento
            )
            ORDER BY d.NomeDepartamento
        """;

        PreparedStatement ps = con.prepareStatement(
                sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

        ResultSet rs = ps.executeQuery();
        rs.afterLast();
        while (rs.previous())
            System.out.println(rs.getString(1) + " - " + rs.getString(2));
    }

    // EJERCICIO 10
    //SQLite antiguo no soporta subconsulta en FROM - MySQL y SQL Server

    public static List<DepartamentoDTO> ejercicio10_mysql_sqlserver(Connection con) {

        String sql = """
            SELECT d.NumDepartamento, d.NomeDepartamento
            FROM DEPARTAMENTO d
            JOIN PROXECTO p ON d.NumDepartamento = p.NumDepartControla
            GROUP BY d.NumDepartamento
            HAVING COUNT(*) = (
                SELECT MAX(c)
                FROM (
                    SELECT COUNT(*) c
                    FROM PROXECTO
                    GROUP BY NumDepartControla
                ) t
            )
        """;
        return obtenerDepartamentos(con, sql);
    }

    // MÉTODOS AUXILIARES COMUNES
    private static List<DepartamentoDTO> obtenerDepartamentos(Connection con, String sql, Object... params) {

        List<DepartamentoDTO> lista = new ArrayList<>();
        try (ResultSet rs = GestorConexiones.ejecutarConsulta(con, sql, params)) {
            while (rs.next())
                lista.add(new DepartamentoDTO(rs.getInt(1), rs.getString(2)));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return lista;
    }

    
    private static List<EmpregadoResumoDTO> empleadosConParametro(Connection con, String sql, Object... params) {

        List<EmpregadoResumoDTO> lista = new ArrayList<>();

        try (ResultSet rs = GestorConexiones.ejecutarConsulta(con, sql, params)) {
            
            while (rs.next()) {
                lista.add(new EmpregadoResumoDTO(rs.getString(1),rs.getString(2),rs.getString(3)));
            }
            
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return lista;
    }
}
