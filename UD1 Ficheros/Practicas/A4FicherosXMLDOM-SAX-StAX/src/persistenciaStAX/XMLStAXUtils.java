package persistenciaStAX;

import javax.xml.XMLConstants;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;

public class XMLStAXUtils {


    public XMLStreamReader crearReader(String ruta) {
        XMLInputFactory factory = XMLInputFactory.newInstance();
        try {
            // 1. CONFIGURACIONES DE SEGURIDAD
            // a) Desactivar el soporte para entidades externas
            factory.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, Boolean.FALSE );
            // b) Restringir el acceso a DTDs externos y otros recursos (propiedad JAXP estándar)
            // El String vacío significa que no se permite el acceso a URLs externas
            factory.setProperty(XMLConstants.ACCESS_EXTERNAL_DTD,"" );
            // c) Activar el procesamiento seguro: impone límites de recursos y restricciones generales
            factory.setProperty(XMLConstants.FEATURE_SECURE_PROCESSING, Boolean.TRUE );


            // ACTIVAMOS VALIDACIÓN
            // a) Activar la validación del esquema (DTD o XSD)
            factory.setProperty(XMLInputFactory.IS_VALIDATING, Boolean.TRUE);
            // b) Activar el procesamiento seguro (anti-XXE)
            factory.setProperty(XMLConstants.FEATURE_SECURE_PROCESSING,
                    Boolean.TRUE);

            // 3. CREAR el lector (Reader)
            XMLStreamReader reader = factory.createXMLStreamReader(new
                    FileReader(rutaArchivo));
            return reader;
        } catch (FileNotFoundException e) {
            System.err.println("ERROR: Archivo XML no encontrado en la ruta: " +
                    rutaArchivo);
        } catch (IllegalArgumentException e) {
            System.err.println("ERROR: La implementación del Parser no soporta
                    alguna propiedad configurada.");
        } catch (XMLStreamException e) {
            // Captura errores de sintaxis, parseo y validación (si IS_VALIDATING es TRUE)
            System.err.println("ERROR DE PARSEO/VALIDACIÓN en XML: " +
                    e.getMessage());
        }
        return null; // Devuelve null si falla la creación


    }
}
