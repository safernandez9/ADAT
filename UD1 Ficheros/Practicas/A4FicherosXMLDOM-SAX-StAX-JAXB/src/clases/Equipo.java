package clases;

import java.util.HashSet;
import java.util.Set;

/**
 * HashSet: Unicidad y rendimiento
 * LinkedHashSet: Determinismo
 * TreeSet: Ordena de según el orden natural del Objeto
 */
public class Equipo {
    private String idEquipo;
    private String nombre;
    private int numPatrocinadores;          //Variable para facilitar la lectura
    private boolean borrado = false;
    private Set <Patrocinador> patrocinadores = new HashSet<>();


    public Equipo(){}

    public Equipo(String id, String nombre) {
        this.idEquipo = id;
        this.nombre = nombre;
    }

    public Equipo(String idEquipo, String nombre, Patrocinador p) {
        this.idEquipo = idEquipo;
        this.nombre = nombre;
        this.patrocinadores.add(p);
        this.numPatrocinadores = this.patrocinadores.size();
    }

    // Getters y Setters

    public String getIdEquipo() {
        return idEquipo;
    }

    public void setIdEquipo(String idEquipo) {
        this.idEquipo = idEquipo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getNumPatrocinadores() {
        return numPatrocinadores;
    }

    public void setNumPatrocinadores(int numPatrocinadores) {
        this.numPatrocinadores = numPatrocinadores;
    }

    public boolean isBorrado() {
        return borrado;
    }

    public void setBorrado(boolean borrado) {
        this.borrado = borrado;
    }

    public Set<Patrocinador> getPatrocinadores() {
        return patrocinadores;
    }

    public void setPatrocinadores(Set<Patrocinador> patrocinadores) {
        this.patrocinadores = patrocinadores;
    }

    public void addPatrocinador(Patrocinador p) {
        patrocinadores.add(p);
    }

    // toString

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ID: ").append(this.idEquipo).append(" | Nombre: ").append(this.nombre).append("\n");
        int numPat = (patrocinadores == null) ? 0 : patrocinadores.size();
        if(numPat > 0) {
            sb.append((numPat == 1) ? "Patrocinador:\n" : "Patrocinadores:\n");
            for(Patrocinador p : patrocinadores) {
                sb.append(" - ").append(p).append("\n");
            }
        }
        else{
            sb.append("No tiene patrocinadores.\n");
        }
        return sb.toString();
    }

}
