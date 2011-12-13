
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class HttpServer {

	public static final String index = "index.html";

	private File root = new File("./contents");
	private ServerSocket socket;
	
	public HttpServer(File root, int port) {
		this.root = root;
		try {
			socket = new ServerSocket(port);
			System.out.println("Server started\n\tListening on port: " + port);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//main server loop
	public void init() {
		while(true) {
			try {
				//accept a new connection from the browser
				Socket newConnection = socket.accept();
				// open an input stream from the browser (to read the HTTP request)
				InputStream fromBrowser = newConnection.getInputStream();
				// open an output stream to the browser (for sending the HTTP reply + data)
				OutputStream toBrowser = newConnection.getOutputStream();
				
				// create an HTTPRequest object from the data received from the input stream			
				HTTPRequest request = HTTPRequest.parseHTTPRequest(fromBrowser);
				System.out.println("Request (start)------------");
				System.out.println(request);
				System.out.println("Request (end)--------------\n");
				
				
				// identify the requested method
				if(request.requestType().equals(HTTPRequest.GET)) { // reply to a GET request
					System.out.println("browser sent a GET operation");
					
					// create an http reply based on the requested object
					HTTPReply reply = HTTPReply.createHTTPReply(root, request.requestedObject());
					System.out.println("Reply (start)------------");
					System.out.println(reply);
					System.out.println("Reply (end)--------------\n");
					
					// send reply
					toBrowser.write(reply.toString().getBytes());
					toBrowser.write("\r\n".getBytes());
					
					// if reply is OK
					if(reply.getHTTPResponse().equals(HTTPReply.HTTP_OK)) { // the files needs to be sent
						// open requested file
						// read it and send its contents to browser
						FileInputStream fis = new FileInputStream(reply.getObjectFile());
						byte[] buffer = new byte[4096];
						int read;
						while((read = fis.read(buffer))!= -1){
							toBrowser.write(buffer, 0, read);
						}
						// close file
						fis.close();
					}
					else { // file Not Found
						System.out.println("File not found");
					}
				}
				else if(request.requestType().equals(HTTPRequest.PUT)) { // PUT request
					System.out.println("PUT operation");
					
					//TODO: Implement this
					
					toBrowser.write((HTTPReply.HTTP_ERROR+"\r\n\r\n").getBytes());
				}
				else if(request.requestType().equals(HTTPRequest.POST)) { // POST request
					System.out.println("POST operation");
					
					//TODO: Implement this
					
					toBrowser.write((HTTPReply.HTTP_ERROR+"\r\n\r\n").getBytes());
				}
				else { // Unkown request
					System.out.println("ERROR");
					toBrowser.write((HTTPReply.HTTP_ERROR+"\r\n\r\n").getBytes());
				}
				
				//close input stream from browser
				fromBrowser.close();
				// close output stream to browser
				toBrowser.close();
				//close socket to browser
				newConnection.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) {
        boolean error = false;
        boolean help = false;
        int serverPort = 8090;
        File serverDir = new File("./contents");

        try {
            for (int i = 0; i < args.length; i++) {
                if (args[i].equalsIgnoreCase("--help")) {
                    help = true;
                } else if (args[i].startsWith("--server-dir=")) {
                    serverDir = new File(args[i].substring(13));
                } else if (args[i].startsWith("--port=")) {
                    serverPort = Integer.parseInt(args[i].substring(7));
                } else {
                    error = true;
                }
            }
        } catch (Exception e) {
            error = true;
        }
        if (error || help) {
            System.out.println("Use: java HttpServer [--help] [--server-dir=dir]");
        } else {
			HttpServer server = new HttpServer(serverDir, serverPort);
			server.init();
        }
	}

}
