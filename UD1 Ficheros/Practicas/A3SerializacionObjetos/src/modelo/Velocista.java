package modelo;

import java.time.LocalDate;

public class Velocista extends Corredor{

    private static final long serialVersionUID = 1L;
    float velocidadMedia;

    public Velocista(String nombre, LocalDate fechaNacimiento, int equipo, float velocidadMedia) {
        super(nombre, fechaNacimiento, equipo);
        this.velocidadMedia = velocidadMedia;
    }

    // Getters y Setters

    public float getVelocidadMedia() {
        return velocidadMedia;
    }

    public void setVelocidadMedia(float velocidadMedia) {
        this.velocidadMedia = velocidadMedia;
    }

    // toString

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(super.toString());
        sb.append("  |  VELOCIDAD MEDIA:  ");
        sb.append(String.format("%.2f km/h",this.velocidadMedia));
        return sb.toString();
    }
}
