package modelo;

import java.io.Serializable;

public class EmpleadoDepartamento implements Serializable {

    private final long serialVersionUID = 1L;
    private String idDepartamento;
    private String nombre;
    private String localizacion;

    public EmpleadoDepartamento(String idDepartamento, String departamento, String localizacion) {
        this.idDepartamento = idDepartamento;
        this.nombre = departamento;
        this.localizacion = localizacion;
    }

    public String getIdDepartamento() {
        return idDepartamento;
    }
    public String getNombre() {
        return nombre;
    }
    public String getLocalizacion() {
        return localizacion;
    }
    public long getSerialVersionUID() {
        return serialVersionUID;
    }
    public void setIdDepartamento(String idDepartamento) {
        this.idDepartamento = idDepartamento;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public void setLocalizacion(String localizacion) {
        this.localizacion = localizacion;
    }
}
