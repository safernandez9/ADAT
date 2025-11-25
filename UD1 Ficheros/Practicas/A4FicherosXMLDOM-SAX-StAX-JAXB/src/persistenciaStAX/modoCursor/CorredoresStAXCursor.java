package persistenciaStAX.modoCursor;

import clases.Corredor;
import clases.Fondista;
import clases.Puntuacion;
import clases.Velocista;
import persistenciaDOM.ExcepcionXML;
import persistenciaDOM.TipoValidacion;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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
                            case "corredores" -> {
                                // No hacemos nada al abrir el elemento raíz
                            }
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
                                p.setAnio(Integer.parseInt(XMLStAXUtilsCursor.leerAtributo(reader, "anio")));
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
                            case "corredores" -> {
                                // No hacemos nada al cerrar el elemento raíz
                            }
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

    // INCOMPLETO: Método para leer corredores por equipo
    public List<Corredor> leerCorredoresPorEquipo(XMLStreamReader reader, String equipoBuscado) throws Exception {

        List<Corredor> lista = new ArrayList<>();

        Corredor corredorActual = null;
        List<Puntuacion> historialActual = null;
        Puntuacion puntuacion = null;
        String contenidoActual = null;

        while (reader.hasNext()) {
            int tipo = reader.next();


            switch (tipo) {
                case XMLStreamReader.START_ELEMENT -> {

                    String nombreEtiqueta = XMLStAXUtilsCursor.obtenerNombreEtiqueta(reader);

                    switch (nombreEtiqueta) {
                        case "velocista", "fondista" -> {
                            historialActual = new ArrayList<>();
                            corredorActual = nombreEtiqueta.equals("velocista") ? new Velocista() : new Fondista();

                            corredorActual.setCodigo(
                                    XMLStAXUtilsCursor.leerAtributo(reader, "codigo")
                            );
                            corredorActual.setDorsal(Integer.parseInt(
                                    XMLStAXUtilsCursor.leerAtributo(reader, "dorsal")
                            ));
                            corredorActual.setEquipo(
                                    XMLStAXUtilsCursor.leerAtributo(reader, "equipo")
                            );
                        }
                        case "puntuacion" -> {
                            puntuacion = new Puntuacion();
                            puntuacion.setAnio(Integer.parseInt(
                                    XMLStAXUtilsCursor.leerAtributo(reader, "anio")
                            ));
                        }
                    }
                }


                case XMLStreamReader.CHARACTERS -> {
                    String txt = XMLStAXUtilsCursor.leerTexto(reader);
                    if (!txt.isBlank()) contenidoActual = txt;
                }


                case XMLStreamReader.END_ELEMENT -> {
                    String nombreEtiqueta = XMLStAXUtilsCursor.obtenerNombreEtiqueta(reader);
                    switch (nombreEtiqueta) {

                        case "velocista", "fondista" -> {
                            if (corredorActual.getEquipo().equals(equipoBuscado)) {
                                lista.add(corredorActual);
                            }
                        }

                        case "puntuacion" -> {
                            puntuacion.setPuntos(Float.parseFloat(contenidoActual));
                            historialActual.add(puntuacion);
                        }

                        case "historial" -> corredorActual.setHistorial(historialActual);

                        case "nombre" -> corredorActual.setNombre(contenidoActual);

                        case "fecha_nacimiento" -> corredorActual.setFechaNacimiento(LocalDate.parse(contenidoActual));

                        case "velocidad_media" -> {
                            if (corredorActual instanceof Velocista v) {
                                v.setVelocidadMedia(Float.parseFloat(contenidoActual));
                            }
                        }

                        case "distancia_max" -> {
                            if (corredorActual instanceof Fondista f) {
                                f.setDistanciaMax(Float.parseFloat(contenidoActual));
                            }
                        }
                    }
                }
            }
        }

        return lista;
    }


    /**
     * Calcula el total de donaciones por patrocinador desde un XML usando modelo Cursor
     *
     * @param rutaEntrada ruta del fichero XML
     * @return TreeMap con clave=NombrePatrocinador y valor=totalDonado
     * @throws ExcepcionXML
     */
    public static Map<String, Double> calcularDonaciones(String rutaEntrada) throws ExcepcionXML {
        XMLStreamReader reader = null;
        Map<String, Double> mapaDonaciones = new TreeMap<>();
        try {
            reader = XMLStAXUtilsCursor.cargarDocumentoStAXCursor(rutaEntrada, persistenciaDOM.TipoValidacion.XSD);

            String nombrePatrocinador = "";
            Double donacionActual = 0.0;
            boolean esPatrocinador = false;

            while (reader.hasNext()) {
                int tipo = reader.next();

                switch (tipo) {
                    case XMLStreamConstants.START_ELEMENT -> {
                        String nombreEtiqueta = XMLStAXUtilsCursor.obtenerNombreEtiqueta(reader);
                        if ("Patrocinador".equals(nombreEtiqueta)) {
                            nombrePatrocinador = "";
                            String don = XMLStAXUtilsCursor.leerAtributo(reader, "donacion");
                            donacionActual = don != null ? Double.parseDouble(don) : 0.0;
                            esPatrocinador = true;
                        }
                    }
                    case XMLStreamConstants.CHARACTERS -> {
                        if (esPatrocinador) {
                            nombrePatrocinador += XMLStAXUtilsCursor.leerTexto(reader);
                        }
                    }
                    case XMLStreamConstants.END_ELEMENT -> {
                        String nombreEtiqueta = XMLStAXUtilsCursor.obtenerNombreEtiqueta(reader);
                        if ("Patrocinador".equals(nombreEtiqueta)) {
                            esPatrocinador = false;
                            nombrePatrocinador = nombrePatrocinador.trim();
                            mapaDonaciones.merge(nombrePatrocinador, donacionActual, Double::sum);
                            // Si el patrocinador ya existe, suma la donación actual a la existente
                            // Si no existe, la añade con la donación actual
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new ExcepcionXML("Error al calcular donaciones: " + e.getMessage(), e);
        } finally {
            try {
                if (reader != null) reader.close();
            } catch (XMLStreamException e) {
                // Ignorar cierre
            }
        }

        return mapaDonaciones;
    }


}