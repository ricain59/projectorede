import java.io.DataInputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer {
    
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
            Socket socket_to_client = server_socket.accept();
            System.out.println("server accepted connection from: "+ socket_to_client.getRemoteSocketAddress());
            
            //opens input stream 
            DataInputStream stream_from_client = new DataInputStream(socket_to_client.getInputStream());
        
            //reads message from input stream and writes to standard output
            String message = stream_from_client.readUTF();
            System.out.println("client (" + socket_to_client.getRemoteSocketAddress() + ") sent this message:");
            System.out.println("\t " + message);
            System.out.println();

            //closes stream and connection
            stream_from_client.close();
            socket_to_client.close();
        }
    }
}
