package irc.http;

public enum HTTPVersion
{
    v1_0,
    v1_1;
    
    public String toString()
    {
        switch (this)
        {
            case v1_0:   return "HTTP/1.0";
            case v1_1:   return "HTTP/1.1";
        }
        throw new AssertionError("Unknown op: " + this);
    }
}
