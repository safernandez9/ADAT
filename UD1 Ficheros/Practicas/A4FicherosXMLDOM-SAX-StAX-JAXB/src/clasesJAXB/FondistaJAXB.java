package clasesJAXB;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;

import java.time.LocalDate;

// LISTO

@XmlAccessorType(XmlAccessType.FIELD)
// Opcional lo coge por defecto. Sirve para ordenar los elementos en el XML
@XmlType(propOrder = {"nombre", "fechaNacimiento", "distanciaMax", "historial"})
public class FondistaJAXB extends CorredorJAXB {

    @XmlElement(name = "distanciaMax")
    private float distanciaMax;

    public FondistaJAXB(){}

    public FondistaJAXB(String codigo, String nombre, LocalDate fechaNacimiento, String equipo, float distancia){

    }

    public FondistaJAXB(String codigo, int dorsal, String nombre, LocalDate fecha, String equipo, float distancia) {
        super(codigo, dorsal, nombre, fecha, equipo);
        this.distanciaMax = distancia;
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
        return sb.toString();
    }
}
