import logica.GestorCorredores;
import persistenciaDOM.TipoValidacion;

private static final String RUTA = "ArchivosXMLDTD/Corredores.xml";
private static final GestorCorredores gestor = new GestorCorredores();

public static void main(String[] args) {

    gestor.cargarDocumento(RUTA, TipoValidacion.DTD);



    // TRABAJO SAX

    System.out.println("\n\n=============================");
    System.out.println("TRABAJO CON SAX");
    System.out.println("============================");

    gestor.cargarDocumentoSAX(RUTA, TipoValidacion.DTD);




}

