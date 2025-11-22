package servicio;

import excepciones.*;

import java.io.*;
import java.util.List;

/**
 * Anotacion importante:
 *
 * Si el archivo o directorio no existe, File.isFile() e isDirectory() devuelven false siempre;
 */
public class OperacionesIO {

    /**
     * visualizarContenido(String)
     * <p>
     * Recibe una ruta a un directorio, llama a su validación y muestra un listado de
     * su contenido. Uso el Array en lugar de un foreach directo para evitar recibir null.
     *
     * @param ruta ruta al directorio
     * @throws NoEsDirectorioException
     * @throws DirectorioNoExisteException
     */
    public static void visualizarContenido(String ruta) throws NoEsDirectorioException, DirectorioNoExisteException {
        System.out.println("\n\n-- LISTANDO EL DIRECTORIO " + ruta);

        File dir = new File(ruta);
        Utilidades.validarDirectorioIO(dir);


        File[] ficheros = dir.listFiles();
        if (ficheros != null) {
            for (File f : ficheros) {
                Utilidades.mostrarInfo(f);
            }
        }

    }

    /**
     * recorrerRecursivo(String)
     * <p>
     * Recibe una ruta a un directorio, llama a su validación y llama a un método
     * que lo recorre recursivamente mostrando la información de sus descendientes
     *
     * @param ruta String con la ruta al directorio
     * @throws NoEsDirectorioException
     * @throws DirectorioNoExisteException
     */
    public static void recorrerRecursivo(String ruta) throws NoEsDirectorioException, DirectorioNoExisteException {
        System.out.println("\n\n-- LISTANDO DIRECTORIO " + ruta + " Y SUBDIRECTORIOS -- ");
        File dir = new File(ruta);
        Utilidades.validarDirectorioIO(dir);

        Utilidades.recorrerRec(dir, 1);
    }

    /**
     * filtrarPorExtension(String, String)
     * <p>
     * Recibe la ruta de un directorio y la valida.
     * A continuación valida la extensión recibida como parámetro.
     * Crea un filtro tipo FiltroExcepcion y llama a un método que le devuelve una lista con los archivos del
     * directorio pasado por parametros con el filtro pasado tambien como parametro.
     * Si no encuentra ninguno lo muestra en pantalla, si los encuentra muestra su información.
     *
     * @param ruta      String con la ruta del directorio a filtrar
     * @param extension extensión a filtrar en formato .ext
     * @throws NoEsDirectorioException
     * @throws DirectorioNoExisteException
     * @throws ExtensionIncorrectaException
     */
    public static void filtrarPorExtension(String ruta, String extension) throws NoEsDirectorioException, DirectorioNoExisteException, ExtensionIncorrectaException {

        System.out.println("\n\n-- LISTANDO MIEMBROS DEL DIRECTORIO " + ruta + "  CON EXTENSIÓN " + extension + "-- ");

        // Creo la lista donde almacenaré los archivos coincidentes
        List<File> listaCoincidentes;

        // Validaciones
        File dir = new File(ruta);
        Utilidades.validarDirectorioIO(dir);
        Utilidades.validarExtension(extension);

        // Creo el filtro y obtengo la lista de archivos coincidentes
        FiltroExtension filter = new FiltroExtension(extension);
        listaCoincidentes = Utilidades.filtrar(dir, filter);

        if (listaCoincidentes.isEmpty()) {
            System.out.println("\nNingún archivo con la extension " + extension + " en el directorio " + ruta);
        } else {
            for (File f : listaCoincidentes) {
                Utilidades.mostrarInfo(f);
            }
        }
    }

    /**
     * filtrarPorExtensionYOrdenar(String, String, boolean)
     *
     * Recibe la ruta de un directorio y la valida.
     * A continuación valida la extensión recibida como parámetro.
     * Crea un filtro tipo FiltroExcepcion y llama a un método que le devuelve una lista con los archivos del
     * directorio pasado por parametros con el filtro pasado tambien como parametro.
     * Si no encuentra ninguno lo muestra en pantalla, si los ecuentra muestra su información.
     * Además según el valor del parámetro descendente ordena los resultados.
     *
     * @param ruta     String con la ruta del directorio a filtrar
     * @param extension extensión a filtrar en formato .ext
     * @param descendente boolean para indicar el orden (true = descendente, false = ascendente)
     * @throws NoEsDirectorioException
     * @throws DirectorioNoExisteException
     */
    public static void filtrarPorExtensionYOrdenar(String ruta, String extension, boolean descendente) throws NoEsDirectorioException, DirectorioNoExisteException, ExtensionIncorrectaException {

        // Mensaje inicial
        String orden = descendente ? "DESCENDENTE" : "ASCENDENTE";
        System.out.println("\n\n-- LISTANDO MIEMBROS DEL DIRECTORIO " + ruta + "  CON EXTENSIÓN " + extension +
                " ORDENADOS DE MANERA " + orden + "-- ");

        // Creo la lista donde almacenaré los archivos coincidentes
        List<File> listaCoincidentes;

        // Validaciones
        File dir = new File(ruta);
        Utilidades.validarDirectorioIO(dir);
        Utilidades.validarExtension(extension);

        // Creo el filtro y obtengo la lista de archivos coincidentes
        listaCoincidentes = Utilidades.filtrar(dir,  new FiltroExtension(extension));

        /* listaCoincidentes.sort((f1, f2) -> f1.getName().compareTo(f2.getName()));
        * sort() recibe un Comparator que define cómo comparar dos elementos de la lista.
        * (f1, f2) -> ... es la lambda que compara dos File.
        * f1.getName().compareTo(f2.getName()) devuelve:
        * <0 si f1 < f2
        *  0 si f1 == f2
        * >0 si f1 > f2
        * según el orden alfabético de los nombres.
        * Esto es equivalente a usar un Comparator clásico pero más conciso con lambda.
        * */
        if (listaCoincidentes.isEmpty()) {
            System.out.println("\nNingún archivo con la extension " + extension + " en el directorio " + ruta);
        } else {
            if (descendente) {
                listaCoincidentes.sort((f1, f2) -> f2.getName().compareTo(f1.getName()));
            } else {
                listaCoincidentes.sort((f1, f2) -> f1.getName().compareTo(f2.getName()));
            }

            for (File f : listaCoincidentes) {
                Utilidades.mostrarInfo(f);
            }
        }
    }

    /**
     * filtrarPorSubcadena(String, String)
     *
     * Recibe la ruta de un directorio y la valida.
     * Crea un filtro tipo FiltroSubcadena y llama a un método que le devuelve una lista con los archivos del
     * directorio pasado por parametros con el filtro pasado tambien como parametro.
     * Si no encuentra ninguno lo muestra en pantalla, si los encuentra muestra su información.
     *
     * @param ruta
     * @param subcadena
     * @throws NoEsDirectorioException
     * @throws DirectorioNoExisteException
     */
    public static void filtrarPorSubcadena(String ruta, String subcadena) throws NoEsDirectorioException, DirectorioNoExisteException {
        System.out.println("\n\n-- LISTANDO MIEMBROS DEL DIRECTORIO " + ruta + " QUE CONTIENEN LA SUBCADENA " + subcadena + " --");

        // Creo la lista donde almacenaré los archivos coincidentes
        List<File> listaCoincidentes;

        // Validaciones
        File dir = new File(ruta);
        Utilidades.validarDirectorioIO(dir);

        // Creo el filtro y obtengo la lista de archivos coincidentes
        listaCoincidentes = Utilidades.filtrar(dir, new FiltroSubcadena(subcadena));

        if (listaCoincidentes.isEmpty()) {
            System.out.println("\nNingún archivo que contenga \"" + subcadena + "\" en el directorio " + ruta);
        } else {
            for (File f : listaCoincidentes) {
                Utilidades.mostrarInfo(f);
            }
        }


    }

    /**
     * copiarArchivo(String, String)
     *
     * Creo los File y valido la ruta de origen. Si no existe el destino creo su estructura con mkdirs,
     * en caso de error se imprime.
     * Creo los flujos de lectura y escritura de bytes. Voy leyendo de 8KB en 8 KB y escribiend mientras haya archivo que leer.
     *
     * @param origen fichero origen
     * @param destino directorio destino
     * @throws ArchivoNoExisteException
     * @throws NoEsArchivoException
     */
    public static void copiarArchivo(String origen, String destino) throws ArchivoNoExisteException, NoEsArchivoException, IOException {

        System.out.println("\n\n-- COPIANDO EL ARCHIVO " + origen + " a " + destino + " --");

        // Creo los File y valido el archivo de origen
        File fOrigen = new File(origen);
        File dirDestino = new File(destino);
        Utilidades.validarArchivoIO(fOrigen);

        // Si la carpeta no existe creo su estructura
        if (!dirDestino.exists()) {
            if (!dirDestino.mkdirs()) {
                System.out.println("Error: No se pudo crear el directorio destino: "
                        + dirDestino.getAbsolutePath());
                return;
            }
        }

        // Creo el File del archivo destino
        File fDestino = new File(dirDestino, fOrigen.getName());

        // Abro los flujos para los archivos de origen y destino para lectura y escritura de bytes respectivamente.
        try (InputStream in = new BufferedInputStream(new FileInputStream(fOrigen));
             OutputStream out = new BufferedOutputStream(new FileOutputStream(fDestino))) {

            //Buffer donde iré guardando los bytes que leeré del origen en cada iteración y los escribirá en el destino
            //Usamos 8192 bytes (8KB) ya que es un estándar
            byte[] buffer = new byte[8192];
            int bytesLeidos;

            //Mintras no sea -1 (fin de archivo)
            while ((bytesLeidos = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesLeidos);
            }

            System.out.println("Archivo copiado correctamente a: " + dirDestino.getAbsolutePath());


        } catch (IOException e) {
            System.out.println("Error al copiar el archivo: " + e.getMessage());
            //Throw e relanza la excepcion para poder usarla en moverArchivo
            throw e;
        }
    }

    /**
     * moverArchivo(String, String)
     *
     * Realiza lo mismo que copiarArchivo con la diferencia de que esta vez si todo va bien
     * borra el fichero origen.
     *
     * @param origen
     * @param destino
     */
    public static void moverArchivo(String origen, String destino) throws ArchivoNoExisteException, NoEsArchivoException, ErrorBorradoException {

        // Creo los File y valido la ruta de origen
        File fOrigen = new File(origen);
        File dirDestino = new File(destino);
        Utilidades.validarArchivoIO(fOrigen);

        // Creo el File del archivo destino
        File fDestino = new File(dirDestino, fOrigen.getName());

        // Intento copiar el archivo
        try {
            copiarArchivo(fOrigen.getPath(), dirDestino.getPath());
            System.out.println("Archivo movido correctamente a: " + dirDestino.getAbsolutePath());

            //Al acabar borro el archivo del origen
            borrarArchivo(fOrigen);

        } catch (IOException e) {
            System.out.println("Error al copiar el archivo: " + e.getMessage());
            //Si falla la escritura a medias borro el archivo corrupto del destino
            if (fDestino.exists()) {
                borrarArchivo(fDestino);
            }
        }


    }

    /**
     * copiarDirectorio(String, String)
     *
     * Copia toda una estructura de directorios comenzada en el directorio origen al directorio destino.
     * Para ello valida la ruta origen, apunta con un file a la ruta del nuevo directorio y lo crea.
     * Posteriormente, llama a copiarRecursivo que irá copiando toda la estructura de directorios y archivos.
     *
     * Copia A dentro de B. Si quiero copiar solo contenido de A dentro de B debo pasar B/A como destino
     * File dirDestino = new File(destino);
     * copiarRecursivo(dirOrigen, dirDestino);
     *
     * @param origen directorio origen
     * @param destino directorio destino
     * @throws DirectorioNoExisteException
     * @throws NoEsDirectorioException
     */
    public static void copiarDirectorio(String origen, String destino) throws DirectorioNoExisteException, NoEsDirectorioException {

        System.out.println("\n\n-- COPIANDO EL DIRECTORIO " + origen + " a " + destino + " --");

        // Creo el File y valido el directorio de origen
        File dirOrigen = new File(origen);
        Utilidades.validarDirectorioIO(dirOrigen);

        // Creo el File del nuevo directorio destino
        File dirDestino = new File(destino, dirOrigen.getName());

        //Si dirDestino no existe lo creo, si falla muestra error por pantalla
        if (!dirDestino.exists()) {
            if (!dirDestino.mkdirs()) {
                System.out.println("Error: No se pudo crear el directorio destino: "
                        + dirDestino.getAbsolutePath());
                return;

            }
        }

        //Llamo al método recursivo que copiará toda la estructura
        copiarRecursivo(dirOrigen, dirDestino);
    }

    /**
     * copiarRecursivo(File, File)
     * copia toda una estructura de directorios recursivamente
     *
     * @param origen
     * @param destino
     */
    public static void copiarRecursivo(File origen, File destino) {

        // Intento crear el directorio destino si no existe
        if (!destino.exists() && !destino.mkdirs()) {
            System.out.println("Error: No se pudo crear el directorio destino: " + destino.getAbsolutePath());
            return; // No podemos continuar si no se crea
        }

        // Listo los ficheros del directorio origen
        File[] ficheros = origen.listFiles();
        if (ficheros == null) {
            System.out.println("No se pudo listar el contenido de: " + origen.getAbsolutePath());
            return;
        }

        // Recorro los ficheros y directorios
        for (File f : ficheros) {
            try {
                if (f.isFile()) {
                    copiarArchivo(f.getPath(), destino.getPath());
                } else if (f.isDirectory()) {
                    File nuevoDir = new File(destino, f.getName());
                    copiarRecursivo(f, nuevoDir);
                }
            } catch (IOException e) {
                System.out.println("Error al copiar el archivo " + f.getAbsolutePath() + ": " + e.getMessage());
            } catch (ArchivoNoExisteException | NoEsArchivoException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }


    /**
     * borrar(String)
     * Borra un directorio o fichero
     *
     * @param ruta
     * @throws ArchivoNoExisteException
     * @throws DirectorioNoExisteException
     * @throws ErrorBorradoException
     */
    public static void borrar(String ruta) throws ArchivoNoExisteException, DirectorioNoExisteException, ErrorBorradoException {
        File f = new File(ruta);

        // Compruebo si existe
        if (!f.exists()) {
                throw new ArchivoNoExisteException(ruta);
        }

        // Si es un directorio, borro recursivamente su contenido
        if (f.isDirectory()) {
            File[] elementos = f.listFiles();
            if (elementos != null) {
                for (File elemento : elementos) {
                    borrar(elemento.getAbsolutePath());
                }
            }
        }

        // Intento borrar el archivo o directorio
        if (!f.delete()) {
            throw new ErrorBorradoException(f.getAbsolutePath());
        } else {
            System.out.println("Borrado: " + f.getAbsolutePath());
        }


    }

    /**
     * borrarArchivo(File)
     * Borra un archivo. Si no lo consigue lanza una excepción
     *
     * @param f
     * @throws ErrorBorradoException
     */
    public static void borrarArchivo(File f) throws ErrorBorradoException {
        if (!f.delete()) {
            throw new ErrorBorradoException(f.getAbsolutePath());
        }
    }
}


