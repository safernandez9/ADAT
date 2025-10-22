import java.util.Scanner;

public class Main{
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        String archivos;

        //Ejercicio 1
        System.out.println("Ejercicio 1");
        System.out.println("Introduzca los archivos a contar separados por espacios");
        archivos = sc.nextLine();
        Operaciones.ejercicio1(archivos);

        //Ejercicio 2
        System.out.println("Ejercicio 2");

        //Ejercicio 3
        System.out.println("Ejercicio 3");
    }
}

