package Server;

import java.io.IOException;
import java.lang.Thread;
import java.util.*;


/**
 * Diese Klasse ist für den Start des Servers verantwortlich
 * @author Lukas Rumpel
 * @author Tobias Ohnmacht
 * @version 1.0
 */
public class Server {
	
	
	/**
	 * Dies ist die Main Methode sie Startet notrmalMode()
	 * @param args Array von Zeichenketten
	 */
	public static void main(String[] args) {
		normalMode();
	}
	
	/**
	 * Erzeugt das Objekt DataBaseHandler und den ServerSocket für TCP/IP-Verbindung
	 */
	public static void normalMode() {
		DataBaseHandler dbh;
		dbh = new DataBaseHandler("jdbc:sqlite:TODO.db");
		ServSock server = new ServSock(1112, dbh);
		System.out.println("Server gestartet!");
		while(true) {
			server.login();
		}
	}

}
