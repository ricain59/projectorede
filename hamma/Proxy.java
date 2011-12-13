package hamma;

import java.net.*;
import java.util.LinkedList;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;


/*------------------------Je lance un proxy pour chaque nouveau connecte------------------------------------------------*/
/*------------------------------------------------------------------------------------*/
public class Proxy implements Runnable{

	private			Serveur 			_serv;
	private 		Socket 				_socket;
	private 		Thread		 		_t;
	private 		PrintWriter 		_out; // pour gestion du flux de sortie
	private 		BufferedReader 		_in;
	private 		String[]   			IP;//pou recuper les IP permis
	
 	/*dans adresseInterdite je met l'adresse IP des gens que je veux pas qu il accede sur mon serveur-------*/
	
	//private String	adresseInterdite="/127.0.0.1";
	

/*-------------------------Constructeur qui pren le serveur et le socket du client-------------------------------------*/	
	Proxy(Serveur s,Socket ss,String[] ip){
		this._serv=s;
		this._socket=ss;
		this.IP=ip;
		try{
			
			  _out = new PrintWriter(new BufferedOutputStream(_socket.getOutputStream()));
			  _in = new BufferedReader(new InputStreamReader(_socket.getInputStream()));
			  System.out.println("on est ds le proxy");
		}
		catch(Exception e){
			
		}

/*-----------------------------pour chaque client je lance un thread client apres filtrage-------------------------------------*/		
		 _t=new Thread(this);
		 _t.start();
		
	}

	

/*--------------------------------Lancement du tread pour chaque client------------------------------------*/
	public void run() {
		/*IpClient est l adresse IP du client qui s est connecte*/
		InetAddress IpClient= _socket.getInetAddress();
		/*Je transforme cette adress ip en string pour la comparer avec les IP Interdit*/
		String IpClientS=IpClient.toString();
		
		System.out.println("IP du client ---> : "+IpClientS);
		
		for(int i=0;i<IP.length;i++){
				if (IP[i].equalsIgnoreCase(IpClientS)){
					//si je rentre C.A.D mon ip a le droit d y acceder
					System.out.println("Acces autorise");
					new ThreadClient(_serv,_socket);
					break;
				}
		}
		
}
	
}


