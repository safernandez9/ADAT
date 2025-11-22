package servicio;

import excepciones.*;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

/* Apuntes para el examen de Java


 ▓▓▓ 1. DIFERENCIA PRINCIPAL ENTRE IO Y NIO
 - IO usa la clase File
 - NIO usa Path, Paths y Files
 - IO es orientado a objetos; NIO es ORIENTADO A MÉTODOS ESTÁTICOS.

 -------------------------------------------------------------
 ▓▓▓ 2. VALIDACIONES
 - IO: file.exists(), file.isFile(), file.isDirectory()
 - NIO: Files.exists(), Files.isRegularFile(), Files.isDirectory()

 → MUY IMPORTANTE: en IO si usas file.isDirectory() sin comprobar
   antes file.exists(), te puede dar un false engañoso.
 → NIO lanza más excepciones → tienes que estar preparado
   para capturar IOException.

 -------------------------------------------------------------
 ▓▓▓ 3. OBTENCIÓN DE INFORMACIÓN (nombre, tamaño, fecha)
 IO:
   - file.length()
   - file.lastModified()
   - file.getName()
 NIO:
   - Files.size(path)
   - Files.getLastModifiedTime(path)
   - path.getFileName()

 → En tus métodos ajustarTamañoArchivo() y ajustarFechaModificacion()
   ya lo tienes bien hecho. Recuerda que en NIO tienes que convertir
   el FileTime a millis: time.toMillis().

 → OJO: en NIO cada operación puede lanzar IOException.

 -------------------------------------------------------------
 ▓▓▓ 4. RECORRER DIRECTORIOS
 IO:
   - Se usa dir.listFiles()
   - Necesitas recursividad manual
   - Si dir.listFiles() devuelve null → fallo silencioso

 NIO:
   - Files.list(path)
   - Files.walk(path)
   - DirectoryStream<Path>

 → En el examen, usar Files.walk(path) es más profesional,
   pero también más "verborrea". listFiles() es más rápido
   para escribir bajo presión.

 En tu código IO:
   - Usas recorrerRec(File dir, int nivel) → perfecto.
   - El prefijo con "-".repeat(nivel) es limpio y correcto.

 En tu código NIO:
   - Tienes método calcularSangria(Path base, Path actual)
     usando base.relativize → MUY BUENO, opción moderna.

 -------------------------------------------------------------
 ▓▓▓ 5. FILTRADO
 IO:
   - FilenameFilter
   - FileFilter
   - Muy limitado y más verboso

 NIO:
   - Streams con filter(...)
   - DirectoryStream con patrones "*.txt"

 → ES MÁS FÁCIL FALLAR FILTRANDO EN IO porque dependes del
   método accept() y tienes que controlar nombres a mano.

 Tu código IO usa correctamente listFiles(filter).

 -------------------------------------------------------------
 ▓▓▓ 6. COPIA Y MOVER ARCHIVOS
 IO:
   - Necesitas abrir FileInputStream + FileOutputStream
   - Copiar byte a byte
   - Cerrar flujos manualmente (o usar try-with-resources)
   - Más fácil cometer errores

 NIO:
   - Files.copy(), Files.move()
   - Una línea
   - Más seguro
   - Puedes usar REPLACE_EXISTING

 → Si sale un ejercicio de copiar/mover, NIO es SIEMPRE la
   opción más compacta y segura.

 -------------------------------------------------------------
 ▓▓▓ 7. CREAR DIRECTORIOS
 IO:
   - dir.mkdir()  (solo un nivel)
   - dir.mkdirs() (varios niveles)

 NIO:
   - Files.createDirectory()
   - Files.createDirectories()

 → En tu código IO ya tienes crearDirectorio(File).

 -------------------------------------------------------------
 ▓▓▓ 8. ERRORES TÍPICOS DE EXAMEN
 - CONFUNDIR File → getName() con Path → getFileName()
 - OLVIDAR que Files.size() y Files.getLastModifiedTime() lanzan IOException
 - USAR mal relativize() (el orden importa)
 - No comprobar existencia antes de usar isDirectory()
 - En IO: dir.listFiles() puede ser null
 - En NIO: usar Files.copy(destino, origen) → parámetros invertidos
 - En IO: file.length() devuelve 0 si no tienes permisos → engaño
 - En NIO: recuerdas que Path NO representa un archivo hasta que se valida

 -------------------------------------------------------------
 ▓▓▓ 9. DETALLES ESPECÍFICOS DE TU CÓDIGO (COSAS IMPORTANTES)
 - En IO tienes métodos duplicados como validarArchivo(File) y validarArchivo(Path).
   MUY BIEN: en el examen recuerda siempre cuál estás usando.

 - El método ajustarTamanhoArchivo() está perfecto y lo puedes reutilizar
   tanto en IO como en NIO (solo cambia la obtención del size).

 - En mostrarInfo(File, String) usas NumberFormat y KB → correcto para IO.
   En NIO usas Files.size() / 1024 → perfecto.

 - formatearFecha() con Instant + ZoneId + DateTimeFormatter → TOP.
   Mucho mejor que SimpleDateFormat (que es viejo).

 - calcularSangria() usando relativize() es ideal para NIO,
   PERO RECUERDA: base.relativize(actual) falla si no están en la misma raíz
   (ej: C:/ y D:/ → IllegalArgumentException).

 - En IO: recorrerRec() muestra los archivos ANTES de entrar en subcarpetas.
   En NIO puedes hacerlo igual con Files.walk().

 -------------------------------------------------------------
 ▓▓▓ 10. RESUMEN ULTRA-CORTO PARA EXAMEN
 - IO: rápido de escribir, menos potente.
 - NIO: más largo pero más profesional, mejor manejo de errores.
 - IO muestra cosas más simples → cuidado con null.
 - NIO siempre puede lanzar IOException.
 - IO para cosas fáciles; NIO para copiar, mover y recorrer bien.
 - En NIO Path es INMUTABLE (File no siempre), mejor para seguridad.

 -------------------------------------------------------------
 FIN DEL COMENTARIO
 -------------------------------------------------------------
*/


public class Utilidades {

    // METODOS IO

    /**
     * validarArchivoIO(File)
     * - Valida que un archivo exista
     * - Valida que la ruta recibida sea un archivo
     * - Lanza excepciones personalizadas si algo de esto falla
     *
     * @param file File con el archivo a validar
     * @throws ArchivoNoExisteException
     * @throws NoEsArchivoException
     */
    public static void validarArchivoIO(File file) throws ArchivoNoExisteException, NoEsArchivoException {

        if (!file.exists()) {
            throw new ArchivoNoExisteException(file.getPath());
        }
        if (!file.isFile()) {
            throw new NoEsArchivoException();
        }
    }

    /**
     * validarDirectorioIO(File)
     * - Valida que un directorio exista
     * - Valida que la ruta recibida sea un archivo
     * - Lanza excepciones personalizadas si algo de esto falla
     *
     * @param dir
     * @throws DirectorioNoExisteException
     * @throws NoEsDirectorioException
     */
    public static void validarDirectorioIO(File dir) throws DirectorioNoExisteException, NoEsDirectorioException {
        if (!dir.exists()) {
            throw new DirectorioNoExisteException(dir.getPath());
        }

        if (!dir.isDirectory()) {
            throw new NoEsDirectorioException();
        }
    }

    /**
     * mostrarInfo(File)
     * - Muestra la información de un archivo o fichero formateada según el formato.
     * Nombre, tipo, tamaño si es archivo y fecha de última modificación
     *
     *
     * @param file Directorio a mostrar su información
     */
    public static void mostrarInfo(File file) {

        String type = file.isDirectory() ? "<DIR>" : "<FICHERO>";
        String size = file.isDirectory() ? " " : ajustarTamanoArchivo(file.length());

        System.out.printf("- |%-22s %-10s %-10s %s%n",
                file.getName(),
                type,
                size,
                ajustarFechaModificacion(file.lastModified())
        );
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

        // El if es por seguridad extra ante NULLPOINTER raros.
        File [] ficheros = dir.listFiles();
        if(ficheros != null){
            for (File f : ficheros) {
                mostrarInfo(f, nivel);

                if (f.isDirectory()) {
                    recorrerRec(f, nivel + 1);
                }
            }
        }

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

    /**
     * mostrarInfo(File, int)
     * Sobrescritura del metodo mostrarInfo que recibe un parametro nivel para representar el
     * anidamiento de los directorios al recorrerlos recursivamente
     *
     * @param file File con archivo o directorio a mostrar su información
     * @param nivel nivel de anidamiento del archivo o directorio
     */
    public static void mostrarInfo(File file, int nivel) {

        String prefijo = "-".repeat(nivel);

        String type = file.isDirectory() ? "<DIR>" : "<FICHERO>";
        String size = file.isDirectory() ? " " : ajustarTamanoArchivo(file.length());

        System.out.printf(prefijo + " |%-22s %-10s %-10s %s%n",
                file.getName(),
                type,
                size,
                ajustarFechaModificacion(file.lastModified())
        );
    }


    // METODOS NIO

    /** validarArchivo(Path)
     *
     * - Valida que un archivo exista
     * - Valida que la ruta recibida sea un archivo
     * - Lanza excepciones personalizadas si algo de esto falla
     * @param path
     * @throws ArchivoNoExisteException
     * @throws NoEsArchivoException
     */
    public static void validarArchivoNIO(Path path) throws ArchivoNoExisteException, NoEsArchivoException {
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
    public static void validarDirectorioNIO(Path path) throws DirectorioNoExisteException, NoEsDirectorioException {
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
    public static void mostrarInfoNIO(Path path) {

        // Obtener tipo
        String type = Files.isDirectory(path) ? "<DIR>" : "<FICHERO>";
        String size;

        // Obtener tamaño
        try {
            size = Files.isDirectory(path) ? " " : ajustarTamanoArchivo(Files.size(path));
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

    // Metodos Comunes

    /**
     * validarExpresion(String)
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
     * ajustarTamanoArchivo(long)
     * Metodo auxiliar que devuelve en una cadena el tamaño del archivo, eligiendo este
     * en función del tamaño en byres del archivo que se le pasa en la entrada como long si la representacion
     * será en KB, MB o GB
     *
     * @param length
     * @return
     */
    private static String ajustarTamanoArchivo(long length) {

        final long KB = 1024;
        final long MB = KB * 1024;
        final long GB = MB * 1024;

        if (length < MB) {
            return String.format("%.2f KB", (double) length / KB);
        } else if (length < GB) {
            return String.format("%.2f MB", (float) length / MB);
        } else {
            return String.format("%.2f GB", (float) length / GB);
        }

    }

    /**
     * AjustarFechaModificacion(long)
     * Recibe un long con los ms desde 1970 y con un objeto tipo SimpleDateFormat le doy formato
     * legible y con format lo convierto a String
     *
     * @param ultimaModificacion
     * @return
     */
    private static String ajustarFechaModificacion(long ultimaModificacion) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return sdf.format(ultimaModificacion);
    }

}






