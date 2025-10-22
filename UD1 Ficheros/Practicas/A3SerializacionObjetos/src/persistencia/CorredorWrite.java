package persistencia;

import modelo.Corredor;
import modelo.Equipo;

import java.io.*;

/**
 * Clase encargada de la escritura secuencial de objetos corredor en el fichero
 * binario correspondiente.
 * <p>
 * Solo gestiona apertura, escritura y cierre del flujo ObjectOutputStream
 */
public class CorredorWrite extends Archivo {


    /*
    OOS escribe una cabecera directamente la primera vez que abre un archivo, escribo objertos y cuando acabo y lo cierro y lo
    vuelvo a abrir, abre con una cabecera nueva.
    Al leer con OIS usa la primera cabecera para reconstruir los objetos de byte a clase, al encontrarse una segnda cabecera en medio del archivo
     en vez de mas objetos da error
    de archivo corrupto
     */


    /*
    Tambien menciono algo de true. al abrirlo en el campo FOS pongo segundo parametro false ña primera vez
     */

    private ObjectOutputStream oos;

    public CorredorWrite(String ruta) {
        super(ruta);
    }

    @Override
    public void abrirArchivo() {
        //Compruebo si existe el archivo y no esta vacío
        boolean appendMode = archivoExiste() && this.f.length() > 0;
        try {
            if (appendMode) {
                // CASO 1: El archivo existe y no está vacío, añado sin cabecera
                oos = new AppendObjectOutputStream(
                        new BufferedOutputStream(
                                new FileOutputStream(this.f, appendMode)));
                System.out.println("Archivo de escritura abierto");
            } else {
                // CASO 2: Archivo nuevo o vacío. Añado con cabecera.
                oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(this.f, appendMode)));
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * Cierra un archivo
     */
    public void cerrarArchivo() {
        if (oos != null) {
            try {
                oos.close();
            } catch (IOException e) {
                throw new RuntimeException("Error al cerrar el archivo de corredores: " + e.getMessage());
            }
        }
    }

    /**
     * Verifica que un equipo existe y no está marcado como borrado
     * @param idEquipo
     * @return
     */
    private boolean verificarEquipoValido(int idEquipo){
        EquipoRandom archivoEquipos = new EquipoRandom("equipos.dat");
        archivoEquipos.abrirArchivo();
        try{
            Equipo equipo = archivoEquipos.leerEquipo(idEquipo);
            return equipo  != null && !equipo.isBorrado();

            //Finally garantiza el cierre aunque haya return;
        } finally {
            archivoEquipos.cerrarArchivo();
        }
    }

    /**
     * Escribe un objeto corredor usando el OOS
     * @param corredor
     * @return
     */
    public boolean escribir(Corredor corredor) {
        if (oos == null) {
            System.out.println("Error: El stream de escritura no está abierto");
            return false;
        }
        try {
            oos.writeObject(corredor);
            oos.flush();
            return true;
        } catch (IOException e) {
            System.out.println("Error escribiendo corredor");
            return false;
        }
    }


    // Los siguientes métodos no son obligatorios pero favorecen diseño y mantenibilidad, así como pruebas o mantenimiento;

    public void iniciarEscritura() {
        this.abrirArchivo();
    }

    public void finalizarEscritura() {
        this.cerrarArchivo();
    }

}
