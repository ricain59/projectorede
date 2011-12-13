import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class TCPEchoClient {
    public static final java.util.Scanner scanner = new java.util.Scanner(System.in);
    
    public static String readLineFromConsole() throws IOException {
        return scanner.nextLine();
    }
    
    public static void main(String[] args) throws Exception {
        if(args.length != 2) {
            System.out.println("usage: java Client server_name server_port");
            System.exit(-1);
        }
        
        //initialize local socket
        String server_name = args[0];
        int server_port = Integer.parseInt(args[1]);
        InetAddress server_addr = InetAddress.getByName(server_name);
        Socket local_socket = new Socket(server_addr, server_port);

        //reads message from standard input
        System.out.print("enter message to send to server:\n\t> ");
        String message = readLineFromConsole();
        
        //writes message to server
        DataInputStream dis = new DataInputStream(local_socket.getInputStream()); //will not be used
        DataOutputStream dos = new DataOutputStream(local_socket.getOutputStream()); //used to send information to server
        dos.writeUTF(message);
        dos.flush();
        
        message = dis.readUTF(); 
        System.out.println("server (" + local_socket.getRemoteSocketAddress() + ") sent this message:");
        System.out.println("\t " + message);
        System.out.println();
        
        //closes stream and connection
        System.out.print("you message has been sent\n");
        dis.close();
        dos.close();
        local_socket.close();
        
        
        
        
        //ends
    }
}
