import java.net.Socket;
import java.io.InputStream;
import java.io.DataInputStream;
import java.io.OutputStream;
import java.io.DataOutputStream;
import java.util.ArrayList;
import java.io.IOException;
import java.net.URL;
import java.io.PrintWriter;
import java.io.OutputStreamWriter;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.util.StringTokenizer;

public class ProxyClientThread extends Thread{
    private Socket socket;
    private Socket socketServer;
    //private DataInputStream fromBrowser;
    private InputStream fromBrowser;
    private DataInputStream fromHost;
    //private InputStream fromHost;
    //private DataOutputStream toBrowser;
    private OutputStream toBrowser;
    private DataOutputStream toHost;
    //private OutputStream toHost;
    private String get;
    private String host;
    private ArrayList pedido;
    private int porta;
    HTTPRequest request;
    ClientToServerThread clientToServer;
    Cache cache;
    boolean inCache = false;
    private DataInputStream fromCache;
    private DataOutputStream toCache;
    byte line[];

    public ProxyClientThread(Socket socket, Cache cache) {
        this.socket = socket;
        this.cache = cache;
        porta = socket.getPort();
        try {
            //fromBrowser = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            //toBrowser = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
            fromBrowser = socket.getInputStream();
            toBrowser = socket.getOutputStream();
            start();
        }
        catch (IOException e) {
            System.out.println("Erro: " + e);
        }
    }

    private void readRequete() throws Exception {
        //BufferedReader bin = new BufferedReader(new InputStreamReader(fromBrowser));
        //HTTPRequest request = HTTPRequest.parseHTTPRequestAs1_0(fromBrowser);
        //String linha;

        // create an HTTPRequest object from the data received from the input stream            
        request = HTTPRequest.parseHTTPRequestAs1_0(fromBrowser);
        System.out.println("Request (start)------------");
        System.out.println(request);
        System.out.println("Request (end)--------------\n");

        // identify the requested method
        if(request.requestType().equals(HTTPRequest.GET)) 
        { // reply to a GET request
            System.out.println("browser sent a GET operation");
            //             for(int i=0;i<request.arrayRequestObject.length;i++)
            //             {
            //System.out.println("test : "+request.requestedObject);
            //                 }
            if(cache.IsCachable(request.requestedObject))
            {
                System.out.println("ok pour le cache");
                inCache = true;
                if(cache.IsCached(request.requestedObject))
                {
                    System.out.println("com cache 1");
                    fromCache = new DataInputStream(cache.getFileInputStream(request.requestedObject));
                    //OutputStream outCache = ClientSocket.getOutputStream();
                    byte data[] = new byte[2000];
                    int count;
                    while (-1 < ( count  = fromCache.read(data)))
                    {
                        toBrowser.write(data,0,count);
                    }
                    toBrowser.flush();
                    fromCache.close();
                }else{

                    URL url;
                    String newurl[] = request.header.get(0).split(" ");
                    url = new URL("http://"+newurl[1]);
                    int portaServer = url.getPort() > 0 ? url.getPort() : 80;

                    socketServer = new Socket(url.getHost(),portaServer);

                    pedido.add("GET " + request.requestedObject + " " + "HTTP/1.0");

                    fromHost = new DataInputStream(socketServer.getInputStream());
                    toHost = new DataOutputStream(socketServer.getOutputStream());

                    clientToServer = new ClientToServerThread(toHost, pedido);

                    String str = fromHost.readLine();
                    StringTokenizer s = new StringTokenizer(str);
                    String retCode = s.nextToken(); // first token is HTTP protocol
                    retCode = s.nextToken();
                    //System.out.println(""+retCode);
                    if (!retCode.equals("200") || !retCode.equals("302")
                    || !retCode.equals("304"))
                    {

                        toCache = new DataOutputStream(cache.getFileOutputStream(request.requestedObject));

                        String tempStr = new String(str+"\r\n");
                        toHost.writeBytes(tempStr);

                        line = new byte[tempStr.length()];
                        tempStr.getBytes(0,tempStr.length(),line,0);

                        // Write bits to file
                        toCache.write(line);
                        //cache.DecrementFreeSpace(line.length,url.toString());
                        System.out.println("com cache 2");
                        if (str.length() > 0)
                        {
                            while (true)
                            {
                                str = fromHost.readLine();
                                tempStr = new String(str+"\r\n");

                                // Send bits to client
                                toHost.writeBytes(tempStr);

                                // Translate reply string to bytes
                                line = new byte[tempStr.length()];
                                tempStr.getBytes(0,tempStr.length(),line,0);

                                // Write bits to file
                                toCache.write(line);
                                //cache.DecrementFreeSpace(line.length,url.toString());

                                if (str.length() <= 0) 
                                    break;
                            }
                        }
                        toHost.flush();
                        byte data[] = new byte[2000];
                        int count;
                        while (( count  = fromHost.read(data)) != -1)
                        {
                            line  = new byte[count];
                            System.arraycopy(data,0,line,0,count);
                            toCache.write(line);
                            //cache.DecrementFreeSpace(count,url.toString());
                            toBrowser.write(data, 0, count);
                            //fromCache = new DataInputStream(cache.getFileInputStream(request.requestedObject));
                        }
                        toBrowser.flush();
                        toCache.close();
                        cache.AddToTable(request.requestedObject);
                        //fromCache = fromHost;
                        //                         byte [] reponse = new byte[4096];
                        //                         int read;
                        //                         // envio das informações recebidas do servidor ao browser
                        //                         while ((read = fromHost.read(reponse)) != -1)
                        //                         {
                        //                             toBrowser.write(reponse, 0, read);
                        //                             toBrowser.flush();
                        //                         }

                    }
                    //toCache = new DataInputStream(cache.getFileOutputStream(url.toString()));
                }
            }else{
                String newurl[] = request.header.get(0).split(" ");
                //                 //url = new URL("http://"+request.header.get(0));
                URL url;
                url = new URL("http://"+newurl[1]);

                int portaServer = url.getPort() > 0 ? url.getPort() : 80;
                System.out.println("sem cache 1");
                socketServer = new Socket(url.getHost(),portaServer);
                System.out.println("sem cache 2");
                fromHost = new DataInputStream(new BufferedInputStream(socketServer.getInputStream()));
                toHost = new DataOutputStream(new BufferedOutputStream(socketServer.getOutputStream()));
                //System.out.println("request object: "+request.requestedObject);

                //
                //pedido.add("GET " + request.requestedObject + " " + "HTTP/1.0");
                pedido.add("GET " + request.requestedObject + " " + "HTTP/1.0");
                // Envio do pedido ao servidor
                clientToServer = new ClientToServerThread(toHost, pedido);
                //fromCache = fromHost;
                byte [] reponse = new byte[4096];
                int read;
                // envio das informações recebidas do servidor ao browser
                while ((read = fromHost.read(reponse)) != -1)
                {
                    toBrowser.write(reponse, 0, read);
                    toBrowser.flush();
                }
            }

        }else if(request.requestType().
        equals(HTTPRequest.PUT)){ // PUT request
            System.out.println("browser sent a PUT operation");

            pedido.add("PUT " + request.requestedObject + " " + "HTTP/1.0");

            //toBrowser.write((HTTPReply.HTTP_ERROR+"\r\n\r\n").getBytes());
        }
        else{ // POST request
            System.out.println("browser sent a POST operation");

            pedido.add("POST " + request.requestedObject + " " + "HTTP/1.0");

            //toBrowser.write((HTTPReply.HTTP_ERROR+"\r\n\r\n").getBytes());
        }

        //System.out.println(""+request.type);
        //System.out.println(""+request.requestedObject);
        //System.out.println(""+request.version);
        //System.out.println(""+request.header.get(0));

        //         // identify the requested method
        //         if(request.requestType().equals(HTTPRequest.GET)) { // reply to a GET request
        //             System.out.println("browser sent a GET operation");
        // 
        //             // create an http reply based on the requested object
        //             HTTPReply reply = HTTPReply.createHTTPReply(root, request.requestedObject());
        //             System.out.println("Reply (start)------------");
        //             System.out.println(reply);
        //             System.out.println("Reply (end)--------------\n");
        // 
        //             // send reply
        //             toBrowser.write(reply.toString().getBytes());
        //             toBrowser.write("\r\n".getBytes());
        // 
        //             // if reply is OK
        //             if(reply.getHTTPResponse().equals(HTTPReply.HTTP_OK)) { // the files needs to be sent
        //                 // open requested file
        //                 // read it and send its contents to browser
        //                 FileInputStream fis = new FileInputStream(reply.getObjectFile());
        //                 byte[] buffer = new byte[4096];
        //                 int read;
        //                 while((read = fis.read(buffer))!= -1){
        //                     toBrowser.write(buffer, 0, read);
        //                 }
        //                 // close file
        //                 fis.close();
        //             }
        //             else { // file Not Found
        //                 System.out.println("File not found");
        //             }
        //         }
        //         else if(request.requestType().equals(HTTPRequest.PUT)) { // PUT request
        //             System.out.println("PUT operation");
        // 
        //             //TODO: Implement this
        // 
        //             toBrowser.write((HTTPReply.HTTP_ERROR+"\r\n\r\n").getBytes());
        //         }
        //         else if(request.requestType().equals(HTTPRequest.POST)) { // POST request
        //             System.out.println("POST operation");
        // 
        //             //TODO: Implement this
        // 
        //             toBrowser.write((HTTPReply.HTTP_ERROR+"\r\n\r\n").getBytes());
        //         }
        //         else { // Unkown request
        //             System.out.println("ERROR");
        //             toBrowser.write((HTTPReply.HTTP_ERROR+"\r\n\r\n").getBytes());
        //         }

        //close input stream from browser
        //fromBrowser.close();
        // close output stream to browser
        //toBrowser.close();
        //close socket to browser
        //newConnection.close();

        //     while ((linha = dados.readLine()) != null) {
        //         if (linha.length() == 0)
        //             break;
        //         /* On transforme la pedido du client car il ne s'agit pas de transmettre au serveur
        //          * l'URL complete mais juste l'element e telecharger, c'est pourquoi on reconstruit la
        //          * ligne GET */
        //         if (linha.trim().toUpperCase().startsWith("GET")) {
        //             String url = linha.substring(3);
        //             String rest = "";
        //             // Obtenir http
        //             int posit = url.toUpperCase().lastIndexOf("HTTP");
        //             if (posit >= 0) {
        //                 rest = url.substring(posit).trim();
        //                 url = url.substring(0, posit).trim();
        //             } else {
        //                 url = url.trim();
        //             }
        //             get = url;
        // 
        //             // Creation nouveau GET -> GET http://ordinateur.xyz:80/toto.htm HTTP 1.0
        //             URL getURL = new URL(url);
        //             pedido.add("GET " + getURL.getFile() + " " + rest);
        //             System.out.println(socket.getInetAddress().getHostName() + " GET " + url);
        //         } else {
        //             pedido.add(linha);
        //         }
        // 
        //         // HOST contient l'adresse du serveur
        //         if (linha.trim().toUpperCase().startsWith("HOST:"))
        //             host = linha.substring(5).trim();
        //     }
    }

    public void run() {
        try
        {
            pedido = new ArrayList();
            readRequete();
            //             URL url;
            //             try 
            //             {
            //                 String newurl[] = request.header.get(0).split(" ");
            //                 //url = new URL("http://"+request.header.get(0));
            //                 url = new URL("http://"+newurl[1]);
            //                 //url = new URL("http://www.google.com");
            //                 //                 for(int i=0;i<newurl.length;i++)
            //                 //                 {
            //                 //                     System.out.println("test"+i+" : "+newurl[i]);
            //                 //                 }
            // 
            //                 int portaServer = url.getPort() > 0 ? url.getPort() : 80;
            //                 socketServer = new Socket(url.getHost(),portaServer);
            //             }
            //             catch (Exception e) {
            //                 PrintWriter out = new PrintWriter(new OutputStreamWriter(toBrowser));
            //                 out.println("Erro : "+e);
            //                 out.flush();
            // 
            //                 if (fromBrowser != null)
            //                     fromBrowser.close();
            //                 if (toBrowser != null)
            //                     toBrowser.close();
            //                 if (socket != null)
            //                     socket.close();
            //                 if (socketServer != null)
            //                     socketServer.close();
            // 
            //                 return;
            //             }

            //fromHost = socketServer.getInputStream();
            //toHost = socketServer.getOutputStream();
            //fromHost = new DataInputStream(new BufferedInputStream(socketServer.getInputStream()));
            //toHost = new DataOutputStream(new BufferedOutputStream(socketServer.getOutputStream()));
            //System.out.println("request object: "+request.requestedObject);

            //
            //pedido.add("GET " + request.requestedObject + " " + "HTTP/1.0");

            // Envio do pedido ao servidor
            //clientToServer = new ClientToServerThread(toHost, pedido);

            //             byte [] reponse = new byte[4096];
            //             int read;
            //             // envio das informações recebidas do servidor ao browser
            //             while ((read = fromCache.read(reponse)) != -1)
            //             {
            //                 toBrowser.write(reponse, 0, read);
            //                 toBrowser.flush();
            //             }
        }
        catch (Exception e) 
        {
            System.out.println("Erro Thread Proxy : " + porta);
        }
        finally
        {
            try 
            {
                if (fromBrowser != null)
                    fromBrowser.close();
                if (toBrowser != null)
                    toBrowser.close();
                if (socket != null)
                    socket.close();
                if (socketServer != null)
                    socketServer.close();
                if (socket != null)
                    socket.close();
                if (socketServer != null)
                    socketServer.close();
            }
            catch (Exception e) {
                System.out.println("Erro Thread Proxy : " + porta);
            }
        }
    }
}
// 
// class ClientToServerThread extends Thread
// {
// 
//     private OutputStream toHost;
//     private ArrayList pedido;
// 
//     public ClientToServerThread(DataOutputStream toHost, ArrayList pedido) throws Exception 
//     {
//         this.toHost = toHost;
//         this.pedido = pedido;
//         start();
//     }
// 
//     public void run()
//     {
//         try 
//         {
//             PrintWriter out = new PrintWriter(new OutputStreamWriter(toHost));
//             for (int i = 0; i < pedido.size(); ++i) {
//                 out.println(pedido.get(i));
//             }
//             out.println();
//             out.flush();
// 
//         }
//         catch (Exception e) {
//             System.out.println("Erro ClientToServerThread : " + e);
//         }
//     }
// }