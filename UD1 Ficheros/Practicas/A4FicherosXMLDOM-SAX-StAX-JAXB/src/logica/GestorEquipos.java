package logica;

import jakarta.xml.bind.JAXBException;
import persistenciaDOM.ExcepcionXML;
import persistenciaJAXB.clasesJAXB.EquipoJAXB;
import persistenciaJAXB.clasesJAXB.EquiposJAXB;
import persistenciaJAXB.XMLJAXBUtils;

public class GestorEquipos {



    //COMPROBAR EXCEPCION
    public void mostrarEquipos(){
        try{
            EquiposJAXB equipos = XMLJAXBUtils.unmarshall(EquiposJAXB.class, "ArchivosXMLSDTD/Equipos.xml");
        }catch(ExcepcionXML | JAXBException e){
            System.out.println(e.getMessage());
        }
    }

    public void mostrarEquiposJAXB(String rutaXML){

        EquiposJAXB equipos = EquiposJAXB.leerEquipos(rutaXML);

        for(EquipoJAXB e : equipos){
            System.out.println(e);
        }


    }

    public void escribirEquipos(String rutaXML){
        try{
            EquiposJAXB.escribirEquipos(rutaXML);
        }
        catch (ExcepcionXML e){
            System.out.println("Error al escribir equipos XML: " + e.getMessage());
        }

    }
}
