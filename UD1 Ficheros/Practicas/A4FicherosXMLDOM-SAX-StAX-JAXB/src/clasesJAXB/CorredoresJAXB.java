package clasesJAXB;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElements;
import persistenciaJAXB.XMLJAXBUtils;

LO DE AQUI
public class CorredoresJAXB {

    @XmlElements({
            @XmlElement(name = "velocista", type = "velocista.class")
            @XmlElement
    })

    Lista Corredores

            Contructores

    public static leerCorredores(String rutaXML){
        try{
            return XMLJAXBUtils.unmarshall(CorredoresJAXB.class, rutaXML);
        }
        catch
    }
}
