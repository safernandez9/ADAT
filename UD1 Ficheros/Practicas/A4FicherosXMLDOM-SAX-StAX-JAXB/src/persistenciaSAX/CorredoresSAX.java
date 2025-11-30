package persistenciaSAX;

import clases.Corredor;
import utilidades.ExcepcionXML;
import utilidades.TipoValidacion;

import java.util.List;


public class CorredoresSAX {

    // Podria hacer aqui una CorredorSAXHandler como atributo si quisiera reutilizarlo;

    /**
     *
     * Devuelve una List recibida del Handler de corredores a partir de un fichero XML con una manejadora que devuelve
     * todos sus datos en una List de objetos Corredor
     * @param rutaFichero ruta del XML en String
     * @param validacion  Enum con el tipo de validación
     * @return Lista de Corredores en el Handler al cargar
     * @throws ExcepcionXML Excepción al cargar el XML
     */
    public List<Corredor> cargarCorredores(String rutaFichero, TipoValidacion validacion) throws ExcepcionXML {

        CorredorSAXHandler miHandler = new CorredorSAXHandler();
        XMLSAXUtils.cargarDocumentoXMLSAX(rutaFichero, validacion, miHandler);
        return miHandler.getCorredores();
    }

    /**
     *
     * Devuelve una List recibida del Handler de corredores a partir de un fichero XML con una manejadora que devuelve
     * todos sus datos en una List de objetos Corredor que pertenecen a un equipo concreto
     * @param rutaFichero ruta del XML en String
     * @param equipo Equipo a buscar en String
     * @param validacion  Enum con el tipo de validación
     * @return Lista de Corredores en el Handler al cargar que pertenecen al equipo buscado
     * @throws ExcepcionXML excepción al cargar el XML
     */
    public List<Corredor> cargarCorredoresPorEquipo(String rutaFichero, String equipo, TipoValidacion validacion) throws ExcepcionXML {
        CorredorPorEquipoSAXHandler miHandler = new CorredorPorEquipoSAXHandler(equipo);
        XMLSAXUtils.cargarDocumentoXMLSAX(rutaFichero, validacion, miHandler);
        return miHandler.getCorredores();
    }
    
}
