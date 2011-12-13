package irc.http;

import java.io.File;
import java.text.DateFormat;
import java.util.Date;

public class HTTPReply {
	
	private static final DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG);

	private HTTPResponce response;
	private Date currentDate;
	private Date lastModified;
	private String server = "MyServer";
	private String connection = "close";
	private String contentType;
	private long length;
	private File object;
	
	private static final String[] HTTPHeader = new String[] {"Date: ", "Server: ", "Last-Modified: ",
															 "Content-Length: ", "Connection: ", "Content-Type: "};
	private static final String[] contentTypes = new String[] {"text/html", "image/jpeg"};
	
	protected HTTPReply(HTTPResponce response, Date current, Date modified, String server, String connection, String contentType, long length, File f) {
		this.response = response;
		this.currentDate = current;
		this.lastModified = modified;
		if(server!= null)
			this.server = server;
		if(connection != null)
			this.connection = connection;
		this.contentType = contentType;
		this.length = length;
		this.object = f;
	}
	
	/*
	 * Returns the http reply code
	 */
	public String getHTTPResponse() {
		return response.toString();
	}
	
	/*
	 * Returns the requested file, or null if the file does not exist
	 */
	public File getObjectFile() {
		return object;
	}
	
	/*
	 * Converts this HTTPReply to a string
	 */
	public String toString() {
		if(!response.equals(HTTPResponce.OK))
			return response + "\r\n";
		else
			return  response + "\r\n" + 
					HTTPHeader[0] + dateFormat.format(currentDate) + "\r\n" + 
					HTTPHeader[1] + server + "\r\n" +
					HTTPHeader[2] + dateFormat.format(lastModified) + "\r\n" +
					HTTPHeader[3] + length + "\r\n" +
					HTTPHeader[4] + connection + "\r\n" + 
					HTTPHeader[5] + contentType + "\r\n";
	}
	
	/*
	 * Creates a new HTTPReply object that represents the response to the request sent from the browser
	 * The created request contains the HTTP reply, as well as other fields,
	 * The request also contains a reference to the requested object.
	 */
	public static HTTPReply createHTTPReply(File root, String request) {
		File f = null;
		if(request.equals("/"))
			f =  new File(root, HttpServer.index);
		else
			f = new File(root, request);
		
		if(f.exists()) {
			String type = contentTypes[1];
			if(f.getName().endsWith("html") || f.getName().endsWith("htm"))
				type = contentTypes[0];
			return new HTTPReply(HTTPResponce.OK, new Date(System.currentTimeMillis()), new Date(f.lastModified()), null, null, type, f.length(), f) ;
		}
		else {
			return new HTTPReply(HTTPResponce.NOT_FOUND, null, null, null, null, null, -1, null);
		}
	}
}
