package servicio;

import modelo.Corredor;
import modelo.Fondista;
import modelo.Puntuacion;
import modelo.Velocista;
import persistencia.CorredorRead;
import persistencia.CorredorWrite;

import java.util.ArrayList;
import java.util.List;

public class GestorCorredores {

    private final String rutaArchivo = "Corredores.dat";

    /**
     * Guarda un corredor en un archivo binario asignandole un nuevo dorsal.
     * Aquí capturo las excepciones que lanza persistencia
     *
     * @param c
     */
    public void guardarCorredor(Corredor c) {

        if (c == null) {
            System.out.println("Corredor Inválido");
            return;
        }

        // Instancio las clases necesarias del paquete persistencia
        CorredorWrite write = new CorredorWrite(rutaArchivo);
        CorredorRead read = new CorredorRead(rutaArchivo);

        try {
            // Obtengo último dorsal
            int ultimoDorsal = read.obtenerUltimoDorsal();
            c.setDorsal(ultimoDorsal + 1);

            // Escribo el nuevo corredor
            write.iniciarEscritura();
            if (write.escribir(c)) {
                System.out.println("Corredor guardado correctamente");
            }


        } catch (IllegalArgumentException e) {
            // Error lógico (equipo no existe o está borrado)
            System.out.println("Error guardando corredor" + e.getMessage());
        } catch (IllegalStateException e) {
            // Error de flujo o archivo no abierto
            System.out.println("Error de estado del archivo: " + e.getMessage());
        } catch (RuntimeException e) {
            // Error general de E/S u otro problema en la capa persistencia
            System.out.println("Error inesperado al guardar corredor" + e.getMessage());
        } finally {
            try {
                write.finalizarEscritura();
            } catch (Exception ignored) {

            }
        }
    }

    /**
     * Guarda una List de objetos corredor en el fichero de persistencia
     *
     * @param lista
     */
    public void guardarListaCorredores(List<Corredor> lista) {
        if (lista == null || lista.isEmpty()) {
            System.out.println("Lista Vacia o Nula");
            return;
        }

        CorredorRead read = new CorredorRead(rutaArchivo);
        CorredorWrite write = new CorredorWrite(rutaArchivo);

        try {
            int dorsal = read.obtenerUltimoDorsal();

            write.iniciarEscritura();
            try {
                for (Corredor c : lista) {
                    dorsal++;
                    c.setDorsal(dorsal);

                    try {
                        write.escribir(c);
                        System.out.println("Corredor guardado :" + c.getDorsal() + " - " + c.getNombre());
                    } catch (IllegalArgumentException e) {
                        // Error lógico (equipo no existe o está borrado)
                        System.out.println("Error guardando corredor" + e.getMessage());
                        dorsal--;
                    } catch (IllegalStateException e) {
                        // Error de flujo o archivo no abierto
                        System.out.println("Error de estado del archivo: " + e.getMessage());
                        dorsal--;
                        throw e;
                    } catch (RuntimeException e) {
                        // Error general de E/S u otro problema en la capa persistencia
                        System.out.println("Error inesperado al guardar corredor" + c.getNombre() + ": " + e.getMessage());
                        dorsal--;
                    }
                }
            } finally {
                try {
                    write.finalizarEscritura();
                } catch (Exception e) {
                    System.out.println("Error al cerrar el archivo tras guardar la lista: " + e.getMessage());
                }
            }
        } catch (RuntimeException e) {
            // Error al iniciar lectura o escritura
            System.out.println("No se pudo iniciar la operación de guardado" + e.getMessage());
        }
    }

    /**
     * Busca un corredor por dorsal. No imprime, solo devuelve el objeto o null
     *
     * @param dorsal
     * @return
     */
    public Corredor buscarCorredorPorDorsal(int dorsal) {
        CorredorRead reader = new CorredorRead(rutaArchivo);
        return reader.buscarPorDorsal(dorsal);
    }

    /**
     * Busca un corredor por dorsal y muestra su info si lo encuentra.
     *
     * @param dorsal
     */
    public void mostrarCorredorPorDorsal(int dorsal) {

        // Busco al corredor
        Corredor c = buscarCorredorPorDorsal(dorsal);
        if (c == null) {
            System.out.println("No se pudo mostrar la información. El objeto corredor es nulo");
            return;
        }

        // Creo el encabezado según su tipo

        String tipoCorredor = "";
        if (c instanceof Fondista) {
            tipoCorredor = "Fondista";
        } else if (c instanceof Velocista) {
            tipoCorredor = "Velocista";
        }

        // Imprimo el encabezado y visualizo al corredor
        System.out.println(tipoCorredor + ": " + c.getDorsal());
        visualizarCorredor(c);
    }

    /**
     * Se vale del método toString de Corredor para mostrar la información de un corredor
     * y muestra su historial de puntuaciones si lo tiene
     *
     * @param c
     */
    public void visualizarCorredor(Corredor c) {
        if (c == null) {
            System.out.println("No se puede mostrar información: El objeto corredores es nulo.");
            return;
        }
        System.out.println("========================================================================");

        // 1. Imprime el toString() o de la subclase
        System.out.println(c.toString());

        // 2. Muestra el historial en la línea de abajo.
        if (c.getHistorial() != null && !c.getHistorial().isEmpty()) {
            // Usa List.toString() para imprimir el historial detallado.
            int numPuntuaciones = c.getHistorial().size();
            // Define la etiqueta singular o plural
            String etiqueta = (numPuntuaciones == 1) ? "PUNTUACIÓN" : "PUNTUACIONES";
            System.out.printf("Msg: %s%n", etiqueta, c.getHistorial().toString());
        } else {
            System.out.println("PUNTUACIONES: Sin datos registrados.");
        }

        System.out.println("===========================================================================");
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
            while ((c = reader.leer()) != null) {
                // Muestra cada corredor inmediatamente después de leerlo

                // Crear el Encabezado ---
                String tipoCorredor = "";
                if (c instanceof Fondista) {
                    tipoCorredor = "CORREDOR FONDISTA";
                } else if (c instanceof Velocista) {
                    tipoCorredor = "CORREDOR VELOCISTA";
                }

                // Mostrar encabezado
                System.out.println("\n" + tipoCorredor + ": DORSAL " + c.getDorsal());

                // Llama al método de visualización que formatea la salida.
                visualizarCorredor(c);
                contador++;
            }
        } catch (Exception e) {
            System.out.println("Error inesperado durante la lectura de corredores: " + e.getMessage());
        } finally {
            // Finaliza la lectura (cierra el ObjectInputStream).
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
     * @param dorsal
     */
    public void eliminarCorredor(int dorsal) {
        if (dorsal <= 0) {
            System.out.println("Dorsal invalido: " + dorsal);
            return;
        }

        final String ARCHIVO_AUX = "auxiliar.dat";

        // Compruebo que el corredor exista
        CorredorRead readCheck = new CorredorRead(rutaArchivo);
        if (readCheck.buscarPorDorsal(dorsal) == null) {
            System.out.println("No se encontró el corredor con dorsal: " + dorsal);
            return;
        }

        CorredorRead read = new CorredorRead(rutaArchivo);
        CorredorWrite write = new CorredorWrite(rutaArchivo);
        try {
            read.iniciarLectura();
            write.iniciarEscritura();
            Corredor c;

            // Reescribo todos los corredores menos el que quiero borrar
            while ((c = read.leer()) != null) {
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
            while ((c = read.leer()) != null) {
                if (c.getDorsal() == dorsal) {
                    // Inicializo historial si está vacío
                    List<Puntuacion> historial = c.getHistorial();
                    if (historial == null)
                        historial = new ArrayList<>();

                    boolean actualizado = false;
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
        }

        System.out.println("Puntuación agregada o actualizada correctamente para el corredor " + dorsal);

    }
}

