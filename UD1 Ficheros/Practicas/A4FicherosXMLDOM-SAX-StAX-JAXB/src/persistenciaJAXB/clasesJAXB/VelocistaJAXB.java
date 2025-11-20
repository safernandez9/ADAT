// Acabado
package persistenciaJAXB.clasesJAXB;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;

import java.time.LocalDate;
import java.util.List;


@XmlAccessorType(XmlAccessType.FIELD)
// En este caso que quiero que coja un atributo del padre entre varios del hijo
// (velocidad_media antes que historial) pongo el orden con los atributos de padre
// e hijo en los hijos
@XmlType(propOrder = {"nombre", "fechaNacimiento", "velocidadMedia", "historial"})
public class VelocistaJAXB extends CorredorJAXB {

    // VARIABLES

    @XmlElement(name = "velocidadMedia")
    private float velocidadMedia;


    // CONSTRUCTORES

    public VelocistaJAXB(){}

    public VelocistaJAXB(String codigo, String nombre, LocalDate fecha, String equipo, float velocidad) {
        super(codigo, nombre, fecha, equipo);
        this.velocidadMedia = velocidadMedia;
    }

    public VelocistaJAXB(String codigo, int dorsal, String nombre, LocalDate fecha, String equipo, float velocidad) {
        super(codigo, dorsal, nombre, fecha, equipo);
        this.velocidadMedia = velocidadMedia;
    }

    public VelocistaJAXB(String nombre, LocalDate fecha, String equipo, List<PuntuacionJAXB> historial, float velocidad) {
        super(nombre, fecha, equipo, historial);
        this.velocidadMedia = velocidadMedia;
    }

    // GETTERS SETTERS Y toString

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
