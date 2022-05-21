package ToDo;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.SwingConstants;
import javax.swing.JInternalFrame;
import javax.swing.JTextField;
import java.awt.Color;
import javax.swing.JTextArea;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.util.ArrayList;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;



public class Fenster {
	

	private JList<String> list;
	private  int index;
	private String Eintrag;
	private JFrame frmTodoListe;
	private JTextField tFTagErled;
	private JTextField tFMonatErled;
	private JTextField tFJahrErled;
	private static java.net.Socket socket;
	static String buffer;
	private static String authkey;
	private static int port ;
	static ArrayList <String> eint;
	DefaultListModel<String>Eintraege;
	static BufferedReader bufferedReader;
	static PrintWriter printWriter;
	ClientParser pars=new ClientParser();
	TODOs todo= new TODOs();
	private String   offeneListe;
	
	
	
	
	
	//static DateiHandler Key;
	static DateiHandler Port;
	static DateiHandler ip;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		//String Dateiname = "AuthKey.txt";
		//Key= new DateiHandler(Dateiname);
		//Key.openDatei(false);
		//authkey= Key.read();
		authkey="veryGoodAdminAuthKey";
		
		String PortDatei = "Port.txt";
		Port= new DateiHandler(PortDatei);
		Port.openDatei(false);
		port=Integer.parseInt(Port.read());
		
		String IPaddress = "IPaddress.txt";
		ip= new DateiHandler(IPaddress);
		ip.openDatei(false);
		String IP =ip.read();
		
		eint=new ArrayList<>();
	
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					socket = new java.net.Socket(IP,port);
					schreibeNachricht(socket,authkey);
					
					Fenster window = new Fenster();
					window.frmTodoListe.setVisible(true);
					window.Aktualisieren();
					window.Aktualisieren();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public Fenster() {
		initialize();
	}

	private void initialize() {
		frmTodoListe = new JFrame();
		frmTodoListe.setTitle("To-do Liste");
		frmTodoListe.setBounds(100, 100, 802, 631);
		frmTodoListe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frmTodoListe.getContentPane().setLayout(null);
		
		
		//Fenster Liste
		
		JInternalFrame Liste = new JInternalFrame("Liste");
		Liste.setClosable(true);
		Liste.setResizable(true);
		Liste.setBounds(131, 10, 485, 493);
		frmTodoListe.getContentPane().add(Liste);
		Liste.getContentPane().setLayout(null);
		
		JLabel lblListe = new JLabel("Liste");
		lblListe.setHorizontalAlignment(SwingConstants.CENTER);
		lblListe.setFont(new Font("Monotype Corsiva", Font.PLAIN, 50));
		lblListe.setBounds(10, 10, 453, 62);
		Liste.getContentPane().add(lblListe);
		
		JButton btnSchliessen = new JButton("Schlie\u00DFen");
		btnSchliessen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				Liste.setVisible(false);
				
			}
		});
		btnSchliessen.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnSchliessen.setBounds(325, 424, 138, 30);
		Liste.getContentPane().add(btnSchliessen);
		
		JScrollPane scrollPane_2 = new JScrollPane();
		scrollPane_2.setBounds(10, 70, 453, 344);
		Liste.getContentPane().add(scrollPane_2);
		
		JTextArea tAListe = new JTextArea();
		tAListe.setFont(new Font("Tahoma", Font.PLAIN, 16));
		tAListe.setEditable(false);
		scrollPane_2.setViewportView(tAListe);
		Liste.setVisible(false);

		
        //Fenster Neuer Eintrag
        
		JInternalFrame NeuerEintrag = new JInternalFrame("Neuer Eintrag");
		NeuerEintrag.setClosable(true);
		NeuerEintrag.setResizable(true);
		NeuerEintrag.setBounds(24, 10, 729, 493);
		frmTodoListe.getContentPane().add(NeuerEintrag);
		NeuerEintrag.getContentPane().setLayout(null);
		
		
		JLabel lblNeuerEintrag = new JLabel("Neuer Eintrag");
		lblNeuerEintrag.setHorizontalAlignment(SwingConstants.CENTER);
		lblNeuerEintrag.setFont(new Font("Monotype Corsiva", Font.BOLD, 50));
		lblNeuerEintrag.setBounds(10, 21, 697, 71);
		NeuerEintrag.getContentPane().add(lblNeuerEintrag);
		
		JButton btnAbbrechen = new JButton("Abbrechen");
		btnAbbrechen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				NeuerEintrag.setVisible(false);	
			}
		});
		btnAbbrechen.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnAbbrechen.setBounds(520, 409, 155, 30);
		NeuerEintrag.getContentPane().add(btnAbbrechen);
		
		JLabel lblErledigungsdatum = new JLabel("Erledigungsdatum");
		lblErledigungsdatum.setHorizontalAlignment(SwingConstants.LEFT);
		lblErledigungsdatum.setFont(new Font("Monotype Corsiva", Font.PLAIN, 25));
		lblErledigungsdatum.setBounds(30, 90, 199, 25);
		NeuerEintrag.getContentPane().add(lblErledigungsdatum);
		
		tFTagErled = new JTextField();
		tFTagErled.setHorizontalAlignment(SwingConstants.CENTER);
		tFTagErled.setToolTipText("");
		tFTagErled.setForeground(Color.BLACK);
		tFTagErled.setFont(new Font("Tahoma", Font.PLAIN, 14));
		tFTagErled.setColumns(10);
		tFTagErled.setBounds(30, 141, 60, 30);
		NeuerEintrag.getContentPane().add(tFTagErled);
		
		tFMonatErled = new JTextField();
		tFMonatErled.setHorizontalAlignment(SwingConstants.CENTER);
		tFMonatErled.setForeground(Color.BLACK);
		tFMonatErled.setFont(new Font("Tahoma", Font.PLAIN, 14));
		tFMonatErled.setColumns(10);
		tFMonatErled.setBounds(89, 141, 60, 30);
		NeuerEintrag.getContentPane().add(tFMonatErled);
		
		tFJahrErled = new JTextField();
		tFJahrErled.setHorizontalAlignment(SwingConstants.CENTER);
		tFJahrErled.setForeground(Color.BLACK);
		tFJahrErled.setFont(new Font("Tahoma", Font.PLAIN, 14));
		tFJahrErled.setColumns(10);
		tFJahrErled.setBounds(148, 141, 60, 30);
		NeuerEintrag.getContentPane().add(tFJahrErled);
		
		JLabel lblEintrag = new JLabel("Eintrag");
		lblEintrag.setHorizontalAlignment(SwingConstants.CENTER);
		lblEintrag.setFont(new Font("Monotype Corsiva", Font.PLAIN, 25));
		lblEintrag.setBounds(30, 185, 645, 25);
		NeuerEintrag.getContentPane().add(lblEintrag);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(30, 220, 645, 174);
		NeuerEintrag.getContentPane().add(scrollPane_1);
		
		JTextArea tAEintrag = new JTextArea();
		tAEintrag.setFont(new Font("Tahoma", Font.PLAIN, 16));
		scrollPane_1.setViewportView(tAEintrag);
		
		JButton btnEintragHinzufuegen = new JButton("Eintrag hinzuf\u00FCgen");	//Befehl zum Eintrag hinzufügen
		btnEintragHinzufuegen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				Eintrag=tAEintrag.getText();
				NeuEintrag();
				NeuerEintrag.setVisible(false);
			}
		});
		btnEintragHinzufuegen.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnEintragHinzufuegen.setBounds(30, 409, 178, 30);
		NeuerEintrag.getContentPane().add(btnEintragHinzufuegen);
		
		JButton btnLeeren = new JButton("Leeren");				//Leere Einträge in Fenster Neuer Eintrag
		btnLeeren.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				tAEintrag.setText("");
				leeren();
			}
		});
		btnLeeren.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnLeeren.setBounds(218, 409, 120, 30);
		NeuerEintrag.getContentPane().add(btnLeeren);
		
		JLabel lblTag_1 = new JLabel("Tag");
		lblTag_1.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblTag_1.setBounds(30, 124, 60, 13);
		NeuerEintrag.getContentPane().add(lblTag_1);
		
		JLabel lblMonat_1 = new JLabel("Monat");
		lblMonat_1.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblMonat_1.setBounds(89, 124, 60, 13);
		NeuerEintrag.getContentPane().add(lblMonat_1);
		
		JLabel lblJahr_1 = new JLabel("Jahr");
		lblJahr_1.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblJahr_1.setBounds(148, 124, 60, 13);
		NeuerEintrag.getContentPane().add(lblJahr_1);
		
		JCheckBox CBAdminTodo = new JCheckBox("Von Benutzer \u00E4nderbar");
		CBAdminTodo.setFont(new Font("Tahoma", Font.PLAIN, 14));
		CBAdminTodo.setBounds(30, 189, 178, 21);
		NeuerEintrag.getContentPane().add(CBAdminTodo);
		CBAdminTodo.setVisible(false);
		if (authkey.equals("veryGoodAdminAuthKey"))
		{
		CBAdminTodo.setVisible(true);	
		}
		
		NeuerEintrag.setVisible(false);
		
		
		//Fenster mit To Do Liste
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 93, 768, 375);
		frmTodoListe.getContentPane().add(scrollPane);
		
		Eintraege=new DefaultListModel<>();
		JList<String> list = new JList<>(Eintraege);
		list.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) 
			{
				index=list.getSelectedIndex();					//Index des ausgewählten ToDos
			}
		});
		scrollPane.setViewportView(list);
		list.setFont(new Font("Tahoma", Font.PLAIN, 16));
		
		JLabel Eintraege = new JLabel("Datum | Status | Eintrag");
		scrollPane.setColumnHeaderView(Eintraege);
		Eintraege.setFont(new Font("Tahoma", Font.PLAIN, 18));
		
		
		JButton btnNeuerEintrag = new JButton("Neuer Eintrag");
		btnNeuerEintrag.setBounds(37, 511, 151, 31);
		frmTodoListe.getContentPane().add(btnNeuerEintrag);
		btnNeuerEintrag.setFont(new Font("Tahoma", Font.PLAIN, 18));
		

		JButton btnLoeschen = new JButton("L\u00F6schen");				//Eintrag wird gelöscht
		btnLoeschen.setBounds(626, 513, 117, 31);
		frmTodoListe.getContentPane().add(btnLoeschen);
		btnLoeschen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
                 if (pars.getAdmin(eint.get(index))==true && authkey.equals("veryGoodAuthKey"))
 				{
                	 JOptionPane.showMessageDialog(btnLoeschen , "Admin Einträge können nicht gelöscht werden." , "Fehler",
 							JOptionPane.ERROR_MESSAGE );
 				}
				else 
				{
				int response = JOptionPane.showConfirmDialog(btnLoeschen, "Soll der Eintrag wirklich gelöscht werden?  " + "","Eintrag löschen",
							JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);         
					
					if (response==JOptionPane.YES_OPTION) 
					{
		
						loescheTodo(socket,(index+1));
						Aktualisieren();
					}
				}
			}			
		});
		btnLoeschen.setFont(new Font("Tahoma", Font.PLAIN, 18));
		
		
		JLabel lblToDoList = new JLabel("To-do Liste");
		lblToDoList.setHorizontalAlignment(SwingConstants.CENTER);
		lblToDoList.setBounds(0, 22, 788, 65);
		frmTodoListe.getContentPane().add(lblToDoList);
		lblToDoList.setFont(new Font("Monotype Corsiva", Font.BOLD, 50));
		
		JButton btnerledigt = new JButton("Erledigt");					//Stratus wird geändert
		btnerledigt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
			aendereStatus();
				if (pars.getAdmin(eint.get(index))==true && authkey.equals("veryGoodAuthKey"))
				{
					JOptionPane.showMessageDialog(btnLoeschen , "Admin Einträge können nicht bearbeitet werden." , "Fehler",
							JOptionPane.ERROR_MESSAGE );
				}
		}
		});
		btnerledigt.setFont(new Font("Tahoma", Font.PLAIN, 18));
		btnerledigt.setBounds(499, 513, 117, 31);
		frmTodoListe.getContentPane().add(btnerledigt);
		
		
		btnNeuerEintrag.addActionListener(new ActionListener() {		//Fenster neuer Eintrag wird geöffnet
			public void actionPerformed(ActionEvent e) 
			{
				NeuerEintrag.setVisible(true);
			}
		});
		
		JMenuBar menuBar = new JMenuBar();
		frmTodoListe.setJMenuBar(menuBar);
		
		JMenu mnDatei = new JMenu("Datei");
		menuBar.add(mnDatei);
		
		JMenuItem mntmNeuerEintrag = new JMenuItem("Neuer Eintrag");	//Fenster neuer Eintrag wird geöffnet (in Menüleiste)
		mntmNeuerEintrag.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				NeuerEintrag.setVisible(true);
			}
		});
		mnDatei.add(mntmNeuerEintrag);
		
		JMenuItem mntmSpeichern = new JMenuItem("Speichern");
		mntmSpeichern.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				Speichern();
			}
		});
		mnDatei.add(mntmSpeichern);
		
		
		JMenuItem mntmOeffnen = new JMenuItem("\u00D6ffnen");
		mntmOeffnen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				load();
				tAListe.setText(offeneListe);
				Liste.setVisible(true);	
			}
		});
		mnDatei.add(mntmOeffnen);
		
		
		JSeparator separator = new JSeparator();
		mnDatei.add(separator);
		
		JMenuItem mntmBeenden = new JMenuItem("Beenden");			//Anwendung wird beendet
		mntmBeenden.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				System.exit(0);
			}
		});
		mnDatei.add(mntmBeenden);
		
		
		
		JMenu mnListe = new JMenu("Liste");
		menuBar.add(mnListe);
		
		JMenuItem mntmAktualisieren = new JMenuItem("Aktualisieren");		//Liste wird Aktualisiert 
		mntmAktualisieren.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				Aktualisieren();
			}
		});
		mnListe.add(mntmAktualisieren);
	}
	
	
	protected static void setVisible(boolean b) {
		// TODO Auto-generated method stub
		
	}

	public void NeuEintrag() 		//Neues ToDo wird erstellt
	{
		String Datum;
		String D = tFTagErled.getText()+"-"+tFMonatErled.getText()+"-"+tFJahrErled.getText();
		String Da= todo.getDate(D);
		boolean Status = todo.statDatum(Da);
		
		if (Status == true)
		{
			Datum=Da;
		}
		else
		{
			JOptionPane.showMessageDialog(null, "Fehlerhafte Eingabe", "Fehler",JOptionPane.ERROR_MESSAGE );
			return;
		}

		Status = false;
		sendTodos(socket,Datum,Eintrag, Status);
		Aktualisieren();
	}
	
	public void leeren()				//Fenster NeuerEintrag wird geleert
	{
		tFTagErled.setText("");
		tFMonatErled.setText("");
		tFJahrErled.setText("");	
	}
	
	
	 static void schreibeNachricht(java.net.Socket socket, String nachricht) //Nachricht wird an Server geschickt  
	 {	
		try {
			printWriter = new PrintWriter(
				new OutputStreamWriter(
					socket.getOutputStream()));
		printWriter.print(nachricht);
 		printWriter.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	static String leseNachricht(java.net.Socket socket)  	//Nachricht vom Server wird gelesen 
	{
		try {
			bufferedReader = new BufferedReader(
				new InputStreamReader(
					socket.getInputStream()));
			char[] buffer = new char[200];
		int anzahlZeichen = bufferedReader.read(buffer, 0, 200); // blockiert bis Nachricht empfangen
		String nachricht = new String(buffer, 0, anzahlZeichen);
		return nachricht;
		} 
	catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
	}
	
	public static void getTodos(java.net.Socket socket) 	//Liste der Todos wird vom Server geholt und in ArrayList gespeichert
	{ 
		schreibeNachricht(socket,"/UPDATE/\n");
		String empfangeneNachricht = leseNachricht(socket);
			
			while(!empfangeneNachricht.contains("/END/"))
			{
			empfangeneNachricht = leseNachricht(socket);
			eint.add(empfangeneNachricht);	
			}
	}
	
	public static void sendTodos(java.net.Socket socket, String Datum, String Eintrag, boolean S)  //Neue ToDos werden an den Server geschickt
	{
		String Nachricht;
		boolean b=false;
		if (authkey.equals("veryGoodAdminAuthKey")) 
			{
				b=true;
			
			}
		
		Nachricht="/INSERT/"+Eintrag+"/"+Datum+"/"+S+"/"+b+"\n";
		schreibeNachricht(socket,Nachricht);
		String empfangeneNachricht = leseNachricht(socket);
		
			while(!empfangeneNachricht.contains("/END/"))
			{
			empfangeneNachricht = leseNachricht(socket);
			}	
	}
	
	public static void loescheTodo(java.net.Socket socket,int i)	//ToDos werden durch den gesendeten Befehl im Server gelöscht
	{
		String Nachricht="/DELETE/"+i+"\n";
		schreibeNachricht(socket,Nachricht);
		String empfangeneNachricht = leseNachricht(socket);
		
			while(!empfangeneNachricht.contains("/END/"))
			{
			empfangeneNachricht = leseNachricht(socket);
			}
	}
	
	public void fuelleListe()					//Liste in GUI neu befüllen
	{
		for (int i=0; i<(eint.size()-1);i++)
		{
			String Notiz= pars.setStatus(eint.get(i), pars.getStatus(eint.get(i)));
			Eintraege.addElement(Notiz);
		}	  
	}
	
	public void loescheListe()			//ArrayLists werden geleert 
	{
		Eintraege.clear();
		eint.clear();	
	}
	
	public void Aktualisieren()		//Liste in GUI wird Aktualisiert
	{
		loescheListe();
		getTodos(socket);	
		fuelleListe();
	}
	
	public void aendereStatus()	//Erledigungsstatus der ToDos werden geändert
	{
		int i=index;
		int j= (index+1);
		String Notiz=eint.get(i);
		String Datum=pars.getDatum(Notiz);
		boolean Stat=pars.getNewStatus(Notiz);
		String Nachricht;
		
		String teilstr[];
		teilstr = Notiz.split(",");
		String Eintrag= teilstr[1];
		
		Nachricht="/MODIFY/"+j+"/"+Eintrag+"/"+Datum+"/"+Stat+"/"+port+"\n";
		schreibeNachricht(socket,Nachricht);
		String empfangeneNachricht = leseNachricht(socket);
		
			while(!empfangeneNachricht.contains("/END/"))
			{
			empfangeneNachricht = leseNachricht(socket);
			}
			
		Aktualisieren();
	}
	
	
	protected void Speichern()						//Speichere Liste
	{
		final JFileChooser fc = new JFileChooser();
		int returnVal= fc.showSaveDialog(list);
		
		if(returnVal == JFileChooser.APPROVE_OPTION){
			File file = fc.getSelectedFile();
			saveText(file);
			}
}
		private void saveText(File file) {
			try {
				FileWriter writer =new FileWriter(file);
				
				for (int i=0; i<Eintraege.getSize();i++)
				{
				String text = Eintraege.get(i)+"\n";
				writer.write(text);
				}
				writer.flush();
				writer.close();
			}
			catch (IOException e){
				e.printStackTrace();
			}
		}
		
		protected void load()				//Öffne Datei
		{
			final JFileChooser fc = new JFileChooser();
			int returnVal = fc.showOpenDialog(list);   // jFrame durch getParent() frame ersetzt
			if(returnVal == JFileChooser.APPROVE_OPTION)
			{
				File file = fc.getSelectedFile();
				showText (file);
			}
		}
		
		private void showText (File file)
		{
			StringBuffer buf = new StringBuffer();
			if (file.exists())
			{
				try
				{
					BufferedReader reader = new BufferedReader (new FileReader (file));
					String line = "";
					while ((line = reader.readLine( )) != null)
					{
						buf.append(line+"\n");
					}
					reader.close();
				}
				catch (FileNotFoundException e)
				{
					e.printStackTrace();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
			
			  offeneListe= buf.toString();
		}
}
