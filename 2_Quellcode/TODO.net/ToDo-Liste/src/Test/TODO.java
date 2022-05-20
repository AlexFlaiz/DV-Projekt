package Test;

import java.util.ArrayList;

public class TODO {
	
	String Tag;
	String Monat;
	String Jahr;
	String Eintrag;
	boolean Status;
	ArrayList<String> ToDo;
	
	public TODO(String Tag, String Monat, String Jahr , String Eintrag, boolean Status)
	{
	this.Tag=Tag;
	this.Monat=Monat;
	this.Jahr=Jahr;
	this.Eintrag=Eintrag;
	Status=false;
		
	}
	
	public String getEintrag()
	{
		return Tag+"."+Monat+"."+Jahr+"     "+Eintrag;
	}

	public boolean aendereStatus(String e)
	{
		if (e=="erledigt")
		return Status =true;
		else
		return Status=false;
	}
	
	
}
