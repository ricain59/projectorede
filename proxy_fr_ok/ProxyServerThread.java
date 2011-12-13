package proxy_fr_ok;

import java.net.*;
import java.io.*;

/**
 * <p>Title: Serveur Proxy</p>
 * <p>Description: Etablissement d'un serveur proxy</p>
 * @author CHABLE Julien
 * @version 1.0
 */

public class ProxyServerThread extends Thread{

  private int port;

  public ProxyServerThread(int port) {
    this.port = port;
    start(); // On execute la thread;
  }

  public void run() {
    try {
      System.out.println("Serveur Proxy sur port : " + port + " demarre !");
      ServerSocket server = new ServerSocket(port);
      // On attend l'arrive d'un client
      while (!interrupted()) {
        Socket client = server.accept();
        System.out.println("Client : " + client.getInetAddress());
        new ProxyServerClientThread(client);	// On cree un nouveau client proxy
        sleep(5);
      }
      System.out.println("Serveur Proxy sur port : " + port + " arrete !");
    }
    catch (IOException e) {
      //...
    }
    catch (InterruptedException e) {
      //...
    }
  }
}