package persistenciaStAX.modoEventos;

import org.xml.sax.SAXException;
import persistenciaDOM.ExcepcionXML;
import persistenciaDOM.TipoValidacion;

import javax.xml.XMLConstants;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

// ESTO LISTO

public class XMLStAXUtilsEventos {

    /**
     * Crea el reader StAx para el XML utilizando el modelo Eventos.
     *
     * @param rutaFichero Ruta del fichero XML
     * @param validacion Tipo de validación que se aplicará
     * @return XMLStreamReader para leer el documento XML en modelo Eventos
     * @throws ExcepcionXML
     */
    public static XMLEventReader cargarDocumentoStAXEventos(String rutaFichero, TipoValidacion validacion) throws ExcepcionXML {
        try {
            // 1. COMPROBACIONES INICIALES. Ruta en blanco, validación nula y existencia del fichero

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

            // 2. APLICO VALIDACIÓN

            switch (validacion) {
                case XSD -> validarConXSD(file);
                case DTD -> validarConDTD(file);
                case NO_VALIDAR -> {
                }//No se aplica validación;
            }

            // 3. CREAR FACTORY Y CONFIGURACIONES DE SEGURIDAD

            XMLInputFactory inputFactory = XMLInputFactory.newFactory();

            inputFactory.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, Boolean.FALSE);
            // b) Restringir el acceso a DTDs externos y otros recursos (propiedad JAXP estándar)
            // El String vacío significa que no se permite el acceso a URLs externas
            inputFactory.setProperty(XMLConstants.ACCESS_EXTERNAL_DTD, "");
            // c) Activar el procesamiento seguro: impone límites de recursos y restricciones generales
            inputFactory.setProperty(XMLConstants.FEATURE_SECURE_PROCESSING, Boolean.TRUE);

            return inputFactory.createXMLEventReader(new FileInputStream(file), "UTF-8");

        } catch (Exception e) {
            throw new ExcepcionXML("Error al cargar el fichero: " + rutaFichero + ": " + e.getMessage(), e);
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
     * Obtiene el nombre de la etiqueta actual
     *
     * @param event Evento XML ya consumido
     * @return Nombre local de la etiqueta o null
     */
    public static String obtenerNombreEtiqueta(XMLEvent event){
    //Verifico si el evento es de apertura
        if(event.isStartElement()){
            // Convertir (castear) el XNLEvent a StartElement() para accedera sus metodos y obtener la parte local del nombre de este (sin namespace)
            return event.asStartElement().getName().getLocalPart();
        }
        else if (event.isEndElement()){
            return event.asEndElement().getName().getLocalPart();
        }
        else {
            return null;
        }
    }

    /**
     * Lee el texto de la etiqueta del evento
     *
     * @param event Evento XML ya consumido
     * @return El texto convertido a String
     */
    public static String leerTexto(XMLEvent event){
        // Si el evento es de texto
        String texto = event.isCharacters() ? event.asCharacters().getData().trim() : "";
        return texto;
    }

    /**
     * Extrae el valor de un atributo desde un evento de apertura
     *
     * @param event Evento XML ya consumido
     * @param nombre Nombre del atributo en local. (Sin el namespace)
     * @return Valor del atributo buscado.
     */
    public static String leerAtributo(XMLEvent event, String nombre){
        //Asegurarse de que es un evento de apertura
        if(event.isStartElement()){
            StartElement startElement = event.asStartElement();
            // Crear un Qname para buscar el atributo
            QName name = new QName(nombre);
            // Obtener el objeto atribute a  partir del qnam
            Attribute atributo = startElement.getAttributeByName(name);
            // Devolver si existe
            return atributo != null ? atributo.getValue() : null;
        }
        return null;
    }




}
