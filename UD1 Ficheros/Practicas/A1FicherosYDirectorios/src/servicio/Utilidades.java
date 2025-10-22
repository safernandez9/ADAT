package servicio;

import excepciones.*;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

public class Utilidades {

    //Metodos IO

    /**
     * validarArchivo(File)
     * - Valida que un archivo exista
     * - Valida que la ruta recibida sea un archivo
     * - Lanza excepciones personalizadas si algo de esto falla
     *
     * @param file
     * @throws ArchivoNoExisteException
     * @throws NoEsArchivoException
     */
    public static void validarArchivo(File file) throws ArchivoNoExisteException, NoEsArchivoException {

        if (!file.exists()) {
            throw new ArchivoNoExisteException(file.getPath());
        }
        if (!file.isFile()) {
            throw new NoEsArchivoException();
        }
    }

    /**
     * validarDirectorio(File)
     * - Valida que un directorio exista
     * - Valida que la ruta recibida sea un archivo
     * - Lanza excepciones personalizadas si algo de esto falla
     *
     * @param dir
     * @throws DirectorioNoExisteException
     * @throws NoEsDirectorioException
     */
    public static void validarDirectorio(File dir) throws DirectorioNoExisteException, NoEsDirectorioException {
        if (!dir.exists()) {
            throw new DirectorioNoExisteException(dir.getPath());
        }

        if (!dir.isDirectory()) {
            throw new NoEsDirectorioException();
        }
    }

    /**
     * validarExtesion(String)
     * - Valida que la cadena introducida tenga forma de extensión (empiece por ".")
     *
     * @param extension
     * @throws ExtensionIncorrectaException
     */
    public static void validarExtension(String extension) throws ExtensionIncorrectaException {
        if (!extension.startsWith(".")) {
            throw new ExtensionIncorrectaException(extension);
        }
    }

    /**
     * mostrarInfo(File)
     * - Muestra la información de un archivo o fichero formateada
     *
     * @param file
     */
    public static void mostrarInfo(File file) {

        String type = file.isDirectory() ? "<DIR>" : "<FICHERO>";
        String size = file.isDirectory() ? " " : ajustarTamanhoArchivo(file.length());

        System.out.printf("- |%-22s %-10s %-10s %s%n",
                file.getName(),
                type,
                size,
                ajustarFechaModificacion(file.lastModified())
        );
    }

    /**
     * ajustarTamañoArchivo(long)
     * Metodo auxiliar que devuelve en una cadena el tamaño del archivo, eligiendo
     * en función de su tamaño en bytes que se le pasa en la entrada si la representacion
     * será en KB, MB o GB
     *
     * @param length
     * @return
     */
    private static String ajustarTamanhoArchivo(long length) {

        final long KB = 1024;
        final long MB = KB * 1024;
        final long GB = MB * 1024;

        if (length < MB) {
            return String.format("%.2f KB", (float) length / KB);
        } else if (length < GB) {
            return String.format("%.2f MB", (float) length / MB);
        } else {
            return String.format("%.2f GB", (float) length / GB);
        }

    }

    /**
     * AjustarFechaModificacion(long)
     * recibe un long con los ms desde 1970 y con un objeto tipo SimpleDateFormat le doy formato
     * legible y con format lo convierto a String
     *
     * @param ultimaModificacion
     * @return
     */
    private static String ajustarFechaModificacion(long ultimaModificacion) {

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return sdf.format(ultimaModificacion);

    }

    /**
     * recorrerRec(File, int)
     * Recibe una ruta y el nivel de anidamiento en el que esta según la inicial
     * Recorre sus elementos y lLama a mostrarInfo(File, int) para cada uno.
     * Si el siguiente es directorio se llama a si mismo de forma recursiva incrementando el nivel de andiamiento
     *
     * @param dir
     * @param nivel
     */
    public static void recorrerRec(File dir, int nivel) {

        for (File f : dir.listFiles()) {
            mostrarInfo(f, nivel);

            if (f.isDirectory()) {
                recorrerRec(f, nivel + 1);
            }
        }
    }

    /**
     * mostrarInfo(File, int)
     * Sobrescritura del metodo mostrarInfo que recibe un parametro nivel para representar el
     * anidamiento de los directorios al recorrerlos recursivamente
     *
     * @param file
     * @param nivel
     */
    public static void mostrarInfo(File file, int nivel) {

        String prefijo = "-".repeat(nivel);

        String type = file.isDirectory() ? "<DIR>" : "<FICHERO>";
        String size = file.isDirectory() ? " " : ajustarTamanhoArchivo(file.length());

        System.out.printf(prefijo + " |%-22s %-10s %-10s %s%n",
                file.getName(),
                type,
                size,
                ajustarFechaModificacion(file.lastModified())
        );
    }

    /**
     * filtrar(File, FilenameFilter)
     * Recorro el directorio a revisar con el filtro que haya creado añadiendo a una lista solo los
     * elementos que cumplan el filtro.
     *
     * @param dir
     * @param filter
     * @return
     */
    public static List<File> filtrar(File dir, FilenameFilter filter) {
        List<File> list = new ArrayList<>();

        //Al pasar un objeto Filtro a FILE.listfiles() lo utiliza para filtrar usando su metodo accept
        for (File f : dir.listFiles(filter)) {
            list.add(f);
        }

        return list;
    }

    // METODOS NIO

    /** validarArchivo(Path)
     * - Valida que un archivo exista
     * - Valida que la ruta recibida sea un archivo
     * - Lanza excepciones personalizadas si algo de esto falla
     * @param path
     * @throws ArchivoNoExisteException
     * @throws NoEsArchivoException
     */
    public static void validarArchivo(Path path) throws ArchivoNoExisteException, NoEsArchivoException {
        if (!Files.exists(path)) {
            throw new ArchivoNoExisteException(path.toString());
        }
        if (!Files.isRegularFile(path)) {
            throw new NoEsArchivoException();
        }
    }

    /**
     * validarDirectorio(Path)
     * - Valida que un directorio exista
     * - Valida que la ruta recibida sea un archivo
     * - Lanza excepciones personalizadas si algo de esto falla
     *
     * @param path
     * @throws DirectorioNoExisteException
     * @throws NoEsDirectorioException
     */
    public static void validarDirectorio(Path path) throws DirectorioNoExisteException, NoEsDirectorioException {
        if (!Files.exists(path)) {
            throw new DirectorioNoExisteException(path.toString());
        }
        if (!Files.isDirectory(path)) {
            throw new NoEsDirectorioException();
        }
    }

    /** mostrarInfo(Path)
     * - Muestra la información de un archivo o fichero formateada
     *
     * @param path
     */
    public static void mostrarInfo(Path path) {

        String type = Files.isDirectory(path) ? "<DIR>" : "<FICHERO>";
        String size;

        try {
            size = Files.isDirectory(path) ? " " : ajustarTamanhoArchivo(Files.size(path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            System.out.printf("- |%-22s %-10s %-10s %s%n",
                    path.getFileName(),
                    type,
                    size,
                    ajustarFechaModificacion(Files.getLastModifiedTime(path).toMillis())
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


}






