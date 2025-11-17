package clases;

import java.util.HashSet;
import java.util.Set;

/**
 * HashSet: Unicidad y rendimiento
 * LinkedHashSet: Determinismo
 * TreeSet: Ordena de según el orden natural del Objeto
 */
public class Equipo {
    private int idEquipo;
    private String nombre;
    private int numPatrocinadores;          //Variable para facilitar la lectura
    private boolean borrado = false;
    private Set <Patrocinador> patrocinadores = new HashSet<>();

    public void addPatrocinador(Patrocinador p) {
        patrocinadores.add(p);
    }

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
