package modelo;

import java.sql.Date;

public class VehiculoPropio extends Vehiculo {

    private Date dataCompra;
    private double prezoPagado;

    public VehiculoPropio(String matricula, String marca, String modelo, String tipoCombustible,
                          Date dataCompra, double prezoPagado) {
        super(matricula, marca, modelo, tipoCombustible);
        this.dataCompra = dataCompra;
        this.prezoPagado = prezoPagado;
    }

    public Date getDataCompra() {
        return dataCompra;
    }

    public void setDataCompra(Date dataCompra) {
        this.dataCompra = dataCompra;
    }

    public double getPrezoPagado() {
        return prezoPagado;
    }

    public void setPrezoPagado(double prezoPagado) {
        this.prezoPagado = prezoPagado;
    }
}
