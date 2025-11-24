package modelo;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;

import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
public class Telefonos extends Contacto{

    // No va en Persona por que es opcional
    @XmlElement(name = "Telefono")
    private List<Telefono> telefonos;

    public Telefonos() {
        telefonos = new ArrayList<Telefono>();
    }

    public List<Telefono> getTelefonos() {
        return telefonos;
    }

    public void setTelefonos(List<Telefono> telefonos) {
        this.telefonos = telefonos;
    }
}
