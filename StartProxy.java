import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class StartProxy
{
    private ProxyThread proxyServer;
    Cache cache = new Cache();

    /*
     * Lança a class ProxyThread que permite se executar em varias thread
     */
    public StartProxy()
    {
        System.out.println("Start do proxy");
        proxyServer = new ProxyThread(8080, cache);
    }

    /*
     * Caso o tulizador escreva a palavra EXIT o proxy é parado
     */
    public void stop()
    {
        if (proxyServer.isAlive())
            proxyServer.interrupt();
    }

    /*
     * Class com o main para iniciar o proxy
     */
    public static void main(String[] args)
    {
        StartProxy proxy = new StartProxy();
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        String exit;
        boolean done = false;

        try {
            while (!done){
                exit = input.readLine();
                if (exit.toUpperCase().equals("EXIT"))
                    done = true;
            }
        }
        catch (Exception e) {
            System.out.println("Erreur MAIN : " + e);
            System.exit(1);
        }
        finally {
            proxy.stop();
        }
        System.exit(0);
    }
}