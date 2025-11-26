package persistenciaArchivos.persistenciaTexto;

import excepciones.ArchivoNoExisteException;
import excepciones.ExcepcionFicheros;
import modelo.EmpleadoCompleto;

import java.io.IOException;
import java.util.List;

public class EmpleadosTxt {

    /**
     * Lee un archivo de empleados en formato texto y devuelve una lista de objetos EmpleadoCompleto
     *
     * @param rutaArchivo ruta del archivo de empleados
     * @param separador   separador de campos en el archivo
     * @return lista de empleados leídos del archivo
     * @throws ExcepcionFicheros si ocurre un error durante la lectura del archivo
     */
    public List<EmpleadoCompleto> leerArchivoDeEmpleados(String rutaArchivo, String separador, List<EmpleadoCompleto> empleados) throws ExcepcionFicheros {

        // Implementación de la lectura del archivo de empleados en formato texto
        LecturaTexto reader = new LecturaTexto(rutaArchivo);
        try {
            reader.abrirArchivo();
            String linea;
            while ((linea = reader.leerLinea()) != null) {
                String[] datos = linea.split(separador);
                if (datos.length >= 4) { // Suponiendo que hay al menos 4 campos
                    EmpleadoCompleto empleado = new EmpleadoCompleto();
                    empleado.setId(Integer.parseInt(datos[0]));
                    empleado.setNombre(datos[1]);
                    empleado.setEdad(Integer.parseInt(datos[2]));
                    empleado.setDepartamento(datos[3]);

                    // Verificar si el ID ya existe en la lista

                    if (idRepetido(empleados, empleado.getId())) {
                        throw new ExcepcionFicheros("Id de un empleado repetido: " + empleado.getId());
                    }
                    if (empleado.getEdad() < 0) {
                        throw new ExcepcionFicheros("Edad negativa para el empleado con ID: " + empleado.getId());
                    }

                    // Agregar el empleado a la lista
                    empleados.add(empleado);
                }
            }
        } catch (ExcepcionFicheros e) {
            throw new ExcepcionFicheros("Error leyendo archivo de empleados: " + e.getMessage());
        } catch (ArchivoNoExisteException | IOException e) {
            throw new ExcepcionFicheros("Error: leyendo archivo de empleados: " + e.getMessage());
        } finally {
            try {
                reader.cerrarArchivo();
            } catch (Exception e) {
            }
        }

        return empleados;
    }

    /**
     * Comprueba si un ID ya existe en la lista de empleados
     *
     * @param empleados lista de empleados
     * @param id        ID a comprobar
     * @return true si el ID está repetido, false en caso contrario
     */
    private boolean idRepetido(List<EmpleadoCompleto> empleados, int id) {
        int contadorId = 0;

        for (EmpleadoCompleto emp : empleados) {
            if (emp.getId() == id && contadorId == 1) {
                return true;
            } else if (emp.getId() == id) {
                contadorId++;
            }
        }
        return false;
    }


    // Esto de recibir la ruta del archivo por aqui, es rarete no se si esta bien.
    // Pero vamos temas de package y eso.
    public int leerEdadMaxima(String rutaArchivo) throws ExcepcionFicheros, ArchivoNoExisteException {
        LecturaTexto reader = new LecturaTexto(rutaArchivo);
        try {
            reader.abrirArchivo();

            String linea;

            while ((linea = reader.leerLinea()) != null) {
                String[] datos = linea.split("=");
                if (datos[0].contains("MAX")) {
                    return Integer.parseInt(datos[1]);
                }
            }

            throw new ExcepcionFicheros("No se encontró la edad máxima en el archivo: " + rutaArchivo);

        } catch (ArchivoNoExisteException e) {
            throw new ArchivoNoExisteException("Error en la lectura de la edad máxima: " + e.getMessage());
        } catch (IOException e) {
            throw new ExcepcionFicheros("Error en la lectura de la edad máxima: " + e.getMessage());
        }
    }

    public int leerEdadMinima(String rutaArchivo) throws ArchivoNoExisteException, ExcepcionFicheros {
        LecturaTexto reader = new LecturaTexto(rutaArchivo);
        try {
            reader.abrirArchivo();

            String linea;

            while ((linea = reader.leerLinea()) != null) {
                String[] datos = linea.split("=");
                if (datos[0].contains("MIN")) {
                    return Integer.parseInt(datos[1]);
                }
            }

            throw new ExcepcionFicheros("No se encontró la edad máxima en el archivo: " + rutaArchivo);

        } catch (ArchivoNoExisteException e) {
            throw new ArchivoNoExisteException("Error en la lectura de la edad máxima: " + e.getMessage());
        } catch (IOException e) {
            throw new ExcepcionFicheros("Error en la lectura de la edad máxima: " + e.getMessage());
        }
    }
}
