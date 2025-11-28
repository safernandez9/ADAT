// Saúl Fernández Salgado

package logica;

import clases.Fotografo;
import persistencia.EscrituraTexto;
import persistencia.FotografosRandom;
import persistenciaXML.FotografosStAXCursor;
import persistenciaXML.XMLStAXUtilsCursor;
import utilidades.ExcepcionFicheros;
import utilidades.ExcepcionXML;

import javax.xml.stream.XMLStreamReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GestorFotografos {

    FotografosStAXCursor gestorXML;
    FotografosRandom gestorRAND;

    public GestorFotografos(String rutaXML, String rutaRAND) {
        gestorXML = new FotografosStAXCursor();
        gestorRAND = new FotografosRandom(rutaRAND);
    }

    /**
     * Añade los fotografos a una lista y llama a un metodo que solo lista los borrados
     */
    public List<Fotografo> leerFotografosRAND() {

        gestorRAND.abrirArchivo();

        Fotografo fotografo;
        List<Fotografo> listaFotografos = new ArrayList<>();

        for (int id = 1; id <= gestorRAND.numeroRegistrosConBorrados(); id++) {
            try {
                fotografo = gestorRAND.leerFotografoPorID(id);
                if (fotografo != null) {
                    listaFotografos.add(fotografo);
                }
            } catch (RuntimeException e) {
                System.out.println("Error de persistencia al leer el equipo con ID " + id + ": " + e.getMessage());
            }
        }

        return listaFotografos;
    }

    /**
     * Visualiza una lista de fotografos excluyendo los borrados
     * @param listaFotografos
     */
    public void visualizarFotografosSinBorrados(List<Fotografo> listaFotografos) {
        for (Fotografo f : listaFotografos) {
            System.out.println("---------------------------------------------------\n");
            if (!f.isBorrado()) {
                System.out.println(f.toString());
            }
            System.out.println("---------------------------------------------------\n");
        }
    }


    /**
     * Escribe los fotografos filtrados por estudio en un txt
     * @param rutaTXT
     */
    public void escribirFotografosTXT(String rutaTXT) {
        try {
            List<Fotografo> lista = leerFotografosRAND();
            EscrituraTexto writer = new EscrituraTexto(rutaTXT);
            writer.abrirArchivo();

            List<String> listaEstudios = new ArrayList<>();
            for (Fotografo f : lista) {
                if (!listaEstudios.contains(f.getEstudio())) {
                    listaEstudios.add(f.getEstudio());
                }
            }

            writer.escribirLinea("----------------------------------------------");

            for (String estudio : listaEstudios) {

                writer.escribirLinea("Estudio: " + estudio);
                writer.escribirLinea("----------------------------------------------");
                for (Fotografo f : lista) {
                    if (f.getEstudio().equals(estudio)) {
                        writer.escribirLinea("Codigo: " + f.getCodigo() + ", Nombre: " + f.getNombre() + ", Licencia: " + f.getNumLicencia());
                    }
                }
            }

        }catch(ExcepcionFicheros e){
            System.err.println("Error al escribir el archivo: " + e.getMessage());
        }

    }

    /**
     * Lee un XML con StAX, muestra el resultado por pantalla y lo escribe en un fichero binario
     * @param ruta
     */
    public void mostrarFotografosStAXCursor(String ruta) {
        try {
            XMLStreamReader reader = XMLStAXUtilsCursor.cargarDocumentoStAXCursor(ruta);
            List<Fotografo> lista = gestorXML.leerFotografos(reader, gestorRAND.calcularNuevoID());

            System.out.println("Lista de fotógrafos (StAX Cursor)");
            for (Fotografo c : lista) {
                System.out.println(c);
            }

            escribirFotografosEnFichero(lista);

        } catch (ExcepcionXML | IOException e) {
            System.err.println("Error al leer corredores ocn StAX Cursor: " + e.getMessage());
        }
    }

    /**
     * Va enviando fotografos para ir escribiendolos en un archivo
     * @param lista
     */
    private void escribirFotografosEnFichero(List<Fotografo> lista) throws IOException {
        for (Fotografo f : lista) {
            añadirFotografosRAND(f);
        }
    }

    /**
     * Valida y llama a escribir un fotografo en el archivo binarios
     * @param f
     */
    public void añadirFotografosRAND(Fotografo f) throws IOException {

        if (f == null) {
            throw new ExcepcionXML("Error: Fotógrafo nulo");
        }

        if (f.getCodigo() <= 0) {
            System.out.println("ID de fotógrafo inválido: " + f.getCodigo());
            return;
        }

//        if(gestorRAND.buscarLicenciaEnArchivo(f.getNumLicencia())){
//            System.err.println("Fotografo con licencia " + f.getNumLicencia() + " ya existe el el fichero.");
//            return;
//        }

        try {

            // Validar tamaño maximo del registro
            if (f.bytesSerializacionFotografo() > FotografosRandom.TAM_REGISTRO) {
                System.out.println("El equipo excede el tamaño máximo permitido de "
                        + FotografosRandom.TAM_REGISTRO + " bytes después de agregar/actualizar el patrocinador.");
                return;
            }
            gestorRAND.escribirFotografo(f);

        } catch (RuntimeException e) {
            System.out.println("Error de persistencia al agregar/actualizar el patrocinador: " + e.getMessage());
        }
    }
}


