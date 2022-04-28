package Server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

public class DataBaseHandler {
	
	private String dbUrl; 
	 private DateiHandler fehler;
	
	public DataBaseHandler(String dbUrl) {
		this.dbUrl = dbUrl;
		fehler = new DateiHandler("DBerrLog.txt");
	}
	
	public synchronized void insertEntry(int id, String name, String datum, String state, String prot) {
		try {
			Connection con = DriverManager.getConnection(this.dbUrl);
			Statement command = con.createStatement();
			String cmd = "insert into current (Bezeichner, Date, State, ID, protected) values (" + "'" +name + "'," + "'" +datum+ "', '"  +state + "'," + id + ", '" + prot +"')";
			command.executeUpdate(cmd);
			command.close();
			con.close();
		} catch (SQLException e) {
			fehler.openDatei(true);
			fehler.writeErr(e.getMessage()+ "\n");
			fehler.close();
		}
	}
	
	public synchronized void modifyEntry(int id, String name, String datum, String state, String prot) {
		try {
			Connection con = DriverManager.getConnection(this.dbUrl);
			Statement command = con.createStatement();
			String cmd = "update current set Bezeichner = '" + name + "' where ID = " + id ;
			command.executeUpdate(cmd);
			cmd = "update current set Date = '" + datum + "' where ID = " + id ;
			command.executeUpdate(cmd);
			cmd = "update current set State = '" + state + "' where ID = " + id ;
			command.executeUpdate(cmd);
			cmd = "update current set protected = '" + prot + "' where ID = " + id ;
			command.executeUpdate(cmd);
			command.close();
			con.close();
		} catch (SQLException e) {
			fehler.openDatei(true);
			fehler.writeErr(e.getMessage()+ "\n");
			fehler.close();
		}
	}
	
	public synchronized String[] getEntry(int id) {
		String retVal[] = new String[4];
		try {
			Connection con = DriverManager.getConnection(this.dbUrl);
			Statement command = con.createStatement();
			String cmd = "select Bezeichner, Date, State, protected from current where ID = " + id;
			ResultSet buffer = command.executeQuery(cmd);
			if(!buffer.isClosed()) {
			retVal[0] = buffer.getString(1);
			retVal[1] = buffer.getString(2);
			retVal[2] = buffer.getString(3);
			retVal[3] = buffer.getString(4);
			}
			else {
				retVal[0] = null;
				retVal[1] = null;
				retVal[2] = null;
				retVal[3] = null;
			}
			buffer.close();
			command.close();
			con.close();
		} catch (SQLException e) {
			fehler.openDatei(true);
			fehler.writeErr(e.getMessage()+ "\n");
			fehler.close();
		}
		return retVal;
	}
	
	public synchronized  void deleteEntry(int id) {
		String buffer[] = new String[5];
		buffer = this.getEntry(id);
		
		try {
			Connection con = DriverManager.getConnection(this.dbUrl);
			Statement command = con.createStatement();
			String cmd = "insert into legacy (Bezeichner, Date, State, ID, protected) values (" + "'" + buffer[0] + "'," + "'" +buffer[1]+ "', '"  +buffer[2] + "'," + id + ", "+ buffer[3]+")";
			command.executeUpdate(cmd);
			cmd = "delete from current where ID = " + id;
			command.executeUpdate(cmd);
			command.close();
			con.close();
		} catch (SQLException e) {
			fehler.openDatei(true);
			fehler.writeErr(e.getMessage()+ "\n");
			fehler.close();
		}
	}
	
	public synchronized int getNumRows() {
		int count = 1;
		String buffer[] = new String[4];
		buffer = getEntry(count);
		while(!(buffer[0] == null)) {
			count++;
			buffer = getEntry(count);
		}
		return count-1;
	}
	
	public synchronized int udpdateIDs() {
		Connection con;
		int id = 0;
		int count = 1;
		try {
			con = DriverManager.getConnection(this.dbUrl);
			Statement command = con.createStatement();
			Statement update = con.createStatement();
			String cmd_id;
			String cmd = "select * from current";
			ResultSet rs = command.executeQuery(cmd);
			while(rs.next()) {
				id= rs.getInt("ID");
				cmd_id =  "update current set ID = " + count +" where ID = " + id;
				update.executeUpdate(cmd_id);
				count++;
			}
			rs.first();
			con.close();
			command.close();
			
		} catch (SQLException e) {
			fehler.openDatei(true);
			fehler.writeErr(e.getMessage()+ "\n");
			fehler.close();
		}
		return id;
	}
	
	public synchronized boolean getPriv(int id){
		try {
		Connection con = DriverManager.getConnection(this.dbUrl);
		Statement command = con.createStatement();
		String cmd = "select protected from current where ID = " + id;
		ResultSet buffer = command.executeQuery(cmd);
		String state = buffer.getString(1);
		con.close();
		if(state.equals("true")) {
			return true;
		}
		else {
			return false;
		}
		}catch(SQLException e) {
			fehler.openDatei(true);
			fehler.writeErr(e.getMessage()+ "\n");
			fehler.close();
			return false;
		}
	}
	
}
