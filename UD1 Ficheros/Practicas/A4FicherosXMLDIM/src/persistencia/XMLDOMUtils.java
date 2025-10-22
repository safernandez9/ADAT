package persistencia;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

//AQUI VA LO QUE ME SERVIRA PARA OTROS EJERCICIOS

public class XMLDOMUtils {


    public static Document cargarDocumentoXML(String rutaFichero, TipoValidacion validacion){

        try {
            // 1. Crear y configurar la factoria de parsers segun el tipo de validacion (DEBE SER ANTES QUE EL PUNTO 2)
            DocumentBuilderFactory dbf = configurarFactory(validacion);

            // 2 . Crear el parser (DocumentBuilder)
            DocumentBuilder db = dbf.newDocumentBuilder();

            // 3. Establecer un manejador de errores si trabajamos con validacion
            if(validacion != TipoValidacion.NO_VALIDAR){
                db.setErrorHandler(new SimpleErrorHandler());
            }

            // 4. Cargar el documento XML en memoria
            Document documento = db.parse(new File(rutaFichero));

            // Una vez cargado lo normalizo. getDocumentElement obtiene la raiz del documento
            documento.getDocumentElement().normalize();
            return documento;
        }
        catch (ParserConfigurationException e){
            //Error en la confiduracion del parser DOM
            throw new ExcepcionXML("Error de configuracion" + e.getMessage());
        }
        catch (SAXException e){
            //Error en
            throw new ExcepcionXML("Error de configuracion" + e.getMessage());
        }
        catch(IOException e){
            // Error de E/S (archivo no existe
            throw new ExcepcionXML("Error de configuracion" + e.getMessage());
        }

    }

    /**
     * Activa el tipo de validacion sobre el dbf, que creo em este mismo metodo.
     * Activo también configuraciones generales como dbf.setIgnoringElementContentWhitespace(true)
     * En caso de validacion debo poner la linea correspondiente en el XML al DTD o XSD
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

    public static String obtenerTexto(Element padre, String etiqueta) {

        NodeList lista = padre.getElementsByTagName(etiqueta);

        // Busca todas las etiquetas hija dentor del elemento padre y comprueba si se encontró al menos una
        if (lista.getLength() > 0) {
            // Si la encontró devuelve el texto del primer elemento
            return lista.item(0).getTextContent();
        }
        return "";
    }



}
