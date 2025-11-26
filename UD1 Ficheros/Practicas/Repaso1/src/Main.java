import logica.GestorEmpleados;
import utilidades.TipoValidacion;

public class Main {

    static GestorEmpleados gestor = new GestorEmpleados();
    static final String RUTA_EMPLEADOS_TXT = "Recursos/Entrada/texto/empleados.txt";
    static final String RUTA_EMPLEADOS_DAT = "Recursos/Entrada/binario/salarios.dat";
    static final String RUTA_EMPLEADOS_OBJ = "Recursos/Entrada/serializado/departamentos.obj";
    static final String RUTA_SALIDA = "Recursos/Salida/xmlGenerado/empleados.xml";
    static final String RUTA_SALIDA_OBJ = "Recursos/Salida/ficherosCombinados/empleado.obj";
    static final String RUTA_SALIDA_RAND = "Recursos/Salida/ficherosCombinados/empleados_rand.dat";
    static final String VALIDACION_EDAD_TXT = "Recursos/Entrada/edadPermitida.txt";
    static final String LOG = "Recuros/Salida/ficherosCombinados/log.txt";

    public static void main(String[] args) {

        gestor.leerEmpleadosVariosArchivos(RUTA_EMPLEADOS_TXT, RUTA_EMPLEADOS_DAT, RUTA_EMPLEADOS_OBJ);
        gestor.escribirEmpleadosEnStAX(RUTA_SALIDA);
        gestor.leerEmpleadosStAX(RUTA_SALIDA, TipoValidacion.NO_VALIDAR);
        gestor.listarEmpleados();

        gestor.guardarEmpleadosVariosArchivos(RUTA_SALIDA_OBJ, RUTA_SALIDA_RAND, VALIDACION_EDAD_TXT, LOG);

    }

}
