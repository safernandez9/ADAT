package clases;

import java.time.LocalDate;

public class Velocista extends Corredor {
    private float velocidadMedia;

    public Velocista(String codigo, int dorsal, String nombre, LocalDate fecha, String equipo, float velocidad) {
        super(codigo, dorsal, nombre, fecha, equipo);
        this.velocidadMedia = velocidadMedia;
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(super.toString());
        sb.append("  |  VELOCIDAD MEDIA:  ");
        sb.append(String.format("%.2f km/h",this.velocidadMedia));
        return sb.toString();
    }

}
