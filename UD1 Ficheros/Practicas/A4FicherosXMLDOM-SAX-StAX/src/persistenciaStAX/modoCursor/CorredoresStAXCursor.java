package persistenciaStAX.modoCursor;

import clases.Corredor;
import clases.Fondista;
import clases.Puntuacion;
import clases.Velocista;
import persistenciaDOM.ExcepcionXML;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * FUNCIONALIDAD DE STaX Cursor
 * Funciona por tokens o eventos de distintos tipos. Accedemos a ellos con las constantes de XMLStreamConstants
 * Hay mas tipos pero los mas relevantes son:
 * START_ELEMENT, END_ELEMENT, CHARACTERS, START_DOCUMENT, END_DOCUMENT
 */

public class CorredoresStAXCursor {

    /**
     * Constructor vacío que usará el gestor
     */
    public CorredoresStAXCursor() {
    }

    /**
     * Lee los corredores de un XML utilizando la lectura de StAX con Cursor leyendo los elementos como
     * tokens
     *
     * @param reader Lector para recorrer el XML
     * @return Lista de corredores del XML
     */
    public List<Corredor> leerCorredores(XMLStreamReader reader) throws ExcepcionXML {

        // Lista que irá guardando los corredores
        List<Corredor> corredores = new ArrayList<>();

        // Variables para ir guardando los valores en la lectura
        Corredor corredorActual = null;
        List<Puntuacion> historialActual = null;
        Puntuacion p = new Puntuacion();
        String anioActual = null;
        String contenidoActual = null;

        try {
            // Bucle principal
            while (reader.hasNext()) {
                int tipo = reader.next();
                switch (tipo) {
                    case XMLStreamConstants.START_ELEMENT -> {
                        String nombreEtiqueta = XMLStAXUtilsCursor.obtenerNombreEtiqueta(reader);
                        switch (nombreEtiqueta) {
                            case "velocista", "fondista" -> {
                                // Según el tipo creo un tipo de Corredor u otro
                                corredorActual = nombreEtiqueta.equals("velocista") ? new Velocista() : new Fondista();
                                corredorActual.setCodigo(XMLStAXUtilsCursor.leerAtributo(reader, "codigo"));
                                corredorActual.setDorsal(Integer.parseInt(XMLStAXUtilsCursor.leerAtributo(reader, "dorsal")));
                                corredorActual.setEquipo(XMLStAXUtilsCursor.leerAtributo(reader, "equipo"));
                            }
                            case "nombre", "fecha_nacimiento", "velocidad_media", "distancia_max" -> {
                                contenidoActual = "";
                            }
                            case "historial" -> {
                                historialActual = new ArrayList<>();
                            }
                            case "puntuacion" -> {
                                p = new Puntuacion();
                                p.setAnio(Integer.parseInt(XMLStAXUtilsCursor.leerAtributo(reader,"anio")));
                            }
                            default -> throw new ExcepcionXML("Etiqueta no esperada: " + nombreEtiqueta);
                        }
                    }
                    case XMLStreamConstants.CHARACTERS -> {
                        contenidoActual = XMLStAXUtilsCursor.leerTexto(reader);
                    }
                    case XMLStreamConstants.END_ELEMENT -> {
                        String nombreEtiqueta = XMLStAXUtilsCursor.obtenerNombreEtiqueta(reader);
                        switch (nombreEtiqueta) {
                            case "velocista", "fondista" -> {
                                corredores.add(corredorActual);
                            }
                            case "nombre" -> {
                                corredorActual.setNombre(contenidoActual);
                            }
                            case "fecha_nacimiento" -> {
                                corredorActual.setFechaNacimiento(LocalDate.parse(contenidoActual));
                            }
                            case "velocidad_media" -> {
                                if (corredorActual instanceof Velocista) {
                                    ((Velocista) corredorActual).setVelocidadMedia(Float.parseFloat(contenidoActual));
                                }
                            }
                            case "distancia_max" -> {
                                // Si no entra un Fondista, al añadir el if con el instanceof no guarda pero
                                // tampoco peta:
                                if (corredorActual instanceof Fondista) {
                                    ((Fondista) corredorActual).setDistanciaMax(Float.parseFloat(contenidoActual));
                                }
                            }
                            case "historial" -> {
                                corredorActual.setHistorial(historialActual);
                            }
                            case "puntuacion" -> {
                                p.setPuntos(Float.parseFloat(contenidoActual));
                                historialActual.add(p);
                            }
                            default -> throw new ExcepcionXML("Etiqueta no esperada: " + nombreEtiqueta);
                        }
                    }

                }
            }
        } catch (XMLStreamException e) {
            throw new ExcepcionXML("Error al leer los corredores: " + e.getMessage(), e);
        }

        return corredores;
    }


}