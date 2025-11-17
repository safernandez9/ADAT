package clasesJAXB;

import clases.Equipo;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import persistenciaDOM.ExcepcionXML;
import persistenciaJAXB.XMLJAXBUtils;

import java.util.ArrayList;
import java.util.List;

@XmlRootElement (name = "equipos")
@XmlAccessorType (XmlAccessType.FIELD)
public class EquiposJAXB {

    @XmlElement(name = "equipo", required = true)
    private List<Equipo> equipos = new ArrayList<Equipo>();

    // Constructor vacío obligatorio en JAXB
    public EquiposJAXB() {
    }

    // Constructor normal
    public EquiposJAXB(List<Equipo> equipos) {
        this.equipos = (equipos != null) ? equipos : new ArrayList<>();
    }

    // GETTERS Y SETTERS OBLIGATORIOS

    public List<Equipo> getEquipos() {
        return equipos;
    }

    public void setEquipos(List<Equipo> equipos) {
        this.equipos = equipos;
    }


    //MÉTODOS

    /**
     * Lee el XML y devuelve un objeto Equipos
     * @param rutaXML
     * @return
     */
    public static EquiposJAXB leerEquipos(String rutaXML) {
        try{
            return XMLJAXBUtils.unmarshall(EquiposJAXB.class, rutaXML);
        } catch (JAXBException e){
            throw new ExcepcionXML("");
        }
    }



    // MÉTODOS OVERRIDE

//    @Override
//    public String toString() {
//
//    }

}
