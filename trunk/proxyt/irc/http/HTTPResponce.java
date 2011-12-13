package irc.http;

public enum HTTPResponce
{
    OK,
    NOT_FOUND,
    ERROR;
    
    public String toString(HTTPVersion version)
    {
        return version.toString() + " " + this.getCode() + " " + this.getDescription();
    }
    
    public String toString()
    {
        return toString(HTTPVersion.v1_0);
    }
    
    public int getCode()
    {
        switch (this) {
            case OK:        return 200;
            case NOT_FOUND: return 404;
            case ERROR:     return 500;
        }
        throw new AssertionError("Unknown op: " + this);
    }
    
    public String getDescription()
    {
        switch (this) {
            case OK:        return "OK";
            case NOT_FOUND: return "Not Found";
            case ERROR:     return "Server Error";
        }
        throw new AssertionError("Unknown op: " + this);
    }
}
