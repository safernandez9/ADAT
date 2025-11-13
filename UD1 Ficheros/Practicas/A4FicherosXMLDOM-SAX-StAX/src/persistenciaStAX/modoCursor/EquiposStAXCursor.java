package persistenciaStAX.modoCursor;

import persistenciaDOM.ExcepcionXML;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.util.Map;

public class EquiposStAXCursor {


    public static void escribirDonacionesTotales(String rutaSalida, Map<String, Double> mapaDonaciones){

        XMLStreamWriter writer = XMLStAXUtilsCursor.crearWriterStAX(rutaSalida);

        XMLStAXUtilsCursor.ADDDeclaracion(writer);
        XMLStAXUtilsCursor.ADDSaltoLinea(writer, 0);

        XMLStAXUtilsCursor.ADDStartElemento(writer, "donaciones");      // Raiz <donaciones>

        for(Map.Entry<String, Double> entrada: mapaDonaciones.entrySet()){
            String nombre = entrada.getKey();
            String total = String.format("%.1f",entrada.getValue());

            XMLStAXUtilsCursor.ADDSaltoLinea(writer, i);
            XMLStAXUtilsCursor.ADDStartElemento(writer, "patrocinador");
            XMLStAXUtilsCursor.ADDAtributo(writer, "totalDonado", total);
            XMLStAXUtilsCursor.ADDTextoElemento(writer, nombre);
            XMLStAXUtilsCursor.ADDEndElemento(writer);
        }

        // Cerrar raiz
        XMLStAXUtilsCursor.ADDSaltoLinea(writer, 0);
        XMLStAXUtilsCursor.ADDEndElemento(writer);      //</donaciones>

        XMLStAXUtilsCursor.ADDEndDocumento(writer);

    } catch(Exception e){
        System.err.println("Error inesperado al generar DonacionesTotales.xml." + e.getMessage());        // POR QUE UN PRINT AQUI
    } finally {
        if(writer != null){
            try{
                // SE CIERRA SIEMPRE EN StAX O COMO VA?
                write.flush();
                writer.close();
            }
            catch(XMLStreamException e){
                throw new ExcepcionXML("Error al cerrar el writer." + e.getMessage());
            }
        }
    }
}
