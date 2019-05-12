package server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Authorize implements Runnable {

	//Attributes
	private ServerSocket server; 
	private Socket client;
	protected Thread thread;
	private BufferedReader terminal; // Container der Input Character gefüllt
	boolean accepted=false;
	
	public Authorize(ServerSocket server, Socket client) {
		this.server=server; // Übergabe des Servers
		this.client=client; // Übergabe des Clients zum Prüfen
		
		this.terminal = new BufferedReader(new InputStreamReader(System.in));  // Input & Read Input aus Tastatur eingabe
																				// Abgleich mit den Dem PWD
		thread = new Thread(this); // Übergabe Console an Thread / Eröffnung neuen Thread
		thread.start();		// run Thread*			
		
	}
	
	
	
	
	

	@Override
	public void run() { // In diesem Thread muss jetzt das PWD abgefragt werden
		// TODO Auto-generated method stub
		
				
		
		
		
		
		

		
		
	}
	
	
}
