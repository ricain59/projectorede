package irc.http;

import java.io.InputStream;
import java.util.Iterator;
import java.util.LinkedList;
import java.net.*;

/*
 * Object type that encapsulates an HTTP request
 */
public class HTTPRequest {
    
    private String type; // operation type EX: GET, PUT or POST
    private String requestedObject; // Ex: /index.hmtl
    private String version; // protocol version
    private String host;
    private int port;
    
    private LinkedList<String> header; // remainder of the request's header
    
    private HTTPRequest(String type, String requestedObject, String version) {
        this.type = type;
        this.requestedObject = requestedObject;
        this.version = version;
        this.host = "";
        this.port = 0;
        this.header = new LinkedList<String>();
        updateHostPort();
    }
    
    private void updateHostPort()
    {
        int index;
        String request = this.requestedObject;
        
        index = request.indexOf("//");
        int iapp = (index == -1) ? 0 : index + 2;
        
        index = request.indexOf("/", iapp);
        int ihost = (index == -1) ? request.length() - 1 : index;
        
        index = request.indexOf(":", iapp);
        int iport = (index == -1 || index > ihost) ? ihost : index;
       
        this.host = request.substring(iapp, iport);
        this.port = (iport == ihost) ? 80 : Integer.parseInt(request.substring(iport + 1, ihost));
    }
    
    private void addHeader(String header) {
        this.header.add(header);
    }
    
    /*
     * returns the request type of this request
     */
    public String requestType() {
        return type;
    }
    
    /*
     * returns the requested object of this request
     */
    public String requestedObject() {
        return requestedObject;
    }
    
    /*
     * returns the HTTP version of this request
     */
    public String httpVersion() {
        return version;
    }
    
    public String getHost()
    {
       return host;
    }
    
    public int getPort()
    {
        return port;
    }
    
    public InetAddress getHostIP()
    {
        try {
            return InetAddress.getByName(this.getHost());
        } catch (Exception e) {
            return null;
        }
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
     * Creates an HTTPRequest object that represents a browser request  
     */
    public static HTTPRequest parseHTTPRequest(InputStream is) {
        java.util.Scanner sc = new java.util.Scanner(is);
        String method = sc.next();
        String requestedObject = sc.next();
        String version = sc.next();
        sc.nextLine(); // limpa o \r\n do final da primeira linha

        HTTPRequest request = new HTTPRequest(method, requestedObject, version);
        String line;
        while(!(line = sc.nextLine()).equals("")) {
            request.addHeader(line);
        }
        return request;
    }

    /*
     * Creates an HTTPRequest object that represents a browser request converted to HTTP version 1.0
     * removing all Connection: keep-alive
     */
    public static HTTPRequest parseHTTPRequestAs1_0(InputStream is) {
        java.util.Scanner sc = new java.util.Scanner(is);
        String method = sc.next();
        String requestedObject = sc.next();
        sc.next(); // skeep http version
        sc.nextLine(); // limpa o \r\n do final da primeira linha

        HTTPRequest request = new HTTPRequest(method, requestedObject, "HTTP/1.0");
        String line;
        while(!(line = sc.nextLine()).equals("")) {
            if(!(line.contains("connection")||line.contains("Connection")))
                request.addHeader(line);
        }
        return request;
    }

}
