package ToDo;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TODOs {
	
	
	public boolean statDatum(String Datum)
	{
		
	        DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
	        df.setLenient( false );
	        try {
	          @SuppressWarnings("unused")
			Date d1 = df.parse(Datum);
	          return true;
	        } catch ( ParseException e ) {
	            // nichts wenn falsch!
	        }
	        
	        return false;
	   }

	public String getDate(String Datum)
	{
		String Tag,Monat,Jahr;
		String teilstr[];
		teilstr = Datum.split("-");
		
		if (Integer.parseInt(teilstr[0])<10)
		{
		  Tag = "0"+teilstr[0];
		}	
		else
		{
			Tag=teilstr[0];
		}
		
		if (Integer.parseInt(teilstr[1])<10)
		{
		  Monat = "0"+teilstr[1];
		}	
		else
		{
			Monat=teilstr[1];
		}
		
		Jahr=teilstr[2];
		
		return Tag+"-"+Monat+"-"+Jahr;
	}
			
		
	
	
}
