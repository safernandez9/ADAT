package persistenciaStAX.modoEventos;

import clases.Corredor;
import clases.Fondista;
import clases.Puntuacion;
import clases.Velocista;
import persistenciaStAX.modoCursor.XMLStAXUtilsCursor;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.XMLEvent;
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
            //Checkear esto

            while(reader.hasNext()) {
                XMLEvent event = reader.nextEvent();
                // int tipo reader.next()

                if (event.isStartElement()) {
                    String nombreEtiqueta = XMLStAXUtilsEventos.obtenerNombreEtiqueta(event);
                    switch (nombreEtiqueta) {
                        case "velocista", "fondista" -> {
                            corredorActual = nombreEtiqueta.equals("velocista") ? new Velocista() : new Fondista();
                            corredorActual.setCodigo(XMLStAXUtilsEventos.leerAtributo(event, "codigo"));
                            corredorActual.setDorsal(Integer.parseInt(XMLStAXUtilsEventos.leerAtributo(event, "dorsal")));
                            corredorActual.setEquipo(XMLStAXUtilsEventos.leerAtributo(event, "equipo"));
                        }
                        case "historial" -> {
                            historialActual = new ArrayList<>();
                        }
                        case "puntuacion" -> {
                            p = new Puntuacion();
                            p.setAnio(Integer.parseInt(attributes.getValue("anio")));
                        }
                    }
                }
                if (event.isEndElement()) {
                }
                if(event.isCharacters()){
                }

            }
            }
            }
}
