import servicio.OperacionesIO;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

// Main generado con IA para agilizar

public class Main {
    public static void main(String[] args) {
        String base = "C:/Users/Saúl/Desktop/PRUEBAS";

        // Crear estructura inicial de carpetas y archivos
        try {
            File dirA = new File(base, "DirA");
            File dirB = new File(base, "DirB");
            File dirC = new File(base, "DirA\\SubDirC");

            dirA.mkdirs();
            dirB.mkdirs();
            dirC.mkdirs();

            // Crear archivos de prueba
            File f1 = new File(dirA, "archivo1.txt");
            File f2 = new File(dirA, "archivo2.log");
            File f3 = new File(dirC, "archivo3.txt");

            f1.createNewFile();
            f2.createNewFile();
            f3.createNewFile();

            // Escribir algo en los archivos
            try (FileWriter fw = new FileWriter(f1)) { fw.write("Contenido archivo1"); }
            try (FileWriter fw = new FileWriter(f2)) { fw.write("Contenido archivo2"); }
            try (FileWriter fw = new FileWriter(f3)) { fw.write("Contenido archivo3"); }

            // Mostrar estructura inicial
            System.out.println("\nESTRUCTURA INICIAL:");
            OperacionesIO.recorrerRecursivo(base);

            // Ejecutar todas las funciones
            System.out.println("\n--- VISUALIZAR CONTENIDO DirA ---");
            OperacionesIO.visualizarContenido(dirA.getAbsolutePath());

            System.out.println("\n--- RECORRER RECURSIVO DirA ---");
            OperacionesIO.recorrerRecursivo(dirA.getAbsolutePath());

            System.out.println("\n--- FILTRAR POR EXTENSION .txt EN DirA ---");
            OperacionesIO.filtrarPorExtension(dirA.getAbsolutePath(), ".txt");

            System.out.println("\n--- FILTRAR POR EXTENSION Y ORDENAR ASCENDENTE .txt EN DirA ---");
            OperacionesIO.filtrarPorExtensionYOrdenar(dirA.getAbsolutePath(), ".txt", false);

            System.out.println("\n--- FILTRAR POR SUBCADENA 'archivo1' EN DirA ---");
            OperacionesIO.filtrarPorSubcadena(dirA.getAbsolutePath(), "archivo1");

            System.out.println("\n--- COPIAR archivo1.txt a DirB ---");
            OperacionesIO.copiarArchivo(f1.getAbsolutePath(), dirB.getAbsolutePath());

            System.out.println("\n--- MOVER archivo2.log a DirB ---");
            OperacionesIO.moverArchivo(f2.getAbsolutePath(), dirB.getAbsolutePath());

            System.out.println("\n--- COPIAR DirA a DirB ---");
            OperacionesIO.copiarDirectorio(dirA.getAbsolutePath(), dirB.getAbsolutePath());

            System.out.println("\n--- BORRAR DirB\\DirA ---");
            OperacionesIO.borrar(new File(dirB, "DirA").getAbsolutePath());

            System.out.println("\nESTRUCTURA FINAL:");
            OperacionesIO.recorrerRecursivo(base);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
