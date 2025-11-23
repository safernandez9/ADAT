package persistenciaSAX;

import clases.Corredor;
import clases.Fondista;
import clases.Puntuacion;
import clases.Velocista;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;
import utilidades.Utilidades;

import javax.xml.namespace.QName;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


/*
    Manejador SAX para procesar corredores desde un documento XML
    Simpre crea una ista con todo el documento en este caso, para leer de otra forma habría que tener otro manejador
 */

public class CorredorSAXHandler extends DefaultHandler {

    // Lista final de corredores procesados
    private List<Corredor> corredores = new ArrayList<>();

    // Corredor que se está construyendo actualmente
    private Corredor corredorActual;

    // Lista temporal para acumular puntuaciones del historial
    private List <Puntuacion> historialActual;
    private Puntuacion p = new Puntuacion();

    // Texto capturado entre etiquetas (Se reinicia en cada startElement)
    private String contenidoActual = "";


    /**
     * Metodo que se ejecuta con el evento startDocument
     */
    @Override
    public void startDocument(){
        System.out.println("CorredorSAX inicio del documento");
    }

    /**
     * Metodo que se ejecuta con el evento startElement cuando se lee un elememto
     *
     * @param uri The Namespace URI, or the empty string if the
     *        element has no Namespace URI or if Namespace
     *        processing is not being performed.
     * @param localName The local name (without prefix), or the
     *        empty string if Namespace processing is not being
     *        performed.
     * @param qName The qualified name (with prefix of the namespace), or the
     *        empty string if qualified names are not available.
     * @param attributes The attributes attached to the element.  If
     *        there are no attributes, it shall be an empty
     *        Attributes object.
     */
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
            switch(qName) {
                case "velocista", "fondista" -> {
                    corredorActual = inicializarCorredorActual(qName, attributes);
                }
                case "nombre", "fecha_nacimiento", "velocidad_media", "distancia_max" -> {
                    contenidoActual = "";
                }
                case "historial" -> {
                    historialActual = new ArrayList<>();
                }
                case "puntuacion" ->{
                    p = new Puntuacion();
                    p.setAnio(Integer.parseInt(attributes.getValue("anio")));
                    contenidoActual = "";
                }
            }
    }

    /**
     * Metodo que se ejecuta con el evento characters cuando se lee un texto entre etiquetas
     * @param ch The characters.
     * @param start The start position in the character array.
     * @param length The number of characters to use from the
     *               character array.
     */
    @Override
    public void characters(char[] ch, int start, int length){
        contenidoActual += new String(ch, start, length);
    }

    /**
     * Metodo que se ejecuta con el evento endElement cuando se lee un la etiqueta final de un elememto
     * @param uri The Namespace URI, or the empty string if the
     *        element has no Namespace URI or if Namespace
     *        processing is not being performed.
     * @param localName The local name (without prefix), or the
     *        empty string if Namespace processing is not being
     *        performed.
     * @param qName The qualified name (with prefix), or the
     *        empty string if qualified names are not available.
     */
    @Override
    public void endElement(String uri, String localName, String qName){
        switch(qName) {
            case "velocista", "fondista" -> {
                corredores.add(corredorActual);
            }
            case "nombre" -> {
                corredorActual.setNombre(contenidoActual);
            }
            case "fecha_nacimiento" -> {
                corredorActual.setFechaNacimiento(LocalDate.parse(contenidoActual));
            }
            case "velocidad_media"-> {
                // Si no entra un Velocista, al añadir el if con el instanceof no guarda pero
                // tampoco peta:
                if(corredorActual instanceof Velocista){
                    String valor = contenidoActual.trim();
                    if(!valor.isEmpty()) {
                        ((Velocista) corredorActual).setVelocidadMedia(Float.parseFloat(contenidoActual));
                    }
                }
            }
            case "distancia_max" -> {
                // Si no entra un Fondista, al añadir el if con el instanceof no guarda pero
                // tampoco peta:
                if(corredorActual instanceof Fondista){
                    ((Fondista) corredorActual).setDistanciaMax(Float.parseFloat(contenidoActual));
                }
            }
            case "historial" -> {
                corredorActual.setHistorial(historialActual);
            }
            case "puntuacion" ->{
                p.setPuntos(Float.parseFloat(contenidoActual));
                historialActual.add(p);
            }
        }
    }

    /**
     * Inicializa los atributos comunes de los Corredores creando un Velocista o un Fondista
     * según el tipo de elemento que se lea en el XML
     * @param qName Nombre completo con NameSpace de un elemento
     * @param attributes Variable que almacena todos los atributos de un elemento. Se rescatan con
     *                   attributes.getValue("nombreAtributo")
     * @return
     */
    private Corredor inicializarCorredorActual(String qName, Attributes attributes) {

        // Los switch con -> tienen una linea y devuelven un valor / objeto / resultado directamente
        // Los switch con -> {} tienen código y devuelven un valor con yield
        // Los switch con : requieren un break para que no se ejecuten todos los case

        Corredor c;

        c = switch (qName) {
            case "velocista" -> new Velocista();
            case "fondista" -> new Fondista();
            default -> throw new IllegalStateException("Valor inesperado al inicializar corredor: " + qName);
        };

        c.setCodigo(attributes.getValue("codigo"));
        c.setDorsal(Integer.parseInt(attributes.getValue("dorsal")));
        c.setEquipo(attributes.getValue("equipo"));

        return c;
    }

    /**
     * Getter de la lista de corredores
     * @return
     */
    public List<Corredor> getCorredores(){
        return corredores;
    }

}
