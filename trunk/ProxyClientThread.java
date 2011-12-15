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
    private InputStream fromBrowser;
    private DataInputStream fromHost;
    private OutputStream toBrowser;
    private DataOutputStream toHost;
    private String get;
    private String host;
    private ArrayList pedido;
    private int porta;
    HTTPRequest request;
    ClientToServerThread clientToServer;
    Cache cache;
    private DataInputStream fromCache;
    private DataOutputStream toCache;
    byte line[];
    //String tempStr;

    /*
     * Recebe os dados da class proxythread para poder depois tratar os mesmos.
     */
    public ProxyClientThread(Socket socket, Cache cache) {
        this.socket = socket;
        this.cache = cache;
        porta = socket.getPort();
        try {
            fromBrowser = socket.getInputStream();
            toBrowser = socket.getOutputStream();
            start();
        }
        catch (IOException e) {
            System.out.println("Erro: " + e);
        }
    }

    /*
     * Faz o tratamento do pedido
     * Vê se exite na cache e caso existe devolve para o browser, nesse caso o cliente
     * Caso não existe vai buscar as informações ao host, mete as na cache e depois devolve as mesma para o browser, , nesse caso o cliente.
     */
    private void readRequete() throws Exception {
        request = HTTPRequest.parseHTTPRequestAs1_0(fromBrowser);
        System.out.println("Request (start)------------");
        System.out.println(request);
        System.out.println("Request (end)--------------\n");

        String requestedObject = request.requestedObject();
        String type = request.requestType();
        // identify the requested method
        if(type.equals("GET")) 
        { // reply to a GET request
            System.out.println("browser sent a GET operation");
            if(cache.IsCachable(requestedObject))
            {
                //inCache = true;
                if(cache.IsCached(requestedObject))
                {
                    fromCache = new DataInputStream(cache.getFileInputStream(requestedObject));
                    sendToBrowser(fromCache);
                    //                     byte data[] = new byte[4096];
                    //                     int count;
                    //                     while (-1 < ( count  = fromCache.read(data)))
                    //                     {
                    //                         toBrowser.write(data,0,count);
                    //                     }
                    //                     toBrowser.flush();
                    //                     fromCache.close();
                }else{
                    URL url;
                    String newurl[] = request.header.get(0).split(" ");
                    url = new URL("http://"+newurl[1]);
                    int portaServer = url.getPort() > 0 ? url.getPort() : 80;

                    socketServer = new Socket(url.getHost(),portaServer);

                    pedido.add("GET " + requestedObject + " " + "HTTP/1.0");

                    fromHost = new DataInputStream(socketServer.getInputStream());
                    toHost = new DataOutputStream(socketServer.getOutputStream());

                    clientToServer = new ClientToServerThread(toHost, pedido);

                    String str = fromHost.readLine();
                    StringTokenizer s = new StringTokenizer(str);
                    String retCode = s.nextToken();
                    retCode = s.nextToken();

                    if (!retCode.equals("200") || !retCode.equals("302")
                    || !retCode.equals("304"))
                    {
                        toCache = new DataOutputStream(cache.getFileOutputStream(requestedObject));

                        String tempStr = new String(str+"\r\n");
                        toHost.writeBytes(tempStr);

                        line = new byte[tempStr.length()];
                        tempStr.getBytes(0,tempStr.length(),line,0);

                        toCache.write(line);

                        if (str.length() > 0)
                        {
                            while (true)
                            {
                                str = fromHost.readLine();
                                tempStr = new String(str+"\r\n");

                                toHost.writeBytes(tempStr);
                                line = new byte[tempStr.length()];
                                tempStr.getBytes(0,tempStr.length(),line,0);

                                toCache.write(line);

                                if (str.length() <= 0) 
                                    break;
                            }
                        }
                        toHost.flush();
                        byte data[] = new byte[4096];
                        int count;
                        while (( count  = fromHost.read(data)) != -1)
                        {
                            line  = new byte[count];
                            System.arraycopy(data,0,line,0,count);
                            toCache.write(line);
                            toBrowser.write(data, 0, count);

                        }
                        toBrowser.flush();
                        toCache.close();
                        cache.AddToTable(requestedObject);
                    }
                }
            }else{
                String newurl[] = request.header.get(0).split(" ");

                URL url;
                url = new URL("http://"+newurl[1]);

                int portaServer = url.getPort() > 0 ? url.getPort() : 80;

                socketServer = new Socket(url.getHost(),portaServer);

                fromHost = new DataInputStream(new BufferedInputStream(socketServer.getInputStream()));
                toHost = new DataOutputStream(new BufferedOutputStream(socketServer.getOutputStream()));

                pedido.add("GET " + requestedObject + " " + "HTTP/1.0");

                clientToServer = new ClientToServerThread(toHost, pedido);
                sendToBrowser(fromHost);
                //                 byte [] reponse = new byte[4096];
                //                 int read;
                //                 // envio das informações recebidas do servidor ao browser
                //                 while ((read = fromHost.read(reponse)) != -1)
                //                 {
                //                     toBrowser.write(reponse, 0, read);
                //                     toBrowser.flush();
                //                 }
            }

        }else if(type.equals("PUT")){ // PUT request
            System.out.println("browser sent a PUT operation");

            pedido.add("PUT " + requestedObject + " " + "HTTP/1.0");

        }
        else if(type.equals("POST")){ // POST request
            System.out.println("browser sent a POST operation");

            pedido.add("POST " + requestedObject + " " + "HTTP/1.0");

        }

    }

    /*
     * Recebe dados e envia para o browser
     */
    private void sendToBrowser(DataInputStream from)
    {
        try
        {
            byte data[] = new byte[4096];
            int count;
            while ((count = from.read(data)) != -1)
            {
                toBrowser.write(data,0,count);
            }
        }
        catch (IOException e) 
        {
            System.out.println("Erro Thread Proxy : " + porta);
        }
        finally
        {
            try
            {
                toBrowser.flush();
                fromCache.close();
            }
            catch (Exception e) {
                System.out.println("Erro Thread Proxy : " + porta);
            }
        }
    }

    /*
     * Lança o metodoa reaRequete e ao fim fecha todas as linhas de dados.
     */
    public void run() {
        try
        {
            pedido = new ArrayList();
            readRequete();     
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
