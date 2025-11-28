import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Cliente {

    public static void main(String[] args) throws IOException {

        String servidor = "localhost", FIN="fin", mensaje="";
        int puerto = 7; // puerto ECHO
        Scanner sc=new Scanner(System.in);
        System.out.println("Introduzca el servidor:");
        servidor=sc.nextLine();
        Socket socket = new Socket(servidor, puerto);
        DataInputStream in = new DataInputStream(socket.getInputStream());
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());
        System.out.println("Conectado con el servidor");
        while(!mensaje.equalsIgnoreCase(FIN)) {
// ENVIAMOS ...
            mensaje=sc.nextLine();
            out.writeUTF(mensaje);
            System.out.println("Cliente envía: " + mensaje);
// RECIBIMOS ...
            String strRecibido = in.readUTF();
            System.out.println("Cliente recibe: " + strRecibido);
            if(!mensaje.equals(strRecibido))
                System.out.println("Ha ocurrido un problema: las cadenas son distintas.");
            System.out.println("**************************************");
        }
        socket.close();
    }
}
