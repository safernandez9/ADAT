package persistenciaSAX;

import clases.Corredor;
import clases.Equipo;
import clases.Patrocinador;
import utilidades.ExcepcionXML;
import utilidades.TipoValidacion;

import java.util.List;

public class EquiposSAX {
    
    public List<Patrocinador> cargarPatrocinadoresActualizacion(String rutaFichero, TipoValidacion validacion) throws ExcepcionXML {
        try {
            PatrocinadoresActualizacionHandler miHandler = new PatrocinadoresActualizacionHandler();
            XMLSAXUtils.cargarDocumentoXMLSAX(rutaFichero, validacion, miHandler);
            return miHandler.getPatrocinadores();
        } catch (Exception e) {
            throw new ExcepcionXML("Error al cargar los patrocinadores de actualizacion: " + e.getMessage());
        }
    }
}
