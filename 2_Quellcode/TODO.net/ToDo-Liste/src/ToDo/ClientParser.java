/**
 *
 * @author alex flaiz, jan bechtel
 * @version 1.0.0
 *
 */
package ToDo;

public class ClientParser {
    /**
     * Verarbeitet die vom Server kommenden Nachrichten in eine uebersichtlichere Ausgabe in der GUI
     *
     * @param eventstr gibt den String aus der in der GUI ausgegeben wird
     * @param erledigt boolean beschreibt ob erleidigt oder nicht
     * @return Rueckgabe beinhaltet Datum, Name und Erledigungsstatus (standardisierte Form)
     */
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
        inputtext = inputtext.replace("Ae","Ä");
        inputtext = inputtext.replace("Oe","Ö");
        inputtext = inputtext.replace("Ue","Ü");
        inputtext = inputtext.replace("*~","/");
        inputtext = inputtext.replace("$~",":");
        String Eintrag=inputtext;

        eventstr = Datum[0]+"."+Datum[1]+"."+Datum[2]+"   " + teilstr[3]+"     " + Eintrag;

        return eventstr;
    }

    /**
     * Status-Abfrage
     *
     * @param str aktueller Status
     * @return Rueckgabe ob erledigt oder offen
     */
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

    /**
     * veraenderung des Status
     *
     * @param str aktueller Status-String
     * @return gibt den jeweils anderen Status aus
     */
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

    /**
     * Abfrage des Datums
     *
     * @param str aktueller Status-String
     * @return Datum aus dem String
     */
    public String getDatum(String str) {
        String teilstr[];
        teilstr = str.split("::");

        return teilstr[2];
    }

    /**
     * Abfrage ob der Admin uebereinstimmt
     *
     * @param str aktueller Status-String
     * @return true wenn admin uebereinstimmt
     */
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