package proxy_fr_ok;

import java.net.*;
import java.io.*;
import java.util.*;

/**
 * <p>Title: Serveur Proxy Client</p>
 * <p>Description: Partie du serveur qui traite reellement la connexion 
 * entre le client et le serveur</p>
 * @author CHABLE Julien
 * @version 1.0
 */

public class ProxyServerClientThread extends Thread{

    private Socket socket, socketServer;
    private DataInputStream clientIn, serverIn;
    private DataOutputStream clientOut, serverOut;
    private String get, host;
    private ArrayList requete;
    private int port;

    public ProxyServerClientThread(Socket socket) {
        this.socket = socket;
        port = socket.getPort();
        try {
            // On cree les flux pour la socket client
            clientIn = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            clientOut = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
            start();
        }
        catch (IOException e) {
            System.out.println("Erreur IO Client Proxy Server : " + e);
        }
    }

    private void readRequete() throws Exception {
        BufferedReader bin = new BufferedReader(new InputStreamReader(clientIn));
        String line;
        while ((line = bin.readLine()) != null) {
            if (line.length() == 0)
                break;
            /* On transforme la requete du client car il ne s'agit pas de transmettre au serveur
             * l'URL complete mais juste l'element e telecharger, c'est pourquoi on reconstruit la
             * ligne GET */
            if (line.trim().toUpperCase().startsWith("GET")) {
                String url = line.substring(3);
                String rest = "";
                // Obtenir http
                int posit = url.toUpperCase().lastIndexOf("HTTP");
                if (posit >= 0) {
                    rest = url.substring(posit).trim();
                    url = url.substring(0, posit).trim();
                } else {
                    url = url.trim();
                }
                get = url;

                // Creation nouveau GET -> GET http://ordinateur.xyz:80/toto.htm HTTP 1.0
                URL getURL = new URL(url);
                requete.add("GET " + getURL.getFile() + " " + rest);
                System.out.println("2222GET " + getURL.getFile() + " " + rest);
                System.out.println(socket.getInetAddress().getHostName() + " GET " + url);
            } else {
                requete.add(line);
            }

            // HOST contient l'adresse du serveur
            if (line.trim().toUpperCase().startsWith("HOST:"))
                host = line.substring(5).trim();
                System.out.println("le host:"+host);
        }
    }

    public void run() {
        try {
            get = host = "";
            requete = new ArrayList();
            // Obtention et transformation de la requete du client
            readRequete();
            System.out.println("get avant le try: "+get);
            // Connexion avec le serveur
            try {
                URL url = new URL(get);
                System.out.println("url:"+url);
                int port = url.getPort() > 0 ? url.getPort() : 80;
                socketServer = new Socket(url.getHost(),port);
                System.out.println("url.gethost:"+url.getHost());
            }
            catch (Exception e) {
                // Envoi de l'erreur au client
                PrintWriter out = new PrintWriter(new OutputStreamWriter(clientOut));

                out.println("HTTP/1.0 200 ");
                out.println("Content-Type: text/plain");
                out.println();

                out.println("GET:  " + get);
                out.println("HOST : " + host);
                out.println("Erreur !");
                out.println(e);
                out.flush();

                if (clientIn != null)
                    clientIn.close();
                if (clientOut != null)
                    clientOut.close();
                if (socket != null)
                    socket.close();
                if (socketServer != null)
                    socketServer.close();

                return;
            }
            // Creation flux serveur
            serverIn = new DataInputStream(new BufferedInputStream(socketServer.getInputStream()));
            serverOut = new DataOutputStream(new BufferedOutputStream(socketServer.getOutputStream()));
            // Envoi de la requete au serveur
            new ClientToServerThread(serverOut, requete);
            byte [] reponse = new byte[4096];
            int bytesRead;
            // Lecture des informations du serveur et envoi au client
            while ((bytesRead = serverIn.read(reponse)) != -1) {
                clientOut.write(reponse, 0, bytesRead);
                clientOut.flush();
            }
        }
        catch (Exception e) {
            System.out.println("Erreur Client Thread Serveur Proxy sur port : " + port);
        }
        finally {
            try {
                if (clientIn != null)
                    clientIn.close();
                if (clientOut != null)
                    clientOut.close();
                if (socket != null)
                    socket.close();
                if (socketServer != null)
                    socketServer.close();
                if (socket != null)
                    socket.close();
                if (socketServer != null)
                    socketServer.close();
            }
            catch (Exception e) {
                System.out.println("Erreur Client Thread Serveur Proxy sur port " + port);
            }
        }
    }
}

class ClientToServerThread extends Thread{

    private DataOutputStream serverOut;
    private ArrayList requete;

    public ClientToServerThread(DataOutputStream serverOut, ArrayList requete) throws Exception {
        this.serverOut = serverOut;
        this.requete = requete;
        start();
    }

    public void run() {
        try {
            PrintWriter out = new PrintWriter(new OutputStreamWriter(serverOut));
            for (int i = 0; i < requete.size(); ++i) {
                out.println(requete.get(i));
            }
            out.println(); // Envoyer une ligne vierge -> fin de la requete
            out.flush();
        }
        catch (Exception e) {
            System.out.println("Erreur ClientToServerThread : " + e);
        }
    }
}