package clasesJAXB;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class EquipoJAXB {

    @XmlAttribute(name = "id", required = true)
    private String idEquipo;
    @XmlElement(name = "nombre", required = true)
    private String nombre;

    //NO PUEDO HACERLO CON WRAPPER YA QUE TIENE UN ATRIBUTO, SI CONTUVIERA TEXTO TAMPOCO PODRIA
    //SOLO PUEDO CON ETIQUETAS QUE TRABAJAN UNICAMENTE COMO CAPSULAS <a><b></b></a>
    @XmlElement(name = "patrocinadores", required = true)
    private PatrocinadoresJAXB patrocinadores;

    public EquipoJAXB() {}

    public EquipoJAXB(String idEquipo, String nombre) {
        this.idEquipo = idEquipo;
        this.nombre = nombre;
    }

    public EquipoJAXB(String nombre) {
        this.nombre = nombre;
    }

    public String getIdEquipo() {
        return idEquipo;
    }

    public void setIdEquipo(String idEquipo) {
        this.idEquipo = idEquipo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public PatrocinadoresJAXB getPatrocinadores() {
        return patrocinadores;
    }

    public void setPatrocinadores(PatrocinadoresJAXB patrocinadores) {
        this.patrocinadores = patrocinadores;
    }
}
