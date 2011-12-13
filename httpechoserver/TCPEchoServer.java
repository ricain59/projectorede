import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPEchoServer {
    
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
            
            Thread.sleep(3000);
            
            //opens input stream 
            DataInputStream stream_from_client = new DataInputStream(socket_to_client.getInputStream());
            DataOutputStream stream_to_client = new DataOutputStream(socket_to_client.getOutputStream()); //used to send information to server
        
            //reads message from input stream and writes to standard output
            String message = stream_from_client.readUTF();
            System.out.println("client (" + socket_to_client.getRemoteSocketAddress() + ") sent this message:");
            System.out.println("\t " + message);
            System.out.println();
            
            //transforms message to uppercase and writes to standard output
            String messageToUpperCase = message.toUpperCase();
            System.out.println("\t " + messageToUpperCase);
            System.out.println();
            
            //writes message to client
            stream_to_client.writeUTF(messageToUpperCase);
            stream_to_client.flush();
            
            //closes stream and connection
            stream_from_client.close();
            socket_to_client.close();
        }
    }
}
