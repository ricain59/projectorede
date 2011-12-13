import java.io.OutputStream;
import java.util.ArrayList;
import java.io.DataOutputStream;
import java.io.PrintWriter;
import java.io.OutputStreamWriter;

class ClientToServerThread extends Thread
{

    private OutputStream toHost;
    private ArrayList pedido;

    public ClientToServerThread(DataOutputStream toHost, ArrayList pedido) throws Exception 
    {
        this.toHost = toHost;
        this.pedido = pedido;
        start();
    }

    public void run()
    {
        try 
        {
            PrintWriter out = new PrintWriter(new OutputStreamWriter(toHost));
            for (int i = 0; i < pedido.size(); ++i) {
                out.println(pedido.get(i));
            }
            out.println();
            out.flush();

        }
        catch (Exception e) {
            System.out.println("Erro ClientToServerThread : " + e);
        }
    }
}