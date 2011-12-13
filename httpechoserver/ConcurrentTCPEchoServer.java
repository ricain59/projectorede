import java.net.ServerSocket;
import java.net.Socket;

public class ConcurrentTCPEchoServer {

    public static void main(String[] args) throws Exception {
        if(args.length != 1) {
            System.out.println("usage: java Server server_port");
            System.exit(-1);
        }

        //initialize local socket
        int server_port = Integer.parseInt(args[0]);
        ServerSocket server_socket = new ServerSocket(server_port);
        System.out.println("server listening on port: "+ server_port);
        while(true) {
            //waits connection from clients
            final Socket socket_to_client = server_socket.accept();
            System.out.println("server accepted connection from: "+ socket_to_client.getRemoteSocketAddress());

            // creates a new thread to handle the new connection
            //isso serve para criar 
            new Thread(new TCPConnectionHandler(socket_to_client)).start();
        }
    }
}
