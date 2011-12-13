package proxyt.irc.proxy;


import java.net.*;
import irc.utils.*;

public class ProxyServer
{
        
    public static void configureProxy(String host, String port)
    {
        System.setProperty("http.proxyHost", host);
        System.setPropery("http.proxyPort", port);   
    }

    public static void main(String[] args) throws Exception {
        
        configureProxy("proxy.uminho.pt", "3128");
        
        //initialize server socket
        int server_port = 3128;
        ServerSocket server_socket = new ServerSocket(server_port);
        Console.println("proxy listening on port: " + server_port);
        
        //main loop
        while(true) {
            //waits connection from clients
            Socket socket_client = server_socket.accept();
            System.out.println("proxy accepted connection from: "+ socket_client.getRemoteSocketAddress());
            
            new Thread(new ConnectionHandler(socket_client)).start();
        }
    }
    
}
