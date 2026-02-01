package logica;

//DatabaseMetadata meta
// meta.getTables()
// exported keys trae todas las tablas a las que referencia mi clave
// imported keys todas las de mis claves foraneas

// Los try que hago para los ResultSet son try with resources, que cierran automaticamente el recurso.
// Se distinguen por los parentesis despues del try.

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import modelo.*;
import util.GestorConexiones;
import util.TipoSGBD;

public class EmpresaDAO {

    private Connection conexion;

    /**
     * Constructor de EmpresaDAO
     * @param conexion conexión a la base de datos
     */
    public EmpresaDAO(Connection conexion) {
        this.conexion = conexion;
    }


    // EJERCICIO 6
    
    /**
     * Mostrar todos los departamentos
     * @return
     */
    public List<Departamento> mostrarDepartamentos() throws Exception {
        
        List<Departamento> lista = new ArrayList<>();
        String sql = "SELECT NumDepartamento, NomeDepartamento, NSSDirector FROM DEPARTAMENTO";

        try (ResultSet rs = GestorConexiones.ejecutarConsulta(conexion, sql)) {
            while (rs.next()) {
                Departamento d = new Departamento();
                d.setId(rs.getInt("NumDepartamento"));
                d.setNombre(rs.getString("NomeDepartamento"));
                d.setNssDirector(rs.getString("NSSDirector"));
                lista.add(d);
            }
        } catch (SQLException e) {
            throw new Exception("Error al mostrar departamentos", e);
        }

        return lista;
    }

    /**
     * Insertar un nuevo proyecto. Le asigna el numProyecto automáticamente.
     * @param p
     */
    public void insertarProyecto(Proxecto p) throws Exception {
        
        // Le doy numProyecto del último proyecto
        p.setNumProxecto(obtenerUltimoNumProyecto() + 1);
        
        String sqlInsert = "INSERT INTO PROXECTO (NumProxecto, NomeProxecto, Lugar, NumDepartControla) VALUES (?,?,?,?)";
        
        try{
            GestorConexiones.ejecutarSentencia(conexion, sqlInsert, p.getNumProxecto(), p.getNomeProxecto(), p.getLugar(), p.getNumDepartControla());  
        }catch(SQLException e){
            throw new Exception("Error al insertar proyecto", e);
        }
    }

    /**
     * Obtener el último número de proyecto
     * @return último número de proyecto
     */
    private int obtenerUltimoNumProyecto() throws Exception {
        String sql = "SELECT MAX(NumProxecto) FROM PROXECTO";
        try (ResultSet rs = GestorConexiones.ejecutarConsulta(conexion, sql)) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new Exception("Error al obtener último número de proyecto", e);
        }
        return 0;
    }

    /**
     * Comprobar si existe un proyecto por su nombre
     * @param nombreProyecto
     * @return
     */
    public boolean existeProyecto(String nombreProyecto) throws Exception {
        String sql = "SELECT COUNT(*) FROM PROXECTO WHERE NomeProxecto = ?";
        try (ResultSet rs = GestorConexiones.ejecutarConsulta(conexion, sql, nombreProyecto)) {
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            throw new Exception("Error al comprobar existencia del proyecto", e);
        }
        return false;
    }

    // EJERCICIO 7

    /**
     * Comprobar si existe un departamento por su número
     * @param numDepartamento
     * @return
     */
    public boolean existeDepartamento(int numDepartamento) {
        String sql = "SELECT COUNT(*) FROM DEPARTAMENTO WHERE NumDepartamento = ?";
        try (ResultSet rs = GestorConexiones.ejecutarConsulta(conexion, sql, numDepartamento)) {
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al comprobar existencia del departamento", e);
        }
        return false;
    }

    // ---------------- TABLAS AUXILIARES ----------------

    /**
     * Crear tabla Familiar. Aquí dejo un ejemplo de las Strings multilinea que usaremos para consultas
     * Son sentencias DDL así que usaremos ejecutarLoteTransaccional
     * @throws Exception
     */
    public void crearTablaFamiliar() throws Exception {

        if(GestorConexiones.tablaExiste(conexion, "FAMILIAR")) {
            throw new Exception("La tabla FAMILIAR ya existe");
        }
        String sql = "";

        switch (GestorConexiones.getTipoSGBD(conexion)) {
            case TipoSGBD.SQLSERVER:
                sql = """
                        CREATE TABLE FAMILIAR (
                            NSS_EMPLEADO     VARCHAR(15) NOT NULL,
                            NUM_FAMILIAR     INT NOT NULL,
                            NSS_FAMILIAR     VARCHAR(15) NOT NULL,
                            NOME             VARCHAR(50) NOT NULL,
                            APELIDOS         VARCHAR(100) NOT NULL,
                            DATA_NACEMENTO   DATE NOT NULL,
                            PARENTESCO       VARCHAR(30) NOT NULL,
                            SEXO             CHAR(1) DEFAULT 'M',
                        
                            CONSTRAINT PK_FAMILIAR
                                PRIMARY KEY (NSS_EMPLEADO, NUM_FAMILIAR),
                        
                            CONSTRAINT FK_FAMILIAR_EMPLEADO
                                FOREIGN KEY (NSS_EMPLEADO)
                                REFERENCES EMPREGADO(NSS),
                        
                            CONSTRAINT CK_FAMILIAR_SEXO
                                CHECK (SEXO IN ('H','M'))
                        );
                    """;
                break;
            case TipoSGBD.MYSQL:
                sql = """
                        CREATE TABLE FAMILIAR (
                            NSS_EMPLEADO     VARCHAR(15) NOT NULL,
                            NUM_FAMILIAR     INT NOT NULL,
                            NSS_FAMILIAR     VARCHAR(15) NOT NULL,
                            NOME             VARCHAR(50) NOT NULL,
                            APELIDOS         VARCHAR(100) NOT NULL,
                            DATA_NACEMENTO   DATE NOT NULL,
                            PARENTESCO       VARCHAR(30) NOT NULL,
                            SEXO             CHAR(1) DEFAULT 'M',
                        
                            CONSTRAINT PK_FAMILIAR
                                PRIMARY KEY (NSS_EMPLEADO, NUM_FAMILIAR),
                        
                            CONSTRAINT FK_FAMILIAR_EMPLEADO
                                FOREIGN KEY (NSS_EMPLEADO)
                                REFERENCES empregado(NSS),
                        
                            CONSTRAINT CK_FAMILIAR_SEXO
                                CHECK (SEXO IN ('H','M'))
                        );
                        """;
                break;
            case TipoSGBD.SQLITE:
                sql = """
                        CREATE TABLE FAMILIAR (
                            NSS_EMPLEADO     TEXT NOT NULL,
                            NUM_FAMILIAR     INTEGER NOT NULL,
                            NSS_FAMILIAR     TEXT NOT NULL,
                            NOME             TEXT NOT NULL,
                            APELIDOS         TEXT NOT NULL,
                            DATA_NACEMENTO   DATE NOT NULL,
                            PARENTESCO       TEXT NOT NULL,
                            SEXO             CHAR(1) DEFAULT 'M'
                             CHECK (SEXO IN ('H','M')),
                        
                         PRIMARY KEY (NSS_EMPLEADO, NUM_FAMILIAR),
                         FOREIGN KEY (NSS_EMPLEADO) REFERENCES EMPREGADO(NSS)
                        );
                        """;
                break;
        }

        try {
            GestorConexiones.ejecutarLoteTransaccional(conexion, sql);
        } catch (SQLException e) {
            throw new RuntimeException("Error al crear tabla Familiar", e);
        }
    }

    public void crearTablaVehiculo() throws Exception {

        if(GestorConexiones.tablaExiste(conexion, "VEHICULO")) {
            throw new Exception("La tabla VEHICULO ya existe");
        }
        if(GestorConexiones.tablaExiste(conexion, "VEHICULO_PROPIO")) {
            throw new Exception("La tabla VEHICULO_PROPIO ya existe");
        }
        if(GestorConexiones.tablaExiste(conexion, "VEHICULO_RENTING")) {
            throw new Exception("La tabla VEHICULO_RENTING ya existe");
        }

        String sqlVehiculo = "";
        String sqlVehiculoPropio = "";
        String sqlVehiculoRenting = "";

        switch (GestorConexiones.getTipoSGBD(conexion)) {
            case TipoSGBD.SQLSERVER:
                sqlVehiculo = """
                       CREATE TABLE VEHICULO (
                               CodVehiculo INT IDENTITY(1,1) PRIMARY KEY,
                               Matricula VARCHAR(20) NOT NULL UNIQUE,
                               Marca VARCHAR(50) NOT NULL,
                               Modelo VARCHAR(50) NOT NULL,
                               TipoCombustible VARCHAR(20) NOT NULL
                           );
                    """;

                sqlVehiculoPropio = """
                          CREATE TABLE VEHICULO_PROPIO (
                                 CodVehiculo INT PRIMARY KEY,
                                 DataCompra DATE NOT NULL,
                                 FOREIGN KEY (CodVehiculo) REFERENCES VEHICULO(CodVehiculo)
                           );
                        """;

                sqlVehiculoRenting = """
                          CREATE TABLE VEHICULO_RENTING (
                              CodVehiculo INT PRIMARY KEY,
                              FechaInicio DATE NOT NULL,
                              PrecioMensual DECIMAL(10,2) NOT NULL,
                              MesesContratados INT NOT NULL,
                              FOREIGN KEY (CodVehiculo) REFERENCES VEHICULO(CodVehiculo)
                          );
                        """;

                break;
            case TipoSGBD.MYSQL:
                sqlVehiculo = """
                        CREATE TABLE VEHICULO (
                               CodVehiculo INTEGER PRIMARY KEY AUTO_INCREMENT,
                               Matricula VARCHAR(20) NOT NULL UNIQUE,
                               Marca VARCHAR(50) NOT NULL,
                               Modelo VARCHAR(50) NOT NULL,
                               TipoCombustible VARCHAR(20) NOT NULL
                           );
                        """;

                sqlVehiculoPropio = """
                          CREATE TABLE VEHICULO_PROPIO (
                                 CodVehiculo INT PRIMARY KEY,
                                 DataCompra DATE NOT NULL,
                                 FOREIGN KEY (CodVehiculo) REFERENCES VEHICULO(CodVehiculo)
                           );
                        """;

                sqlVehiculoRenting = """
                          CREATE TABLE VEHICULO_RENTING (
                              CodVehiculo INT PRIMARY KEY,
                              FechaInicio DATE NOT NULL,
                              PrecioMensual DECIMAL(10,2) NOT NULL,
                              MesesContratados INT NOT NULL,
                              FOREIGN KEY (CodVehiculo) REFERENCES VEHICULO(CodVehiculo)
                          );
                        """;
                break;
            case TipoSGBD.SQLITE:
                sqlVehiculo = """
                        CREATE TABLE VEHICULO (
                               CodVehiculo INTEGER PRIMARY KEY,
                               Matricula VARCHAR(20) NOT NULL UNIQUE,
                               Marca VARCHAR(50) NOT NULL,
                               Modelo VARCHAR(50) NOT NULL,
                               TipoCombustible VARCHAR(20) NOT NULL
                           );
                        """;

                sqlVehiculoPropio = """
                          CREATE TABLE VEHICULO_PROPIO (
                                 CodVehiculo INTEGER PRIMARY KEY,
                                 DataCompra DATE NOT NULL,
                                 FOREIGN KEY (CodVehiculo) REFERENCES VEHICULO(CodVehiculo)
                           );
                        """;

                sqlVehiculoRenting = """
                          CREATE TABLE VEHICULO_RENTING (
                              CodVehiculo INTEGER PRIMARY KEY,
                              FechaInicio DATE NOT NULL,
                              PrecioMensual DECIMAL(10,2) NOT NULL,
                              MesesContratados INT NOT NULL,
                              FOREIGN KEY (CodVehiculo) REFERENCES VEHICULO(CodVehiculo)
                          );
                        """;
                break;
        }

        try {
            GestorConexiones.ejecutarLoteTransaccional(conexion, sqlVehiculo, sqlVehiculoPropio, sqlVehiculoRenting);
        } catch (SQLException e) {
            throw e;
        }
    }

            
}
