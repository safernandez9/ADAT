import logica.GestorFotografos;

public class Main{

    private static final String  RUTA_XML = "Archivos/UpdateFotografos.xml";
    private static final String RUTA_DAT = "Archivos/fotografos.dat";
    private static final String RUTA_TXT = "FotografosEstudio.txt";

    static GestorFotografos gestor = new GestorFotografos(RUTA_XML,  RUTA_DAT);


    public static void main(String[] args){

        // Ejercicio 1
        System.out.println("EJERCICIO 1");
        gestor.visualizarFotografosSinBorrados(gestor.leerFotografosRAND());

        // Ejercicio 2

        System.out.println("EJERCICIO 2");
        gestor.escribirFotografosTXT(RUTA_TXT);

        // Ejercicio 3

       System.out.println("EJERCICIO 3");
       gestor.mostrarFotografosStAXCursor(RUTA_XML);



    }

}
