package persistenciaStAX.modoCursor;

import org.xml.sax.SAXException;
import persistenciaDOM.ExcepcionXML;
import persistenciaDOM.TipoValidacion;
import persistenciaStAX.modoEventos.XMLStAXUtilsEventos;

import javax.xml.XMLConstants;
import javax.xml.stream.*;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

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
            throw new ExcepcionXML("Error al crear XMLStreamReader: " + e.getMessage(), e);
        }
    }

    /**
     * Como no se puede validar con DTD directamente validaremos con SAX
     * Metodo de prueba, no sabemos si funciona. Comentarlo si da errores
     * Lo haremos validandolo con SAX o DOM primero
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



    /**
     * Calcula el total de donaciones por patrocinador desde un XML usando modelo Cursor
     * @param rutaEntrada ruta del fichero XML
     * @return TreeMap con clave=NombrePatrocinador y valor=totalDonado
     * @throws ExcepcionXML
     */
    public static Map<String, Double> calcularDonaciones(String rutaEntrada) throws ExcepcionXML {
        XMLStreamReader reader = null;
        Map<String, Double> mapaDonaciones = new TreeMap<>();
        try {
            reader = XMLStAXUtilsCursor.cargarDocumentoStAXCursor(rutaEntrada, persistenciaDOM.TipoValidacion.XSD);

            String nombrePatrocinador = "";
            Double donacionActual = 0.0;
            boolean esPatrocinador = false;

            while (reader.hasNext()) {
                int tipo = reader.next();

                switch (tipo) {
                    case XMLStreamConstants.START_ELEMENT -> {
                        String nombreEtiqueta = XMLStAXUtilsCursor.obtenerNombreEtiqueta(reader);
                        if ("Patrocinador".equals(nombreEtiqueta)) {
                            nombrePatrocinador = "";
                            String don = XMLStAXUtilsCursor.leerAtributo(reader, "donacion");
                            donacionActual = don != null ? Double.parseDouble(don) : 0.0;
                            esPatrocinador = true;
                        }
                    }
                    case XMLStreamConstants.CHARACTERS -> {
                        if (esPatrocinador) {
                            nombrePatrocinador += XMLStAXUtilsCursor.leerTexto(reader);
                        }
                    }
                    case XMLStreamConstants.END_ELEMENT -> {
                        String nombreEtiqueta = XMLStAXUtilsCursor.obtenerNombreEtiqueta(reader);
                        if ("Patrocinador".equals(nombreEtiqueta)) {
                            esPatrocinador = false;
                            nombrePatrocinador = nombrePatrocinador.trim();
                            mapaDonaciones.merge(nombrePatrocinador, donacionActual, Double::sum);
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new ExcepcionXML("Error al calcular donaciones: " + e.getMessage(), e);
        } finally {
            try {
                if (reader != null) reader.close();
            } catch (XMLStreamException e) {
                // Ignorar cierre
            }
        }

        return mapaDonaciones;
    }



    // SOLO FALTA EXCEPCIONES

     /**
     *
     * @param rutaSalida
     * @return
     * @throws ExcepcionXML
     */
//    public static XMLStreamWriter crearWriterStAX (String rutaSalida) throws ExcepcionXML {
//        try{
//            XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
//            return outputFactory.createXMLStreamWriter(new FileWriter(rutaSalida));
//        } catch (IOException e) {
//            throw new ExcepcionXML("No se pude crear el fichero XML: " + rutaSalida, e);
//        } catch (XMLStreamException e) {
//            throw new ExcepcionXML();
//        } catch (Exception e){
//            throw new ExcepcionXML();
//        }
//    }


    /**
     * Crea un XMLStreamWriter para la ruta indicada (UTF-8)
     * Metodo acabado con ia
     */
    public static XMLStreamWriter crearWriterStAX(String rutaSalida) throws ExcepcionXML {
        try {
            XMLOutputFactory outputFactory = XMLOutputFactory.newFactory();
            // Usar FileOutputStream para poder pasar encoding
            return outputFactory.createXMLStreamWriter(new FileOutputStream(rutaSalida), "UTF-8");
        } catch (IOException e) {
            throw new ExcepcionXML("No se pudo crear el fichero XML: " + rutaSalida, e);
        } catch (XMLStreamException e) {
            throw new ExcepcionXML("Error al crear XMLStreamWriter: " + e.getMessage(), e);
        }
    }

    /**
     * Añade la declaración de cabecera XML al nuevo archivo mediante el XMLStreamWriter
     *
     * @param writer XMLStreamWriter que escribe el documento
     * @throws ExcepcionXML
     */
    public static void ADDDeclaracion(XMLStreamWriter writer) throws ExcepcionXML {
        try{
            writer.writeStartDocument("UTF-8", "1.0");
            writer.writeCharacters("\n");
        } catch (XMLStreamException e){
            throw new ExcepcionXML("Error al escribir la declaración XML.", e);
        }
    }

    // VER LO DE NIVEL
    /**
     * Añade un salto de línea y espacios de indentación al XML.
     *
     * @param writer XMLStreamWriter que escribe el documento
     * @param nivel ?
     * @throws ExcepcionXML
     */
    public static void ADDSaltoLinea(XMLStreamWriter writer, int nivel) throws ExcepcionXML {
        try{
            String indentacion = "\n" + "   ".repeat(nivel);
            writer.writeCharacters(indentacion);
        } catch (XMLStreamException e) {
            throw new ExcepcionXML("Error al añadir saldo de línea o indentación.", e);
        }
    }

    /**
     * Añade un atributo de un elemento XML al nuevo archivo mediante XMLStreamWriter.
     *
     * @param writer XMLStreamWriter que escribe el documento
     * @param nombre nombre del atributo
     * @param valor valor del atributo
     * @throws ExcepcionXML
     */
    public static void ADDAtributo(XMLStreamWriter writer, String nombre, String valor) throws  ExcepcionXML {
        try {
            writer.writeAttribute(nombre, valor);
        } catch (XMLStreamException e) {
            throw new ExcepcionXML("Error al añadir atributo: " + nombre, e);
        }
    }


    /**
     * Añade un elemento sin valor en texto a al XML mediante el XMLStreamWriter
     *
     * @param writer XMLStreamWriter que escribe el documento
     * @param nombre LocalName del elemento
     * @throws ExcepcionXML
     */
    public static void ADDStartElemento(XMLStreamWriter writer, String nombre) throws ExcepcionXML {
        try{
            writer.writeStartElement(nombre);
        }catch (XMLStreamException e){
            throw new ExcepcionXML("Error al crear el elemento: " + nombre, e);
        }
    }

    /**
     * Añade un elemento con un valor en texto a al XML mediante el XMLStreamWriter
     *
     * @param writer XMLStreamWriter que escribe el documento
     * @param nombre nombre del elemento
     * @param valor valor textual del elemento
     * @throws ExcepcionXML
     */
    public static void ADDElemento(XMLStreamWriter writer, String nombre, String valor) throws ExcepcionXML {
        try {
            writer.writeStartElement(nombre);
            writer.writeCharacters(valor);
            writer.writeEndElement();
        }
        catch (XMLStreamException e) {
            throw new ExcepcionXML("Error al crear el elemento: " + nombre, e);
        }
    }

    /**
     * Cierra el elemento actual escribiendo la etiqueta de cierre
     *
     * @param writer XMLStreamWriter que escribe el documento
     * @throws ExcepcionXML
     */
    public static void ADDEndElemento(XMLStreamWriter writer) throws ExcepcionXML {
        try {
            writer.writeEndElement();
        } catch (XMLStreamException e) {
            throw new ExcepcionXML("Error al cerrar elemento.", e);
        }
    }

    /**
     * Crea un elemento vacío en el flujo XML de la forma <nombre/>
     *
     * @param writer XMLStreamWriter que escribe el documento
     * @param nombre nombre de la etiqueta a crear
     * @throws ExcepcionXML
     */
    public static void ADDElementoVacio(XMLStreamWriter writer, String nombre) throws ExcepcionXML {
        try {
            writer.writeEmptyElement(nombre);
        } catch (XMLStreamException e) {
            throw new ExcepcionXML("Error al crear el elemento vacío: " + nombre, e);
        }
    }

    /**
     *
     * @param writer XMLStreamWriter que escribe el documento
     * @throws ExcepcionXML
     */
//    public static void ADDEndDocumento(XMLStreamWriter writer) throws ExcepcionXML {
//        try {
//            writer.writeEndElement();
//        } catch (XMLStreamException e) {
//            throw new ExcepcionXML();
//        }
//    }





}
