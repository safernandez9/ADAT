package persistenciaStAX.modoCursor;

import org.xml.sax.SAXException;
import persistenciaDOM.ExcepcionXML;
import persistenciaDOM.TipoValidacion;

import javax.xml.XMLConstants;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

// Por ahora al día.

public class XMLStAXUtilsCursor {

    /**
     * Crea el reader StAx para el XML utilizando el modelo cursor.
     *
     * @param rutaFichero Ruta del fichero XML
     * @param validacion  Tipo de validació que se aplicará
     * @return XMLStreamReader para leer el documento XML en modelo cursor
     * @throws ExcepcionXML
     */
    public static XMLStreamReader cargarDocumentoStAXCursor(String rutaFichero, TipoValidacion validacion) throws ExcepcionXML {
        try {

            // COMPROBACIONES INICIALES. Ruta en blanco, validación nula y existencia del fichero

            if (rutaFichero == null || rutaFichero.isBlank()) {
                throw new ExcepcionXML("La ruta del fichero XML " + rutaFichero + " está vacía: ");
            }
            if (validacion == null) {
                throw new ExcepcionXML("El tipo de validacion es nulo");
            }

            File file = new File(rutaFichero);
            if (!file.exists()) {
                throw new ExcepcionXML("El fichero " + rutaFichero + " no existe");
            }


            // APLICO VALIDACIÓN

            switch (validacion) {
                case XSD -> validarConXSD(file);
                case DTD -> validarConDTD(file);
                case NO_VALIDAR -> {
                }//No se aplica validación;
            }


            // CREAR FACTORY Y CONFIGURACIONES DE SEGURIDAD

            XMLInputFactory inputFactory = XMLInputFactory.newFactory();

            inputFactory.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, Boolean.FALSE);
            // b) Restringir el acceso a DTDs externos y otros recursos (propiedad JAXP estándar)
            // El String vacío significa que no se permite el acceso a URLs externas
            inputFactory.setProperty(XMLConstants.ACCESS_EXTERNAL_DTD, "");
            // c) Activar el procesamiento seguro: impone límites de recursos y restricciones generales
            inputFactory.setProperty(XMLConstants.FEATURE_SECURE_PROCESSING, Boolean.TRUE);

            return inputFactory.createXMLStreamReader(new FileInputStream(file), "UTF-8");

        } catch (Exception e) {
            throw new ExcepcionXML("");
        }
    }

    /**
     * Como no se puede validar con DTD directamente validaremos con SAX
     * Metodo de prueba, no sabemos si funciona. Comentarlo si da errores
     *
     * @param xmlfile Archivo XML a validar
     */
    private static void validarConDTD(File xmlfile) {


    }

    /**
     * Valida el documento XML contra un esquema XSD.
     * Requiere referencia interna al XSD en el XML
     *
     * @param xmlFile Archivo XML a validar
     * @throws ExcepcionXML
     */
    private static void validarConXSD(File xmlFile) throws ExcepcionXML {
        try {
            SchemaFactory schemaFactory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
            Schema schema = schemaFactory.newSchema();
            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(xmlFile));
        } catch (IOException | SAXException e) {
            throw new ExcepcionXML("Error en la validación XSD:  " + e.getMessage(), e);
        }

    }

    /**
     * Extrae el texto del evento actual si es de tipo CHARACTER
     * @param lector
     * @return
     */
    public static String leerTexto(XMLStreamReader lector) {
        return lector.getEventType() == XMLStreamConstants.CHARACTERS ? lector.getText().trim() : "";
    }

    /**
     * Extrae el valor de un atributo dado en la etiqueta actual
     *
     * @param lector
     * @param nombre
     * @return
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
