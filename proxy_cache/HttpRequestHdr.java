package proxy_cache;

/******************************************************************
 *** File HttpRequestHdr.java 
 ***
 ***/

import java.io.InputStream;
import java.io.DataInputStream;
import java.util.StringTokenizer;

// 
// Class:     HttpRequestHdr
// Abstract:  The headers of the client HTTP request.
//

public class HttpRequestHdr 
{

    /**
     * Http Request method. Such as get or post.
     */
    public  String method = new String();

    /**
     * The requested url. The universal resource locator that
     * hopefully uniquely describes the object or service the
     * client is requesting. 
     */
    public String url   = new String();

    /**
     * Version of http being used. Such as HTTP/1.0
     */
    public String version           = new String();

    /**
     * The client's browser's name.
     */
    public String userAgent         = new String();

    /**
     * The requesting documents that contained the url link.
     */
    public String referer           = new String();

    /**
     * A internet address date of the remote copy.
     */
    public String ifModifiedSince   = new String();

    /**
     * A list of mime types the client can accept.
     */
    public String accept            = new String();

    /**
     * The clients authorization. Don't belive it.
     */

    public String authorization     = new String();
    /**
     * The type of content following the request header.
     * Normally there is no content and this is blank, however
     * the post method usually does have a content and a content 
     * length.
     */
    public String contentType       = new String();
    /**
     * The length of the content following the header. Usually
     * blank.
     */
    public int    contentLength     = -1;
    /**
     * The content length of a remote copy of the requested object.
     */
    public int    oldContentLength  = -1;
    /**
     * Anything in the header that was unrecognized by this class.
     */
    public String unrecognized      = new String();
    /**
     * Indicates that no cached versions of the requested object are
     * to be sent. Usually used to tell proxy not to send a cached copy.
     * This may also effect servers that are front end for data bases.
     */
    public boolean pragmaNoCache    = false;

    static String CR ="\r\n";

    /**
     * Parses a http header from a stream.
     *
     * @param in  The stream to parse. 
     * @return    true if parsing sucsessfull.
     */
    public boolean parse(InputStream In)
    {
        String CR ="\r\n";

        /*
         * Read by lines
         */
        DataInputStream lines;
        StringTokenizer tz;
        try 
        {
            lines = new DataInputStream(In);
            tz = new StringTokenizer(lines.readLine());
        }
        catch (Exception e) 
        {
            return false;
        }

        /*
         * HTTP COMMAND LINE < <METHOD==get> <URL> <HTTP_VERSION> >
         */
        method = getToken(tz).toUpperCase();
        url    = getToken(tz);
        version= getToken(tz);

        while (true) 
        {
            try 
            {
                tz = new StringTokenizer(lines.readLine());
            } 
            catch (Exception e) 
            {
                return false;
            }
            String Token = getToken(tz); 

            // look for termination of HTTP command
            if (0 == Token.length())
                break;

            if (Token.equalsIgnoreCase("USER-AGENT:")) {
                // line =<User-Agent: <Agent Description>>
                userAgent = getRemainder(tz);           
            } else if (Token.equalsIgnoreCase("ACCEPT:")) {
                // line=<Accept: <Type>/<Form>
                // examp: Accept image/jpeg
                accept += " " + getRemainder(tz);

            } else if (Token.equalsIgnoreCase("REFERER:")) {
                // line =<Referer: <URL>>
                referer = getRemainder(tz);

            } else if (Token.equalsIgnoreCase("PRAGMA:")) { 
                // Pragma: <no-cache>
                Token = getToken(tz);

                if (Token.equalsIgnoreCase("NO-CACHE"))
                    pragmaNoCache = true;
                else
                    unrecognized += "Pragma:" + Token + " "
                    +getRemainder(tz) +"\n";            
            } else if (Token.equalsIgnoreCase("AUTHORIZATION:")) { 
                // Authenticate: Basic UUENCODED
                authorization=  getRemainder(tz);

            } else if (Token.equalsIgnoreCase("IF-MODIFIED-SINCE:")) {
                // line =<If-Modified-Since: <http date>
                // *** Conditional GET replaces HEAD method ***
                String str = getRemainder(tz);
                int index = str.indexOf(";");
                if (index == -1) {
                    ifModifiedSince  =str;
                } else {
                    ifModifiedSince  =str.substring(0,index);

                    index = str.indexOf("=");
                    if (index != -1) {
                        str = str.substring(index+1);
                        oldContentLength =Integer.parseInt(str);
                    }
                }
            } else if (Token.equalsIgnoreCase("CONTENT-LENGTH:")) {
                Token = getToken(tz);
                contentLength =Integer.parseInt(Token);
            } else if (Token.equalsIgnoreCase("CONTENT-TYPE:")) {
                contentType = getRemainder(tz);
            } else {  
                unrecognized += Token + " " + getRemainder(tz) + CR;
            }
        }
        return true;
    }

    /*
     * Rebuilds the header in a string
     * @returns      The header in a string.
     */
    public String toString(boolean sendUnknowen) {
        String Request; 

        if (0 == method.length())
            method = "GET";

        Request = method +" "+ url + " HTTP/1.0" + CR;

        if (0 < userAgent.length())
            Request +="User-Agent:" + userAgent + CR;

        if (0 < referer.length())
            Request+= "Referer:"+ referer  + CR;

        if (pragmaNoCache)
            Request+= "Pragma: no-cache" + CR;

        if (0 < ifModifiedSince.length())
            Request+= "If-Modified-Since: " + ifModifiedSince + CR;

        // ACCEPT TYPES //
        if (0 < accept.length())
            Request += "Accept: " + accept + CR;
        else 
            Request += "Accept: */"+"* \r\n";

        if (0 < contentType.length())
            Request += "Content-Type: " + contentType   + CR;

        if (0 < contentLength)
            Request += "Content-Length: " + contentLength + CR;

        if (0 != authorization.length())
            Request += "Authorization: " + authorization + CR;

        if (sendUnknowen) {
            if (0 != unrecognized.length())
                Request += unrecognized;
        }   

        Request += CR;

        return Request;
    }

    /**
     * (Re)builds the header in a string.
     *
     * @returns      The header in a string.
     */
    public String toString() {
        return toString(true);
    }

    /**
     *  Returns the next token in a string
     *
     * @param   tk String that is partially tokenized.
     * @returns The remainder
     */
    String  getToken(StringTokenizer tk){
        String str ="";
        if  (tk.hasMoreTokens())
            str =tk.nextToken();
        return str; 
    }   

    /**
     *  Returns the remainder of a tokenized string
     *
     * @param   tk String that is partially tokenized.
     * @returns The remainder
     */
    String  getRemainder(StringTokenizer tk){
        String str ="";
        if  (tk.hasMoreTokens())
            str =tk.nextToken();
        while (tk.hasMoreTokens()){
            str +=" " + tk.nextToken();
        }
        return str;
    }

} 
