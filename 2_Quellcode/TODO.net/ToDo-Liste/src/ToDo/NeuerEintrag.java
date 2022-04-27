package ToDo;


import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import java.awt.Color;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;


public class NeuerEintrag {
	
	private String neuEintrag;
	
	private String Eintrag;
	private String TagE;
	private String MonatE;
	private String JahrE;
	private String TagErled;
	private String MonatErled;
	private String JahrErled;
	private String Ein;
	
	private JFrame frame;
	private JTextField tFTagErst;
	private JTextField tFMonatErst;
	private JTextField tFJahrErst;
	private JTextField tFTagErled;
	private JTextField tFMonatErled;
	private JTextField tFJahrErled;
	
	ArrayList <String> listEintraege=new ArrayList<String>();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					NeuerEintrag window = new NeuerEintrag();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public NeuerEintrag() {
		initialize();
	}
	
	/**
	 * Initialize the contents of the frame.
	 */
	public void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 745, 484);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblNeuerEintrag = new JLabel("Neuer Eintrag");
		lblNeuerEintrag.setFont(new Font("Tahoma", Font.PLAIN, 34));
		lblNeuerEintrag.setBounds(246, 20, 225, 58);
		frame.getContentPane().add(lblNeuerEintrag);
		
		tFTagErst = new JTextField();
		tFTagErst.setForeground(Color.LIGHT_GRAY);
		tFTagErst.setFont(new Font("Tahoma", Font.PLAIN, 15));
		tFTagErst.setText("Tag");
		tFTagErst.setToolTipText("");
		tFTagErst.setBounds(50, 130, 60, 30);
		frame.getContentPane().add(tFTagErst);
		tFTagErst.setColumns(10);
		
		tFMonatErst = new JTextField();
		tFMonatErst.setFont(new Font("Tahoma", Font.PLAIN, 15));
		tFMonatErst.setForeground(Color.LIGHT_GRAY);
		tFMonatErst.setText("Monat");
		tFMonatErst.setColumns(10);
		tFMonatErst.setBounds(109, 130, 60, 30);
		frame.getContentPane().add(tFMonatErst);
		
		tFJahrErst = new JTextField();
		tFJahrErst.setForeground(Color.LIGHT_GRAY);
		tFJahrErst.setFont(new Font("Tahoma", Font.PLAIN, 15));
		tFJahrErst.setText("Jahr");
		tFJahrErst.setColumns(10);
		tFJahrErst.setBounds(168, 130, 60, 30);
		frame.getContentPane().add(tFJahrErst);
		
		JLabel lblErstellung = new JLabel("Erstellung");
		lblErstellung.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblErstellung.setBounds(50, 95, 178, 25);
		frame.getContentPane().add(lblErstellung);
		
		
		JButton btnAbbrechen = new JButton("Abbrechen");
		btnAbbrechen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				System.exit(0);
			}
		});
		btnAbbrechen.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnAbbrechen.setBounds(507, 398, 178, 30);
		frame.getContentPane().add(btnAbbrechen);
		
		JLabel lblErledigungsdatum = new JLabel("Erledigungsdatum");
		lblErledigungsdatum.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblErledigungsdatum.setBounds(319, 95, 178, 25);
		frame.getContentPane().add(lblErledigungsdatum);
		
		tFTagErled = new JTextField();
		tFTagErled.setToolTipText("");
		tFTagErled.setText("Tag");
		tFTagErled.setForeground(Color.LIGHT_GRAY);
		tFTagErled.setFont(new Font("Tahoma", Font.PLAIN, 15));
		tFTagErled.setColumns(10);
		tFTagErled.setBounds(319, 130, 60, 30);
		frame.getContentPane().add(tFTagErled);
		
		tFMonatErled = new JTextField();
		tFMonatErled.setText("Monat");
		tFMonatErled.setForeground(Color.LIGHT_GRAY);
		tFMonatErled.setFont(new Font("Tahoma", Font.PLAIN, 15));
		tFMonatErled.setColumns(10);
		tFMonatErled.setBounds(378, 130, 60, 30);
		frame.getContentPane().add(tFMonatErled);
		
		tFJahrErled = new JTextField();
		tFJahrErled.setText("Jahr");
		tFJahrErled.setForeground(Color.LIGHT_GRAY);
		tFJahrErled.setFont(new Font("Tahoma", Font.PLAIN, 15));
		tFJahrErled.setColumns(10);
		tFJahrErled.setBounds(437, 130, 60, 30);
		frame.getContentPane().add(tFJahrErled);
		
		JLabel lblEintrag = new JLabel("Eintrag");
		lblEintrag.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblEintrag.setBounds(50, 174, 80, 25);
		frame.getContentPane().add(lblEintrag);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(50, 209, 633, 174);
		frame.getContentPane().add(scrollPane);
		
		JTextArea tAEintrag = new JTextArea();
		scrollPane.setViewportView(tAEintrag);

		JButton btnEintragHinzufuegen = new JButton("Eintrag hinzuf\u00FCgen");
		btnEintragHinzufuegen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				Ein=tAEintrag.getText();
				NeuEintrag();
				
				for (int i=0; i<listEintraege.size();i++)
				{
				System.out.println(listEintraege.get(i));
				}
			}
				
		});
		btnEintragHinzufuegen.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnEintragHinzufuegen.setBounds(50, 398, 178, 30);
		frame.getContentPane().add(btnEintragHinzufuegen);
		
		JButton btnLeeren = new JButton("Leeren");
		btnLeeren.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				leeren();
				tAEintrag.setText("");
			}
		});
		btnLeeren.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnLeeren.setBounds(238, 398, 120, 30);
		frame.getContentPane().add(btnLeeren);
		
	}
	
	public String getEintraege()
	{
		return Eintrag;
	}
	
	
	
	
	public String getErstellung()
	{
	
	String Erstellung= TagE +"."+MonatE+"."+JahrE;
	
		return Erstellung;	
		
	}
	
	public String getErledigt()
	{
		String Erledigt= TagErled +"."+MonatErled+"."+JahrErled;
		return Erledigt;
	}
	
	public void Eintrag()
	{
		neuEintrag = "Erstellungsdatum: "+getErstellung()+"   "+"Erledigungsdatum: "+getErledigt()+"   "+"Eintrag: "+getEintraege(); 
	}
	
	
	public void NeuEintrag() 
	{
	
		int TE,Ter,ME,Mer,JE,Jer;
		
		TE= Integer.parseInt(tFTagErst.getText());
		Ter= Integer.parseInt(tFTagErled.getText());
		ME= Integer.parseInt(tFMonatErst.getText());
		Mer= Integer.parseInt(tFMonatErled.getText());
		JE=Integer.parseInt(tFJahrErst.getText());
		Jer=Integer.parseInt(tFJahrErled.getText());
		
		
		if (TE>0 && Ter>0 && TE<32 && Ter<32)
		{		
		TagE= tFTagErst.getText();
		TagErled= tFTagErled.getText();
		}
		else
		{
			JOptionPane.showMessageDialog(null, "Fehlerhafte Eingabe: Tag", "Fehler",JOptionPane.ERROR_MESSAGE );
			return;
		}
		
		
		if (ME>0 && Mer>0 && ME<13 && Mer<13)
		{		
			MonatE=tFMonatErst.getText();
			MonatErled=tFMonatErled.getText();
		}
		else
		{
			JOptionPane.showMessageDialog(null, "Fehlerhafte Eingabe: Monat", "Fehler", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		
		if (JE>2021 && Jer>2021 && JE<2100 && Jer<2100)
		{
			JahrE=tFJahrErst.getText();
			JahrErled=tFJahrErled.getText();
		}
		else
		{
			JOptionPane.showMessageDialog(null, "Fehlerhafte Eingabe: Jahr", "Fehler", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		Eintrag=Ein;
		Eintrag();
		
		listEintraege.add(neuEintrag);
		
	}
	
	public void leeren()
	{
		tFTagErst.setText("");
		tFTagErled.setText("");
		tFMonatErst.setText("");
		tFMonatErled.setText("");
		tFJahrErst.setText("");
		tFJahrErled.setText("");
		
	}
}
