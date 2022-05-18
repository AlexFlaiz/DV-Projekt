package Test;

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





public class Fenster {
	

	private  int index;
	private String neuEintrag;
	private String Eintrag;
	private String TagErled;
	private String MonatErled;
	private String JahrErled;
	private String Ein;
	
	
	private JFrame frmTodoListe;
	private JTextField tFTagErled;
	private JTextField tFMonatErled;
	private JTextField tFJahrErled;
	private boolean Status;
	private static java.net.Socket socket;
	private static String authkey;
	
	static ArrayList <String> eint;
	DefaultListModel<String>Eintraege;
	static BufferedReader bufferedReader;
	static PrintWriter printWriter;
	static String foo;
	Parser pars=new Parser();
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		int port = 1112;
		String ip = "127.0.0.1";
		authkey= "veryGoodAdminAuthKey";
		eint=new ArrayList<>();
		
		try {
	
			socket = new java.net.Socket(ip,port);
			schreibeNachricht(socket,authkey);
			foo=leseNachricht(socket);
			getTodos(socket);
			
			
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return;
		}
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Fenster window = new Fenster();
					window.frmTodoListe.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Fenster() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmTodoListe = new JFrame();
		frmTodoListe.setTitle("To-do Liste");
		frmTodoListe.setBounds(100, 100, 802, 631);
		frmTodoListe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frmTodoListe.getContentPane().setLayout(null);
		
		
        
		
        
        //Fenster Neuer Eintrag
        
		JInternalFrame NeuerEintrag = new JInternalFrame("Neuer Eintrag");
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
		
		JButton btnEintragHinzufuegen = new JButton("Eintrag hinzuf\u00FCgen");
		btnEintragHinzufuegen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				Ein=tAEintrag.getText();
				NeuEintrag();
			
				NeuerEintrag.setVisible(false);
			}
		});
		btnEintragHinzufuegen.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnEintragHinzufuegen.setBounds(30, 409, 178, 30);
		NeuerEintrag.getContentPane().add(btnEintragHinzufuegen);
		
		JButton btnLeeren = new JButton("Leeren");
		btnLeeren.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				leeren();
				tAEintrag.setText("");
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
		
		
		
		
		
		//Fenster mit To Do Liste
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 93, 768, 375);
		frmTodoListe.getContentPane().add(scrollPane);
		
		Eintraege=new DefaultListModel<>();
		JList<String> list = new JList<>(Eintraege);
		list.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) 
			{
				index= list.getSelectedIndex();
				//System.out.print(index);
			}
		});
		scrollPane.setViewportView(list);
		list.setFont(new Font("Tahoma", Font.PLAIN, 16));
		
		JLabel Eintraege = new JLabel("Eintrag | Erledigungsdatum | Status");
		scrollPane.setColumnHeaderView(Eintraege);
		Eintraege.setFont(new Font("Tahoma", Font.PLAIN, 18));
		
		
		JButton btnNeuerEintrag = new JButton("Neuer Eintrag");
		btnNeuerEintrag.setBounds(37, 511, 151, 31);
		frmTodoListe.getContentPane().add(btnNeuerEintrag);
		btnNeuerEintrag.setFont(new Font("Tahoma", Font.PLAIN, 18));
		

		JButton btnLoeschen = new JButton("l\u00F6schen");
		btnLoeschen.setBounds(626, 513, 117, 31);
		frmTodoListe.getContentPane().add(btnLoeschen);
		btnLoeschen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
                  
				if(authkey.equals("veryGoodAdminAuthKey"))
				{
	              
	int response = JOptionPane.showConfirmDialog(btnLoeschen, "Soll der Eintrag wirklich gelöscht werden?  " + "","Eintrag löschen", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
	if (response==JOptionPane.YES_OPTION) 
	{
		
		int i=index+1;
		loescheTodo(socket,i);
		
		//Aktualisieren();
	}
				}		
				else
				{
					JOptionPane.showMessageDialog(btnLoeschen , "Sie haben keine Admin Rechte" , "Fehler",
							JOptionPane.ERROR_MESSAGE );
				}
				

				//String i=Integer.toString(index+1);
				//loescheTodo(socket,i);	
			}		
		});
		btnLoeschen.setFont(new Font("Tahoma", Font.PLAIN, 18));
		
		
		
		JLabel lblToDoList = new JLabel("To-do Liste");
		lblToDoList.setHorizontalAlignment(SwingConstants.CENTER);
		lblToDoList.setBounds(0, 22, 788, 65);
		frmTodoListe.getContentPane().add(lblToDoList);
		lblToDoList.setFont(new Font("Monotype Corsiva", Font.BOLD, 50));
		
		JButton btnerledigt = new JButton("erledigt");
		btnerledigt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				aendereNachricht(index);
			}
		});
		btnerledigt.setFont(new Font("Tahoma", Font.PLAIN, 18));
		btnerledigt.setBounds(499, 513, 117, 31);
		frmTodoListe.getContentPane().add(btnerledigt);
		
		btnNeuerEintrag.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				NeuerEintrag.setVisible(true);
			}
		});
		
		JMenuBar menuBar = new JMenuBar();
		frmTodoListe.setJMenuBar(menuBar);
		
		JMenu mnDatei = new JMenu("Datei");
		menuBar.add(mnDatei);
		
		JMenuItem mntmBeenden = new JMenuItem("Beenden");
		mntmBeenden.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				System.exit(0);
			}
		});
		mnDatei.add(mntmBeenden);
		
		JMenuItem mntmNeuerEintrag = new JMenuItem("Neuer Eintrag");
		mntmNeuerEintrag.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				NeuerEintrag.setVisible(true);
			}
		});
		mnDatei.add(mntmNeuerEintrag);
		
		
		JMenu mnListe = new JMenu("Liste");
		menuBar.add(mnListe);
		
		JMenuItem mntmAktualisieren = new JMenuItem("Aktualisieren");
		mntmAktualisieren.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				Aktualisieren();
			}
		});
		mnListe.add(mntmAktualisieren);
		
		JMenuItem mntmListeLeeren = new JMenuItem("Liste Leeren");
		mntmListeLeeren.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				loescheListe();
			}
		});
		mnListe.add(mntmListeLeeren);
		
		JMenuItem mntmgetListServer = new JMenuItem("Liste vom Server");
		mntmgetListServer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				
					getTodos(socket);
			
			}
		});
		mnListe.add(mntmgetListServer);
	
	
	}
	
	public String getEintraege()
	{
		return Eintrag;
	}
	
	
	public String getErledigt()
	{
		String Erledigt= TagErled +"."+MonatErled+"."+JahrErled;
		return Erledigt;
	}
	
	public void Eintrag()
	{
		neuEintrag = getErledigt()+"    "+getEintraege(); 
	}
	
	
	public void NeuEintrag() 
	{
	
		int Ter,Mer,Jer;
		
		Ter= Integer.parseInt(tFTagErled.getText());
		Mer= Integer.parseInt(tFMonatErled.getText());
		Jer=Integer.parseInt(tFJahrErled.getText());
		
		
		if (Ter>0 && Ter<32)
		{	
			if (Ter<10)
			{
				TagErled= "0" + tFTagErled.getText();
			}	
			else
			{
				TagErled= tFTagErled.getText();
			}
		}
		else
		{
			JOptionPane.showMessageDialog(null, "Fehlerhafte Eingabe: Tag", "Fehler",JOptionPane.ERROR_MESSAGE );
			return;
		}
		
		
		if (Mer>0 && Mer<13)
		{	
			if (Ter<10)
			{
				MonatErled="0"+tFMonatErled.getText();
			}	
			else
			{
				MonatErled=tFMonatErled.getText();
			}
			
		}
		else
		{
			JOptionPane.showMessageDialog(null, "Fehlerhafte Eingabe: Monat", "Fehler", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		
		if (Jer>2021 && Jer<2100)
		{
			JahrErled=tFJahrErled.getText();
		}
		else
		{
			JOptionPane.showMessageDialog(null, "Fehlerhafte Eingabe: Jahr", "Fehler", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		Eintrag=Ein;
		Eintrag();
		Status = false;
		
		sendTodos(socket,TagErled,MonatErled,JahrErled,Eintrag, Status);
		//Aktualisieren();
	}
	
	public void leeren()
	{
		
		tFTagErled.setText("");
		tFMonatErled.setText("");
		tFJahrErled.setText("");
		
	}
	
	
	 static void schreibeNachricht(java.net.Socket socket, String nachricht)  {
		
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
	
	
	static String leseNachricht(java.net.Socket socket)  {
	
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
	
	
	public static void getTodos(java.net.Socket socket) 
	{ 
		
		
		schreibeNachricht(socket,"/UPDATE/\n");
		String empfangeneNachricht = leseNachricht(socket);
			
			
			while(!empfangeneNachricht.contains("/END/"))
			{
			empfangeneNachricht = leseNachricht(socket);
			eint.add(empfangeneNachricht);
			System.out.println(empfangeneNachricht);
			
		}
	
	}
	
		
	
	
	public static void sendTodos(java.net.Socket socket, String t,String m,String j, String Eintrag, boolean S)
	{
		String Nachricht;
		if (authkey.equals("veryGoodAdminAuthKey")) 
		{
			Nachricht="/INSERT/"+Eintrag+"/"+t+"-"+m+"-"+j+"/"+S+"/"+true+"\n";
		}
		else
		{
		Nachricht="/INSERT/"+Eintrag+"/"+t+"-"+m+"-"+j+"/"+S+"/"+false+"\n";
		}
		schreibeNachricht(socket,Nachricht);
		foo=leseNachricht(socket);
	}
	
	public static void loescheTodo(java.net.Socket socket,int i)
	{
		
		String Nachricht="/DELETE/"+i+"\n";
		schreibeNachricht(socket,Nachricht);
		foo=leseNachricht(socket);
		
	
	}
	
	public void fuelleListe()
	{
		
	
		for (int i=0; i<(eint.size()-1);i++)
		{
			
			
			String Notiz= pars.setStatus(eint.get(i), pars.getStatus(eint.get(i)));
			
			Eintraege.addElement(Notiz);
			
			
			
		}
		 //Liste in GUI neu befüllen 
	}
	
	public void loescheListe()
	{
		
		Eintraege.clear();
		eint.clear();
		
	}
	
	public void Aktualisieren()
	{
		//loescheListe();
		
		//getTodos(socket);
		
		fuelleListe();
	}
	
	public void aendereNachricht(int i)
	{
		
		String Notiz=eint.get(i);
		boolean Status=pars.getNewStatus(Notiz);
		
		String teilstr[];
		teilstr = Notiz.split(",");
		String Eintrag= teilstr[1];
		loescheListe();
		sendTodos(socket,pars.getTag(Notiz),pars.getMonat(Notiz),pars.getJahr(Notiz),Eintrag, Status);
		getTodos(socket);
		fuelleListe();
		
	}
}

