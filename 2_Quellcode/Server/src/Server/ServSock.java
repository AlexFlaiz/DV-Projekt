package Server;

import java.util.*;
import java.io.*;

/**
 * Diese Klasse ist die Schnittstelle zur Anmeldung und Kommunikation mit Client
 * @author Lukas Rumpel
 * @author Tobias Ohnmacht
 * @version 1.0
 */
public class ServSock {
	
	/**
	 * Identifikationsschluessel normaler Nutzer
	 */
	private String id;
	
	/**
	 * Identifikatinsschluessel Admin
	 */
	private String adminID;
	
	/**
	 * Adresse zur Kommunikatin zwischen Server und Client
	 */
	private int port;
	
	/**
	 * TCP/IP-Verbindung Client
	 */
	private java.net.Socket client;
	
	/**
	 * TCP/IP-Verbindung Server 
	 */
	private java.net.ServerSocket server;
	
	/**
	 *BufferedReader fuer TCP/IT-Verbindung
	 */
	private BufferedReader reader;
	
	/**
	 * erstellt Ausgabedaten in allgemein lesbaren Textform
	 */
	private PrintWriter printWriter;
	
	/**
	 * Nummer des Threads
	 */
	private int thraedCount = 0;
	
	/**
	 * Name des Threads
	 */
	private String nameOfThread;
	
	/**
	 * DateiHandler der Fehler in Datei schreibt
	 */
	private DateiHandler fehler;
	
	/**
	 * DataBaseHandler 
	 */
	private DataBaseHandler dbh ;
	
	/**
	 * Schluessel fuer die Anmeldung als normaler Nutzer
	 */
	private DateiHandler authGet;
	
	/**
	 * Schluessel fuer die Anmeldung als Admin
	 */
	private DateiHandler adminGet;
	
	/**
	 * ist Admin- oder Normaler- Zugriff
	 */
	private boolean adminLoggedIn = false;
	
	
	/**
	 * Konstruktor zur erstellung der Anmeldschluessel
	 * @param port den Port der Verbindung 
	 * @param dbh DataBaseHandler 
	 */
	public ServSock(int port, DataBaseHandler dbh) {
		this.port = port;
		fehler = new DateiHandler("SERVSOCKerrLog.txt");
		this.authGet = new DateiHandler("AuthKey.txt");
		this.adminGet = new DateiHandler("AdminAuthKey.txt");
		this.dbh = dbh;
		authGet.openDatei(false);
		id = authGet.read();
		adminGet.openDatei(false);
		adminID = adminGet.read();
	}
	
	
	/**
	 * Stellt Verbindung mit dem Client her
	 * @return Verbindungsstatus und ob Admin eingelockt ist oder nicht
	 * @throws IOException
	 */
	public boolean startConnection() throws IOException {
		
		server = new java.net.ServerSocket(port);
		server.setReuseAddress(true);
		java.net.Socket client =  clientConnect(server);
		reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
		printWriter = new PrintWriter(new OutputStreamWriter(client.getOutputStream()));
		String verifyStr = getStr();
		
		
		if(verifyStr.equals(id)) {
			this.adminLoggedIn = false;
			return true;
		}
		else if(verifyStr.equals(adminID)) {
			this.adminLoggedIn = true;
			return true;
		}
		else {
			server.close();
			return false;
		}
	}
	
	
	/**
	 * Die Aufgebaute Verbindung wird an den ClientSocket weitergereicht
	 * @param server verbindung mit Server 
	 * @return Client mit dem Verbindung eingegangen wurde 
	 * @throws IOException
	 */
	public java.net.Socket clientConnect(java.net.ServerSocket server) throws IOException{
		client = server.accept();
		
		return client;
	}
	
	/**
	 * List Zeilenweise Stings aus dem TCP/IP-Socket
	 * @return gibt den String der Aufgaben zurueck
	 * @throws IOException
	 */
	public String getStr() throws IOException {
		//BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		char buffer[] = new char[200];
		int numChar = reader.read(buffer);
		String str = new String(buffer, 0, numChar);
		
		return str;
	}
	
	/**
	 * Schreibt Strings Zeilenweise in eine Textdatei
	 * @param stringToSend ist der String der Textdatei
	 * @throws IOException
	 */
	public void putStr( String stringToSend) throws IOException {
		//printWriter = new PrintWriter(new OutputStreamWriter(client.getOutputStream()));
		printWriter.print(stringToSend);
		printWriter.flush();
		
	}
	
	/**
	 * Schliesst den ServerSocket der TCP/IP-Verbindung
	 * @throws IOException
	 */
	public void close() throws IOException {
		server.close();
	}
	
	/**
	 * hier wird geprueft ob eine Verbindung mit dem Client eingegangen werden kann, anhand der Authentifikationsschluessel
	 * @return Ist Benutzer eingeloggt oder nicht
	 */
	public boolean login() {
		boolean connAccept;
			try {
				connAccept = this.startConnection();
				if (connAccept == true) {
					this.putStr("ok!");
					System.out.println("Verbindung mit Client akzeptiert!" + " auf Port: " + this.port);
					this.close();
					this.nameOfThread = String.format("Con-%d", this.thraedCount);
					new Thread(new ServThread(client, dbh, adminLoggedIn), this.nameOfThread).start();
					this.thraedCount++;
					return true;
				}
				else {
					this.putStr("denied!");
					System.out.println("Verbindung mit Client abgelehnt!");
					this.close();
					return false;
				}
			} catch (IOException e) {
				fehler.openDatei(true);
				fehler.writeErr(e.getMessage() + "\n");
				fehler.close();
				return false;
			}
		
	}
	
}
