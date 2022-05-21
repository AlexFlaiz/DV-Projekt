/**
 * 
 * @author lukasrumpel markusschlatter
 *
 */
package Server;

/**
 * 
 * Die Klasse Parser wird verwendet um die Eingegeben Variablen fuer die Weiterverarbeitung umzuwandeln. 
 *
 */
public class Parser {

	/**
	 * Setzt den Status einer ToDo auf erledigt oder unerledigt.
	 * 
	 * @param eventstr 	Befehlsstring
	 * @param erledigt	Neuer Wert fuer Erledigt oder Unerledigt
	 * @return			Aktualisierter Befehlsstring
	 */
	public static String setStatus(String eventstr, boolean erledigt) {
		String teilstr[];
		teilstr = eventstr.split("::");
		
		if(erledigt == true ) {
			teilstr[3] = "erledigt";
		}
		else {
			teilstr[3] = "unerledigt";
		}
		
		eventstr = "::"+ teilstr[1] + "::" + teilstr[2]+  "::" + teilstr[3] + "::";
		
		return eventstr;
	}
	
	/**
	 * Wandelt den auf der Serverseite verwendeten String mit :: Trennung in ein Array um das alle Einzelbefehle im Array enthaelt.
	 * 
	 * @param eventstr Uebergabeparameter der den 
	 * @return	Array mit allen Einzelbefehlen
	 */
	public static String[] getAttr(String eventstr) {
		String teilstr[];
		teilstr = eventstr.split("::");
		return teilstr;
	}
	
	/**
	 * Wandelt das Darum in ein Array um und speichert Tag, Monat und Jahr seperat im Array.
	 * 
	 * @param eventstr 	Befehlsstring
	 * @return			Array mit Datum
	 */
	public static int[] getDate(String eventstr) {
		int daten[] = new int[3];
		String teilstrings[] = new String[5];
		String datum[];
		teilstrings = getAttr(eventstr);
		datum = teilstrings[2].split("-");
		daten[0] = Integer.parseInt(datum[0]);
		daten[1] = Integer.parseInt(datum[1]);
		daten[2] = Integer.parseInt(datum[2]);
		return daten;
	}
	
	/**
	 * Ist Todo erledigt oder nicht erledigt?
	 * 
	 * @param eventstr	Befehlsstring
	 * @return			Rueckgabe true oder false fuer erledigt oder nicht erledigt.
	 */
	public static boolean getStatus(String eventstr) {
		String teilstr[] = eventstr.split("::");
		if(teilstr[3].equals("true")) {
			return true;
		}
		else {
			return false;
		}
	}
	
	/**
	 * Gibt nur den Namen zurueck
	 * 
	 * 
	 * @param eventstr 	Befehlsstring
	 * @return			
	 */
	public static String getName(String eventstr) {
		String teilstr[] = eventstr.split("::");
		return teilstr[1];
	}
	
	/**
	 * Aendert das Datum ab und gibt einen Befehlsstring mit aktualisiertem Datum zurueck.
	 * 
	 * @param eventstr	Befehlsstring mit altem Datum
	 * @param tag		Int fuer Tag
	 * @param monat		Int fuer Monat
	 * @param jahr		Int fuer Jahr
	 * @return			Befehlsstring mit aktualisiertem Datum
	 */
	public static String setDate(String eventstr, int tag, int monat, int jahr) {
		String teilstr[];
		teilstr = eventstr.split("::");
		String datum = String.format("%02d", tag)+ "-" + String.format("%02d", monat) + "-" + String.format("%02d", jahr);
		teilstr[2] = datum;
		eventstr = "::"+ teilstr[1] + "::" + teilstr[2]+  "::" + teilstr[3] + "::";
		return eventstr;
	}
	
	/**
	 * Aendert den Namen der ToDo
	 * 
	 * @param eventstr 	Befehlsstring
	 * @param newname	String mit neuem Namen
	 * @return			Befehlsstring mit Aktualisiertem Namen
	 */
	public static String setName(String eventstr, String newname) {
		String teilstr[];
		teilstr = eventstr.split("::");
		teilstr[1] = newname;
		eventstr = "::"+ teilstr[1] + "::" + teilstr[2]+  "::" + teilstr[3] + "::";
		return eventstr;
	}
	
	/**
	 * Welche Aktion ist vom GUI gefordert im Befehlsstring. gibt fuer jede moegliche Aktion eine Int zurueck die in ServThread ausgewertet wird.
	 * 
	 * @param command 	Befehlsstring
	 * @return			Int mit Wert fuer Aktion
	 */
	public static int parseCMD(String command) {
		int cmd = 0;
			String teilStr[] = command.split("//");
			if(teilStr[1].equals("UPDATE")){
				cmd = 1;
			}
			else if(teilStr[1].equals("INSERT")) {
				cmd = 2;
			}
			else if(teilStr[1].equals("MODIFY")) {
				cmd = 3;
			}
			else if(teilStr[1].equals("DELETE")) {
				cmd = 4;
			}
			else if(teilStr[1].equals("ENDCON")){
				cmd = 5;
			}
			else if(teilStr[1].equals("GETADMIN")) {
				cmd = 6;
			}
		return cmd;
	}
	
	/**
	 * Wandelt den auf GUI seite verwerndeten String mit // trennung in den auf Serverseite verwendeten String mit :: Trennung um.
	 * 
	 * @param command	Commandstring GUI
	 * @return			Befehlsstring Server
	 */
	public static String getNewInsert(String command) {
		String teilStr[];
		teilStr = command.split("//");
		String eventStr= "::"+ teilStr[2] + "::" + teilStr[3]+  "::" + teilStr[4] + "::" + teilStr[5] + "::";
		return eventStr;
	}
	
	/**
	 * Welche ID hat die gesuchte ToDo?
	 * 
	 * @param command	Commandstring
	 * @return			Integer mit ID
	 */
	public static int getID(String command) {
		String teilStr[];
		teilStr = command.split("//");
		String id = teilStr[2];
		return Integer.parseInt(id);
	}
	
	/**
	 * Wandelt den Commandstring von GUI so um das er ohne ID und ohne Aktionswert verwendet werde kann.
	 * 
	 * @param command 	Commandstring
	 * @return			Befehlsstring ohne Aktionswert und ohne ID
	 */
	public static String getModifyStr(String command) {
		String teilStr[];
		teilStr = command.split("//");
		String eventStr= "::"+ teilStr[3] + "::" + teilStr[4]+  "::" + teilStr[5] + "::" + teilStr[6] + "::";
		System.out.println(eventStr);
		return eventStr;
	}
	
	/**
	 * Gibt zurueck ob AdminToDo oder nicht
	 * 
	 * @param command	Commandstring
	 * @return			True oder False fuer Admin oder Benutzer
	 */
	public static boolean getPriv(String command) {
		String teilStr[];
		teilStr = command.split("//");
		String priv = teilStr[4];
		if(priv.equals("true")) {
			return true;
		}
		else {
			return false;
		}
	}
}
