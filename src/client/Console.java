package client;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Calendar;

import common.Change;
import common.Normal;
import common.ROT13;

public class Console implements ChatLineListener,Runnable {
	
	protected Thread thread;
	private Client chatClient;
	boolean color=false;
	
	private BufferedReader terminal; 												// Container der Input Character gefüllt

	public Console(Client chatClient) {
		
		// register listener so that we are informed whenever a new chat message
		// is received (observer pattern)
		chatClient.addLineListener(this); 											// Übergabe der Console an den LineListener
		this.chatClient = chatClient; 												// Client wird zugewiesen
		
		// I/O from console
		this.terminal = new BufferedReader(new InputStreamReader(System.in));  		// (Sammeln der Zeichen(Zuordnung eines ASCI Zeichen(Eingabe Tastatur))))

		thread = new Thread(this); 													// Übergabe Console an Thread / Eröffnung neuen Thread
		thread.start();		// run Thread*
	}
	@Override
	public void run() { 	// run Thread*
		
		try { 																		//a) Farbe über Konsole einlesen und freischalten
			Thread thisthread = Thread.currentThread();
			while (thread == thisthread) { // Immer wahr
				try {
					String msg = terminal.readLine(); 								// Warte auf enter und lese die Zeile (String), danach an msg
					if (msg != null || msg != "") {  								// Prüfe ob eine msg leer ist oder nur permanente eingabe
						if (msg.equals("red")) {
							this.color=true;
							System.out.println("Um die Farbe zu entfernen gebe -redOff- ein!");
						}
						if (msg.equals("redOff")) {
							this.color=false;
						}
						if (msg != null && color==true) {
							// Encryption method									// c) Verschlüsseln der Eingabe 
							msg=chatClient.hide.encrypt(msg);
							chatClient.send2(msg,"red");
						}
						if (msg != null && color!=true) {
							msg=chatClient.hide.encrypt(msg);									// c) Verschlüsseln der Eingabe 
							chatClient.send(msg);
						}
					}
						// Hier den String ändern
						//chatClient.send(msg); // Aufruf der send methode vom client + Übergabe des String
				} catch (EOFException e) {
					e.printStackTrace();
				}
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			thread = null;
		}	
	}

	Calendar cal = Calendar.getInstance();
	@Override
	public void newChatLine(String line) {
		System.out.print(cal.getTime()+":"+line);
		
	}
}