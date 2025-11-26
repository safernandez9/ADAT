package persistenciaArchivos.persistenciaObj;

import excepciones.ArchivoNoExisteException;
import excepciones.ExcepcionFicheros;
import modelo.EmpleadoCompleto;
import modelo.EmpleadoDepartamento;

import java.io.EOFException;
import java.util.List;

public class EmpleadosBinObj {

    public List<EmpleadoCompleto> leerArchivoDeDepartamentos(String rutaEmpleadosObj, List<EmpleadoCompleto> empleados) throws ExcepcionFicheros {

        LectorObj lector = new LectorObj(rutaEmpleadosObj);
        List<EmpleadoDepartamento> departamentos;
        EmpleadoDepartamento departamento;

        try {
            // Abrir el archivo para lectura
            lector.iniciarLectura();

            // Leer empleados y asignar departamentos
            while (true) {

                boolean encontrado = false;
                departamento = (EmpleadoDepartamento) lector.leerObjeto();
                for (EmpleadoCompleto emp : empleados) {
                    if (emp.getDepartamento().equals(departamento.getIdDepartamento())) {
                        emp.setLocalizacion(departamento.getLocalizacion());
                        encontrado = true;
                        break;
                    }
                }
                if (!encontrado) {
                    throw new ExcepcionFicheros("No existe el departamento con ID: " + departamento.getIdDepartamento() + " para asignarle departamento.");
                }
            }

        } catch (ArchivoNoExisteException e) {
            throw new ExcepcionFicheros("Error al leer el archivo: " + e.getMessage(), e);
        } catch (EOFException e) {
            // lectura terminada
        } finally {
            lector.finalizarLectura();
        }

        return empleados;
    }
}


