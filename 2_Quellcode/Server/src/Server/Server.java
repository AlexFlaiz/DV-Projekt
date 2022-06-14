package Server;

import java.io.IOException;
import java.lang.Thread;
import java.util.*;


/**
 * Diese Klasse ist fuer den Start des Servers verantwortlich

 * @author Lukas Rumpel
 * @author Tobias Ohnmacht
 * @version 1.0
 */
public class Server {
	
	private static int port; 
	private static DateiHandler Port;
	
	
	/**
	 * Dies ist die Main Methode sie Startet notrmalMode()
	 * @param args Array von Zeichenketten
	 */
	public static void main(String[] args) {
		normalMode();
	}
	
	/**
	 * Erzeugt das Objekt DataBaseHandler und den ServerSocket fuer TCP/IP-Verbindung
	 */
	public static void normalMode() {
		DataBaseHandler dbh;
		dbh = new DataBaseHandler("jdbc:sqlite:TODO.db");
		String PortDatei = "Port.txt";
		Port= new DateiHandler(PortDatei);
		Port.openDatei(false);
		port=Integer.parseInt(Port.read());
		ServSock server = new ServSock(port, dbh);
		System.out.println("Server gestartet!");
		while(true) {
			server.login();
		}
	}

}
