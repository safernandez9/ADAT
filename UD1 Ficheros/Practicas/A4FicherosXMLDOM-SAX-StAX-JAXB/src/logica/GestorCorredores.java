package logica;

/*
 * La capa Lógica:
 * Responsabilidad: Gestionar la lógica de feedback
 * al usuario, y coordinar la persistencia. Es como el director de orquesta de la aplicación.
 * Es el único que "habla" con la clase principal (el main) y es el único que "habla" con la
 * capa de persistencia (CorredorXML). Hay impresión de datos.
 */

import clases.*;
import org.w3c.dom.Document;
import persistenciaDOM.CorredorXML;
import persistenciaDOM.EquipoXML;
import persistenciaJAXB.clasesJAXB.CorredorJAXB;
import persistenciaJAXB.clasesJAXB.CorredoresJAXB;
import persistenciaSAX.EquiposSAX;
import utilidades.ExcepcionXML;
import utilidades.TipoValidacion;
import persistenciaSAX.CorredoresSAX;
import persistenciaStAX.modoCursor.CorredoresStAXCursor;
import persistenciaStAX.modoCursor.XMLStAXUtilsCursor;
import persistenciaStAX.modoEventos.CorredoresStAXEventos;
import persistenciaStAX.modoEventos.XMLStAXUtilsEventos;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamReader;
import java.util.List;
import java.util.Map;


public class GestorCorredores {


    private final CorredorXML gestorDOMCorredores;
    private final EquipoXML gestorDOMEquipos;
    private Document documentoXML;

    private final CorredoresSAX gestorSAXCorredores;
    private final EquiposSAX gestorSAXEquipos = new EquiposSAX();


    private final CorredoresStAXCursor gestorStAXCursor;
    private final CorredoresStAXEventos gestorStAXEventos;


    /**
     * El constructor inicializa los gestores para las diferentes metodologías
     * creando uno nuevo por cada una, DOM, SAX, StAXCursor y StAXEventos
     */
    public GestorCorredores() {
        this.gestorDOMCorredores = new CorredorXML();
        this.gestorDOMEquipos = new EquipoXML();
        this.gestorSAXCorredores = new CorredoresSAX();
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
            this.documentoXML = gestorDOMCorredores.cargarDocumentoDOM(rutaXML, validacion);
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
            List<Corredor> listaCorredores = gestorDOMCorredores.cargarCorredores(documentoXML);
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
            Corredor c = gestorDOMCorredores.mostrarCorredorPorIdDOM(ID);
            if (c != null) {
                System.out.println(c.toString());
            } else {
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
            Corredor c = gestorDOMCorredores.mostrarCorredorPorDorsal(dorsal);
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
            List<Corredor> listaCorredores = gestorDOMCorredores.mostrarCorredoresPorEquipo(equipo);
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
     *
     * @param nuevoCorredor corredor a añadir
     */
    public void añadirNuevoCorredorDOM(Corredor nuevoCorredor) {

        try {
            if (nuevoCorredor == null) {
                System.err.println("El corredor a añadir no puede ser nulo.");
                return;
            }
            gestorDOMCorredores.insertarCorredor(nuevoCorredor);
            System.out.println("Corredor añadido correctamente.");
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
            gestorDOMCorredores.eliminarCorredorPorCodigo(ID);
            System.out.println("Corredor con ID " + ID + " eliminado correctamente.");
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
            if (gestorDOMCorredores.modificarPuntuacion(ID, nuevaPuntuacion)) {
                System.out.println("Puntuación añadida/modificada correctamente para el corredor con ID " + ID);
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
            if (gestorDOMCorredores.eliminarPuntuacionDOM(ID, anho)) {
                System.out.println("Puntuación del año " + anho + " eliminada correctamente para el corredor con ID " + ID);
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
            gestorDOMCorredores.guardarDocumentoDOM(rutaXML);
            System.out.println("Documento XML guardado correctamente en: " + rutaXML);
        } catch (ExcepcionXML ex) {
            System.err.println("Error al guardar documento XML: " + ex.getMessage());
        }
    }


    // Testeo de XPath

    /**
     * Busca corredores por velocidad media usando XPath
     *
     * @param velocidadMinima velocidad mínima para filtrar
     * @throws ExcepcionXML lanzada si hay un error en la búsqueda
     */
    public void buscarCorredoresPorVelocidadMediaXPath(float velocidadMinima) throws ExcepcionXML {
        String expr = "//velocista[velocidad_media > " + velocidadMinima + "]";
        List<Corredor> lista = gestorDOMCorredores.buscarPorXPath(expr);

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
            gestorSAXCorredores.cargarCorredores(rutaXML, validacion);
            System.out.println("Documento XML cargado correctamente");
        } catch (ExcepcionXML e) {
            System.err.println("Error al cargar documento XML: " + e.getMessage());
        }
    }

    /**
     * Función que recibe de CorredorXML un List de Corredores y la muestra en función de su método toString()
     *
     * @param ruta       ruta del fichero XML
     * @param validacion tipo de validación
     */
    public void mostrarCorredoresSAX(String ruta, TipoValidacion validacion) {
        try {
            List<Corredor> lista = gestorSAXCorredores.cargarCorredores(ruta, validacion);
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
     * @param ruta       ruta del fichero XML
     * @param equipo     equipo a buscar
     * @param validacion tipo de validación
     */
    public void mostrarCorredoresPorEquipoSAX(String ruta, String equipo, TipoValidacion validacion) {
        try {
            List<Corredor> lista = gestorSAXCorredores.cargarCorredoresPorEquipo(ruta, equipo, validacion);
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

    /**
     * Actualiza el documento DOM con los datos de un fichero SAX de actualizaciones
     *
     * @param rutaOriginal        ruta del fichero XML original
     * @param rutaActualizaciones ruta del fichero XML con las actualizaciones
     * @param rutaFinal           ruta del fichero XML final donde se guardará el resultado
     * @param validacion1         tipo de validación para el fichero original
     * @param validacion2         tipo de validación para el fichero de actualizaciones
     */
    public void actualizarDOMconSAX(String rutaOriginal, String rutaActualizaciones, String rutaFinal, TipoValidacion validacion1, TipoValidacion validacion2) {
        try {
            documentoXML = gestorDOMEquipos.cargarDocumentoDOM(rutaOriginal, validacion1);

            List<Equipo> equiposOriginal = gestorDOMEquipos.cargarEquipos(documentoXML);
            List<Patrocinador> patrocinadoresActualizacion = gestorSAXEquipos.cargarPatrocinadoresActualizacion(rutaActualizaciones, validacion2);

            for (Patrocinador p : patrocinadoresActualizacion) {
                Equipo eOriginal = gestorDOMEquipos.mostrarEquipoPorIdDOM(p.getIdEquipoPatrocinaActualizacion());
                // No hay aun corredor con ese ID,
                if (eOriginal == null) {
                    if (p.getNombre() != null && p.getIdEquipoPatrocinaActualizacion() != null && !p.getIdEquipoPatrocinaActualizacion().isEmpty()
                            && !p.getNombre().isEmpty() && p.getFechaInicio() != null && p.getNombreEquipoPatrocinaActualizacion() != null
                            && !p.getNombreEquipoPatrocinaActualizacion().isEmpty()) {

                        // Creo un nuevo equipo con el patrocinador
                        Equipo nuevoEquipo = new Equipo(p.getIdEquipoPatrocinaActualizacion(), p.getNombre(), p);
                        gestorDOMEquipos.insertarEquipoDesdeNuevoPatrocinador(nuevoEquipo);
                    }
                } else {
                    boolean patrocinadorExiste = false;

                    // Si el equipo ya tiene patrocinadores los compruebo todos para ver si ya existe el patrocinador a actualizar
                    if (eOriginal.getPatrocinadores() != null) {
                        for (Patrocinador p2 : eOriginal.getPatrocinadores()) {
                            if (p2.getNombre().equalsIgnoreCase(p.getNombre())) {
                                // Si existe el patrocinador lo actualizo
                                p2.setDonacion(p.getDonacion());
                                p2.setFechaInicio(p.getFechaInicio());
                                patrocinadorExiste = true;
                                break;
                            }
                        }
                    }
                    // Si no existe el patrocinador en el equipo o no tiene patrocinadores aun lo añado
                    if (!patrocinadorExiste) {
                        eOriginal.addPatrocinador(p);
                    }

                    gestorDOMEquipos.actualizarPatrocinadoresEquipo(eOriginal);
                }
            }
            System.out.println("Documento DOM actualizado correctamente con los datos de SAX.");
            gestorDOMEquipos.guardarDocumentoDOM(rutaFinal);
        } catch (ExcepcionXML e) {
            System.err.println("Error al actualizar DOM con SAX: " + e.getMessage());
        }
    }


    // STAX METODOS


    /**
     * Mostrar los Corredores sacados de un fichero por pantalla con StAX Cursor.
     *
     * @param ruta       Ruta del fichero XML
     * @param validacion Tipo de validación (DTD, XSD o ninguna)
     */
    public void mostrarCorredoresStAXCursor(String ruta, TipoValidacion validacion) {
        try {
            XMLStreamReader reader = XMLStAXUtilsCursor.cargarDocumentoStAXCursor(ruta, validacion);
            List<Corredor> lista = gestorStAXCursor.leerCorredores(reader);

            System.out.println("Lista de corredores (StAX Cursor)\n");
            for (Corredor c : lista) {
                System.out.println(c);
            }

        } catch (ExcepcionXML e) {
            System.err.println("Error al leer corredores con StAX Cursor: " + e.getMessage());
        }
    }

    /**
     * Mostrar los Corredores sacados de un fichero por pantalla con StAX Eventos.
     *
     * @param ruta       Ruta del fichero XML
     * @param validacion Tipo de validación (DTD, XSD o ninguna)
     */
    public void mostrarCorredoresStAXEventos(String ruta, TipoValidacion validacion) {
        try {
            XMLEventReader reader = XMLStAXUtilsEventos.cargarDocumentoStAXEventos(ruta, validacion);
            List<Corredor> lista = gestorStAXEventos.leerCorredores(reader);

            System.out.println("Lista de corredores (StAX Eventos)\n");
            for (Corredor c : lista) {
                System.out.println(c);
            }

        } catch (ExcepcionXML e) {
            System.err.println("Error al leer corredores con StAX Eventos: " + e.getMessage());
        }
    }

    /**
     * Muestra los Corredores sacados de un fichero por equipo con StAX Cursor.
     *
     * @param ruta       Ruta del fichero XML
     * @param equipo     Equipo a buscar
     * @param validacion Tipo de validación (DTD, XSD o ninguna)
     */
    public void mostrarCorredoresPorEquipoStAXCursor(String ruta, String equipo, TipoValidacion validacion) {
        try {
            XMLStreamReader reader = XMLStAXUtilsCursor.cargarDocumentoStAXCursor(ruta, validacion);
            List<Corredor> lista = gestorStAXCursor.leerCorredoresPorEquipo(reader, equipo);
            if (lista.isEmpty()) {
                System.out.println("No se han encontrado corredores para el equipo " + equipo);
            } else {
                System.out.println("Lista de corredores del equipo " + equipo + ":");
                for (Corredor c : lista) {
                    System.out.println(c);
                }
            }
        } catch (ExcepcionXML e) {
            System.err.println("Error al leer corredores por equipo con StAX Cursor: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error inesperado al leer corredores por equipo con StAX Cursor: " + e.getMessage());
        }
    }

    /**
     * Muestra los Corredores sacados de un fichero por equipo con StAX Eventos.
     *
     * @param ruta       Ruta del fichero XML
     * @param equipo     Equipo a buscar
     * @param validacion Tipo de validación (DTD, XSD o ninguna)
     */
    public void mostrarCorredoresPorEquipoStAXEventos(String ruta, String equipo, TipoValidacion validacion) {
        try {
            XMLEventReader reader = XMLStAXUtilsEventos.cargarDocumentoStAXEventos(ruta, validacion);
            List<Corredor> lista = gestorStAXEventos.leerCorredoresPorEquipo(reader, equipo);
            if (lista.isEmpty()) {
                System.out.println("No se han encontrado corredores para el equipo " + equipo);
            } else {
                System.out.println("Lista de corredores del equipo " + equipo + ":");
                for (Corredor c : lista) {
                    System.out.println(c);
                }
            }
        } catch (ExcepcionXML e) {
            System.err.println("Error al leer corredores por equipo con StAX Eventos: " + e.getMessage());
        }
    }

    /**
     * Actualiza el documento DOM con los datos de un fichero StAX de actualizaciones
     *
     * @param rutaOriginal              ruta del fichero XML original
     * @param rutaActualizaciones       ruta del fichero XML con las actualizaciones
     * @param rutaDomActualizadoConStax ruta del fichero XML final donde se guardará el resultado
     * @param validacion1               tipo de validación para el fichero original
     * @param validacion2               tipo de validación para el fichero de actualizaciones
     */
    public void actualizarDOMconStAX(String rutaOriginal, String rutaActualizaciones, String rutaDomActualizadoConStax, TipoValidacion validacion1, TipoValidacion validacion2) {
        try {
            XMLStreamReader reader = XMLStAXUtilsCursor.cargarDocumentoStAXCursor(rutaActualizaciones, validacion2);

            documentoXML = gestorDOMEquipos.cargarDocumentoDOM(rutaOriginal, validacion1);

            List<Equipo> equiposOriginal = gestorDOMEquipos.cargarEquipos(documentoXML);
            List<Patrocinador> patrocinadoresActualizacion = gestorStAXCursor.cargarPatrocinadoresActualizacion(reader);

            for (Patrocinador p : patrocinadoresActualizacion) {
                Equipo eOriginal = gestorDOMEquipos.mostrarEquipoPorIdDOM(p.getIdEquipoPatrocinaActualizacion());
                // No hay aún corredor con ese ID,
                if (eOriginal == null) {
                    if (p.getNombre() != null && p.getIdEquipoPatrocinaActualizacion() != null && !p.getIdEquipoPatrocinaActualizacion().isEmpty()
                            && !p.getNombre().isEmpty() && p.getFechaInicio() != null && p.getNombreEquipoPatrocinaActualizacion() != null
                            && !p.getNombreEquipoPatrocinaActualizacion().isEmpty()) {

                        // Creo un nuevo equipo con el patrocinador
                        Equipo nuevoEquipo = new Equipo(p.getIdEquipoPatrocinaActualizacion(), p.getNombre(), p);
                        gestorDOMEquipos.insertarEquipoDesdeNuevoPatrocinador(nuevoEquipo);
                    }
                } else {
                    boolean patrocinadorExiste = false;

                    // Si el equipo ya tiene patrocinadores los compruebo todos para ver si ya existe el patrocinador a actualizar
                    if (eOriginal.getPatrocinadores() != null) {
                        for (Patrocinador p2 : eOriginal.getPatrocinadores()) {
                            if (p2.getNombre().equalsIgnoreCase(p.getNombre())) {
                                // Si existe el patrocinador lo actualizo
                                p2.setDonacion(p.getDonacion());
                                p2.setFechaInicio(p.getFechaInicio());
                                patrocinadorExiste = true;
                                break;
                            }
                        }
                    }
                    // Si no existe el patrocinador en el equipo o no tiene patrocinadores aun lo añado
                    if (!patrocinadorExiste) {
                        eOriginal.addPatrocinador(p);
                    }

                    gestorDOMEquipos.actualizarPatrocinadoresEquipo(eOriginal);
                }
            }
            System.out.println("Documento DOM actualizado correctamente con los datos de StAX.");
            gestorDOMEquipos.guardarDocumentoDOM(rutaDomActualizadoConStax);
        } catch (ExcepcionXML e) {
            System.err.println("Error al actualizar DOM con StAX: " + e.getMessage());
        }
    }

    /**
     * Calcula las donaciones por equipo y las escribe en un nuevo fichero XML usando StAX Cursor.
     *
     * @param rutaOriginal  Ruta del fichero XML original
     * @param rutaResultado Ruta del fichero XML donde se guardarán los resultados
     * @param validacion    Tipo de validación (DTD, XSD o ninguna)
     */
    public void calcularDonacionesPorEquipoCursor(String rutaOriginal, String rutaResultado, TipoValidacion validacion) {
        try {
            XMLStreamReader readerEquipos = XMLStAXUtilsCursor.cargarDocumentoStAXCursor(rutaOriginal, validacion);
            Map<String, Double> donaciones = gestorStAXCursor.leerDonacionesPatrocinadores(readerEquipos);
            gestorStAXCursor.escribirDonaciones(rutaResultado, donaciones);
        } catch (ExcepcionXML e) {
            System.err.println("Error al calcular donaciones por equipo CURSOR: " + e.getMessage());
        }
    }

    /**
     * Calcula las donaciones por equipo y las escribe en un nuevo fichero XML usando StAX Eventos.
     *
     * @param rutaOriginal  Ruta del fichero XML original
     * @param rutaResultado Ruta del fichero XML donde se guardarán los resultados
     * @param validacion    Tipo de validación (DTD, XSD o ninguna)
     */
    public void calcularDonacionesPorEquipoEventos(String rutaOriginal, String rutaResultado, TipoValidacion validacion) {
        try {
            XMLEventReader readerEquipos = XMLStAXUtilsEventos.cargarDocumentoStAXEventos(rutaOriginal, validacion);
            Map<String, Double> donaciones = gestorStAXEventos.leerDonacionesPatrocinadores(readerEquipos);
            gestorStAXEventos.escribirDonaciones(rutaResultado, donaciones);
        } catch (ExcepcionXML e) {
            System.err.println("Error al calcular donaciones por equipo EVENTOS: " + e.getMessage());
        }
    }


    /**
     * Muestra los Corredores sacados de un fichero por pantalla.
     *
     * @param ruta
     */
    public void mostrarCorredoresJAXB(String ruta) {
        try {
            CorredoresJAXB corredores = new CorredoresJAXB();
            corredores.leerCorredores(ruta);
            for (CorredorJAXB c : corredores.getCorredores()) {
                System.out.println(c);
            }
        } catch (ExcepcionXML e) {
            System.err.println("Error al leer: " + ruta + " con JAXB. " + e.getMessage());
        }
    }
}

