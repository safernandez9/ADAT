package persistenciaJAXB.clasesJAXB;

import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElements;
import jakarta.xml.bind.annotation.XmlRootElement;
import persistenciaDOM.ExcepcionXML;
import persistenciaJAXB.XMLJAXBUtils;

import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "corredores")
public class CorredoresJAXB {

    @XmlElements({
            @XmlElement(name = "velocista", type = VelocistaJAXB.class),
            @XmlElement(name = "fondista", type = FondistaJAXB.class)
    })
    private List<CorredorJAXB> corredores;

    public CorredoresJAXB() {
        corredores = new ArrayList<CorredorJAXB>();
    }

    /**
     * Devuelve un objeto CorredoresJAXB ya cargado con los valores de un XML
     * No lo hago estático, ya que puedo querer leer más de un XML de corredores
     * bien formado con distintas instancias
     *
     * @param rutaXML ruta del archivo a leer
     * @return
     * @throws ExcepcionXML
     */
    public CorredoresJAXB leerCorredores(String rutaXML) throws ExcepcionXML {
        try{
            return XMLJAXBUtils.unmarshall(CorredoresJAXB.class, rutaXML);
        }
        catch (JAXBException e) {
            throw new ExcepcionXML(e.getMessage(), e);
        }
    }


    // GETTERS Y SETTERS

    public List<CorredorJAXB> getCorredores() {
        return corredores;
    }

    public void setCorredores(List<CorredorJAXB> corredores) {
        this.corredores = corredores;
    }
}
