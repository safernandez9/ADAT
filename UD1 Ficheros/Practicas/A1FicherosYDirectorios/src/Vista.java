import excepciones.*;
import servicio.OperacionesIO;
import servicio.OperacionesNIO;

import java.io.IOException;
import java.util.Scanner;

public class Vista {
    public static void mostrarMenu() {

        Scanner sc = new Scanner(System.in);
        boolean salir = false, flagOrden = false;
        boolean ordenOrdenacion = false;
        int orden;
        String rutaOrigen, rutaDestino, filtro;


        do {
            System.out.println("-------------------------------------");
            System.out.println("Bienvenido");
            System.out.println("-------------------------------------");
            System.out.println();
            System.out.println("Selecciona una función:");
            System.out.println("1. Visualizar contenido");
            System.out.println("2. Recorrer directorio recursivamente");
            System.out.println("3. Filtrar por extensión");
            System.out.println("4. Filtrar por extensión y ordenar");
            System.out.println("5. Filtrar por subcadena");
            System.out.println("6. Copiar archivo");
            System.out.println("7. Mover archivo");
            System.out.println("8. Copiar directorio");
            System.out.println("9. Borrar archivo/directorio");
            System.out.println("0. Salir");
            System.out.print("Elige una opción: ");

            switch (sc.nextInt()) {
                case 0:
                    salir = true;
                    break;
                case 1:
                    System.out.println("\nIntroduzca un directorio para visualizar su contenido:");
                    rutaOrigen = sc.nextLine();
                    try {
                        System.out.println("\nMODO IO");
                        OperacionesIO.visualizarContenido(rutaOrigen);
                        System.out.println("\nMODO NIO");
                        OperacionesNIO.visualizarContenidoNIO(rutaOrigen);
                    } catch (NoEsDirectorioException | DirectorioNoExisteException e) {
                        System.out.println(e.getMessage());
                        ;
                    }
                    break;
                case 2:
                    System.out.println("\nIntroduzca un directorio para recorrer recursivamente:");
                    rutaOrigen = sc.nextLine();
                    try {
                        System.out.println("\nMODO IO");
                        OperacionesIO.recorrerRecursivo(rutaOrigen);
                        System.out.println("\nMODO NIO");
                        OperacionesNIO.recorrerRecursivoNIO(rutaOrigen);
                    } catch (NoEsDirectorioException | DirectorioNoExisteException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 3:
                    System.out.println("\nIntroduzca el directorio que quiere listar:");
                    rutaOrigen = sc.nextLine();
                    System.out.println("\nIntroduzca la extensión por la que quiere filtrar los resultados:");
                    filtro = sc.nextLine();
                    try {
                        System.out.println("\nMODO IO");
                        OperacionesIO.filtrarPorExtension(rutaOrigen, filtro);
                        System.out.println("\nMODO NIO");
                        OperacionesNIO.filtrarPorExtensionNIO(rutaOrigen, filtro);
                    } catch (NoEsDirectorioException | DirectorioNoExisteException | ExtensionIncorrectaException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 4:
                    System.out.println("\nIntroduzca el directorio que quiere listar y ordenar:");
                    rutaOrigen = sc.nextLine();
                    System.out.println("\nIntroduzca la extensión por la que quiere filtrar los resultados:");
                    filtro = sc.nextLine();
                    do {
                        System.out.println("\nIntroduzca 0 para ordenar de forma ascendente y 1 par ordenar de forma descendente");
                        orden = sc.nextInt();
                        if (orden == 0) {
                            ordenOrdenacion = false;
                            flagOrden = true;
                        } else if (orden == 1) {
                            ordenOrdenacion = true;
                            flagOrden = true;
                        } else {
                            System.out.println("\nSolo puede introducir 0 o 1");
                            flagOrden = false;
                        }
                    } while (!flagOrden);
                    try {
                        System.out.println("\nMODO IO");
                        OperacionesIO.filtrarPorExtensionYOrdenar(rutaOrigen, filtro, ordenOrdenacion);
                        System.out.println("\nMODO NIO");
                        OperacionesNIO.filtrarPorExtensionYOrdenarNIO(rutaOrigen, filtro, ordenOrdenacion);
                    } catch (NoEsDirectorioException | DirectorioNoExisteException | ExtensionIncorrectaException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 5:
                    System.out.println("\nIntroduzca el directorio que quiere listar:");
                    rutaOrigen = sc.nextLine();
                    System.out.println("\nIntroduzca la subcadena por la que quiere filtrar los resultados:");
                    filtro = sc.nextLine();
                    try {
                        System.out.println("\nMODO IO");
                        OperacionesIO.filtrarPorSubcadena(rutaOrigen, filtro);
                        System.out.println("\nMODO NIO");
                        OperacionesNIO.filtrarPorSubcadenaNIO(rutaOrigen, filtro);
                    } catch (NoEsDirectorioException | DirectorioNoExisteException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 6:
                    System.out.println("\nIntroduzca la ruta de un archivo para copiarlo:");
                    rutaOrigen = sc.nextLine();
                    System.out.println("\nIntroduzca la ruta del directorio de destino:");
                    rutaDestino = sc.nextLine();
                    try {
                        System.out.println("\nMODO IO");
                        OperacionesIO.copiarArchivo(rutaOrigen, rutaDestino);
                        System.out.println("\nMODO NIO");
                        OperacionesNIO.copiarArchivoNIO(rutaOrigen, rutaDestino);
                    } catch (ArchivoNoExisteException |
                             NoEsArchivoException | IOException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 7:
                    System.out.println("\nIntroduzca la ruta de un archivo para moverlo:");
                    rutaOrigen = sc.nextLine();
                    System.out.println("\nIntroduzca la ruta del directorio de destino:");
                    rutaDestino = sc.nextLine();
                    try {
                        System.out.println("\nMODO IO");
                        OperacionesIO.moverArchivo(rutaOrigen, rutaDestino);
                        System.out.println("\nMODO NIO");
                        OperacionesNIO.moverArchivoNIO(rutaOrigen, rutaDestino);
                    } catch (ArchivoNoExisteException | NoEsArchivoException | ErrorBorradoException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 8:
                    System.out.println("\nIntroduzca la ruta de un directorio para copiarlo:");
                    rutaOrigen = sc.nextLine();
                    System.out.println("\nIntroduzca la ruta del directorio de destino:");
                    rutaDestino = sc.nextLine();
                    try {
                        System.out.println("\nMODO IO");
                        OperacionesIO.copiarDirectorio(rutaOrigen, rutaDestino);
                        System.out.println("\nMODO NIO");
                        OperacionesNIO.copiarDirectorioNIO(rutaOrigen, rutaDestino);
                    } catch (
                            DirectorioNoExisteException | NoEsDirectorioException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 9:
                    System.out.println("\nIntroduzca la ruta de un directorio o archivo para borrarlo:");
                    rutaOrigen = sc.nextLine();
                    try {
                        System.out.println("\nMODO IO");
                        OperacionesIO.borrar(rutaOrigen);
                        System.out.println("\nMODO NIO");
                        OperacionesNIO.borrarNIO(rutaOrigen);
                    } catch (
                            DirectorioNoExisteException | ArchivoNoExisteException |
                            ErrorBorradoException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                default:
                    System.out.println("Opcion no permitida");
                    break;
            }
        } while (!salir);
    }
}
