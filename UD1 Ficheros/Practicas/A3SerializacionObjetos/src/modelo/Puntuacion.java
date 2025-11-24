package modelo;

import java.io.Serializable;

public class Puntuacion implements Serializable {

    private static final long serialVersionUID = 1L;
    private int anio;
    private float puntos;

    public Puntuacion(int anio, float puntos) {
        this.anio = anio;
        this.puntos = puntos;
    }

    // Getters y Setters

    public int getAnio() {
        return anio;
    }

    public void setAnio(int anio) {
        this.anio = anio;
    }

    public float getPuntos() {
        return puntos;
    }

    public void setPuntos(float puntos) {
        this.puntos = puntos;
    }

    // toString

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.anio);
        sb.append("=");
        sb.append(this.puntos);
        return sb.toString();
    }
}
