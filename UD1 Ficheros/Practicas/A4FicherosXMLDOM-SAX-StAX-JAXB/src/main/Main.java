import clases.Corredor;
import clases.Fondista;
import clases.Puntuacion;
import clases.Velocista;
import logica.GestorCorredores;
import logica.GestorEquipos;
import utilidades.TipoValidacion;



// Rutas del archivo XML de corredores principal con las líneas para sus distintas validaciones
// (No lo edito directamente, sino que hago copias para las modificaciones)
private static final String RUTA_CORREDORES = "ArchivosXMLDTD/Corredores.xml";
private static final String RUTA_CORREDORES_DTD = "ArchivosXMLDTD/CorredoresDTD.xml";
private static final String RUTA_CORREDORES_XSD = "ArchivosXMLDTD/CorredoresXSD.xml";

// Rutas del archivo XML de equipos principal con las líneas para sus distintas validaciones
// (No lo edito directamente, sino que hago copias para las modificaciones)
private static final String RUTA_EQUIPOS = "ArchivosXMLDTD/Equipos.xml";
private static final String RUTA_EQUIPOS_DTD = "ArchivosXMLDTD/EquiposDTD.xml";
private static final String RUTA_EQUIPOS_XSD = "ArchivosXMLDTD/EquiposXSD.xml";

// Ruta donde se guardará el XML modificado con DOM (después de añadir/eliminar corredores y modificar puntuaciones)
private static final String RUTA_GUARDADO_DOM = "ArchivosXMLDTD/Corredores_Modificado.xml";
// Ruta del archivo XML de actualizaciones (para la parte de actualizar con SAX)
private static final String RUTA_ACTUALIZACIONES = "ArchivosXMLDTD/Actualizaciones.xml";
private static final String RUTA_DOM_ACTUALIZADO_CON_SAX = "ArchivosXMLDTD/equiposUpdate.xml";
private static final String RUTA_DOM_ACTUALIZADO_CON_STAX = "ArchivosXMLDTD/equiposUpdateStAX.xml";
private static final String RUTA_STAX_DONACIONES_CURSOR = "ArchivosXMLDTD/DonacionesTotalesCursor.xml";
private static final String RUTA_STAX_DONACIONES_EVENTOS = "ArchivosXMLDTD/DonacionesTotalesEventos.xml";

private static final String RUTA_JAXB_EQUIPOS = "ArchivosXMLDTD/Equipos.xml";

private static final GestorCorredores gestor = new GestorCorredores();

public static void main(String[] args) {

    // Manejo de fechas en otros formatos
    /*
     * DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
     * LocalDate fecha = LocalDate.parse(fechaTexto, formato);
     *
     * Si el formato es el de siempre: yyyy-MM-dd
     * LocalDate fecha = LocalDate.parse(fechaTexto)
     */

    // MANEJO DOM

    System.out.println("=============================");
    System.out.println("TRABAJO CON DOM");
    System.out.println("============================");

    // CARGA

    System.out.println("\nCargando documento XML con validación DTD...\n");
    gestor.cargarDocumentoDOM(RUTA_CORREDORES_DTD, TipoValidacion.DTD);

    //System.out.println("\nCargando documento XML con validación XSD...\n");
    //gestor.cargarDocumentoDOM(RUTA_CORREDORES_XSD, TipoValidacion.XSD);

    // Listado general

    System.out.println("\nListado de corredores cargados desde el documento XML:\n");
    gestor.listarCorredoresDOM();

    // Corredores por ID 2 válidos y 1 inválido

    System.out.println("\n\nImprimir corredor con el ID 'C03':\n");
    gestor.mostrarCorredorPorIDDOM("C03");

    System.out.println("\n\nImprimir corredor con el ID 'C02':\n");
    gestor.mostrarCorredorPorIDDOM("C02");

    System.out.println("\n\nImprimir corredor con el ID 'C99' (no existente):\n");
    gestor.mostrarCorredorPorIDDOM("C99");

    // Corredores por dorsal 2 válidos y 1 inválido

    System.out.println("\n\nImprimir corredor con el dorsal 2:\n");
    gestor.mostrarCorredorPorDorsalDOM(2);

    System.out.println("\n\nImprimir corredor con el dorsal 3:\n");
    gestor.mostrarCorredorPorDorsalDOM(3);

    System.out.println("\n\nImprimir corredor con el dorsal 999 (no existente):\n");
    gestor.mostrarCorredorPorDorsalDOM(999);

    // Añadir corredores (Creo un velocista y un fondista nuevos para testear)

    List<Puntuacion> historial = List.of(
            new Puntuacion(2022, 150),
            new Puntuacion(2023, 200)
    );

    Corredor nuevoCorredor = new Fondista("C21", "Nuevo Corredor", LocalDate.of(1992, 7, 10), "Equipo H", historial, (float) 10000);
    Corredor nuevoCorredor2 = new Velocista("C22", "Nuevo Velocista", LocalDate.of(1996, 8, 25), "Equipo I", historial, (float) 10.5);

    System.out.println("\n\nAñadiendo nuevos corredores al documento XML...\n");
    gestor.añadirNuevoCorredorDOM(nuevoCorredor);
    gestor.añadirNuevoCorredorDOM(nuevoCorredor2);
    gestor.listarCorredoresDOM();

    // Eliminar corredores por codigo

    System.out.println("\n\nElimino corredor con ID 'C08' del documento XML...\n");
    gestor.eliminarCorredorPorIDDOM("C08");
    gestor.eliminarCorredorPorIDDOM("C09");
    gestor.eliminarCorredorPorIDDOM("C99"); // No existente

    System.out.println("\nListado de corredores tras las eliminaciones:\n");
    gestor.listarCorredoresDOM();

    // Modificar puntuación corredor

    System.out.println("\n\nModifico la puntuación del corredor con ID 'C01'...\n");
    gestor.añadirOModificarPuntuacionDOM("C01", new Puntuacion(2023, (float)48.7));

    System.out.println("\n\nAñado una puntuación del corredor con ID 'C01'\n");
    gestor.añadirOModificarPuntuacionDOM("C01", new Puntuacion(2024, (float)54.3));

    System.out.println("\n\nAñado una puntuación del corredor con ID 'C99' (no existente)\n");
    gestor.añadirOModificarPuntuacionDOM("C99", new Puntuacion(2024, (float)54.3));

    System.out.println("\nListado de corredores tras las modificaciones de puntuación:\n");
    gestor.listarCorredoresDOM();

    // Eliminar puntuación corredor

    System.out.println("\n\nElimino la puntuación del año 2022 del corredor con ID 'C01'...\n");
    gestor.eliminarPuntuacionDOM("C01", 2022);
    System.out.println("\n\nIntento eliminar la puntuación del año 2025 del corredor con ID 'C01' (no existente)...\n");
    gestor.eliminarPuntuacionDOM("C01", 2025);
    System.out.println("\n\nIntento eliminar la puntuación del año 2022 del corredor con ID 'C99' (corredor no existente)...\n");
    gestor.eliminarPuntuacionDOM("C99", 2022);
    System.out.println("\nListado de corredores tras las eliminaciones de puntuación:\n");
    gestor.listarCorredoresDOM();

    // Guardar documento XML (Para probarlo, está también en la capa lógica de las funciones que se piden)

    System.out.println("\n\nGuardando el documento XML tras las modificaciones...\n");
    gestor.guardarDocumentoDOM(RUTA_GUARDADO_DOM);

    System.out.println("\n\nCargando el documento XML guardado para verificar su contenido...\n");
    gestor.cargarDocumentoDOM(RUTA_GUARDADO_DOM, TipoValidacion.NO_VALIDAR);

    // Buscar con XPath

    System.out.println("\n\nBúsqueda de corredores con velocidad media superior a 25.0:\n");
    gestor.buscarCorredoresPorVelocidadMediaXPath((float)10.0);

    // TRABAJO SAX

    System.out.println("\n\n=============================");
    System.out.println("TRABAJO CON SAX");
    System.out.println("============================");

    // Cargar corredores SAX

    System.out.println("\n\nCargando documento XML con validación XSD...\n");
    gestor.cargarDocumentoSAX(RUTA_CORREDORES_XSD, TipoValidacion.XSD);

    // Listar corredores SAX

    System.out.println("\n\nListado de corredores cargados desde el documento XML:\n");
    gestor.mostrarCorredoresSAX(RUTA_CORREDORES_XSD, TipoValidacion.XSD);

    // Corredores por equipo SAX 1 válidos y 1 inválido

    System.out.println("\n\nListado de corredores del equipo 'E3' cargados desde el documento XML con SAX:\n");
    gestor.mostrarCorredoresPorEquipoSAX(RUTA_CORREDORES_XSD, "E3", TipoValidacion.XSD);
    System.out.println("\n\nListado de corredores del equipo 'E99' (no existente) cargados desde el documento XML con SAX:\n");
    gestor.mostrarCorredoresPorEquipoSAX(RUTA_CORREDORES_XSD, "E99", TipoValidacion.XSD);

    // Corredores por equipo con DOM 1 válidos y 1 inválido

    System.out.println("\n\nListado de corredores del equipo 'E3' cargados desde el documento XML con DOM:\n");
    gestor.mostrarCorredoresPorEquipoDOM("E3");
    System.out.println("\n\nListado de corredores del equipo 'E99' (no existente) cargados desde el documento XML con DOM:\n");
    gestor.mostrarCorredoresPorEquipoDOM("E99");

    // LEER CON DOM UN ARCHIVO, LEER OTRO CON SAX, ACTUALIZAR LOS VALORES DEL DOM CON LOS DEL SAX Y REESCRIBIR EL DOM A UN XML

    System.out.println("\n\nActualizando el documento XML con los datos cargados por SAX...\n");
    gestor.actualizarDOMconSAX(RUTA_EQUIPOS_DTD, RUTA_ACTUALIZACIONES, RUTA_DOM_ACTUALIZADO_CON_SAX, TipoValidacion.DTD, TipoValidacion.NO_VALIDAR);


    // TRABAJO StAX

    System.out.println("\n\n=============================");
    System.out.println("TRABAJO CON StAX");
    System.out.println("============================");


    // Cargar corredores StAX

    System.out.println("\n\nListado de corredores cargados desde el documento XML con StAX Cursor:\n");
    gestor.mostrarCorredoresStAXCursor(RUTA_CORREDORES, TipoValidacion.XSD);

    System.out.println("\n\nListado de corredores cargados desde el documento XML con StAX Eventos:\n");
    gestor.mostrarCorredoresStAXEventos(RUTA_CORREDORES, TipoValidacion.XSD);

    // Cargar corredores por equipo

    System.out.println("\n\nListado de corredores del equipo 'E2' cargados desde el documento XML con StAX Cursor:\n");
    gestor.mostrarCorredoresPorEquipoStAXCursor(RUTA_CORREDORES, "E2", TipoValidacion.XSD);
    System.out.println("\n\nListado de corredores del equipo 'E2' cargados desde el documento XML con StAX Eventos:\n");
    gestor.mostrarCorredoresPorEquipoStAXEventos(RUTA_CORREDORES, "E2", TipoValidacion.XSD);

    // LEER CON DOM UN ARCHIVO, LEER OTRO CON StAX, ACTUALIZAR LOS VALORES DEL DOM CON LOS DEL StAX Y REESCRIBIR EL DOM A UN XML

    System.out.println("\n\nActualizando el documento XML con los datos cargados por StAX...\n");
    gestor.actualizarDOMconStAX(RUTA_EQUIPOS_DTD, RUTA_ACTUALIZACIONES, RUTA_DOM_ACTUALIZADO_CON_STAX, TipoValidacion.DTD, TipoValidacion.NO_VALIDAR);

    // Calcular Donacion Total por Patrocinador

    System.out.println("\n\nCálculo de la donación total por patrocinador con CURSOR:\n");
    gestor.calcularDonacionesPorEquipoCursor(RUTA_EQUIPOS_XSD, RUTA_STAX_DONACIONES_CURSOR, TipoValidacion.XSD);

    System.out.println("\n\nCálculo de la donación total por patrocinador con EVENTOS:\n");
    gestor.calcularDonacionesPorEquipoEventos(RUTA_EQUIPOS_XSD, RUTA_STAX_DONACIONES_EVENTOS, TipoValidacion.XSD);


    // JAXB
    GestorEquipos ge = new GestorEquipos();

    System.out.println("\n\n=============================");
    System.out.println("TRABAJO CON JAXB");
    System.out.println("============================");

    // Cargar Equipos de Archivo XML con JAXB
    System.out.println("\n\nCargando equipos desde el documento XML con JAXB...\n");
    ge.mostrarEquiposJAXB(RUTA_JAXB_EQUIPOS);

//    // Cargar Corredores de Archivo XML con JAXB
//    System.out.println("\n\nCargando corredores desde el documento XML con JAXB...\n");
//    gestor.mostrarCorredoresJAXB(RUTA_CORREDORES);








}

