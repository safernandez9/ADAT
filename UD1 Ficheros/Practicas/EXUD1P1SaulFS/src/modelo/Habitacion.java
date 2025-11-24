package modelo;

import java.util.ArrayList;

public class Habitacion extends Alojamiento {
    private String tipo;

    public Habitacion(String IDnumero, boolean estado, double precio, String tipo) {
        super(IDnumero, estado, precio);
        this.tipo = tipo;
    }
}
