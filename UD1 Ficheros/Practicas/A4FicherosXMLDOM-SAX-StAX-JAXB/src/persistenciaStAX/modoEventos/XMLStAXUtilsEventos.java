package persistenciaStAX.modoEventos;

import org.xml.sax.SAXException;
import persistenciaDOM.ExcepcionXML;
import persistenciaDOM.TipoValidacion;

import javax.xml.XMLConstants;
import javax.xml.namespace.QName;
import javax.xml.stream.*;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;


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
            // inputFactory.setProperty(XMLConstants.ACCESS_EXTERNAL_DTD, ""); // Comentado porque da error en algunas implementaciones
            // c) Activar el procesamiento seguro: impone límites de recursos y restricciones generales
            // inputFactory.setProperty(XMLConstants.FEATURE_SECURE_PROCESSING, Boolean.TRUE); // Comentado porque da error en algunas implementaciones

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

        // SOLO VALIDAR CON DOM, Si valida lo creo en stax sin validacion

    }

    /**
     * Valida el documento XML contra un esquema XSD.
     * Requiere referencia interna al XSD en el XML
     *
     * @param xmlFile Archivo XML a validar
     * @throws ExcepcionXML en caso de error de validación
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

    // ESCRITURA XML CON StAX EVENTOS

    /** Crea un XMLEventWriter para escribir un XML usando StAX (Eventos) *
     * @param rutaSalida Ruta del fichero de salida
     * @return XMLEventWriter configurado
     * @throws ExcepcionXML en caso de error
     * */
    public static XMLEventWriter crearWriterStAXEventos(String rutaSalida) throws ExcepcionXML {
        // Usa la codificación del sistema por defecto, para cambiar debo envolver el FileWriter en un
        // OutputStreamWriter con la codificación deseada (VER CURSOR)
        try {
            XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
            return outputFactory.createXMLEventWriter(new FileWriter(rutaSalida));
        } catch (XMLStreamException e) {
            throw new ExcepcionXML("No se pudo crear XMLEventWriter: " + e.getMessage(), e);
        } catch (Exception e){
            throw new ExcepcionXML("Error general al crear XMLEventWriter: " + e.getMessage(), e);
        }
    }

    /**
     * Añade la etiqueta de inicio (start element) al XML
     * @param writer XMLEventWriter donde se escribirá la etiqueta
     * @param nombre Nombre de la etiqueta a abrir
     * @throws ExcepcionXML en caso de error
     */
    public static void ADDStartElemento(XMLEventWriter writer, String nombre) throws ExcepcionXML {
        try {
            writer.add(XMLEventFactory.newInstance().createStartElement("", "", nombre));
        } catch (XMLStreamException e) {
            throw new ExcepcionXML("Error al escribir start element: " + nombre, e);
        }
    }

    /**
     * Añade la etiqueta de cierre (end element) al XML
     * @param writer XMLEventWriter donde se escribirá la etiqueta
     * @param nombre Nombre de la etiqueta a cerrar
     * @throws ExcepcionXML en caso de error
     */
    public static void ADDEndElemento(XMLEventWriter writer, String nombre) throws ExcepcionXML {
        try {
            writer.add(XMLEventFactory.newInstance().createEndElement("", "", nombre));
        } catch (XMLStreamException e) {
            throw new ExcepcionXML("Error al escribir end element: " + nombre, e);
        }
    }

    /**
     * Añade la declaración XML al inicio del documento
     * @param writer XMLEventWriter donde se escribirá la declaración
     * @throws ExcepcionXML en caso de error
     */
    public static void ADDDeclaracion(XMLEventWriter writer) throws ExcepcionXML {
        try {
            writer.add(XMLEventFactory.newInstance().createStartDocument("UTF-8","1.0"));
        } catch (XMLStreamException e) {
            throw new ExcepcionXML("Error al escribir la declaración XML.", e);
        }
    }

    /**
     * Añade un salto de línea con la indentación adecuada
     * @param writer XMLEventWriter donde se añadirá el salto de línea
     * @param nivel Nivel de indentación (número de espacios)
     * @throws ExcepcionXML en caso de error
     */
    public static void ADDSaltoLinea(XMLEventWriter writer, int nivel) throws ExcepcionXML {
        try {
            String indent = "\n" + " ".repeat(nivel); writer.add(XMLEventFactory.newInstance().createCharacters(indent));
        } catch (XMLStreamException e) {
            throw new ExcepcionXML("Error al añadir salto de línea/indentación.", e);
        }
    }

    /**
     * Añade un elemento con texto
     * @param writer XMLEventWriter donde se añadirá el elemento
     * @param nombre Nombre del elemento
     * @param valor Texto del elemento
     * @throws ExcepcionXML en caso de error
     */
    public static void ADDElemento(XMLEventWriter writer, String nombre, String valor) throws ExcepcionXML {
        try {
            var ef = XMLEventFactory.newInstance();
            writer.add(ef.createStartElement("", "", nombre));
            writer.add(ef.createCharacters(valor));
            writer.add(ef.createEndElement("", "", nombre));
        } catch (XMLStreamException e) {
            throw new ExcepcionXML("Error al añadir elemento: " + nombre, e);
        }
    }

    /**
     * Añade un elemento vacío
     * @param writer XMLEventWriter donde se añadirá el elemento
     * @param nombre Nombre del elemento
     * @throws ExcepcionXML en caso de error
     */
    public static void ADDElementoVacio(XMLEventWriter writer, String nombre) throws ExcepcionXML {
        try { var ef = XMLEventFactory.newInstance();
            writer.add(ef.createStartElement("", "", nombre));
            writer.add(ef.createEndElement("", "", nombre));
        } catch (XMLStreamException e) {
            throw new ExcepcionXML("Error al añadir elemento vacío: " + nombre, e);
        }
    }
}
