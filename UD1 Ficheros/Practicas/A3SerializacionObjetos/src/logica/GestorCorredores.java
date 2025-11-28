package logica;

import modelo.Corredor;
import modelo.Fondista;
import modelo.Puntuacion;
import modelo.Velocista;
import persistencia.CorredorRead;
import persistencia.CorredorWrite;

import java.util.ArrayList;
import java.util.List;

public class GestorCorredores {

    private final String rutaArchivo = "corredores.dat";

    /**
     * Guarda un corredor en el archivo binario, asignándole un dorsal único
     *
     * @param c El corredor a guardar
     */
    public void guardarCorredor(Corredor c) {
        if (c == null) {
            System.out.println("Corredor inválido (null).");
            return;
        }

        CorredorWrite write = new CorredorWrite(rutaArchivo);
        CorredorRead read = new CorredorRead(rutaArchivo);
        write.iniciarEscritura(); // HAGO ESTO O CREO EL ARCHIVO ANTES DE LEER EL ULTIMO DORSAL?

        try {
            // Obtener el último dorsal asignado, dárselo al corredor y guardarlo
            int ultimoDorsal = read.obtenerUltimoDorsal();
            c.setDorsal(ultimoDorsal + 1);

            write.iniciarEscritura();
            write.escribir(c);
            System.out.println("Corredor " + c.getNombre() + " guardado con dorsal: " + c.getDorsal());
        } catch (IllegalArgumentException e) {
            System.out.println("Error al guardar corredor: " + e.getMessage());
        } catch (IllegalStateException e) {
            System.out.println("Error de estado del archivo: " + e.getMessage());
        } catch (RuntimeException e) {
            System.out.println("Error inesperado al guardar corredor: " + e.getMessage());
        } finally {
            try {
                write.finalizarEscritura();
            } catch (Exception ignored) {
                // Si falla el cierre, no podemos hacer mucho más asi que me da igual, lo importante está hecho
                System.out.println("Error al cerrar el archivo de corredores después de guardar la lista.");
            }
        }


    }

    /**
     * Guarda una lista de corredores en el archivo binario, asignándoles dorsales únicos
     *
     * @param listaCorredores La lista de corredores a guardar
     */
    public void guardarListaCorredores(List<Corredor> listaCorredores) {

        if (listaCorredores == null || listaCorredores.isEmpty()) {
            System.out.println("La lista de corredores está vacía o es nula.");
            return;
        }

        CorredorWrite write = new CorredorWrite(rutaArchivo);
        CorredorRead read = new CorredorRead(rutaArchivo);

        try {
            // Obtener el último dorsal asignado
            int ultimoDorsal = read.obtenerUltimoDorsal();
            write.iniciarEscritura();
            try {
                // Por cada corredor le doy un dorsal nuevo y lo guardo
                for (Corredor c : listaCorredores) {
                    if (c != null) {
                        ultimoDorsal++;
                        c.setDorsal(ultimoDorsal);
                        try {
                            write.escribir(c);
                            System.out.println("Corredor " + c.getNombre() + " guardado con dorsal: " + c.getDorsal());
                        } catch (IllegalArgumentException e) {
                            System.out.println("Error lógico al guardar corredor " + c.getNombre() + ": " + e.getMessage());
                            ultimoDorsal--; // Revertir incremento si no se guarda
                        } catch (IllegalStateException e) {
                            System.out.println("Error de estado escribir: " + e.getMessage());
                            ultimoDorsal--; // Revertir incremento si no se guarda
                        } catch (RuntimeException e) {
                            System.out.println("Error inesperado al guardar corredor " + c.getNombre() + ": " + e.getMessage());
                        }
                        ultimoDorsal--; // Revertir incremento si no se guarda
                    }
                }
            } finally {
                try {
                    write.finalizarEscritura();
                } catch (Exception ignored) {
                    // Si falla el cierre, no podemos hacer mucho más asi que me da igual, lo importante está hecho
                    System.out.println("Error al cerrar el archivo de corredores después de guardar la lista.");
                }
            }
        } catch (RuntimeException e) {
            System.out.println("No se pudo iniciar la operación de guardado: " + e.getMessage());
        }
    }

    /**
     * Busca un corredor por su dorsal.
     * Llama a la clase de persistencia correspondiente
     *
     * @param dorsal Dorsal a buscar
     * @return El corredor encontrado o null si no existe
     */
    public Corredor buscarCorredorPorDorsal(int dorsal) {
        CorredorRead read = new CorredorRead(rutaArchivo);
        return read.buscarPorDorsal(dorsal);
    }

    /**
     * Muestra por consola la información de un corredor dado su dorsal
     * Llama a buscarCorredorPorDorsal y visualizaCorredor
     *
     * @param dorsal Dorsal del corredor a mostrar
     */
    public void mostrarCorredorPorDorsal(int dorsal) {
        Corredor corredor = buscarCorredorPorDorsal(dorsal);

        if (corredor == null) {
            System.out.println("El corredor es nulo. No se encontró ningún corredor con el dorsal: " + dorsal);
            return;
        }

        // Compruebo el tipo de corredor para mostrarlo
        String tipoCorredor = "";

        if(corredor instanceof Fondista) {
            tipoCorredor = "CORREDOR FONDISTA";
        } else if (corredor instanceof Velocista){
            tipoCorredor = "CORREDOR VELOCISTA";
        }

        //  Doy formato a la visualización e imprimo con visualizarCorredor
        System.out.println(tipoCorredor + ": DORSAL " + corredor.getDorsal());
        visualizarCorredor(corredor);
    }

    /**
     * Muestra por consola la información detallada de un corredor
     *
     * @param corredor Corredor a visualizar
     */
    public void visualizarCorredor(Corredor corredor) {
        if (corredor == null) {
            System.out.println("Corredor nulo. No se puede visualizar.");
            return;
        }

        // Doy formato a la visualización
        System.out.println("==========================================");
        System.out.println(corredor.toString());

        // Como el historial no está en el toString, lo muestro aparte
        if (corredor.getHistorial() != null && !corredor.getHistorial().isEmpty()) {
            System.out.println("Historial de Puntuaciones:");
            int numPuntuaciones = corredor.getHistorial().size();
            String etiqueta = (numPuntuaciones == 1) ? "PUNTUACIÓN" : "PUNTUACIONES";
            System.out.printf("%s: %s\n", etiqueta, corredor.getHistorial().toString());
        } else {
            System.out.println("No hay puntuaciones en el historial.");
        }
        System.out.println("==========================================");
    }

    /**
     * Muestra la información detallada de todos los corredores registrados leyendo directamente
     * desde el archivo SIN volcar todos los objetos a una lista.
     */
    public void listarTodosLosCorredores() {
        CorredorRead reader = new CorredorRead(rutaArchivo);
        Corredor c;
        int contador = 0;

        System.out.println("\n--- INICIO DE LISTADO DE CORREDORES ---");

        // 1. Inicia la lectura (abre el ObjectInputStream)
        reader.iniciarLectura();
        try {
            // 2. Bucle de lectura y procesamiento.
            while ((c = reader.leerCorredor()) != null) {
                // Muestra cada corredor inmediatamente después de leerlo

                // Crear el Encabezado ---
                String tipoCorredor = "";
                if (c instanceof Fondista) {
                    tipoCorredor = "CORREDOR FONDISTA";
                } else if (c instanceof Velocista) {
                    tipoCorredor = "CORREDOR VELOCISTA";
                }

                // Mostrar encabezado
                System.out.println("\n" + tipoCorredor + " : DORSAL " + c.getDorsal());

                // Llama al método de visualización que formatea la salida.
                visualizarCorredor(c);
                contador++;
            }
        } catch (Exception e) {
            System.err.println("Error inesperado durante la lectura de corredores: " + e.getMessage());
        } finally {
            reader.finalizarLectura();
        }

        if (contador == 0) {
            System.out.println("El fichero de corredores está vacío. No hay datos para mostrar.");
        } else {
            System.out.println("--- FIN DEL LISTADO. Total de corredores mostrados: " + contador + " ---");
        }
    }

    /**
     * Elimina un corredor del archivo binario por su dorsal. Se usa un archivo auxiliar para escribir
     * los corredores a excepción del que se desea borrar.
     *
     * @param dorsal Número de dorsal del corredor a eliminar
     */
    public void eliminarCorredor(int dorsal) {
        if (dorsal <= 0) {
            System.out.println("Dorsal invalido: " + dorsal);
            return;
        }

        final String ARCHIVO_AUX = "auxiliar.dat"; // Constante para archivo auxiliar

        // Compruebo que el corredor exista
        CorredorRead readCheck = new CorredorRead(rutaArchivo);
        if (readCheck.buscarPorDorsal(dorsal) == null) {
            System.out.println("No se encontró el corredor con dorsal: " + dorsal);
            return;
        }

        CorredorRead read = new CorredorRead(rutaArchivo);
        CorredorWrite write = new CorredorWrite(ARCHIVO_AUX);
        try {
            read.iniciarLectura();
            write.iniciarEscritura();
            Corredor c;

            // Reescribo todos los corredores menos el que quiero borrar (El del mismo dorsal)
            while ((c = read.leerCorredor()) != null) {
                if (c.getDorsal() != dorsal) {
                    write.escribir(c);
                }
            }
        } catch (RuntimeException e) {
            System.out.println("Error durante la operación de eliminación: " + e.getMessage());
            // Elimino auxiliar si se creó parcialmente
            write.borrarArchivo();
        } finally {
            read.finalizarLectura();
            write.finalizarEscritura();
        }

        if (!read.borrarArchivo()) {
            System.out.println("Error al borrar el archivo de corredores: " + rutaArchivo);
            return;
        }

        if (!write.renombrarArchivo(rutaArchivo)) {
            System.out.println("No se pudo renombrar el archivo auxiliar: " + ARCHIVO_AUX);
            return;
        }

        System.out.println("Corredor con dorsal: " + dorsal + " borrado");


    }

    /**
     * Agrega o actualiza una puntuación en el historial de un corredor. Si el
     * año ya existe, actualiza la puntuación. Si no existe, añade la puntuación.
     *
     * @param dorsal          Número de dorsal del corredor
     * @param nuevaPuntuacion Puntuación a agregar o actualizar
     */
    public void agregarPuntuacion(int dorsal, Puntuacion nuevaPuntuacion) {
        if (dorsal <= 0) {
            System.out.println("Dorsal inválido: " + dorsal);
            return;
        }

        final String ARCHIVO_AUX = "auxiliar.dat"; // Constante para archivo auxiliar

        // Compruebo que el corredor exista
        CorredorRead readCheck = new CorredorRead(rutaArchivo);
        if (readCheck.buscarPorDorsal(dorsal) == null) {
            System.out.println("No se encontró el corredor con dorsal: " + dorsal);
            return;
        }

        CorredorRead read = new CorredorRead(rutaArchivo);
        CorredorWrite write = new CorredorWrite(ARCHIVO_AUX);
        Corredor c;

        try {
            while ((c = read.leerCorredor()) != null) {
                if (c.getDorsal() == dorsal) {
                    // Inicializo historial si está vacío
                    List<Puntuacion> historial = c.getHistorial();
                    if (historial == null) {
                        historial = new ArrayList<>();
                    }
                    boolean actualizado = false;
                    // Si el historial está vacío el bucle no hace nada y se añade la nueva puntuación directamente
                    for (Puntuacion p : historial) {
                        if (p.getAnio() == nuevaPuntuacion.getAnio()) {
                            // Año ya existe: actualizamos
                            p.setPuntos(nuevaPuntuacion.getPuntos());
                            actualizado = true;
                            break;
                        }
                    }

                    // Si no estaba ese año, lo añadimos
                    if (!actualizado) {
                        historial.add(nuevaPuntuacion);
                    }

                    // Ordenamos por año
                    historial.sort((p1, p2) -> Integer.compare(p1.getAnio(), p2.getAnio()));
                    c.setHistorial(historial);
                }

                // Escribimos en el auxiliar (modificado o no)
                write.escribir(c);
            }
        } catch (RuntimeException e) {
            System.out.println("Error durante la operación de actualización: " + e.getMessage());
            // Intentar eliminar auxiliar si se creó parcialmente
            write.borrarArchivo();
        } finally {
            // Cerramos los flujos
            read.finalizarLectura();
            write.finalizarEscritura();
        }

        if (!read.borrarArchivo()) {
            System.out.println("Error al borrar el archivo de corredores: " + rutaArchivo);
            return;
        }

        if (!write.renombrarArchivo(rutaArchivo)) {
            System.out.println("No se pudo renombrar el archivo auxiliar: " + ARCHIVO_AUX);
            return;
        }

        System.out.println("Puntuación agregada o actualizada correctamente para el corredor " + dorsal);

    }
}