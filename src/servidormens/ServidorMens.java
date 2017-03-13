/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidormens;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManager;
import javax.swing.JOptionPane;

/**
 *
 * @author mmoureperez
 */
public class ServidorMens {

    static int puerto = 5555;
    public static String mensaje;
    

    public static void main(String[] args) throws IOException, KeyManagementException, NoSuchAlgorithmException, KeyStoreException, CertificateException {

        /*puerto del servidor que irá cambiando para que no este en uso por dos clientes
    en este caso cada vez que creamos un puerto distinto porque si lanzamos los tres 
    al mismo tiempo sin esperar al que el primero acabase habría un fallo de conexión 
    por que el puerto estaría ocupado.
    Si tuvieramos la certeza de que el cliente 2 no se conectara hasta que el último 
    termine su conexión podriamos omitir cambiar de puerto cada vez que se cree otra 
    conexión
         */
        //Utilización de la keytool y certificado de confianza//
       System.setProperty("javax.net.ssl.keyStore", "serverKey.jks");
       System.setProperty("javax.net.ssl.keyStorePassword", "servpass");
       System.setProperty("javax.net.ssl.trustStore", "clientTrustedCerts.jks");
       System.setProperty("javax.net.ssl.trustStorePassword", "clientpass");
        
        
        
        
        
       
        
        

       //Creamos el SSLSocket para hacer una conxión encriptada
        SSLServerSocketFactory serverSocketFactory=(SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
        
        ServerSocket serverSocket=(SSLServerSocket) serverSocketFactory.createServerSocket(puerto);
        System.out.println("Creando socket servidor");
       


        System.out.println("Aceptando conexiones");

        //Creamos el socket encriptado y iniciamos la aceptacion.
      
        SSLSocket newSocket=(SSLSocket) serverSocket.accept();

        //Cambiamos el puerto para que pueda entrar el proximo cliente sin que se ocupe por varios.
        System.out.println("Conexion recibida");
        
         /*cambiamos los flujos originales por estes dos porque los originales daban problemas al leer 
            y estes si lo hacen a la perfección */
         
        //abrimos flujo de salida de datos con el cliente
        PrintStream salida = new PrintStream(new BufferedOutputStream(newSocket.getOutputStream()));

        //abrimos flujo de recogida de datos con cliente
        DataInputStream entrada = new DataInputStream(new BufferedInputStream(newSocket.getInputStream()));
       
        String entradaCli;
        
        //Bucle que mientras el cliente tenga mensajes el server escuchará
        while((entradaCli=entrada.readLine())!=null){
            System.out.println("Mensaje del cliente: "+entradaCli);
            //Cuando el mensaje use la palabra clave enviada por el cliente sea adios el server se cerrara rompiendo el bucle
                if(entradaCli.equals("adios")){
                    break;
                }
            //mensaje del servidor cara el cliente   
            String mensaje = JOptionPane.showInputDialog("Introduzca el mensaje del servidor");
            salida.println(mensaje);
            System.out.println("Mensaje del servidor: "+mensaje);
            salida.flush();
       
            
            
        }
        
        //cerramos flujos de entra y salida
        salida.close();
        entrada.close();
            
        System.out.println("Cerrando el nuevo socket");
        //cerramos el Scoket
        newSocket.close();

        System.out.println("Cerrando el socket servidor");
        //cerramos la conexión
        serverSocket.close();

        System.out.println("Terminado");

    }
    }


