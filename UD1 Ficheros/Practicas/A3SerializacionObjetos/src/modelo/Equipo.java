package modelo;

import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;


public class Equipo {

    private int idEquipo;
    private String nombre;
    private int numPatrocinadores;      // Para facilitar la lectura
    private boolean borrado = false;
    private Set<Patrocinador> patrocinadores = new HashSet<>();

    public void addPatrocinador(Patrocinador p){
        patrocinadores.add(p);      // Use equals/hashcode para ver la duplicidad por nombre de patrocinador
    }

    /**
     * RELLENAR
     * equals lo otros se refiere a sobreescribir los metodos para decir cuando son iguales
     * @return
     */
    public int bytesSerializacionEquipo(){
        int camposEnterosBytes = Integer.BYTES * 2;     // Id y numpatrocinadores
        int borradoBytes = 1;                           // Boolean
        int contenidoUtf = 0;

        if(this.nombre != null && !this.nombre.isEmpty()){
            // StandardCharsets.UTF_8 garantiza que no se lanza UnsupportedEncodingException
            contenidoUtf += this.nombre.getBytes(StandardCharsets.UTF_8).length;
        }

        // Si se supera el limite de writeUTF (65535 bytes) -> error de validacion unchecked
        if(contenidoUtf > 0xFFFF){
            throw new IllegalArgumentException("Nombre demasiado largo para writeUTF: " + contenidoUtf + " bytes");
        }

        // Aproximamos writeUTF como 2 bytes de prefijo + longitud en bytes UTF-8
        int nombreTotal = 2 + contenidoUtf;
        int bytesPatrocinadores = 0;

        // Sumar bytes de patrocinadores, protegemos contra patrocinadores = null
        if(this.patrocinadores != null){
            for(Patrocinador p : this.patrocinadores){
                bytesPatrocinadores += p.bytesSerializacionPatrocinador();
            }
        }

        return nombreTotal + camposEnterosBytes + borradoBytes + bytesPatrocinadores;

    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("ID: ").append(this.idEquipo)
                .append(" | Nombre: ").append(this.nombre)
                .append("\n");

        int numPat = patrocinadores ==null ? 0 : patrocinadores.size();
        if(numPat > 0){
            sb.append(numPat == 1 ? "Patrocinador:\n" : "Patrocinadores:\n");
            for(Patrocinador p : patrocinadores){
                sb.append(" - ").append(p).append("\n");
            }
        }
        else {
            sb.append("No tiene patrocinadores.\n");
        }
        return sb.toString();
    }

    public int getIdEquipo() {
        return idEquipo;
    }

    public void setIdEquipo(int idEquipo) {
        this.idEquipo = idEquipo;
    }
}
