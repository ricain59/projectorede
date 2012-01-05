import java.io.InputStream;
import java.util.Iterator;
import java.util.LinkedList;

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
    public String post;
    public String contentype;
    public String contentlength;

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
     * Devolva o post
     */
    public String requestPost() {
        return post;
    }

    /*
     * Metes os valores do post nas string post
     */
    public void setPost(String post) {
        this.post = post;
    }

    /*
     * Metes os valores do content type nas string na string do mesmo nome
     */
    public void setContentTypePost(String contentype)
    {
        this.contentype = contentype;
    }

    /*
     * Devolva o content type
     */
    public String requestContentType() {
        return contentype;
    }

    /*
     * Metes os valores do content type nas string na string do mesmo nome
     */
    public void setContentLengthPost(String contentlength)
    {
        this.contentlength = contentlength;
    }

    /*
     * Devolva o content type
     */
    public String requestContentLength() {
        return contentlength;
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
    static HTTPRequest parseHTTPRequestAs1_0(InputStream is) throws IOException {
        java.util.Scanner sc = new java.util.Scanner(is);
        String operation = sc.next();

        String requestedObject = sc.next();
        sc.next(); // skeep http version
        sc.next(); // limpa o \r\n do final da primeira linha

        HTTPRequest request = new HTTPRequest(operation, requestedObject, "HTTP/1.0");
        String line = "";

        while(!(line = sc.nextLine()).equals("")) {
            if(!(line.contains("connection")||line.contains("Connection")))
                request.addHeader(line);
        }
        return request;
    }

    static HTTPRequest readLine(InputStream is) throws IOException {
        StringBuffer sb = new StringBuffer();

        String operation = "";
        String requestedObject = "";
        int c;
        int i= 0;
        boolean fim = false;
        while ((c = is.read()) >= 0) {

            if(c == ' ' && i < 2)
            {
                if(i == 1)
                {
                    requestedObject = new String(sb);
                    i++;
                    sb = new StringBuffer();
                    fim = true;
                }
                if(i == 0)
                {
                    operation = new String(sb);
                    i++;
                    sb = new StringBuffer();
                }
            }
            if(fim)
                break;
            sb.append(new Character((char) c));            
        }
        HTTPRequest request = new HTTPRequest(operation, requestedObject, "HTTP/1.0");
        fim = false;
        while ((c = is.read()) >= 0) {
            if(i == 10 && c == '\r')
                break;
            i =c;

            if (c == '\n') {
                sb.append(new Character((char) c));

                String headerpedido = new String (sb);

                if(!(headerpedido.contains("HTTP") || headerpedido.contains("connection") || headerpedido.contains("Connection")))
                {
                    if(headerpedido.contains("Content-Type"))
                    {
                        int r = headerpedido.indexOf("\r");
                        String ct = headerpedido.substring(0,r);
                        request.setContentTypePost(ct);
                    }
                    if(headerpedido.contains("Content-Length"))
                    {
                        //para saber o valor do content-length  
                        request.addHeader(headerpedido);
                        int doispontos = headerpedido.indexOf(":");
                        String tamanho = headerpedido.substring(doispontos+2);
                        int r = tamanho.indexOf("\r");
                        tamanho = tamanho.substring(0,r);
                        int pl = Integer.parseInt(tamanho);
                        r = headerpedido.indexOf("\r");
                        String cl = headerpedido.substring(0,r);
                        request.setContentLengthPost(cl);
                        sb = new StringBuffer();
                        
                        for(int s=0;s<pl;s++)
                        {
                            c = is.read();
                            sb.append(new Character((char) c));              
                        }
                        request.setPost(new String (sb));
                       
                        request.addHeader(new String (sb));
                        fim = true;
                    }else{
                        request.addHeader(new String (sb));

                        sb = new StringBuffer();
                    }
                }else{
                    sb = new StringBuffer();
                }
            }else{
                sb.append(new Character((char) c));
            }
            if(fim)
                break;

        }
        return request;
    }

}
