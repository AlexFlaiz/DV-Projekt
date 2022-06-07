/** 
 * 
 * @author alexflaiz tobiasaberle
 * 
 * */
package ToDo;


import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
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
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.*;
import java.util.ArrayList;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;



public class Fenster {
	
	private JList<String> list;  // Liste mit ToDos
	private  int index; // Index fuer ToDo aus der Liste
	private String Eintrag; // Notiz, ohne Datum oder Status
	private JFrame frmTodoListe;  // Fensterdarstellung
	private JTextField tFTagErled;  // Textfeld Erledigungstag
	private JTextField tFMonatErled; // Textfeld Erledigungsmonat
	private JTextField tFJahrErled; // Textfeld Erledigungsjahr
	private static java.net.Socket socket; // TCP/IP-Verbindung zum Server
	static String buffer; // Buffer zum  uebertragen des ToDos
	private static String authkey; // Benutzeridentifizierung
	private static int port; // Port fuer die Verbindung
	static ArrayList <String> eint; // Notiz mit Status
	DefaultListModel<String>Eintraege; // Liste von Notizen fuer das GUI
	static BufferedReader bufferedReader; // Erstellt Ausgabedaten in allgemein lesbarer Form
	static PrintWriter printWriter; // Nummer des Threads
	ClientParser pars=new ClientParser();
	TODOs todo= new TODOs();
	private static String offeneListe;
	private static String Adminpriv; // Uebergabe des Adminstatus
	private static boolean priv; // Rolle des Benutzers 1=Admin 0=Default
	private static boolean AdminBox; // Admin-Checkbox bei ToDo vorhanden/ nicht vorhanden
	ArrayList <String> Drucktext;
	private String dateToStr;
	Date date;
	
	static DateiHandler Key; // Dateihandler fuer Benutzer-ID
	static DateiHandler Port; // Dateihandler fuer den Benutzer-Port
	static DateiHandler ip; // Dateihandler fuer die IP-Adresse des Benutzers

	public static void main(String[] args) {
		/**
		 * Benutzer-ID, Port und IP-Adresse werden aus der jeweiligen Textdatei ausgelesen.
		 */
		
		String Dateiname = "AuthKey.txt";		
		Key= new DateiHandler(Dateiname);
		Key.openDatei(false);
		authkey= Key.read();
		
		String PortDatei = "Port.txt";
		Port= new DateiHandler(PortDatei);
		Port.openDatei(false);
		port=Integer.parseInt(Port.read());
			
		String IPaddress = "IPaddress.txt";
		ip= new DateiHandler(IPaddress);
		ip.openDatei(false);
		String IP =ip.read();
	
		eint=new ArrayList<>();
		AdminBox=false;
		
		/**
		 * Admin-Status wird bei Server abgefragt und in priv gespeichert.
		 * @param IP IP-Adresse Client
		 * @param port Port Client
		 * @param socket
		 * @param authkey Benutzer-ID 
		 */
			EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					socket = new java.net.Socket(IP,port);
					schreibeNachricht(socket,authkey);
				
					String Nachricht="//GETADMIN//"+"\n";		//Frägt Admin Status beim Server nach
					schreibeNachricht(socket,Nachricht);
					String empfangeneNachricht = leseNachricht(socket);
					empfangeneNachricht = leseNachricht(socket);
					empfangeneNachricht = leseNachricht(socket);
					String [] SplidAdmin = empfangeneNachricht.split("//");
					Adminpriv=SplidAdmin[1];
					priv=Boolean.parseBoolean(Adminpriv);	//Speichert Admin Status in priv
					
					while(!empfangeneNachricht.contains("//END//"))
					{
						empfangeneNachricht = leseNachricht(socket);
					}	
					
					Fenster window = new Fenster();
					window.frmTodoListe.setVisible(true);

					window.Aktualisieren();
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	/**
	 *  Initialisiert das Fenster
	 */
	public Fenster() {
		initialize();
	}

	/**
	 * Darstellungsparameter werden gesetzt, Buttonverarbeitung
	 */
	private void initialize() {
		frmTodoListe = new JFrame();
		frmTodoListe.setTitle("TODO.net");
		frmTodoListe.setBounds(100, 100, 800, 630);
		frmTodoListe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frmTodoListe.getContentPane().setLayout(null);
		
		
		
		// Darstellung der Liste
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
		
		// Liste in GUI schliessen
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
		
		JLabel lblEintraegeListe = new JLabel("Datum | Status | Eintrag");
		lblEintraegeListe.setFont(new Font("Tahoma", Font.PLAIN, 15));
		scrollPane_2.setColumnHeaderView(lblEintraegeListe);
		
		JButton btnDrucken = new JButton("Drucken");
		btnDrucken.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				drucken();
			}
		});
		btnDrucken.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnDrucken.setBounds(10, 424, 138, 30);
		Liste.getContentPane().add(btnDrucken);
		Liste.setVisible(false);
	
		
        // Fenster Neuer Eintrag
		JInternalFrame NeuerEintrag = new JInternalFrame("Neuer Eintrag");
		NeuerEintrag.setClosable(true);
		NeuerEintrag.setResizable(true);
		NeuerEintrag.setBounds(24, 10, 729, 493);
		frmTodoListe.getContentPane().add(NeuerEintrag);
		NeuerEintrag.getContentPane().setLayout(null);
		
		// Darstellung Fenster NeuerEintrag
		JLabel lblNeuerEintrag = new JLabel("Neuer Eintrag");
		lblNeuerEintrag.setHorizontalAlignment(SwingConstants.CENTER);
		lblNeuerEintrag.setFont(new Font("Monotype Corsiva", Font.BOLD, 50));
		lblNeuerEintrag.setBounds(10, 21, 697, 71);
		NeuerEintrag.getContentPane().add(lblNeuerEintrag);
		
		// Fenster mit NeuerEintrag schliessen
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
		
		// CheckBox fuer Admin
		JCheckBox CBAdminTodo = new JCheckBox("Von Benutzer \u00E4nderbar"); 
		CBAdminTodo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (CBAdminTodo.isSelected()==true)
				{
					AdminBox=true;
				}
				else
				{
					AdminBox=false;
				}
			}
		});
		CBAdminTodo.setFont(new Font("Tahoma", Font.PLAIN, 14));
		CBAdminTodo.setBounds(30, 189, 230, 21);
		NeuerEintrag.getContentPane().add(CBAdminTodo);
		CBAdminTodo.setVisible(false);
		if (priv==true)
		{
		CBAdminTodo.setVisible(true);	
		}
		
		JTextArea tAEintrag = new JTextArea();
		tAEintrag.setFont(new Font("Tahoma", Font.PLAIN, 16));
		scrollPane_1.setViewportView(tAEintrag);
		
		// Befehl zum Eintrag hinzufuegen
		JButton btnEintragHinzufuegen = new JButton("Eintrag hinzuf\u00FCgen");
		btnEintragHinzufuegen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				if (tAEintrag.getText().length()>151)
				{
					 JOptionPane.showMessageDialog(NeuerEintrag , "Maximal 150 Zeichen möglich" , "Fehler",
	 							JOptionPane.ERROR_MESSAGE );
				}
				else if (tAEintrag.getText().equals(""))
				{
					JOptionPane.showMessageDialog(NeuerEintrag , "Fehlerhafte Texteingabe" , "Fehler",
 							JOptionPane.ERROR_MESSAGE );
				}
				else
				{
					String Datum;
					String D = tFTagErled.getText()+"-"+tFMonatErled.getText()+"-"+tFJahrErled.getText();
					String Da= todo.getDate(D);
					boolean DateGood = todo.statDatum(Da);
					
						if (DateGood == true)
						{
							Datum=Da;
						}
						else
						{
							JOptionPane.showMessageDialog(NeuerEintrag, "Fehlerhaftes Datum eingetragen", "Fehler",
									JOptionPane.ERROR_MESSAGE ); return;
						}
						
					Eintrag=tAEintrag.getText();
					NeuEintrag(Datum);
					NeuerEintrag.setVisible(false);
					leeren();
					tAEintrag.setText("");
				}
			}
		});
		btnEintragHinzufuegen.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnEintragHinzufuegen.setBounds(30, 409, 178, 30);
		NeuerEintrag.getContentPane().add(btnEintragHinzufuegen);
		
		// Leere Eintraege in Fenster Neuer Eintrag
		JButton btnLeeren = new JButton("Leeren");			
		btnLeeren.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				tAEintrag.setText("");
				leeren();
				CBAdminTodo.setSelected(false);
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
		
		NeuerEintrag.setVisible(false);
		
		
		
		// Fenster mit ToDo Liste
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 93, 768, 375);
		frmTodoListe.getContentPane().add(scrollPane);
		
		// Index des ausgewaehlten ToDos
		Eintraege=new DefaultListModel<>();
		JList<String> list = new JList<>(Eintraege);
		list.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) 
			{
				index=list.getSelectedIndex();				
			}
		});
		scrollPane.setViewportView(list);
		list.setFont(new Font("Tahoma", Font.PLAIN, 16));
		
		// Darstellung der Eintraege
		JLabel Eintraege = new JLabel("Datum | Status | Eintrag");
		scrollPane.setColumnHeaderView(Eintraege);
		Eintraege.setFont(new Font("Tahoma", Font.PLAIN, 18));
		
		// Darstellung Button Neuer Eintrag
		JButton btnNeuerEintrag = new JButton("Neuer Eintrag");
		btnNeuerEintrag.setBounds(37, 511, 175, 31);
		frmTodoListe.getContentPane().add(btnNeuerEintrag);
		btnNeuerEintrag.setFont(new Font("Tahoma", Font.PLAIN, 18));
		
		// Darstellung Button loeschen, loeschen des Eintrags
		JButton btnLoeschen = new JButton("L\u00F6schen");			
		btnLoeschen.setBounds(626, 513, 117, 31);
		frmTodoListe.getContentPane().add(btnLoeschen);
		btnLoeschen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
                 if (pars.getAdmin(eint.get(index))==true && priv==false)
 				{
                	 JOptionPane.showMessageDialog(frmTodoListe , "Eintrag ist nicht zum löschen freigegeben." , "Fehler",
 							JOptionPane.ERROR_MESSAGE );
 				}
				else 
				{
				int response = JOptionPane.showConfirmDialog(frmTodoListe, "Soll der Eintrag wirklich gelöscht werden?  " + "","Eintrag löschen",
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
		
		// Status des ToDos wird geaendert
		JButton btnerledigt = new JButton("Erledigt");					
		btnerledigt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
			aendereStatus();
				if (pars.getAdmin(eint.get(index))==true && priv==false)
				{
					JOptionPane.showMessageDialog(frmTodoListe , "Eintrag ist nicht zur Bearbeitung freigegeben." , "Fehler",
							JOptionPane.ERROR_MESSAGE );
				}
		}
		});
		btnerledigt.setFont(new Font("Tahoma", Font.PLAIN, 18));
		btnerledigt.setBounds(499, 513, 117, 31);
		frmTodoListe.getContentPane().add(btnerledigt);
		
		JLabel lblAdmin = new JLabel("Admin");
		lblAdmin.setForeground(Color.LIGHT_GRAY);
		lblAdmin.setHorizontalAlignment(SwingConstants.CENTER);
		lblAdmin.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblAdmin.setBounds(727, -1, 61, 22);
		frmTodoListe.getContentPane().add(lblAdmin);
		lblAdmin.setVisible(false);
		if (priv==true)
		{
		lblAdmin.setVisible(true);	
		}
		
		// Fenster fuer neuer Eintrag wird geoeffnet
		btnNeuerEintrag.addActionListener(new ActionListener() {		
			public void actionPerformed(ActionEvent e) 
			{
				NeuerEintrag.setVisible(true);
				CBAdminTodo.setSelected(false);
				AdminBox=false;
			}
		});
		
		JMenuBar menuBar = new JMenuBar();
		frmTodoListe.setJMenuBar(menuBar);
		
		JMenu mnDatei = new JMenu("Datei");
		menuBar.add(mnDatei);
		
		// Fenster neuer Eintrag wird in Menueleiste geoeffnet
		JMenuItem mntmNeuerEintrag = new JMenuItem("Neuer Eintrag");
		mntmNeuerEintrag.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				NeuerEintrag.setVisible(true);
			}
		});
		mnDatei.add(mntmNeuerEintrag);
		
		
		JMenuItem mntmOeffnen = new JMenuItem("\u00D6ffnen");
		mntmOeffnen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Laden();
				tAListe.setText(offeneListe);
				Liste.setVisible(true);	
			}
		});
		
		JMenu mnSpeichern = new JMenu("Speichern");
		mnDatei.add(mnSpeichern);
		
		JMenuItem mntmAlle = new JMenuItem("Alle Eintr\u00E4ge");
		mntmAlle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				SpeichernAlle();
			}
		});
		mnSpeichern.add(mntmAlle);
		
		JMenuItem mntmOffene = new JMenuItem("Offene Eintr\u00E4ge");
		mntmOffene.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				SpeichernOffene();
			}
		});
		mnSpeichern.add(mntmOffene);
		mnDatei.add(mntmOeffnen);
		
		JSeparator separator = new JSeparator();
		mnDatei.add(separator);
		
		// Anwendung wird beendet
		JMenuItem mntmBeenden = new JMenuItem("Beenden");	
		mntmBeenden.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				System.exit(0);
			}
		});
		mnDatei.add(mntmBeenden);
		
		JMenu mnListe = new JMenu("Liste");
		menuBar.add(mnListe);
		
		// Liste wird aktualisiert
		JMenuItem mntmAktualisieren = new JMenuItem("Aktualisieren");	
		mntmAktualisieren.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				Aktualisieren();
			}
		});
		mnListe.add(mntmAktualisieren);
	}
	
	
	
	// Methoden der Klasse
	
	/**
	 * Neues ToDo wird erstellt
	 * @param Datum Datum in dd-mm-yyyy Format
	 */
	public void NeuEintrag(String Datum) 		
	{
		boolean Status = false;
		sendTodos(socket,Datum,Eintrag, Status);
		Aktualisieren();
	}
	
	/**
	 * Das Fenster NeuerEintrag wird geleert
	 */
	public void leeren()			
	{
		tFTagErled.setText("");
		tFMonatErled.setText("");
		tFJahrErled.setText("");	
		AdminBox=false;
	}
	
	/**
	 * Nachricht wird an Server geschickt
	 * @param socket
	 * @param nachricht String bestehend aus der Nachricht
	 */
	 static void schreibeNachricht(java.net.Socket socket, String nachricht) 
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
	
	 /** Nachricht wird vom Server gelesen
	  * @param socket
	  * @return nachricht Uebertragene Nachricht
	  */
	static String leseNachricht(java.net.Socket socket)   
	{
		try {
			bufferedReader = new BufferedReader(
				new InputStreamReader(
					socket.getInputStream()));
			char[] buffer = new char[200];
		int anzahlZeichen = bufferedReader.read(buffer, 0, 200);
		String nachricht = new String(buffer, 0, anzahlZeichen);
		return nachricht;
		} 
	catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
	}
	
	/**
	 * Liste der ToDos wird vom Server geholt und in ArrayList gespeichert
	 * @param socket
	 */
	public static void getTodos(java.net.Socket socket) 
	{ 
		schreibeNachricht(socket,"//UPDATE//\n");
		String empfangeneNachricht = leseNachricht(socket);
			
			while(!empfangeneNachricht.contains("//END//"))
			{
				empfangeneNachricht = leseNachricht(socket);
				eint.add(empfangeneNachricht);	
			}
	}
	
	/**
	 * Neue ToDos werden an Server geschickt.
	 * @param socket
	 * @param Datum Erledigungsdatum String dd-mm-YYYY
	 * @param Eintrag Notiz, ohne Datum oder Status
	 * @param Status Erledigungsstatus
	 */
	public static void sendTodos(java.net.Socket socket, String Datum, String Eintrag, boolean Status) 
	{
		String Nachricht;
		if (AdminBox==false)
		{
			Nachricht="//INSERT//"+Eintrag+"//"+Datum+"//"+Status+"//"+true+"\n";
		}
		else
		{
			Nachricht="//INSERT//"+Eintrag+"//"+Datum+"//"+Status+"//"+false+"\n";
		}
		schreibeNachricht(socket,Nachricht);
		String empfangeneNachricht = leseNachricht(socket);
		
			while(!empfangeneNachricht.contains("//END//"))
			{
				empfangeneNachricht = leseNachricht(socket);
			}	
	}
	
	/**
	 * ToDos werden durch den gesendeten Befehl im Server geloescht
	 * @param socket
	 * @param i Index der zu loeschenden Nachricht 
	 */
	public static void loescheTodo(java.net.Socket socket,int i)
	{
		String Nachricht="//DELETE//"+i+"\n";
		schreibeNachricht(socket,Nachricht);
		String empfangeneNachricht = leseNachricht(socket);
		
			while(!empfangeneNachricht.contains("//END//"))
			{
				empfangeneNachricht = leseNachricht(socket);
			}
	}
	
	/**
	 * Liste in GUI neu befuellen
	 */ 
	public void fuelleListe()
	{
		for (int i=0; i<(eint.size()-1);i++)
		{
			String Notiz= pars.setStatus(eint.get(i), pars.getStatus(eint.get(i)));
			Eintraege.addElement(Notiz);
		}	  
	}
	
	/**
	 * ArrayLists leeren
	 */
	public void loescheListe()
	{
		Eintraege.clear();
		eint.clear();	
	}
	
	/**
	 * Liste in GUI wird aktualisiert
	 */
	public void Aktualisieren()
	{
		loescheListe();
		getTodos(socket);	
		fuelleListe();
	}
	
	/**
	 * Erledigungsstatus des ToDos wird geaendert
	 */
	public void aendereStatus()
	{
		int i=index;
		int j= (index+1);
		String Notiz=eint.get(i);
		String Datum=pars.getDatum(Notiz);
		boolean Stat=pars.getNewStatus(Notiz);
		boolean priv = pars.getAdmin(Notiz);
		String Nachricht;
		
		String teilstr[];
		teilstr = Notiz.split("::");
		String Eintrag= teilstr[1];
		
		Nachricht="//MODIFY//"+j+"//"+Eintrag+"//"+Datum+"//"+Stat+"//"+priv+"\n";
		schreibeNachricht(socket,Nachricht);
		String empfangeneNachricht = leseNachricht(socket);
		
			while(!empfangeneNachricht.contains("//END//"))
			{
				empfangeneNachricht = leseNachricht(socket);
			}
		Aktualisieren();
	}
	
	/**
	 * Speichern aller ToDos in Liste
	 */
	protected void SpeichernAlle()
	{
		final JFileChooser fc = new JFileChooser();
		int returnVal= fc.showSaveDialog(list);
		
		if(returnVal == JFileChooser.APPROVE_OPTION)
		{
			File file = fc.getSelectedFile();
			saveTextalle(file);
		}
}
	
		/**
		 * Eintrag wird mit aktuellem Datum gespeichert
		 * @param file Datei zum lesen fuer den writer
		 */
		private void saveTextalle(File file) 
		{	
			try {
				FileWriter writer =new FileWriter(file);
				
				try {
					Zeitstempel();
					writer.write("Speicherstand: "+dateToStr+"\n"+"\n");
				   } catch (ParseException e) {
					e.printStackTrace();
				}
				
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
		
		/**
		 * Speichern aller offenen ToDos in Liste
		 */
		protected void SpeichernOffene()
		{
			final JFileChooser fc = new JFileChooser();
			int returnVal= fc.showSaveDialog(list);
			
			if(returnVal == JFileChooser.APPROVE_OPTION)
			{
				File file = fc.getSelectedFile();
				saveTextoffene(file);
			}
	}
			
		/**
		 * Speichern aller offenen Eintraege in text
		 * @param file Datei zum lesen fuer den writer
		 */
			private void saveTextoffene(File file) 
			{
				try {
					FileWriter writer =new FileWriter(file);
					
					try {
						Zeitstempel();
						writer.write("Speicherstand: "+dateToStr+"\n"+"\n");
					   } catch (ParseException e) {
						e.printStackTrace();
					}
	
					for (int i=0; i<Eintraege.getSize();i++)
					{
						if (pars.getStatus(eint.get(i))==false)
						{
							String text = Eintraege.get(i)+"\n";
							writer.write(text);
						}
					}
					writer.flush();
					writer.close();
				}
				catch (IOException e){
					e.printStackTrace();
				}
			}
		
		/**
		 * Oeffnet gespeicherte Dateien 
		 */
		protected void Laden()				
		{
			final JFileChooser fc = new JFileChooser();
			int returnVal = fc.showOpenDialog(list);   
			if(returnVal == JFileChooser.APPROVE_OPTION)
			{
				File file = fc.getSelectedFile();
				showText (file);
			}
		}
		
		/**
		 * Liest die Datei Zeilenweise aus
		 * @param file Datei die ausgelesen wird. 
		 */
		private void showText (File file)
		{	
			Drucktext=new ArrayList<>();
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
						Drucktext.add(line);
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
		
		/**
		 * Zum Drucken von Dateien
		 */
		public void drucken(){

			 PrinterJob pj = PrinterJob.getPrinterJob();
			 pj.setJobName(" Drucke Liste ");

			 pj.setPrintable (new Printable() {   
				 /**
				  * Ueberpruefung des zu druckenden Bereichs
				  * @param pg Grafik
				  * @param pf Seitenformat
				  * @param pageNum Seitennummer
				  * @return Printable Ob die Seite existiert oder nicht
				  */
			  public int print(Graphics pg, PageFormat pf, int pageNum){
			   if (pageNum > 0){
			   return Printable.NO_SUCH_PAGE;
			   }
			   
			   Graphics2D g2 = (Graphics2D) pg;
			   g2.translate(pf.getImageableX(), pf.getImageableY());
			   int Zeilenabstand= 10;
			   g2.drawString("To-do Liste:", 50, 110);
			   g2.drawString(Drucktext.get(0),50,70);
			  
			   try {
				Zeitstempel();
				g2.drawString("Gedruckt am: "+dateToStr, 50, 50);
			   } catch (ParseException e) {
				e.printStackTrace();
			}
			   g2.drawString("Datum:         Status:      Eintrag:", 50, 150);
			   for (int i=2;i<Drucktext.size();i++)
			   {
				   
				   if (Drucktext.get(i).length()<75)
				   {
					  g2.drawString(Drucktext.get(i)+"\n", 50, 160+Zeilenabstand); 
					  Zeilenabstand = Zeilenabstand+20;
				   }
				   else
				   { 
					   g2.drawString(Drucktext.get(i).substring(0, 120)+"\n", 50, 100+Zeilenabstand);
					   Zeilenabstand = Zeilenabstand+20;
					   g2.drawString(Drucktext.get(i).substring(121)+"\n", 50, 100+Zeilenabstand);
					   Zeilenabstand = Zeilenabstand+20;
				   }
			   }
			   
			   return Printable.PAGE_EXISTS;
			  }
			 });
			 if (pj.printDialog() == false)
			 return;

			 try {
			    pj.print();
			 } catch (PrinterException ex) {
			    // handle exception
			 }
			}
		
		/**
		 * Zeitstempel wird erstellt
		 * @throws ParseException
		 */
		public void Zeitstempel() throws ParseException {
		{
			DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy   HH:mm");
			date = new Date();        
			dateToStr = dateFormat.format(date);
		}
		}	
	}