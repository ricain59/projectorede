import java.net.*;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.sun.media.sound.Toolkit;

public class ThreadClient extends JFrame implements Runnable{

    private 		Thread		 		_t; // contiendra le thread du client
    public 		Socket 				_socket; // recevra le socket liant au client
    private 		PrintWriter 		_out; // pour gestion du flux de sortie
    private 		BufferedReader 		_in; // pour gestion du flux d'entr�e
    private 		Serveur 			_Serveur; // pour utilisation des m�thodes de la classe principale
    private 		int 				_numClient=0; // contiendra le num�ro de client g�r� par ce thread
    boolean 		flag=false;

    ThreadClient(Serveur s,Socket ss){
        this._Serveur=s;
        this._socket=ss;
        try {

            _out = new PrintWriter(new BufferedOutputStream(_socket.getOutputStream()));
            _in = new BufferedReader(new InputStreamReader(_socket.getInputStream()));
            System.out.print("nouvelle connexion avec -->   "+_socket.getInetAddress()+"   -- ");
            _numClient =  _Serveur.addClient(_out);

        }
        catch(IOException e){
            e.setStackTrace(null);  
        }

        _t=new Thread(this);
        _t.start();

    }
    public void run() {
        String message = "";
        System.out.println("no "+(_numClient+1));
        try{
            _out.println("HTTP/1.1 200 OK");
            _out.println("Server: myServer/4.7");
            _out.println("Date: 12/02/1986");
            _out.println("Content-type: text/html");
            _out.println("");
            _out.println("vous etes : "+(_numClient+1)+" Connectes sur le serveur");
            _out.println("<html><div style=\"background-color:red;text-align:center;\"><a href=\"programe.java\">hello everybody</a></div></html>");

			  
            //On recupere les information du client
            String line=_in.readLine();
            int i1=line.indexOf(" ");//renvoi lindice de la prmeir position
            int i2=line.lastIndexOf(" ");
            String mes=line.substring(i1+2,i2);
            System.out.println("Le client  numero :"+(_numClient+1)+" a demander :"+mes);
            _out.println("\n");
            _out.println("vous demandez : "+mes);
            _out.flush();
            // _out.close();
        }
        catch(Exception e){
            e.setStackTrace(null);
        }
        // _out.close();
        /* try{

        String line=_in.readLine();
        int i1=line.indexOf(" ");//renvoi lindice de la prmeir position
        int i2=line.lastIndexOf(" ");
        String mes=line.substring(i1+2,i2);
        System.out.println(mes);
        int nn=0;
        while(nn<50){
        sendMsg(mes);
        nn++;
        }
        /*while(flag==false){//tant que ya des ligne je lis

        if(mes.equals("\n")) {
        flag=true;
        break;//pour detecter la fin des lignes 
        }
        if(mes.equals("\r")) {
        flag=true;
        break;//ya des brother qui utilise plin de truk pr fin de fichier
        }
        if(mes.equals("\r\n")){
        flag=true;
        break;
        }
        if(mes.equals("")){
        flag=true;
        break;
        }

        /*BufferedReader theFile=new BufferedReader(new InputStreamReader(new FileInputStream("g.html")));//pour ouvrir le fichier html et le parcourir en entier
        String ll;
        ll=theFile.readLine();
        System.out.println("je lis le fichier "+ll);
        _out.println("<html><div style=\"background-color:red;text-align:center;\">hello everybody</html>");
        _out.flush();
        while(ll!=null){
        _out.println(ll);
        ll=theFile.readLine();
        }
        flag=true;
        }
        // _out.println("<html><div style=\"background-color:red;text-align:center;\">vous etes :"+_numClient+"</html>");
        //_out.flush();
        }
        catch(Exception e){

        }
         */

    }
    public void sendMsg (String sMsg){        
        _out.write(sMsg);
        _out.flush();
    }

}
/*
class Dial extends JFrame implements ActionListener,Runnable{

private JButton okBouton;
private JTextField login;
private JPasswordField pass;
private JLabel log,passs;
private boolean ok;
private			Serveur 			_serv;
private 		Socket 				_socket;
private 		Thread		 		_t;
Dial(){

setTitle("Proxy de mohamed");
setSize(330,200);

Container contenu=getContentPane();
contenu.setLayout(null);

log=new JLabel("Login");log.setBounds(20, 50, 50, 20);contenu.add(log);
JTextField login=new JTextField();login.setBounds(100,50 , 150, 20);contenu.add(login);

okBouton=new JButton("Connexion");
okBouton.setBounds(100,120,100, 20);
contenu.add(okBouton);

okBouton.addActionListener(this);

}

public void actionPerformed(ActionEvent e) {
try{
if(e.getSource()==okBouton){

String name=login.getText();

//Dial.setVisible(false);
}
}
catch(Exception ee){
ee.setStackTrace(null);
}

}

public void run() {
System.out.println("ddd");
new ThreadClient(_serv,_socket);

}

	
}
 */