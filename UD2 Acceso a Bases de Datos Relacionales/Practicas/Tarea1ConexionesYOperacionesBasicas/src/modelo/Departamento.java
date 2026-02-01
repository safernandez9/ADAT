package modelo;

public class Departamento {

    private int id;
    private String nombre;
    private String nssDirector;

    public Departamento() {
    }

    public Departamento(int id, String nombre, String nssDirector) {
        this.id = id;
        this.nombre = nombre;
        this.nssDirector = nssDirector;
    }

    // Getters y Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNssDirector() {
        return nssDirector;
    }

    public void setNssDirector(String nssDirector) {
        this.nssDirector = nssDirector;
    }

    // toString

    @Override
    public String toString() {
        return "Departamento {" + "id=" + id + ", nombre=" + nombre + ", nssDirector=" + nssDirector + '}';
    }
}
