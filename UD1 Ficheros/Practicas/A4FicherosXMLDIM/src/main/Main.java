import logica.GestorCorredores;
import persistencia.TipoValidacion;

private static final String RUTA = "UD1 Ficheros/Practicas/A4FicherosXMLDOM/src/ArchivosXMLDOM/Corredores.xml";
private static final GestorCorredores gestor = new GestorCorredores();

public static void main(String[] args) {

    gestor.cargarDocumento(RUTA, TipoValidacion.DTD);
}

