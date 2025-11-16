package persistenciaSAX;

import clases.Corredor;
import persistenciaDOM.ExcepcionXML;
import persistenciaDOM.TipoValidacion;

import java.util.List;

// COMO DE MAL ESTARIA METER AQUI UN DEFAULTHANDLER GENERAL E IR SOBRESCRIBIENDOLO);


//EJERCICIO LEER LISTADO DE ALGO EN SAX Y ACTUALIZAR OTRO ARCHIVO CON ESTOS DATOS (LETENDO Y ACTUALIAZNDO CON DOM)
public class CorredoresSAX {

    /**
     *
     * Devuelve una List recibida del Handler de corredores a partir de un fichero XML con una manejadora que devuelve
     * todos sus datos en una List de objetos Corredor
     * @param rutaFichero ruta del XML en String
     * @param validacion  Enum con el tipo de validación
     * @return Lista de Corredores en el Handler al cargar
     * @throws ExcepcionXML
     */
    public List<Corredor> cargarCorredores(String rutaFichero, TipoValidacion validacion) throws ExcepcionXML {

        CorredorSAXHandler miHandler = new CorredorSAXHandler();
        XMLSAXUtils.cargarDocumentoXMLSAX(rutaFichero, validacion, miHandler);
        return miHandler.getCorredores();
    }

    
}
