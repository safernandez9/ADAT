package clasesJAXB;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlValue;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import persistenciaJAXB.utilidades.LocalDateAdapter;
import utilidades.UtilidadesFechas;

import java.time.LocalDate;

@XmlAccessorType(XmlAccessType.FIELD)
public class PatrocinadorJAXB {
    @XmlValue
    private String nombre;

    @XmlAttribute(name = "donacion", required = true)
    private float donacion;

    @XmlAttribute(name = "fecha_inicio", required = true)
    @XmlJavaTypeAdapter(LocalDateAdapter.class)
    private LocalDate fechaInicio;

    // CONSTRUCTOR OBLIGATORIO

    public PatrocinadorJAXB() {}

    // CONSTRUCTORES PROPIOS

    public PatrocinadorJAXB(String nombre, float donacion, LocalDate fechaInicio) {
        this.nombre = nombre;
        this.donacion = donacion;
        this.fechaInicio = fechaInicio;
    }

    public PatrocinadorJAXB(String nombre, float donacion) {
        this.nombre = nombre;
        this.donacion = donacion;
    }

    // GETTERS Y SETTERS

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public float getDonacion() {
        return donacion;
    }

    public void setDonacion(float donacion) {
        this.donacion = donacion;
    }

    public LocalDate getfechaInicio() {
        return fechaInicio;
    }

    public void setfechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    // METODOS OVERRIDE

    @Override
    public String toString() {
        return String.format("%s | Donación: %.2f | Inicio: %s",
                nombre, donacion, UtilidadesFechas.formatearCorto(fechaInicio));
    }
}
