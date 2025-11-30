package persistenciaStAX.modoEventos;

import clases.Corredor;
import clases.Fondista;
import clases.Puntuacion;
import clases.Velocista;
import utilidades.ExcepcionXML;
import utilidades.TipoValidacion;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


public class CorredoresStAXEventos {

    // Constructor vacío
    public CorredoresStAXEventos() {
    }

    public List<Corredor> leerCorredores(XMLEventReader reader) {

        List<Corredor> corredores = new ArrayList<>();
        Corredor corredorActual = null;
        List<Puntuacion> historialActual = null;
        Puntuacion p = new Puntuacion();
        String anioActual = null;
        String contenidoActual = null;

        try {
            while (reader.hasNext()) {
                XMLEvent event = reader.nextEvent();

                if (event.isStartElement()) {
                    String nombreEtiqueta = XMLStAXUtilsEventos.obtenerNombreEtiqueta(event);
                    switch (nombreEtiqueta) {
                        case "corredores" -> {
                            // No hacer nada especial al inicio del contenedor principal
                        }
                        case "velocista", "fondista" -> {
                            corredorActual = nombreEtiqueta.equals("velocista") ? new Velocista() : new Fondista();
                            corredorActual.setCodigo(XMLStAXUtilsEventos.leerAtributo(event, "codigo"));
                            corredorActual.setDorsal(Integer.parseInt(XMLStAXUtilsEventos.leerAtributo(event, "dorsal")));
                            corredorActual.setEquipo(XMLStAXUtilsEventos.leerAtributo(event, "equipo"));
                        }
                        case "nombre", "fecha_nacimiento", "velocidad_media", "distancia_max" -> {
                            contenidoActual = "";
                        }
                        case "historial" -> {
                            historialActual = new ArrayList<>();
                        }
                        case "puntuacion" -> {
                            p = new Puntuacion();
                            p.setAnio(Integer.parseInt(XMLStAXUtilsEventos.leerAtributo(event, "anio")));

                        }
                        default -> throw new ExcepcionXML("Etiqueta no esperada: " + nombreEtiqueta);
                    }
                }
                if (event.isCharacters()) {
                    contenidoActual = XMLStAXUtilsEventos.leerTexto(event);
                }
                if (event.isEndElement()) {
                    String nombreEtiqueta = XMLStAXUtilsEventos.obtenerNombreEtiqueta(event);
                    switch (nombreEtiqueta) {
                        case "corredores" -> {
                            // No hacer nada especial al final del contenedor principal
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
        } catch (XMLStreamException e) {
            throw new ExcepcionXML("Error al leer los corredores: " + e.getMessage(), e);
        }
        return corredores;
    }

    public List<Corredor> cargarTodosCorredoresEventos(String rutaXML, TipoValidacion validacion) throws ExcepcionXML {
        XMLEventReader reader = null;
        try {
            reader = XMLStAXUtilsEventos.cargarDocumentoStAXEventos(rutaXML, validacion);
            return leerCorredores(reader);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (XMLStreamException ignored) {
                }
            }
        }
    }

    /**
     * Lee los corredores de un equipo específico del XML usando StAX en modo eventos.
     *
     * @param reader        XMLEventReader
     * @param equipoBuscado String nombre del equipo a buscar
     * @return List<Corredor> lista de corredores del equipo especificado
     * @throws ExcepcionXML si ocurre un error durante la lectura del XMLa
     */
    public List<Corredor> leerCorredoresPorEquipo(XMLEventReader reader, String equipoBuscado) throws ExcepcionXML {
        List<Corredor> encontrados = new ArrayList<>();
        Corredor corredorActual = null;
        List<Puntuacion> historialActual = null;
        Puntuacion p = new Puntuacion();
        String contenidoActual = null;

        try {
            while (reader.hasNext()) {
                XMLEvent event = reader.nextEvent();

                if (event.isStartElement()) {
                    String nombreEtiqueta = XMLStAXUtilsEventos.obtenerNombreEtiqueta(event);
                    switch (nombreEtiqueta) {
                        case "corredores" -> {
                            // No hacer nada especial al inicio del contenedor principal
                        }
                        case "velocista", "fondista" -> {
                            corredorActual = nombreEtiqueta.equals("velocista") ? new Velocista() : new Fondista();
                            corredorActual.setCodigo(XMLStAXUtilsEventos.leerAtributo(event, "codigo"));
                            corredorActual.setDorsal(Integer.parseInt(XMLStAXUtilsEventos.leerAtributo(event, "dorsal")));
                            corredorActual.setEquipo(XMLStAXUtilsEventos.leerAtributo(event, "equipo"));
                        }
                        case "nombre", "fecha_nacimiento", "velocidad_media", "distancia_max" -> {
                            contenidoActual = "";
                        }
                        case "historial" -> {
                            historialActual = new ArrayList<>();
                        }
                        case "puntuacion" -> {
                            p = new Puntuacion();
                            p.setAnio(Integer.parseInt(XMLStAXUtilsEventos.leerAtributo(event, "anio")));
                        }
                        default -> throw new ExcepcionXML("Etiqueta no esperada: " + nombreEtiqueta);
                    }
                }
                if (event.isCharacters()) {
                    contenidoActual = XMLStAXUtilsEventos.leerTexto(event);
                }
                if (event.isEndElement()) {
                    String nombreEtiqueta = XMLStAXUtilsEventos.obtenerNombreEtiqueta(event);
                    switch (nombreEtiqueta) {
                        case "corredores" -> {
                            // No hacer nada especial al final del contenedor principal
                        }
                        case "velocista", "fondista" -> {
                            if (corredorActual != null && corredorActual.getEquipo() != null &&
                                    corredorActual.getEquipo().equalsIgnoreCase(equipoBuscado)) {
                                encontrados.add(corredorActual);
                            }
                        }
                        case "nombre" -> {
                            if (corredorActual != null) corredorActual.setNombre(contenidoActual);
                        }
                        case "fecha_nacimiento" -> {
                            if (corredorActual != null)
                                corredorActual.setFechaNacimiento(LocalDate.parse(contenidoActual));
                        }
                        case "velocidad_media" -> {
                            if (corredorActual instanceof Velocista) {
                                ((Velocista) corredorActual).setVelocidadMedia(Float.parseFloat(contenidoActual));
                            }
                        }
                        case "distancia_max" -> {
                            if (corredorActual instanceof Fondista) {
                                ((Fondista) corredorActual).setDistanciaMax(Float.parseFloat(contenidoActual));
                            }
                        }
                        case "historial" -> {
                            if (corredorActual != null) corredorActual.setHistorial(historialActual);
                        }
                        case "puntuacion" -> {
                            p.setPuntos(Float.parseFloat(contenidoActual));
                            historialActual.add(p);
                        }
                        default -> throw new ExcepcionXML("Etiqueta no esperada: " + nombreEtiqueta);
                    }
                }
            }
        } catch (XMLStreamException e) {
            throw new ExcepcionXML("Error al leer corredores por equipo (Eventos): " + e.getMessage(), e);
        }
        return encontrados;
    }

    /**
     * Escribe un XML con las donaciones totales de cada patrocinador utilizando StAX Cursor
     *
     * @param rutaSalida     Ruta del archivo XML de salida
     * @param mapaDonaciones Mapa con el nombre del patrocinador como clave y la suma de sus donaciones como valor
     * @throws ExcepcionXML Si ocurre un error durante la escritura del XML
     */
    public void escribirDonaciones(String rutaSalida, Map<String, Double> mapaDonaciones) throws ExcepcionXML {

        int numElementos = mapaDonaciones.size();
        int i = 0;

        XMLEventWriter writer = XMLStAXUtilsEventos.crearWriterStAXEventos(rutaSalida);

        try {

            // Declaración XML
            XMLStAXUtilsEventos.ADDDeclaracion(writer);
            // Elemento raíz <donaciones>
            XMLStAXUtilsEventos.ADDStartElemento(writer, "donaciones");
            // Salto de línea + indent 1
            XMLStAXUtilsEventos.ADDSaltoLinea(writer, 1);

            // Elementos Patrocinador
            for (Map.Entry<String, Double> entry : mapaDonaciones.entrySet()) {

                String nombre = entry.getKey();
                Double total = entry.getValue();

                XMLStAXUtilsEventos.ADDStartElemento(writer, "patrocinador");
                XMLStAXUtilsEventos.ADDAtributo(writer, "totalDonado", String.valueOf(total));
                XMLStAXUtilsEventos.ADDTextoAElemento(writer, nombre);

                XMLStAXUtilsEventos.ADDEndElemento(writer, "patrocinador");

                i++;

                if (i < numElementos) {
                    XMLStAXUtilsEventos.ADDSaltoLinea(writer, 1);
                } else {
                    XMLStAXUtilsEventos.ADDSaltoLinea(writer, 0);
                }


            }

            // Cierre del elemento raíz </donaciones>
            XMLStAXUtilsEventos.ADDEndElemento(writer, "donaciones");


        } catch (ExcepcionXML e) {
            throw new ExcepcionXML("Error al escribir las donaciones: " + e.getMessage(), e);
        } finally {
            try {
                if (writer != null)
                    writer.close();
            } catch (XMLStreamException ignored) {
            }
        }
    }

    public Map<String, Double> leerDonacionesPatrocinadores(XMLEventReader reader) {

        Map<String, Double> mapa = new TreeMap<>();

        String contenidoActual = null;
        String nombreActual = null;
        Double donacionActual = null;

        boolean dentroPatrocinador = false;

        try {
            while (reader.hasNext()) {
                XMLEvent event = reader.nextEvent();

                if (event.isStartElement()) {
                    String nombreEtiqueta = XMLStAXUtilsEventos.obtenerNombreEtiqueta(event);
                    switch (nombreEtiqueta) {

                        case "patrocinador" -> {
                            dentroPatrocinador = true;
                            contenidoActual = "";
                            String valorAttr = XMLStAXUtilsEventos.leerAtributo(event, "donacion");
                            donacionActual = Double.parseDouble(valorAttr);
                        }
                    }
                }

                if (event.isCharacters()) {
                    if (dentroPatrocinador) {
                        contenidoActual = XMLStAXUtilsEventos.leerTexto(event);
                    }
                }

                if (event.isEndElement()) {
                    String nombreEtiqueta = XMLStAXUtilsEventos.obtenerNombreEtiqueta(event);
                    switch (nombreEtiqueta) {

                        case "patrocinador" -> {
                            dentroPatrocinador = false;
                            nombreActual = contenidoActual;
                            mapa.merge(nombreActual, donacionActual, Double::sum);
                        }
                    }
                }
            }

        } catch (XMLStreamException e) {
            throw new ExcepcionXML("Error al leer patrocinadores: " + e.getMessage(), e);
        }

        return mapa;

    }
}
