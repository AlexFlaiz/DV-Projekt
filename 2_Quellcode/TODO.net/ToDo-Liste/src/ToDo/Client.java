package ToDo;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

import Test.Fenster;

public class Client {
	Fenster window=new Fenster();

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	private InetSocketAddress address;
	
	public Client(String hostname, int port)
	{
		address=new InetSocketAddress(hostname,port);
	}
	public void sendMessage(String msg)
	{
		try {
		
			Socket socket=new Socket(); // Verbindet sich mit Socket
			socket.connect(address);
			
			//Daten Schreiben
			PrintWriter pw =new PrintWriter(new OutputStreamWriter(socket.getOutputStream())) ;
			pw.println(msg);
			pw.flush();
			
			//Verbindung schlieﬂen
			pw.close();
			socket.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String CreateMessage()
	{
		String Tag,Monat,Jahr,Eintrag, Status;
		
		if (window.getTag()<10)
		{
			Tag = "0"+String.valueOf(window.getTag());
		}
		else
		{
			Tag=String.valueOf(window.getTag());
		}
		
		if (window.getMonat()<10)
		{
			Monat = "0"+String.valueOf(window.getMonat());
		}
		else
		{
			Monat=String.valueOf(window.getMonat());
		}
		Jahr=String.valueOf(window.getJahr());
		
		Eintrag=window.getEintraege();
		
		//Status=window.getStatus();
		
		return Eintrag+"/"+Tag+"-"+Monat+"-"+Jahr+"/"+Status;
		
	}
}
