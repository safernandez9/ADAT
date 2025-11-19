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
    private List<EquipoJAXB> equipos = new ArrayList<EquipoJAXB>();

    // Constructor vacío obligatorio en JAXB
    public EquiposJAXB() {
    }

    // Constructor normal
    public EquiposJAXB(List<EquipoJAXB> equipos) {
        this.equipos = (equipos != null) ? equipos : new ArrayList<>();
    }

    // GETTERS Y SETTERS OBLIGATORIOS

    public List<EquipoJAXB> getEquipos() {
        return equipos;
    }

    public void setEquipos(List<EquipoJAXB> equipos) {
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

    public static void escribirEquipos(String rutaXML) throws ExcepcionXML {
            XMLJAXBUtils.marshall(EquiposJAXB.class, rutaXML);
    }



    // MÉTODOS OVERRIDE

//    @Override
//    public String toString() {
//
//    }

}
