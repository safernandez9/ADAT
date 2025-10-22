package modelo;

import utilidades.UtilidadesFechas;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

public class Patrocinador {

    private String nombre;
    private float donacion;
    private LocalDate fechaInicio;


    public int bytesSerializacionPatrocinador() {

        int contenidoUtf = 0;

        if(this.nombre != null && !this.nombre.isEmpty()){
            // StandardCharsets.UTF_8 garantiza que no se lanza UnsupportedEncodingException
            contenidoUtf += this.nombre.getBytes(StandardCharsets.UTF_8).length;
        }

        // Si se supera el límite de writeUTF (65535 bytes) -> error de validacion unchecked
        if(contenidoUtf > 0xFFFF){
            throw new IllegalArgumentException("Nombre demasiado largo para writeUTF: " + contenidoUtf + " bytes");
        }

        // Aproximamos writeUTF como 2 bytes de prefijo + longitud en bytes UTF-8
        int nombreTotal = 2 + contenidoUtf;
        int donacionBytes = Float.BYTES;
        int fechaBytes = Long.BYTES;

        return nombreTotal + donacionBytes + fechaBytes;

    }

    /**
     *
     * @param o   the reference object with which to compare.
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (this == o){
            return true;
        }
        if (!(o instanceof Patrocinador)){
            return false;
        }
        Patrocinador p = (Patrocinador) o;
        return this.nombre != null && this.nombre.equals(p.nombre);
    }

    @Override
    public int hashCode() {
        return nombre == null ? 0 : nombre.toLowerCase().hashCode();
    }

    @Override
    public String toString() {
        return String.format("%s | Donación: %.2f | Inicio: %s", nombre, donacion, UtilidadesFechas.formatearCorto(fechaInicio));
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
