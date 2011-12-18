import java.io.InputStream;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ArrayList;

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
    public String urlPost;
    public LinkedList<String> header; // remainder of the request's header

    //public ArrayList<String> headerArray;

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
        //this.headerArray = new ArrayList();
    }

    private void addHeader(String header) {
        this.header.add(header);
    }

    //     private void addHeaderArray(String header) {
    //         this.headerArray.add(header);
    //     }

    //     private void headerPrint() {
    //         System.out.println("taille array: "+headerArray.size());
    //     }

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
    //static HTTPRequest parseHTTPRequestAs1_0(InputStream is) {
    static HTTPRequest parseHTTPRequestAs1_0(InputStream is) {

        java.util.Scanner sc = new java.util.Scanner(is);
        String operation = sc.next();
        String requestedObject = sc.next();
        sc.next(); // skeep http version
        sc.next(); // limpa o \r\n do final da primeira linha

        HTTPRequest request = new HTTPRequest(operation, requestedObject, "HTTP/1.0");
        String line;
        //int i = 0;
        //boolean fim = false;
        //while(!(line = sc.nextLine()).equals("") && fim) {
        while(!(line = sc.nextLine()).equals("")) {
            //if(!(line.contains("connection")||line.contains("Connection")))
            //request.addHeader(line);
            if(!(line.contains("connection")||line.contains("Connection")))
            {
                request.addHeader(line);
                //request.addHeaderArray(line);

                //System.out.println("header: "+header.get(i));
                //i++;
                //String post = operation.toUpperCase();
                //                 if(line.contains("Content-Length"))
                //                 {
                // 
                //                     sc.nextLine();
                // 
                //                     request.addHeaderArray("\r\n");
                //                     String ultima =sc.nextLine();
                // 
                //                     request.addHeaderArray(ultima);
                //                     request.headerPrint();
                //                     fim = true;
                // 
                //                 }
            }
        }
        //getHeader();
        //System.out.println("tamanho : "+headerArray.size());
        return request;

    }

}
