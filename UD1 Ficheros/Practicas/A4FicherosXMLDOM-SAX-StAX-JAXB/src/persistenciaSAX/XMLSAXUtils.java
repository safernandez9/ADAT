package persistenciaSAX;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import persistenciaDOM.ExcepcionXML;
import persistenciaDOM.SimpleErrorHandler;
import persistenciaDOM.TipoValidacion;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.*;

public class XMLSAXUtils {

    /*
     * Manera auxiliar para validar con XSD
     * XMLReader reader = parser.getXMLReader();
     * reader.setFeature("http://xml.org/sax/features/validation", true);
     * reader.setFeature("http://apache.org/xml/features/validation/schema", true);
     */

    /**
     * Carga y lee el documento SAX con una manejadora que recibe.
     * Los datos los guarda la manejadora
     * @param rutaFichero String con la ruta del fichero
     * @param validacion Enum con el tipo de validación
     * @param miHandler Manejadora que voy a utilizar
     */
    public static void cargarDocumentoXMLSAX(String rutaFichero, TipoValidacion validacion, DefaultHandler miHandler) throws ExcepcionXML {

        // Comprobaciones de si la cadena rutaFichero está vacía, si la validacion es null y si el fichero no existe
        if (rutaFichero.isEmpty() || rutaFichero == null) {
            throw new ExcepcionXML("Ruta del fichero XML vacía");
        }
        if(validacion == null){
            throw new ExcepcionXML("Tipo de validación introducido nulo");
        }
        if(!(new File(rutaFichero).exists())){
            throw new ExcepcionXML("El fichero no existe");
        }

        try {
            // Creo y configuro la Factory de SAX
            SAXParserFactory factory = configurarFactory(validacion);

            // La Factory crea el SAXParser
            SAXParser saxParser = factory.newSAXParser();

            // Creo el xmlReader desde el SAXParser, le añado mi manejador y le hago el parse a la ruta.
            XMLReader xmlReader = saxParser.getXMLReader();
            xmlReader.setContentHandler(miHandler);
            xmlReader.setErrorHandler(new SimpleErrorHandler()); // Si quiero manejar errores personalizados

            /**
             * Por el funcionamiento de SAX, la ruta del DTD se coge con base en la raiz del programa.
             * CorredoresDTD.dtd aunque tiene logica para el DOCTYPE SYSTEM "CorredoresDTD.dtd", ya que están en la
             * misma carpeta, el parse busca el DTD en la raiz del proyecto. Para evitarlo pongo este codigo.
             */
            File archivo = new File(rutaFichero);
            InputSource is = new InputSource(new BufferedInputStream(new FileInputStream(archivo)));
            is.setSystemId(new File(rutaFichero).toURI().toASCIIString());

            xmlReader.parse(is);
        } catch (IOException e) {
            throw new ExcepcionXML("Error en la lectura del archivo XML: " + e.getMessage());
        } catch (SAXException | ParserConfigurationException e) {
            throw new ExcepcionXML("Error al cargar el documento XML: " + e.getMessage());
        }

    }

    /**
     * Configuración del SAXParserFactory en función del tipo de validación.
     * Si los archivos XML no viniesen con las declaraciones de su tipo en la cabecera
     * esta configuración se haría en el XMLReader con:
     * DTD:
     *      reader.setFeature("http://xml.org/sax/features/validation", true);
     * XSD:
     *      reader.setFeature("http://xml.org/sax/features/validation", true);
     *      reader.setFeature("http://apache.org/xml/features/validation/schema", true);
     * @param validacion Enum con el tipo de validacion
     * @return
     */
    private static SAXParserFactory configurarFactory(TipoValidacion validacion) {

        SAXParserFactory factory = SAXParserFactory.newInstance();

        // CASO EN EL QUE TENGO LAS DECLARACIONES EN LOS XML
        // <!DOCTYPE Actores SYSTEM "nombre.dtd"> para DTD
        // <Actores xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        // xsi:noNamespaceSchemaLocation="nombre.xsd"> PARA XSD

        switch (validacion) {
            case DTD ->  {
                factory.setValidating(true);
                factory.setNamespaceAware(false);       // NO NECESARIO PARA DTD
            }
            case XSD ->  {
                factory.setValidating(true);
                factory.setNamespaceAware(true);
            }
        }

        return factory;
    }



}


