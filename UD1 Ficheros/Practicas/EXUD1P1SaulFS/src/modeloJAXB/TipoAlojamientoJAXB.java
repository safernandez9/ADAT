// Saúl Fernández Salgado
package modeloJAXB;

import jakarta.xml.bind.annotation.*;
import modelo.Reserva;

import java.util.List;

@XmlAccessorType(XmlAccessType.NONE)
@XmlSeeAlso({HabitacionJAXB.class, SuiteJAXB.class})
public class TipoAlojamientoJAXB {

    @XmlAttribute(name = "numero")
    private String IDnumero;

    @XmlAttribute(name = "estado")
    private boolean estado;

    @XmlAttribute(name = "precio")
    private double precio;

    @XmlElementWrapper(name = "Reservas")
    @XmlElement(name = "Reserva")
    private List<ReservaJAXB> reservas;

    public TipoAlojamientoJAXB() {}
}
