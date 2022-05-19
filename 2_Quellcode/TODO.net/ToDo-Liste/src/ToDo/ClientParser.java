package ToDo;

public class ClientParser {

		
		public  String setStatus(String eventstr, boolean erledigt) {
			String teilstr[];
			teilstr = eventstr.split(",");
			
			if(erledigt == true ) {
				teilstr[3] = "erledigt";
			}
			else {
				teilstr[3] = "offen";
			}
			
			eventstr = teilstr[1]+"     " + teilstr[2]+"   " + teilstr[3];
			
			return eventstr;
		}
		
		public boolean getStatus(String str)
		{
			String teilstr[];
			teilstr = str.split(",");
			
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
			teilstr = str.split(",");
			
			if(teilstr[3].equals("true"))
			{
				return false;
			}
			else
			{
				return true;
			}
			
		}
			
		
		public String getTag(String str) {
			String teilstr[];
			teilstr = str.split(",");
			String teilstr2[];
			teilstr2 = teilstr[2].split("-");
			
			return teilstr2[0];
		}
		public String getMonat(String str) {
			String teilstr[];
			teilstr = str.split(",");
			String teilstr2[];
			teilstr2 = teilstr[2].split("-");
			
			return teilstr2[1];
		}
		public String getJahr(String str) {
			String teilstr[];
			teilstr = str.split(",");
			String teilstr2[];
			teilstr2 = teilstr[2].split("-");
			
			return teilstr2[2];
		}
		
	
	
}
