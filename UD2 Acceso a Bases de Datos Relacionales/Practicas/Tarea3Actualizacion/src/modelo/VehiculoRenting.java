package modelo;

import java.sql.Date;

public class VehiculoRenting extends Vehiculo {


    private Date dataInicio;
    private double prezoMensual;
    private int mesesContratados;

    public VehiculoRenting(String matricula, String marca, String modelo, String tipoCombustible,
                          Date dataInicio, double prezoMensual, int mesesContratados) {
        super(matricula, marca, modelo, tipoCombustible);
        this.dataInicio = dataInicio;
        this.prezoMensual = prezoMensual;
        this.mesesContratados = mesesContratados;
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
