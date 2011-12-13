package hamma;

import java.net.*;
import java.io.*;
import java.util.*;

public class Serveur {
	
	private 			Vector 		_tabClient=new Vector();
	private 			int 		_nbClient=0;
	public 	static 		String[] 	IP;//pour filtrer les ip qui se connecte
	
		
	
public static void main(String[] args) {
		Serveur server=new Serveur();
		try{
				
			 IP=new String[2];
		     IP[0]="/127.0.0.1";IP[1]="/195.68.1.11";
			 Integer port;
		      if(args.length<=0){
		    	  	port=new Integer("8080"); // si pas d'argument : port 18000 par d�faut
		      }
		      else {
		    	  	port = new Integer(args[0]); // sinon il s'agit du num�ro de port pass� en argument
		      }
			
			//on creer la connexion
		    ServerSocket theConnexion=new ServerSocket(port.intValue());
			System.out.println("---------Serveur lancer avec succes-------------");
			
			//boucle d attente de client
			
			while(true){
				/*--------------A chaque nouveau client on ouvre un thread proxy-------------------------------*/
				
				new Proxy(server,theConnexion.accept(),IP);
			}
			
		}
		catch(Exception e){
		}
	}

	
	synchronized public int addClient(PrintWriter out)
	  {
	    _nbClient++; // un client en plus ! ouaaaih
	    _tabClient.addElement(out); // on ajoute le nouveau flux de sortie au tableau
	    return _tabClient.size()-1; // on retourne le num�ro du client ajout� (size-1)
	  }
	  synchronized public void delClient(int i)
	  {
	    _nbClient--; // un client en moins ! snif
	    if (_tabClient.elementAt(i) != null) // l'�l�ment existe ...
	    {
	      _tabClient.removeElementAt(i); // ... on le supprime
	    }
	  }
	
	
}
