package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.Thread;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class ServThread implements Runnable{
	 protected java.net.Socket clientsocket = null;
	 protected String serverText   = null;
	 private BufferedReader reader;
	 private PrintWriter printWriter;
	 private dateiHandler fehler;
	 private  dataBaseHandler dbh ;
	 

	    public ServThread(java.net.Socket clientSocket, dataBaseHandler dbh) {
	    	this.dbh = dbh;
	        this.clientsocket = clientSocket;
	        try {
				reader = new BufferedReader(new InputStreamReader(clientsocket.getInputStream()));
				printWriter = new PrintWriter(new OutputStreamWriter(clientsocket.getOutputStream()));
				fehler = new dateiHandler("THREADerrLog.txt");
			} catch (IOException e) {
				fehler.openDatei(true);
				fehler.writeErr(e.getMessage()+ "\n");
				fehler.close();
			}
	    }
	
	public void run() {
		
		try {
			String str = getStr();
			putStr(str);
			System.out.println(Thread.currentThread().getName() + " String: " + str);
			procCMD(str);
		} catch (IOException e) {
			fehler.openDatei(true);
			fehler.writeErr(e.getMessage()+ "\n");
			fehler.close();
		}
	}
	
	public String getStr() throws IOException {
		String str = reader.readLine();
		return str;
	}
	
	public void putStr( String stringToSend) throws IOException {
		printWriter.print(stringToSend);
		printWriter.flush();
	}
	
	public  void procCMD(String cmd) {
		switch(Parser.parseCMD(cmd)) {
		case 1:
			doUpdate2DB(); // /UPDATE/
			break;
		case 2:
			newEvent2DB(cmd); // /INSERT/Bezeichner/tt-mm-jjjj/state
			break;
		case 3:
			modifiyEvent2DB(cmd); // /MODIFY/id/Bzeichner/tt-mm-jjjj/state
			break;
		case 4:
			deleteEntry2DB(cmd); // /DELETE/id
			break;
		default:
			break;
		}
		
		try {
			putStr("END");
		}
		
		catch (IOException e) {
			fehler.openDatei(true);
			fehler.writeErr(e.getMessage()+ "\n");
			fehler.close();
		}
	}
	
	public void doUpdate2DB() {
		int i = 1;
		System.out.println("UPDATE");
		String buffer[] = new String[4];
		try {
			buffer = dbh.getEntry(i);
			while(!(buffer[0] == null)) {
				i++;
				putStr("," +buffer[0]+ "," + buffer[1] + "," + buffer[2]+ "," + "\n");
				buffer = dbh.getEntry(i);
			}
			
			
		} catch (IOException e) {
			fehler.openDatei(true);
			fehler.writeErr(e.getMessage()+ "\n");
			fehler.close();
		}
	}
	
	public void newEvent2DB(String cmd){
		String buffer = Parser.getNewInsert(cmd);
		String attr[] = Parser.getAttr(buffer);
		String bezeichner = attr[1];
		String date = attr[2];
		String state = attr[3];
		int id = dbh.getNumRows()+1;
		dbh.insertEntry(id, bezeichner, date, state);
	}
	
	public void modifiyEvent2DB(String cmd) {
		int id = Parser.getID(cmd);
		String buffer = Parser.getModifyStr(cmd);
		String attr[] = Parser.getAttr(buffer);
		String bezeichner = attr[1];
		String date = attr[2];
		String state = attr[3];
		dbh.modifyEntry(id, bezeichner, date, state);
	}
	
	public void deleteEntry2DB(String cmd) {
		int id = Parser.getID(cmd);
		dbh.deleteEntry(id);
		dbh.udpdateIDs();
	}
	
}
