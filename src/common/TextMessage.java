package common;

import java.io.Serializable;

/**
 * serializable message that can be send over the sockets between client and
 * server. 
 */
public class TextMessage implements Serializable {

	private static final long serialVersionUID = -9161595018411902079L;
	private String content;
	//private String color;
	// Möglicher container für Farben
	

	public TextMessage(String content) {
		super();
		this.content = content;
	}
	
	public TextMessage(String content, String color) { 							//a) Wrappen der Nachricht mit Farbe bisher nur rot
		super();
		this.content= color+">"+content+"<"+color;
	}
	
	public String getContent() {
		return content;
	}
	
	

}
