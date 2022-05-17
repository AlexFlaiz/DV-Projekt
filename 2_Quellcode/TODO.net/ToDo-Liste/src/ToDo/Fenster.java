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




public class Fenster {
	

	private int index;
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
	
	
	DefaultListModel<String>Eintraege;

	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
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
			}
		});
		scrollPane.setViewportView(list);
		list.setFont(new Font("Tahoma", Font.PLAIN, 16));
		
		JLabel Eintraege = new JLabel("Erledigungsdatum | Eintrag");
		scrollPane.setColumnHeaderView(Eintraege);
		Eintraege.setFont(new Font("Tahoma", Font.PLAIN, 18));
		
		
		JButton btnNeuerEintrag = new JButton("Neuer Eintrag");
		btnNeuerEintrag.setBounds(37, 511, 151, 31);
		frmTodoListe.getContentPane().add(btnNeuerEintrag);
		btnNeuerEintrag.setFont(new Font("Tahoma", Font.PLAIN, 18));
		

		JButton btnErledigt = new JButton("erledigt");
		btnErledigt.setBounds(613, 511, 130, 31);
		frmTodoListe.getContentPane().add(btnErledigt);
		btnErledigt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				   
				String selectedElem = "";
	                int selectedIndices[] = list.getSelectedIndices();
	                for (int j = 0; j < selectedIndices.length; j++) {
	                   String elem =
	                            (String) list.getModel().getElementAt(selectedIndices[j]);
	                  selectedElem += "\n" + elem;
	                    
	                }
	              
				
			//	JOptionPane.showMessageDialog(null,"Du hast " + selectedElem+ " ausgewählt!");
	int response = JOptionPane.showConfirmDialog(btnErledigt, "Ist der Eintrag erledigt?  " + "' "+Eintrag+" '", "To-do Erledigt", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
		
	if (response==JOptionPane.YES_OPTION) 
	{
		//String Str=Fenster.this.Eintraege.get(index);
		Fenster.this.Eintraege.remove(index);
		//erledigt=true;
		//Fenster.this.Eintraege.add(index, Str);
	}
	

				
			}
		});
		btnErledigt.setFont(new Font("Tahoma", Font.PLAIN, 18));
		
		
		
		JLabel lblToDoList = new JLabel("To-do Liste");
		lblToDoList.setHorizontalAlignment(SwingConstants.CENTER);
		lblToDoList.setBounds(0, 22, 788, 65);
		frmTodoListe.getContentPane().add(lblToDoList);
		lblToDoList.setFont(new Font("Monotype Corsiva", Font.BOLD, 50));
		
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
		
		JMenuItem mntmNeuerEintrag = new JMenuItem("Neuer Eintrag");
		mntmNeuerEintrag.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				NeuerEintrag.setVisible(true);
			}
		});
		mnDatei.add(mntmNeuerEintrag);
		mnDatei.add(mntmBeenden);
	
	
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
		//neuEintrag = getErledigt()+"   "+getStatus()+"     "+getEintraege(); 
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
		TagErled= tFTagErled.getText();
		}
		else
		{
			JOptionPane.showMessageDialog(null, "Fehlerhafte Eingabe: Tag", "Fehler",JOptionPane.ERROR_MESSAGE );
			return;
		}
		
		
		if (Mer>0 && Mer<13)
		{		
			MonatErled=tFMonatErled.getText();
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
		
		Eintraege.addElement(neuEintrag);
	
	}
	
	public void leeren()
	{
		
		tFTagErled.setText("");
		tFMonatErled.setText("");
		tFJahrErled.setText("");
		
	}
	

	
	public int getTag()
	{
		return Integer.parseInt(tFTagErled.getText());
	}
	public int getMonat()
	{
		return Integer.parseInt(tFMonatErled.getText());
	}
	public int getJahr()
	{
		return Integer.parseInt(tFJahrErled.getText());
	}
	
	
	
	
}

