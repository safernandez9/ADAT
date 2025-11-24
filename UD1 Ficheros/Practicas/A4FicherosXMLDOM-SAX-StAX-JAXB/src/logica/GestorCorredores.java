package logica;

/*
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
    private String rutaGuardado = "ArchivosXMLDTD/Corredores_Modificado.xml";

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

    /**
     * Función que recibe de CorredorXML un List de Corredores y la muestra en función de su método toString()
     */
    public void listarCorredoresDOM() {
        try {
            List<Corredor> listaCorredores = gestorDOM.cargarCorredores(documentoXML);
            for (Corredor c : listaCorredores) {
                System.out.println(c);
            }
        } catch (ExcepcionXML ex) {
            System.err.println(ex.getMessage() + " " + ex.getCause());
        }
    }

    /**
     * Muestra un corredor por su ID
     *
     * @param ID id del corredor a mostrar
     */
    public void mostrarCorredorPorIDDOM(String ID) {
        try {
            Corredor c = gestorDOM.mostrarCorredorPorIdDOM(ID);
            if (c != null) {
                System.out.println(c.toString());
            }
            else {
                System.err.println("No existe el corredor con el id: " + ID);
            }

        } catch (ExcepcionXML ex) {
            System.err.println("Error al mostrar corredor por ID: " + ex.getMessage());
        }
    }

    /**
     * Muestra un corredor por su dorsal
     *
     * @param dorsal dorsal del corredor a mostrar
     */
    public void mostrarCorredorPorDorsalDOM(int dorsal) {
        try {
            Corredor c = gestorDOM.mostrarCorredorPorDorsal(dorsal);
            System.out.println(c.toString());
        } catch (ExcepcionXML ex) {
            System.err.println("Error al mostrar corredor por dorsal: " + ex.getMessage());
        }
    }

    /**
     * Muestra los corredores de un equipo concreto
     *
     * @param equipo equipo a buscar
     */
    public void mostrarCorredoresPorEquipoDOM(String equipo) {
        try {
            List<Corredor> listaCorredores = gestorDOM.mostrarCorredoresPorEquipo(equipo);
            if (listaCorredores.isEmpty()) {
                System.out.println("No se han encontrado corredores para el equipo " + equipo);
            } else {
                System.out.println("Lista de corredores del equipo " + equipo + ":");
                for (Corredor c : listaCorredores) {
                    System.out.println(c);
                }
            }
        } catch (ExcepcionXML ex) {
            System.err.println("Error al mostrar corredores por equipo: " + ex.getMessage());
        }
    }

    /**
     * Añade un nuevo corredor al documento XML
     */
    public void añadirNuevoCorredorDOM() {

        // Creo 2 corredores para testear, 1 velocista y un fondista

        List<Puntuacion> historial = List.of(
                new Puntuacion(2022, 150),
                new Puntuacion(2023, 200)
        );

        Corredor nuevoCorredor = new Fondista("C08", "Nuevo Corredor", LocalDate.of(1992, 7, 10), "Equipo H", historial, (float) 10000);
        Corredor nuevoCorredor2 = new Velocista("C09", "Nuevo Velocista", LocalDate.of(1996, 8, 25), "Equipo I", historial, (float) 10.5);

        try {
            gestorDOM.insertarCorredor(nuevoCorredor);
            gestorDOM.insertarCorredor(nuevoCorredor2);
            System.out.println("Corredor añadido correctamente.");
            guardarDocumentoDOM(rutaGuardado);
        } catch (ExcepcionXML ex) {
            System.err.println("Error al añadir nuevo corredor: " + ex.getMessage());
        }
    }

    /**
     * Elimina un corredor por su ID
     *
     * @param ID id del corredor a eliminar
     */
    public void eliminarCorredorPorIDDOM(String ID) {
        try {
            gestorDOM.eliminarCorredorPorCodigo(ID);
            System.out.println("Corredor con ID " + ID + " eliminado correctamente.");
            guardarDocumentoDOM(rutaGuardado);
        } catch (ExcepcionXML ex) {
            System.err.println("Error al eliminar corredor por ID: " + ex.getMessage());
        }
    }

    /**
     * Añade o modifica la puntuación de un corredor por su ID
     *
     * @param ID              id del corredor a modificar
     * @param nuevaPuntuacion nueva puntuación a añadir o modificar
     */
    public void añadirOModificarPuntuacionDOM(String ID, Puntuacion nuevaPuntuacion) {
        try {
            if (gestorDOM.modificarPuntuacion(ID, nuevaPuntuacion)) {
                System.out.println("Puntuación añadida/modificada correctamente para el corredor con ID " + ID);
                guardarDocumentoDOM(rutaGuardado);
            }
        } catch (ExcepcionXML ex) {
            System.err.println("Error al añadir/modificar puntuación: " + ex.getMessage());
        }
    }

    /**
     * Elimina la puntuación de un corredor por su ID y año
     *
     * @param ID   id del corredor a modificar
     * @param anho año por el que filtrar la puntuación a eliminar
     */
    public void eliminarPuntuacionDOM(String ID, int anho) {
        try {
            if (gestorDOM.eliminarPuntuacionDOM(ID, anho)) {
                System.out.println("Puntuación del año " + anho + " eliminada correctamente para el corredor con ID " + ID);
                guardarDocumentoDOM(rutaGuardado);
            } else {
                System.out.println("No se encontró la puntuación del año " + anho + " para el corredor con ID " + ID);
            }
        } catch (ExcepcionXML ex) {
            System.err.println("Error al eliminar puntuación: " + ex.getMessage());
        }
    }

    /**
     * Guarda el documento XML modificado en una ruta dada
     *
     * @param rutaXML String con la ruta del fichero
     */
    public void guardarDocumentoDOM(String rutaXML) {
        try {
            gestorDOM.guardarDocumentoDOM(rutaXML);
            System.out.println("Documento XML guardado correctamente en: " + rutaXML);
        } catch (ExcepcionXML ex) {
            System.err.println("Error al guardar documento XML: " + ex.getMessage());
        }
    }

    public void buscarCorredoresPorVelocidadMediaXPath(float velocidadMinima) throws ExcepcionXML {
        String expr = "//velocista[velocidad_media > " + velocidadMinima + "]";
        List<Corredor> lista = gestorDOM.buscarPorXPath(expr);

        lista.forEach(System.out::println);
    }


    // SAX METODOS

    /**
     * Este método solo sirve para testear la carga.
     * LLama a cargarDocumento de CorredorSAX
     *
     * @param rutaXML    String con la ruta del fichero
     * @param validacion Enum con el tipo de validación
     * @throws ExcepcionXML lanzada si hay un error al cargar el documento
     */
    public void cargarDocumentoSAX(String rutaXML, TipoValidacion validacion) throws ExcepcionXML {
        try {
            gestorSAX.cargarCorredores(rutaXML, validacion);
            System.out.println("Documento XML cargado correctamente");
        } catch (ExcepcionXML e) {
            System.err.println("Error al cargar documento XML: " + e.getMessage());
        }
    }

    /**
     * Función que recibe de CorredorXML un List de Corredores y la muestra en función de su método toString()
     *
     * @param ruta ruta del fichero XML
     * @param validacion tipo de validación
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
     * Función que recibe de CorredorXML un List de Corredores filtrados por equipo y la muestra en función de su método toString()
     *
     * @param ruta ruta del fichero XML
     * @param equipo equipo a buscar
     * @param validacion tipo de validación
     */
    public void mostrarCorredoresPorEquipoSAX(String ruta, String equipo, TipoValidacion validacion) {
        try {
            List<Corredor> lista = gestorSAX.cargarCorredoresPorEquipo(ruta, equipo, validacion);
            if (lista.isEmpty()) {
                System.out.println("No se han encontrado corredores para el equipo " + equipo);
            } else {
                System.out.println("Lista de corredores del equipo " + equipo + ":");
                for (Corredor c : lista) {
                    System.out.println(c);
                }
            }
        } catch (ExcepcionXML e) {
            System.out.println(e.getMessage());
        }

    }

    public void actualizarDOMconSAX(String ruta1, String ruta2, TipoValidacion validacion1, TipoValidacion validacion2) {
        try {
            documentoXML = gestorDOM.cargarDocumentoDOM(ruta1, validacion1);

            List<Corredor> corredoresOriginal = gestorDOM.cargarCorredores(documentoXML);
            List<Corredor> corredoresActualizacion = gestorSAX.cargarCorredoresActualizacion(ruta2, validacion2);

            for(Corredor cActualizaciones : corredoresActualizacion){
                Corredor cOriginal = gestorDOM.mostrarCorredorPorIdDOM(cActualizaciones.getCodigo());
                // No hay aun corredor con ese ID,
                if(cOriginal == null){
                    if(cActualizaciones.getEquipo() != null && cActualizaciones.getCodigo() != null
                    && cActualizaciones.getNombre() != null && cActualizaciones.getFechaNacimiento() != null){
                        gestorDOM.insertarCorredor(cActualizaciones);
                    }
                }
                else{
                    // Actualizo los datos del corredor original con los del de actualizaciones
                    if(cActualizaciones.getNombre() != null){
                        cOriginal.setNombre(cActualizaciones.getNombre());
                    }
                    if(cActualizaciones.getFechaNacimiento() != null){
                        cOriginal.setFechaNacimiento(cActualizaciones.getFechaNacimiento());
                    }
                    if(cActualizaciones.getEquipo() != null){
                        cOriginal.setEquipo(cActualizaciones.getEquipo());
                    }
                    if(cActualizaciones instanceof Velocista && cOriginal instanceof Velocista){
                        ((Velocista) cOriginal).setVelocidadMedia(((Velocista) cActualizaciones).getVelocidadMedia());
                    }
                    if(cActualizaciones instanceof Fondista && cOriginal instanceof Fondista){
                        ((Fondista) cOriginal).setDistanciaMax(((Fondista) cActualizaciones).getDistanciaMax());
                    }
                    if(cActualizaciones.getHistorial() != null){
                        cOriginal.setHistorial(cActualizaciones.getHistorial());
                    }
                }
            }

            guardarDocumentoDOM(ruta1);


            System.out.println("Documento DOM actualizado correctamente con los datos de SAX.");
            guardarDocumentoDOM(rutaGuardado);
        } catch (ExcepcionXML e) {
            System.err.println("Error al actualizar DOM con SAX: " + e.getMessage());
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
