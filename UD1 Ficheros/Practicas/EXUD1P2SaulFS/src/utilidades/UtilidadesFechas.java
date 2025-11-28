// Saúl Fernández Salgado
package utilidades;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class UtilidadesFechas {

    // Formato: dd-MMMM-yyyy  ->  05-Agosto-2025
    private static final DateTimeFormatter FORMATO_LARGO =
            DateTimeFormatter.ofPattern("dd-MMMM-yyyy", new Locale("es", "ES"));

    // Formato dd/MM/yyyy  -> 05/08/2025
    private static final DateTimeFormatter FORMATO_CORTO =
            DateTimeFormatter.ofPattern("dd-MM-yyyy");

    // COVERSION LocalDate <-> LONG

    /**
     * Convierte LocalDate a long usando epochDay. Si la fecha es null devuelve Long.MIN_VALUE
     * @param fecha LocalDate a convertir
     * @return long con el valor de epochDay o Long.MIN_VALUE si la fecha es null
     */
    public static long toLongFecha(LocalDate fecha) {
        return (fecha == null) ? Long.MIN_VALUE : fecha.toEpochDay();
    }

    /**
     * Convierte un longo guardado en fichero a LocalDate. Si es Long.MIN_VALUE devuelve null.
     * @param valor long a convertir
     * @return LocalDate o null si el valor es Long.MIN_VALUE
     */
    public static LocalDate fromLongFecha(long valor) {
        return (valor == Long.MIN_VALUE) ? null : LocalDate.ofEpochDay(valor);
    }



}

