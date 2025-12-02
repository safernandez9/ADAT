package logica;

import persistenciaJAXB.XMLJAXBUtils;
import persistenciaJAXB.clasesJAXB.EquipoJAXB;
import utilidades.ExcepcionXML;
import persistenciaJAXB.clasesJAXB.EquiposJAXB;


public class GestorEquipos {

    public void mostrarEquiposJAXB(String ruta){
        try{
            EquiposJAXB equipos = XMLJAXBUtils.unmarshall(EquiposJAXB.class, ruta);
            for(EquipoJAXB equipo : equipos.getEquipos()){
                System.out.println(equipo);
            }
        }catch(ExcepcionXML e){
            System.out.println(e.getMessage());
        }
    }



}
