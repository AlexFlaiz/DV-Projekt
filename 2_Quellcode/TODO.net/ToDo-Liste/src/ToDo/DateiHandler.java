package ToDo;

import java.io.*;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

public class DateiHandler {
	private String dateiname;
	private FileWriter writer;
	private BufferedReader leser;
	private Calendar timeStamp;

	public DateiHandler(String dateiname) {
		this.dateiname = dateiname;
		timeStamp = Calendar.getInstance(); 
	}
	
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
	
	public void write(String data) {
		try {
			
				writer.append(data);
			
		} catch (IOException e) {
			System.out.println("Fehler beim Schreiben!");
		}
	}
	
	public String read() {
		try {
			return leser.readLine();
		} catch (IOException e) {
			return "Fehler";
		}
	}
	
	public void close() {
		try {
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
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
	
	public void writeToLegacy(String eventstr) {
		DateiHandler leg = new DateiHandler(".\\todo\\legacy.dat");
		leg.openDatei(true);
		leg.write(eventstr);
		leg.close();
	}
	
	public void writeErr(String data) {
		write(timeStamp.get(Calendar.HOUR)+ ":"  + timeStamp.get(Calendar.MINUTE)+ "|" + +timeStamp.get( Calendar.DATE)+ "-" +timeStamp.get(Calendar.MONTH)+ "-" + timeStamp.get(Calendar.YEAR) +":   " + data );
	}
	
	
}
