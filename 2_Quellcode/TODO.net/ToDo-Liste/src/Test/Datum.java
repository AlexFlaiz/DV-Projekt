package Test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Datum {
	

	String	s="12.06.2022";
	
	
	
	public boolean checkID ( String s ) 
		{
	        DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
	        df.setLenient( false );
	        try {
	          Date d1 = df.parse(s);
	          return true;
	        } catch ( ParseException e ) {
	            // nichts wenn falsch!
	        }
	        
	        return false;
	    }
}

