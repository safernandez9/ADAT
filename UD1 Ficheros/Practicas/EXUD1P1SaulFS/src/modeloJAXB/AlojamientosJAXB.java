// Saúl Fernández Salgado
package modeloJAXB;

import jakarta.xml.bind.annotation.*;

import java.util.List;

@XmlRootElement(name = "Alojamientos")
@XmlAccessorType(XmlAccessType.FIELD)
public class AlojamientosJAXB {

    public AlojamientosJAXB() {}

    @XmlElements({
            @XmlElement(name = "Habitacion", type = HabitacionJAXB.class),
            @XmlElement(name = "Suite", type = SuiteJAXB.class)
    })
    private List<TipoAlojamientoJAXB> listaPersonas;

}
