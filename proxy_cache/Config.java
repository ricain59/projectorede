/******************************************************************
*** File Config.java 
***
***/

import java.util.*;
import java.net.*;
import java.io.*;

// 
// Class:     Config
// Abstract:  Configurable parameters of the proxy. This class is
//            used by both the applet and the proxy.
//

class Config 
{
	//
	// Member variables
	//
    private boolean  isFatherProxy;
    private String   fatherProxyHost;
    private int      fatherProxyPort;
    private String[] deniedHosts;
    private String   password;
	private boolean  isCaching;  // enable/disable caching
	private long 	 cacheSize;  // cache size in bytes
    private boolean  cleanCache;
    private String[] cacheMasks;
    
    private long     filesCached;
    private long     bytesCached;
    private long     bytesFree;
    private long     hits;
    private long     misses;

    private final int defaultProxyPort = 8080;
    private final String defaultPassword = "admin";
    private final long defaultCacheSize = 1000000;
	
	private String adminPath;
	private int    adminPort;

	private String localHost;
	private String localIP;

	private boolean isAppletContext;
	private String  separator = " ";
	private String proxyMachineNameAndPort;


	//
	// Member methods
	//

	//
	// Constructor
	//
	Config()
	{        
		filesCached = 0;
		bytesCached = 0;
		bytesFree = cacheSize;
		hits = 0;
		misses = 0;

		reset();
	}

	//
	// Re-initialize
	//
	public void reset()
	{
        isFatherProxy = true;
        fatherProxyHost = "wwwproxy.ac.il";
        fatherProxyPort = defaultProxyPort;
        password = defaultPassword;
    	isCaching = true;
    	cacheSize = defaultCacheSize;
        cleanCache = false;

		deniedHosts = new String[0];
		cacheMasks = new String[0];
	}


	//
	// Set/get methods
	//

	// Set if we are in the applet or in the proxy
	public void setIsAppletContext(boolean b)
	{
		isAppletContext = b;
	}

	public void setProxyMachineNameAndPort(String s)
	{
		proxyMachineNameAndPort = s;
	}

	public String getProxyMachineNameAndPort()
	{
		return proxyMachineNameAndPort;
	}

	public int getAdminPort()
	{
		return adminPort;
	}

	public void setAdminPort(int port)
	{
		adminPort = port;
	}
	
	public void setAdminPath(String path)
	{
		adminPath = path;
	}

	public String getAdminPath()
	{
		return adminPath;
	}
	
	public void setLocalHost(String host)
	{
		localHost = host;
	}

	public String getLocalHost()
	{
		return localHost;
	}
	
	public void setLocalIP(String ip)
	{
		localIP = ip;
	}
	
	public String getLocalIP()
	{
		return localIP;
	}
	
	boolean getIsCaching()
	{
		return isCaching;
	}
	
	public synchronized void setIsCaching(boolean caching)
	{
		isCaching = caching;
	}

	public synchronized long getCacheSize()
	{
		return cacheSize;
	}
	public synchronized void setCacheSize(long size)
	{
		cacheSize = size;
	}

    public boolean getIsFatherProxy()
    {
        return isFatherProxy;
    }
    
    public synchronized void setIsFatherProxy(boolean fatherProxy)
    {
        isFatherProxy  = fatherProxy;
    }
    
    public String getFatherProxyHost()
    {
        return fatherProxyHost;
    }
    
    public synchronized void setFatherProxyHost(String host)
    {
        fatherProxyHost = host;
    }
    
    public int getFatherProxyPort()
    {
        return fatherProxyPort;
    }
    
    public synchronized void setFatherProxyPort(int port)
    {
        fatherProxyPort = port;
    }
    
    public String[] getDeniedHosts()
    {
        return deniedHosts;
    }
    
    public synchronized void setDeniedHosts(String[] hosts)
    {
        deniedHosts = hosts;
    }
    
    public String getPassword()
    {
        return password;
    }
    
    public synchronized void setPassword(String newPassword)
    {
        password = newPassword;
    }
    
    public boolean getCleanCache()
    {
        return cleanCache;
    }
    
    public synchronized void setCleanCache(boolean clean)
    {
        cleanCache = clean;
    }
    
    public String[] getCacheMasks()
    {
        return cacheMasks;
    }
    
    public synchronized void setCacheMasks(String[] masks)
    {
        cacheMasks = masks;
    }
    
    public long getFilesCached()
    {
        return filesCached;
    }
    
    public synchronized void increaseFilesCached()
    {
        filesCached++;
    }

    public synchronized void decreaseFilesCached()
    {
        filesCached--;
    }

	public synchronized void setFilesCached(long number)
	{
		filesCached = number;
	}
    
    public long getBytesCached()
    {
        return bytesCached;
    }
    
    public synchronized void setBytesCached(long number)
    {
        bytesCached = number;
    }

    public long getBytesFree()
    {
        return cacheSize - bytesCached;
    }
    
    public long getHits()
    {
        return hits;
    }
    
    public synchronized void increaseHits()
    {
        hits++;
    }

	public synchronized void setHits(long number)
	{
		hits = number;
	}
    
    public long getMisses()
    {
        return misses;
    }
    
    public synchronized void increaseMisses()
    {
        misses++;
    }

	public synchronized void setMisses(long number)
	{
		misses = number;
	}

    public double getHitRatio()
    {
        if ((hits + misses)==0)
            return 0;
        else
            return 100*hits / (hits + misses);
    }
    

	//
	// Construct a string with all parameters
	//
	public synchronized String toString()
	{
		int i;
		String s = "";
		s += isFatherProxy + separator;
        s += fatherProxyHost.equals("") ? "NULL" : fatherProxyHost;
        s +=                    separator +
        fatherProxyPort       + separator;

        s += deniedHosts.length + separator;
        for (i=0; i<deniedHosts.length; i++)
            s += deniedHosts[i] + separator;
            
        s +=
        password              + separator +
    	isCaching             + separator +
    	cacheSize             + separator +
        cleanCache            + separator;

        s += cacheMasks.length + separator;
        for (i=0; i<cacheMasks.length; i++)
            s += cacheMasks[i] + separator;
            
        s+= proxyMachineNameAndPort + separator;
		
		s +=
        filesCached           + separator +
        bytesCached           + separator +
        bytesFree             + separator +
        hits                  + separator +
        misses                + separator +
		"\n";
		return s;
	}

	//
	// Set parameters according to a string (that was sent by applet)
	//
	public synchronized void parse(String config)
	{
		System.out.println("Parsing administrator request...");
		int size,i;
		StringTokenizer s = new StringTokenizer(config,separator);

        isFatherProxy       = s.nextToken().equals("true");
		System.out.println("Use father proxy = "+isFatherProxy);
        fatherProxyHost     = s.nextToken();
        if(fatherProxyHost.equals("NULL"))
           fatherProxyHost = ""; 
		System.out.println("Father proxy name = "+fatherProxyHost);

        fatherProxyPort     = Integer.parseInt(s.nextToken());
		System.out.println("Father proxy port = "+fatherProxyPort);

        size = Integer.parseInt(s.nextToken());
        deniedHosts = new String[size];
        for (i=0; i<size; i++)
		{
            deniedHosts[i] = s.nextToken();
			System.out.println("Deny access to "+deniedHosts[i]);
		}

        password            = s.nextToken();
		System.out.println("password = "+password);
    	isCaching           = s.nextToken().equals("true");
		System.out.println("Caching = "+isCaching);
    	cacheSize           = Long.parseLong(s.nextToken());
		System.out.println("Cache size = "+cacheSize);
        cleanCache          = s.nextToken().equals("true");
		System.out.println("Do cache clean up = "+cleanCache);
 
        size = Integer.parseInt(s.nextToken());
        cacheMasks = new String[size];
        for (i=0; i<size; i++)
		{
            cacheMasks[i] = s.nextToken();
			System.out.println("Don't cache "+cacheMasks[i]);
		}

        proxyMachineNameAndPort = s.nextToken();
		
		if (isAppletContext)
		{
			filesCached         = Long.parseLong(s.nextToken());
			bytesCached         = Long.parseLong(s.nextToken());
			bytesFree           = Long.parseLong(s.nextToken());
			hits                = Long.parseLong(s.nextToken());
			misses              = Long.parseLong(s.nextToken());
		}

		//
		// Update bytesFree to reflect the change in cache size.
		// Note that free bytes can be below min free level now.
		//
		bytesFree = cacheSize - bytesCached;
	}
}

