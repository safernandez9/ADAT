// Saúl Fernández Salgado
package main;

import logica.gestorAlojamientos;
import modelo.Reserva;
import modelo.Usuario;
import utiles.TipoValidacion;

import java.time.LocalDate;

public class Main {

    private static final String RUTA = "ArchivosXMLDTD/AlojamientosHotel.xml";
    private static final String rutaGuardado = "ArchivosXMLDTD/AlojamientosUpdate.xml";
    private static final gestorAlojamientos gestor = new gestorAlojamientos();

    public static void main(String[] args) {

        System.out.println("EJERCICIO 2\n\n");
        System.out.println("CARGAR DOCUMENTO DOM");
        gestor.cargarDocumentoDOM(RUTA, TipoValidacion.DTD);


        // RESERVA SUITE SIN RESERVAS

        Reserva reserva1 = new Reserva("ABCD", new Usuario("77013586H", "Saul fernandez"), LocalDate.parse("2020-12-12"));
        String idAlojamiento1 = "S201";
        System.out.println("AÑADIR RESERVA " + reserva1.getcodigo() + " A SUITE " + idAlojamiento1 + " SIN RESERVAS");
        gestor.añadirReserva(idAlojamiento1, reserva1);


        // RESERVA HABITACION SIN RESERVAS

        Reserva reserva2 = new Reserva("EFGH", new Usuario("77013586H", "Saul fernandez"), LocalDate.parse("2020-12-12"));
        String idAlojamiento2 = "H109";
        System.out.println("AÑADIR RESERVA " + reserva2.getcodigo() + " A HABITACION " + idAlojamiento2 + " SIN RESERVAS");
        gestor.añadirReserva(idAlojamiento2, reserva2);


        // RESERVA ALOJAMIENTO INEXISTENTE

        String idAlojamiento3 = "XXXXX";
        System.out.println("AÑADIR RESERVA " + reserva2.getcodigo() + " A HABITACION " + idAlojamiento3 + " INEXISTENTE");
        gestor.añadirReserva(idAlojamiento3, reserva2);


        // RESERVA HABITACION OCUPADA

        Reserva reserva3 = new Reserva("R100", new Usuario("77013586H", "Saul fernandez"), LocalDate.parse("2020-12-12"));
        String idAlojamiento4 = "H101";
        System.out.println("AÑADIR RESERVA " + reserva3.getcodigo() + " A HABITACION " + idAlojamiento4 + " OCUPADA");
        gestor.añadirReserva(idAlojamiento4, reserva3);

        // RESERVA CODIGO DUPLICADO

        Reserva reserva4 = new Reserva("R001", new Usuario("77013586H", "Saul fernandez"), LocalDate.parse("2020-12-12"));
        String idAlojamiento5 = "S202";
        System.out.println("AÑADIR RESERVA " + reserva4.getcodigo() + " A HABITACION " + idAlojamiento5 + " RESERVA CON MISMO CODIGO");
        gestor.añadirReserva(idAlojamiento5, reserva4);

        gestor.guardarDOM(rutaGuardado);


    }
}