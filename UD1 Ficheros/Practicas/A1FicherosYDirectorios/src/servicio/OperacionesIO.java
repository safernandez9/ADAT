package servicio;

import excepciones.*;

import java.io.*;
import java.util.List;

public class OperacionesIO {

    /**
     * visualizarContenido(String)
     * Recibe una ruta a un directorio, llama a su validación y muestra un listado de
     * su contenido
     *
     * @param ruta
     * @throws NoEsDirectorioException
     * @throws DirectorioNoExisteException
     */
    public static void visualizarContenido(String ruta) throws NoEsDirectorioException, DirectorioNoExisteException {
        System.out.println("\n\n-- LISTANDO EL DIRECTORIO " + ruta);

        File dir = new File(ruta);
        Utilidades.validarDirectorio(dir);

        for (File f : dir.listFiles()) {
            Utilidades.mostrarInfo(f);

        }
    }

    /**
     * recorrerRecursivo(String)
     * Recibe una ruta a un directorio, llama a su validación y llama a un método
     * que lo recorre recursivamente mostrando la información de sus descendientes
     *
     * @param ruta
     * @throws NoEsDirectorioException
     * @throws DirectorioNoExisteException
     */
    public static void recorrerRecursivo(String ruta) throws NoEsDirectorioException, DirectorioNoExisteException {
        System.out.println("\n\n-- LISTANDO DIRECTORIO " + ruta + " Y SUBDIRECTORIOS -- ");

        File dir = new File(ruta);
        Utilidades.validarDirectorio(dir);

        Utilidades.recorrerRec(dir, 1);
    }

    /**
     * filtrarPorExtension(String, String)
     * Recibe la ruta de un directorio y la valida. A continuación valida la extensión recibida como parámetro.
     * Crea un filtro tipo FiltroExcepcion y llama a un método que le devuelve una lista con los archivos del
     * directorio pasado por parametros con el filtro pasado tambien como parametro.
     * Si no encuentra ninguno lo muestra en pantalla, si los ecuentra muestra su información.
     * String
     *
     * @param ruta
     * @param extension
     * @throws NoEsDirectorioException
     * @throws DirectorioNoExisteException
     * @throws ExtensionIncorrectaException
     */
    public static void filtrarPorExtension(String ruta, String extension) throws NoEsDirectorioException, DirectorioNoExisteException, ExtensionIncorrectaException {

        System.out.println("\n\n-- LISTANDO MIEMBROS DEL DIRECTORIO " + ruta + "  CON EXTENSIÓN " + extension + "-- ");

        List<File> listaCoincidentes;

        File dir = new File(ruta);
        Utilidades.validarDirectorio(dir);
        Utilidades.validarExtension(extension);

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
     * Recibe la ruta de un directorio y la valida. A continuación valida la extensión recibida como parámetro.
     * Crea un filtro tipo FiltroExcepcion y llama a un método que le devuelve una lista con los archivos del
     * directorio pasado por parametros con el filtro pasado tambien como parametro.
     * Si no encuentra ninguno lo muestra en pantalla, si los ecuentra muestra su información.
     * Además según el valor del parámetro descendente ordena los resultados.
     *
     * @param ruta
     * @param extension
     * @param descendente
     * @throws NoEsDirectorioException
     * @throws DirectorioNoExisteException
     */
    public static void filtrarPorExtensionYOrdenar(String ruta, String extension, boolean descendente) throws NoEsDirectorioException, DirectorioNoExisteException, ExtensionIncorrectaException {

        String orden = descendente ? "DESCENDENTE" : "ASCENDENTE";

        System.out.println("\n\n-- LISTANDO MIEMBROS DEL DIRECTORIO " + ruta + "  CON EXTENSIÓN " + extension +
                " ORDENADOS DE MANERA " + orden + "-- ");

        List<File> listaCoincidentes;

        File dir = new File(ruta);
        Utilidades.validarDirectorio(dir);
        Utilidades.validarExtension(extension);

        FiltroExtension filter = new FiltroExtension(extension);

        listaCoincidentes = Utilidades.filtrar(dir, filter);

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

        List<File> listaCoincidentes;

        File dir = new File(ruta);
        Utilidades.validarDirectorio(dir);

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
     * copiarArchivo(String, String )
     * Creo los File y valido la ruta de origen. Si no existe el destino creo su estructura con mkdirs,
     * en caso de error se imprime. Creo los flujos de lectura y escritura de bytes. Voy leyendo de 8KB en 8 KB y escribiendo
     * mientras haya archivo que leer.
     *
     * @param origen
     * @param destino
     * @throws ArchivoNoExisteException
     * @throws NoEsArchivoException
     */
    public static void copiarArchivo(String origen, String destino) throws ArchivoNoExisteException, NoEsArchivoException, IOException {

        System.out.println("\n\n-- COPIANDO EL ARCHIVO " + origen + " a " + destino + " --");

        File fOrigen = new File(origen);
        File dirDestino = new File(destino);
        Utilidades.validarArchivo(fOrigen);

        // Si la carpeta no existe creo su estructura
        if (!dirDestino.exists()) {
            if (!dirDestino.mkdirs()) {
                System.out.println("Error: No se pudo crear el directorio destino: "
                        + dirDestino.getAbsolutePath());
                return;
            }
        }

        // Archivo destino sobre el que abro el flujo = carpeta destino
        // + nombre del archivo original (Creará el archivo en la carpeta dirDestino)
        File fDestino = new File(dirDestino, fOrigen.getName());

        //Abro los flujos para los archivos de origen y destino para lectura y escritura de bytes respectivamente.
        try (InputStream in = new FileInputStream(fOrigen);
             OutputStream out = new FileOutputStream(fDestino)) {

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
     * Realiza lo mismo que copiarArchivo con la diferencia de que esta vez si todo va bien
     * borra el fichero origen.
     *
     * @param origen
     * @param destino
     */
    public static void moverArchivo(String origen, String destino) throws ArchivoNoExisteException, NoEsArchivoException, ErrorBorradoException {

        File fOrigen = new File(origen);
        File dirDestino = new File(destino);
        Utilidades.validarArchivo(fOrigen);

        File fDestino = new File(dirDestino, fOrigen.getName());

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
     * Copia toda una estructura de directorios comenzada en origen al directorio destino. Para ello
     * valida la ruta origen, apunta con un file a la ruta del nuevo directorio y lo crea.
     * Posteriormente llama a copiarRecursivo que irá copiando toda la estructura de directorios y archivos.
     *
     * @param origen
     * @param destino
     * @throws DirectorioNoExisteException
     * @throws NoEsDirectorioException
     */
    public static void copiarDirectorio(String origen, String destino) throws DirectorioNoExisteException, NoEsDirectorioException {

        File dirOrigen = new File(origen);
        Utilidades.validarDirectorio(dirOrigen);

        //Usando esto la ruta apuntada por File no será la de destino, si no destino + /nombreCarpetaOrigen
        //No crea aun, solo apunta, la creará luego con el mkdirs()
        File dirDestino = new File(destino, dirOrigen.getName());

        //Si dirDestino no existe lo creo, si falla muestra error por pantalla
        if (!dirDestino.exists()) {
            if (!dirDestino.mkdirs()) {
                System.out.println("Error: No se pudo crear el directorio destino: "
                        + dirDestino.getAbsolutePath());
                return;

            }
        }
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

      /*  boolean exito = true;

        //Si no existe creo la carpeta recibida como destino
        //Si falla la creación, el boolean impedirá que mas adelante se intente copiar el contenido
        //del directorio origen
        if (!destino.exists()) {
            if (!destino.mkdirs()) {
                System.out.println("Error: No se pudo crear el directorio destino: "
                        + destino.getAbsolutePath());
                exito = false;
            }
        }

        //Recorro  el directorio origen copiando archivos en el destino y copiando recursivamente directorios con su contenido
        for (File f : origen.listFiles()) {
            try {
                if (f.isFile()) {
                    copiarArchivo(f.getPath(), destino.getPath());
                } else if (f.isDirectory() && exito) {
                    File nuevoDirectorio = new File(destino.getPath(), f.getName());
                    copiarDirectorio(f.getPath(), nuevoDirectorio.getPath());
                }
            } catch (IOException e) {
                System.out.println("Error al copiar el archivo " + e.getMessage());
            } catch (ArchivoNoExisteException | NoEsArchivoException | DirectorioNoExisteException | NoEsDirectorioException e ) {
                System.out.println(e.getMessage());
            };

        }*/
    }

    /** borrar(String)
     * Borra un directorio o fichero
     * @param ruta
     * @throws ArchivoNoExisteException
     * @throws DirectorioNoExisteException
     * @throws ErrorBorradoException
     */
    public static void borrar(String ruta) throws ArchivoNoExisteException, DirectorioNoExisteException, ErrorBorradoException {

        File f = new File(ruta);

        //Manejo de excepciones
        if (!f.exists()) {
            if (f.isDirectory()) {
                throw new DirectorioNoExisteException(ruta);
            } else {
                throw new ArchivoNoExisteException(ruta);
            }
        }

        if (f.isDirectory()) {
            borrarDirectorioRecursivo(f);
        } else {
            borrarArchivo(f);
        }
    }

    /**
     * borrarDirectorioRecursivo(File)
     * Recorre recursivamente una estructura de directorios borrandola.
     *
     * @param dir
     * @throws ErrorBorradoException
     */
    private static void borrarDirectorioRecursivo(File dir) throws ErrorBorradoException {

        for (File f : dir.listFiles()) {
            if (f.isDirectory()) {
                borrarDirectorioRecursivo(f); // recursión para subdirectorios
            } else {
                borrarArchivo(f);
            }
        }

        //Si no hay f que leer Borrar el propio directorio
        if (dir.delete()) {
            System.out.println("Directorio borrado: " + dir.getAbsolutePath());
        } else {
            throw new ErrorBorradoException(dir.getPath());
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


