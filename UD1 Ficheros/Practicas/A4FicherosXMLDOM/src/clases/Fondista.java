package clases;

import java.time.LocalDate;

public class Fondista extends Corredor{

    private float distanciaMax;

    public Fondista(String codigo, int dorsal, String nombre, LocalDate fecha, String equipo, float distancia) {
        super(codigo, dorsal, nombre, fecha, equipo);
        this.distanciaMax = distancia;
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(super.toString());
        sb.append("  |  DISTANCIA MAX:  ");
        sb.append(String.format("%.3f km", this.distanciaMax));
        return sb.toString();
    }
}
