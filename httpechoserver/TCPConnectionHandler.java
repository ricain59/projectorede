import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class TCPConnectionHandler implements Runnable {
    
    private Socket socket_to_client;
    
    public TCPConnectionHandler(Socket socket) {
        //this.socket_to_client = socket;
		socket_to_client = socket;
    }
    
    //@Override
    public void run() {
        try {
	    Thread.sleep(10000);

            System.out.println("\t[Thread"+Thread.currentThread()+"] replying to cliente "+ socket_to_client.getRemoteSocketAddress());
            //opens input stream 
            DataInputStream stream_from_client = new DataInputStream(socket_to_client.getInputStream());

            //reads message from input stream and writes to standard output
            String message = stream_from_client.readUTF();
            System.out.println("client (" + socket_to_client.getRemoteSocketAddress() + ") sent this message:");
            System.out.println("\t " + message);
            System.out.println();

            //opens output stream
            DataOutputStream stream_to_client = new DataOutputStream(socket_to_client.getOutputStream()); 
            stream_to_client.writeUTF(message.toUpperCase());
            stream_to_client.flush();
            
            //closes stream and connection
            stream_to_client.close();
            stream_from_client.close();
            socket_to_client.close();
        } catch (Exception e) { e.printStackTrace(); }
    }
    
}
