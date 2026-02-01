package modelo;

import java.sql.Date;

public class EmpregadoTemporal {

    private String nss;
    private Date dataInicio;
    private Date dataFin;
    private double costeHora;
    private double numHoras;

    public String getNss() {
        return nss;
    }

    public void setNss(String nss) {
        this.nss = nss;
    }

    public Date getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(Date dataInicio) {
        this.dataInicio = dataInicio;
    }

    public Date getDataFin() {
        return dataFin;
    }

    public void setDataFin(Date dataFin) {
        this.dataFin = dataFin;
    }

    public double getCosteHora() {
        return costeHora;
    }

    public void setCosteHora(double costeHora) {
        this.costeHora = costeHora;
    }

    public double getNumHoras() {
        return numHoras;
    }

    public void setNumHoras(double numHoras) {
        this.numHoras = numHoras;
    }
}
