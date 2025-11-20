// Acabado
package persistenciaJAXB;

import jakarta.xml.bind.*;
import persistenciaDOM.ExcepcionXML;

import java.io.File;



public class XMLJAXBUtils {

    /**
     * Hace el marshall del Objeto raiz en memoria al documento XML rutaArchivo
     *
     * @param objeto Objeto raiz a introducir en el documento XML
     * @param rutaArchivo Ruta del archivo XML donde quiero escribir
     * @param <T> Acepta cualquier objeto
     * @throws ExcepcionXML
     */
    public static <T> void marshall(T objeto, String rutaArchivo) throws ExcepcionXML {
        try{
            // Crear contexto JAXB
            JAXBContext context = JAXBContext.newInstance(objeto.getClass());

            // Crear marshaller
            Marshaller marshaller = context.createMarshaller();

            // Propiedades comunes
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            // Con esquema XSD
            // marshaller.setProperty(Marshaller.JAXB_SCHEMA_LOCATION, "XMLPuntos.xsd");

            marshaller.marshal(objeto, new File(rutaArchivo));
        } catch (JAXBException e) {
            throw new ExcepcionXML("Error al marshall el archivo: " + rutaArchivo, e);
        }
    }

    /**
     * Deserializa (unmarshall) un XML a un objeto de la clase indicada
     *
     * @param clase Clase del objeto a deserializar
     * @param rutaArchivo Ruta del archivo XML de entrada
     * @return Objeto de tipo T
     * @param <T>
     * @throws JAXBException si ocurre un error de JAXB
     */
    public static <T> T unmarshall(Class<T> clase, String rutaArchivo) throws ExcepcionXML {

        try {
            // Crear el contexto JAXB para la clase indicada
            JAXBContext context = JAXBContext.newInstance(clase);

            // Crear el unmarshaller
            Unmarshaller unmarshaller = context.createUnmarshaller();

            // Leer y convertir el XML a objeto
            return clase.cast(unmarshaller.unmarshal(new File(rutaArchivo)));
        } catch (JAXBException e) {
            throw  new ExcepcionXML("Error al unmarshall el archivo: " + rutaArchivo, e);
        }
    }


}
