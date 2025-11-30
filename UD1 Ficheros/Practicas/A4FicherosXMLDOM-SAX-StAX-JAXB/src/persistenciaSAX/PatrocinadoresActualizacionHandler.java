package persistenciaSAX;

import clases.*;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;
import utilidades.ExcepcionXML;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Manejador SAX para procesar actualizaciones de equipos desde un documento XML..
 */
public class PatrocinadoresActualizacionHandler extends DefaultHandler {

    // Lista final de corredores procesados
    private final List<Patrocinador> patrocinadores = new ArrayList<>();

    // Patrocinador temporal
    private Patrocinador patrocinadorActual;

    // Variables temporales para decidir el tipo
    private String codigoTemporal;
    private String equipoTemporal;
    private String fechaDonacionTemporal;
    private String cantidadDonacionTemporal;
    private String nombrePatrocinadorTemp;

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
            case "Actualizaciones" -> {
                // No se necesita hacer nada especial al iniciar el documento de actualizaciones
                // se pone para evitar el default
            }
            case "Patrocinador" -> {
                // Guardo temporalmente los atributos
                codigoTemporal = attributes.getValue("idEquipo");
                equipoTemporal = attributes.getValue("nombreEquipo");
            }
            case "nombre" -> contenidoActual = "";

            case "Donacion" -> {
                fechaDonacionTemporal = attributes.getValue("fecha");
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

            case "Actualizaciones" -> {
                // No se necesita hacer nada especial al finalizar el documento de actualizaciones
                // se pone para evitar el default
            }
            case "Patrocinador" -> {
                    if(nombrePatrocinadorTemp != null && !nombrePatrocinadorTemp.isEmpty()
                            && cantidadDonacionTemporal != null && !cantidadDonacionTemporal.isEmpty()
                            && fechaDonacionTemporal != null && !fechaDonacionTemporal.isEmpty()
                            && codigoTemporal != null && !codigoTemporal.isEmpty()
                            && equipoTemporal != null && !equipoTemporal.isEmpty()) {

                        float donacion = Float.parseFloat(cantidadDonacionTemporal);
                        LocalDate fechaInicio = LocalDate.parse(fechaDonacionTemporal);

                        patrocinadorActual = new Patrocinador(nombrePatrocinadorTemp, donacion, fechaInicio, codigoTemporal, equipoTemporal);
                        patrocinadores.add(patrocinadorActual);

                        // Reseteo variables temporales
                        nombrePatrocinadorTemp = null;
                        cantidadDonacionTemporal = null;
                        fechaDonacionTemporal = null;
                        codigoTemporal = null;
                        equipoTemporal = null;

                    } else {
                        System.err.println("Datos incompletos para el patrocinador con idEquipo: " + codigoTemporal);
                    }
            }
            case "nombre" -> nombrePatrocinadorTemp = contenidoActual;
            case "Donacion" ->  cantidadDonacionTemporal = contenidoActual;

            default -> throw new ExcepcionXML("Elemento inesperado en endElement: " + qName);
        }
    }

    /**
     * Getter de la lista de patocinadores procesados
     *
     * @return
     */
    public List<Patrocinador> getPatrocinadores() {
        return patrocinadores;
    }

}

