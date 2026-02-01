package modelo;

public class EmpregadoProxecto {

    private String nssEmpregado;
    private int numProxecto;
    private Integer horas;

    public EmpregadoProxecto(String nssEmpregado, int numProxecto, Integer horas) {
        this.nssEmpregado = nssEmpregado;
        this.numProxecto = numProxecto;
        this.horas = horas;
    }

    public EmpregadoProxecto(String nssEmpregado, int numProxecto) {
        this.nssEmpregado = nssEmpregado;
        this.numProxecto = numProxecto;
        this.horas = null;
    }

    public String getNssEmpregado() {
        return nssEmpregado;
    }

    public void setNssEmpregado(String nssEmpregado) {
        this.nssEmpregado = nssEmpregado;
    }

    public int getNumProxecto() {
        return numProxecto;
    }

    public void setNumProxecto(int numProxecto) {
        this.numProxecto = numProxecto;
    }

    public Integer getHoras() {
        return horas;
    }

    public void setHoras(Integer horas) {
        this.horas = horas;
    }
}
