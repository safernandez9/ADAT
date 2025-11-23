package persistenciaDOM;

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
import java.util.ArrayList;
import java.util.List;

/**
 * Contiene métodos genéricos y reutilizables para el manejo del DOM y XPath
 */
public class XMLDOMUtils {

    // ===================== CHULETA RÁPIDA DOM (JAVA) ======================

// Insertar un nodo al final
// parent.appendChild(nuevoNodo);

// Insertar un nodo antes que un hermano
// parent.insertBefore(nuevoNodo, nodoReferencia);

// Insertar en una posición concreta
// Node ref = parent.getChildNodes().item(indice);
// parent.insertBefore(nuevoNodo, ref);

// Insertar después de un nodo (no existe método directo → truco)
// parent.insertBefore(nuevoNodo, nodoViejo.getNextSibling());

// Reemplazar un nodo por otro
// parent.replaceChild(nuevoNodo, nodoViejo);

// Eliminar un nodo hijo
// parent.removeChild(nodo);

// Crear elementos y texto
// Element e = doc.createElement("Tag");
// Text t = doc.createTextNode("contenido");
// e.appendChild(t);

// ===============================================================

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
     * Obtiene el texto de una etiqueta hija dentro de un elemento padre
     *
     * @param padre    Padre de la etiqueda de la que quiero sacar texto
     * @param etiqueta Nmobre de la etiqueta
     * @return
     */
    public static String obtenerTexto(Element padre, String etiqueta) {

        NodeList lista = padre.getElementsByTagName(etiqueta);

        if (lista.getLength() == 0) {
            return "";
        }

        return lista.item(0).getTextContent();
    }

    /**
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
     * Añade un atributo a un elemento de un documento XML
     *
     * @param doc    Raiz del Document
     * @param nombre Nombre del atributo
     * @param valor  Valor del atributo
     * @param padre  Elemento al que se le añadirá el atributo
     * @return Atributo creado
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
     * Añadir atributo que será considerado como ID. Se hace como un atributo normal pero añado al acabar
     * el metodo padre.setIdAttributeNode(atributo, true);
     *
     * @param doc     Raiz del Document
     * @param nombre  Nombre del atributo
     * @param valorID Valor del atributo
     * @param padre   Elemento al que se le añadirá el atributo
     * @return
     */
    public static Attr añadirAtributoID(Document doc, String nombre, String valorID, Element padre) {
        try {
            Attr atributoID = doc.createAttribute(nombre);

            atributoID.setValue(valorID);
            padre.setAttributeNode(atributoID);

            // Registrar el atributo como tipo ID, no va a poder haber otro con el mismo nombre
            padre.setIdAttributeNode(atributoID, true);

            return atributoID;
        } catch (DOMException e) {
            throw new ExcepcionXML("Error al añadir atributo ID: " + e.getMessage());
        }
    }

    /**
     * Crea elemento con texto y lo añade como hijo a un padre.
     *
     * @param doc    Raiz del Document
     * @param nombre Nombre del Element que voy a crear
     * @param valor  Valor (En texto) del Element que voy a crear
     * @param padre  Nombre del padre del que colgaré el Element
     * @return
     */
    public static Element addElement(Document doc, String nombre, String valor, Element padre) {

        // Creo elemento en el doc
        Element elemento = doc.createElement(nombre);
        Text texto = doc.createTextNode(valor);                     // CreateTextNode crea un nodo de texto con el valor que le paso

        // Añado hijo al padre primero. Despues añado el texto al hijo.
        padre.appendChild(elemento);
        elemento.appendChild(texto);

        return elemento;
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
     * Elimina un elemento, cojo su padre y desde él elimino el hijo.
     * El resto de nodos se borraran en cascada.
     *
     * @param elemento
     * @return
     */
    public static boolean eliminarElemento(Element elemento) {
        if (elemento != null && elemento.getParentNode() != null) {
            elemento.getParentNode().removeChild(elemento);
            return true;
        }
        return false;
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
     * Busca un elemento por uno de sus atributos.
     * Devuelve el primero que encuentra.
     *
     * @param doc            Raiz del Document
     * @param nombreElemento Nombre del elemento
     * @param nombreAtributo Nombre del atributo
     * @param valorBuscado   Valor del atributo buscado
     * @return Elemento
     */
    public static Element buscarElementoPorAtributo(Document doc, String nombreElemento, String nombreAtributo, String valorBuscado) {
        NodeList lista = doc.getElementsByTagName(nombreElemento);

        for (int i = 0; i < lista.getLength(); i++) {
            Element e = (Element) lista.item(i);
            if (e.hasAttribute(nombreAtributo) && e.getAttribute(nombreAtributo).equals(valorBuscado)) {
                return e;
            }
        }

        return null;
    }

    /**
     * Busca varios elementos en todo el DOC por uno de sus atributos comunes independientemente de como se llamen.
     *
     * @param doc            Raiz del Document
     * @param nombreElemento Nombre del elemento
     * @param nombreAtributo Nombre del atributo
     * @param valorBuscado   Valor del atributo buscado
     * @return Lista de Elementos
     */
    public static List<Element> buscarElementosMultiplesPorAtributo(Document doc, String nombreElemento, String nombreAtributo, String valorBuscado) {
        NodeList lista = doc.getElementsByTagName(nombreElemento);
        List<Element> elementosEncontrados = new ArrayList<>();

        for (int i = 0; i < lista.getLength(); i++) {
            Element e = (Element) lista.item(i);
            if (e.hasAttribute(nombreAtributo) && e.getAttribute(nombreAtributo).equals(valorBuscado)) {
                elementosEncontrados.add(e);
            }
        }

        return elementosEncontrados;
    }

    /**
     * Busca varios elementos hijos directos de un elemento padre por uno de sus atributos comunes independientemente de su tipo.
     *
     * @param padre          Elemento padre desde el que buscar
     * @param nombreAtributo Nombre del atributo
     * @param valorBuscado   Valor del atributo buscado
     * @return Lista de Elementos
     */
    public static List<Element> buscarHijosDirectosPorAtributo(Element padre,  String nombreAtributo, String valorBuscado) {
        NodeList nodos = padre.getChildNodes();
        List<Element> resultado = new ArrayList<>();

        for(int i = 0; i < nodos.getLength(); i++) {
            if(nodos.item(i) instanceof Element) {
                Element e = (Element) nodos.item(i);
                if(e.hasAttribute(nombreAtributo) && e.getAttribute(nombreAtributo).equals(valorBuscado)) {
                    resultado.add(e);
                }
            }
        }

        return resultado;
    }

    /**
     * Busca varios elementos descendientes de un elemento padre por uno de sus atributos comunes independientemente de su tipo.
     *
     * @param padre          Elemento padre desde el que buscar
     * @param nombreAtributo Nombre del atributo
     * @param valorBuscado   Valor del atributo buscado
     * @return Lista de Elementos
     */
    public static List<Element> buscarDescendientesPorAtributo(Element padre, String nombreAtributo, String valorBuscado) {
        List<Element> resultado = new ArrayList<>();
        NodeList nodos = padre.getChildNodes();

        for (int i = 0; i < nodos.getLength(); i++) {
            if (nodos.item(i) instanceof Element) {
                Element e = (Element) nodos.item(i);

                // Comprueba el elemento actual
                if (e.hasAttribute(nombreAtributo) && e.getAttribute(nombreAtributo).equals(valorBuscado)) {
                    resultado.add(e);
                }

                // Llamada recursiva para los hijos de este nodo
                resultado.addAll(buscarDescendientesPorAtributo(e, nombreAtributo, valorBuscado));
            }
        }

        return resultado;
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
    public static NodeList evaluarXPathNodeList(Object contexto, String expresion) {
        return (NodeList) evaluarXPath(contexto, expresion, XPathConstants.NODESET);
    }

    /**
     * Llama a evaluarXPath esperando un Node, castea el Object recibido a Node y
     * lo devuelve. Cuando espero un único nodo.
     *
     * @param contexto
     * @param expresion
     * @return
     */
    public static Node evaluarXPathNode(Object contexto, String expresion) {
        return (Node) evaluarXPath(contexto, expresion, XPathConstants.NODE);
    }

    /**
     * Llama a evaluarXPath esperando un Boolean, castea el Object recibido a Boolean y
     * lo devuelve. Cuando espero un valor true/false.
     *
     * @param contexto
     * @param expresion
     * @return
     */
    public static Boolean evaluarXPathBoolean(Object contexto, String expresion) {
        return (Boolean) evaluarXPath(contexto, expresion, XPathConstants.BOOLEAN);
    }

    /**
     * Llama a evaluarXPath esperando un String, castea el Object recibido a String y
     * lo devuelve. Cuando espero un valor en texto.
     *
     * @param contexto
     * @param expresion
     * @return
     */
    public static String evaluarXPathString(Object contexto, String expresion) {
        return (String) evaluarXPath(contexto, expresion, XPathConstants.STRING);
    }

    /**
     * Llama a evaluarXPath esperando un Double, castea el Object recibido a Double y
     * lo devuelve. Cuando espero un valor numérico.
     *
     * @param contexto
     * @param expresion
     * @return
     */
    public static Double evaluarXPathNumber(Object contexto, String expresion) {
        return (Double) evaluarXPath(contexto, expresion, XPathConstants.NUMBER);
    }

    // CHULETA DE XPATH PARA CORREDORES

    /*

```
        CHULETA COMPLETA DE XPATH PARA CORREDORES
```

=======================================================

1. Selección básica de nodos

---

//velocista
→ Todos los elementos <velocista> en cualquier parte del documento
/fondistas/fondista
→ Todos los <fondista> hijos directos de <fondistas>

2. Seleccionar atributos

---

//velocista/@codigo
→ Obtiene el atributo “codigo” de todos los velocistas
/fondista[@codigo='F03']
→ Selecciona el <fondista> con atributo codigo="F03"

3. Seleccionar nodos por valor de hijo

---

//velocista[nombre='Juan']
→ Velocista cuyo hijo <nombre> sea "Juan"
//fondista[distancia_max > 10]
→ Fondista con distancia_max > 10

4. Selección de un hijo concreto

---

//velocista/nombre
→ El hijo <nombre> de cada <velocista>
//fondista/velocidad_media
→ Hijo <velocidad_media> de cada fondista

5. Funciones XPath útiles

---

count(//velocista)                  → Número total de velocistas
sum(//fondista/distancia_max)       → Suma de todas las distancias máximas
max(//velocista/velocidad_media)    → Velocidad máxima
min(//fondista/distancia_max)       → Distancia mínima

6. Combinación de filtros

---

//velocista[velocidad_media > 25 and equipo='Rojo']
→ Velocistas con velocidad_media > 25 y del equipo "Rojo"
//fondista[distancia_max > 10 or equipo='Azul']
→ Fondistas con distancia máxima > 10 o equipo "Azul"

7. Selección por índice

---

//velocista[1]           → Primer velocista
//fondista[last()]       → Último fondista

8. Obtener texto de un nodo

---

//velocista/nombre/text()
→ Devuelve el texto dentro del <nombre> del velocista
//fondista/distancia_max/text()
→ Devuelve el texto dentro de <distancia_max>

9. Obtener nodos hijos o descendientes

---

//Corredor/*
→ Todos los hijos directos de <Corredor>
//Corredor//puntuacion
→ Todos los <puntuacion> descendientes de <Corredor>, a cualquier nivel

10. Resumen rápido de símbolos

---

/          → hijo directo
//         → cualquier descendiente
@          → atributo
[]         → filtro / condición
text()     → texto dentro del nodo

* ```
       → cualquier hijo
  ```

last()     → último elemento del conjunto
count(), sum(), max(), min() → funciones XPath numéricas

11. Aplicación práctica a tu XML de corredores

---

//velocista[@codigo='V01']/nombre/text()
→ Nombre del velocista con código V01

//fondista[distancia_max>15]/nombre/text()
→ Nombres de fondistas cuya distancia_max > 15

//Corredor[puntuacion/@anio=2023]/nombre/text()
→ Nombres de corredores que tengan una puntuación en el año 2023

//velocista[velocidad_media>25]/@codigo
→ Códigos de velocistas con velocidad_media > 25

//fondista[distancia_max>10]/puntuacion[@anio=2022]/text()
→ Puntos de fondistas con distancia_max > 10 en el año 2022

=======================================================
*/

}
