import clases.Puntuacion;
import logica.GestorCorredores;
import persistenciaDOM.TipoValidacion;

private static final String RUTA = "ArchivosXMLDTD/Corredores.xml";
private static final GestorCorredores gestor = new GestorCorredores();

public static void main(String[] args) {

    //Lidiar con cambios de formato en fechas
    // DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    // LocalDate fecha = LocalDate.parse(fechaTexto, formato);
    // En todas las partes donde antes tenia el parseo básico de LocalDate.parse(fechaTexto);


    System.out.println("=============================");
    System.out.println("TRABAJO CON DOM");
    System.out.println("============================");


    System.out.println("\nCargando documento XML con validación DTD...\n");
    gestor.cargarDocumentoDOM(RUTA, TipoValidacion.DTD);
//
//    System.out.println("\nListado de corredores cargados desde el documento XML:\n");
//    gestor.listarCorredoresDOM();
//
//
//    // Corredores por ID 2 válidos y 1 inválido
//
//    System.out.println("\n\nImprimir corredor con el ID 'C03':\n");
//    gestor.mostrarCorredorPorIDDOM("C03");
//
//    System.out.println("\n\nImprimir corredor con el ID 'C02':\n");
//    gestor.mostrarCorredorPorIDDOM("C02");
//
//    System.out.println("\n\nImprimir corredor con el ID 'C99' (no existente):\n");
//    gestor.mostrarCorredorPorIDDOM("C99");
//
//    // Corredores por dorsal 2 válidos y 1 inválido
//
//    System.out.println("\n\nImprimir corredor con el dorsal 2:\n");
//    gestor.mostrarCorredorPorDorsalDOM(2);
//
//    System.out.println("\n\nImprimir corredor con el dorsal 3:\n");
//    gestor.mostrarCorredorPorDorsalDOM(3);
//
//    System.out.println("\n\nImprimir corredor con el dorsal 999 (no existente):\n");
//    gestor.mostrarCorredorPorDorsalDOM(999);
//
//    // Añadir corredores
//
//    System.out.println("\n\nAñadiendo nuevos corredores al documento XML...\n");
//    gestor.añadirNuevoCorredorDOM();
//    gestor.listarCorredoresDOM();
//
//    // Eliminar corredores por codigo
//    System.out.println("\n\nElimino corredor con ID 'C08' del documento XML...\n");
//    gestor.eliminarCorredorPorIDDOM("C08");
//    gestor.eliminarCorredorPorIDDOM("C09");
//    gestor.eliminarCorredorPorIDDOM("C99"); // No existente
//
//    System.out.println("\nListado de corredores tras las eliminaciones:\n");
//    gestor.listarCorredoresDOM();
//
//    // Modificar puntuación corredor
//
//    System.out.println("\n\nModifico la puntuación del corredor con ID 'C01'...\n");
//    gestor.añadirOModificarPuntuacionDOM("C01", new Puntuacion(2023, (float)48.7));
//    System.out.println("\n\nAñado una puntuación del corredor con ID 'C01'\n");
//    gestor.añadirOModificarPuntuacionDOM("C01", new Puntuacion(2024, (float)54.3));
//    System.out.println("\n\nAñado una puntuación del corredor con ID 'C99' (no existente)\n");
//    gestor.añadirOModificarPuntuacionDOM("C99", new Puntuacion(2024, (float)54.3));
//    System.out.println("\nListado de corredores tras las modificaciones de puntuación:\n");
//    gestor.listarCorredoresDOM();
//
//    // Eliminar puntuación corredor
//
//    System.out.println("\n\nElimino la puntuación del año 2022 del corredor con ID 'C01'...\n");
//    gestor.eliminarPuntuacionDOM("C01", 2022);
//    System.out.println("\n\nIntento eliminar la puntuación del año 2025 del corredor con ID 'C01' (no existente)...\n");
//    gestor.eliminarPuntuacionDOM("C01", 2025);
//    System.out.println("\n\nIntento eliminar la puntuación del año 2022 del corredor con ID 'C99' (corredor no existente)...\n");
//    gestor.eliminarPuntuacionDOM("C99", 2022);
//    System.out.println("\nListado de corredores tras las eliminaciones de puntuación:\n");
//    gestor.listarCorredoresDOM();
//
//    // Guardar documento XML (Para probarlo, está tambien en la capa lógica de las funciones que se piden)
//
//    System.out.println("\n\nGuardando el documento XML tras las modificaciones...\n");
//    gestor.guardarDocumentoDOM("ArchivosXMLDTD/CorredoresParaSobreescribir.xml");
//    gestor.listarCorredoresDOM();

      // Buscar con XPath

//    System.out.println("\n\nBúsqueda de corredores con velocidad media superior a 25.0:\n");
//    gestor.buscarCorredoresPorVelocidadMediaXPath((float)10.0);

    // TRABAJO SAX

    System.out.println("\n\n=============================");
    System.out.println("TRABAJO CON SAX");
    System.out.println("============================");

//    // Cargar corredores SAX
//
//    System.out.println("\n\nCargando documento XML con validación DTD...\n");
//    gestor.cargarDocumentoSAX(RUTA, TipoValidacion.DTD);
//
//    // Listar corredores SAX
//
//    System.out.println("\n\nListado de corredores cargados desde el documento XML:\n");
//    gestor.mostrarCorredoresSAX(RUTA, TipoValidacion.DTD);
//
//    // Corredores por equipo SAX 1 válidos y 1 inválido
//
//    System.out.println("\n\nListado de corredores del equipo 'E3' cargados desde el documento XML:\n");
//    gestor.mostrarCorredoresPorEquipoSAX(RUTA, "E3", TipoValidacion.DTD);
//    System.out.println("\n\nListado de corredores del equipo 'E99' (no existente) cargados desde el documento XML:\n");
//    gestor.mostrarCorredoresPorEquipoSAX(RUTA, "E99", TipoValidacion.DTD);
//
//    // Corredores por equipo con DOM 1 válidos y 1 inválido
//
//    System.out.println("\n\nListado de corredores del equipo 'E3' cargados desde el documento XML con DOM:\n");
//    gestor.mostrarCorredoresPorEquipoDOM("E3");
//    System.out.println("\n\nListado de corredores del equipo 'E99' (no existente) cargados desde el documento XML con DOM:\n");
//    gestor.mostrarCorredoresPorEquipoDOM("E99");

    // LEER CON DOM UN ARCHIVO, LEER OTRO CON SAX, ACTUALIZAR LOS VALORES DEL DOM CON LOS DEL SAX Y REESCRIBIR EL DOM A UN XML

    String rutaAActualizar = "ArchivosXMLDTD/Corredores.xml";
    String rutaActualizaciones = "ArchivosXMLDTD/Actualizaciones.xml";

    System.out.println("\n\nActualizando el documento XML con los datos cargados por SAX...\n");
    gestor.actualizarDOMconSAX(rutaAActualizar, rutaActualizaciones, TipoValidacion.DTD, TipoValidacion.NO_VALIDAR);






//    public List<Corredor> leerCorredoresDesdeArchivo(String rutaArchivo) {
//        List<Corredor> corredores = new ArrayList<>();
//        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
//            String linea;
//            while ((linea = br.readLine()) != null) {
//// Ignorar líneas vacías
//                if (linea.trim().isEmpty()) continue;
//
//```
//                // Suponiendo formato: tipo;codigo;dorsal;nombre;fecha_nacimiento;equipo;valor
//                // valor = velocidad_media para velocista, distancia_max para fondista
//                String[] campos = linea.split(";");
//                if (campos.length < 7) continue; // ignorar líneas mal formadas
//
//                String tipo = campos[0].trim().toLowerCase();
//                String codigo = campos[1].trim();
//                int dorsal = Integer.parseInt(campos[2].trim());
//                String nombre = campos[3].trim();
//                LocalDate fecha = LocalDate.parse(campos[4].trim());
//                String equipo = campos[5].trim();
//                float valor = Float.parseFloat(campos[6].trim());
//
//                Corredor c;
//                if (tipo.equals("velocista")) {
//                    c = new Velocista();
//                    ((Velocista) c).setVelocidadMedia(valor);
//                } else if (tipo.equals("fondista")) {
//                    c = new Fondista();
//                    ((Fondista) c).setDistanciaMax(valor);
//                } else {
//                    continue; // tipo desconocido
//                }
//
//                c.setCodigo(codigo);
//                c.setDorsal(dorsal);
//                c.setNombre(nombre);
//                c.setEquipo(equipo);
//                c.setFechaNacimiento(fecha);
//
//                corredores.add(c);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return corredores;
//}


}

