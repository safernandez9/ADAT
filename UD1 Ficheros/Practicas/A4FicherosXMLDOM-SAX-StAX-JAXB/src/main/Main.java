import clases.Puntuacion;
import logica.GestorCorredores;
import persistenciaDOM.TipoValidacion;

private static final String RUTA = "ArchivosXMLDTD/Corredores.xml";
private static final GestorCorredores gestor = new GestorCorredores();

public static void main(String[] args) {

    System.out.println("=============================");
    System.out.println("TRABAJO CON DOM");
    System.out.println("============================");

    System.out.println("\nCargando documento XML con validación DTD...\n");
    gestor.cargarDocumentoDOM(RUTA, TipoValidacion.DTD);

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

    // Añadir corredores

    System.out.println("\n\nAñadiendo nuevos corredores al documento XML...\n");
    gestor.añadirNuevoCorredorDOM();
    gestor.listarCorredoresDOM();

    // Eliminar corredores por codigo
    System.out.println("\n\nElimino corredor con ID 'C08' del documento XML...\n");
    gestor.eliminarCorredorPorIDDOM("C08");
    gestor.eliminarCorredorPorIDDOM("C09");
    gestor.eliminarCorredorPorIDDOM("C99"); // No existente

    System.out.println("\nListado de corredores tras las eliminaciones:\n");
    gestor.listarCorredoresDOM();

    // Modificar puntuacion corredor

    System.out.println("\n\nModifico la puntuación del corredor con ID 'C01'...\n");
    gestor.añadirOModificarPuntuacionDOM("C01", new Puntuacion(2023, (float)48.7));
    System.out.println("\n\nAñado una puntuación del corredor con ID 'C01'\n");
    gestor.añadirOModificarPuntuacionDOM("C01", new Puntuacion(2024, (float)54.3));
    System.out.println("\n\nAñado una puntuación del corredor con ID 'C99' (no existente)\n");
    gestor.añadirOModificarPuntuacionDOM("C99", new Puntuacion(2024, (float)54.3));
    System.out.println("\nListado de corredores tras las modificaciones de puntuación:\n");
    gestor.listarCorredoresDOM();






    // TRABAJO SAX

//    System.out.println("\n\n=============================");
//    System.out.println("TRABAJO CON SAX");
//    System.out.println("============================");
//
//    gestor.cargarDocumentoSAX(RUTA, TipoValidacion.DTD);




}

