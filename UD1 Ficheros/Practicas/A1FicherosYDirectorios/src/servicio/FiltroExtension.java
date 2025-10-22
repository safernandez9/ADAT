package servicio;

import java.io.File;
import java.io.FilenameFilter;

/* Implementacion estándar de un filtro, recibe el archivo
    y su nombre. Trata la condicion que quiera con el String del nombre
        y devuelve true o false para el File tratado */

public class FiltroExtension implements FilenameFilter {

    String extension;

    FiltroExtension(String extension) {
        this.extension = extension;
    }

    //Método de la interfaz FilenameFilter. Debe sobreescribirse
    @Override
    public boolean accept(File dir, String name) {
        return name.endsWith(extension);
    }
}
