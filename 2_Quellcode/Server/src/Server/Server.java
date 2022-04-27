package Server;

import java.io.IOException;
import java.lang.Thread;
import java.util.*;


public class Server {
	
	
	
	public static void main(String[] args) {
		normalMode();
	}
	
	public static void normalMode() {
		DataBaseHandler dbh;
		dbh = new DataBaseHandler("jdbc:sqlite:TODO.db");
		ServSock server = new ServSock(1112, dbh);
		System.out.println("Server gestartet!");
		while(server.login()) {}
	}

}
