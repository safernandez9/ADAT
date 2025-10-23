package persistencia;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

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

/**
 *  Contiene métodos genéricos y reutilizables para el manejo del DOM y XPath
 */
public class XMLDOMUtils {


    /**
     *
     * @param rutaFichero
     * @param validacion
     * @return
     */
    public static Document cargarDocumentoXML(String rutaFichero, TipoValidacion validacion) {

        try {
            // 1. Crear y configurar la factoria de parsers segun el tipo de validacion (DEBE SER ANTES QUE EL PUNTO 2)
            DocumentBuilderFactory dbf = configurarFactory(validacion);

            // 2 . Crear el parser (DocumentBuilder)
            DocumentBuilder db = dbf.newDocumentBuilder();

            // 3. Establecer un manejador de errores si trabajamos con validacion
            if (validacion != TipoValidacion.NO_VALIDAR) {
                db.setErrorHandler(new SimpleErrorHandler());
            }

            // 4. Cargar el documento XML en memoria
            Document documento = db.parse(new File(rutaFichero));

            // Una vez cargado lo normalizo. getDocumentElement obtiene la raiz del documento
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
     * Activa el tipo de validacion sobre el dbf, que creo em este mismo metodo.
     * Activo también configuraciones generales como dbf.setIgnoringElementContentWhitespace(true)
     * En caso de validacion debo poner la linea correspondiente en el XML al DTD o XSD
     *
     * @param validacion
     * @return
     */
    private static DocumentBuilderFactory configurarFactory(TipoValidacion validacion) {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        switch (validacion) {
            case DTD -> {
                dbf.setValidating(true);
                //Evitar que cuente los enter para la validacion
                dbf.setIgnoringElementContentWhitespace(true);
            }
            case XSD -> {
                // En los apuntes hay una forma mas moderna de hacerlo, pero mejor usar esta
                dbf.setNamespaceAware(true);
                dbf.setIgnoringElementContentWhitespace(true);

                dbf.setAttribute("http:/java.sun.com/xml/jaxp/properties/schemaLanguage", "http://www.w3.org/2001/XMLSchema");
            }
            case NO_VALIDAR -> dbf.setValidating(false);

        }

        return dbf;
    }

    /**
     *
     * @param padre
     * @param etiqueta
     * @return
     */
    public static String obtenerTexto(Element padre, String etiqueta) {

        NodeList lista = padre.getElementsByTagName(etiqueta);

        // Busca todas las etiquetas hija dentor del elemento padre y comprueba si se encontró al menos una
        if (lista.getLength() > 0) {
            // Si la encontró devuelve el texto del primer elemento
            return lista.item(0).getTextContent();
        }
        return "";
    }

    /**
     *
     * @param doc Raiz del Document
     * @param rutaDestino
     */
    public static void guardarDocumentoXML(Document doc, String rutaDestino) {
        try {
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer();

            // Evitar espacios excesivos
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("(http://xml.apache.org/xslt) indent-amount", "4");

            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new FileWriter(rutaDestino));
            transformer.transform(source, result);

        } catch (TransformerException | IOException e) {
            throw new ExcepcionXML("Error al guardar el documento XML: " + e.getMessage());
        }
    }

    /**
     * Añade un atributo a un elemento de un documento XML
     *
     * @param doc Raiz del Document
     * @param nombre
     * @param valor
     * @param padre
     * @return
     */
    public static Attr añadirAtributo(Document doc, String nombre, String valor, Element padre) {
        Attr atributo = doc.createAttribute(nombre);
        atributo.setValue(valor);
        padre.setAttributeNode(atributo);
        return atributo;
    }

    /**
     * Añadir atributo que será considerado como ID. De esta manera los atributos con el mismo
     * nombre no podrán tener mismo valor?
     *
     * @param doc Raiz del Document
     * @param nombre
     * @param valorID
     * @param padre
     * @return
     */
    public static Attr añadirAtributoID(Document doc, String nombre, String valorID, Element padre) {
        Attr atributoID = doc.createAttribute(nombre);
        atributoID.setValue(valorID);
        padre.setAttributeNode(atributoID);

        // Registrar el atributo como tipo ID, no va a poder haber otro con el mismo nombre
        padre.setIdAttributeNode(atributoID, true);

        return atributoID;
    }

    /**
     * Crea elemento con texto y lo añade como hijo a un padre.
     *
     * @param doc Raiz del Document
     * @param nombre
     * @param valor
     * @param padre
     * @return
     */
    public static Element addElement(Document doc, String nombre, String valor, Element padre) {

        // Creo elemento en el doc
        Element elemento = doc.createElement(nombre);
        // Creo un nodo de texto con el valor del elemento
        Text texto = doc.createTextNode(valor);
        // Añado hijo al padre
        padre.appendChild(elemento);
        // Doy valor al hijo
        elemento.appendChild(texto);

        return elemento;
    }

    /**
     * Crea elemento vacio y lo añade como hijo al padre.
     * (Recordar que vacío = sin texto, no sin atributos)
     *
     * @param doc Raiz del Document
     * @param nombre
     * @param padre
     * @return
     */
    public static Element addElement(Document doc, String nombre, Element padre) {
        Element elemento = doc.createElement(nombre);
        padre.appendChild(elemento);
        return elemento;
    }

    /**
     * Elimina un elemento, cojo su padre y desde el elimino su hijo (El elemento que he pasado)
     * @param elemento
     * @return
     */
    public static boolean eliminarElemento(Element elemento){
        if(elemento != null && elemento.getParentNode() != null){
            elemento.getParentNode().removeChild(elemento);
            return true;
        }
        return false;
    }

    /**
     * Modifica un elemento
     * @param elemento
     * @param nombre
     * @param valor Es Object porque no se que tipo de dato recibiré
     */
    public static void modificarElemento(Element elemento, String nombre, Object valor){

        // Conviverto el tipo de dato recibido a String
        String valorStr = String.valueOf(valor);
        // setAttribute si el atributo no existe lo crea
        elemento.setAttribute(nombre, valorStr);


    }

    /**
     * Busca un elemento DOM por su atributo ID. Requiere que el documento haya sido
     * cargado con validacion DTD/XSD para que el parser haya recibido el atributo como ID
     * @param doc Raiz del Document
     * @param idValue id del elemento que busco
     * @return null si no existe, El elemento si existe
     */
    public static Element buscarElementoPorID(Document doc, String idValue){
        return doc.getElementById(idValue);
    }


    // EVALUAR XPATH

    /**
     * Evalua y aplica una expresión XPath. Devuelve un Object de manera genérica
     * @param contexto Nodo desde el que busca
     * @param expresion Expresión XPath
     * @param resultadoEsperado Tipo de objeto que espero recibir de mi XPath
     * @return
     */
    public static Object evaluarXPath(Object contexto, String expresion, QName resultadoEsperado) throws ExcepcionXML {
        try{
            // Inicializo y compilo el XPath
            XPath xpath = XPathFactory.newInstance().newXPath();

            return xpath.evaluate(expresion, contexto, resultadoEsperado);
        } catch (XPathExpressionException e) {
            throw new ExcepcionXML("Error al evaluar la expresion xpath: " + e.getMessage());
        }
    }

    /**
     * Llama a evaluarXPath esperando un NodeList, castea el Object recibido a NodeList y
     * lo devuelve.
     * @param contexto
     * @param expresion
     * @return
     */
    public static NodeList evaluarXPathNodeList(Object contexto, String expresion){
        return (NodeList) evaluarXPath(contexto, expresion, XPathConstants.NODESET);
    }

    /**
     * Llama a evaluarXPath esperando un Node, castea el Object recibido a Node y
     * lo devuelve.
     * @param contexto
     * @param expresion
     * @return
     */
    public static Node evaluarXPathNode(Object contexto, String expresion){
        return (Node) evaluarXPath(contexto, expresion, XPathConstants.NODE);
    }

    /**
     * Llama a evaluarXPath esperando un Boolean, castea el Object recibido a Boolean y
     * lo devuelve.
     * @param contexto
     * @param expresion
     * @return
     */
    public static Boolean evaluarXPathBoolean(Object contexto, String expresion){
        return (Boolean) evaluarXPath(contexto, expresion, XPathConstants.BOOLEAN);
    }

    /**
     * Llama a evaluarXPath esperando un String, castea el Object recibido a String y
     * lo devuelve.
     * @param contexto
     * @param expresion
     * @return
     */
    public static String evaluarXPathString(Object contexto, String expresion){
        return (String) evaluarXPath(contexto, expresion, XPathConstants.STRING);
    }

    /**
     * Llama a evaluarXPath esperando un Double, castea el Object recibido a Double y
     * lo devuelve.
     * @param contexto
     * @param expresion
     * @return
     */
    public static Double evaluarXPathNumber(Object contexto, String expresion){
        return (Double) evaluarXPath(contexto, expresion, XPathConstants.NUMBER);
    }



}
