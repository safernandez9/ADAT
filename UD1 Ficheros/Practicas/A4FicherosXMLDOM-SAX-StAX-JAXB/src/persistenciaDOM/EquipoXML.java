package persistenciaDOM;

import clases.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import utilidades.ExcepcionXML;
import utilidades.TipoValidacion;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class EquipoXML {

    private static Document documentoXML;

    /**
     * Llama a la creacion del Document con el que trabajará esta clase, que se hará en XMLDOMUtils.
     * Propaga las excepciones, no las maneja aquí.
     *
     * @param rutaXML    String con la ruta del archivo xml
     * @param validacion Enum creado por mi con el tipo de validacion
     * @return Document cargado
     * @throws ExcepcionXML Excepcion personalizada para errores con XML
     */
    public Document cargarDocumentoDOM(String rutaXML, TipoValidacion validacion) throws ExcepcionXML {
        documentoXML = XMLDOMUtils.cargarDocumentoXML(rutaXML, validacion);
        return documentoXML;
    }

    /**
     * Carga los equipos del Document en una List. Obtiene los nodos equipo del Document,
     * Verifica que sean de tipo Element, los envía a la funcion que los convierte en objetos Equipo
     * y si no recibe null los añade a la List.
     *
     * @param doc Document sobre el que trabajo
     * @return List de Equipos
     */
    public List<Equipo> cargarEquipos(Document doc) {

        List<Equipo> lista = new ArrayList<>();
        Element raiz = doc.getDocumentElement();
        NodeList nodos = raiz.getChildNodes();      // Devuelve hijos directos

        for (int i = 0; i < nodos.getLength(); i++) {
            //Si es una instancia de nodo elemento, crea un Element corredorElem y almacena en el
            if (nodos.item(i) instanceof Element equipoElem) {
                Equipo equipo = crearEquipo(equipoElem);
                if (equipo != null) {
                    lista.add(equipo);
                }
            }
        }
        return lista;
    }

    /**
     * Crea un objeto equipo a partir de un Element (Nodo) Equipo que recibe por parametros.
     *
     * @param equipoElem Elemento Equipo del que quiero crear el objeto
     * @return Equipo creado
     */
    public Equipo crearEquipo(Element equipoElem) {

        // Comprobar que el elemento no es null
        if (equipoElem == null) {
            return null;
        }
        // Tener en cuenta que de un XML todo viene en forma de String, por ello habrá datos que parsear.

        // Datos en forma de atributos
        String id = equipoElem.getAttribute("id");
        // Datos en forma de texto (Necesito función propia)
        String nombre = XMLDOMUtils.obtenerTexto(equipoElem, "nombre");

        Equipo equipo = new Equipo(id, nombre);

        // Si todo fue bien, añado el historial (Funcion propia que me devuelva una List)
        if (equipo != null) {
            Set<Patrocinador> patrocinadores = cargarPatrocinadores(equipoElem);
            equipo.setPatrocinadores(patrocinadores);
            equipo.setNumPatrocinadores(patrocinadores.size());
        }

        return equipo;

    }

    /**
     * PARA CAMPOS UNICOS CON ID
     * Busca un equipo en el document por su ID, lo crea y lo devuelve como objeto
     * @param ID id del Equipo
     * @return equipo buscado
     */
    public Equipo mostrarEquipoPorIdDOM(String ID) {
        Element elem = XMLDOMUtils.buscarElementoPorID(documentoXML, ID);

        if (elem == null) {
            return null;
        }

        return crearEquipo(elem);
    }

    /**
     * Inserta un nuevo Equipo en el Document XML
     *
     * @param e Equipo a insertar
     */
    public void insertarEquipoDesdeNuevoPatrocinador(Equipo e) {

        // Obtener nodo raiz <corredores>
        Element raiz = documentoXML.getDocumentElement();

        // Creo nodo principal del equipo, le paso el documento, el nombre del nodo y el nodo padre y me quedo con su referencia.
        Element nodoEquipo = XMLDOMUtils.addElement(documentoXML, "equipo", raiz);

        //Añadir los atributos: id
        XMLDOMUtils.añadirAtributoID(documentoXML, "id", e.getIdEquipo(), nodoEquipo);

        // Añadir los elementos: nombre, patrocinadores
        XMLDOMUtils.addElement(documentoXML, "nombre", e.getNombre(), nodoEquipo);
        Element patrocinadoresElem = XMLDOMUtils.addElement(documentoXML, "patrocinadores", nodoEquipo);
        XMLDOMUtils.añadirAtributo(documentoXML, "numPatrocinadores", Integer.toString(e.getNumPatrocinadores()), patrocinadoresElem);

        // Añadir los patrocinadores

        if(e.getPatrocinadores() != null){
            for (Patrocinador p : e.getPatrocinadores()) {
                Element patrocinadorElem = XMLDOMUtils.addElement(documentoXML, "patrocinador", patrocinadoresElem);
                XMLDOMUtils.añadirAtributo(documentoXML, "donacion", Float.toString(p.getDonacion()), patrocinadorElem);
                XMLDOMUtils.añadirAtributo(documentoXML, "fecha_inicio", p.getFechaInicio().toString(), patrocinadorElem);
                patrocinadorElem.setTextContent(p.getNombreEquipoPatrocinaActualizacion());
            }
        }
    }

    /**
     * Actualiza los patrocinadores de un equipo en el Document XML
     *
     * @param e Equipo con los datos actualizados
     */
    public void actualizarPatrocinadoresEquipo(Equipo e) {
        // Buscar el nodo <equipo> correspondiente en documentoXML
        Element elemEquipo = XMLDOMUtils.buscarElementoPorID(documentoXML, e.getIdEquipo());
        if(elemEquipo != null) {
            // Actualizar patrocinadores
            Element patrocinadoresElem = (Element) elemEquipo.getElementsByTagName("patrocinadores").item(0);
            if(patrocinadoresElem != null){
                // Limpiar patrocinadores existentes
                while(patrocinadoresElem.hasChildNodes()){
                    patrocinadoresElem.removeChild(patrocinadoresElem.getFirstChild());
                }

                for(Patrocinador p : e.getPatrocinadores()){
                    Element patrElem = XMLDOMUtils.addElement(documentoXML, "patrocinador", patrocinadoresElem);
                    patrElem.setTextContent(p.getNombre());
                    patrElem.setAttribute("donacion", Float.toString(p.getDonacion()));
                    patrElem.setAttribute("fecha_inicio", p.getFechaInicio().toString());
                }
            }
        }
    }

    /**
     * Cargo desde un elemento Equipo sus Patrocinadores a una List
     *
     * @param equipoElem Elemento Equipo del que quiero cargar los patrocinadores
     * @return List de Patrocinadores
     */
    private Set<Patrocinador> cargarPatrocinadores(Element equipoElem) {

        Set<Patrocinador> patrocinadores = new HashSet<>();


        // getElementsByTagName devuelve los nodos hijos y descendientes de ellos si los hay con ese nombre.
        // item(0) devuelve el primero que encuentra
        Element patrocinadoresElem = (Element) equipoElem.getElementsByTagName("patrocinadores").item(0);


        if (patrocinadoresElem != null) {
            NodeList listaPatrocinadores = patrocinadoresElem.getElementsByTagName("patrocinador");

            // Recorro una NodeList con los nodos <puntuacion>
            for (int i = 0; i < listaPatrocinadores.getLength(); i++) {
                Element patr = (Element) listaPatrocinadores.item(i);
                float donacion = Float.parseFloat(patr.getAttribute("donacion"));
                LocalDate fechaInicio = LocalDate.parse(patr.getAttribute("fecha_inicio"));
                String nombre = XMLDOMUtils.ObtenerTextoElementoActual(patr);
                patrocinadores.add(new Patrocinador(nombre, donacion, fechaInicio));
            }
        }
        return patrocinadores;
    }

    /**
     * Guarda el Document actual en un fichero XML en la ruta indicada
     *
     * @param rutaXML String con la ruta del fichero donde guardar
     * @throws ExcepcionXML Excepcion personalizada para errores con XML
     */
    public void guardarDocumentoDOM(String rutaXML) throws ExcepcionXML {
        XMLDOMUtils.guardarDocumentoXML(documentoXML, rutaXML);
    }
}
