package modelo;

import utilidades.UtilidadesFechas;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

/**
 * Clase abstracta para no instanciar directamente un Corredor ya que
 * siempre será fondista o velocista
 */
public class Corredor implements Serializable { //Se implementa serializable para puntuacion por que? en todas las que hacen referencias dice

    private static final long serialVersionUID = 1L; //??
    private int dorsal;
    private String nombre;
    private LocalDate fechaNacimiento;
    private int equipo;
    private List<Puntuacion> historial;

    public Corredor(String nombre, LocalDate fechaNacimiento, int equipo) {
        this.nombre = nombre;
        this.fechaNacimiento = fechaNacimiento;
        this.equipo = equipo;
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

    public int getEquipo() {
        return equipo;
    }

    public void setEquipo(int equipo) {
        this.equipo = equipo;
    }

    public List<Puntuacion> getHistorial() {
        return historial;
    }

    public void setHistorial(List<Puntuacion> historial) {

        // Ordena el historial de puntuaciones por año automáticamente
        if(historial != null) {
            historial.sort((p1,p2) -> Integer.compare(p1.getAnio(),p2.getAnio()));
        }

        // Usando métodos referenciados

        this.historial = historial;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("NOMBRE: ").append(this.nombre);
        sb.append(" | FECHANACIMIENTO: ").append(UtilidadesFechas.formatearLargo(this.fechaNacimiento));
        sb.append(" | EQUIPO: ").append(this.equipo);

        return sb.toString();
    }
}
