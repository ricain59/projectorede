package irc.http;


/**
 * Enumeration class HTTPMethod - write a description of the enum class here
 * 
 * @author (your name here)
 * @version (version number or date here)
 */
public enum HTTPMethod
{
    GET,
    PUT,
    POST;
    
    public String toString()
    {
        switch (this) {
            case GET:   return "GET";
            case PUT:   return "PUT";
            case POST:  return "POST";
        }
        throw new AssertionError("Unknown op: " + this);
    }
}
