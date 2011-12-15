import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;

public class ProxyThread extends Thread
{
    Cache cache;
    private int porta;

    public ProxyThread(int porta, Cache cache) 
    {
        this.porta = porta;
        this.cache = cache;
        start();
    }

    public void run() {
        try {
            System.out.println("Servidor Proxy na porta : " + porta);
            ServerSocket server = new ServerSocket(porta);
            while (!interrupted()) {
                Socket cliente = server.accept();
                System.out.println("Pedido do cliente com ip : " + cliente.getInetAddress());
                new ProxyClientThread(cliente, cache);
                sleep(5);
            }
            System.out.println("Stop");
        }
        catch (IOException e) {
            System.out.println("Erreur MAIN : " + e);
        }
        catch (InterruptedException e) {
            System.out.println("Erreur MAIN : " + e);
        }
    }
}
