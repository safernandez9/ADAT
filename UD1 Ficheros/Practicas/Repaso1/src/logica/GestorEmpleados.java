package logica;

import modelo.EmpleadoCompleto;
import persistenciaArchivos.persistenciaBin.EmpleadosBinDat;
import persistenciaArchivos.persistenciaObj.EmpleadosBinObj;
import persistenciaArchivos.persistenciaRand.EmpleadosRand;
import persistenciaArchivos.persistenciaTexto.EmpleadosTxt;
import persistenciaStAX.EmpleadosStAXCursor;
import persistenciaStAX.XMLStAXUtilsCursor;
import utilidades.TipoValidacion;

import javax.xml.stream.XMLStreamReader;
import java.util.ArrayList;
import java.util.List;

public class GestorEmpleados {

    private List<EmpleadoCompleto> empleados = new ArrayList<>();
    private final EmpleadosStAXCursor gestorEmpleadosStAX;
    private final EmpleadosTxt gestorTxt;
    private final EmpleadosBinDat gestorBinDat;
    private final EmpleadosBinObj gestorBinObj;
    private final EmpleadosRand gestorRand;

    public GestorEmpleados() {
        gestorEmpleadosStAX = new EmpleadosStAXCursor();
        gestorTxt = new EmpleadosTxt();
        gestorBinDat = new EmpleadosBinDat();
        gestorBinObj = new EmpleadosBinObj();
        gestorRand = new EmpleadosRand("Recursos/Salida/ficherosCombinados/empleados_rand.dat");
    }

    /**
     * Lee empleados desde varios archivos y los combina en una lista completa
     * @param rutaEmpleadosTxt Ruta del archivo de texto con datos básicos
     * @param rutaEmpleadosDat Ruta del archivo binario .dat con salarios
     * @param rutaEmpleadosObj Ruta del archivo serializado .obj con departamentos
     */
    public void leerEmpleadosVariosArchivos(String rutaEmpleadosTxt, String rutaEmpleadosDat, String rutaEmpleadosObj) {

        // Implementación de la lectura de empleados desde varios archivos
        try {
            empleados = gestorTxt.leerArchivoDeEmpleados(rutaEmpleadosTxt, ";", empleados);
            empleados = gestorBinDat.leerArchivoDeSalarios(rutaEmpleadosDat, empleados);
            empleados = gestorBinObj.leerArchivoDeDepartamentos(rutaEmpleadosObj, empleados);

            for(EmpleadoCompleto emp : empleados) {
                if(!validarEmpleado(emp)) {
                    System.err.println("Faltan datos para el empleado con ID: " + emp.getId());
                }
            }
        } catch (Exception e) {
            System.err.println("Error leyendo empleados desde varios archivos: " + e.getMessage());
        }

        listarEmpleados();
    }

    /**
     * Valida que un empleado tiene todos sus datos completos
     * @param emp Empleado a validar
     * @return true si el empleado es válido, false en caso contrario
     */
    private boolean validarEmpleado(EmpleadoCompleto emp) {
        return emp.getId() != 0 && emp.getNombre() != null && emp.getEdad() >= 0
                && emp.getDepartamento() != null && emp.getSalario() != 0.0;
    }

    /**
     * Lista los empleados en la consola
     */
    public void listarEmpleados() {
        for (EmpleadoCompleto emp : empleados) {
            System.out.println(emp);
        }
    }
    /**
     * Escribe la lista completa de empleados en un archivo XML usando StAX
     * @param rutaSalida Ruta del archivo XML de salida
     */
    public void escribirEmpleadosEnStAX(String rutaSalida) {
        try {
            gestorEmpleadosStAX.escribirEmpleados(rutaSalida, empleados);
        } catch (Exception e) {
            System.err.println("Error escribiendo empleados en StAX: " + e.getMessage());
        }
    }

        public  void leerEmpleadosStAX(String rutaEntrada, TipoValidacion tipoValidacion) {
            XMLStreamReader reader = XMLStAXUtilsCursor.cargarDocumentoStAXCursor(rutaEntrada, tipoValidacion);
            empleados = gestorEmpleadosStAX.leerEmpleados(reader);
        }


    public void guardarEmpleadosVariosArchivos(String rutaSalidaObj, String rutaSalidaRand, String validacionEdadTxt, String log) {


        gestorBinObj.escribirEmpleados(rutaSalidaObj, empleados, log);
        gestorRand.escribirEmpleados(rutaSalidaRand, empleados, log);
    }
}
