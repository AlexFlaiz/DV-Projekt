/**
 *
 * @author alex flaiz, jan bechtel
 * @version 1.0.0
 *
 */
package ToDo;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TODOs {
	/**
	 * Kontrolliert ob Datum im richtigen Format eingegeben ist und kontrolliert, ob es ein gueltiges Datum ist.
	 *
	 * @param Datum kontrolliert diesen String
	 * @return true or false, ob richtiges Eingabeformat benutzt wurde
	 */
	public boolean statDatum(String Datum)
	{
		String teilstr[];
		teilstr = Datum.split("-");
		int Jahr= Integer.parseInt(teilstr[2]);
		if(Jahr<1900||Jahr>2200)
		{
			return false;
		}
		else {
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
	}

	/**
	 * gibt Datum des uebergebenen Strings in richtigem String-Format wieder, damit es richtig an den Server gesendet wird.
	 *
	 * @param Datum dieser String wird richtig formatiert
	 * @return Datum im richtigen format
	 */
	public String getDate(String Datum)
	{
		String Tag,Monat,Jahr;
		String teilstr[];
		teilstr = Datum.split("-");

		if (Integer.parseInt(teilstr[0])<10)
		{
			Tag = "0"+Integer.parseInt(teilstr[0]);
		}
		else
		{
			Tag=teilstr[0];
		}

		if (Integer.parseInt(teilstr[1])<10)
		{
			Monat = "0"+Integer.parseInt(teilstr[1]);
		}
		else
		{
			Monat=teilstr[1];
		}

		Jahr=teilstr[2];

		return Tag+"-"+Monat+"-"+Jahr;
	}

}