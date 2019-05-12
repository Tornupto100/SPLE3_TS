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
	private BufferedReader terminal; // Container der Input Character gef�llt
	boolean accepted=false;
	
	public Authorize(ServerSocket server, Socket client) {
		this.server=server; // �bergabe des Servers
		this.client=client; // �bergabe des Clients zum Pr�fen
		
		this.terminal = new BufferedReader(new InputStreamReader(System.in));  // Input & Read Input aus Tastatur eingabe
																				// Abgleich mit den Dem PWD
		thread = new Thread(this); // �bergabe Console an Thread / Er�ffnung neuen Thread
		thread.start();		// run Thread*			
		
	}
	
	
	
	
	

	@Override
	public void run() { // In diesem Thread muss jetzt das PWD abgefragt werden
		// TODO Auto-generated method stub
		
				
		
		
		
		
		

		
		
	}
	
	
}
