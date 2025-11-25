package modelo;

import utilidades.UtilidadesFechas;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

public class Patrocinador {

    private String nombre;
    private float donacion;
    private LocalDate fechaInicio;

    public Patrocinador(String nombre, float donacion, LocalDate fechaInicio) {
        this.nombre = nombre;
        this.donacion = donacion;
        this.fechaInicio = fechaInicio;
    }

// Getters y Setters


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

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    // Calculo bytes serializacion

    public int bytesSerializacionPatrocinador() {

        int contenidoUtf = 0;
        int donacionBytes = Float.BYTES;
        // Fecha como long (la convertiré luego a LocalDate)
        int fechaBytes = Long.BYTES;

        // NOMBRE
        // Calculo bytes UTF-8 del nombre
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


        return nombreTotal + donacionBytes + fechaBytes;

    }


    // Override equals y hashCode

    /**
     * Dos patrocinadores son iguales si tienen el mismo nombre (ignorando mayúsculas/minúsculas)
     * Se protege contra nombre null devolviendo false en ese caso.
     *
     * Esto permite que las colecciones que usan equals/hashcode (como Set) funcionen correctamente
     * para evitar duplicados por nombre de patrocinador.
     *
     * Si se modifica el nombre de un patrocinador que ya está en un Set, puede causar errores. Para
     * evitar esto deberia retirar del Set, modificar y luego volver a añadir.
     */
    @Override
    public boolean equals(Object o) {
        // Si son el mismo objeto
        if (this == o){
            return true;
        }
        // Si el objeto no es una instancia de Patrocinador
        if (!(o instanceof Patrocinador)){
            return false;
        }
        // Comparar nombres ignorando mayúsculas/minúsculas
        Patrocinador p = (Patrocinador) o;
        return this.nombre != null && this.nombre.equals(p.nombre);
    }

    /**
     * Cada vez que sobrescribes equals(), también debes sobrescribir hashCode().
     * Si no lo haces:
     * Las colecciones basadas en hash (HashSet, HashMap, HashTable) no detectarán duplicados correctamente.
     * Solo con base en su referencia.
     * Puedes tener objetos que equals() considera iguales, pero que el Set permite añadir varias veces.
     *
     * @return Devuelve el hashcode basado en el nombre en minúsculas para que coincida con equals().
     */
    @Override
    public int hashCode() {
        return nombre == null ? 0 : nombre.toLowerCase().hashCode();
        // Integer.hashCode(id); Para un solo campo entero
        //Objects.hash(nombre, id);  Para varios campos
    }


    // toString

    @Override
    public String toString() {
        return String.format("%s | Donación: %.2f | Inicio: %s", nombre, donacion, UtilidadesFechas.formatearCorto(fechaInicio));
    }

}
