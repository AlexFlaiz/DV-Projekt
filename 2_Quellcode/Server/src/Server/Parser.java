package Server;


public class Parser {

	
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
	
	public static String[] getAttr(String eventstr) {
		String teilstr[];
		teilstr = eventstr.split("::");
		return teilstr;
	}
	
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
	
	public static boolean getStatus(String eventstr) {
		String teilstr[] = eventstr.split("::");
		if(teilstr[3].equals("true")) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public static String getName(String eventstr) {
		String teilstr[] = eventstr.split("::");
		return teilstr[1];
	}
	
	public static String setDate(String eventstr, int tag, int monat, int jahr) {
		String teilstr[];
		teilstr = eventstr.split("::");
		String datum = String.format("%02d", tag)+ "-" + String.format("%02d", monat) + "-" + String.format("%02d", jahr);
		teilstr[2] = datum;
		eventstr = "::"+ teilstr[1] + "::" + teilstr[2]+  "::" + teilstr[3] + "::";
		return eventstr;
	}
	
	public static String setName(String eventstr, String newname) {
		String teilstr[];
		teilstr = eventstr.split("::");
		teilstr[1] = newname;
		eventstr = "::"+ teilstr[1] + "::" + teilstr[2]+  "::" + teilstr[3] + "::";
		return eventstr;
	}
	
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
	
	public static String getNewInsert(String command) {
		String teilStr[];
		teilStr = command.split("//");
		String eventStr= "::"+ teilStr[2] + "::" + teilStr[3]+  "::" + teilStr[4] + "::" + teilStr[5] + "::";
		return eventStr;
	}
	
	public static int getID(String command) {
		String teilStr[];
		teilStr = command.split("//");
		String id = teilStr[2];
		return Integer.parseInt(id);
	}
	
	public static String getModifyStr(String command) {
		String teilStr[];
		teilStr = command.split("//");
		String eventStr= "::"+ teilStr[3] + "::" + teilStr[4]+  "::" + teilStr[5] + "::" + teilStr[6] + "::";
		System.out.println(eventStr);
		return eventStr;
	}
	
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
