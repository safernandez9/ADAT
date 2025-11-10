package persistenciaStAX.modoCursor;

import clases.Corredor;
import clases.Puntuacion;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CorredoresStAXCursor {

    public List<Corredor> corredores = new ArrayList<>();
    Corredor corredorActual = null;
    List <Puntuacion> historialActual = null;
    String anioActual = null;
    String contenidoActual = "";

    /**
     *
     * @param reader
     */
    public static void leerCorredores(XMLStreamReader reader) {

        // COMPROBACIONES BÁSICAS
        if (reader == null) return;

        // Variable para guardar el nombre de la etiqueta que se acaba de abrir
        String etiquetaActual = "";
        System.out.println("--- INICIO DE PROCESAMIENTO ---");
        try {
            // Bucle principal: El corazón del modelo PULL
            while (reader.hasNext()) {
                int eventType = reader.next();
                switch (eventType) {
                    case XMLStreamConstants.START_ELEMENT -> {
                        // 1. Guardamos el nombre de la nueva etiqueta
                        etiquetaActual = reader.getLocalName();
                        // 2. Lógica específica al encontrar el inicio de un LIBRO
                        if ("libro".equals(etiquetaActual)) {
                            // Extraemos el ID como atributo
                            String id = reader.getAttributeValue(null, "id");
                            System.out.println("\nLIBRO ENCONTRADO [ID: " + id + "]");
                        }
                    }
                    case XMLStreamConstants.CHARACTERS -> {
                        contenidoActual = XMLStAXUtilsCursor.leerTexto(reader);
                    }
                    case XMLStreamConstants.END_ELEMENT -> {
                        // 4. Lógica de cierre
                        if ("libro".equals(reader.getLocalName())) {
                            System.out.println("--------------------");
                        }
                        // limpiar etiquetaActual
                        // etiquetaActual = "";
                    }
                    case XMLStreamConstants.START_DOCUMENT ->
                        // Casos de una sola línea pueden ir sin llaves
                            System.out.println("Documento listo para ser leído.");
                }
                reader.close();
            } catch (Exception e) {
                System.err.println("Error durante la lectura StAX: " +
                        e.getMessage());
            } }




    }
