// Saúl Fernández Salgado
package persistenciaDOM;

import modelo.Alojamiento;
import modelo.Reserva;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import utiles.ExcepcionXML;
import utiles.TipoValidacion;

import java.time.LocalDate;


public class AlojamientosXML {

    private Document documentoXML;


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
        return documentoXML;
    }

    /**
     * Busca un alojamiento por su ID
     * @param ID
     * @return
     */
    public Element buscarAlojamiento(String ID){
        return XMLDOMUtils.buscarElementoPorID(documentoXML, ID);
    }

    /**
     * Añade una reserva al alojamiento con ID id
     * @param ID
     * @param reserva
     */
    public void añadirReservas(String ID, Reserva reserva) {

        Element alojamientoElement = XMLDOMUtils.buscarElementoPorID(documentoXML, ID);
        Element reservasElem = (Element) alojamientoElement.getElementsByTagName("Reservas").item(0);
        if(reservasElem != null){
            Element nodoReserva = XMLDOMUtils.addElement(documentoXML, "Reserva", reservasElem);
            crearReserva(reserva, nodoReserva, reservasElem);
        }
        else{
            reservasElem = XMLDOMUtils.addElement(documentoXML, "Reservas", alojamientoElement);
            Element nodoReserva = XMLDOMUtils.addElement(documentoXML, "Reserva", reservasElem);
            crearReserva(reserva, nodoReserva, reservasElem);
        }



    }

    /**
     * Crea el elemento reserva con los valores del objeto recibido
     * @param reserva
     * @param nodoReserva
     * @param padre
     * @return
     */
    public Element crearReserva(Reserva reserva, Element nodoReserva, Element padre) {

        //Añadir los atributos
        XMLDOMUtils.añadirAtributo(documentoXML, "codigo", reserva.getcodigo(), nodoReserva);

        // Añado Elementos
        Element usuarioElem = XMLDOMUtils.addElement(documentoXML, "Usuario", nodoReserva);
        XMLDOMUtils.añadirAtributo(documentoXML, "dni", reserva.getUsuario().getDni(), usuarioElem);
        usuarioElem.setTextContent(reserva.getUsuario().getNombreApellidos());

        Element fechaInicioElement = XMLDOMUtils.addElement(documentoXML, "FechaInicio", nodoReserva);
        fechaInicioElement.setTextContent(reserva.getFechaInicio().toString());

        if(reserva.getFechaFin() != null && reserva.getFechaFin().isAfter(reserva.getFechaInicio())) {
            Element fechaFin = XMLDOMUtils.addElement(documentoXML, "FechaFin", nodoReserva);
            fechaFin.setTextContent(reserva.getFechaFin().toString());
        }

        return nodoReserva;
    }

    /**
     * Comprueba si el codigo de la reserva ya existe
     *
     * @param codigoReserva
     * @return
     */
    public boolean codigoReservaExiste(String codigoReserva) throws ExcepcionXML {

            NodeList reservas = XMLDOMUtils.evaluarXPathNodeList(documentoXML, "//Reserva");

            for (int i = 0; i < reservas.getLength(); i++) {
                Element reserva = (Element) reservas.item(i);
                if (reserva.getAttribute("codigo").equals(codigoReserva)) {
                    return true;
                }
            }
            return false;

    }

    /**
     * Comprueba el estado de un alojamiento
     * @param alojamiento alojamiento en el que buscar
     * @return
     * @throws ExcepcionXML Si obtiene algo que no sea ocupada o disponible
     */
    public boolean alojamientoOcupado(Element alojamiento) throws ExcepcionXML {
        String ocupado = XMLDOMUtils.obtenerNombreAtributo("estado", alojamiento);
        ocupado = ocupado.trim().toLowerCase();

        if(ocupado.equals("ocupada")) {
            return true;
        }
        else if(ocupado.equals("disponible")) {
            return false;
        }
        else{
            throw new ExcepcionXML("Valor no esperado en el atributo 'estado'");
        }
    }

    /**
     * guarda el dom
     * @param rutaGuardado
     * @throws ExcepcionXML
     */
    public void guardarDOM (String rutaGuardado) throws ExcepcionXML {
        XMLDOMUtils.guardarDocumentoXML(documentoXML, rutaGuardado);
    }

    /**
     * Actualiza el estado del alojamiento
     * @param idAlojamiento
     */
    public void actualizarEstadoAlojamiento(String idAlojamiento) {
        Element alojamiento = buscarAlojamiento(idAlojamiento);
        XMLDOMUtils.modificarAtributo(alojamiento, "estado", "ocupada");

    }

    /**
     * Valida que la fecha inicial sea anterior a la final del otro elemento
     * @param alojamiento
     * @param fechaInicio
     * @return
     */
    public boolean validarFecha(Element alojamiento, LocalDate fechaInicio) {
        NodeList reservas =  XMLDOMUtils.evaluarXPathNodeList(alojamiento, "//Reserva");
        if(fechaInicio.isBefore(LocalDate.parse(reservas.item(reservas.getLength()).toString())) && reservas.item(reservas.getLength())!= null){
            return false;
        }
        return true;

    }
}
