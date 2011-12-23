import java.io.InputStream;
import java.util.Iterator;
import java.util.LinkedList;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

/*
 * Object type that encapsulates an HTTP request
 */
public class HTTPRequest {

    private static final String GET = "GET";
    private static final String PUT = "PUT";
    private static final String POST = "POST";

    private String type; // operation type EX: GET, PUT or POST
    private String requestedObject; // Ex: /index.hmtl
    public String version; // protocol version
    public String urlHost;

    public LinkedList<String> header; // remainder of the request's header

    /*
     * Recupera os dados necessario para as variaveis
     */
    private HTTPRequest(String type, String requestedObject, String version) {
        this.type = type;
        this.requestedObject = requestedObject;
        int primeirosSlash = requestedObject.indexOf("//");
        String semHttp =  requestedObject.substring(primeirosSlash+2);
        int valorUrl = semHttp.indexOf("/");
        this.urlHost = semHttp.substring(0,valorUrl);     
        this.version = version;
        this.header = new LinkedList<String>();        
    }

    private void addHeader(String header) {
        this.header.add(header);
    }

    /*
     * Devolva o tipo de pedido
     */
    public String requestType() {
        return type;
    }

    /*
     * Devolva o objecto pedido
     */
    public String requestedObject() {
        return requestedObject;
    }

    /*
     * Devolva a versão do http
     */
    public String httpVersion() {
        return version;
    }

    /*
     * String representation of this HTTPRequest
     */
    public String toString() {
        String res = type + " " + requestedObject + " " + version + "\r\n";
        Iterator<String> i = header.iterator();
        while(i.hasNext()) {
            res += i.next() + "\r\n";
        }
        return res;
    }

    /*
     * Converte para a versão http 1.0
     * E remove todas as linhas do header que comporta connection e keep-alive
     */
    static HTTPRequest parseHTTPRequestAs1_0(InputStream is) {
        java.util.Scanner sc = new java.util.Scanner(is);
        String operation = sc.next();
        //System.out.println("operation :"+operation);
        String requestedObject = sc.next();
        sc.next(); // skeep http version
        sc.next(); // limpa o \r\n do final da primeira linha

        HTTPRequest request = new HTTPRequest(operation, requestedObject, "HTTP/1.0");
        String line = "";

        while(!(line = sc.nextLine()).equals("")) {
            if(!(line.contains("connection")||line.contains("Connection")))
                request.addHeader(line);
        }

        BufferedReader rd = new BufferedReader(new InputStreamReader(is));
        String line2;
        try{
            while ((line2 = rd.readLine()) != null) {
                System.out.println("Buffer :"+line);
            }
        }catch (IOException e)
        {}

        //System.out.println("test :"+sc.next());
        return request;
    }

}
