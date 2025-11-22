package persistenciaJAXB.clasesJAXB;

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
     * Devuelve un objeto EquiposJAXB ya cargado con los valores de un XML
     * No lo hago estático, ya que puedo querer leer más de un XML de equipos
     * bien formado con distintas instancias
     *
     * @param rutaXML ruta del archivo a leer
     * @return
     * @throws ExcepcionXML
     */
    public static EquiposJAXB leerEquipos(String rutaXML) throws ExcepcionXML {
        try{
            return XMLJAXBUtils.unmarshall(EquiposJAXB.class, rutaXML);
        } catch (ExcepcionXML e){
            throw new ExcepcionXML(e.getMessage(), e);
        }
    }

    // COMO SABE QUE OBJETO ESCRIBIR
    /**
     * Escribe en rutaXML los
     *
     * @param rutaXML
     * @throws ExcepcionXML
     */
    public static void escribirEquipos(String rutaXML) throws ExcepcionXML {
            try{
                //XMLJAXBUtils.marshall(, rutaXML);
            } catch (ExcepcionXML e) {
                throw new ExcepcionXML(e.getMessage(), e);
            }
    }



    // MÉTODOS OVERRIDE

//    @Override
//    public String toString() {
//
//    }

}
