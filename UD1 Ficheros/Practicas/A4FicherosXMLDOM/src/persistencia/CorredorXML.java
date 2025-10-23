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

    public void cargarDocumentoXML(String rutaDocumentoXML, TipoValidacion tipoValidacion) throws Exception{
        this.documentoXML = XMLDOMUtils.cargarDocumentoXML(rutaDocumentoXML, tipoValidacion);
    }

    /**
     * Llama a la creacion del Document pasando como parametros la ruta en String y la validacion en Enum.
     * Propaga las excepciones, no las maneja aquí
     *
     * @param rutaXML
     * @param validacion
     * @return
     * @throws ExcepcionXML
     */
    public Document cargarDocumentoDOM(String rutaXML, TipoValidacion validacion) throws ExcepcionXML {
        return XMLDOMUtils.cargarDocumentoXML(rutaXML, validacion);
    }

    /**
     *
     * @param doc
     * @return
     */
    public List<Corredor> cargarCorredores(Document doc) {
        List<Corredor> lista = new ArrayList();
        Element raiz = doc.getDocumentElement();
        NodeList nodos = raiz.getChildNodes();

        for (int i = 0; i < nodos.getLength(); i++) {
            //Si es una instancia de nodo elemento (buscar google)
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
     * Crea un objeto corredor a partir de un nodo Corredor que recibe de un DOM
     *
     * @param corredorElem
     * @return
     */
    public Corredor crearCorredor(Element corredorElem) {
        String codigo = corredorElem.getAttribute("codigo");
        int dorsal = Integer.parseInt(corredorElem.getAttribute("dorsal"));
        String equipo = corredorElem.getAttribute("equipo");
        //Conseguir texto de la etiqueta nombre
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

        // Si acabo de construir un corredor en el switch
        if (corredor != null) {
            corredor.setHistorial(cargarHistorial(corredorElem));
        }

        return corredor;

    }

    /**
     *
     * @param corredorElem
     * @return
     */
    private List<Puntuacion> cargarHistorial(Element corredorElem) {
        List<Puntuacion> historial = new ArrayList();
        Element historialElem = (Element) corredorElem.getElementsByTagName("historial").item(0);

        if (historialElem != null) {
            NodeList puntuaciones = historialElem.getElementsByTagName("puntuacion");
            for (int i = 0; i < puntuaciones.getLength(); i++) {
                Element punt = (Element)
                        puntuaciones.item(i);
                int anio = Integer.parseInt(punt.getAttribute("anio"));
                float puntos = Float.parseFloat(punt.getTextContent());
                historial.add(new Puntuacion(anio, puntos));
            }
        }
        return historial;
    }

    public void insertarCorredor(Corredor corredor) {

        // Obtener nodo raiz <corredores>
        Element raiz = documentoXML.getDocumentElement():

        // Determinar tipo de corredor
        String tipo = corredor instanceof Velocista ? "velocista" : "fondista";

        // Creo nodo principal del corredor
        Element nodoCorredor = XMLDOMUtils.addElement(documentoXML, tipo, raiz);

        //Añadir los atributos: código, dorsal, equipo
        XMLDOMUtils.añadirAtributoID(documentoXML );
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