package clasesJAXB;

import java.time.LocalDate;

public class VelocistaJAXB extends CorredorJAXB {
    private float velocidadMedia;

    /**
     * Constructor vacío creado para la manejadora de SAX
     */
    public VelocistaJAXB(){}

    public VelocistaJAXB(String codigo, int dorsal, String nombre, LocalDate fecha, String equipo, float velocidad) {
        super(codigo, dorsal, nombre, fecha, equipo);
        this.velocidadMedia = velocidadMedia;
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
        return sb.toString();
    }

}
