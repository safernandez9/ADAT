package clases;

import java.time.LocalDate;
import java.util.List;
import utilidades.UtilidadesFechas;

public class Corredor{

    // Variables

    private String codigo;
    private int dorsal;
    private String nombre;
    private LocalDate fechaNacimiento;
    private String equipo;
    private List<Puntuacion> historial;

    // Constructores

    /**
     * Constructor vacío creado para la manejadora de SAX
     */
    public Corredor(){}

    public Corredor(String nombre, LocalDate fechaNacimiento, String equipo) {
        this.nombre = nombre;
        this.fechaNacimiento = fechaNacimiento;
        this.equipo = equipo;
    }

    public Corredor(String codigo, int dorsal, String nombre, LocalDate fecha, String equipo) {
        this.codigo = codigo;
        this.dorsal = dorsal;
        this.nombre = nombre;
        this.fechaNacimiento = fecha;
        this.equipo = equipo;
    }

    public Corredor(String codigo, String nombre, LocalDate fecha, String equipo, List<Puntuacion> historial) {
        this.codigo = codigo;
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


    // Otros métodos

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("NOMBRE: ").append(this.nombre);
        sb.append(" | DORSAL: ").append(this.dorsal);
        sb.append(" | CÓDIGO: ").append(this.codigo);
        sb.append(" | FECHANACIMIENTO: ").append(UtilidadesFechas.formatearLargo(this.fechaNacimiento));
        sb.append(" | EQUIPO: ").append(this.equipo);

        return sb.toString();
    }
}
