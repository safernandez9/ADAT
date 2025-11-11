package logica;

/**
 * La capa Lógica:
 * Responsabilidad: Gestionar la lógica de feedback
 * al usuario, y coordinar la persistencia. Es como el director de orquesta de la aplicación.
 * Es el único que "habla" con la clase principal (el main) y es el único que "habla" con la
 * capa de persistencia (CorredorXML). Hay impresión de datos.
 */

import clases.Corredor;
import org.w3c.dom.Document;
import persistenciaDOM.CorredorXML;
import persistenciaDOM.ExcepcionXML;
import persistenciaDOM.TipoValidacion;
import persistenciaSAX.CorredoresSAX;
import persistenciaStAX.modoCursor.CorredoresStAXCursor;
import persistenciaStAX.modoCursor.XMLStAXUtilsCursor;
import persistenciaStAX.modoEventos.CorredoresStAXEventos;
import persistenciaStAX.modoEventos.XMLStAXUtilsEventos;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamReader;
import java.util.List;


public class GestorCorredores {


    private final CorredorXML gestorDOM;
    private final CorredoresSAX gestorSAX;
    private Document documentoXML;
    private final CorredoresStAXCursor gestorStAXCursor;
    private final CorredoresStAXEventos gestorStAXEventos;


    /**
     *  El constructor inicializa los gestores para las diferentes metodologías
     *  creando uno nuevo por cada una, DOM, SAX, StAXCursor y StAXEventos
     */
    public GestorCorredores() {
        this.gestorDOM = new CorredorXML();
        this.gestorSAX = new CorredoresSAX();
        this.gestorStAXCursor = new CorredoresStAXCursor();
        this.gestorStAXEventos = new CorredoresStAXEventos();
    }

    /**
     *  LLama a cargarDocumento de CorredorXML
     * @param rutaXML String con la ruta del fichero
     * @param validacion Enum con el tipo de validacion (DTD, XSD o ninguna)
     * @throws ExcepcionXML
     */
    public void cargarDocumento(String rutaXML, TipoValidacion validacion) throws ExcepcionXML {
        try{
            this.documentoXML = gestorDOM.cargarDocumentoDOM(rutaXML, validacion);
            System.out.println("Documento XML cargado correctamente");
        }
        catch(ExcepcionXML e){
            System.err.println("Error al cargar documento XML: " + e.getMessage());
        }
    }

    /**
     * LLama a cargarDocumento de CorredorSAX
     * @param rutaXML String con la ruta del fichero
     * @param validacion Enum con el tipo de validación
     * @throws ExcepcionXML
     */
    public void cargarDocumentoSAX(String rutaXML, TipoValidacion validacion) throws ExcepcionXML {
        gestorSAX.cargarCorredores(rutaXML, validacion);
    }

    /**
     * Función que recibe de CorredorXML un List de Corredores y la muestra en función de su método toString()
     */
    public void mostrarCorredoresDOM(){
        List<Corredor> lista = gestorDOM.cargarCorredores(documentoXML);
        for(Corredor c : lista){
            System.out.println(c);
        }
    }

    /**
     * Función que recibe de CorredorXML un List de Corredores y la muestra en función de su método toString()
     */
    public void mostrarCorredoresSAX(String ruta, TipoValidacion validacion) {
        List<Corredor> lista = gestorSAX.cargarCorredores(ruta, validacion);
        for(Corredor c : lista){
            System.out.println(c);
        }
    }

    public void mostrarCorredoresStAXCursor(String ruta, TipoValidacion validacion) {
        try{
            XMLStreamReader reader = XMLStAXUtilsCursor.cargarDocumentoStAXCursor(ruta, validacion);
            List <Corredor> lista = gestorStAXCursor.leerCorredores(reader);

            System.out.println("Lista de corredores (StAX Cursor");
            for(Corredor c : lista){
                System.out.println(c);
            }

        }catch(ExcepcionXML e){
            System.err.println("Error al leer corredores ocn StAX Cursor: " + e.getMessage());
        }
    }

    public void mostrarCorredoresStAXEventos(String ruta, TipoValidacion validacion) {
        try{
            XMLEventReader reader = XMLStAXUtilsEventos.cargarDocumentoStAXEventos(ruta, validacion);
            List <Corredor> lista = gestorStAXEventos.leerCorredores(reader);

            System.out.println("Lista de corredores (StAX Evento");
            for(Corredor c : lista){
                System.out.println(c);
            }

        } catch(ExcepcionXML e){
            System.err.println("Error al leer corredores con StAX Eventos: " + e.getMessage());
        }
    }





}
