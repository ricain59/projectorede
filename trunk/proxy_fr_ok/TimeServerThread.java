package proxy_fr_ok;

import java.net.*;
import java.io.*;
import java.util.Date;

/**
 * <p>Titre : Serveur Horaire</p>
 * <p>Description : Serveur horaire (Attention : non conforme aux RFC !)
 * -> le serveur envoi l'heure au client.</p>
 * @author CHABLE Julien
 * @version 1.0
 */

public class TimeServerThread extends Thread{

  private int port = 0;

  public TimeServerThread(int port) {
    this.port = port;
    start();	// On lance la thread
  }

  public void run(){
    try{
      System.out.println("Serveur horaire sur port : " + port + " demarre !");
      ServerSocket server = new ServerSocket(port);	// On initialise une socket serveur sur le port desire
      while (!interrupted()){
        Socket client = server.accept();	// On accepte le client
        System.out.println("Connecte au client " + client.getInetAddress());
        // On envoit l'heure et la date au client
        OutputStream out = client.getOutputStream();
        Date date = new Date();
        byte b[] = (date.toString() + "\n").getBytes();
        out.write(b);
        client.close();
        sleep(5); // On met en sommeil la thread en sommeil pour 5 millisecondes
      }
      System.out.println("Serveur horaire sur port : " + port + " arrete !");
    }
    catch (IOException e){
      System.out.println("Erreur du serveur sur la connexion reseau : " + e);
    }
    catch (InterruptedException e){
      System.out.println("Erreur de thread : " + e);
    }
  }
}
