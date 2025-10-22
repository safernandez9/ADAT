package servicio;

import excepciones.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.io.IOException;
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

        Path dir = Paths.get(ruta);
        Utilidades.validarDirectorio(dir);

        //DirectoryStream de Path
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
            for (Path entry : stream) {
                Utilidades.mostrarInfo(entry);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * recorrerRecursivoNIO(String ruta)
     * Recorre un directorio y su contenido de manera recursiva usando Files.walk
     */
    public static void recorrerRecursivoNIO(String ruta) throws NoEsDirectorioException, DirectorioNoExisteException {
        System.out.println("\n\n-- RECORRIDO RECURSIVO EN " + ruta);

        Path dir = Paths.get(ruta);
        Utilidades.validarDirectorio(dir);

        try (Stream<Path> paths = Files.walk(dir)) {
            paths.forEach(Utilidades::mostrarInfo);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * copiarNIO(String origen, String destino)
     * Copia un archivo de origen a destino
     */
    public static void copiarArchivoNIO(String origen, String destino) throws ArchivoNoExisteException, NoEsArchivoException {
        Path origenPath = Paths.get(origen);
        Path destinoPath = Paths.get(destino);

        Utilidades.validarArchivo(origenPath);

        try {
            Files.copy(origenPath, destinoPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * moverNIO(String origen, String destino)
     * Mueve un archivo de origen a destino
     */
    public static void moverArchivoNIO(String origen, String destino) throws ArchivoNoExisteException, NoEsArchivoException {
        Path origenPath = Paths.get(origen);
        Path destinoPath = Paths.get(destino);

        Utilidades.validarArchivo(origenPath);

        try {
            Files.move(origenPath, destinoPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * borrarNIO(String ruta)
     * Borra un archivo o directorio vacío
     */
    public static void borrarNIO(String ruta) throws ArchivoNoExisteException {
        Path path = Paths.get(ruta);

        if (!Files.exists(path)) {
            throw new ArchivoNoExisteException(ruta);
        }

        try {
            Files.delete(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * borrarRecursivoNIO(String ruta)
     * Borra un directorio y todo su contenido
     */
    public static void borrarRecursivoNIO(String ruta) throws DirectorioNoExisteException, NoEsDirectorioException {
        Path dir = Paths.get(ruta);
        Utilidades.validarDirectorio(dir);

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

    public static void filtrarPorExtensionNIO(String rutaOrigen, String filtro) {
    }

    public static void filtrarPorExtensionYOrdenarNIO(String rutaOrigen, String filtro, boolean ordenOrdenacion) {
    }

    public static void filtrarPorSubcadenaNIO(String rutaOrigen, String filtro) {
    }

    public static void copiarDirectorioNIO(String rutaOrigen, String rutaDestino) {
    }
}
