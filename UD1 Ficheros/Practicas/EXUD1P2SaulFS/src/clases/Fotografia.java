package clases;
// Saúl Fernández Salgado

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

public class Fotografia {
    private String titulo;
    private LocalDate fechaToma;
    private String tipo;
    private double tamanoMB;

    public Fotografia(){

    }

    public Fotografia(String titulo, LocalDate fechaToma, String tipo, double tamanoMB) {
        this.titulo = titulo;
        this.fechaToma = fechaToma;
        this.tipo = tipo;
        this.tamanoMB = tamanoMB;
    }


    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public LocalDate getFechaToma() {
        return fechaToma;
    }

    public void setFechaToma(LocalDate fechaToma) {
        this.fechaToma = fechaToma;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public double getTamanoMB() {
        return tamanoMB;
    }

    public void setTamanoMB(double tamanoMB) {
        this.tamanoMB = tamanoMB;
    }

    public int bytesSerializacionFotografia() {

        int contenidoUtf = 0;
        int tamañoMB = Double.BYTES;
        int fechaBytes = Long.BYTES;

        // NOMBRE
        // Calculo bytes UTF-8 del nombre
        if(this.titulo != null && !this.titulo.isEmpty()){
            // StandardCharsets.UTF_8 garantiza que no se lanza UnsupportedEncodingException
            contenidoUtf += this.titulo.getBytes(StandardCharsets.UTF_8).length;
        }

        if(this.tipo != null && !this.tipo.isEmpty()){
            // StandardCharsets.UTF_8 garantiza que no se lanza UnsupportedEncodingException
            contenidoUtf += this.tipo.getBytes(StandardCharsets.UTF_8).length;
        }

        // Si se supera el límite de writeUTF (65535 bytes) -> error de validacion unchecked
        if(contenidoUtf > 0xFFFF){
            throw new IllegalArgumentException("Nombre demasiado largo para writeUTF: " + contenidoUtf + " bytes");
        }

        // Aproximamos writeUTF como 2 bytes de prefijo + longitud en bytes UTF-8
        int utfTotal = 4 + contenidoUtf;


        return utfTotal + tamañoMB + fechaBytes;

    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();

        sb.append("\t" + this.getTitulo());
        sb.append(", " + this.getFechaToma());
        sb.append(", " + this.getTipo());
        sb.append(", " + this.getTamanoMB());
        sb.append("\n");

        return sb.toString();
    }

}
