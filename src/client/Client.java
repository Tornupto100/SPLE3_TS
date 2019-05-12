package client;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

import common.Change;
import common.Normal;
import common.ROT13; 
import common.TextMessage;

/**
 * simple chat client
 */
public class Client implements Runnable {
	
	protected ObjectInputStream inputStream;
	protected ObjectOutputStream outputStream;
	BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
	public String alias="Client";
	public FileWriter write2; 													// Log File für den Client
	
	// Verschlüsselungsmethoden
	//ROT13 hide=new ROT13(); 	// Objects for encryption
	//Change hide=new Change(); // Uncomment for swap encryption
	Normal hide=new Normal(); // No encryption
	
	// Password für Abfrage
	private String pwd="1111";
	
	protected Thread thread;
	
	public static void main(String args[]) throws IOException {
//		if (args.length != 2)
//			throw new RuntimeException("Syntax: ChatClient <host> <port>");
//		Client client = new Client(args[0], Integer.parseInt(args[1]));		
		
		Client client = new Client("localhost",3000);											//1. Erstelle Client

		
		// call user interface here
		new Console(client);
	}

	
	public Client(String host, int port) {														//1.1 In den Constructor
		try {
			System.out.println("Connecting to " + host + " (port " + port + ")...");
			System.out.println("Für Farbe gebe -red- ein!");
			Socket s = new Socket(host, port);													//1.2 Erstelle Socket object mit host und port
			this.write2=new FileWriter("clientHistory.txt"); 	  								// Definition of the chatlog for the client
			
			this.outputStream = new ObjectOutputStream((s.getOutputStream())); 					//1.3 Erstelle Gesendete Daten(obj) // Schnittstelle zum Server
			this.inputStream = new ObjectInputStream((s.getInputStream())); 					//1.4 Erstelle Empfange Daten(obj)	// Schnittstelle zum Server
			
			//System.out.println(this.pwd);
			// Ab hier 
			//this.send(pwd);			
			
			thread = new Thread(this); 															//1.5 Eigenes Objekt wird einem Thread zugeordnet
			thread.start();																		//1.6 Starte den Thread
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * main method. waits for incoming messages. // Hier kann ich die Nachrichten lesen
	 */
	public void run() {																			//1.6.1	thread wir gestartet		
		try {
			Thread thisthread = Thread.currentThread(); 										//1.6.2 Abgleich ob Thread immernoch ist
			while (thread == thisthread) { // Wenn wahr
				try {
					Object msg = inputStream.readObject(); 										//1.6.3 Liest aus dem ObjectInputStream(msg) vom typ obj
					handleIncomingMessage(msg); 												//1.6.4 Verarbeite ? Handler?
				} catch (EOFException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				} ////////////////// Bleibt in der while schleife
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			thread = null;
			try {
				outputStream.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	/**
	 * decides what to do with incoming messages
	 * 
	 * @param msg
	 *            the message (Object) received from the sockets
	 */
	protected void handleIncomingMessage(Object msg) { 											//1.6.4.a Nimm das Object und Entschlüssel die Nachricht
		if (msg instanceof TextMessage) {														// Prüfe ob es vom Typ msg ist
			//fireAddLine(((TextMessage) msg).getContent() + "\n"); // Casting					// Wenn ja, wandle object zu msg object um und
			
			// Decryption
			fireAddLine(hide.decrypt(((TextMessage) msg).getContent())+ "\n");					// Decryption funktioniert noch nicht
		}	
	}

	public void send(String line) {
		send(new TextMessage(line));	
	}
	
	public void send2(String line,String color) {
		send(new TextMessage(line,color));	
	}

	public void send(TextMessage msg) {
		try {
			
			// Objekte für das LogBook ////////////////////////////7
			Calendar cal = Calendar.getInstance();
			String line=hide.decrypt(msg.getContent());
			write2.write(cal.getTime()+":"+alias+":"+line+"\n"); 								// Erstellen eines Loggers für den Client
			write2.flush();
			////////////////////////////////////////////////////////
			
			outputStream.writeObject(msg); // Verarbeitet den Input aus der Console zum Outputstream // Auch hier ein Textfile möglich zu erstellen
			outputStream.flush();
		} catch (IOException ex) {
			ex.printStackTrace();
			this.stop();
		}
	}
	
	/**
	 * listener-list for the observer pattern
	 */
	private ArrayList<ChatLineListener> listeners = new ArrayList<ChatLineListener>();

	/**
	 * addListner method for the observer pattern
	 */
	public void addLineListener(ChatLineListener listener) {
		listeners.add(listener);
	}

	/**
	 * remove Listner method for the observer pattern
	 */
	public void removeLineListener(ChatLineListener listner) {
		listeners.remove(listner);
	}

	/**
	 * fire Listner method for the observer pattern
	 */
	
	public void fireAddLine(String line) {																// 1.6.4b Nehme den String
		for (Iterator<ChatLineListener> iterator = listeners.iterator(); iterator.hasNext();) {			// 
			ChatLineListener listener = (ChatLineListener) iterator.next();
			listener.newChatLine(line); 																// Kommando um alle Nachrichten auch an die Konsole zu schicken
		}
	}

	public void stop() {
		thread = null;
	}
	
	public void aliasName(String alias) { // Avatar name für den Client möglich
		this.alias=alias;
	}
}
