// Saúl Fernández Salgado
package persistenciaXML;

import utilidades.ExcepcionXML;

import javax.xml.stream.*;
import java.io.File;
import java.io.FileInputStream;


public class XMLStAXUtilsCursor {

    /**
     * Crea el reader StAx para el XML utilizando el modelo cursor.
     *
     * @param rutaFichero Ruta del fichero XML
     * @return XMLStreamReader para leer el documento XML en modelo cursor
     * @throws ExcepcionXML excepción en el procesamiento del XML
     */
    public static XMLStreamReader cargarDocumentoStAXCursor(String rutaFichero) throws ExcepcionXML {
        try {

            // COMPROBACIONES INICIALES. Ruta en blanco, validación nula y existencia del fichero

            if (rutaFichero == null || rutaFichero.isBlank()) {
                throw new ExcepcionXML("La ruta del fichero XML " + rutaFichero + " está vacía: ");
            }
            File file = new File(rutaFichero);
            if (!file.exists()) {
                throw new ExcepcionXML("El fichero " + rutaFichero + " no existe");
            }

            // SIN VALIDACIÓN

            // CREAR FACTORY Y CONFIGURACIONES DE SEGURIDAD

            XMLInputFactory inputFactory = XMLInputFactory.newFactory();

            inputFactory.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, Boolean.FALSE);
            // b) Restringir el acceso a DTDs externos y otros recursos (propiedad JAXP estándar)
            // El String vacío significa que no se permite el acceso a URLs externas (COMENTO POR ERRORES EN CASA)
            // inputFactory.setProperty(XMLConstants.ACCESS_EXTERNAL_DTD, "");
            // c) Activar el procesamiento seguro: impone límites de recursos y restricciones generales (COMENTO POR ERRORES EN CASA)
            // inputFactory.setProperty(XMLConstants.FEATURE_SECURE_PROCESSING, Boolean.TRUE);

            return inputFactory.createXMLStreamReader(new FileInputStream(file), "UTF-8");

        } catch (Exception e) {
            throw new ExcepcionXML("Error al crear XMLStreamReader: " + e.getMessage(), e);
        }
    }


    /**
     * Extrae el texto del evento actual si es de tipo CHARACTER
     * @param lector XMLStreamReader lector StAX
     * @return Texto del evento actual o cadena vacía si no es CHARACTER
     */
    public static String leerTexto(XMLStreamReader lector) {
        return lector.getEventType() == XMLStreamConstants.CHARACTERS ? lector.getText().trim() : "";
    }

    /**
     * Extrae el valor de un atributo dado en la etiqueta actual
     *
     * @param lector XMLStreamReader, lector StAX
     * @param nombre Nombre del atributo a leer
     * @return Valor del atributo o null si no existe
     */
    public static String leerAtributo(XMLStreamReader lector, String nombre) {
        return lector.getAttributeValue(null, nombre);

    }

    /**
     * Devuelve el nombre local (sin prefijo) de la etiqueta actual
     * solo si es etiqueta de inicio o cierre
     *
     * @param lector XMLStreamReader, lector StAX
     * @return Nombre de la etiqueta (solo lo devuelve si es de apertura o cierre)
     */
    public static String obtenerNombreEtiqueta(XMLStreamReader lector) {
        if (lector.isStartElement() || lector.isEndElement()) {
            return lector.getLocalName();
        } else {
            return null;
        }
    }

}
