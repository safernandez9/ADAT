package persistenciaSAX;

import clases.Corredor;
import clases.Fondista;
import clases.Puntuacion;
import clases.Velocista;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Manejador SAX para procesar actualizaciones de corredores desde un documento XML.
 * Crea objetos Velocista o Fondista según los datos leídos y los almacena en una lista.
 * Se hará teniendo en cuenta que es un XML no validado. Es decir, si no esta bien formado, lanzará excepciones.
 */
public class CorredoresActualizacionSAXHandler extends DefaultHandler {

    // Lista final de corredores procesados
    private final List<Corredor> corredores = new ArrayList<>();

    // Corredor temporal
    private Corredor corredorActual;

    // Variables temporales para decidir el tipo
    private String codigoTemporal;
    private String equipoTemporal;
    private String nombreTemp;
    private Float velocidadMediaTemp;
    private Float distanciaMaxTemp;

    // Historial de puntuaciones temporal
    private List<Puntuacion> historialActual;
    private Puntuacion puntuacionTemp;

    // Texto entre etiquetas
    private String contenidoActual = "";


    /**
     * Metodo que se ejecuta con el evento startDocument
     */
    @Override
    public void startDocument() {
        System.out.println("CorredorSAX inicio del documento");
    }

    /**
     * Metodo que se ejecuta con el evento startElement cuando se lee un elememto
     *
     * @param uri        The Namespace URI, or the empty string if the
     *                   element has no Namespace URI or if Namespace
     *                   processing is not being performed.
     * @param localName  The local name (without prefix), or the
     *                   empty string if Namespace processing is not being
     *                   performed.
     * @param qName      The qualified name (with prefix of the namespace), or the
     *                   empty string if qualified names are not available.
     * @param attributes The attributes attached to the element.  If
     *                   there are no attributes, it shall be an empty
     *                   Attributes object.
     */
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        switch (qName) {
            case "corredor" -> {
                // Guardo temporalmente los atributos comunes
                codigoTemporal = attributes.getValue("codigo");
                equipoTemporal = attributes.getValue("equipo");
                // Podrías almacenar estos datos en variables temporales para luego inicializar el objeto correcto
            }
            case "nombre", "velocidad_media", "distancia_max" -> contenidoActual = "";
            case "historial" -> historialActual = new ArrayList<>();
            case "puntuacion" -> {
                puntuacionTemp = new Puntuacion();
                puntuacionTemp.setAnio(Integer.parseInt(attributes.getValue("anio")));
                contenidoActual = "";
            }
            default -> throw new ExcepcionXML("Elemento inesperado en startElement: " + qName);
        }
    }

    /**
     * Metodo que se ejecuta con el evento characters cuando se lee un texto entre etiquetas
     *
     * @param ch     The characters.
     * @param start  The start position in the character array.
     * @param length The number of characters to use from the
     *               character array.
     */
    @Override
    public void characters(char[] ch, int start, int length) {
        contenidoActual += new String(ch, start, length);
    }

    /**
     * Metodo que se ejecuta con el evento endElement cuando se lee un la etiqueta final de un elememto
     *
     * @param uri       The Namespace URI, or the empty string if the
     *                  element has no Namespace URI or if Namespace
     *                  processing is not being performed.
     * @param localName The local name (without prefix), or the
     *                  empty string if Namespace processing is not being
     *                  performed.
     * @param qName     The qualified name (with prefix), or the
     *                  empty string if qualified names are not available.
     */
    @Override
    public void endElement(String uri, String localName, String qName) {
        switch (qName) {
            case "corredor" -> {
                    if (velocidadMediaTemp != null) {
                        corredorActual = new Velocista();
                        ((Velocista) corredorActual).setVelocidadMedia(velocidadMediaTemp);
                    } else if (distanciaMaxTemp != null) {
                        corredorActual = new Fondista();
                        ((Fondista) corredorActual).setDistanciaMax(distanciaMaxTemp);
                    }
                    if(corredorActual != null  && codigoTemporal != null && equipoTemporal != null) {
                        // Asignar los atributos comunes
                        corredorActual.setCodigo(codigoTemporal);
                        corredorActual.setEquipo(equipoTemporal);

                        // Si no hay nombre, asignara null
                        corredorActual.setNombre(nombreTemp);
                        if (historialActual != null) {
                            corredorActual.setHistorial(historialActual);
                        }
                        corredores.add(corredorActual);
                    }

                // Limpiar variables temporales
                    codigoTemporal = null;
                    equipoTemporal = null;
                    velocidadMediaTemp = null;
                    distanciaMaxTemp = null;
                    nombreTemp = null;
                    historialActual = null;
            }
            case "nombre" -> nombreTemp = contenidoActual;
            case "velocidad_media" -> velocidadMediaTemp = Float.parseFloat(contenidoActual);
            case "distancia_max" -> distanciaMaxTemp = Float.parseFloat(contenidoActual);
            case "puntuacion" -> {
                puntuacionTemp.setPuntos(Float.parseFloat(contenidoActual));
                historialActual.add(puntuacionTemp);
            }
            default -> throw new ExcepcionXML("Elemento inesperado en endElement: " + qName);
        }
    }

    /**
     * Getter de la lista de corredores
     *
     * @return
     */
    public List<Corredor> getCorredores() {
        return corredores;
    }

}

