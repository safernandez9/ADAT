package logica;

/**
 * La capa Lógica:
 * Responsabilidad: Gestionar la lógica de feedback
 * al usuario, y coordinar la persistencia. Es como el director de orquesta de la aplicación.
 * Es el único que "habla" con la clase principal (el main) y es el único que "habla" con la
 * capa de persistencia (CorredorXML). Hay impresión de datos.
 */

import clases.Corredor;
import clases.Fondista;
import clases.Puntuacion;
import clases.Velocista;
import jakarta.xml.bind.JAXBException;
import persistenciaJAXB.XMLJAXBUtils;
import persistenciaJAXB.clasesJAXB.CorredorJAXB;
import persistenciaJAXB.clasesJAXB.CorredoresJAXB;
import persistenciaJAXB.clasesJAXB.EquipoJAXB;
import persistenciaJAXB.clasesJAXB.EquiposJAXB;
import org.w3c.dom.Document;
import persistenciaDOM.CorredorXML;
import persistenciaDOM.ExcepcionXML;
import persistenciaDOM.TipoValidacion;
import persistenciaSAX.CorredoresSAX;
import persistenciaStAX.modoCursor.CorredoresStAXCursor;
import persistenciaStAX.modoCursor.XMLStAXUtilsCursor;
import persistenciaStAX.modoEventos.CorredoresStAXEventos;
import persistenciaStAX.modoEventos.XMLStAXUtilsEventos;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamReader;
import java.time.LocalDate;
import java.util.List;


public class GestorCorredores {


    private final CorredorXML gestorDOM;
    private Document documentoXML;
    private final CorredoresSAX gestorSAX;
    private final CorredoresStAXCursor gestorStAXCursor;
    private final CorredoresStAXEventos gestorStAXEventos;


    /**
     * El constructor inicializa los gestores para las diferentes metodologías
     * creando uno nuevo por cada una, DOM, SAX, StAXCursor y StAXEventos
     */
    public GestorCorredores() {
        this.gestorDOM = new CorredorXML();
        this.gestorSAX = new CorredoresSAX();
        this.gestorStAXCursor = new CorredoresStAXCursor();
        this.gestorStAXEventos = new CorredoresStAXEventos();
    }

    // DOM METODOS

    /**
     * LLama a cargarDocumento de CorredorXML
     *
     * @param rutaXML    String con la ruta del fichero
     * @param validacion Enum con el tipo de validacion (DTD, XSD o ninguna)
     * @throws ExcepcionXML
     */
    public void cargarDocumentoDOM(String rutaXML, TipoValidacion validacion) throws ExcepcionXML {
        try {
            this.documentoXML = gestorDOM.cargarDocumentoDOM(rutaXML, validacion);
            System.out.println("Documento XML cargado correctamente");
        } catch (ExcepcionXML e) {
            System.err.println("Error al cargar documento XML: " + e.getMessage());
        }
    }

    /**
     * Función que recibe de CorredorXML un List de Corredores y la muestra en función de su método toString()
     */
    public void listarCorredoresDOM(){
        try{
            List<Corredor> listaCorredores = gestorDOM.cargarCorredores(documentoXML);
            for(Corredor c : listaCorredores){
                System.out.println(c);
            }
        }catch(ExcepcionXML ex){
            System.err.println(ex.getMessage() + " " + ex.getCause());
        }
    }

    /**
     * Muestra un corredor por su ID
     * @param ID
     */
    public void mostrarCorredorPorIDDOM(String ID){
        try{
            Corredor c = gestorDOM.mostrarCorredorPorIdDOM(ID);
            System.out.println(c.toString());
        }catch(ExcepcionXML ex){
            System.err.println("Error al mostrar corredor por ID: " + ex.getMessage());
        }
    }

    /**
     * Muestra un corredor por su dorsal
     * @param dorsal
     */
    public void mostrarCorredorPorDorsalDOM(int dorsal){
        try{
            Corredor c = gestorDOM.mostrarCorredorPorDorsal(dorsal);
            System.out.println(c.toString());
        }catch(ExcepcionXML ex){
            System.err.println("Error al mostrar corredor por dorsal: " + ex.getMessage());
        }
    }

    /**
     * Añade un nuevo corredor al documento XML
     */
    public void añadirNuevoCorredorDOM(){

        // Creo 2 corredores para testear, 1 velocista y un fondista

        List <Puntuacion> historial = List.of(
            new Puntuacion(2022, 150),
            new Puntuacion(2023, 200)
        );

        Corredor nuevoCorredor = new Fondista("C08", "Nuevo Corredor", LocalDate.of(1992,7,10), "Equipo H", historial, (float) 10000);
        Corredor nuevoCorredor2 = new Velocista("C09", "Nuevo Velocista", LocalDate.of(1996,8,25), "Equipo I", historial, (float) 10.5);

        try{
            gestorDOM.insertarCorredor(nuevoCorredor);
            gestorDOM.insertarCorredor(nuevoCorredor2);
            System.out.println("Corredor añadido correctamente.");
        }catch(ExcepcionXML ex){
            System.err.println("Error al añadir nuevo corredor: " + ex.getMessage());
        }
    }

    public void eliminarCorredorPorIDDOM(String ID){
        try{
            gestorDOM.eliminarCorredorPorCodigo(ID);
            System.out.println("Corredor con ID " + ID + " eliminado correctamente.");
        }catch(ExcepcionXML ex){
            System.err.println("Error al eliminar corredor por ID: " + ex.getMessage());
        }
    }

    public void añadirOModificarPuntuacionDOM(String ID, Puntuacion nuevaPuntuacion){
        try{
            if(gestorDOM.modificarPuntuacion(ID, nuevaPuntuacion)) {
                System.out.println("Puntuación añadida/modificada correctamente para el corredor con ID " + ID);
            }
        }catch(ExcepcionXML ex){
            System.err.println("Error al añadir/modificar puntuación: " + ex.getMessage());
        }
    }






    /**
     * Este método solo sirve para testear la carga.
     * LLama a cargarDocumento de CorredorSAX
     *
     * @param rutaXML    String con la ruta del fichero
     * @param validacion Enum con el tipo de validación
     * @throws ExcepcionXML
     */
    public void cargarDocumentoSAX(String rutaXML, TipoValidacion validacion) throws ExcepcionXML {
        try {
            gestorSAX.cargarCorredores(rutaXML, validacion);
        } catch (ExcepcionXML e) {
            System.err.println("Error al cargar documento XML: " + e.getMessage());
            ;
        }
    }

    // LOS SIGUIENTES MÉTODOS ASUMEN Y REQUIEREN QUE PRIMERO SE HAYA REALIZADO EL MÉTODO cargarDocumento()


    /**
     * Función que recibe de CorredorXML un List de Corredores y la muestra en función de su método toString()
     *
     * @param ruta
     * @param validacion
     */
    public void mostrarCorredoresSAX(String ruta, TipoValidacion validacion) {
        try {
            List<Corredor> lista = gestorSAX.cargarCorredores(ruta, validacion);
            for (Corredor c : lista) {
                System.out.println(c);
            }
        } catch (ExcepcionXML e) {
            System.out.println(e.getMessage());
        }

    }

    /**
     *
     * @param ruta
     * @param validacion
     */
    public void mostrarCorredoresStAXCursor(String ruta, TipoValidacion validacion) {
        try {
            XMLStreamReader reader = XMLStAXUtilsCursor.cargarDocumentoStAXCursor(ruta, validacion);
            List<Corredor> lista = gestorStAXCursor.leerCorredores(reader);

            System.out.println("Lista de corredores (StAX Cursor");
            for (Corredor c : lista) {
                System.out.println(c);
            }

        } catch (ExcepcionXML e) {
            System.err.println("Error al leer corredores ocn StAX Cursor: " + e.getMessage());
        }
    }

    /**
     *
     * @param ruta
     * @param validacion
     */
    public void mostrarCorredoresStAXEventos(String ruta, TipoValidacion validacion) {
        try {
            XMLEventReader reader = XMLStAXUtilsEventos.cargarDocumentoStAXEventos(ruta, validacion);
            List<Corredor> lista = gestorStAXEventos.leerCorredores(reader);

            System.out.println("Lista de corredores (StAX Evento");
            for (Corredor c : lista) {
                System.out.println(c);
            }

        } catch (ExcepcionXML e) {
            System.err.println("Error al leer corredores con StAX Eventos: " + e.getMessage());
        }
    }

    /**
     * Muestra los Corredores sacados de un fichero por pantalla.
     *
     * @param ruta
     */
//    public void mostrarCorredoresJAXB(String ruta){
//        try {
//            CorredoresJAXB corredores = new CorredoresJAXB();
//            corredores.leerCorredores(ruta);
//            for (CorredorJAXB c : corredores.getCorredores()) {
//                System.out.println(c);
//            }
//        } catch (ExcepcionXML e) {
//            System.err.println("Error al leer: " + ruta + " con JAXB. " + e.getMessage());
//        }
//    }




}
