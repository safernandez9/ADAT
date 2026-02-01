package modelo;

import java.sql.Date;

public class VehiculoRenting {

    private int idVehiculo;
    private Date dataInicio;
    private double prezoMensual;
    private int mesesContratados;

    public int getIdVehiculo() {
        return idVehiculo;
    }

    public void setIdVehiculo(int idVehiculo) {
        this.idVehiculo = idVehiculo;
    }

    public Date getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(Date dataInicio) {
        this.dataInicio = dataInicio;
    }

    public double getPrezoMensual() {
        return prezoMensual;
    }

    public void setPrezoMensual(double prezoMensual) {
        this.prezoMensual = prezoMensual;
    }

    public int getMesesContratados() {
        return mesesContratados;
    }

    public void setMesesContratados(int mesesContratados) {
        this.mesesContratados = mesesContratados;
    }
}
