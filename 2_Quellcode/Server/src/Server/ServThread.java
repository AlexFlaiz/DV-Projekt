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
	 private DateiHandler fehler;
	 private  DataBaseHandler dbh ;
	 private boolean adminLoggedIn;
	 

	    public ServThread(java.net.Socket clientSocket, DataBaseHandler dbh, boolean admin) {
	    	this.dbh = dbh;
	        this.clientsocket = clientSocket;
	        this.adminLoggedIn = admin;
	        System.out.println("Admin: " + admin);
	        fehler = new DateiHandler("THREADerrLog.txt");
	        try {
				reader = new BufferedReader(new InputStreamReader(clientsocket.getInputStream()));
				printWriter = new PrintWriter(new OutputStreamWriter(clientsocket.getOutputStream()));
				
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
		if(Parser.parseCMD(cmd) == 1){
			doUpdate2DB(); // /UPDATE/
		}
		else if(Parser.parseCMD(cmd) == 2) {
			newEvent2DB(cmd); // /INSERT/Bzeichner/tt-mm-jjjj/state
		}
		else if(Parser.parseCMD(cmd) == 3) {
			modifiyEvent2DB(cmd); // /MODIFY/id/Bzeichner/tt-mm-jjjj/state/prot
		}
		else if(Parser.parseCMD(cmd) == 4) {
			deleteEntry2DB(cmd); // /DELETE/id
		}
		else {}
		try {
			putStr("END");
		} catch (IOException e) {
			fehler.openDatei(true);
			fehler.writeErr(e.getMessage()+ "\n");
			fehler.close();
		}
	}
	
	public void doUpdate2DB() {
		int i = 1;
		String buffer[] = new String[4];
		try {
			buffer = dbh.getEntry(i);
			while(!(buffer[0] == null)) {
				i++;
				putStr("," +buffer[0]+ "," + buffer[1] + "," + buffer[2]+ "," + buffer[3] + "," + "\n");
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
		String prot = attr[4];
		int id = dbh.getNumRows()+1;
		if(!this.adminLoggedIn) {
			dbh.insertEntry(id, bezeichner, date, state, "false");
		}
		else {
			dbh.insertEntry(id, bezeichner, date, state, prot);
		}
	}
	
	public void modifiyEvent2DB(String cmd) {
		int id = Parser.getID(cmd);
		String buffer = Parser.getModifyStr(cmd);
		String attr[] = Parser.getAttr(buffer);
		String bezeichner = attr[1];
		String date = attr[2];
		String state = attr[3];
		String prot = attr[4];
		
		
		if(dbh.getPriv(id) && this.adminLoggedIn) {
			dbh.modifyEntry(id, bezeichner, date, state, prot);
		}
		else if(!this.adminLoggedIn && !dbh.getPriv(id)) {
			dbh.modifyEntry(id, bezeichner, date, state, "false");
		}
	}
	
	public void deleteEntry2DB(String cmd) {
		int id = Parser.getID(cmd);
		if(dbh.getPriv(id) && this.adminLoggedIn) {
		dbh.deleteEntry(id);
		dbh.udpdateIDs();
		}
		else if(!dbh.getPriv(id)){
			dbh.deleteEntry(id);
			dbh.udpdateIDs();
		}
	}
	
}
