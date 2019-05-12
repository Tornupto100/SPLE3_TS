package server;

import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Iterator;
import common.TextMessage;

/**
 * server's main class. accepts incoming connections and allows broadcasting
 */
public class Server {

	/**
	 * list of all known connections
	 */
	protected HashSet<Connection> connections = new HashSet<Connection>();
	private String pwdServer="1111";
	public FileWriter write2;
	protected ObjectInputStream cli;

	public static void main(String args[]) throws IOException {
//		if (args.length != 1)
//			throw new RuntimeException("Syntax: ChatServer <port>");
//		new Server(Integer.parseInt(args[0]));
		
		new Server(3000); // Erstelle einen Server mit Port nummer
	}

	/**
	 * awaits incoming connections and creates Connection objects accordingly.
	 * 
	 * @param port
	 *            port to listen on
	 */
	
	
	public Server(int port) throws IOException {
		ServerSocket server = new ServerSocket(port);   		// Initialsiere Server Object
		System.out.println("Initialized Logger");
		this.write2=new FileWriter("ServerChatHistory.txt"); 			//  3) Initilize your chat history book
		while (true) { 											// Solange wahr, als immer 
			System.out.println("Waiting for Connections..."); 
			Socket client = server.accept();					// Warte auf Verbindungen, bei Verbndung über den Client	
			
			
			//System.out.println(authentification(client));		// Funktioniert noch nicht
			/*
			if (authentification(client)) {
				System.out.println("Accepted from " + client.getInetAddress());
				Connection c = connectTo(client); // Übergebe den Client
				c.start();
			}
			else {
				client.close();}
			
			*/
			System.out.println("Accepted from " + client.getInetAddress());
			Connection c = connectTo(client); 					// Übergebe den Client
			c.start();
		}
	}
	
	
	private boolean authentification(Socket client) { // Funktioniert noch nicht
		boolean correct=false;
		String pwdClient1=null;
		Object msg;
			try {
				cli = new ObjectInputStream((client.getInputStream()));
				msg=cli.readObject();
				if (msg!=null) {
					pwdClient1=handleIncomingMessage(msg);
					System.out.println(pwdClient1);
					return correct;}
			} catch (IOException | ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return correct;}
			
	
	
	
	
	

	/**
	 * creates a new connection for a socket
	 * 
	 * @param socket
	 *            socket
	 * @return the Connection object that handles all further communication with
	 *         this socket
	 */
	public Connection connectTo(Socket socket) { // Hinzufügen von neuen Clients
		Connection connection = new Connection(socket, this);
		connections.add(connection);
		System.out.println("Teilnehmer im Chat hinzugefügt:" +connections.size());
		return connection;
	}

	/**
	 * send a message to all known connections
	 * 
	 * @param text
	 *            content of the message
	 */
	public void broadcast(String text) { // Verteilung an alle die im Server eingewählt wurden
		synchronized (connections) {
			for (Iterator<Connection> iterator = connections.iterator(); iterator.hasNext();) {
				Connection connection = (Connection) iterator.next();
				connection.send(text);
			}
		}
	}
	
	/**
	 * remove a connection so that broadcasts are no longer sent there.
	 * 
	 * @param connection
	 *            connection to remove
	 */
	public void removeConnection(Connection connection) {
		connections.remove(connection);
	}
	
	
	static protected String handleIncomingMessage(Object msg) { 	
		String pwd=null;
		if (msg instanceof TextMessage) {														
			pwd=((TextMessage) msg).getContent();
		}
		System.out.println("Incoming:"+pwd);
		return pwd;	
	}	
}

