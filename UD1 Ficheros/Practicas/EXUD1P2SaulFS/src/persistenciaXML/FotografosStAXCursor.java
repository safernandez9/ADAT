// Saúl Fernández Salgado
package persistenciaXML;


import clases.Fotografia;
import clases.Fotografo;
import utilidades.ExcepcionXML;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FotografosStAXCursor {

    /**
     * Constructor vacío que usará el gestor
     */
    public FotografosStAXCursor() {
    }

    /**
     * Lee los fotografos de un XML utilizando la lectura de StAX con Cursor leyendo los elementos como
     * tokens
     *
     * @param reader Lector para recorrer el XML
     * @return Lista de fotografos del XML
     */
    public List<Fotografo> leerFotografos(XMLStreamReader reader, int codigoFotografo) throws ExcepcionXML {


        // Lista que irá guardando los fotografos
        List<Fotografo> fotografos = new ArrayList<>();

        // Variables para ir guardando los valores en la lectura
        Fotografo FotografoActual = null;
        List<Fotografia> fotografiasActuales = null;
        Fotografia f = new Fotografia();
        String anioActual = null;
        String contenidoActual = null;
        boolean licenciaValida;


        try {
            // Bucle principal
            while (reader.hasNext()) {
                int tipo = reader.next();
                switch (tipo) {
                    case XMLStreamConstants.START_ELEMENT -> {
                        String nombreEtiqueta = XMLStAXUtilsCursor.obtenerNombreEtiqueta(reader);
                        switch (nombreEtiqueta) {
                            case "Fotografos" -> {
                                // No hacemos nada al abrir el elemento raíz
                            }
                            case "Fotografo" -> {
                                licenciaValida = false;
                                FotografoActual = new Fotografo();
                                String licencia =  XMLStAXUtilsCursor.leerAtributo(reader, "NumeroLicencia");
                                if(comprobarLicencia(fotografos, licencia)){
                                    FotografoActual.setCodigo(codigoFotografo);
                                    codigoFotografo++;
                                    FotografoActual.setNumLicencia(licencia);
                                    licenciaValida = true;
                                }
                            }
                            case "Nombre", "Estudio" -> {
                                contenidoActual = "";
                            }
                            case "Fotografias" -> {
                                fotografiasActuales = new ArrayList<>();
                            }
                            case "Fotografia" -> {
                                f = new Fotografia();
                                contenidoActual = "";
                                f.setTipo(XMLStAXUtilsCursor.leerAtributo(reader, "Formato"));
                                f.setFechaToma(LocalDate.parse(XMLStAXUtilsCursor.leerAtributo(reader, "fecha")));
                            }
                            default -> throw new ExcepcionXML("Etiqueta no esperada: " + nombreEtiqueta);
                        }
                    }
                    case XMLStreamConstants.CHARACTERS -> {
                        contenidoActual = XMLStAXUtilsCursor.leerTexto(reader);
                    }
                    case XMLStreamConstants.END_ELEMENT -> {
                        String nombreEtiqueta = XMLStAXUtilsCursor.obtenerNombreEtiqueta(reader);
                        switch (nombreEtiqueta) {
                            case "Fotografos" -> {
                                // No hacemos nada al cerrar el elemento raíz
                            }
                            case "Fotografo" -> {
                                fotografos.add(FotografoActual);
                            }
                            case "Nombre" -> {
                                FotografoActual.setNombre(contenidoActual);
                            }
                            case "Estudio" -> {
                                FotografoActual.setEstudio(contenidoActual);
                            }
                            case "Fotografias" -> {
                                FotografoActual.setFotografias(fotografiasActuales);
                                FotografoActual.setNumFotografia(fotografiasActuales.size());
                            }
                            case "Fotografia" -> {
                                f.setTitulo(contenidoActual);
                                f.setTamanoMB((f.bytesSerializacionFotografia()));
                                fotografiasActuales.add(f);
                            }
                            default -> throw new ExcepcionXML("Etiqueta no esperada: " + nombreEtiqueta);
                        }
                    }

                }
            }
        } catch (XMLStreamException e) {
            throw new ExcepcionXML("Error al leer los fotografos: " + e.getMessage(), e);
        }
        return fotografos;
    }

    /**
     * Comprueba que la licencia no este repetida entre los fotografos ya leidos
     *
     * @param fotografos
     * @param licencia
     * @return
     */
    private boolean comprobarLicencia(List<Fotografo> fotografos, String licencia) {
        for(Fotografo f : fotografos){
            if(f.getNumLicencia().equals(licencia)){
                return false;
            }
        }

        return true;
    }

}