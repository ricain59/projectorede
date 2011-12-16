import java.lang.String;


/**
 * Write a description of class test here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class test
{
    // instance variables - replace the example below with your own
    private int x;

    /**
     * Constructor for objects of class test
     */
    public test()
    {
        String test = "http://www.google.pt/pauvrecon";
        int a = test.indexOf("//");
        String test2 = test.substring(a+2);
        
        int b = test2.indexOf("/");
        //String um = test.substring(a);
        String dois = test2.substring(0,b);
        System.out.println(""+test);
        System.out.println(""+test2);
        System.out.println(""+dois);
    }

    
}
