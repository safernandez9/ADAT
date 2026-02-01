package logica;

import persistencia.EmpresaDAO;
import java.sql.Connection;
public class GestorEmpresa {

    
    // EJERCICIO 1
    // ok - estandar
    
    /**
     * 
     * @param con Conexion a la base de datos
     */
    public static void ejercicio1(Connection con) {
        System.out.println("EJERCICIO 1");
        EmpresaDAO.ejercicio1(con).forEach(System.out::println);
    }

    // EJERCICIO 2
    // ok - concat no en sqlite
    public static void ejercicio2(Connection con) {
        System.out.println("EJERCICIO 2");

        try {
            String sgbd = con.getMetaData().getDatabaseProductName().toUpperCase();

            if (sgbd.contains("SQLITE")) { EmpresaDAO.ejercicio2_sqlite(con).forEach(System.out::println);
            } else {
                // MySQL y SQL Server
                EmpresaDAO.ejercicio2_mysql_sqlserver(con).forEach(System.out::println);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // EJERCICIO 3
    // fechas?
    public static void ejercicio3(Connection con) {

        System.out.println("EJERCICIO 3");

        try {
            String sgbd = con.getMetaData().getDatabaseProductName().toUpperCase();

            if (sgbd.contains("MYSQL")) {EmpresaDAO.ejercicio3_mysql(con).forEach(System.out::println);

            } else if (sgbd.contains("SQLITE")) {EmpresaDAO.ejercicio3_sqlite(con).forEach(System.out::println);

            } else if (sgbd.contains("SQL SERVER")) {EmpresaDAO.ejercicio3_sqlserver(con).forEach(System.out::println);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // EJERCICIO 4
    // if solo mysqlserver, s i no case when
    public static void ejercicio4(Connection con, String dep) {
        System.out.println("EJERCICIO 4");

        try {
            String sgbd = con.getMetaData().getDatabaseProductName().toUpperCase();

            if (sgbd.contains("MYSQL")) {
                EmpresaDAO.ejercicio4_mysql(con, dep).forEach(System.out::println);
            } else {
                EmpresaDAO.ejercicio4_sqlite_sqlserver(con, dep).forEach(System.out::println);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // EJERCICIO 5
    // concan't en SQLite
    public static void ejercicio5(Connection con, String p, String l) {
        System.out.println("EJERCICIO 5");

        try {
            String sgbd = con.getMetaData().getDatabaseProductName().toUpperCase();

            if (sgbd.contains("SQLITE")) {
                EmpresaDAO.ejercicio5_sqlite(con, p, l).forEach(System.out::println);
            } else {
                EmpresaDAO.ejercicio5_mysql_sqlserver(con, p, l).forEach(System.out::println);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // EJERCICIO 6
    // SUM(x) solo MySQL -- casewhen los otros
    public static void ejercicio6(Connection con) {
        System.out.println("EJERCICIO 6");

        try {
            String sgbd = con.getMetaData().getDatabaseProductName().toUpperCase();

            if (sgbd.contains("MYSQL")) {
                EmpresaDAO.ejercicio6_mysql(con).forEach(System.out::println);
            } else {
                EmpresaDAO.ejercicio6_sqlite_sqlserver(con).forEach(System.out::println);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    // EJERCICIO 7
    public static void ejercicio7(Connection con, int n) {
        System.out.println("EJERCICIO 7");
        EmpresaDAO.ejercicio7(con, n).forEach(System.out::println);
    }

    // EJERCICIO 8
    // concan't
    public static void ejercicio8(Connection con, double s) {
        System.out.println("EJERCICIO 8");

        try {
            String sgbd = con.getMetaData()
                    .getDatabaseProductName().toUpperCase();

            if (sgbd.contains("SQLITE")) {
                EmpresaDAO.ejercicio8_sqlite(con, s).forEach(System.out::println);
            } else {
                // MySQL y SQL Server
                EmpresaDAO.ejercicio8_mysql_sqlserver(con, s).forEach(System.out::println);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // EJERCICIO 9
    // Scroll ResultSet NO funciona en SQLite, Solo MySQL y SQL Server
    public static void ejercicio9(Connection con) {
        System.out.println("EJERCICIO 9");

        try {
            String sgbd = con.getMetaData()
                    .getDatabaseProductName().toUpperCase();

            if (!sgbd.contains("SQLITE")) {
                EmpresaDAO.ejercicio9_mysql_sqlserver(con);
            } else {
                System.out.println("Ejercicio 9 no soportado en SQLite");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // EJERCICIO 10
    // ⚠ SQLite antiguo no soporta subconsulta en FROM
    // ✔ MySQL y SQL Server
    public static void ejercicio10(Connection con) {
        System.out.println("EJERCICIO 10");

        try {
            String sgbd = con.getMetaData()
                    .getDatabaseProductName()
                    .toUpperCase();

            if (!sgbd.contains("SQLITE")) {
                EmpresaDAO.ejercicio10_mysql_sqlserver(con)
                        .forEach(System.out::println);
            } else {
                System.out.println("Ejercicio 10 no soportado en SQLite");
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
