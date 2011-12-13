/******************************************************************
*** File Proxy.java 
***
***/


import java.net.*;
import java.io.*;
import java.util.*;


//
// Class:     Proxy
// Abstract:  Thread to handle one client request. get the requested
//            object from the web server or from the cache, and delivers
//            the bits to client.
//
public class Proxy extends Thread
{
	//
	// Member variables
	//
	Socket ClientSocket = null;              // Socket to client
	Socket SrvrSocket = null;                // Socket to web server
    Cache  cache = null;                     // Static cache manager object 
	String localHostName = null;             // Local machine name
	String localHostIP = null;               // Local machine IP address
	String adminPath = null;                 // Path of admin applet
	Config config = null;                    // Config object

	
	//
	// Public member methods
	//

	//
	// Constructor
	//
	Proxy(Socket clientSocket, Cache CacheManager, Config configObject)
	{
		//
		// Initialize member variables
		//
		config = configObject;
		ClientSocket = clientSocket;     
		cache = CacheManager;
		localHostName = config.getLocalHost();
		localHostIP = config.getLocalIP();
		adminPath = config.getAdminPath();
    }


	//
	// run - Main work is done here:
	//
    public void run()
	{
        String serverName ="";
		URL url;

		byte line[];
        HttpRequestHdr request = new HttpRequestHdr();
        HttpReplyHdr   reply   = new HttpReplyHdr();
		FileInputStream fileInputStream = null;
		FileOutputStream fileOutputStream = null;
		boolean TakenFromCache = false;
		boolean isCachable = false;

		try
		{
			//
			// Read HTTP Request from client 
			//
            request.parse(ClientSocket.getInputStream());
            url = new URL(request.url);
            System.out.println("Request = " + url);
            
			//
			// Send Web page with applet to administrator
			//
			if (url.getFile().equalsIgnoreCase("/admin") &&
				(url.getHost().equalsIgnoreCase(localHostName) ||
				 url.getHost().equalsIgnoreCase(localHostIP) ))
			{
				sendAppletWebPage();
				return;
			}

			//
			// Send Applet Files to administrator
			//
			if ((url.getHost().equalsIgnoreCase(localHostName) ||
				 url.getHost().equalsIgnoreCase(localHostIP) ))
			{
				sendAppletClass(url.getFile());
				return;
			}

			//
			// Check if accessing the URL is allowed by administrator
			//
			String[] denied = config.getDeniedHosts();
			for (int i=0; i<denied.length; i++)
			{
				if (url.toString().indexOf(denied[i]) != -1)
				{
					System.out.println("Access not allowed...");
					DataOutputStream out = 
					   new DataOutputStream(ClientSocket.getOutputStream());
					out.writeBytes(reply.formNotAllowed());
					out.flush();
				    ClientSocket.close();
					return;
				}
			}
				
			
			//
			// Client wants a web page - let's see if we have it in cache
			//
			if (cache.IsCached(url.toString()))
			{
				//
				// Client request is allready cached - get it from file
				//				
				System.out.println("Hit! Getting from cache!!!");
				config.increaseHits();
				TakenFromCache = true;

				// Get FileInputStream from Cache Manager
				fileInputStream  = cache.getFileInputStream(url.toString());
				OutputStream out = ClientSocket.getOutputStream();

				// Send the bits to client
				byte data[] = new byte[2000];
				int count;
				while (-1 < ( count  = fileInputStream.read(data)))
				{
					out.write(data,0,count);
				}
				out.flush();
				fileInputStream.close();
			}


            //
			// We do not have the page in cache
			// 
			else
			{
				//
				// Open socket to web server (or father proxy)
				//
				if (config.getIsFatherProxy())
				{
				    System.out.println("Miss! Forwarding to father proxy " +
						config.getFatherProxyHost() + ":" + 
						config.getFatherProxyPort() + "...");
					config.increaseMisses();
					SrvrSocket = new Socket(config.getFatherProxyHost(),
						                    config.getFatherProxyPort());
				}
				else
				{
				    serverName  = url.getHost();
					System.out.println("Miss! Forwarding to server "+
						serverName + "...");
					config.increaseMisses();
					SrvrSocket = new Socket(serverName(request.url),
										    serverPort(request.url));
					request.url = serverUrl(request.url);
				}
           
				DataOutputStream srvOut = 
				   new DataOutputStream(SrvrSocket.getOutputStream());


				//
				// Send the url to web server (or father proxy)
				//
				srvOut.writeBytes(request.toString(false));
				srvOut.flush();

				//
				// Send data to server (needed for post method)
				//
				for (int i =0; i < request.contentLength; i++)
				{
				   SrvrSocket.getOutputStream().write(ClientSocket.getInputStream().read());    
				}
				SrvrSocket.getOutputStream().flush(); 


				//
				// Find if reply should be cached - 
				//   First, check if caching is on.
				//
				isCachable = config.getIsCaching();
				String reasonForNotCaching = "Caching is OFF.";

				// Second, parse the URL for special characters.
				if (isCachable)
				{
					isCachable = cache.IsCachable(url.toString());
					reasonForNotCaching = "URL not cacheable";
				}

				// Third, check reply headers (we must read first
				//         line of headers for that).
				DataInputStream  Din  = 
				   new DataInputStream(SrvrSocket.getInputStream());
				DataOutputStream Dout = 
				   new DataOutputStream(ClientSocket.getOutputStream());
				String str = Din.readLine();
				StringTokenizer s = new StringTokenizer(str);
				String retCode = s.nextToken(); // first token is HTTP protocol
				retCode = s.nextToken(); // second is return code
				// Return codes 200,302,304 are OK to cache
				if (isCachable)
				{
					if (!retCode.equals("200") && !retCode.equals("302")
						&& !retCode.equals("304"))
					{
						isCachable = false;
                        reasonForNotCaching = "Return Code is "+retCode;
					}
				}

				// Fourth, check if URL is cache-allowed by administrator
				if (isCachable)
				{
					String[] denyCache = config.getCacheMasks();
					for (int i=0; i<denyCache.length; i++)
					{
						if (url.toString().indexOf(denyCache[i]) != -1)
						{
							isCachable = false;
							reasonForNotCaching = "Caching this URL is not allowed";
							break;
						}
					}
				}

                if (isCachable)
				{
					System.out.println("Caching the reply...");
					fileOutputStream = cache.getFileOutputStream(url.toString());
				}
				else
				{
					System.out.println("NOT Caching the reply. Reason:"
						+reasonForNotCaching);
				}
			
				// 
				// First line was read - send it to client and cache it
				//
				String tempStr = new String(str+"\r\n");
				Dout.writeBytes(tempStr);
				if (isCachable)
				{
					// Translate reply string to bytes
					line = new byte[tempStr.length()];
					tempStr.getBytes(0,tempStr.length(),line,0);
					
					// Write bits to file
					fileOutputStream.write(line);
					cache.DecrementFreeSpace(line.length,url.toString());
				}


				// 
				// Read next lines in reply header, send them to
				// client and cache them
				//
				if (str.length() > 0)
					while (true)
					{
						str = Din.readLine();
						tempStr = new String(str+"\r\n");

						// Send bits to client
						Dout.writeBytes(tempStr);
						
						if (isCachable)
						{
							// Translate reply string to bytes
							line = new byte[tempStr.length()];
							tempStr.getBytes(0,tempStr.length(),line,0);
							
							// Write bits to file
							fileOutputStream.write(line);
							cache.DecrementFreeSpace(line.length,url.toString());
						}

						if (str.length() <= 0) 
							break;
					}
				Dout.flush();
            

				//
				// With the HTTP reply body do:
				//   (1) Send it to client.
				//   (2) Cache it.
				//
				InputStream  in  = SrvrSocket.getInputStream();
				OutputStream out = ClientSocket.getOutputStream();

				byte data[] = new byte[2000];
				int count;
				while (( count  = in.read(data)) > 0)
				{
					// Send bits to client
					out.write(data,0,count);

					if (isCachable)
					{
						// Write bits to file
						line  = new byte[count];
						System.arraycopy(data,0,line,0,count);
						fileOutputStream.write(line);
						cache.DecrementFreeSpace(count,url.toString());
					}
				}
				out.flush();
				if (isCachable)
				{
					fileOutputStream.close();

					// Add new entry to hash table
					cache.AddToTable(url.toString());
				}

			}
		}
           
        catch (UnknownHostException uhe)
		{
			//
            // Requested Server could not be located
            //
            System.out.println("Server Not Found.");
           
            try
			{
				// Notify client that server not found
				DataOutputStream out = 
                   new DataOutputStream(ClientSocket.getOutputStream());
                out.writeBytes(reply.formServerNotFound());
                out.flush();
            }
			catch (Exception uhe2)
			{}
		}
		
		catch (Exception e)
		{
            try
			{
				if (TakenFromCache)
					fileInputStream.close();
				else if (isCachable)
					fileOutputStream.close();
				
				// Notify client that internal error accured in proxy
				DataOutputStream out = 
                   new DataOutputStream(ClientSocket.getOutputStream());
                out.writeBytes(reply.formTimeout());
                out.flush();                
				
            }
			catch (Exception uhe2)
			{}
        }
		
		finally
		{
			try
			{
				ClientSocket.getOutputStream().flush();
				ClientSocket.close();
			}
			catch (Exception e)
			{} 
		}
    }


  
	//
	// Private methods
	//

	//
	// Send to administrator web page containing reference to applet
	//
	private void sendAppletWebPage()
	{
		System.out.println("Sending the applet...");
		String page = "";
		try
		{
			File appletHtmlPage = new File(config.getAdminPath() +
										   File.separator + "Admin.html");
			DataInputStream in = new DataInputStream(new FileInputStream(appletHtmlPage));

			String s = null;

			while((s = in.readLine()) != null)
				page += s;
										   
			page =	page.substring(0,page.indexOf("PORT")) +
					config.getAdminPort() + 
					page.substring(page.indexOf("PORT")+4);
			
			in.close();
			DataOutputStream out = new DataOutputStream(ClientSocket.getOutputStream());
			out.writeBytes(page);
			out.flush();
			out.close();
		}
		catch (Exception e)
		{
			System.out.println("Error: can't open applet html page");
		}

	}



	//
	// Send the applet to administrator
	//
	private void sendAppletClass(String className)
	{
		try
		{
			byte data[] = new byte[2000];
			int count;
			HttpReplyHdr   reply   = new HttpReplyHdr();
			File appletFile = new File(adminPath + File.separatorChar + className);
			long length = appletFile.length();

			FileInputStream in = new FileInputStream(appletFile);
			DataOutputStream out = new DataOutputStream(ClientSocket.getOutputStream());

			out.writeBytes(reply.formOk("application/octet-stream",length));
			
			while (-1 < ( count  = in.read(data)))
			{
				out.write(data,0,count);
			}
			out.flush();
			in.close();
			out.close();
		}
		catch (Exception e)
		{}
	}



	//
	// Parsing Methods
	//

    /**
     * Find the //server.name from an url.
     *
     * @return Servers internet name
     */
    private String serverName(String str)
	{
        // chop to "server.name:x/thing"
        int i = str.indexOf("//");   
        if (i< 0) return "";  
        str = str.substring(i+2);
       
        // chop to  server.name:xx
        i = str.indexOf("/");
        if (0 < i) str = str.substring(0,i);
 
        // chop to server.name
        i = str.indexOf(":");
        if (0 < i) str = str.substring(0,i);
       
        return str;  
    } 
   
    /**
     * Find the :PORT form http://server.ect:PORT/some/file.xxx
     *
     * @return Servers internet name
     */
    private int serverPort(String str)
	{
        // chop to "server.name:x/thing"
        int i = str.indexOf("//");   
        if (i< 0) return 80;  
        str = str.substring(i+2);
       
        // chop to  server.name:xx
        i = str.indexOf("/");
        if (0 < i) str = str.substring(0,i);
 
        // chop XX
        i = str.indexOf(":");
        if (0 < i)
		{
            return Integer.parseInt(str.substring(i).trim());
        }
       
        return 80;  
    }  

    /**
     * Find the /some/file.xxxx form http://server.ect:PORT/some/file.xxx
     *
     * @return the deproxied url
     */
    private String serverUrl(String str)
	{
        int i = str.indexOf("//");   
        if (i< 0) return str;   

        str = str.substring(i+2);
        i = str.indexOf("/");   
        if (i< 0) return str;  

        return str.substring(i);   
    }
}
