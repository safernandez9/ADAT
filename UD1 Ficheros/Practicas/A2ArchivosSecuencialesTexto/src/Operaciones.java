import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Operaciones {

    /*
     * Escribe un método que cuente el número de líneas de cada fichero que se
     * especifique en la línea de
     * comandos (Nota: pueden especificarse varios archivos, como por ejemplo:
     * "exercicio5-1 file1.txt file3.txt
     * file2.txt"). Los archivos deben ser archivos de texto con la extensión txt.
     * Escribe en un fichero de texto llamado Salida.txt: el nombre de cada fichero,
     * junto con el número de líneas
     * del fichero. Si ocurre un error al intentar leer uno de los ficheros, en el
     * fichero salida.txt se graba un mensaje
     * de error para el archivo, y se deben procesar todos los ficheros restantes
     */
    public static void contarLineas(String[] archivos, String salida) throws Exception {
        // int contador = 0; aqui no que se acumula al pasar varios
        // falta comprobar que sean txt y que existan

        System.out.println("Total ficheros: " + archivos.length);

        try (EscrituraTexto writer = new EscrituraTexto(salida)) {
            writer.abrirArchivo();
            for (String archivo : archivos) {
                int contador = 0; // reinicia en cada archivo
                try (LecturaTexto reader = new LecturaTexto(archivo)) {
                    reader.abrirArchivo();
                    while (reader.leerLinea() != null) {
                        contador++;
                    }
                    writer.escribirLinea("Archivo: " + archivo + ", " + contador + " líneas");
                } catch (IOException e) {
                    writer.escribirLinea("error leyendo " + archivo);
                    System.out.println(e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("error escribiendo en " + salida);
        }
    }

    /*
     * A partir de un fichero de texto con el formato CURSO/NUMERO/ALUMNO crear un
     * directorio por cada
     * curso y dentro de este un directorio por cada alumno perteneciente a ese
     * curso. En un fichero de texto
     * llamado ficherolog.txt se irá escribiendo el éxito o fracaso en la creación
     * de cada directorio de alumnos
     */
    public static void crearDirectorios(String archivoEntrada, String logSalida) throws Exception {
        // + comprobar si exisyte
        try (LecturaTexto reader = new LecturaTexto(archivoEntrada);
             EscrituraTexto log = new EscrituraTexto(logSalida)) {

            reader.abrirArchivo();
            log.abrirArchivo();

            String linea;
            while ((linea = reader.leerLinea()) != null) {
                String[] partes = linea.split("/");

                // antes de validar el path o salta el ArrayIndexOutOfBoundsException:
                if (partes.length != 3) {
                    log.escribirLinea(linea + " ---> no se ha creado porque no sigue el formato");
                    continue;
                }

                Path dirCurso = Paths.get(partes[0]);
                Path dirAlumno = dirCurso.resolve(partes[2]);

                try {
                    // + con io
                    Files.createDirectories(dirAlumno);
                    log.escribirLinea(dirAlumno + "---> se ha creado correctamente");
                } catch (IOException e) {
                    log.escribirLinea(dirAlumno + "---> no se ha creado");
                }
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    /*
     * Escribe un método en Java que, dado un fichero de texto y una palabra, cuente
     * cuántas veces aparece esa
     * palabra en cada línea del fichero y también el número total de apariciones en
     * todo el documento. La salida se
     * grabará en un fichero de texto.
     * Recibir como parámetros:
     * ▪ El nombre del fichero de entrada (texto plano con extensión .txt).
     * ▪ La palabra a buscar.
     * ▪ El nombre del fichero de salida donde se guardará el resultado.
     * Gestionar excepciones:
     * ▪ Si el fichero de entrada no existe o no se puede leer, mostrar un mensaje
     * de error.
     * ▪ Si ocurre un error al escribir el fichero de salida, también debe
     * informarse.
     */
    public static void contarPalabra(String archivoEntrada, String palabra, String archivoSalida) throws Exception {
        // con matcher y pattern \\b y que lo aplique a cada linea

        try (
                LecturaTexto reader = new LecturaTexto(archivoEntrada);
                EscrituraTexto writer = new EscrituraTexto(archivoSalida)) {

            reader.abrirArchivo();
            writer.abrirArchivo();

            String nombreArchivo = Paths.get(archivoEntrada).getFileName().toString();

            String linea;
            int total = 0;
            int numLinea = 1;

            writer.escribirLinea("La palabra \"" + palabra + "\" aparece en el fichero " + nombreArchivo);
            writer.escribirLinea("________________________________________________");

            while ((linea = reader.leerLinea()) != null) {
                int contador = (linea.split("\\b" + palabra + "\\b", -1).length - 1);
                total += contador;
                writer.escribirLinea("Línea " + numLinea + ": aparece " + contador + " veces.");
                numLinea++;
            }

            writer.escribirLinea("________________________________________________");
            writer.escribirLinea("Aparece un total de " + total + " veces. ");

        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    public static String normalizarLinea(String linea) {
        if (linea == null)
            return null;

        Pattern patronPuntuacion = Pattern.compile("\\p{Punct}+");
        String normalizada = patronPuntuacion.matcher(linea).replaceAll(" ");
        return normalizada.replaceAll("\\s+", " ").trim();
    }

    public static void contarPalabras(String archivoEntrada, String archivoSalida) throws Exception {
        try (
                LecturaTexto reader = new LecturaTexto(archivoEntrada);
                EscrituraTexto writer = new EscrituraTexto(archivoSalida)) {

            reader.abrirArchivo();
            writer.abrirArchivo();

            String nombreArchivo = Paths.get(archivoEntrada).getFileName().toString();
            writer.escribirLinea("Frecuencia de palabras en el fichero " + nombreArchivo);

            Map<String, Integer> contadorPalabras = new HashMap<>();
            Pattern patronPalabras = Pattern.compile("\\b\\p{L}+\\b");

            String linea;
            while ((linea = reader.leerLinea()) != null) {
                linea = normalizarLinea(linea).toLowerCase();
                Matcher matcher = patronPalabras.matcher(linea);

                while (matcher.find()) {
                    String palabra = matcher.group();
                    contadorPalabras.put(palabra, contadorPalabras.getOrDefault(palabra, 0) + 1);
                }
            }

            for (Map.Entry<String, Integer> entrada : contadorPalabras.entrySet()) {
                writer.escribirLinea(entrada.getKey() + ": " + entrada.getValue());
            }

            writer.escribirLinea("\nTotal de palabras diferentes: " + contadorPalabras.size());

        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

}