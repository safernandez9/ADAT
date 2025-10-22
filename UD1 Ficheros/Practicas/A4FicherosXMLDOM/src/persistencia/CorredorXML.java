package persistencia;

// En persistencia va todo lo que acceda directamente a los archivos XML
// PERSISTENCIA NO MANDA MENSAJES DE ERROR Y ES PARA LO QUE TOCA EL XML DIRECTAMENTE, es decir usa throws en los catch

import clases.Corredor;
import clases.Fondista;
import clases.Puntuacion;
import clases.Velocista;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CorredorXML {

    /**
     * Llama a la creacion del Document pasando como parametros la ruta en String y la validacion en Enum.
     * Propaga las excepciones, no las maneja aquí
     *
     * @param rutaXML
     * @param validacion
     * @return
     * @throws ExcepcionXML
     */
    public Document cargarDocumentoDOM(String rutaXML, TipoValidacion validacion) throws ExcepcionXML {
        return XMLDOMUtils.cargarDocumentoXML(rutaXML, validacion);
    }

    public List<Corredor> cargarCorredores(Document doc) {
        List<Corredor> lista = new ArrayList();
        Element raiz = doc.getDocumentElement();
        NodeList nodos = raiz.getChildNodes();

        for (int i = 0; i < nodos.getLength(); i++) {
            //Si es una instancia de nodo elemento (buscar google)
            if (nodos.item(i) instanceof Element corredorElem) {
                Corredor corredor = crearCorredor(corredorElem);
                if (corredor != null) {
                    lista.add(corredor);
                }
            }
        }
        return lista;
    }

    /**
     * Crea un objeto corredor a partir de un nodo Corredor que recibe de un DOM
     *
     * @param corredorElem
     * @return
     */
    public Corredor crearCorredor(Element corredorElem) {
        String codigo = corredorElem.getAttribute("codigo");
        int dorsal = Integer.parseInt(corredorElem.getAttribute("dorsal"));
        String equipo = corredorElem.getAttribute("equipo");
        //Conseguir texto de la etiqueta nombre
        String nombre = XMLDOMUtils.obtenerTexto(corredorElem, "nombre");
        LocalDate fecha = LocalDate.parse(XMLDOMUtils.obtenerTexto(corredorElem, "fecha"));

        Corredor corredor = switch (corredorElem.getTagName()) {
            case "fondista" -> {
                float distancia = Float.parseFloat(XMLDOMUtils.obtenerTexto(corredorElem, "distancia"));
                // Yield funciona como un return en un switch
                yield new Fondista(codigo, dorsal, nombre, fecha, equipo, distancia);
            }
            case "velocista" -> {
                float velocidad = Float.parseFloat(XMLDOMUtils.obtenerTexto(corredorElem, "velocidad"));
                yield new Velocista(codigo, dorsal, nombre, fecha, equipo, velocidad);
            }
            default -> null;
        };

        // Si acabo de construir un corredor en el switch
        if (corredor != null) {
            corredor.setHistorial(cargarHistorial(corredorElem));
        }

        return corredor;

    }

    private List<Puntuacion> cargarHistorial(Element corredorElem) {
        List<Puntuacion> historial = new ArrayList();
        Element historialElem = (Element) corredorElem.getElementsByTagName("historial").item(0);

        if (historialElem != null) {
            NodeList puntuaciones = historialElem.getElementsByTagName("puntuacion");
            for (int i = 0; i < puntuaciones.getLength(); i++) {
                Element punt = (Element)
                        puntuaciones.item(i);
                int anio = Integer.parseInt(punt.getAttribute("anio"));
                float puntos = Float.parseFloat(punt.getTextContent());
                historial.add(new Puntuacion(anio, puntos));
            }
        }
        return historial;
    }
//Hacer el de que si le doy un codigo de cprredpr me devuelva el corredor
}