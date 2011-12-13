package proxy_fr_ok;

//import server.time.*;
//import server.proxy.*;

import java.io.*;

/* La creation d'un serveur proxy HTTP necessite la connaisssance du protocole HTTP (RFC 2068).
 * Aussi attention ici la gestion des threads est inexistante (voir la fonction System.exit(0)
 * en fin de main. Pour cette raison il serait interessant de developper des threads daemon et
 * des groupes de threads, mais ce n'est pas le but de ce petit programme qui se veut le plus
 * simple possible.
 *
 * Pour toutes questions ou aide : webmaster@neogamedev.com*/

/**
 * <P>Description: Serveur proxy + horaire</P>
 * @author CHABLE Julien
 * @version 1.0
 */

public class startServer {

  private TimeServerThread timeServer; // Notre serveur horaire
  private ProxyServerThread proxyServer; // Notre proxy

  public startServer() {
    // Demarrage des serveurs
    System.out.println("Chargement des services en cours ...");
    timeServer = new TimeServerThread(3000);
    proxyServer = new ProxyServerThread(8080);
  }

  public void stop()
  {
    // Arret des services -> interruption des threads
    if (timeServer.isAlive())
      timeServer.interrupt();
    if (proxyServer.isAlive())
      proxyServer.interrupt();
  }

  public static void main(String[] args) {
    startServer server = new startServer(); // Demarrage du serveur (proxy + horaire)
    BufferedReader input = new BufferedReader(new InputStreamReader(System.in)); // Entree clavier utilisateur
    String command; // Ligne de commande
    boolean done = false;

    try {
      while (!done){
        System.out.print("Commande : ");
        command = input.readLine();
        if (command.toUpperCase().equals("QUIT")) // On saisit 'QUIT' pour quitter !
          done = true;
      }
    }
    catch (Exception e) {
      System.out.println("Erreur MAIN : " + e);
      System.exit(1);
    }
    finally {
      server.stop();
    }
    System.exit(0);
  }
}