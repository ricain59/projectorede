package proxy_cache;

/******************************************************************
*** File Daemon.java 
***
***/

import java.net.*;
import java.io.*;


// 
// Class:     Daemon
// Abstract:  Web daemon thread. creates main socket on port 8080
//            and listens on it forever. For each client request,
//            creates proxy thread to handle the request.
//
public class Daemon extends Thread
{
	//
	// Member variables
	//
	static ServerSocket MainSocket = null;
    static Cache cache = null;
	static Config config;
	static String adminPath;

	final static int defaultDaemonPort = 8080;
	final static int maxDaemonPort = 65536;

	
	//
	// Member methods
	//

	// Application starts here
	public static void main(String args[])
	{
		int daemonPort;

		// Parse command line
		switch (args.length)
		{
			case 0: daemonPort = defaultDaemonPort;
					break;

			case 1: try
					{
						daemonPort = Integer.parseInt(args[0]);
					}
					catch (NumberFormatException e)
					{
						System.out.println("Error: Invalid daemon port");
						return;
					}
					if (daemonPort > maxDaemonPort)
					{
						System.out.println("Error: Invalid daemon port");
						return;
					}
					break;

			default:System.out.println("Usage: Proxy [daemon port]");
					return;
		}

					
		
		try
		{
			// Create the Cache Manager and Configuration objects
			System.out.println("Initializing...");
			System.out.print("Creating Config Object...");
			config = new Config();
			config.setIsAppletContext(false);
			config.setLocalHost(InetAddress.getLocalHost().getHostName());
			String tmp = InetAddress.getLocalHost().toString();
			config.setLocalIP(tmp.substring(tmp.indexOf('/')+1));
			config.setProxyMachineNameAndPort(InetAddress.getLocalHost().getHostName()+":"+daemonPort);
			File adminDir = new File("Applet");
			config.setAdminPath(adminDir.getAbsolutePath());
			System.out.println("OK");

			System.out.print("Creating Cache Manager...");
			cache = new Cache(config);
			System.out.println("OK");

			// Start the admin thread
			System.out.print("Creating Admin Thread...");
			Admin adminThd = new Admin(config,cache); 
			adminThd.start();
			System.out.println(" port " + config.getAdminPort() + " OK");
			
			// Create main socket
            System.out.print("Creating Daemon Socket...");
			MainSocket = new ServerSocket(daemonPort);
			System.out.println(" port " + daemonPort + " OK");

			if (config.getIsFatherProxy())
			{
				System.out.println("Using Father Proxy "+
					config.getFatherProxyHost()+
					":"+config.getFatherProxyPort()+" .");
			}
			else
			{
				System.out.println("Not Using Father Proxy .");
			}
			System.out.println("Proxy up and running!");

			// Main loop
			while (true)
			{
				// Listen on main socket
				Socket ClientSocket = MainSocket.accept();

				// Pass request to new proxy thread
				Proxy thd = new Proxy(ClientSocket,cache,config);
				thd.start();
			}
			
		}
		
		catch (Exception e)
		{}

		finally
		{
			try
			{
				MainSocket.close();
			}
			catch (Exception exc)
			{
			}
		}
	}
}


