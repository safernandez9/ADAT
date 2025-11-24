package modelo;

import java.util.ArrayList;
import java.util.List;

public class Alojamiento {
    private String IDnumero;
    private boolean estado;
    private double precio;
    List<Reserva> reservas;

    public Alojamiento(String IDnumero, boolean estado, double precio) {
        this.IDnumero = IDnumero;
        this.estado = estado;
        this.precio = precio;
        this.reservas = new ArrayList<>();
    }

}
