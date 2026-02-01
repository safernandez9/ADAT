// Saul Fernandez Salgado 77013586H
package modelo;

import java.sql.Date;
import java.time.LocalDate;

public class FotografiaDocumental extends Fotografia {

    private String tipo;

    public FotografiaDocumental(String nome, String medidas, LocalDate data, char color, String tipo) {
        super(nome, medidas, data, color);
        this.tipo = tipo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
