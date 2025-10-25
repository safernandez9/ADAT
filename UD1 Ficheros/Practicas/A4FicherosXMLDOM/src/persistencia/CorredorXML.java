package persistencia;

/* Responsabilidad de persistencia: Acceso y manipulación directa del Document Object Model (DOM) del fichero
 * corredores.xml. No imprime por consola.
 */

import clases.Corredor;
import clases.Fondista;
import clases.Puntuacion;
import clases.Velocista;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Contiene la lógica específica para la entidad Corredor
 */
public class CorredorXML {

    private static Document documentoXML;

    /**
     * Llama a la creacion del Document con el que trabajará esta clase, que se hará en XMLDOMUtils.
     * Propaga las excepciones, no las maneja aquí.
     * @param rutaXML String con la ruta del archivo xml
     * @param validacion Enum creado por mi con el tipo de validacion
     * @return
     * @throws ExcepcionXML
     */
    public Document cargarDocumentoDOM(String rutaXML, TipoValidacion validacion) throws ExcepcionXML {
        return XMLDOMUtils.cargarDocumentoXML(rutaXML, validacion);
    }

    /**
     * Carga los corredores del Document en una List. Obtiene los nodos corredor del Document,
     * Verifica que sean de tipo Element, los envía a la funcion que los convierte en objetos Corredor
     * y si no recibe null los añade a la List.
     *
     * @param doc Document sobre el que trabajo
     * @return List de Corredores
     */
    public List<Corredor> cargarCorredores(Document doc) {

        List<Corredor> lista = new ArrayList();
        Element raiz = doc.getDocumentElement();
        NodeList nodos = raiz.getChildNodes();      // Devuelve hijos directos

        for (int i = 0; i < nodos.getLength(); i++) {
            //Si es una instancia de nodo elemento, crea un Element corredorElem y almacena en el
            if (nodos.item(i) instanceof Element corredorElem) {
                Corredor corredor = crearCorredor(corredorElem);
                if (corredor != null) {
                    lista.add(corredor);
                }
            }
        }
        return lista;
    }

    /**
     * Crea un objeto corredor a partir de un Element (Nodo) Corredor que recibe por parametros.
     *
     * @param corredorElem
     * @return
     */
    public Corredor crearCorredor(Element corredorElem) {

        // Tener en cuenta que de un XML todo viene en forma de String, por ello habrá datos que parsear.

        // Datos en forma de atributos
        String codigo = corredorElem.getAttribute("codigo");
        int dorsal = Integer.parseInt(corredorElem.getAttribute("dorsal"));
        String equipo = corredorElem.getAttribute("equipo");

        // Datos en forma de texto (Necesito función propia)
        String nombre = XMLDOMUtils.obtenerTexto(corredorElem, "nombre");
        LocalDate fecha = LocalDate.parse(XMLDOMUtils.obtenerTexto(corredorElem, "fecha"));

        Corredor corredor = switch (corredorElem.getTagName()) {
            case "fondista" -> {
                float distancia = Float.parseFloat(XMLDOMUtils.obtenerTexto(corredorElem, "distancia"));
                // Yield funciona como un return en un switch
                yield new Fondista(codigo, dorsal, nombre, fecha, equipo, distancia);
            }
            case "velocista" -> {
                float velocidad = Float.parseFloat(XMLDOMUtils.obtenerTexto(corredorElem, "velocidad"));
                yield new Velocista(codigo, dorsal, nombre, fecha, equipo, velocidad);
            }
            default -> null;
        };

        // Si todo fue bien, añado el historial (Funcion propia que me devuelva una List)
        if (corredor != null) {
            corredor.setHistorial(cargarHistorial(corredorElem));
        }

        return corredor;

    }

    /**
     * Cargo desde un elemento Corredor su historial a una List
     * @param corredorElem
     * @return
     */
    private List<Puntuacion> cargarHistorial(Element corredorElem) {
        List<Puntuacion> historial = new ArrayList();

        // getElementsByTagName devuelve los nodos hijos y descendientes de ellos si los hay con ese nombre.
        // item(0) devuelve el primero que encuentra
        Element historialElem = (Element) corredorElem.getElementsByTagName("historial").item(0);

        if (historialElem != null) {
            NodeList puntuaciones = historialElem.getElementsByTagName("puntuacion");

            // Recorro una NodeList con los nodos <puntuacion>
            for (int i = 0; i < puntuaciones.getLength(); i++) {
                Element punt = (Element) puntuaciones.item(i);
                int anio = Integer.parseInt(punt.getAttribute("anio"));
                float puntos = Float.parseFloat(punt.getTextContent());
                historial.add(new Puntuacion(anio, puntos));
            }
        }
        return historial;
    }

    /**
     * Inserta un nuevo corredor en el Document XML
     * @param corredor
     */
    public void insertarCorredor(Corredor corredor) {

        // Obtener nodo raiz <corredores>
        Element raiz = documentoXML.getDocumentElement();

        // Determinar tipo de corredor que he recibido para ver que escribo en el nodo
        String tipo = corredor instanceof Velocista ? "velocista" : "fondista";

        // Creo nodo principal del corredor, le paso el documento, el nombre del nodo y el nodo padre.
        Element nodoCorredor = XMLDOMUtils.addElement(documentoXML, tipo, raiz);

        //Añadir los atributos: código, dorsal, equipo
        XMLDOMUtils.añadirAtributoID(documentoXML, "codigo", corredor.getCodigo(), nodoCorredor);
        XMLDOMUtils.añadirAtributo(documentoXML, "dorsal", corredor.getDorsal(), nodoCorredor);
    }

    public int obtenerSiguienteDorsal(){
        Element raiz = documentoXML.getDocumentElement();
    }

    public void guardarDocumentoDOM(){

    }

    public Corredor buscarCorredorPorDorsal (int dorsal){
        Element raiz = documentoXML.getDocumentElement();
        NodeList hijos = raiz.getChildNodes();

        for (int i = hijos.getLength()-1; i >= 0; i--) {
            Node nodo = hijos.item(i);

            if(nodo instanceof Element corredorElem){

            }

        }
    }

    /**
     * Elimina un elemento por su id
     * @param codigo
     * @return
     */
    public boolean eliminarCorredorPorCodigo(String codigo){
        Element corredor = XMLDOMUtils.buscarElementoPorID(documentoXML, codigo);
        return XMLDOMUtils.eliminarElemento(corredor);
    }

    public boolean eliminarCorredorPorDorsal(int dorsal){

        Corredor corredorAeliminar = buscarCorredorPorDorsal(dorsal);
        if (corredorAeliminar != null) {
            String codigoAEliminar = corredorAeliminar.getCodigo();
        }
    }


}