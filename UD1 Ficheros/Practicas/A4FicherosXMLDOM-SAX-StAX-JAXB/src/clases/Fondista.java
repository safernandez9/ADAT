package clases;

import java.time.LocalDate;
import java.util.List;

public class Fondista extends Corredor{

    private float distanciaMax;

    /**
     * Constructor vacío creado para la manejadora de SAX
     */
    public Fondista(){}

    public Fondista(String codigo, int dorsal, String nombre, LocalDate fecha, String equipo, float distancia) {
        super(codigo, dorsal, nombre, fecha, equipo);
        this.distanciaMax = distancia;
    }

    public Fondista(String id, String nombre, LocalDate fecha, String equipo, List<Puntuacion> historial, float distanciaMax) {
        super(id, nombre, fecha, equipo, historial);
        this.distanciaMax = distanciaMax;

    }

    public float getDistanciaMax() {
        return distanciaMax;
    }

    public void setDistanciaMax(float distanciaMax) {
        this.distanciaMax = distanciaMax;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(super.toString());
        sb.append("  |  DISTANCIA MAX:  ");
        sb.append(String.format("%.3f km", this.distanciaMax));
        sb.append(" | HISTORIAL: ");
        if(this.getHistorial() != null && !this.getHistorial().isEmpty()){
            for(Puntuacion p : this.getHistorial()){
                sb.append(" [");
                sb.append(p.toString());
                sb.append("] ");
            }
        } else {
            sb.append("No tiene puntuaciones.");
        }
        return sb.toString();
    }
}
