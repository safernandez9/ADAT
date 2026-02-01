package modelo;

public class Proxecto {
    private int numProxecto;
    private String nomeProxecto;
    private String lugar;
    private int numDepartControla;

    public Proxecto(int numProxecto, String nomeProxecto, String lugar, int numDepartControla) {
        this.numProxecto = numProxecto;
        this.nomeProxecto = nomeProxecto;
        this.lugar = lugar;
        this.numDepartControla = numDepartControla;
    }

    public int getNumProxecto() {
        return numProxecto;
    }

    public void setNumProxecto(int numProxecto) {
        this.numProxecto = numProxecto;
    }

    public String getNomeProxecto() {
        return nomeProxecto;
    }

    public void setNomeProxecto(String nomeProxecto) {
        this.nomeProxecto = nomeProxecto;
    }

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    public int getNumDepartControla() {
        return numDepartControla;
    }

    public void setNumDepartControla(int numDepartControla) {
        this.numDepartControla = numDepartControla;
    }
}
