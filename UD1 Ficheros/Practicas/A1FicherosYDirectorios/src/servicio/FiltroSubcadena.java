package servicio;

import java.io.File;
import java.io.FilenameFilter;

public class FiltroSubcadena implements FilenameFilter {

    private String subcadena;

    public FiltroSubcadena(String subcadena) {
        this.subcadena = subcadena;
    }

    // Añado los toLowerCase para que no diferencie mayúsculas de minúsculas
    @Override
    public boolean accept(File dir, String name) {
        return name.toLowerCase().contains(subcadena.toLowerCase());
    }
}
