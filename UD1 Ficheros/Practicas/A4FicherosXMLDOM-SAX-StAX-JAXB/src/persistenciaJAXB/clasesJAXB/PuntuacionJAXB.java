// Acabado
package persistenciaJAXB.clasesJAXB;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlValue;

@XmlAccessorType(XmlAccessType.FIELD)
public class PuntuacionJAXB {

    // VARIABLES

    @XmlAttribute(name = "anio")
    private int anio;

    @XmlValue
    private float puntos;


    // CONSTRUCTORES

    public PuntuacionJAXB() {

    }

    public PuntuacionJAXB(int anio, float puntos) {
        this.anio = anio;
        this.puntos = puntos;
    }


    // GETTERS SETTERS Y toString

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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.anio);
        sb.append("=");
        sb.append(this.puntos);
        return sb.toString();
    }
}
