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
        //HTTPRequest request = new HTTPRequest;
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
                    //System.out.println("metodo:"+requestedObject+"TEST");      
                    //request = new HTTPRequest(operation, requestedObject, "HTTP/1.0");
                    fim = true;
                }
                if(i == 0)
                {
                    operation = new String(sb);
                    i++;
                    sb = new StringBuffer();
                    //System.out.println("metodo:"+operation+"test");
                }

            }
            if(fim)
                break;
            if (c == '\r') {
                continue;
            }
            if (c == '\n') {
                //break;
                //request.addHeader(line)
            }
            sb.append(new Character((char) c));
            //Character a = new Character((char) c);            
        }

        HTTPRequest request = new HTTPRequest(operation, requestedObject, "HTTP/1.0");
        fim = false;
        while ((c = is.read()) >= 0) {
            if(i == 10 && c == '\r')
                break;
            i =c;

            //System.out.println(" "+c);
            if (c == '\n') {
                //break;
                //request.addHeader(line)
                sb.append(new Character((char) c));
                //System.out.println(""+new String (sb));
                String test = new String (sb);
                //System.out.println(test);
                if(!test.contains("HTTP"))
                {
                    System.out.println("nao tem http");
                    if(test.contains("Content-Length"))
                    {
                        int doispontos = test.indexOf(":");
                        //String longueur = test.substring(doispontos+1);
                        String longueur = test.substring(doispontos+2);
                        String longueur2 = longueur.substring(0,2);
                        int fl = Integer.parseInt(longueur2);

                        for(int r=0;r<fl;i++)
                        {
                            //while ((c = is.read()) >= 0) {
                            sb.append(new Character((char) c));
                            //System.out.println("no outro while "+(new String (sb)));                            
                        }

                        
                        request.addHeader(new String (sb));
                        fim = true;
                    }else{
                        request.addHeader(new String (sb));
                        //System.out.println("test : "+(new String (sb)));
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
            //Character a = new Character((char) c);
            //System.out.println("test : "+a);

        }
        System.out.println("fim");
        //return sb.toString();
        return request;
    }

}
