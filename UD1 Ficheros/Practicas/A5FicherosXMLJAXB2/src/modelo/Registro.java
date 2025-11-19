package modelo;

import jakarta.xml.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "Registro")
@XmlAccessorType(XmlAccessType.FIELD)
public class Registro {



    @XmlElement(name = "Categorias")
    @XmlList
    private List<String> categorias;

    @XmlElementWrapper(name = "Personas")
    @XmlElements({
            @XmlElement(name = "Trabajador", type = Trabajador.class)
            @XmlElement(name = "Estudiante", type = Estudiante.class)
    })
    private List <Persona> listaPersonas;

    public Registro() {
        this.categorias = new ArrayList<String>();
        this.listaPersonas = new ArrayList<>();
    }
}
