package servicio;

import excepciones.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.io.IOException;
import java.util.Comparator;
import java.util.stream.Stream;

public class OperacionesNIO {

    /** visualizarContenidoNIO(String)
     *
     * @param ruta
     * @throws NoEsDirectorioException
     * @throws DirectorioNoExisteException
     */
    public static void visualizarContenidoNIO(String ruta) throws NoEsDirectorioException, DirectorioNoExisteException {
        System.out.println("\n\n-- LISTANDO EL DIRECTORIO " + ruta);

        // Obtener el Path del directorio y validarlo
        Path dir = Paths.get(ruta);
        Utilidades.validarDirectorioNIO(dir);

        // Por si le pasan solo un archivo
        if (Files.isRegularFile(dir)) {
            Utilidades.mostrarInfoNIO(dir);
        } else {
            //DirectoryStream de Path, forma NIO de listar directorios
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
                for (Path entry : stream) {
                    Utilidades.mostrarInfoNIO(entry);
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * recorrerRecursivoNIO(String ruta)
     * Recorre un directorio y su contenido de manera recursiva usando Files.walk
     */
    public static void recorrerRecursivoNIO(String ruta) throws NoEsDirectorioException, DirectorioNoExisteException {
        System.out.println("\n\n-- RECORRIDO RECURSIVO EN " + ruta);

        Path dir = Paths.get(ruta);
        Utilidades.validarDirectorioNIO(dir);

        // Files.walk para recorrer recursivamente. Es parecido a newDirectoryStream pero recursivo.
        try (Stream<Path> paths = Files.walk(dir)) {
            paths.forEach(Utilidades::mostrarInfoNIO);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * copiarNIO(String origen, String destino)
     * Copia un archivo de origen a destino
     */
    public static void copiarArchivoNIO(String origen, String destino) throws ArchivoNoExisteException, NoEsArchivoException, IOException {
        Path origenPath = Paths.get(origen);
        Path destinoPath = Paths.get(destino);

        Utilidades.validarArchivoNIO(origenPath);

        // Crear los directorios padre si no existen
        Files.createDirectories(destinoPath.getParent());

        // Copio el archivo
        Files.copy(origenPath, destinoPath, StandardCopyOption.REPLACE_EXISTING);
    }

    /**
     * moverNIO(String origen, String destino)
     * Mueve un archivo de origen a destino
     */
    public static void moverArchivoNIO(String origen, String destino) throws ArchivoNoExisteException, NoEsArchivoException, IOException {
        Path origenPath = Paths.get(origen);
        Path destinoPath = Paths.get(destino);

        // Validar que el origen existe y es un archivo
        Utilidades.validarArchivoNIO(origenPath);

        // Crear los directorios padre del destino si no existen
        Files.createDirectories(destinoPath.getParent());

        // Mover el archivo, reemplazando si existe
        Files.move(origenPath, destinoPath, StandardCopyOption.REPLACE_EXISTING);
    }

    /**
     * borrarNIO(String ruta)
     * Borra un archivo o directorio vacío
     */
    public static void borrarNIO(String ruta) throws ArchivoNoExisteException {
        Path path = Paths.get(ruta);

        if (!Files.exists(path)) {
            throw new ArchivoNoExisteException("No existe: " + ruta);
        }

        try {
            Files.delete(path);
            System.out.println("Borrado: " + path.toAbsolutePath());
        } catch (DirectoryNotEmptyException e) {
            System.err.println("No se puede borrar, el directorio no está vacío: " + path.toAbsolutePath());
        } catch (IOException e) {
            System.err.println("Error borrando: " + path.toAbsolutePath() + " -> " + e.getMessage());
        }
    }

    /**
     * borrarRecursivoNIO(String ruta)
     * Borra un directorio y todo su contenido
     */
    public static void borrarRecursivoNIO(String ruta) throws DirectorioNoExisteException, NoEsDirectorioException {
        Path dir = Paths.get(ruta);
        Utilidades.validarDirectorioNIO(dir);

        try (Stream<Path> paths = Files.walk(dir)) {
            paths.sorted((p1, p2) -> p2.compareTo(p1)) // primero los hijos, luego el padre
                    .forEach(p -> {
                        try {
                            Files.delete(p);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * filtrarPorExtensionNIO(String rutaOrigen, String filtro)
     *
     * Filtra los archivos de un directorio y su contenido recursivo por extensión
     * usando Files.walk y muestra su información
     *
     * @param rutaOrigen
     * @param filtro
     */
    public static void filtrarPorExtensionNIO(String rutaOrigen, String filtro) {
        Path dir = Paths.get(rutaOrigen);

        try (Stream<Path> stream = Files.walk(dir)) {
            // Filtro el stream para quedarme solo con archivos regulares que terminen
            // con la extensión dada y por cada uno muestro su info
            stream.filter(p -> Files.isRegularFile(p)
                    && p.getFileName().toString().toLowerCase().endsWith(filtro.toLowerCase()))
                    .forEach(Utilidades::mostrarInfoNIO);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * filtrarPorExtensionYOrdenarNIO(String rutaOrigen, String filtro, boolean descendente)
     *
     * Filtra los archivos de un directorio y su contenido recursivo por extensión
     * usando Files.walk, los ordena y muestra su información
     *
     * @param rutaOrigen
     * @param filtro
     * @param descendente
     */
    public static void filtrarPorExtensionYOrdenarNIO(String rutaOrigen, String filtro, boolean descendente) {
        Path dir = Paths.get(rutaOrigen);

        try (Stream<Path> stream = Files.walk(dir)) {
            Comparator<Path> comparator = Comparator.comparing(p -> p.getFileName().toString().toLowerCase());
            if (descendente) {
                comparator = comparator.reversed();
            }

            stream.filter(p -> Files.isRegularFile(p) &&
                            p.getFileName().toString().toLowerCase().endsWith(filtro.toLowerCase()))
                    .sorted(comparator)
                    .forEach(Utilidades::mostrarInfoNIO);

        } catch (IOException e) {
            System.err.println("Error recorriendo el directorio: " + e.getMessage());
        }
    }

    /**
     * filtrarPorSubcadenaNIO(String rutaOrigen, String filtro)
     *
     * Filtra los archivos de un directorio y su contenido recursivo por subcadena
     * usando Files.walk y muestra su información
     *
     * @param rutaOrigen
     * @param filtro
     */
    public static void filtrarPorSubcadenaNIO(String rutaOrigen, String filtro) {
        Path dir = Paths.get(rutaOrigen);

        try (Stream<Path> stream = Files.walk(dir)) {
            // Filtrar por subcadena en el nombre del archivo (ignorando mayúsculas/minúsculas)
            // A nivel apuntes: coge cada elemento de un iterable, comprueba condiciones y si las cumple aplica el la funcion
            // .loquesea que tenga
            stream.filter(p -> Files.isRegularFile(p)
                    && p.getFileName().toString().toLowerCase().contains(filtro.toLowerCase())).forEach(Utilidades::mostrarInfoNIO);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * copiarDirectorioNIO(String origen, String destino)
     *
     * Copia un directorio completo de origen a destino de forma recursiva
     *
     * @param origen
     * @param destino
     * @throws IOException
     */
    public static void copiarDirectorioNIO(String origen, String destino) throws IOException {
        // Obtener Paths de origen y destino
        Path dirOrigen = Paths.get(origen);
        Path dirDestino = Paths.get(destino).resolve(dirOrigen.getFileName());

        // Crear el directorio destino si no existe con sus padres
        if (!Files.exists(dirDestino)) {
            Files.createDirectories(dirDestino);
        }

        // Usar DirectoryStream para copiar cada entrada
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dirOrigen)) {
            for (Path entry : stream) {
                Path destinoPath = dirDestino.resolve(entry.getFileName());
                if (Files.isDirectory(entry)) {
                    copiarDirectorioNIO(entry.toString(), dirDestino.toString());
                } else {
                    Files.copy(entry, destinoPath, StandardCopyOption.REPLACE_EXISTING);
                }
            }
        }
    }


}
