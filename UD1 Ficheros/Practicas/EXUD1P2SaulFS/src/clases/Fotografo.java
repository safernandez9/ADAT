// Saúl Fernández Salgado
package clases;

import java.nio.charset.StandardCharsets;
import java.util.List;

public class Fotografo {
    private boolean borrado;
    private int codigo;
    private String numLicencia;
    private String nombre;
    private String estudio;
    private int numFotografia;
    private List<Fotografia> fotografias;

    public Fotografo() {

    }

    public Fotografo(boolean borrado, int codigo, String numLicencia, String nombre, String estudio, int numFotografia) {
        this.borrado = borrado;
        this.codigo = codigo;
        this.numLicencia = numLicencia;
        this.nombre = nombre;
        this.estudio = estudio;
        this.numFotografia = numFotografia;
    }

    public boolean isBorrado() {
        return borrado;
    }

    public void setBorrado(boolean borrado) {
        this.borrado = borrado;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getNumLicencia() {
        return numLicencia;
    }

    public void setNumLicencia(String numLicencia) {
        this.numLicencia = numLicencia;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEstudio() {
        return estudio;
    }

    public void setEstudio(String estudio) {
        this.estudio = estudio;
    }

    public int getNumFotografia() {
        return numFotografia;
    }

    public void setNumFotografia(int numFotografia) {
        this.numFotografia = numFotografia;
    }

    public List<Fotografia> getFotografias() {
        return fotografias;
    }

    public void setFotografias(List<Fotografia> fotografias) {
        this.fotografias = fotografias;
    }

    public int bytesSerializacionFotografo(){

        int camposEnterosBytes = Integer.BYTES * 2;
        int borradoBytes = 1;
        int contenidoUtf = 0;

        // NOMBRE

        if(this.nombre != null && !this.nombre.isEmpty()){
            // StandardCharsets.UTF_8 garantiza que no se lanza UnsupportedEncodingException
            contenidoUtf += this.nombre.getBytes(StandardCharsets.UTF_8).length;
        }
        if(this.estudio != null && !this.estudio.isEmpty()){
            // StandardCharsets.UTF_8 garantiza que no se lanza UnsupportedEncodingException
            contenidoUtf += this.estudio.getBytes(StandardCharsets.UTF_8).length;
        }
        if(this.numLicencia != null && !this.numLicencia.isEmpty()){
            // StandardCharsets.UTF_8 garantiza que no se lanza UnsupportedEncodingException
            contenidoUtf += this.numLicencia.getBytes(StandardCharsets.UTF_8).length;
        }

        // Si se supera el limite de writeUTF (65535 bytes) -> error de validacion unchecked
        if(contenidoUtf > 0xFFFF){
            throw new IllegalArgumentException("Nombre demasiado largo para writeUTF: " + contenidoUtf + " bytes");
        }

        // Aproximamos writeUTF como 2 bytes de prefijo + longitud en bytes UTF-8
        int totalTexto = 6 + contenidoUtf;

        int bytesFotografias = 0;

        // Sumar bytes de patrocinadores, protegemos contra patrocinadores = null
        if(this.fotografias != null){
            for(Fotografia f : this.fotografias){
                bytesFotografias += f.bytesSerializacionFotografia();
            }
        }

        return bytesFotografias + borradoBytes + camposEnterosBytes + totalTexto;

    }


    // toString

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("Código: ").append(this.codigo);
        sb.append(", Licencia: ").append(this.numLicencia);
        sb.append(", Nombre: ").append(this.nombre);
        sb.append(", Estudio: ").append(this.estudio);
        sb.append(", Número de fotografías: ").append(this.numFotografia);
        sb.append("\n");

        sb.append("Fotografias:\n");
        for(Fotografia f : fotografias){
            sb.append(f.toString());
        }
        return sb.toString();
    }
}
