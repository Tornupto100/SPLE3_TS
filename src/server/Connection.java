package server;

import java.io.EOFException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.sql.Date;
import java.util.Calendar;

import common.TextMessage;

/**
 * class for an individual connection to a client. allows to send messages to
 * this client and handles incoming messages.
 */
public class Connection extends Thread {
	protected Socket socket;
	protected ObjectInputStream inputStream;
	protected ObjectOutputStream outputStream;
	private Server server;
	//private ObjectOutputStream logBookStream; // Outputstream für das Logbuch

	public Connection(Socket s, Server server) {
		this.socket = s;
		try {
			inputStream = new ObjectInputStream((s.getInputStream()));  // Erhält hier den Input als auch den Outputstream, aber warum?
			outputStream = new ObjectOutputStream((s.getOutputStream()));
			
		} catch (IOException e) {
			e.printStackTrace();
		}

		this.server = server;			
	}
	

	/**
	 * waits for incoming messages from the socket
	 */
	public void run() {
		String clientName = socket.getInetAddress().toString(); 
		try {
			server.broadcast(clientName + " has joined."); // Jungs ich bin jetzt da
			Object msg = null;
			while ((msg = inputStream.readObject()) != null) {  //Einlesen des Inputstreams
				handleIncomingMessage(clientName, msg);
			}
		} catch (SocketException e) {
		} catch (EOFException e) {
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			server.removeConnection(this);
			server.broadcast(clientName + " has left.");
			try {
				socket.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	/**
	 * decides what to do with incoming messages
	 * 
	 * @param name
	 *            name of the client
	 * @param msg
	 *            received message
	 */
	private void handleIncomingMessage(String name, Object msg) {
		if (msg instanceof TextMessage) {
			
			String incomingMessage = ((TextMessage) msg).getContent(); // Hier wird die Message entschlüsselt zum String
			//server.broadcast(name + " - " + incomingMessage); 			// Hier muss die Nummer, der Name des Sockets rein
			server.broadcast(incomingMessage); 							// <-- Für die Verschlüsselung
		}
	}

	/**
	 * sends a message to the client
	 * 
	 * @param line
	 *            text of the message
	 */
	
		
	public void send(String line) { // Nimm den String
		Calendar cal = Calendar.getInstance();
		send(new TextMessage(line));
		try {
			server.write2.write(cal.getTime()+":"+line+"\n"); // Erstellen eines Backlogs
			server.write2.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void send(TextMessage msg) { 		// Wandel den String in msg object um
		try {
			synchronized (outputStream) { 		// an alle outputstream
				outputStream.writeObject(msg); 	// füge die Nachrichten dem Client Outputstream hinzu
			}
				outputStream.flush(); 			// Absenden der Nachrichten				
		} catch (IOException ex) {
		}
	}
	
}
