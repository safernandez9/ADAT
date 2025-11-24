// Saúl Fernández Salgado
package persistenciaDOM;

import org.w3c.dom.*;
import org.xml.sax.SAXException;
import utiles.ExcepcionXML;
import utiles.SimpleErrorHandler;
import utiles.TipoValidacion;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class XMLDOMUtils {


    /**
     * Siempre es el mismo método. Carga un XML en el Document. LLama a ConfigurarFactory.
     *
     * @param rutaFichero Ruta del XML
     * @param validacion  Tipo validacion, Enum
     * @return Document Documento generado a partir del XML
     */
    public static Document cargarDocumentoXML(String rutaFichero, TipoValidacion validacion) throws ExcepcionXML {

        try {
            // 1. Crear y configurar la factoria de parsers segun el tipo de validacion (DEBE SER ANTES QUE EL PUNTO 2)
            DocumentBuilderFactory dbf = configurarFactory(validacion);

            // 2 . Crear el parser (DocumentBuilder)
            DocumentBuilder db = dbf.newDocumentBuilder();

            // 3. Establecer un manejador de errores si trabajamos con validacion
            if (validacion != TipoValidacion.NO_VALIDAR) {
                db.setErrorHandler(new SimpleErrorHandler());
            }

            // 4. Cargar el documento XML en memoria. Adquirir la raiz del Documetno y normalizarlo
            // (Para evitar problemas de espacios y saltos de linea).
            Document documento = db.parse(new File(rutaFichero));
            documento.getDocumentElement().normalize();
            return documento;

        } catch (ParserConfigurationException e) {
            //Error en la confiduracion del parser DOM
            throw new ExcepcionXML("Error de configuracion" + e.getMessage());
        } catch (SAXException e) {
            //Error en
            throw new ExcepcionXML("Error de configuracion" + e.getMessage());
        } catch (IOException e) {
            // Error de E/S (archivo no existe
            throw new ExcepcionXML("Error de configuracion" + e.getMessage());
        }

    }

    /**
     * Activa el tipo de validacion sobre el dbf, que creo en este mismo metodo.
     * Activo también configuraciones generales.
     * En caso de validacion debo poner la linea correspondiente en el XML al DTD o XSD
     *
     * @param validacion
     * @return DocumentBuildeFactory creada y configurada
     */
    private static DocumentBuilderFactory configurarFactory(TipoValidacion validacion) {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        switch (validacion) {

            case DTD -> {
                //Activar Validacion y evitar que cuente los enter para la ella.
                dbf.setValidating(true);
                dbf.setIgnoringElementContentWhitespace(true);
            }
            case XSD -> {
                //Activar el NameSpace, ignorar Whitespaces y setAttribute del espacio de nombres?
                dbf.setNamespaceAware(true);
                dbf.setIgnoringElementContentWhitespace(true);

                dbf.setAttribute("http:/java.sun.com/xml/jaxp/properties/schemaLanguage", "http://www.w3.org/2001/XMLSchema");
            }
            case NO_VALIDAR -> dbf.setValidating(false);

        }

        return dbf;
    }

    /**
     *  Guarda documento en el DOM
     *
     * @param doc         Raiz del Document
     * @param rutaDestino
     */
    public static void guardarDocumentoXML(Document doc, String rutaDestino) {
        try {
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer();

            // Configuraciones de salida. No obligatoria. Probar primero a quitarla en el examen.
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

            // Evitar espacios excesivos
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new FileWriter(rutaDestino));
            transformer.transform(source, result);

        } catch (TransformerException | IOException e) {
            throw new ExcepcionXML("Error al guardar el documento XML: " + e.getMessage());
        }
    }

    /**
     * Busca un elemento DOM por su atributo ID. Requiere que el documento haya sido
     * cargado con validacion DTD/XSD para que el parser haya recibido el atributo como ID
     *
     * @param doc     Raiz del Document
     * @param idValue id del elemento que busco como String
     * @return null si no existe, El elemento si existe
     */
    public static Element buscarElementoPorID(Document doc, String idValue) {
        return doc.getElementById(idValue);
    }

    /**
     * Obtienen el nombre de un atributo
     * @param name
     * @param elemento
     * @return
     */
    public static String obtenerNombreAtributo(String name, Element elemento) {
        return elemento.getAttribute(name);
    }

    /**
     * Añade un atributo a un elemento padre
     * @param doc
     * @param nombre
     * @param valor
     * @param padre
     * @return
     */
    public static Attr añadirAtributo(Document doc, String nombre, String valor, Element padre) {

        // Si no devolviera el atributo podría usar el siguiente metodo:
        // padre.setAttribute(nombre, valor);

        Attr atributo = doc.createAttribute(nombre);        //CreateAttribute crea el atributo con el nombre que le paso
        atributo.setValue(valor);                           // SetValue le asigna el valor que le paso
        padre.setAttributeNode(atributo);
        return atributo;
    }

    /**
     * Crea elemento vacio y lo añade como hijo al padre.
     *
     * @param doc    Raiz del Document
     * @param nombre Nombre del Element que voy a crear
     * @param padre  Nombre del padre del que colgaré el Element
     * @return
     */
    public static Element addElement(Document doc, String nombre, Element padre) {

        Element elemento = doc.createElement(nombre);   // createElement crea el Element con el nombre que le paso
        padre.appendChild(elemento);                    // appendChild añade el elemento como hijo del padre
        return elemento;
    }

    /**
     * Modifica un atributo de un elemento. Si no existe lo crea.
     *
     * @param elemento
     * @param nombre
     * @param valor    Es Object porque no se que tipo de dato recibiré
     */
    public static void modificarAtributo(Element elemento, String nombre, Object valor) {

        // Conviverto el tipo de dato recibido a String
        String valorStr = String.valueOf(valor);
        // setAttribute si el atributo no existe lo crea, si existe lo sobreescribe
        elemento.setAttribute(nombre, valorStr);


    }

    // EVALUAR XPATH

    /**
     * Evalua y aplica una expresión XPath. Devuelve un Object de manera genérica
     *
     * @param contexto          Nodo desde el que busca
     * @param expresion         Expresión XPath
     * @param resultadoEsperado Tipo de objeto que espero recibir de mi XPath
     * @return
     */
    public static Object evaluarXPath(Object contexto, String expresion, QName resultadoEsperado) throws ExcepcionXML {
        try {
            // Inicializo y compilo el XPath
            XPath xpath = XPathFactory.newInstance().newXPath();

            return xpath.evaluate(expresion, contexto, resultadoEsperado);
        } catch (XPathExpressionException e) {
            throw new ExcepcionXML("Error al evaluar la expresion xpath: " + e.getMessage());
        }
    }

    /**
     * Llama a evaluarXPath esperando un NodeList, castea el Object recibido a NodeList y
     * lo devuelve. Cuando espero un conjunto de nodos.
     *
     * @param contexto
     * @param expresion
     * @return
     */
    public static NodeList evaluarXPathNodeList(Object contexto, String expresion) throws ExcepcionXML {
        return (NodeList) evaluarXPath(contexto, expresion, XPathConstants.NODESET);
    }


}
