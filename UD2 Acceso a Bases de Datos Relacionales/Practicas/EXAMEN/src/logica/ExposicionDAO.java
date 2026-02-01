// Saul Fernandez Salgado 77013586H
package logica;

import modelo.Fotografia;
import modelo.FotografiaArtistica;
import modelo.FotografiaDocumental;
import utilidades.GestorConexiones;
import utilidades.ResultadoProcedimiento;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

public class ExposicionDAO {

    private Connection conexion;

    public ExposicionDAO(Connection conexion){
        this.conexion = conexion;
    }

    // Ejercicio 1

    /**
     *
     * @throws SQLException
     */
    public void crearTablaLaboratorioYFotografoColabora() throws SQLException {

        String sqlLaboratorio = """
                CREATE TABLE LABORATORIO (
                idLaboratorio INT IDENTITY(1,1),
                nombre VARCHAR(100) NOT NULL,
                anioInauguracion DATE NOT NULL,
                
                CONSTRAINT PK_LABORATORIO PRIMARY KEY (idLaboratorio),
                CONSTRAINT UQ_NOMBRELABORATORIO UNIQUE (nombre)
                );
                """;

        String sqlFotografoColabora = """
                CREATE TABLE FOTOGRAFO_COLABORA (
                idFotografo INT NOT NULL,
                idLaboratorio INT NOT NULL,
                fechaInicio DATE NOT NULL,
                fechaFin DATE,
                CONSTRAINT PK_FOTOGRAFO_COLABORA PRIMARY KEY (idFotografo, idLaboratorio, fechaInicio),
                CONSTRAINT FK_FOTOGRAFO_FOTOGRAFOCOLABORA FOREIGN KEY (idFotografo) REFERENCES FOTOGRAFO(CODIGO),
                CONSTRAINT FK_LABORATORIO_FOTOGRAFOCOLABORA FOREIGN KEY (idLaboratorio) REFERENCES LABORATORIO(idLaboratorio),
                CONSTRAINT CK_FECHAFIN_FOTOGRAFOCOLABORA CHECK (fechaFin IS NULL OR fechaFIN >= fechaInicio)
                );
                """;

        try {
            GestorConexiones.ejecutarLoteTransaccional(conexion, sqlLaboratorio, sqlFotografoColabora);
        } catch (SQLException e) {
            throw new SQLException("Error al crear tablas Laboratorio y FotografoColabora: " + e.getMessage());
        }
    }


    // Ejercicio 2

    /**
     * Comprueba si existe un Fotografo por su nombre
     * @param nombreFotografo
     * @return
     * @throws SQLException
     */
    public int existeFotografo(String nombreFotografo) throws SQLException {

        String sql = "SELECT codigo AS CODIGO FROM FOTOGRAFO WHERE NOME = ?";

        try (ResultSet rs = GestorConexiones.ejecutarConsulta(conexion, sql, nombreFotografo)) {
            if (rs.next()) {
                return rs.getInt("CODIGO");
            }
        } catch (SQLException e) {
            throw new SQLException("Error al comprobar la existencia del fotógrafo: " + e.getMessage());
        }

        return -1;
    }

    /**
     * Comprueba una exposicion por su nombre
     * @param nombreExposicion
     * @return
     * @throws SQLException
     */
    public int existeExposicion(String nombreExposicion) throws SQLException {

        String sql = "SELECT CODIGO AS CODIGO FROM EXPOSICION WHERE NOME = ?";

        try (ResultSet rs = GestorConexiones.ejecutarConsulta(conexion, sql, nombreExposicion)) {
            if (rs.next()) {
                return rs.getInt("CODIGO");
            }
        } catch (SQLException e) {
            throw new SQLException("Error al comprobar la existencia de la exposicion: " + e.getMessage());
        }

        return -1;
    }

    public void insertarFotografias(List<Fotografia> fotos) throws SQLException {

        String sqlFotografia = """
                INSERT INTO FOTOGRAFIA (NOME, MEDIDAS, DATA, COD_FOTOGRAFO, COD_EXPOSICION, COLOR)
                VALUES (?,?,?,?,?,?)
                """;
        String sqlFotografiaArtistica = """
                INSERT INTO ARTISTICA (CODIGO, ENCUADRE, COMPOSICION)
                VALUES (?,?,?)
                """;
        String sqlFotografiaDocumental = """
                INSERT INTO DOCUMENTAL (CODIGO, TIPO)
                VALUES (?,?)
                """;
        String sqlFuncionNumFotos = "{? = call fn_nFotFotografo(?)}";

        String sqlActualizarFotografos = """
                UPDATE FOTOGRAFO
                SET NUMFOTOGRAFIAS = ?
                where CODIGO = ?
                """;

        try {
            conexion.setAutoCommit(false);

            for(Fotografia f: fotos){

                // INSERCION FOTO

                int claveGenerada = GestorConexiones.ejecutarSentenciaRecuperandoClave(conexion, sqlFotografia,
                        f.getNome(), f.getMedidas(), f.getData(), f.getCodFotografo(), f.getCodExposicion(), String.valueOf(f.getColor()));

                if(f instanceof FotografiaArtistica){

                    FotografiaArtistica fA = (FotografiaArtistica) f;
                    GestorConexiones.ejecutarSentencia(conexion, sqlFotografiaArtistica,
                            claveGenerada, fA.getEncuadre(), fA.getComposicion());

                } else if (f instanceof FotografiaDocumental){

                    FotografiaDocumental fD = (FotografiaDocumental) f;
                    GestorConexiones.ejecutarSentencia(conexion, sqlFotografiaDocumental,
                            claveGenerada, fD.getTipo());

                }

                // ACTUALIZACION NUMERO FOTOS FOTOGRAFO

                Object resultado = GestorConexiones.ejecutarFuncionEscalar(conexion, sqlFuncionNumFotos, Types.INTEGER, f.getCodFotografo());

                int nFotosFotografo = (resultado != null) ? (Integer) resultado : 0;

                int filasAfectadas = GestorConexiones.ejecutarSentencia(conexion, sqlActualizarFotografos, nFotosFotografo, f.getCodFotografo());


            }
        }
        catch (SQLException e){
            conexion.rollback();
            throw new SQLException("Error al insertar las fotografias." + e.getMessage());
        } finally {
            conexion.setAutoCommit(true);
        }
    }


    // Ejercicio 3

    public String obtenerLocalidadExpo(String expoOrigen) throws SQLException {

        try {

            String sql = "{call pr_localidadProvincia(?,?)}";

            ResultadoProcedimiento res = GestorConexiones.ejecutarProcedimiento(conexion, sql, new Object[]{expoOrigen},
                    new int[]{Types.VARCHAR});

            return res.getParametrosOut()[0].toString();

        } catch (SQLException e) {
            throw new SQLException("Error al obtener los datos de provincia y localidad: " + e.getMessage());
        }

    }

}
