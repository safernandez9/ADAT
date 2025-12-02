package persistenciaStAX.modoCursor;

import clases.*;
import utilidades.ExcepcionXML;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
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

    /**
     * Lee los corredores de un XML utilizando la lectura de StAX con Cursor leyendo los elementos como
     * tokens, pero solo los que pertenecen a un equipo concreto
     *
     * @param reader        Lector para recorrer el XML
     * @param equipoBuscado Nombre del equipo a buscar
     * @return Lista de corredores del XML que pertenecen al equipo buscado
     * @throws ExcepcionXML Si ocurre un error durante la lectura del XML
     */
    public List<Corredor> leerCorredoresPorEquipo(XMLStreamReader reader, String equipoBuscado) throws ExcepcionXML {

        List<Corredor> lista = new ArrayList<>();

        Corredor corredorActual = null;
        List<Puntuacion> historialActual = null;
        Puntuacion puntuacion = null;
        String contenidoActual = null;

        try {
            while (true) {

                if (!reader.hasNext()) break;

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
                            case "fecha_nacimiento" ->
                                    corredorActual.setFechaNacimiento(LocalDate.parse(contenidoActual));
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
        } catch (XMLStreamException e) {
            throw new ExcepcionXML("Error al leer los corredores por equipo StAX: " + e.getMessage(), e);
        }
        return lista;
    }

    public List<Patrocinador> cargarPatrocinadoresActualizacion(XMLStreamReader reader) throws ExcepcionXML {

        // Lista final de corredores procesados
        List<Patrocinador> patrocinadores = new ArrayList<>();

        // Patrocinador temporal
        Patrocinador patrocinadorActual = null;

        // Variables temporales para decidir el tipo
        String codigoTemporal = null;
        String equipoTemporal = null;
        String fechaDonacionTemporal = null;
        String cantidadDonacionTemporal = null;
        String nombrePatrocinadorTemp = null;

        // Texto entre etiquetas
        String contenidoActual = "";


        try {
            // Bucle principal
            while (reader.hasNext()) {
                int tipo = reader.next();
                switch (tipo) {
                    case XMLStreamConstants.START_ELEMENT -> {
                        String nombreEtiqueta = XMLStAXUtilsCursor.obtenerNombreEtiqueta(reader);
                        switch (nombreEtiqueta) {
                            case "Actualizaciones" -> {
                                // No se necesita hacer nada especial al iniciar el documento de actualizaciones
                                // se pone para evitar el default
                            }
                            case "Patrocinador" -> {
                                // Guardo temporalmente los atributos
                                codigoTemporal = XMLStAXUtilsCursor.leerAtributo(reader, "idEquipo");
                                equipoTemporal = XMLStAXUtilsCursor.leerAtributo(reader, "nombreEquipo");
                            }
                            case "nombre" -> contenidoActual = "";

                            case "Donacion" -> {
                                fechaDonacionTemporal = XMLStAXUtilsCursor.leerAtributo(reader, "fecha");
                                contenidoActual = "";
                            }

                            default -> throw new ExcepcionXML("Elemento inesperado en startElement: " + nombreEtiqueta);
                        }

                    }
                    case XMLStreamConstants.CHARACTERS -> {
                        contenidoActual += XMLStAXUtilsCursor.leerTexto(reader);
                    }
                    case XMLStreamConstants.END_ELEMENT -> {
                        String nombreEtiqueta = XMLStAXUtilsCursor.obtenerNombreEtiqueta(reader);
                        switch (nombreEtiqueta) {
                            case "Actualizaciones" -> {
                                // No se necesita hacer nada especial al finalizar el documento de actualizaciones
                                // se pone para evitar el default
                            }
                            case "Patrocinador" -> {
                                if (nombrePatrocinadorTemp != null && !nombrePatrocinadorTemp.isEmpty()
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
                            case "Donacion" -> cantidadDonacionTemporal = contenidoActual;
                            default -> throw new ExcepcionXML("Elemento inesperado en endElement: " + nombreEtiqueta);
                        }
                    }
                }
            }
        } catch (XMLStreamException e) {
            throw new ExcepcionXML("Error al leer el fichero de actualización de patrocinadores: " + e.getMessage(), e);
        }
        return patrocinadores;
    }

    /**
     * Lee las donaciones de los patrocinadores desde un XML utilizando StAX Cursor
     *
     * @param reader Lector para recorrer el XML
     * @return Mapa con el nombre del patrocinador como clave y la suma de sus donaciones como valor
     * @throws ExcepcionXML Si ocurre un error durante la lectura del XML
     */
    public Map<String, Double> leerDonacionesPatrocinadores(XMLStreamReader reader) throws ExcepcionXML {

        // Mapa resultado
        Map<String, Double> mapa = new TreeMap<>();

        String contenidoActual = "";
        String nombrePatrocinadorActual = null;
        Double donacionActual = null;

        // Flag para saber si estamos dentro de un patrocinador (para el case CHARACTERS)
        boolean dentroPatrocinador = false;

        try {
            while (reader.hasNext()) {

                int tipo = reader.next();
                switch (tipo) {
                    case XMLStreamReader.START_ELEMENT -> {

                        String etiqueta = XMLStAXUtilsCursor.obtenerNombreEtiqueta(reader);
                        switch (etiqueta) {
                            case "patrocinador" -> {
                                dentroPatrocinador = true;

                                // Leer atributo donacion
                                String don = XMLStAXUtilsCursor.leerAtributo(reader, "donacion");
                                donacionActual = Double.parseDouble(don);

                                contenidoActual = "";
                            }
                        }
                    }

                    case XMLStreamReader.CHARACTERS -> {
                        if (dentroPatrocinador) {
                            contenidoActual += XMLStAXUtilsCursor.leerTexto(reader);
                        }
                    }

                    case XMLStreamReader.END_ELEMENT -> {

                        String etiqueta = XMLStAXUtilsCursor.obtenerNombreEtiqueta(reader);

                        switch (etiqueta) {
                            case "patrocinador" -> {
                                dentroPatrocinador = false;

                                // El texto entre etiquetas es el nombre del patrocinador
                                nombrePatrocinadorActual = contenidoActual.trim();

                                // Si ya existe → sumar; si no → insertar
                                mapa.merge(nombrePatrocinadorActual, donacionActual, Double::sum);
                            }


                        }
                    }
                }
            }

        } catch (XMLStreamException e) {
            throw new ExcepcionXML("Error al leer patrocinadores: " + e.getMessage(), e);
        }

        return mapa;
    }

    /**
     * Escribe un XML con las donaciones totales de cada patrocinador utilizando StAX Cursor
     *
     * @param rutaSalida      Ruta del archivo XML de salida
     * @param mapaDonaciones Mapa con el nombre del patrocinador como clave y la suma de sus donaciones como valor
     * @throws ExcepcionXML Si ocurre un error durante la escritura del XML
     */
    public void escribirDonaciones(String rutaSalida, Map<String, Double> mapaDonaciones) throws ExcepcionXML {

        int numElementos = mapaDonaciones.size();
        int i = 0;

        XMLStreamWriter writer = XMLStAXUtilsCursor.crearWriterStAX(rutaSalida);

        try {

            // Declaración XML
            XMLStAXUtilsCursor.ADDDeclaracion(writer);
            // Elemento raíz <donaciones>
            XMLStAXUtilsCursor.ADDStartElemento(writer, "donaciones");
            // Salto de línea + indent 1
            XMLStAXUtilsCursor.ADDSaltoLinea(writer, 1);

            // Elementos Patrocinador
            for (Map.Entry<String, Double> entry : mapaDonaciones.entrySet()) {

                String nombre = entry.getKey();
                Double total = entry.getValue();

                XMLStAXUtilsCursor.ADDStartElemento(writer, "patrocinador");
                XMLStAXUtilsCursor.ADDAtributo(writer, "totalDonado", String.valueOf(total));
                XMLStAXUtilsCursor.ADDTextoAElemento(writer, nombre);

                XMLStAXUtilsCursor.ADDEndElemento(writer);

                i++;

                if(i<numElementos){
                    XMLStAXUtilsCursor.ADDSaltoLinea(writer, 1);
                }
                else{
                    XMLStAXUtilsCursor.ADDSaltoLinea(writer, 0);
                }


            }

            // Cierre del elemento raíz </donaciones>
            writer.writeEndElement();

            // Fin del documento
            writer.writeEndDocument();

        } catch (XMLStreamException e) {
            throw new ExcepcionXML("Error escribiendo donaciones XML", e);

        } finally {
            try {
                if (writer != null)
                    writer.close();
            } catch (XMLStreamException ignored) {}
        }
    }

}