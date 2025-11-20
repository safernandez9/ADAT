package persistenciaJAXB.clasesJAXB;

import jakarta.xml.bind.annotation.*;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import persistenciaJAXB.utilidades.LocalDateAdapter;
import utilidades.UtilidadesFechas;

import java.time.LocalDate;
import java.util.List;
// LISTO

@XmlAccessorType(XmlAccessType.FIELD)
@XmlSeeAlso({VelocistaJAXB.class, FondistaJAXB.class})
@XmlTransient // Se asegura de que Corredor no se genera como etiqueta propia
public abstract class CorredorJAXB {

    // Variables

    @XmlAttribute(name = "codigo", required = true)
    private String codigo;
    @XmlElement(name = "dorsal", required = true)
    private int dorsal;
    @XmlAttribute(name = "equipo", required = true)
    private String equipo;

    @XmlElement(name = "nombre")
    private String nombre;

    @XmlElement(name = "fecha_nacimiento")
    @XmlJavaTypeAdapter(LocalDateAdapter.class)
    private LocalDate fechaNacimiento;

    // 1. Etiqueta contenedora de la lista. <historial>
    @XmlElementWrapper(name = "historial")
    // 2. Nombre de cada elemento repetido. <puntuacion>
    @XmlElement(name = "puntuacion")
    private List<PuntuacionJAXB> historial;

    // Constructores

    /**
     * Constructor vacío obligatorio en JAXB
     */
    public CorredorJAXB(){}

    public CorredorJAXB(String nombre, LocalDate fechaNacimiento, String equipo) {
        this.nombre = nombre;
        this.fechaNacimiento = fechaNacimiento;
        this.equipo = equipo;
    }

    public CorredorJAXB(String codigo, int dorsal, String nombre, LocalDate fecha, String equipo) {
        this.codigo = codigo;
        this.dorsal = dorsal;
        this.nombre = nombre;
        this.fechaNacimiento = fecha;
        this.equipo = equipo;
    }

    public CorredorJAXB(String codigo, String nombre, LocalDate fecha, String equipo) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.fechaNacimiento = fecha;
        this.equipo = equipo;
    }

    public CorredorJAXB(String nombre, LocalDate fecha, String equipo, List <PuntuacionJAXB> historial) {
        this.nombre = nombre;
        this.fechaNacimiento = fecha;
        this.equipo = equipo;
       this.historial = historial;
    }

    // Getters y Setters


    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public int getDorsal() {
        return dorsal;
    }

    public void setDorsal(int dorsal) {
        this.dorsal = dorsal;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEquipo() {
        return equipo;
    }

    public void setEquipo(String equipo) {
        this.equipo = equipo;
    }

    public List<PuntuacionJAXB> getHistorial() {
        return historial;
    }

    public void setHistorial(List<PuntuacionJAXB> historial) {

        // Ordena el historial de puntuaciones por año automáticamente
        if(historial != null) {
            historial.sort((p1,p2) -> Integer.compare(p1.getAnio(),p2.getAnio()));
        }

        // Usando métodos referenciados

        this.historial = historial;
    }


    // Otros métodos

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("NOMBRE: ").append(this.nombre);
        sb.append(" | FECHANACIMIENTO: ").append(UtilidadesFechas.formatearLargo(this.fechaNacimiento));
        sb.append(" | EQUIPO: ").append(this.equipo);

        return sb.toString();
    }
}
