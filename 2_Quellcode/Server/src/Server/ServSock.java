package Server;

import java.util.*;
import java.io.*;

public class ServSock {
	private String id;
	private String adminID;
	private int port;
	private java.net.Socket client;
	private java.net.ServerSocket server;
	private BufferedReader reader;
	private PrintWriter printWriter;
	private int thraedCount = 0;
	private String nameOfThread;
	private DateiHandler fehler;
	private DataBaseHandler dbh ;
	private DateiHandler authGet;
	private DateiHandler adminGet;
	private boolean adminLoggedIn = false;
	
	public ServSock(int port, DataBaseHandler dbh) {
		this.port = port;
		fehler = new DateiHandler("SERVSOCKerrLog.txt");
		this.authGet = new DateiHandler("AuthKey.txt");
		this.adminGet = new DateiHandler("AdminAuthKey.txt");
		this.dbh = dbh;
		authGet.openDatei(false);
		id = authGet.read();
		adminGet.openDatei(false);
		adminID = adminGet.read();
	}
	
	public boolean startConnection() throws IOException {
		
		server = new java.net.ServerSocket(port);
		server.setReuseAddress(true);
		java.net.Socket client =  clientConnect(server);
		reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
		printWriter = new PrintWriter(new OutputStreamWriter(client.getOutputStream()));
		String verifyStr = getStr();
		
		
		if(verifyStr.equals(id)) {
			this.adminLoggedIn = false;
			return true;
		}
		else if(verifyStr.equals(adminID)) {
			this.adminLoggedIn = true;
			return true;
		}
		else {
			server.close();
			return false;
		}
	}
	
	public java.net.Socket clientConnect(java.net.ServerSocket server) throws IOException{
		client = server.accept();
		
		return client;
	}
	
	public String getStr() throws IOException {
		//BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		char buffer[] = new char[200];
		int numChar = reader.read(buffer);
		String str = new String(buffer, 0, numChar);
		
		return str;
	}
	
	public void putStr( String stringToSend) throws IOException {
		//printWriter = new PrintWriter(new OutputStreamWriter(client.getOutputStream()));
		printWriter.print(stringToSend);
		printWriter.flush();
		
	}
	
	public void close() throws IOException {
		server.close();
	}
	
	public boolean login() {
		boolean connAccept;
			try {
				connAccept = this.startConnection();
				if (connAccept == true) {
					this.putStr("ok!");
					System.out.println("Verbindung mit Client akzeptiert!" + " auf Port: " + this.port);
					this.close();
					this.nameOfThread = String.format("Con-%d", this.thraedCount);
					new Thread(new ServThread(client, dbh, adminLoggedIn), this.nameOfThread).start();
					this.thraedCount++;
					return true;
				}
				else {
					this.putStr("denied!");
					System.out.println("Verbindung mit Client abgelehnt!");
					this.close();
					return false;
				}
			} catch (IOException e) {
				fehler.openDatei(true);
				fehler.writeErr(e.getMessage() + "\n");
				fehler.close();
				return false;
			}
		
	}
	
}
