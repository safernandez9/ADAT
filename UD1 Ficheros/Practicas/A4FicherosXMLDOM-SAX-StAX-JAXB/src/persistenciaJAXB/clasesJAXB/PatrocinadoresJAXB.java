// Acabado
package persistenciaJAXB.clasesJAXB;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;

import java.util.HashSet;
import java.util.Set;

@XmlAccessorType(XmlAccessType.FIELD)
public class PatrocinadoresJAXB {

    // VARIABLES

    @XmlAttribute(name = "numPatrocinadores", required = true)
    private int numPatrocinadores;

    @XmlElement(name = "patrocinador", required=true)
    private Set<PatrocinadorJAXB> patrocinadores = new HashSet<>();


    // CONSTRUCTORES

    /**
     * Constructor Obligatorio
     */
    public PatrocinadoresJAXB() {}

    /**
     * Contructor propio
     * @param numPatrocinadores
     */
    public PatrocinadoresJAXB(int numPatrocinadores) {
       this.numPatrocinadores = numPatrocinadores;
    }

    // GETTERS Y SETTERS

    public int getNumPatrocinadores() {
        return numPatrocinadores;
    }

    public void setNumPatrocinadores(int numPatrocinadores) {
        this.numPatrocinadores = numPatrocinadores;
    }

    public Set<PatrocinadorJAXB> getPatrocinadores() {
        return patrocinadores;
    }

    public void setPatrocinadores(Set<PatrocinadorJAXB> patrocinadores) {
        this.patrocinadores = patrocinadores;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(PatrocinadorJAXB p : patrocinadores){
            sb.append(p.toString()).append("\n");
        }
        return sb.toString();
    }
}


