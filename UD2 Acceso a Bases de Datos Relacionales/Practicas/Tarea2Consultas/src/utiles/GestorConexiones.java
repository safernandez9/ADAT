package utiles;

import java.sql.*;

public class GestorConexiones {

    public static Connection getConnection(TipoSGBD tipo, String baseDatos, String usuario, String contrasena) {
        System.out.println(usuario);
        String url;
        url = switch (tipo) {
            case SQLSERVER ->
                "jdbc:sqlserver://localhost:1433;" + "databaseName=" + baseDatos + ";" + "encrypt=true;" + "trustServerCertificate=true";
            case MYSQL ->
                "jdbc:mysql://localhost:3306/" + baseDatos + "?serverTimezone=UTC";
            case SQLITE ->
                "jdbc:sqlite:" + baseDatos;
            default ->
                "";
        };
        try {
            if (tipo == TipoSGBD.SQLITE) {
                return DriverManager.getConnection(url);

            } else {
                return DriverManager.getConnection(url, usuario, contrasena);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Ejecuta una consulta SQL desde una conexión a un SGBD
     * @param con Conexion al SGBD
     * @param consulta String con la consulta SQL
     * @param parametros Parametros que sustituirán los ? de la expresión sql. Deben estar en el mismo orden.
     * @return
     * @throws SQLException 
     */
    public static ResultSet ejecutarConsulta(Connection con, String consulta, Object... parametros) throws SQLException {
        
        // PS de la consulta. Que es? 
        PreparedStatement stat = con.prepareStatement(consulta);

        // Sustituyo en el PS los ? por sus parametros correspondientes
        for (int i = 0; i < parametros.length; i++) {
            stat.setObject(i + 1, parametros[i]);
        }

        return stat.executeQuery(); //el llamador tiene que cerrar el sttement y el resultset cuando termine
    }

    public static void ejecutarSentencia(Connection con, String consulta, Object... parametros) throws SQLException {
        try (
                PreparedStatement ps = con.prepareStatement(consulta)) {
            setParametros(ps, parametros);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static String obtenerMetaDatos(Connection con) {
        try {
            var meta = con.getMetaData();

            StringBuilder sb = new StringBuilder();
            sb.append("Driver name: " + meta.getDriverName()).append("\n");
            sb.append("Driver version: " + meta.getDriverVersion()).append("\n");
            sb.append("Producto bd: " + meta.getDatabaseProductName()).append("\n");
            sb.append("Version BD: " + meta.getDatabaseProductVersion()).append("\n");
            sb.append("URL: " + meta.getURL()).append("\n");
            sb.append("Usuario: " + meta.getUserName()).append("\n");
            return sb.toString();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

public static void cerrarConexion(Connection con) {
    if (con != null) {
        try {
            if (!con.isClosed()) {
                con.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
//
//falta crear tabla 
//CREAR TABLA SQLITE
//boolean proyectoexiste
    public static void borrarTabla(Connection con, String... tablas) throws SQLException {
        try (Statement stmt = con.createStatement()) {
            for (String tabla : tablas) {
                if (tablaExiste(con, tabla)) {
                    stmt.addBatch("DROP TABLE " + tabla);
                }
            }
            //ejecuta el lote
            stmt.executeBatch();
            con.commit();

        } catch (SQLException ex) {
            con.rollback();
        } finally {
            con.setAutoCommit(true); //restaura confirmacion automatica
        }

    }

public static void ejecutarLoteTransaccional(Connection con, String... sentenciaSQL) {
    try (Statement stmt = con.createStatement()) {
        con.setAutoCommit(false);

        // Añadir cada sentencia al lote
        for (String sql : sentenciaSQL) {
            stmt.addBatch(sql);
        }

        // Ejecutar el lote
        stmt.executeBatch();
        con.commit();

    } catch (SQLException e) {
        try {
            con.rollback();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        e.printStackTrace();
    } finally {
        try {
            con.setAutoCommit(true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}


    public static boolean tablaExiste(Connection connection, String tabla) throws SQLException {
        try (ResultSet rs = connection.getMetaData()
                .getTables(null, null, tabla, null)) {

            return rs.next(); // true si existe, false si no
        }
    }


    private static void setParametros(PreparedStatement ps, Object... parametros) throws SQLException {
        for (int i = 0; i < parametros.length; i++) {
            ps.setObject(i + 1, parametros[i]);
        }
    }
}
