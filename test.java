import java.lang.String;
import java.net.URLEncoder;
import java.io.IOException;

/**
 * Write a description of class test here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class test
{
    // instance variables - replace the example below with your own
    String urlNotfinal2;

    /**
     * Constructor for objects of class test
     */
    public test()
    {
        String urlNotFinal = "index.php?s=64d54749807e01f79a5582dca3f0a5cd&act=Login&CODE=01&CookieDate=1";
        String urlPost = "";
        int interro = urlNotFinal.indexOf("?");
        urlNotfinal2 = urlNotFinal.substring(interro+1);

        //int tamanho = urlNotfinal2.length();
        while(this.urlNotfinal2.length() > 0)
        {
            int igual = urlNotfinal2.indexOf("=");
            int and = urlNotfinal2.indexOf("&");
            if(and <0) break;
            String a = urlNotfinal2.substring(igual);
            String b = urlNotfinal2.substring(igual+1, and);

            try{
                if(urlPost == "")
                {
                    urlPost = URLEncoder.encode("a", "UTF-8")+"="+URLEncoder.encode(b, "UTF-8");
                    //urlPost = URLEncoder.encode("a", "UTF-8");
                }else{
                    urlPost += URLEncoder.encode(a, "UTF-8")+"="+URLEncoder.encode(b, "UTF-8");
                }

                this.urlNotfinal2 = urlNotfinal2.substring(and+1);
            }catch (IOException e)
            {}
            System.out.println(""+urlPost);
        }
        
    }

    //String um = test.substring(a);
    //String dois = test2.substring(0,b);
    
    //System.out.println(""+test2);
    //System.out.println(""+dois);
}


