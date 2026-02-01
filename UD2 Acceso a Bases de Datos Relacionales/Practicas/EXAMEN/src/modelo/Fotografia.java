// Saul Fernandez Salgado 77013586H
package modelo;

import java.sql.Date;
import java.time.LocalDate;

public class Fotografia {

    private int codigo;
    private String nome;
    private String medidas;
    private LocalDate data;
    private int codFotografo;
    private int codExposicion;
    private char color;

    public Fotografia(String nome, String medidas, LocalDate data, char color) {
        this.codigo = codigo;
        this.nome = nome;
        this.medidas = medidas;
        this.data = data;
        this.color = color;
    }

    public int getCodigo() {
        return codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getMedidas() {
        return medidas;
    }

    public void setMedidas(String medidas) {
        this.medidas = medidas;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public int getCodFotografo() {
        return codFotografo;
    }

    public void setCodFotografo(int codFotografo) {
        this.codFotografo = codFotografo;
    }

    public int getCodExposicion() {
        return codExposicion;
    }

    public void setCodExposicion(int codExposicion) {
        this.codExposicion = codExposicion;
    }

    public char getColor() {
        return color;
    }

    public void setColor(char color) {
        this.color = color;
    }
}
