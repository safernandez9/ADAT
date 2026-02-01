package utilidades;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase encargada de gestionar la creación de conexiones JDBC hacia distintos
 * Sistemas Gestores de Bases de Datos: - SQL Server - MySQL - SQLite
 */

public class GestorConexiones {

    /**
     * Devuelve una conexión JDBC configurada según el SGBD indicado.
     *
     * @param tipo Tipo de SGBD.
     * @param baseDatos Nombre de la base de datos.
     * @param usuario Usuario de conexión.
     * @param contrasinal Contraseña del usuario.
     * @return Connection objeto de conexión JDBC ya abierto.
     * @throws SQLException Si ocurre un error al conectarse.
     */
    public static Connection getConnection(TipoSGBD tipo, String baseDatos, String usuario, String contrasinal) throws SQLException {

        String url;

        // Construcción de la URL dependiendo del tipo de SGBD
        url = switch (tipo) {
            case SQLSERVER ->
            // Conexión local en el puerto 1433
                "jdbc:sqlserver://localhost:1433;"
                + "databaseName=" + baseDatos + ";"
                // Evita problemas de certificados
                + "encrypt=true;"
                + "trustServerCertificate=true;";
                
            case MYSQL ->
            // Conexión a MySQL en el puerto 3306
                "jdbc:mysql://localhost:3306/" + baseDatos
                + "?serverTimezone=UTC";
                
            case SQLITE ->
            // En SQLite la "BD" es realmente un fichero (BD Embebida)
                "jdbc:sqlite:" + baseDatos;

            default ->
                throw new UnsupportedOperationException(
                        "Tipo SGBD (" + tipo + ") no soportado");
        };

        // En SQLite NO hay usuario ni contraseña
        if (tipo == TipoSGBD.SQLITE) {
            Connection con = DriverManager.getConnection(url);
            try(Statement st = con.createStatement()){
                st.execute("PRAGMA foreign_keys = ON");
            }
            return con;

        }

        // En MySQL y SQLServer si se usa usuario y contraseña
        return DriverManager.getConnection(url, usuario, contrasinal);
    }


    // Ejecutar consultas y sentencias + PreparedStatement, ResultSet, ExecuteQuery, ExecuteUpdate

    /**
     * CONSULTAS (SELECT)
     *
     * PreparedStatement es un objeto que representa una sentencia SQL precompilada. Esto quiere decir, una con ? en lugar de valores concretos.
     * Luego llamo a mi método setParametros para asignar los valores a los ?.
     * ExecuteQuery() ejecuta la consulta y devuelve un ResultSet con los resultados.
     * Un ResultSet es una tabla virtual que contiene los datos devueltos por la consulta SQL. Puedo iterarlo con un while(rs.next()).
     * EL RESULTSET DEBE CERRARSE TRAS USARLO PARA LIBERAR RECURSOS.
     *
     * @param conexion      Conexión JDBC abiertas
     * @param sql           Sentencia SQL con ? como marcadores de posición
     * @param parametros    Valores a asignar a los marcadores de posición.
     * @return              ResultSet con los resultados de la consulta
     * @throws SQLException
     */
    public static ResultSet ejecutarConsulta(Connection conexion, String sql, Object... parametros) throws SQLException {
        PreparedStatement stat = conexion.prepareStatement(sql);

        for (int i = 0; i < parametros.length; i++) {
            stat.setObject(i + 1, parametros[i]);
        }

        return stat.executeQuery(); //el llamador tiene que cerrar el sttement y el resultset cuando termine
    }

    /**
     * SENTENCIAS (INSERT / UPDATE / DELETE / DDL)
     *
     * Mismos apuntes que para el anterior método, pero en este caso uso executeUpdate(),
     * que ejecuta sentencias SQL que modifican datos o la estructura de la BD.
     * No devuelve resultados, sino el número de filas afectadas (si aplica).
     * El PreparedStatement se cierra automáticamente con el try-with-resources.
     *
     * @param conexion
     * @param sql
     * @param parametros
     * @throws SQLException
     * @return número de filas afectadas
     */
    public static int ejecutarSentencia(Connection conexion, String sql, Object... parametros) throws SQLException {
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            setParametros(ps, parametros);
            int filas = ps.executeUpdate();
            return filas;

        } catch (SQLException e) {
            throw e;
        }
    }

    /**
     * Ejecuta una sentencia INSERT y devuelve la clave generada (autoincremental).
     * Uso Statement.RETURN_GENERATED_KEYS al crear el PreparedStatement para indicar que quiero recuperar las claves generadas.
     * Luego, tras ejecutar la sentencia, uso getGeneratedKeys() para obtener un ResultSet con las claves generadas.
     * Si la inserción no afecta a ninguna fila, lanzo una excepción.
     * Si no se puede recuperar la clave generada, también lanzo una excepción.
     * @param conexion
     * @param sql
     * @param parametros
     * @return
     * @throws SQLException
     */
    public static int ejecutarSentenciaRecuperandoClave(Connection conexion, String sql, Object... parametros) throws SQLException {

        try (PreparedStatement ps = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            setParametros(ps, parametros);
            int filas = ps.executeUpdate();

            // Compruebo esto aqui ya que aqui no puedo devolver las filas afectadas ya que ya devuelvo la clave.
            // Podría hacer un array pero lo veo innecesario.
            if (filas == 0) {
                throw new SQLException("No se pudo insertar el registro.");
            }

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                } else {
                    throw new SQLException("No se pudo recuperar la clave generada.");
                }
            }
        }
    }


    /**
     * Asigna los valores a los marcadores de posición (?) en el PreparedStatement.
     * Object... permite pasar un número variable de argumento que luego se recogen como un array dentro del método.
     * setObject() asigna el valor al marcador de posición en la posición i+1 (los índices empiezan en 1 en SQL).
     *
     * @param ps PreparedStatement con los marcadores de posición.
     * @param params Valores a asignar a los marcadores de posición.
     * @throws SQLException 
     */
    public static void setParametros(PreparedStatement ps, Object... params) throws SQLException{
        for(int i = 0; i< params.length;i++){
            ps.setObject(i+1, params[i]);
        }
        
    }


    // Metodos auxiliares + Statement y batch

    /**
     * Borra las tablas indicadas si existen. Usa Statement y batch para eficiencia.
     * Statement es un objeto que representa una sentencia SQL simple sin parámetros que se usa
     * para DDL principalmente en este caso.
     * Con addBatch() agrego varias sentencias al lote y luego las ejecuto todas con executeBatch().
     * La operación se realiza dentro de una transacción para asegurar que todas las tablas se borran
     * correctamente o ninguna en caso de error.
     * @param conexion
     * @param tablas
     * @throws SQLException 
     */
    public static void borrarTablas(Connection conexion, String... tablas) throws SQLException{
        try{
            conexion.setAutoCommit(false);   // AutoCommit a false para manejar la transacción manualmente
            try(Statement stmt = conexion.createStatement()){
                // Agregar todas las sentencias DROP TABLE al lote
                for(String tabla: tablas){
                    if(tablaExiste(conexion, tabla)){
                        stmt.addBatch("DROP TABLE " + tabla);
                    }
                }
                // Ejecutar el lote
                stmt.executeBatch();
                conexion.commit();           // Confirmar la transacción si no huo errores
            }catch(SQLException ex){
                conexion.rollback();
                throw ex;
            }
        } finally {
            conexion.setAutoCommit(true); // Restaurar el modo de confirmación automatico
        }
    }

    /**
     * Comprueba si una tabla existe en la base de datos.
     * @param conexion
     * @param tabla
     * @return 
     */
    public static boolean tablaExiste(Connection conexion, String tabla) {
        try(ResultSet rs = conexion.getMetaData().getTables(null, null, tabla, null)){
            return rs.next();
        } catch (SQLException ex) {
            return false;            
        }
        
    }


    // Lotes y cerrar conexión

    /**
     * Util para sentencias DDL o DML que deben ejecutarse como un lote dentro de una transacción.
     * Si alguna sentencia falla, se revierte toda la transacción.
     * @param conexion
     * @param sentenciasSQL 
     */
    public static void ejecutarLoteTransaccional(Connection conexion, String... sentenciasSQL) throws SQLException {
        try{
            conexion.setAutoCommit(false);
            try(Statement stmt = conexion.createStatement()){
                for(String sql: sentenciasSQL){
                    stmt.addBatch(sql);
                }
                stmt.executeBatch();
                conexion.commit();
                
            }catch(BatchUpdateException ex){        // Me sirve para saber que falló concretamente
                conexion.rollback();
                throw ex;                
            }
        } finally {
            conexion.setAutoCommit(true);
        }
    }

    /**
     * Ejecuta múltiples sentencias SQL con parámetros como un lote dentro de una transacción.
     * Cada sentencia puede tener su propia lista de parámetros para múltiples ejecuciones.
     * Si alguna ejecución falla, se revierte toda la transacción.
     *
     * @param conexion
     * @param sqls
     * @param parametrosPorSql
     * @throws SQLException
     */
    public static void ejecutarLoteTransaccionalPreparedStatement(
            Connection conexion,
            List<String> sqls,
            List<List<Object[]>> parametrosPorSql
    ) throws SQLException {

        if (sqls.size() != parametrosPorSql.size()) {
            throw new IllegalArgumentException("El número de sentencias y listas de parámetros debe coincidir");
        }

        try {
            conexion.setAutoCommit(false);

            for (int i = 0; i < sqls.size(); i++) {
                String sql = sqls.get(i);
                List<Object[]> parametrosLista = parametrosPorSql.get(i);

                try (PreparedStatement ps = conexion.prepareStatement(sql)) {
                    for (Object[] params : parametrosLista) {
                        for (int j = 0; j < params.length; j++) {
                            ps.setObject(j + 1, params[j]);
                        }
                        ps.addBatch();
                    }
                    ps.executeBatch();
                }
            }

            conexion.commit();
        } catch (BatchUpdateException e) {
            conexion.rollback();
            throw e;
        } finally {
            conexion.setAutoCommit(true);
        }
    }


    /* Hay que verlo pero va así:
     * catch (BatchUpdateException bue) {
     *      System.out.println("Mensaje: " + bue.getMessage());
     *      System.out.println("SQLState: " + bue.getSQLState());
     *      System.out.println("Error code: " + bue.getErrorCode());
     *      int[] resultados = bue.getUpdateCounts();
     *      for(int i = 0; i < resultados.length; i++){
     *          System.out.println("Sentencia " + i + " resultado: " + resultados[i]);
     *      }
     *
     * resultados[0] = Statement.SUCCESS_NO_INFO   // primera ejecutada bien
     * resultados[1] = Statement.EXECUTE_FAILED   // segunda falló
     * resultados[2] = (no llega a ejecutarse)

}

     */


    // Metadatos

    /**
     * Imprime por consola los metadatos de la base de datos conectada.
     *
     * @param conexion Conexión JDBC abierta.
     */
    public static void obtenerMetadatos(Connection conexion) throws Exception {

        try {
            DatabaseMetaData meta = conexion.getMetaData();

            // Información general
            System.out.println("=== Información de la base de datos ===");
            System.out.println("SGBD: " + meta.getDatabaseProductName() + " " + meta.getDatabaseProductVersion());
            System.out.println("Driver: " + meta.getDriverName() + " " + meta.getDriverVersion());
            System.out.println("URL: " + meta.getURL());
            System.out.println("Usuario: " + meta.getUserName());
            System.out.println("Es de solo lectura: " + meta.isReadOnly());
            System.out.println("=======================================");

            // Listado de tablas
            try (ResultSet tablas = meta.getTables(null, null, "%", new String[]{"TABLE"})) {
                while (tablas.next()) {
                    String tabla = tablas.getString("TABLE_NAME");
                    System.out.println("\nTabla: " + tabla);

                    // Columnas
                    try (ResultSet columnas = meta.getColumns(null, null, tabla, "%")) {
                        while (columnas.next()) {
                            String nombre = columnas.getString("COLUMN_NAME");
                            String tipo = columnas.getString("TYPE_NAME");
                            int tamaño = columnas.getInt("COLUMN_SIZE");
                            int nullable = columnas.getInt("NULLABLE");
                            String autoInc = columnas.getString("IS_AUTOINCREMENT");
                            System.out.printf("  Columna: %-15s Tipo: %-10s Tamaño: %-4d Nullable: %-2s Autoinc: %-3s%n",
                                    nombre, tipo, tamaño, (nullable == DatabaseMetaData.columnNullable ? "Sí" : "No"), autoInc);
                        }
                    }

                    // Claves primarias
                    try (ResultSet pk = meta.getPrimaryKeys(null, null, tabla)) {
                        System.out.print("  PK: ");
                        while (pk.next()) {
                            System.out.print(pk.getString("COLUMN_NAME") + " ");
                        }
                        System.out.println();
                    }

                    // Claves foráneas
                    try (ResultSet fk = meta.getImportedKeys(null, null, tabla)) {
                        while (fk.next()) {
                            String fkCol = fk.getString("FKCOLUMN_NAME");
                            String refTabla = fk.getString("PKTABLE_NAME");
                            String refCol = fk.getString("PKCOLUMN_NAME");
                            System.out.println("  FK: " + fkCol + " -> " + refTabla + "(" + refCol + ")");
                        }
                    }
                }
            }

        } catch (SQLException e) {
            throw new Exception("Error al obtener metadatos de la base de datos", e);
        }
    }

    /**
     * Devuelve el tipo de SGBD de la conexión proporcionada.
     *
     * @param conexion Conexión JDBC abierta.
     * @return TipoSGBD tipo de SGBD.
     * @throws SQLException Si ocurre un error al obtener los metadatos.
     */
    public static TipoSGBD getTipoSGBD(Connection conexion) throws Exception {
        String nombreProducto = conexion.getMetaData().getDatabaseProductName().toLowerCase();
        if (nombreProducto.contains("sql server")) {
            return TipoSGBD.SQLSERVER;
        } else if (nombreProducto.contains("mysql")) {
            return TipoSGBD.MYSQL;
        } else if (nombreProducto.contains("sqlite")) {
            return TipoSGBD.SQLITE;
        } else {
            throw new Exception("Tipo SGBD no soportado: " + nombreProducto);
        }
    }

    /**
     * Obtiene los nombres de las columnas de una tabla específica.
     * @param conexion Conexión JDBC abierta.
     * @param nombreTabla Nombre de la tabla.
     * @return Lista de nombres de columnas.
     */
    public static List<String> obtenerColumnasTabla(Connection conexion, String nombreTabla) {
        List<String> columnas = new ArrayList<>();
        try {
            DatabaseMetaData meta = conexion.getMetaData();
            try (ResultSet rs = meta.getColumns(null, null, nombreTabla, "%")) {
                while (rs.next()) {
                    columnas.add(rs.getString("COLUMN_NAME"));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener columnas de la tabla " + nombreTabla, e);
        }
        return columnas;
    }

    /**
     * Obtiene los nombres de las columnas que forman la clave primaria de una tabla específica.
     * @param conexion
     * @param nombreTabla
     * @return
     */
    public static List<String> obtenerClavePrimaria(Connection conexion, String nombreTabla) {
        List<String> claves = new ArrayList<>();
        try {
            DatabaseMetaData meta = conexion.getMetaData();
            try (ResultSet rs = meta.getPrimaryKeys(null, null, nombreTabla)) {
                while (rs.next()) {
                    claves.add(rs.getString("COLUMN_NAME"));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener clave primaria de la tabla " + nombreTabla, e);
        }
        return claves;
    }

    /**
     * Obtiene las claves foráneas de una tabla específica.
     * @param conexion Conexión JDBC abierta.
     * @param nombreTabla Nombre de la tabla.
     * @return Lista de claves foráneas en formato "FKCOLUMN_NAME -> PKTABLE_NAME.PKCOLUMN_NAME".
     */
    public static List<String> obtenerClavesForaneas(Connection conexion, String nombreTabla) {
        List<String> claves = new ArrayList<>();
        try {
            DatabaseMetaData meta = conexion.getMetaData();
            try (ResultSet rs = meta.getImportedKeys(null, null, nombreTabla)) {
                while (rs.next()) {
                    claves.add(rs.getString("FKCOLUMN_NAME") + " -> " +
                            rs.getString("PKTABLE_NAME") + "." +
                            rs.getString("PKCOLUMN_NAME"));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener claves foráneas de la tabla " + nombreTabla, e);
        }
        return claves;
    }


    /**
     * Cierra la conexión JDBC proporcionada.
     * @param conexion 
     */
    public static void cerrarConexion(Connection conexion) throws Exception {
        try {
            if (conexion != null && !conexion.isClosed()) {
                conexion.close();
            }
        } catch (SQLException e) {
            throw new Exception("Error al cerrar la conexión", e);
        }
    }
}
