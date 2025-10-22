import excepciones.ArchivoNoExisteException;

import java.io.IOException;

public class Operaciones {

    public static void ejercicio1(String archivos) {

        final String RUTA_SALIDA_E1 = "../Salida.txt";
        String [] archivosEntrada;

        archivosEntrada = archivos.split("\\s+");

        try {
            EscrituraTexto escritor = new EscrituraTexto(RUTA_SALIDA_E1);
            escritor.abrirArchivo();

            for (String ruta : archivosEntrada) {
                try {
                    LecturaTexto lector = new LecturaTexto(ruta);
                    lector.abrirArchivo();
                    int contador = 0;
                    String linea;
                    while ((linea = lector.leerLinea()) != null) {
                        contador++;
                    }
                    escritor.escribirLinea(ruta + ": " + contador + " líneas");

                } catch (ArchivoNoExisteException e) {
                    escritor.escribirLinea("Archivo no existe: " + ruta);
                } catch (IOException e) {
                    escritor.escribirLinea("Error leyendo archivo: " + ruta);
                }
            }
        } catch (IOException e) {
            System.out.println("Error escribiendo archivo de salida: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}