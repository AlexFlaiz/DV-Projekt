/**
 * 
 * @author lukasrumpel markusschlatter
 *
 */
package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.Thread;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;


/**
 * In dieser Klasse wird der von der GUI erstellte StringBefehl im Server verarbeitet und die dazugehörige Funktion gestartet.
 */
public class ServThread implements Runnable{
	 protected java.net.Socket clientsocket = null;
	 protected String serverText   = null;
	 private BufferedReader reader;
	 private PrintWriter printWriter;
	 private DateiHandler fehler;
	 private  DataBaseHandler dbh ;
	 private boolean adminLoggedIn;
	 
	 /**
	  * Konstruktor für die Klasse ServThread. 
	  * Wird von ServSock gestartet wenn Verbindung erfolgreich.
	  * 
	  * @param clientSocket 	
	  * @param dbh				Vom Server definierter DataBaseHandler
	  * @param admin			Ist der User als Admin eingeloggt? 
	  * 
	  */
	    public ServThread(java.net.Socket clientSocket, DataBaseHandler dbh, boolean admin) {
	    	this.dbh = dbh;
	        this.clientsocket = clientSocket;
	        this.adminLoggedIn = admin;
	        System.out.println("Admin: " + admin);
	        fehler = new DateiHandler("THREADerrLog.txt");
	        try {
				reader = new BufferedReader(new InputStreamReader(clientsocket.getInputStream()));
				printWriter = new PrintWriter(new OutputStreamWriter(clientsocket.getOutputStream()));
				
			} catch (IOException e) {
				
				fehler.openDatei(true);
				fehler.writeErr(e.getMessage()+ "\n");
				fehler.close();
			}
	    }
	
	/**
	 *     Startet für jeden User einen neuen Thread im Server.
	 *     Variable str wird über getStr der Befehlsstring zugewiesen der vom GUI an den Server gesendet wird. 
	 *     gibt die Variable an putStr weiter als überprüfungsfunktion.
	 *	   gibt die Variable an procCMD weiter 
	 *     
	 */
	public void run() {
		while(true) {
			try {
				String str = getStr();
				putStr(str);
				System.out.println(Thread.currentThread().getName() + " String: " + str);
				procCMD(str);
			} catch (IOException e) {
				fehler.openDatei(true);
				fehler.writeErr(e.getMessage()+ "\n");
				fehler.close();
			}
		}
	}
	
	/**
	 * liest eine Zeile des InputStream und gibt diesen als String zurück. Eine Zeile ist ein Server Befehl.
	 * 
	 * @return str		Befehlsstring 
	 * @throws IOException
	 */
	public String getStr() throws IOException {
		String str = reader.readLine();
		return str;
	}
	
	/**
	 * SendetStringToSend an GUI
	 * 
	 * @param stringToSend	String der an GUI gesendet wird. 
	 * @throws IOException
	 */
	public void putStr( String stringToSend) throws IOException {
		printWriter.print(stringToSend);
		printWriter.flush();
	}
	
	/**
	 * Startet Parser.parseCMD
	 * Über Parser.parseCMD wird der erste Teil des Befehlsstrings ausgelesen. 
	 * Rückgabe wert ist eine Integer zwischen 1 bis 5.
	 * Je nach rückgabewert wird die dazugehörige Methode gestartet. 
	 * 
	 * @param cmd Befehlsstring von run
	 */
	public  void procCMD(String cmd) {
		switch(Parser.parseCMD(cmd)) {
		case 1:
			doUpdate2DB(); // /UPDATE/
			break;
		case 2:
			newEvent2DB(cmd); // /INSERT/Bezeichner/tt-mm-jjjj/state
			break;
		case 3:
			modifiyEvent2DB(cmd); // /MODIFY/id/Bzeichner/tt-mm-jjjj/state
			break;
		case 4:
			deleteEntry2DB(cmd); // /DELETE/id
			break;
		case 5:
			try {
				clientsocket.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				fehler.openDatei(true);
				fehler.writeErr(e1.getMessage()+ "\n");
				fehler.close();
			}
			Thread.currentThread().stop();
			break;
		default:
			break;
		}
		
		try {
			putStr("/END/");
		} catch (IOException e) {
			fehler.openDatei(true);
			fehler.writeErr(e.getMessage()+ "\n");
			fehler.close();
		}
	}	
	
	
	
	/**
	 * Wird von procCMD gestartet. 
	 * Der User hat über Befehlsstring ein Update gefordert. 
	 * Bekommt von DataBaseHandler Die aktuellen Daten übergeben und Baut den String der an das GUI geschickt wird.
	 * 
	 */
	public void doUpdate2DB() {
		int i = 1;
		String buffer[] = new String[4];
		try {
			buffer = dbh.getEntry(i);
			while(!(buffer[0] == null)) {
				i++;
				putStr("," +buffer[0]+ "," + buffer[1] + "," + buffer[2]+ "," + buffer[3] + "," + "\n");
				buffer = dbh.getEntry(i);
			}
			
			
		} catch (IOException e) {
			fehler.openDatei(true);
			fehler.writeErr(e.getMessage()+ "\n");
			fehler.close();
		}
	}
	
	/**
	 * Methode zur erstellung eines neuen ToDos
	 * Der Befehlsstring wird von getNewInsert in die auf Serverseite verwendete schreibweise auf Trennung mit Komma Umgewandelt.
	 * Der Befehlsstring wird von getAttr aufgeteilt und in Einzeltrings gespeichert. 
	 * Wenn ein Addmin eingeloggt ist kann eine AdminToDO oder BenutzerToDo erstellt werden sonst nur Benutzer ToDo.
	 * 
	 * @param cmd	Befehlsstring 
	 */
	public void newEvent2DB(String cmd){
		String buffer = Parser.getNewInsert(cmd);
		String attr[] = Parser.getAttr(buffer);
		String bezeichner = attr[1];
		String date = attr[2];
		String state = attr[3];
		String prot = attr[4];
		int id = dbh.getNumRows()+1;
		if(!this.adminLoggedIn) {
			dbh.insertEntry(id, bezeichner, date, state, "false");
		}
		else {
			dbh.insertEntry(id, bezeichner, date, state, prot);
		}
	}
	
	/**
	 * Methode zur veränderung einer ToDo 
	 * Von getID wird die ID der zu ändernden ToDo ermittelt. 
	 * Danach wird auf dem Platz der Vorheringen ToDO die geänderte ToDo gespeichert und die alte Todo überschrieben.
	 * 
	 * @param cmd	Befehlsstring von run
	 */
	public void modifiyEvent2DB(String cmd) {
		int id = Parser.getID(cmd);
		String buffer = Parser.getModifyStr(cmd);
		String attr[] = Parser.getAttr(buffer);
		String bezeichner = attr[1];
		String date = attr[2];
		String state = attr[3];
		String prot = attr[4];
		
		
		if( this.adminLoggedIn) {
			dbh.modifyEntry(id, bezeichner, date, state, prot);
		}
		else if(!this.adminLoggedIn && !dbh.getPriv(id)) {
			dbh.modifyEntry(id, bezeichner, date, state, "false");
		}
	}
	
	/**
	 * Methode zur löschung eines ToDos.
	 * Admins können alle ToDos löschen.
	 * Bei Benutzern wird überprüft ob ToDo eine BenutzerToDo ist.
	 * 
	 * @param cmd 	Befehlsstring von run
	 */
	public void deleteEntry2DB(String cmd) {
		int id = Parser.getID(cmd);
		if(this.adminLoggedIn) {
		dbh.deleteEntry(id);
		dbh.udpdateIDs();
		}
		else if(!dbh.getPriv(id)){
			dbh.deleteEntry(id);
			dbh.udpdateIDs();
		}
	}
	
}
