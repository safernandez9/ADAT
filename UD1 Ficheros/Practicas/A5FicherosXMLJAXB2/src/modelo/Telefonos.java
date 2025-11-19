package modelo;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;

import java.util.List;

public class Telefonos {

    @XmlElementWrapper(name = "Teléfonos")
    @XmlElement(name = "Teléfono")
    private List<Telefono> telefonos;


    //PROPERTI DELANTE DE LOS GET A VECES???
}
