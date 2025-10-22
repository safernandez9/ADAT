package logica;

import clases.Corredor;
import org.w3c.dom.Document;
import persistencia.CorredorXML;
import persistencia.ExcepcionXML;
import persistencia.TipoValidacion;

import java.util.List;

//IMPRESION Y PEDIDA DE DATOS

public class GestorCorredores {

    private final CorredorXML gestor;
    private Document documentoXML;

    public GestorCorredores() {
        this.gestor = new CorredorXML();
    }

    public void cargarDocumento(String rutaXML, TipoValidacion validacion) throws ExcepcionXML {
        try{
            this.documentoXML = gestor.cargarDocumentoDOM(rutaXML, validacion);
            System.out.println("Documento XML cargado correctamente");
        }
        catch(ExcepcionXML e){
            System.err.println("Error al cargar documento XML: " + e.getMessage());
        }
    }

    public void mostrarCorredores(){
        List<Corredor> lista = gestor.cargarCorredores(documentoXML);
        for(Corredor c : lista){
            System.out.println(c);
        }
    }

}
