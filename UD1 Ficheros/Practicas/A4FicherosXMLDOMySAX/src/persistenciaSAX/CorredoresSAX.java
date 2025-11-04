package persistenciaSAX;

import clases.Corredor;
import org.xml.sax.helpers.DefaultHandler;
import persistencia.CorredorXML;
import persistencia.ExcepcionXML;
import persistencia.TipoValidacion;

import java.util.List;

//EJERCICIO LEER LISTADO DE ALGO EN SAX Y ACTUALIZAR OTRO ARCHIVO CON ESTOS DATOS (LETENDO Y ACTUALIAZNDO CON DOM)
public class CorredoresSAX {

    /**
     * Recibe una List de corredores a partir de un fichero XML con una manejadora que devuelve
     * todos sus datos en una List de objetos Corredor
     * @param rutaFichero ruta del XML en String
     * @param validacion  Enum con el tipo de validación
     * @return
     * @throws ExcepcionXML
     */
    public List<Corredor> cargarCorredores(String rutaFichero, TipoValidacion validacion) throws ExcepcionXML {

        CorredorSAXHandler miHandler = new CorredorSAXHandler();
        XMLSAXUtils.cargarDocumentoXMLSAX(rutaFichero, validacion, miHandler);
        return miHandler.getCorredores();
    }
}
