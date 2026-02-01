// Saul Fernandez Salgado 77013586H
package utilidades;

import java.sql.*;
import java.time.LocalDate;
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
     * @param tipo        Tipo de SGBD.
     * @param baseDatos   Nombre de la base de datos.
     * @param usuario     Usuario de conexión.
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


            default -> throw new UnsupportedOperationException(
                    "Tipo SGBD (" + tipo + ") no soportado");
        };



        // En MySQL y SQLServer si se usa usuario y contraseña
        return DriverManager.getConnection(url, usuario, contrasinal);
    }


    // Ejecutar consultas y sentencias + PreparedStatement, ResultSet, ExecuteQuery, ExecuteUpdate


    /**
     * CONSULTAS (SELECT)
     *
     * PreparedStatement es un objeto que representa una sentencia SQL precompilada. Esto quiere decir, una con ? en lugar de valores concretos.
     * Luego llamo a mi método setParametros para asignar los valores a los ?.
     * Un ResultSet es una tabla virtual que contiene los datos devueltos por la consulta SQL. Puedo iterarlo con un while(rs.next()).
     * EL RESULTSET DEBE CERRARSE TRAS USARLO PARA LIBERAR RECURSOS.
     * Usa ExecuteQuery: Devuelve un ResultSet con los resultados de la consulta.
     *
     * @param conexion   Conexión JDBC abiertas
     * @param sql        Sentencia SQL con ? como marcadores de posición
     * @param parametros Valores a asignar a los marcadores de posición.
     * @return ResultSet con los resultados de la consulta
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
     * <p>
     * Mismos apuntes que para el anterior método, pero en este caso uso executeUpdate(),
     * que ejecuta sentencias SQL que modifican datos o la estructura de la BD.
     * No devuelve resultados, sino el número de filas afectadas (si aplica).
     * El PreparedStatement se cierra automáticamente con el try-with-resources.
     * Usa PreparedStatement: Más seguro, evita inyección SQL y admite parámetros.
     * Usa executeUpdate: Devuelve el número de filas afectadas.En DDL devuelve 0.
     *
     * @param conexion
     * @param sql
     * @param parametros
     * @return número de filas afectadas
     * @throws SQLException
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
     *
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
     * @param ps     PreparedStatement con los marcadores de posición.
     * @param params Valores a asignar a los marcadores de posición.
     * @throws SQLException
     */
    public static void setParametros(PreparedStatement ps, Object... params) throws SQLException {
        try {
            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }
        } catch (SQLException e) {
            throw e;
        }
    }




    // Metodos auxiliares + Statement y batch


    /**
     * Util para sentencias DDL o DML que deben ejecutarse como un lote dentro de una transacción.
     * Si alguna sentencia falla, se revierte toda la transacción.
     * Siempre crear tablas con esto.
     * Usa Statement: Menos seguro, no admite parámetros.
     * Usa ExecuteBatch: devuelve un array con el número de filas afectadas por cada sentencia.
     *
     * @param conexion
     * @param sentenciasSQL
     */
    public static void ejecutarLoteTransaccional(Connection conexion, String... sentenciasSQL) throws SQLException {
        try {
            conexion.setAutoCommit(false);
            try (Statement stmt = conexion.createStatement()) {
                for (String sql : sentenciasSQL) {
                    stmt.addBatch(sql);
                }
                stmt.executeBatch();
                conexion.commit();

            } catch (BatchUpdateException ex) {        // Me sirve para saber que falló concretamente
                conexion.rollback();
                throw ex;
            }
        } finally {
            conexion.setAutoCommit(true);
        }
    }


    /**
     * Borra las tablas indicadas si existen. Usa Statement y batch para eficiencia.
     * Statement es un objeto que representa una sentencia SQL simple sin parámetros que se usa
     * para DDL principalmente en este caso.
     * Con addBatch() agrego varias sentencias al lote y luego las ejecuto todas con executeBatch().
     * La operación se realiza dentro de una transacción para asegurar que todas las tablas se borran
     * correctamente o ninguna en caso de error.
     *
     * @param conexion
     * @param tablas
     * @throws SQLException
     */
    public static void borrarTablas(Connection conexion, String... tablas) throws SQLException {
        try {
            conexion.setAutoCommit(false);   // AutoCommit a false para manejar la transacción manualmente
            try (Statement stmt = conexion.createStatement()) {
                // Agregar todas las sentencias DROP TABLE al lote
                for (String tabla : tablas) {
                    if (tablaExiste(conexion, tabla)) {
                        stmt.addBatch("DROP TABLE " + tabla);
                    }
                }
                // Ejecutar el lote
                stmt.executeBatch();
                conexion.commit();           // Confirmar la transacción si no huo errores
            } catch (SQLException ex) {
                conexion.rollback();
                throw ex;
            }
        } finally {
            conexion.setAutoCommit(true); // Restaurar el modo de confirmación automatico
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
    public static void ejecutarLoteTransaccionalPreparedStatement(Connection conexion, List<String> sqls, List<List<Object[]>> parametrosPorSql) throws SQLException {

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


    /**
     * Método genérico para procedimientos Y funciones. Soporta IN, OUT, RESULLTSET Y UPDATECOUNT
     * Supongo que dará menos rendimiento que hacer cada uno por separado, pero es más cómodo.
     *
     *
     * @param conexion Conexión JDBC abierta.
     * @param sql Sentencia SQL del procedimiento o la función
     * @param parametrosIn Array de objetos con los parámetros de entrada (IN).
     * @param tiposOut Array de enteros con los tipos SQL de los parámetros de salida (OUT).
     * @return ResultadoProcedimiento objeto con los resultados del procedimiento.
     * @throws SQLException Si ocurre un error al ejecutar el procedimiento.
     */
    public static ResultadoProcedimiento ejecutarProcedimiento(Connection conexion, String sql, Object[] parametrosIn, int[] tiposOut) throws SQLException {

        ResultadoProcedimiento resultado = new ResultadoProcedimiento();

        try (CallableStatement cs = conexion.prepareCall(sql)) {

            // índice para los parámetros
            int index = 1;

            // seteo los parámetros IN y registro los OUT
            if (parametrosIn != null) {
                for (Object param : parametrosIn) {
                    cs.setObject(index++, param);
                }
            }

            if (tiposOut != null) {
                for (int tipo : tiposOut) {
                    cs.registerOutParameter(index++, tipo);
                }
            }

            // Ejecuto el callableStatement y recojo si hay ResultSet
            boolean hayResultSet = cs.execute();

            // ResultSet o UpdateCount
            if (hayResultSet) {
                resultado.setResultSet(cs.getResultSet());
                resultado.setEsConsulta(true);
            } else {
                resultado.setUpdateCount(cs.getUpdateCount());
                resultado.setEsConsulta(false);
            }

            // Recojo los parametros OUT si hay.
            if (tiposOut != null) {
                Object[] outValues = new Object[tiposOut.length];

                for (int i = 0; i < tiposOut.length; i++) {
                    // Calculo en que posicion está el parámetro OUT para recogerlo.
                    outValues[i] = cs.getObject((parametrosIn == null ? 0 : parametrosIn.length) + i + 1);
                }
                resultado.setParametrosOut(outValues);
            }
        }

        return resultado;
    }


    /**
     * Ejecuta una función escalar en la base de datos y devuelve su valor.
     *
     * @param conexion   Conexión JDBC abierta.
     * @param sql        Sentencia SQL de la función con ? para parámetros.
     * @param tipoRetorno Tipo SQL del valor devuelto por la función.
     * @param parametros Valores a asignar a los parámetros de la función.
     * @return Valor devuelto por la función.
     * @throws SQLException Si ocurre un error al ejecutar la función.
     */
    public static Object ejecutarFuncionEscalar(Connection conexion, String sql, int tipoRetorno, Object... parametros) throws SQLException {
        try (CallableStatement cs = conexion.prepareCall(sql)) {

            cs.registerOutParameter(1, tipoRetorno);

            if (parametros != null) {
                for (int i = 0; i < parametros.length; i++) {
                    cs.setObject(i + 2, parametros[i]); // IN empieza en posición 2
                }
            }

            cs.execute();

            return cs.getObject(1);
        }
    }


    /**
     * Comprueba si una tabla existe en la base de datos.
     *
     * @param conexion
     * @param tabla
     * @return
     */
    public static boolean tablaExiste(Connection conexion, String tabla) {
        try (ResultSet rs = conexion.getMetaData().getTables(null, null, tabla, null)) {
            return rs.next();
        } catch (SQLException ex) {
            return false;
        }

    }


    // Metadatos

    public static void obtenerMetadatosTabla(Connection conexion, String nombreTabla) throws Exception {

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
            try (ResultSet tablas = meta.getTables(null, null, nombreTabla, new String[]{"TABLE"})) {
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