// Saúl Fernández Salgado
package logica;

import modelo.Reserva;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import persistenciaDOM.AlojamientosXML;
import utiles.ExcepcionXML;
import utiles.TipoValidacion;



public class gestorAlojamientos {

    private final AlojamientosXML gestorDOM;
    private Document documentoXML;

    public gestorAlojamientos() {
        this.gestorDOM = new AlojamientosXML();
    }

    /**
     * LLama a cargarDocumento de Alojamientos.xml
     *
     * @param rutaXML    String con la ruta del fichero
     * @param validacion Enum con el tipo de validacion (DTD, XSD o ninguna)
     * @throws ExcepcionXML lanzada si hay un error al cargar el documento
     */
    public void cargarDocumentoDOM(String rutaXML, TipoValidacion validacion) throws ExcepcionXML {
        try {
            this.documentoXML = gestorDOM.cargarDocumentoDOM(rutaXML, validacion);
            System.out.println("Documento XML cargado correctamente");
        } catch (ExcepcionXML e) {
            System.err.println("Error al cargar documento XML: " + e.getMessage());
        }
    }


    public void añadirReserva(String IDAlojamiento, Reserva nuevaReserva) {
        try{
        Element elementAlojamiento = gestorDOM.buscarAlojamiento(IDAlojamiento);

        if(elementAlojamiento == null){
            System.err.println("El alojamiento " + IDAlojamiento + " no existe");
            return;
        }
        if(gestorDOM.codigoReservaExiste(nuevaReserva.getcodigo())){
            System.err.println("La reserva " + nuevaReserva.getcodigo() + "  ya existe");
            return;
        }
        if(gestorDOM.alojamientoOcupado(elementAlojamiento)){
            System.err.println("El alojamiento " + IDAlojamiento + "  ya tiene reserva activa");
            return;
        }
//        if(!gestorDOM.validarFecha(elementAlojamiento, nuevaReserva.getFechaInicio())){
//            System.err.println("La fecha tiene que ser posterior a la última reserva");
//            return;
//        }

        gestorDOM.añadirReservas(IDAlojamiento, nuevaReserva);
        gestorDOM.actualizarEstadoAlojamiento(IDAlojamiento);
            System.out.println("Reserva actualizada correctamente");

        }catch(ExcepcionXML e){
            System.err.println("Error al añadir la reserva: " + e.getMessage());
        }

    }

    public void guardarDOM(String rutaGuardado){
        gestorDOM.guardarDOM(rutaGuardado);
    }
}
