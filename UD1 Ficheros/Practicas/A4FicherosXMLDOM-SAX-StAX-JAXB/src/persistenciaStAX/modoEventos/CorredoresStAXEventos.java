package persistenciaStAX.modoEventos;

import clases.Corredor;
import clases.Fondista;
import clases.Puntuacion;
import clases.Velocista;
import persistenciaDOM.ExcepcionXML;
import persistenciaStAX.modoCursor.XMLStAXUtilsCursor;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.XMLEvent;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

//POR QUE ESTAS CLASES NO PUEDEN SER ESTÁTICAS?

public class CorredoresStAXEventos {

    public List<Corredor> leerCorredores(XMLEventReader reader){

        List<Corredor> corredores = new ArrayList<>();
        Corredor corredorActual = null;
        List<Puntuacion> historialActual = null;
        Puntuacion p = new Puntuacion();
        String anioActual = null;
        String contenidoActual = null;

        try{
            while(reader.hasNext()) {
                XMLEvent event = reader.nextEvent();

                if (event.isStartElement()) {
                    String nombreEtiqueta = XMLStAXUtilsEventos.obtenerNombreEtiqueta(event);
                    switch (nombreEtiqueta) {
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
                            p.setAnio(Integer.parseInt(XMLStAXUtilsEventos.leerAtributo(event,"anio")));

                        }
                        default -> throw new ExcepcionXML("Etiqueta no esperada: " + nombreEtiqueta);
                    }
                }
                if(event.isCharacters()){
                    contenidoActual = XMLStAXUtilsEventos.leerTexto(event);
                }
                if (event.isEndElement()) {
                    String nombreEtiqueta = XMLStAXUtilsEventos.obtenerNombreEtiqueta(event);
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
        } catch (XMLStreamException e) {
            throw new ExcepcionXML("Error al leer los corredores: " + e.getMessage(), e);
        }
        return corredores;
    }


}
