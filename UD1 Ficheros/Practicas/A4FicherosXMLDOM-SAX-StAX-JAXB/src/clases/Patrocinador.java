package clases;

import utilidades.UtilidadesFechas;
import java.time.LocalDate;

public class Patrocinador {
    private String nombre;
    private float donacion;
    private LocalDate fechaInicio;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Patrocinador)) return false;
        Patrocinador that = (Patrocinador) o;
        return nombre != null && nombre.equalsIgnoreCase(that.nombre);
    }

    @Override
    public int hashCode() {
        return nombre == null ? 0 : nombre.toLowerCase().hashCode();
    }

    @Override
    public String toString() {
        return String.format("%s | Donación: %.2f | Inicio: %s",
                nombre, donacion, UtilidadesFechas.formatearCorto(fechaInicio));
    }
}


