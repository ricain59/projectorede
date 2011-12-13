package irc.utils;

public class UApp
{
    public static String getPath()
    {
        return System.getProperty("user.dir");
    }
    
    public static void printErrorMessage(Exception e)
    {
        Console.printError(e);
    }
}
