package persistencia;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import utilidades.ExcepcionXML;
import java.io.File;

public class XMLJAXBUtils {

    /**
     * CAMBIO LA EXCEPCION POR LA NUESTRA?
     *
     * @param objeto
     * @param rutaArchivo
     * @param <T>
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
            System.err.println("JAXBException: " + e.getMessage());
            Throwable linked = e.getLinkedException();
            if(linked != null){
                System.err.println("Linked Exception: " + linked.getMessage());
                linked.printStackTrace();
            }
            else{
                e.printStackTrace();
            }
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
    public static <T> T unmarshall(Class<T> clase, String rutaArchivo) throws JAXBException {

        // Crear el contexto JAXB para la clase indicada
        JAXBContext context = JAXBContext.newInstance(clase);

        // Crear el unmarshaller
        Unmarshaller unmarshaller = context.createUnmarshaller();

        // Leer y convertir el XML a objeto
        return clase.cast(unmarshaller.unmarshal(new File(rutaArchivo)));
    }


}
