package clases;

import utilidades.UtilidadesFechas;

import java.time.LocalDate;

public class Patrocinador {
    private String nombre;
    private float donacion;
    private LocalDate fechaInicio;
    // Variables para actualizaciones SAX
    private String idEquipoPatrocinaActualizacion;
    private String nombreEquipoPatrocinaActualizacion;

    public Patrocinador(String nombre, float donacion, LocalDate fechaInicio, String idEquipo, String nombreEquipo) {
        this.nombre = nombre;
        this.donacion = donacion;
        this.fechaInicio = fechaInicio;
        this.idEquipoPatrocinaActualizacion = idEquipo;
        this.nombreEquipoPatrocinaActualizacion = nombreEquipo;

    }

    public Patrocinador(String nombre, float donacion, LocalDate fechaInicio) {
        this.nombre = nombre;
        this.donacion = donacion;
        this.fechaInicio = fechaInicio;
    }

    // Getters y Setters

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

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getIdEquipoPatrocinaActualizacion() {
        return idEquipoPatrocinaActualizacion;
    }

    public void setIdEquipoPatrocinaActualizacion(String idEquipoPatrocinaActualizacion) {
        this.idEquipoPatrocinaActualizacion = idEquipoPatrocinaActualizacion;
    }

    public String getNombreEquipoPatrocinaActualizacion() {
        return nombreEquipoPatrocinaActualizacion;
    }

    public void setNombreEquipoPatrocinaActualizacion(String nombreEquipoPatrocinaActualizacion) {
        this.nombreEquipoPatrocinaActualizacion = nombreEquipoPatrocinaActualizacion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Patrocinador)) return false;
        Patrocinador that = (Patrocinador) o;
        return nombre != null && nombre.equalsIgnoreCase(that.nombre);
    }

    @Override
    public int hashCode() {
        return nombre == null ? 0 : nombre.toLowerCase().hashCode();
    }

    @Override
    public String toString() {
        return String.format("%s | Donación: %.2f | Inicio: %s",
                nombre, donacion, UtilidadesFechas.formatearCorto(fechaInicio));
    }
}


