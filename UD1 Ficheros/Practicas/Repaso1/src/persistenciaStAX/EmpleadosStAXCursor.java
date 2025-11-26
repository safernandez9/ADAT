package persistenciaStAX;

import utilidades.Archivo;
import excepciones.ExcepcionXML;
import modelo.EmpleadoCompleto;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EmpleadosStAXCursor {

    public List<EmpleadoCompleto> leerEmpleados(XMLStreamReader reader) throws ExcepcionXML {
        List<EmpleadoCompleto> empleados = new ArrayList<>();
        EmpleadoCompleto empleadoActual = null;
        String contenidoActual = null;

        try {
            while (reader.hasNext()) {
                int tipo = reader.next();
                switch (tipo) {

                    case XMLStreamConstants.START_ELEMENT -> {
                        String nombreEtiqueta = XMLStAXUtilsCursor.obtenerNombreEtiqueta(reader);
                        switch (nombreEtiqueta) {
                            case "empleado" -> {
                                empleadoActual = new EmpleadoCompleto();
                                String idStr = XMLStAXUtilsCursor.leerAtributo(reader, "id");
                                empleadoActual.setId(Integer.parseInt(idStr));
                            }
                            case "nombre", "departamento" -> contenidoActual = ""; // texto
                            case "edad" -> contenidoActual = "0"; // numérico entero
                            case "salario" -> contenidoActual = "0.0"; // numérico decimal
                            // ignoramos localizacion
                        }
                    }

                    case XMLStreamConstants.CHARACTERS -> {
                        contenidoActual = XMLStAXUtilsCursor.leerTexto(reader);
                    }

                    case XMLStreamConstants.END_ELEMENT -> {
                        String nombreEtiqueta = XMLStAXUtilsCursor.obtenerNombreEtiqueta(reader);
                        switch (nombreEtiqueta) {
                            case "empleado" -> empleados.add(empleadoActual);
                            case "nombre" -> empleadoActual.setNombre(contenidoActual);
                            case "edad" -> empleadoActual.setEdad(Integer.parseInt(contenidoActual));
                            case "salario" -> empleadoActual.setSalario(Double.parseDouble(contenidoActual));
                            case "departamento" -> empleadoActual.setDepartamento(contenidoActual);
                            // ignoramos localizacion
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new ExcepcionXML("Error al leer los empleados: " + e.getMessage(), e);
        }

        return empleados;
    }


    public void escribirEmpleados(String rutaSalida, List<EmpleadoCompleto> empleados) {
        // Implementación de escritura de empleados en XML usando StAX Cursor

        File archivo = new File(rutaSalida);

        // Crear directorios padres si no existen
        if (!archivo.getParentFile().exists()) {
            if (!archivo.getParentFile().mkdirs()) {
                throw new ExcepcionXML("No se ha podido crear el directorio para el archivo XML de salida.");
            }
        }

        int nivel = 0; // nivel de indentación inicial

        try {
            XMLStreamWriter writer = XMLStAXUtilsCursor.crearWriterStAX(rutaSalida);

            // Declaración XML
            XMLStAXUtilsCursor.ADDDeclaracion(writer);

            // Inicio raíz <empleados>
            XMLStAXUtilsCursor.ADDStartElemento(writer, "empleados");
            XMLStAXUtilsCursor.ADDSaltoLinea(writer, ++nivel);

            for (EmpleadoCompleto empleado : empleados) {
                // Inicio <empleado>
                XMLStAXUtilsCursor.ADDStartElemento(writer, "empleado");
                XMLStAXUtilsCursor.ADDAtributo(writer, "id", Integer.toString(empleado.getId()));
                XMLStAXUtilsCursor.ADDSaltoLinea(writer, ++nivel);

                // Campos internos
                XMLStAXUtilsCursor.ADDElemento(writer, "nombre", empleado.getNombre());
                XMLStAXUtilsCursor.ADDSaltoLinea(writer, nivel);

                XMLStAXUtilsCursor.ADDElemento(writer, "edad", Integer.toString(empleado.getEdad()));
                XMLStAXUtilsCursor.ADDSaltoLinea(writer, nivel);

                XMLStAXUtilsCursor.ADDElemento(writer, "salario", Double.toString(empleado.getSalario()));
                XMLStAXUtilsCursor.ADDSaltoLinea(writer, nivel);

                XMLStAXUtilsCursor.ADDElemento(writer, "departamento", empleado.getDepartamento());
                XMLStAXUtilsCursor.ADDSaltoLinea(writer, nivel);

                XMLStAXUtilsCursor.ADDElemento(writer, "localizacion", empleado.getLocalizacion());
                XMLStAXUtilsCursor.ADDSaltoLinea(writer, nivel);

                // Fin <empleado>
                nivel--;
                XMLStAXUtilsCursor.ADDEndElemento(writer);
                XMLStAXUtilsCursor.ADDSaltoLinea(writer, nivel);
            }

            // Fin raíz <empleados>
            nivel--;
            XMLStAXUtilsCursor.ADDEndElemento(writer);

        } catch (ExcepcionXML e) {
            throw new ExcepcionXML("Error al escribir el archivo XML de empleados: " + e.getMessage(), e);
        }
    }
}


