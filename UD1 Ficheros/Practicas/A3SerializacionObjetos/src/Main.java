import logica.GestorCorredores;
import modelo.Corredor;
import modelo.Fondista;
import modelo.Velocista;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        GestorCorredores gc = new GestorCorredores();


        // Corredor con (new Fondista(String nombre, LocalDate fechaNacimiento, int equipo, float distanciaMax));
        gc.guardarCorredor(new Fondista("Juan Perez", java.time.LocalDate.of(1990, 5, 15),
                1, 42.195f));
        gc.guardarCorredor(new Velocista("Alberto MArtinez", java.time.LocalDate.of(1990, 6, 15),
                2, 46.23f));


        // Con lista
        List<Corredor> lista = new ArrayList<>();

        lista.add(new Velocista("Carlos Lopez", java.time.LocalDate.of(1992, 3,3),
                22, 50.0f));
        lista.add(new Fondista("Miguel Sanchez", java.time.LocalDate.of(1988, 12, 7),
                        20, 39.5f));
        lista.add(new Velocista("Luis Gomez", java.time.LocalDate.of(1995, 1, 25),
                        18, 45.0f));

        gc.guardarListaCorredores(lista);




    }
}
