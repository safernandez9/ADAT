package persistenciaArchivos.persistenciaBin;

import excepciones.ExcepcionFicheros;
import modelo.EmpleadoCompleto;

import java.io.EOFException;
import java.util.ArrayList;
import java.util.List;

public class EmpleadosBinDat {


    /**
     * Lee un archivo binario que contiene IDs y salarios de empleados,
     * y actualiza la lista de empleados con los salarios correspondientes.
     * Si un ID del archivo no coincide con ningún empleado en la lista,
     * se lanza una ExcepcionFicheros.
     *
     * @param ruta ruta del archivo binario de salarios
     * @param empleados lista de empleados a actualizar
     * @return lista de empleados actualizada con los salarios
     * @throws ExcepcionFicheros si ocurre un error en la lectura del archivo o si hay IDs no coincidentes
     */
    public List<EmpleadoCompleto> leerArchivoDeSalarios(String ruta, List<EmpleadoCompleto> empleados) throws ExcepcionFicheros {

        LecturaBinDat lector = new LecturaBinDat(ruta);
        lector.abrirArchivo();

        try {
            // Lectura hasta el final del archivo
            while (true) {
                boolean encontrado = false;

                int id = lector.leerInt();
                double sueldo = lector.leerDouble();

                // Buscar el empleado por ID y actualizar su salario
                for (EmpleadoCompleto emp : empleados) {
                    if (emp.getId() == id) {
                        emp.setSalario(sueldo);
                        encontrado = true;
                        break;
                    }
                }
                if(!encontrado){
                    throw new ExcepcionFicheros("El ID " + id +
                            " del archivo binario no coincide con ningún empleado.");
                }
            }

        } catch (EOFException e) {
            try{
                lector.cerrarArchivo();
                return empleados;
            } catch (Exception ex){
                throw new ExcepcionFicheros("Error al cerrar el archivo de salarios: " + e.getMessage(), e);
            }
        } catch (ExcepcionFicheros e){
            throw new ExcepcionFicheros("Error en la lectura de los salarios: " + e.getMessage(),e);
        }
    }
}
