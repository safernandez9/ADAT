package utilidades;

import jakarta.xml.bind.annotation.adapters.XmlAdapter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * XMLAdapter para convertir entre LocalDate y String en JAXB.
 * Permite serializar (marshal) y deserializar (unmarshal) fechas en formato yyyy-MM-dd
 */
public class LocalDateAdapter extends XmlAdapter<String, LocalDate> {

    // Definimos el formato de fecha que vamos a usar en el XML
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yy");

    /**
     * Convierte un String a objeto LocalDate desserializarlo (unmarshal) con JAXB
     *
     * @param v La fecha en formato String salida del XML
     * @return Objeto LocalDate correspondiente
     * @throws Exception si el formato del String no es válido
     */
    @Override
        public LocalDate unmarshal(String v) throws Exception {
            if(v==null || v.isEmpty()){
                return null;
            }
            return LocalDate.parse(v, formatter);
        }

    /**
     * Convierte un objeto LocalDate a String para serializarlo (marshal) en un XML con JAXB
     *
     * @param v Objeto LocalDate
     * @return String con la fecha en formato yyyy-MM-dd
     * @throws Exception
     */
    @Override
        public String marshal(LocalDate v) throws Exception {
            if(v==null){
                return null;
            }
            return v.format(formatter);
        }
    }


