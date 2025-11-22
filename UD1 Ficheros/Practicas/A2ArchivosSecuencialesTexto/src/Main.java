public class Main {
    public static void main(String[] args) throws Exception {

        // String rutaLog = "\\log.txt";
        // String rutaEscoba = "\\escoba.txt";

        // String rutaLog = "/Volumes/DAM/ADAT/Prueba1/src/Ejercicio2/log.txt";
        String rutaEscoba = "src/escoba.txt";

        // String salidaLineas = "/Volumes/DAM/ADAT/Prueba1/src/Ejercicio2/Salida.txt";
        // String salidaPalabras = "/Volumes/DAM/ADAT/Prueba1/src/Ejercicio2/ResultadoPalabras.txt";
        // String logDirectorios = "/Volumes/DAM/ADAT/Prueba1/src/Ejercicio2/ficherolog.txt";
        String salidaContar = "src/contarpalabras.txt";

        // String[] archivos = {rutaLog, rutaEscoba};
        // Operaciones.contarLineas(archivos, salidaLineas);

        // Operaciones.crearDirectorios(rutaLog, logDirectorios);

        // Operaciones.contarPalabra(rutaEscoba, "escoba", salidaPalabras);
        Operaciones.contarPalabras(rutaEscoba, salidaContar);

    }
}
