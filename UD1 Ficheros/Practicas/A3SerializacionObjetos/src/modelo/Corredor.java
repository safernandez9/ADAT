package modelo;

import utilidades.UtilidadesFechas;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

/**
 * Clase abstracta para no instanciar directamente un Corredor ya que
 * siempre será fondista o velocista
 */

/*
 * Los campos estaticos no se serializan
 * Los campos transient no se serializan
 *  Si cierras un OOS y luego abres otro sobre el mismo fichero en modo append, el segundo OOS
 *  vuelve a escribir su cabecera.
 *  Al leerCorredor con ObjectInputStream esa cabecera adicional se interpreta como datos inesperados y
 * provoca errores como StreamCorruptedException o fallos al readObject
 */


    // Se serializan padres solo si quiero, ya que también se hereda.
    // El serial version UID se pone en padres e hijos como buena práctica.

public abstract class Corredor implements Serializable {

    private static final long serialVersionUID = 1L; //??
    private int dorsal;
    private String nombre;
    private LocalDate fechaNacimiento;
    private int equipo;
    private List<Puntuacion> historial;

    private transient String campoAExcluirEnSerializavion;

    public Corredor(String nombre, LocalDate fechaNacimiento, int equipo) {
        this.nombre = nombre;
        this.fechaNacimiento = fechaNacimiento;
        this.equipo = equipo;
    }

    // Getters y Setters

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

    /**
     * Establece el historial de puntuaciones del corredor y lo ordena por año
     * @param historial
     */
    public void setHistorial(List<Puntuacion> historial) {

        if(historial != null) {
            historial.sort((p1,p2) -> Integer.compare(p1.getAnio(),p2.getAnio()));
        }

        this.historial = historial;
    }


    // Overrides

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("NOMBRE: ").append(this.nombre);
        sb.append(" | FECHANACIMIENTO: ").append(UtilidadesFechas.formatearLargo(this.fechaNacimiento));
        sb.append(" | EQUIPO: ").append(this.equipo);

        return sb.toString();
    }
}
