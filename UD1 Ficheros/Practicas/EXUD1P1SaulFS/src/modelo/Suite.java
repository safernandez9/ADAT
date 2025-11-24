package modelo;

import utiles.Categorias;

import java.util.ArrayList;

public class Suite extends Alojamiento{

    private boolean terraza;
    private Categorias categoria;

    public Suite(String IDnumero, boolean estado, double precio, boolean terraza, Categorias categoria) {
        super(IDnumero, estado, precio);
        this.terraza = terraza;
        this.categoria = categoria;
    }

}
