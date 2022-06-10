/**
 *
 * @author alex flaiz, tobias aberle
 * @version 1.0.0
 *
 */
package ToDo;


import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.imageio.ImageIO;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
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
import java.awt.image.BufferedImage;
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
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;



public class Fenster {

	private JList<String> list;  // Liste mit ToDos
	private int index; // Index fuer ToDo aus der Liste
	private String Eintrag; // Notiz, ohne Datum oder Status
	private JFrame frmTodoListe; // Fensterdarstellung
	private JTextField tFTagErled; // Textfeld Erledigungstag
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
	private Date date;
	private int counter;
	private BufferedImage logo;

	static DateiHandler Key; // Dateihandler fuer Benutzer-ID
	static DateiHandler Port; // Dateihandler fuer den Benutzer-Port
	static DateiHandler ip; // Dateihandler fuer die IP-Adresse des Benutzers

	/**
	 * Benutzer-ID, Port und IP-Adresse werden aus der jeweiligen Textdatei ausgelesen.
	 */
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
		String IP = ip.read();

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
				/**
				 * Admin-Status wird bei Server abgefragt und in priv gespeichert.
				 */
			public void run() {
				try {
					socket = new java.net.Socket(IP,port);
					schreibeNachricht(socket,authkey);

					String Nachricht="//GETADMIN//\n";
					schreibeNachricht(socket,Nachricht);
					
					String empfangeneNachricht = leseNachricht(socket);
					empfangeneNachricht = leseNachricht(socket);
					empfangeneNachricht = leseNachricht(socket);
					String [] SplidAdmin = empfangeneNachricht.split("//");
					Adminpriv=SplidAdmin[1];
					priv=Boolean.parseBoolean(Adminpriv);

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

		// Ueberschrift der Liste
		JLabel lblListe = new JLabel("Liste");
		lblListe.setHorizontalAlignment(SwingConstants.CENTER);
		lblListe.setFont(new Font("Monotype Corsiva", Font.PLAIN, 50));
		lblListe.setBounds(10, 10, 453, 62);
		Liste.getContentPane().add(lblListe);

		// Liste in GUI schliessen
		JButton btnSchliessen = new JButton("Schlie\u00DFen");
		btnSchliessen.addActionListener(new ActionListener() {
			/**
			 * Liste im GUI schliessen wenn der Button "Schliessen" betaetigt wird
			 * @param e Wenn der Button schliessen betaetigt wird
			 */
			public void actionPerformed(ActionEvent e)
			{
				Liste.setVisible(false);
				Aktualisieren();
			}
		});
		btnSchliessen.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnSchliessen.setBounds(325, 424, 138, 30);
		Liste.getContentPane().add(btnSchliessen);

		// Scrollfunktion
		JScrollPane scrollPane_2 = new JScrollPane();
		scrollPane_2.setBounds(10, 70, 453, 344);
		Liste.getContentPane().add(scrollPane_2);

		// Textfeld-Darstellung
		JTextArea tAListe = new JTextArea();
		tAListe.setFont(new Font("Tahoma", Font.PLAIN, 16));
		tAListe.setEditable(false);
		scrollPane_2.setViewportView(tAListe);

		// Darstellung der ToDo-Liste
		JLabel lblEintraegeListe = new JLabel("Datum | Status | Eintrag");
		lblEintraegeListe.setFont(new Font("Tahoma", Font.PLAIN, 15));
		scrollPane_2.setColumnHeaderView(lblEintraegeListe);

		// Verarbeitung und Darstellung des Drucken-Buttons
		JButton btnDrucken = new JButton("Drucken");
		btnDrucken.addActionListener(new ActionListener() {
			/**
			 * Datei ausdrucken, wenn Button "Drucken" betaetigt wird
			 * @param e Wenn der Button "Drucken" betaetigt wird
			 */
			public void actionPerformed(ActionEvent e)
			{
				drucken();
			}
		});
		btnDrucken.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnDrucken.setBounds(10, 424, 138, 30);
		Liste.getContentPane().add(btnDrucken);
		Liste.setVisible(false);



        //Fenster NeuerEintrag
		JInternalFrame NeuerEintrag = new JInternalFrame("Neuer Eintrag");
		NeuerEintrag.setClosable(true);
		NeuerEintrag.setResizable(true);
		NeuerEintrag.setBounds(31, 22, 729, 458);
		frmTodoListe.getContentPane().add(NeuerEintrag);
		NeuerEintrag.getContentPane().setLayout(null);

		// Darstellung Fenster NeuerEintrag
		JLabel lblNeuerEintrag = new JLabel("Neuer Eintrag");
		lblNeuerEintrag.setHorizontalAlignment(SwingConstants.CENTER);
		lblNeuerEintrag.setFont(new Font("Monotype Corsiva", Font.BOLD, 50));
		lblNeuerEintrag.setBounds(10, 10, 697, 71);
		NeuerEintrag.getContentPane().add(lblNeuerEintrag);

		// Fenster NeuerEintrag schliessen
		JButton btnAbbrechen = new JButton("Abbrechen");
		btnAbbrechen.addActionListener(new ActionListener() {
			/**
			 * Fenster NeuerEintrag wird geschlossen, wenn Button "Abbrechen" betaetigt wird
			 * @param e Wenn Button "Abbrechen" betaetigt wird
			 */
			public void actionPerformed(ActionEvent e)
			{
				NeuerEintrag.setVisible(false);
				Aktualisieren();
			}
		});
		// Darstellung Abbrechen-Button
		btnAbbrechen.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnAbbrechen.setBounds(30, 369, 178, 30);
		NeuerEintrag.getContentPane().add(btnAbbrechen);

		// Darstellung Erledigungsdatum
		JLabel lblErledigungsdatum = new JLabel("Erledigungsdatum");
		lblErledigungsdatum.setHorizontalAlignment(SwingConstants.LEFT);
		lblErledigungsdatum.setFont(new Font("Monotype Corsiva", Font.PLAIN, 25));
		lblErledigungsdatum.setBounds(30, 90, 199, 25);
		NeuerEintrag.getContentPane().add(lblErledigungsdatum);
		
		// Darstellung Textfeld Erledigungstag
		tFTagErled = new JTextField();
		tFTagErled.setHorizontalAlignment(SwingConstants.CENTER);
		tFTagErled.setToolTipText("");
		tFTagErled.setForeground(Color.BLACK);
		tFTagErled.setFont(new Font("Tahoma", Font.PLAIN, 14));
		tFTagErled.setColumns(10);
		tFTagErled.setBounds(30, 141, 60, 30);
		NeuerEintrag.getContentPane().add(tFTagErled);

		// Darstellung Textfeld Erledigungsmonat
		tFMonatErled = new JTextField();
		tFMonatErled.setHorizontalAlignment(SwingConstants.CENTER);
		tFMonatErled.setForeground(Color.BLACK);
		tFMonatErled.setFont(new Font("Tahoma", Font.PLAIN, 14));
		tFMonatErled.setColumns(10);
		tFMonatErled.setBounds(89, 141, 60, 30);
		NeuerEintrag.getContentPane().add(tFMonatErled);

		// Darstellung Textfeld Erledigungsjahr
		tFJahrErled = new JTextField();
		tFJahrErled.setHorizontalAlignment(SwingConstants.CENTER);
		tFJahrErled.setForeground(Color.BLACK);
		tFJahrErled.setFont(new Font("Tahoma", Font.PLAIN, 14));
		tFJahrErled.setColumns(10);
		tFJahrErled.setBounds(148, 141, 60, 30);
		NeuerEintrag.getContentPane().add(tFJahrErled);

		// Darstellung Eintrag
		JLabel lblEintrag = new JLabel("Eintrag");
		lblEintrag.setHorizontalAlignment(SwingConstants.CENTER);
		lblEintrag.setFont(new Font("Monotype Corsiva", Font.PLAIN, 25));
		lblEintrag.setBounds(253, 90, 431, 25);
		NeuerEintrag.getContentPane().add(lblEintrag);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(253, 142, 431, 257);
		NeuerEintrag.getContentPane().add(scrollPane_1);

		// CheckBox fuer Admin-ToDo
		JCheckBox CBAdminTodo = new JCheckBox("Von Benutzer \u00E4nderbar");
		CBAdminTodo.addActionListener(new ActionListener() {
			/**
			 * Entscheidung ob ToDo eine AdminBox hat oder nicht
			 * @param e Ob ToDo von Benutzer aenderbar sein soll oder nicht
			 */
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

		// Darstellung Admin-ToDo Ceckbox
		CBAdminTodo.setFont(new Font("Tahoma", Font.PLAIN, 14));
		CBAdminTodo.setBounds(30, 185, 217, 21);
		NeuerEintrag.getContentPane().add(CBAdminTodo);
		CBAdminTodo.setVisible(false);
		if (priv==true)
		{
		CBAdminTodo.setVisible(true);
		}

		JLabel lblCounter = new JLabel(0+"/150");
		lblCounter.setHorizontalAlignment(SwingConstants.RIGHT);
		lblCounter.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblCounter.setBounds(624, 115, 60, 30);
		NeuerEintrag.getContentPane().add(lblCounter);
		
		// Darstellung Textfeld Eintrag
		JTextArea tAEintrag = new JTextArea();
		tAEintrag.addKeyListener(new KeyAdapter() {
			/**
			 * Zaehlt die eingegebenen Zeichen und gibt die Anzahl im GUI aus.
			 * Maximal 150 Zeichen sind erlaubt.
			 * @param e Wenn eine Taste der Tastatur betaetigt wird
			 */
			public void keyTyped(KeyEvent e)
			{
				counter=tAEintrag.getText().length();         
				if (counter>150)
				{
					lblCounter.setForeground(Color.red);
				}
				else
				{
					lblCounter.setForeground(Color.black);
				}
				lblCounter.setText(counter+"/150");
			}												
		});
		tAEintrag.setFont(new Font("Tahoma", Font.PLAIN, 16));
		tAEintrag.setLineWrap(true);
		tAEintrag.setWrapStyleWord(true);
		scrollPane_1.setViewportView(tAEintrag);

		// Befehl zum Eintrag hinzufuegen
		JButton btnEintragHinzufuegen = new JButton("Eintrag hinzuf\u00FCgen");
		btnEintragHinzufuegen.addActionListener(new ActionListener() {
			/**
			 * Gibt je nach Anzahl der eingegebenen Zeichen fuer Eintrag einen Fehler im GUI aus.
			 * Das eingegebene Erledigungsdatum wird ebenfalls ueberprueft. Bei korrektem Datum wird
			 * das Erledigungsdatum in Da gespeichert.
			 * @param e Wenn Button Eintrag hinzufuegen gedrueckt wurde
			 */
		public void actionPerformed(ActionEvent e) 
		{
			boolean isNumericT = tFTagErled.getText().chars().allMatch( Character::isDigit );
			boolean isNumericM = tFMonatErled.getText().chars().allMatch( Character::isDigit );
			boolean isNumericJ = tFJahrErled.getText().chars().allMatch( Character::isDigit );
			
			if (tAEintrag.getText().length()>150)
			{
				 JOptionPane.showMessageDialog(NeuerEintrag , "Maximal 150 Zeichen möglich" , "Fehler",
 							JOptionPane.ERROR_MESSAGE );
			}
			else if (isNumericT==false||isNumericM==false||isNumericJ==false) 	
			{
				JOptionPane.showMessageDialog(NeuerEintrag , "Fehlerhaftes Datum eingegeben" , "Fehler",
							JOptionPane.ERROR_MESSAGE );
			}																								
			else if (tFTagErled.getText().equals("")||tFMonatErled.getText().equals("")||tFJahrErled.getText().equals("")) 
			{
				JOptionPane.showMessageDialog(NeuerEintrag , "Fehlerhaftes Datum eingegeben" , "Fehler",
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
						JOptionPane.showMessageDialog(NeuerEintrag, "Fehlerhaftes Datum eingegeben", "Fehler",
								JOptionPane.ERROR_MESSAGE ); return;
					}
				Aktualisieren();	
				String inputtext = tAEintrag.getText();      
			    inputtext = inputtext.replace("ä","ae");
			    inputtext = inputtext.replace("ö","oe");
			    inputtext = inputtext.replace("ü","ue");
			    inputtext = inputtext.replace("/", "><$*+");
			    inputtext = inputtext.replace(":", "><$*-");
				Eintrag=inputtext;							
				NeuEintrag(Datum);
				NeuerEintrag.setVisible(false);
				leeren();
				tAEintrag.setText("");
				lblCounter.setText(counter+"/150");
			}
		}
		});
		// Darstellung Button Eintrag Hinzufuegen
		btnEintragHinzufuegen.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnEintragHinzufuegen.setBounds(30, 237, 178, 30);
		NeuerEintrag.getContentPane().add(btnEintragHinzufuegen);

		// Leert Eintraege in Fenster Neuer Eintrag
		JButton btnLeeren = new JButton("Leeren");
		btnLeeren.addActionListener(new ActionListener() {
			/**
			 * Wenn der Button "Leeren" geklickt wird, wird der Eintrag geleert
			 * @param e Wenn der "Loeschen" Button betaetigt wird
			 */
			public void actionPerformed(ActionEvent e)
			{
				tAEintrag.setText("");
				leeren();
				CBAdminTodo.setSelected(false);
				lblCounter.setText(counter+"/150");
			}
		});

		// Darstellung Button Leeren
		btnLeeren.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnLeeren.setBounds(30, 277, 178, 30);
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



		// Fenster mit ToDo-Liste
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 93, 768, 375);
		frmTodoListe.getContentPane().add(scrollPane);

		// Index des ausgewaehlten ToDos
		Eintraege=new DefaultListModel<>();
		JList<String> list = new JList<>(Eintraege);
		list.addMouseListener(new MouseAdapter() {
			/**
			 * Wenn die Maus im Bereich Eintraege geklickt wird,
			 * wird das ToDo mit dem ToDo mit gleichem Index vom Server verglichen.
			 * Wenn die Indizes uebereinstimmen, wird der Index gespeichert.
			 * Anderenfalls wird die Liste aktualisiert und der Nutzer wird aufgefordert,
			 * den Eintrag erneut auszuwaehlen.
			 * @param e Wenn die Maus im Bereich Eintraege betaetigt wird
			 */
			public void mouseClicked(MouseEvent e)
			{
				int i=list.getSelectedIndex();
				String a= eint.get(i);
				eint.clear();
				getTodos(socket);
				String b=eint.get(i);

				if (a.equals(b))
				{
					index=list.getSelectedIndex();
				}
				else
				{
					Aktualisieren();
					JOptionPane.showMessageDialog(frmTodoListe , "Liste wurde Aktualisiert, bitte Eintrag erneut auswählen" , "Achtung",
	 							JOptionPane.INFORMATION_MESSAGE);
				}
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
		btnNeuerEintrag.setBounds(10, 502, 200, 31);
		frmTodoListe.getContentPane().add(btnNeuerEintrag);
		btnNeuerEintrag.setFont(new Font("Tahoma", Font.PLAIN, 18));

		// Darstellung Button Loeschen
		JButton btnLoeschen = new JButton("L\u00F6schen");
		btnLoeschen.setBounds(638, 502, 140, 31);
		frmTodoListe.getContentPane().add(btnLoeschen);
		btnLoeschen.addActionListener(new ActionListener() {
			/**
			 * Wenn ein ToDo geloescht werden soll wird die Berechtigung des Nutzers
			 * zum loeschen des ToDos ueberprueft.
			 * Ist der Nutzer nicht berechtigt, wird eine Fehlermeldung ausgegeben.
			 * Hat der Nutzer die Berechtigung wird das ToDo auf dem Server geloescht und die GUI wird aktualisiert
			 * @param e Wenn der Button Loeschen betaetigt wird
			 */
			public void actionPerformed(ActionEvent e) 
			{
				if (index==-1)
				{
					JOptionPane.showMessageDialog(frmTodoListe , "Es wurde kein Eintrag ausgewählt" , "Achtung",
 							JOptionPane.INFORMATION_MESSAGE );
				}
				else 
				{
                 if (pars.getAdmin(eint.get(index))==true && priv==false)
 				{
                	 JOptionPane.showMessageDialog(frmTodoListe , "Eintrag wurde nicht zum löschen freigegeben." , "Warnung",
 							JOptionPane.WARNING_MESSAGE );
 				}
				else 
				{
				ImageIcon icon = new ImageIcon("Bilder/mulleimer.png");
				int response = JOptionPane.showConfirmDialog(frmTodoListe, "Soll der Eintrag wirklich gelöscht werden?  " + "","Eintrag löschen",
							JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE,icon);         
					
					if (response==JOptionPane.YES_OPTION) 
					{
						loescheTodo(socket,(index+1));
						Aktualisieren();
					}
				}
			}	
		}									
		});
		btnLoeschen.setFont(new Font("Tahoma", Font.PLAIN, 18));

		// Darstellung der ToDo-Liste
		JLabel lblToDoList = new JLabel("To-do Liste");
		lblToDoList.setHorizontalAlignment(SwingConstants.CENTER);
		lblToDoList.setBounds(0, 22, 788, 65);
		frmTodoListe.getContentPane().add(lblToDoList);
		lblToDoList.setFont(new Font("Monotype Corsiva", Font.BOLD, 50));

		JButton btnerledigt = new JButton("Erledigt");
		btnerledigt.addActionListener(new ActionListener() {
			/**
			 * Wenn der Button Erledigt betaetigt wird, wird die Berechtigung des Nutzers
			 * zum veraendern des Erledigungsstatus ueberprueft. Bei nicht vorhandener
			 * Berechtigung wird eine Fehlermeldung ausgegeben,
			 * bei vorhandener Berechtigung wird der Erledigungsstatus geandert.
			 * @param e Wenn der Button Erledigt betaetigt wird
			 */
			public void actionPerformed(ActionEvent e) 
			{
				if (index==-1)						
				{
					JOptionPane.showMessageDialog(frmTodoListe , "Es wurde kein Eintrag ausgewählt" , "Achtung",
 							JOptionPane.INFORMATION_MESSAGE );
				}
				else
				{
					int i=index;
					aendereStatus();
					if (pars.getAdmin(eint.get(i))==true && priv==false)
					{
					JOptionPane.showMessageDialog(frmTodoListe , "Eintrag wurde nicht zur Bearbeitung freigegeben." , "Warnung",
							JOptionPane.WARNING_MESSAGE );
					}
				}
		}
		});
		// Darstellung Button Erledigt
		btnerledigt.setFont(new Font("Tahoma", Font.PLAIN, 18));
		btnerledigt.setBounds(488, 502, 140, 31);
		frmTodoListe.getContentPane().add(btnerledigt);

		// Darstellung Admin-Anzeige. Wird nur angezeigt, wenn der Nutzer Admin-Rechte hat.
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
		
		//Erstellt HFU Logo
		try {															
			JLabel lblLogo = new JLabel();								
			lblLogo.setHorizontalAlignment(SwingConstants.RIGHT);
			logo = ImageIO.read(new File("Bilder/HFU-Logo.png"));
			lblLogo.setIcon(new ImageIcon(logo));
			lblLogo.setBounds(563, 10, 213, 78);
			frmTodoListe.getContentPane().add(lblLogo);	
		} catch (IOException e1) {
			e1.printStackTrace();
		}		
		
		btnNeuerEintrag.addActionListener(new ActionListener() {
			/**
			 * Wenn der Button NeuerEintrag betaetigt wird, werden die ToDos aktualisiert
			 * und das Fenster Neuer Eintrag oeffnet sich.
			 * @param e Wenn der Button NeuerEintrag betaetigt wird
			 */
			public void actionPerformed(ActionEvent e)
			{
				Aktualisieren();
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
			/**
			 * Wenn ueber die Menueleiste auf NeuerEintrag geklickt wird,
			 * soll sich das Fenster aktualisieren und sich oeffnen
			 * @param e Wenn Menuepunkt NeuerEintrag betaetigt wird
			 */
			public void actionPerformed(ActionEvent e)
			{
				Aktualisieren();
				NeuerEintrag.setVisible(true);
			}
		});
		mnDatei.add(mntmNeuerEintrag);

		JMenuItem mntmOeffnen = new JMenuItem("\u00D6ffnen");
		mntmOeffnen.addActionListener(new ActionListener() {
			/**
			 * Wenn in der Menueleiste auf Oeffnen geklickt wird sollen die offenen ToDos angezeigt werden.
			 * @param e Wenn Menuepunkt Oeffnen betaetigt wird
			 */
			public void actionPerformed(ActionEvent e) {
				Laden();
				tAListe.setText(offeneListe);
				Liste.setVisible(true);
				Aktualisieren();
			}
		});

		// Menuepunkt zum Speichern
		JMenu mnSpeichern = new JMenu("Speichern");
		mnDatei.add(mnSpeichern);

		// Unterpunkt "Alle Eintraege" im Menuepunkt "Speichern"
		JMenuItem mntmAlle = new JMenuItem("Alle Eintr\u00E4ge");
		mntmAlle.addActionListener(new ActionListener() {
			/**
			 * Alle Eintraege speichern, wenn im Menue unter "Speichern" "Alle Eintrage" geklickt wird
			 * @param e Wenn im Menue unter "Speichern" "Alle Eintraege" geklickt wird.
			 */
			public void actionPerformed(ActionEvent e)
			{
				Aktualisieren();
				SpeichernAlle();
			}
		});
		mnSpeichern.add(mntmAlle);

		// Unterpunkt "Offene Eintraege" im Menuepunkt "Speichern"
		JMenuItem mntmOffene = new JMenuItem("Offene Eintr\u00E4ge");
		mntmOffene.addActionListener(new ActionListener() {
			/**
			 * Offene Eintraege speichern, wenn im Menue unter "Speichern" "Alle Eintraege" geklickt wird
			 * @param e Wenn im Menue unter "Speichern" "Offene Eintraege" geklickt wird.  */
			public void actionPerformed(ActionEvent e)
			{
				Aktualisieren();
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
			/**
			 * Wenn im Menue der Punkt "Beenden" ausgewaehlt wird, schliesst sich die Anwendung
			 * @param e Wenn im Menue "Beenden" geklickt wird
			 */
			public void actionPerformed(ActionEvent e)
			{
				System.exit(0);
			}
		});
		mnDatei.add(mntmBeenden);

		JMenu mnListe = new JMenu("Liste");
		menuBar.add(mnListe);

		// Liste wird Aktualisiert
		JMenuItem mntmAktualisieren = new JMenuItem("Aktualisieren");
		mntmAktualisieren.addActionListener(new ActionListener() {
			/**
			 * Wenn im Menue der Punkt "Aktualisieren" ausgewaehlt wird, werden die ToDos aktualisiert
			 * @param e Wenn im Menue "Aktualisieren" geklickt wird
		     */
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
		counter=0;
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

	 /** Nachricht vom Server wird gelesen
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
				if (!empfangeneNachricht.contains("//END//"))
				{
				schreibeNachricht(socket, "//OK//\n");
				}
			}
	}

	/**
	 * Neue ToDos werden an Server geschickt.
	 * @param socket
	 * @param Datum Erledigungsdatum String dd-mm-yyyy
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
		index=-1;
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
		 * @param file Datei zum lesen fuer den FileWriter
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
		 	 * @param file Datei zum lesen fuer den FileWriter
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
			   g2.drawString("To-do Liste:", 45, 110);
			   g2.drawString(Drucktext.get(0),45,70);

			   try {
				Zeitstempel();
				g2.drawString("Gedruckt am: "+dateToStr, 45, 50);
			   } catch (ParseException e) {
				e.printStackTrace();
			}
			   // Einstellungen wie die Datei auf dem Blatt aussieht
			   g2.drawString("Datum:         Status:      Eintrag:", 45, 140);
			   g2.drawString("-------------------------------------------", 45, 150);
			   for (int i=2;i<Drucktext.size();i++)
			   {

				   if (Drucktext.get(i).length()<85)
				   {
					  g2.drawString(Drucktext.get(i)+"\n", 45, 160+Zeilenabstand);
				   }
				   else if(Drucktext.get(i).length()>85 && Drucktext.get(i).length()<143)
				   {
					   g2.drawString(Drucktext.get(i).substring(0, 85)+"\n", 45, 160+Zeilenabstand);
					   Zeilenabstand = Zeilenabstand+20;
					   g2.drawString("                                      "+Drucktext.get(i).substring(85)+"\n", 45, 160+Zeilenabstand);
				   }
				   else
				   {
					   g2.drawString(Drucktext.get(i).substring(0, 85)+"\n", 45, 160+Zeilenabstand);
					   Zeilenabstand = Zeilenabstand+20;
					   g2.drawString("                                      "+Drucktext.get(i).substring(85, 143)+"\n", 45, 160+Zeilenabstand);
					   Zeilenabstand = Zeilenabstand+20;
					   g2.drawString("                                      "+Drucktext.get(i).substring(143)+"\n", 45, 160+Zeilenabstand);
				   }
				   Zeilenabstand = Zeilenabstand+20;
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
