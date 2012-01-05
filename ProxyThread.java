import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;

public class ProxyThread extends Thread
{
    Cache cache;
    private int porta;

    /*
     * Recebe os dados necessario para iniciar uma thread na porta certa
     */
    public ProxyThread(int porta, Cache cache) 
    {
        this.porta = porta;
        this.cache = cache;
        start();
    }

    /*
     * Esse método recebe o ip da mquina que lhe esta a pedir informações.
     * E depois inicia outro thread para receber mais pedido.
     */
    public void run() {
        try {
            System.out.println("Servidor Proxy na porta : " + porta);
            ServerSocket server = new ServerSocket(porta);
            while (!interrupted()) {
                Socket cliente = server.accept();
                //System.out.println("Pedido do cliente com ip : " + cliente.getInetAddress());
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
