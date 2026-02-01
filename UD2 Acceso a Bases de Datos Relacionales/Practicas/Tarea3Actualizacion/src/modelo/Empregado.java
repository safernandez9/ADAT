package modelo;

import java.sql.Date;

public class Empregado {

    private String nombre;
    private String apellido1;
    private String apellido2;
    private String nss;
    private String calle;
    private int numCalle;
    private String piso;
    private String cp;
    private String localidad;
    private String provincia;
    private Date dataNacemento;
    private char sexo;
    private String NSSSupervisa;
    private int numDepartamentoPertenece;

    public Empregado (String nss, String nombre, String apellido1, String apellido2){
        this.nss = nss;
        this.nombre = nombre;
        this.apellido1 = apellido1;
        this.apellido2 = apellido2;
    }

    // Getters y Setters


    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido1() {
        return apellido1;
    }

    public void setApellido1(String apellido1) {
        this.apellido1 = apellido1;
    }

    public String getApellido2() {
        return apellido2;
    }

    public void setApellido2(String apellido2) {
        this.apellido2 = apellido2;
    }

    public String getNss() {
        return nss;
    }

    public void setNss(String nss) {
        this.nss = nss;
    }

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public int getNumCalle() {
        return numCalle;
    }

    public void setNumCalle(int numCalle) {
        this.numCalle = numCalle;
    }

    public String getPiso() {
        return piso;
    }

    public void setPiso(String piso) {
        this.piso = piso;
    }

    public String getCp() {
        return cp;
    }

    public void setCp(String cp) {
        this.cp = cp;
    }

    public String getLocalidad() {
        return localidad;
    }

    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public Date getDataNacemento() {
        return dataNacemento;
    }

    public void setDataNacemento(Date dataNacemento) {
        this.dataNacemento = dataNacemento;
    }

    public char getSexo() {
        return sexo;
    }

    public void setSexo(char sexo) {
        this.sexo = sexo;
    }

    public String getNSSSupervisa() {
        return NSSSupervisa;
    }

    public void setNSSSupervisa(String NSSSupervisa) {
        this.NSSSupervisa = NSSSupervisa;
    }

    public int getNumDepartamentoPertenece() {
        return numDepartamentoPertenece;
    }

    public void setNumDepartamentoPertenece(int numDepartamentoPertenece) {
        this.numDepartamentoPertenece = numDepartamentoPertenece;
    }
}
