package modelo;

import jakarta.xml.bind.annotation.*;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import utilidades.LocalDateAdapter;

@XmlAccessorType(XmlAccessType.FIELD)
public class Empresa {

    @XmlAttribute(name="puesto")
    private String puesto;

    @XmlValue
    private String empresa;

    public Empresa(String empresa, String puesto) {
        this.empresa = empresa;
        this.puesto = puesto;
    }

    public String getPuesto() {
        return puesto;
    }

    public void setPuesto(String puesto) {
        this.puesto = puesto;
    }

    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    public Empresa() {
    }
}
