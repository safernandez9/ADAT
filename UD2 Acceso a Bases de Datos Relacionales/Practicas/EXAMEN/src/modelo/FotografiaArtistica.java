// Saul Fernandez Salgado 77013586H
package modelo;

import java.sql.Date;
import java.time.LocalDate;

public class FotografiaArtistica extends Fotografia{

    private String encuadre;
    private String composicion;


    public FotografiaArtistica(String nome, String medidas, LocalDate data, char color, String encuadre, String composicion) {
        super(nome, medidas, data, color);
        this.encuadre = encuadre;
        this. composicion = composicion;
    }

    public String getEncuadre() {
        return encuadre;
    }

    public void setEncuadre(String encuadre) {
        this.encuadre = encuadre;
    }

    public String getComposicion() {
        return composicion;
    }

    public void setComposicion(String composicion) {
        this.composicion = composicion;
    }
}
