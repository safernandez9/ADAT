package modelo;

import jakarta.xml.bind.annotation.*;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import utilidades.LocalDateAdapter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

// @XmlAccessorType(XmlAccessType.FIELD) mapea todos los atributos de la clase independientemente de su visibilidad, no usa los getters.
@XmlRootElement(name = "Registro")
@XmlAccessorType(XmlAccessType.FIELD)
public class Registro {

    @XmlAttribute(name = "version")
    private String version;

    @XmlAttribute(name = "fechaCreacion")
    @XmlJavaTypeAdapter(LocalDateAdapter.class)
    private LocalDate fechaCreacion;

    // @XMLList para que se listen: <> a b c d <\>
    @XmlElement(name = "Categorias")
    @XmlList
    private List<String> categorias;

    // Envuelve la lista dentro de un solo elemento XML llamado <Personas> ... </Personas>.
    @XmlElementWrapper(name = "Personas")
    // Permite definir subtipos concretos que pueden aparecer dentro de la lista.
    @XmlElements({
            @XmlElement(name = "Trabajador", type = Trabajador.class),
            @XmlElement(name = "Estudiante", type = Estudiante.class)
    })
    private List <Persona> listaPersonas;

    public Registro() {
        this.categorias = new ArrayList<String>();
        this.listaPersonas = new ArrayList<>();
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public LocalDate getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDate fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public List<String> getCategorias() {
        return categorias;
    }

    public void setCategorias(List<String> categorias) {
        this.categorias = categorias;
    }

    public List<Persona> getListaPersonas() {
        return listaPersonas;
    }

    public void setListaPersonas(List<Persona> listaPersonas) {
        this.listaPersonas = listaPersonas;
    }
}
