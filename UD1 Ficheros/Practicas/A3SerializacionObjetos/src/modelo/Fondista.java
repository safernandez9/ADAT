package modelo;

import java.time.LocalDate;

public class Fondista extends Corredor {

    private static final long serialVersionUID = 1L;
    private float distanciaMax;

    public Fondista(String nombre, LocalDate fechaNacimiento, int equipo, float distanciaMax) {
        super(nombre, fechaNacimiento, equipo);
        this.distanciaMax = distanciaMax;
    }

    // Getters y Setters

    public float getDistanciaMax() {
        return distanciaMax;
    }

    public void setDistanciaMax(float distanciaMax) {
        this.distanciaMax = distanciaMax;
    }

    // toString

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(super.toString());
        sb.append(" | DISTANCIA MAX: ");
        sb.append(String.format("%.3f km", this.distanciaMax));
        return sb.toString();
    }

}
