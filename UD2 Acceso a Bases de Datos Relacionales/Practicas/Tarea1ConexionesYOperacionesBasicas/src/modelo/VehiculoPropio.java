package modelo;

import java.sql.Date;

public class VehiculoPropio {

    private int idVehiculo;
    private Date dataCompra;
    private double prezoPagado;

    public int getIdVehiculo() {
        return idVehiculo;
    }

    public void setIdVehiculo(int idVehiculo) {
        this.idVehiculo = idVehiculo;
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
