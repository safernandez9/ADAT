import logica.GestorCorredores;
import persistencia.TipoValidacion;

private static final String RUTA = "ArchivosXMLDTD/Corredores.xml";
private static final GestorCorredores gestor = new GestorCorredores();

public static void main(String[] args) {

    gestor.cargarDocumento(RUTA, TipoValidacion.DTD);
    gestor.cargarDocumentoSAX(RUTA, TipoValidacion.DTD);
}

