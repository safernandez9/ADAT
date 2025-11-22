package clases;

import java.time.LocalDate;
import java.util.List;

public class Velocista extends Corredor {
    private float velocidadMedia;

    /**
     * Constructor vacío creado para la manejadora de SAX
     */
    public Velocista(){}

    public Velocista(String codigo, int dorsal, String nombre, LocalDate fecha, String equipo, float velocidad) {
        super(codigo, dorsal, nombre, fecha, equipo);
        this.velocidadMedia = velocidad;
    }

    public Velocista(String id, String nombre, LocalDate fecha, String equipo, List<Puntuacion> historial, float velocidad) {
        super(id, nombre, fecha, equipo, historial);
        this.velocidadMedia = velocidad;
    }

    public float getVelocidadMedia() {
        return velocidadMedia;
    }

    public void setVelocidadMedia(float velocidadMedia) {
        this.velocidadMedia = velocidadMedia;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(super.toString());
        sb.append("  |  VELOCIDAD MEDIA:  ");
        sb.append(String.format("%.2f km/h",this.velocidadMedia));
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
