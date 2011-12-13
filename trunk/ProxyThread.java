import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;

public class ProxyThread extends Thread
{

    private int porta;

    public ProxyThread(int porta) 
    {
        this.porta = porta;
        start();
    }

    public void run() {
        try {
            System.out.println("Servidor Proxy na porta : " + porta);
            ServerSocket server = new ServerSocket(porta);

            while (!interrupted()) {
                Socket cliente = server.accept();
                System.out.println("Pedido do cliente com ip : " + cliente.getInetAddress());
                new ProxyClientThread(cliente);
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
