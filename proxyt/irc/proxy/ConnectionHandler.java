package irc.proxy;



// // import java.io.BufferedReader;
// // import java.io.InputStreamReader;
// // import java.io.OutputStreamWriter;
import java.net.Socket;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import irc.http.HTTPRequest;
import irc.utils.*;

public class ConnectionHandler implements Runnable {
    
    private Socket socket_client;
    private HTTPRequest request;
    private BufferedWriter stream_to_client;
    
    private Socket socket_server;
    private BufferedReader stream_from_server;
    private DataOutputStream stream_to_server; 
    
    public ConnectionHandler(Socket socket) throws java.io.IOException {
        this.socket_client = socket;
    }
    
    public void run() {
        try {
            //Lê o cabeçalho do pedido do browser
            request = HTTPRequest.parseHTTPRequestAs1_0(this.socket_client.getInputStream());
//             stream_to_client = new BufferedWriter(new OutputStreamWriter(socket_client.getInputStream()));
//             
//             socket_server = new Socket(request.Host(), request.Port());
//             stream_to_server = new DataOutputStream(socket_server.getOutputStream());
//             stream_from_server = new BufferedReader(new InputStreamReader(socket_server.getInputStream()));
//             
//             stream_to_server.writeBytes(request.toString());
//             stream_to_server.flush();
//             
//             String line;
//             while(!(line = stream_from_server.readLine()).equals("")) {
//                 
//             }
            
        
        } catch (Exception e) {
            UApp.printErrorMessage(e);
        } finally {
            this.close();
        }
    }
    
    public void close()
    {
// //         try { this.stream_to_client.close(); }
// //         catch (Exception e) { UApp.printErrorMessage(e); }
// //             
// //         try { this.stream_from_client.close(); }
// //         catch (Exception e) { UApp.printErrorMessage(e); }
        
        try { this.socket_client.close(); }
        catch (Exception e) { UApp.printErrorMessage(e); }
        
        socket_client = null;
    }
    
}
