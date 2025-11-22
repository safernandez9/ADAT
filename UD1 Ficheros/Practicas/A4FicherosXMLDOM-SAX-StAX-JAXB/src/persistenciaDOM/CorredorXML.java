package persistenciaDOM;

/* Responsabilidad de persistencia: Acceso y manipulación directa del Document Object Model (DOM) del fichero
 * corredores.xml. No imprime por consola.
 */

import clases.Corredor;
import clases.Fondista;
import clases.Puntuacion;
import clases.Velocista;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static persistenciaDOM.XMLDOMUtils.buscarElementoPorAtributo;

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
        documentoXML = XMLDOMUtils.cargarDocumentoXML(rutaXML, validacion);
        return this.documentoXML;
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
    public Corredor crearCorredor(Element corredorElem) throws ExcepcionXML {

        // Tener en cuenta que de un XML todo viene en forma de String, por ello habrá datos que parsear.

        // Datos en forma de atributos
        String codigo = corredorElem.getAttribute("codigo");
        int dorsal = Integer.parseInt(corredorElem.getAttribute("dorsal"));
        String equipo = corredorElem.getAttribute("equipo");

        // Datos en forma de texto (Necesito función propia)
        String nombre = XMLDOMUtils.obtenerTexto(corredorElem, "nombre");
        LocalDate fecha = LocalDate.parse(XMLDOMUtils.obtenerTexto(corredorElem, "fecha_nacimiento"));

        Corredor corredor = switch (corredorElem.getTagName()) {
            case "fondista" -> {
                float distancia = Float.parseFloat(XMLDOMUtils.obtenerTexto(corredorElem, "distancia_max"));
                // Yield funciona como un return en un switch
                yield new Fondista(codigo, dorsal, nombre, fecha, equipo, distancia);
            }
            case "velocista" -> {
                float velocidad = Float.parseFloat(XMLDOMUtils.obtenerTexto(corredorElem, "velocidad_media"));
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
     * Busca un corredor en el document por su ID, lo crea y lo devuelve como objeto
     * @param ID id del Corredor
     * @return
     */
    public Corredor mostrarCorredorPorIdDOM(String ID) throws ExcepcionXML {
        Element elem = XMLDOMUtils.buscarElementoPorID(documentoXML, ID);

        if (elem == null) {
            throw new ExcepcionXML("No existe corredor con el ID " + ID);
        }

        try {
            return crearCorredor(elem);
        } catch (ExcepcionXML ex) {
            throw new ExcepcionXML(ex.getMessage());
        }
    }

    /**
     * Usa el método de XMLDOMUtils obtenerElementoPorAtributo() para buscar un corredor
     * por su dorsal, crearlo si existe y devolverlo
     * @param dorsal Dorsal del corredor buscado
     * @return Corredor buscado
     * @throws ExcepcionXML
     */
    public Corredor mostrarCorredorPorDorsal(int dorsal) throws ExcepcionXML {
        Element e = buscarElementoPorAtributo(documentoXML, "velocista", "dorsal", Integer.toString(dorsal));
        if(e == null){
            e = buscarElementoPorAtributo(documentoXML, "fondista", "dorsal", Integer.toString(dorsal));
        }

        if(e == null){
            throw new ExcepcionXML("No existe el corredor para el dorsal " + dorsal);
        }

        try{
            return crearCorredor(e);
        } catch (ExcepcionXML ex) {
            throw new ExcepcionXML("Error al crear corredor por dorsal: " + ex.getMessage());
        }

    }

    /**
      * Inserta un nuevo corredor en el Document XML(El dorsal es autoIncremental)
      * @param corredor
      */
    public void insertarCorredor(Corredor corredor) {

        // Obtener nodo raiz <corredores>
        Element raiz = documentoXML.getDocumentElement();

        // Obtener siguiente dorsal disponible antes de añadirlo
        String nuevoDorsal = obtenerSiguienteDorsal();

        // Determinar tipo de corredor que he recibido para ver que escribo en el nodo
        String tipo = corredor instanceof Velocista ? "velocista" : "fondista";

        // Creo nodo principal del corredor, le paso el documento, el nombre del nodo y el nodo padre y me quedo con su referencia.
        Element nodoCorredor = XMLDOMUtils.addElement(documentoXML, tipo, raiz);

        //Añadir los atributos: código, dorsal, equipo
        XMLDOMUtils.añadirAtributoID(documentoXML, "codigo", corredor.getCodigo(), nodoCorredor);
        XMLDOMUtils.añadirAtributo(documentoXML, "dorsal", nuevoDorsal, nodoCorredor);
        XMLDOMUtils.añadirAtributo(documentoXML, "equipo", corredor.getEquipo(), nodoCorredor);

        // Añadir los elementos: nombre, fecha
        XMLDOMUtils.addElement(documentoXML, "nombre", corredor.getNombre(), nodoCorredor);
        XMLDOMUtils.addElement(documentoXML, "fecha_nacimiento", corredor.getFechaNacimiento().toString(), nodoCorredor);

        // Añadir el elemento específico según el tipo de corredor
        if (corredor instanceof Velocista velocista) {
            XMLDOMUtils.addElement(documentoXML, "velocidad_media", Float.toString(velocista.getVelocidadMedia()), nodoCorredor);
        } else if (corredor instanceof Fondista fondista) {
            XMLDOMUtils.addElement(documentoXML, "distancia_max", Float.toString(fondista.getDistanciaMax()), nodoCorredor);
        }

        // Añadir el historial

        Element historialElem = XMLDOMUtils.addElement(documentoXML, "historial", nodoCorredor);
        for (Puntuacion p : corredor.getHistorial()) {
            Element puntuacionElem = XMLDOMUtils.addElement(documentoXML, "puntuacion", historialElem);
            XMLDOMUtils.añadirAtributo(documentoXML, "anio", Integer.toString(p.getAnio()), puntuacionElem);
            puntuacionElem.setTextContent(Float.toString(p.getPuntos()));
        }
    }

    /**
     * Obtiene el siguiente dorsal disponible (Mayor dorsal + 1)
     * @return
     */
    public String obtenerSiguienteDorsal(){
        Element raiz = documentoXML.getDocumentElement();
        NodeList nodos = raiz.getChildNodes();
        int maxDorsal = 0;
        for (int i = 0; i < nodos.getLength(); i++) {
            if (nodos.item(i) instanceof Element corredorElem) {
                int dorsal = Integer.parseInt(corredorElem.getAttribute("dorsal"));
                if(dorsal > maxDorsal){
                    maxDorsal = dorsal;
                }
            }
        }
        maxDorsal += 1;
        return Integer.toString(maxDorsal);
    }

    /**
     * Elimina un elemento por su id
     * @param codigo
     * @return
     */
    public boolean eliminarCorredorPorCodigo(String codigo) throws ExcepcionXML {
        Element corredor = XMLDOMUtils.buscarElementoPorID(documentoXML, codigo);
        if(corredor == null){
            throw new ExcepcionXML("Corredor con ID " + codigo + " no encontrado.");
        }
        return XMLDOMUtils.eliminarElemento(corredor);
    }

    /**
     * Permitir agregar o actualizar una puntuación en el historial de un corredor existente
     * (buscado por codigo).
     * o Si el año de la nueva puntuación no existe, se debe agregar un nuevo registro de puntuación.
     * o Si el año ya está presente, se debe actualizar el valor de la puntuación para ese año.
     * o Tras la operación exitosa, debe darse un feedback claro sobre si la puntuación se añadió o se
     * modificó.
     */
    public boolean modificarPuntuacion(String ID, Puntuacion nuevaPuntuacion) throws ExcepcionXML {
        Element corredorElem = XMLDOMUtils.buscarElementoPorID(documentoXML, ID);
        if(corredorElem == null){
            throw new ExcepcionXML("Corredor con ID " + ID + " no existe.");
        }

        // Pongo .item(0) porque aunque solo haya un historial, getElementsByTagName devuelve una NodeList no convertible a Element.
        Element historialElem = (Element) corredorElem.getElementsByTagName("historial").item(0);
        if(historialElem == null){
            throw new ExcepcionXML("El corredor con ID " + ID + " no tiene historial.");
        }

        // Saco el NodeList de puntuaciones del Element historial
        NodeList puntuaciones = historialElem.getElementsByTagName("puntuacion");

        for (int i = 0; i < puntuaciones.getLength(); i++) {
            Element puntuacionElem = (Element) puntuaciones.item(i);
            int anio = Integer.parseInt(puntuacionElem.getAttribute("anio"));
            if(anio == nuevaPuntuacion.getAnio()){
                // Actualizar puntuacion
                puntuacionElem.setTextContent(Float.toString(nuevaPuntuacion.getPuntos()));
                return true; // Indica que se modificó
            }
        }
        // Si no se encontró el año, añadir nueva puntuacion
        Element nuevaPuntuacionElem = XMLDOMUtils.addElement(documentoXML, "puntuacion", historialElem);
        XMLDOMUtils.añadirAtributo(documentoXML, "anio", Integer.toString(nuevaPuntuacion.getAnio()), nuevaPuntuacionElem);
        nuevaPuntuacionElem.setTextContent(Float.toString(nuevaPuntuacion.getPuntos()));
        return true; // Indica que se añadió

    }

    /**
     * Eliminar un único registro de puntuación del historial de un corredor, utilizando su
     * codigo y el anio de la puntuación a borrar como claves.
     *
     * @param ID
     * @param anio
     * @throws ExcepcionXML
     */
    public boolean eliminarPuntuacionDOM(String ID, int anio) throws ExcepcionXML {
        Element corredorElem = XMLDOMUtils.buscarElementoPorID(documentoXML, ID);
        if(corredorElem == null){
            throw new ExcepcionXML("Corredor con ID " + ID + " no existe.");
        }

        Element historialElem = (Element) corredorElem.getElementsByTagName("historial").item(0);
        if(historialElem == null){
            throw new ExcepcionXML("El corredor con ID " + ID + " no tiene historial.");
        }

        NodeList puntuaciones = historialElem.getElementsByTagName("puntuacion");
        for (int i = 0; i < puntuaciones.getLength(); i++) {
            Element puntuacionElem = (Element) puntuaciones.item(i);
            int anioPuntuacion = Integer.parseInt(puntuacionElem.getAttribute("anio"));
            if(anioPuntuacion == anio){
                XMLDOMUtils.eliminarElemento(puntuacionElem);
                return true;
            }
        }

        return false;
    }

    public void guardarDocumentoDOM(String rutaXML) throws ExcepcionXML {
        XMLDOMUtils.guardarDocumentoXML(documentoXML, rutaXML);
    }

}