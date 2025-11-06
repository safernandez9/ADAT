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
import persistencia.CorredorXML;
import persistencia.ExcepcionXML;
import persistencia.TipoValidacion;
import persistenciaSAX.CorredoresSAX;

import java.util.List;


public class GestorCorredores {


    private final CorredorXML gestorDOM;
    private final CorredoresSAX gestorSAX;
    private Document documentoXML;


    /**
     *  El constructor inicializa un único gestorDOM de Operaciones para Corredor DOM y otro para
     *  Corredor SAX
     */
    public GestorCorredores() {
        this.gestorDOM = new CorredorXML();
        this.gestorSAX = new CorredoresSAX();
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





}
