package ToDo;

public class ClientParser {

		
		public  String setStatus(String eventstr, boolean erledigt) {
			String teilstr[];
			teilstr = eventstr.split("::");
			
			if(erledigt == true ) {
				teilstr[3] = "erledigt";
			}
			else {
				teilstr[3] = "offen    ";
			}
			
			String Datum[];
			Datum = teilstr[2].split("-");
			
			String inputtext = teilstr[1];     						 
		    inputtext = inputtext.replace("ae","ä");
		    inputtext = inputtext.replace("oe","ö");
		    inputtext = inputtext.replace("ue","ü");
			String Eintrag=inputtext;								
			
			eventstr = Datum[0]+"."+Datum[1]+"."+Datum[2]+"   " + teilstr[3]+"     " + Eintrag;
			
			return eventstr;
		}
		
		public boolean getStatus(String str)
		{
			String teilstr[];
			teilstr = str.split("::");
			
			if(teilstr[3].equals("true"))
			{
				return true;
			}
			else {
				return false;
			}
		}
		public boolean getNewStatus(String str)
		{
			String teilstr[];
			teilstr = str.split("::");
			
			if(teilstr[3].equals("true"))
			{
				return false;
			}
			else
			{
				return true;
			}
			
		}
			
		
		public String getDatum(String str) {
			String teilstr[];
			teilstr = str.split("::");
			
			return teilstr[2];
		}
	
		public boolean getAdmin(String str)
		{
			String teilstr[];
			teilstr = str.split("::");
			
			if(teilstr[4].equals("true"))
			{
				return true;
			}
			else {
				return false;
			}
		}
	
}
