package Server;

import java.io.*;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

/**
 * Klasse zum Erstellen von Objekten zu Dateizugriffen
 * @author Lukas Rumpel
 * @author Tobias Ohnmacht
 * @version 1.0
 */
public class DateiHandler {
	
	/**
	 * Name der Datei, welche geoeffnet wird 
	 */
	private String dateiname;
	
	/**
	 * FileWrite fuer zu schreibende Daten 
	 */
	private FileWriter writer;
	
	/**
	 * Buffererd Reader fuer TCP/IP Verbindung
	 */
	private BufferedReader leser;
	
	/**
	 * Zeitstempel fuer Fehler Logbuch
	 */
	private Calendar timeStamp;

	
	/**
	 * Konstruktor, initialisiert Dateizugriff, wie Zeitstempel
	 * @param dateiname Name der Datei
	 */
	public DateiHandler(String dateiname) {
		this.dateiname = dateiname;
		timeStamp = Calendar.getInstance(); 
	}
	
	/**
	 * Oeffnet Datei, wenn write=true zum Schreibzugriff ansonsten als Lesezugriff  
	 * @param write Parameter ob schreiben oder lesen
	 */
	public void openDatei(boolean write){
		if (write== true) {
		try {
			
				writer = new FileWriter(this.dateiname,true);
			
		}
		catch(IOException e) {
			System.out.println("Fehler beim Oeffnen!");
		}
		}
		else {
			try {
				leser = new BufferedReader(new FileReader(this.dateiname));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * haengt Zeilenweise Strings an der Datei an
	 * @param data der String der geschrieben wird
	 */
	public void write(String data) {
		try {
			
				writer.append(data);
			
		} catch (IOException e) {
			System.out.println("Fehler beim Schreiben!");
		}
	}
	
	/**
	 * Liest den String Zeilenweise aus der Datei aus
	 * @return Den String aus der Datei
	 */
	public String read() {
		try {
			return leser.readLine();
		} catch (IOException e) {
			return "Fehler";
		}
	}
	
	/**
	 * Schliesst die Datei
	 */
	public void close() {
		try {
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Leert die Datei
	 */
	public void delete() {
		try {
			writer = new FileWriter(dateiname);
			writer.write("");
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		
	}
	
	/**
	 * Erstellt String Arrays fuer Aufgabenelemente
	 * @param elements die Aufgaben
	 * @return str den String der Elemente
	 */
	public String[] getStrings(int elements) {
		String str[] = new String[elements];
		int i = 0;
		boolean loop = true;
		while(loop) {
		str[i] = this.read();
		if(str[i] == null) {
			loop = false;
		}
		i++;
		}
		return str;
	}
	
	/**
	 * Erstellt die Aufgaben mit ihrem Erledigungsstatus
	 * @param str Array von String der Elemente 
	 * @param write Array Erledigt nicht erledigt
	 */
	public  void writeNewStrings(String [] str, boolean write[]) {
		int b = 0;
		while(str[b] != null) {
		if(write[b] == false) {	
		this.write(str[b] + '\n');
		}
		else {
			writeToLegacy(str[b] + '\n');
		}
		b++;
		}
	}
	
	/**
	 * Verarbeitet den Eventstring der Aufgabe
	 * @param eventstr String mit Aufgabe 
	 */
	public void writeToLegacy(String eventstr) {
		DateiHandler leg = new DateiHandler(".\\todo\\legacy.dat");
		leg.openDatei(true);
		leg.write(eventstr);
		leg.close();
	}
	
	/**
	 * schreibt formatierten Fehler in Fehler Log-Datei
	 * @param data Fehler Datei
	 */
	public void writeErr(String data) {
		write(timeStamp.get(Calendar.HOUR)+ ":"  + timeStamp.get(Calendar.MINUTE)+ "|" + +timeStamp.get( Calendar.DATE)+ "-" +timeStamp.get(Calendar.MONTH)+ "-" + timeStamp.get(Calendar.YEAR) +":   " + data );
	}
	
	
}
