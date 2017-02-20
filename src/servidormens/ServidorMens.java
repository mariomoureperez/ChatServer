/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidormens;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.JOptionPane;

/**
 *
 * @author mmoureperez
 */
public class ServidorMens {

    static int puerto = 5555;
    public static String mensaje;

    public static void main(String[] args) throws IOException {

        /*puerto del servidor que irá cambiando para que no este en uso por dos clientes
    en este caso cada vez que creamos un puerto distinto porque si lanzamos los tres 
    al mismo tiempo sin esperar al que el primero acabase habría un fallo de conexión 
    por que el puerto estaría ocupado.
    Si tuvieramos la certeza de que el cliente 2 no se conectara hasta que el último 
    termine su conexión podriamos omitir cambiar de puerto cada vez que se cree otra 
    conexión
         */
        System.out.println("Creando socket servidor");

        ServerSocket serverSocket = new ServerSocket();

        System.out.println("Realizando el bind");

        //Dirección del ip y puerto del servidor
        InetSocketAddress addr = new InetSocketAddress("localhost", puerto);
        //Asignamos la dirección al socket
        serverSocket.bind(addr);

        System.out.println("Aceptando conexiones");

        //Creamos el socket y iniciamos la aceptacion.
        Socket newSocket = serverSocket.accept();

        //Cambiamos el puerto para que pueda entrar el proximo cliente sin que se ocupe por varios.
        System.out.println("Conexion recibida");

        PrintStream salida = new PrintStream(new BufferedOutputStream(newSocket.getOutputStream()));

        //abrimos flujo de recogida de datos con cliente
        DataInputStream entrada = new DataInputStream(new BufferedInputStream(newSocket.getInputStream()));
        //abrimos flujo de recogida de datos con cliente

        /*while ((mensaje = entrada.readLine()) != null) {
            System.out.println(mensaje);
        }*/
        String entradaCli;
        
        while((entradaCli=entrada.readLine())!=null){
            System.out.println("Mensaje del cliente: "+entradaCli);
                if(entradaCli.equals("adios")){
                    break;
                }
            String mensaje = JOptionPane.showInputDialog("Introduzca el mensaje del servidor");
            salida.println(mensaje);
            System.out.println("Mensaje del servidor: "+mensaje);
            salida.flush();
       
            
            
        }
       
        salida.close();
        entrada.close();
            
            
            //enviamos los datos al cliente
        

        System.out.println("Cerrando el nuevo socket");
        //cerramos el Scoket
        newSocket.close();

        System.out.println("Cerrando el socket servidor");
        //cerramos la conexión
        serverSocket.close();

        System.out.println("Terminado");

    }

}
