package Test;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.ButtonGroup;
import javax.swing.JFileChooser;
import javax.swing.JMenuBar;
import javax.swing.JRadioButton;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import java.awt.Color;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JCheckBox;
import java.awt.ScrollPane;
import java.awt.List;
import javax.swing.JTable;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JRadioButtonMenuItem;
import java.awt.TextArea;
import javax.swing.border.TitledBorder;
import javax.swing.JTextArea;
import javax.swing.border.LineBorder;
import javax.swing.border.EtchedBorder;
import java.awt.SystemColor;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class test extends JFrame {

	private JPanel contentPane;
	private JTextField tfE_eingabe_Name;
	private JRadioButton rbTG11;
	private JRadioButton rbTG12;
	private JRadioButton rbTG13;
	private JCheckBox cbDeutschbuch;
	private JCheckBox cbEnglischbuch;
	private JCheckBox cbMathebuch;
	private JCheckBox cbPhysikbuch;
	private JCheckBox cbChemiebuch;
	private JTextArea taBuecherliste;
	private ButtonGroup gruppeRadioButtons;
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					test frame = new test();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public test() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 583, 545);
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.setBorderPainted(false);
		menuBar.setBackground(Color.BLACK);
		setJMenuBar(menuBar);
		
		JMenu mnDatei = new JMenu("Datei");
		mnDatei.setForeground(Color.WHITE);
		menuBar.add(mnDatei);
		
		JMenuItem mnuDateiSpeichern = new JMenuItem("Speichern");
		mnuDateiSpeichern.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				save();
			}
		});
		mnDatei.add(mnuDateiSpeichern);
		
		JMenuItem mnuDateiOeffnen = new JMenuItem("\u00D6ffnen");
		mnuDateiOeffnen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				load();
			}
		});
		mnDatei.add(mnuDateiOeffnen);
		
		JSeparator separator = new JSeparator();
		mnDatei.add(separator);
		
		JMenuItem mnuDateiBeenden = new JMenuItem("Beenden");
		mnuDateiBeenden.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) 
			{
				beenden();
			}
		});
		mnDatei.add(mnuDateiBeenden);
		
		JMenu mnBearbeiten = new JMenu("Bearbeiten");
		mnBearbeiten.setForeground(Color.WHITE);
		menuBar.add(mnBearbeiten);
		
		JMenuItem mnuBearbeitenListeLoeschen = new JMenuItem("Liste l\u00F6schen");
		mnuBearbeitenListeLoeschen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				ListeLoeschen();
			}
		});
		mnBearbeiten.add(mnuBearbeitenListeLoeschen);
		
		JMenuItem mnuBearbeitenListeErstellen = new JMenuItem("Liste erstellen");
		mnuBearbeitenListeErstellen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				ListeErstellen();
			}
		});
		mnBearbeiten.add(mnuBearbeitenListeErstellen);
		contentPane = new JPanel();
		contentPane.setBackground(SystemColor.menu);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblBcherlisteErstellenFr = new JLabel("B\u00FCcherliste erstellen f\u00FCr Sch\u00FClerin / Sch\u00FCler: ");
		lblBcherlisteErstellenFr.setBounds(22, 29, 458, 37);
		lblBcherlisteErstellenFr.setFont(new Font("MS UI Gothic", Font.BOLD, 16));
		contentPane.add(lblBcherlisteErstellenFr);
		
		tfE_eingabe_Name = new JTextField();
		tfE_eingabe_Name.setBounds(383, 37, 147, 25);
		contentPane.add(tfE_eingabe_Name);
		tfE_eingabe_Name.setColumns(10);
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "Buchwahl", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(29, 255, 178, 169);
		contentPane.add(panel);
		panel.setLayout(null);
		
		cbDeutschbuch = new JCheckBox("Deutschbuch");
		cbDeutschbuch.setBounds(14, 34, 126, 23);
		panel.add(cbDeutschbuch);
		
		cbEnglischbuch = new JCheckBox("Englischbuch");
		cbEnglischbuch.setBounds(14, 58, 126, 23);
		panel.add(cbEnglischbuch);
		
		cbMathebuch = new JCheckBox("Mathebuch");
		cbMathebuch.setBounds(14, 84, 126, 23);
		panel.add(cbMathebuch);
		
		cbPhysikbuch = new JCheckBox("Physikbuch");
		cbPhysikbuch.setBounds(14, 110, 126, 23);
		panel.add(cbPhysikbuch);
		
		cbChemiebuch = new JCheckBox("Chemiebuch");
		cbChemiebuch.setBounds(14, 136, 126, 23);
		panel.add(cbChemiebuch);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(null, "Klasse / Kurs", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_1.setBounds(33, 95, 174, 130);
		contentPane.add(panel_1);
		panel_1.setLayout(null);
		
		rbTG11 = new JRadioButton("TG 11");
		rbTG11.setBounds(6, 20, 90, 23);
		panel_1.add(rbTG11);
		
		rbTG12 = new JRadioButton("TG 12");
		rbTG12.setBounds(6, 59, 90, 23);
		panel_1.add(rbTG12);
		
		rbTG13 = new JRadioButton("TG 13");
		rbTG13.setBounds(6, 100, 90, 23);
		panel_1.add(rbTG13);
		
		ButtonGroup gruppeRadioButtons = new
		ButtonGroup();                        //Gruppe hinzufügen
		
			gruppeRadioButtons.add(rbTG11);
			gruppeRadioButtons.add(rbTG12);
			gruppeRadioButtons.add(rbTG13);
		
		taBuecherliste = new JTextArea();
		taBuecherliste.setEditable(false);
		taBuecherliste.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		taBuecherliste.setText("B\u00FCcherliste von:");
		taBuecherliste.setBounds(277, 95, 253, 329);
		contentPane.add(taBuecherliste);
		
	}
	
	public void beenden()
	//public: Funktion von überall aufrufbar
	//void: Funktion gibt kein Ergebnis zurück
	//beenden: Belibiger Funktionsname
	{
		dispose();		
	}// Programm ende
	
	protected void save()
	{
		final JFileChooser fc = new JFileChooser();
		int returnVal= fc.showSaveDialog(getParent());
		
		if(returnVal == JFileChooser.APPROVE_OPTION){
			File file = fc.getSelectedFile();
			saveText(file);
			}
}
		private void saveText(File file) {
			try {
				FileWriter writer =new FileWriter(file);
				String text = this.taBuecherliste.getText();
				writer.write(text);
				writer.flush();
				writer.close();
			}
			catch (IOException e){
				e.printStackTrace();
			}
		}
	public void load()
	{
		final JFileChooser fc = new JFileChooser();
		int returnVal = fc.showOpenDialog(getParent());
		//jFrame durch getParent() frame ersetzt
		
		if (returnVal==JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();
			showText(file);
		}
	}
	
	private void showText(File file){
		StringBuffer buf = new StringBuffer();
		if (file.exists()){
			try{
				BufferedReader reader = new BufferedReader(new FileReader(file));
				String line = "";
				while ((line = reader.readLine()) != null){
					buf.append(line+"\n");
				}
				reader.close();
			}
			catch ( FileNotFoundException e){
				e.printStackTrace();
			}
			catch (IOException e){
				e.printStackTrace();
			}
		}
		this.taBuecherliste.setText(buf.toString());
	}
					
	
	public void ListeLoeschen()
	{
		taBuecherliste.setText("");
		tfE_eingabe_Name.setText("");
		cbChemiebuch.setSelected(false);
		cbDeutschbuch.setSelected(false);
		cbEnglischbuch.setSelected(false);
		cbMathebuch.setSelected(false);
		cbPhysikbuch.setSelected(false);
		gruppeRadioButtons.clearSelection();
		
	}
	
	public void ListeErstellen()
	{
		
		
		String Eingabe;
		Eingabe = tfE_eingabe_Name.getText();
		taBuecherliste.append("\n"+Eingabe);
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy   hh.mm");  //Datumsformat einstellen
		Timestamp aktuelleZeit = new Timestamp(System.currentTimeMillis()); //Aktuelle Zeit
		String ZeitAusgabe = sdf.format(aktuelleZeit);
		taBuecherliste.append("\n"+"Ausgabedatum: "+ZeitAusgabe);
		taBuecherliste.append(" Uhr");
		
		if(rbTG11.isSelected()==true)
		{
			taBuecherliste.append("\n"+"Eingangsklasse TG 11"+"\n");
		}

		if(rbTG12.isSelected()==true)
		{
			taBuecherliste.append("\n" +"Jahrgangsstufe TG 12"+"\n");
		}
		
		if(rbTG13.isSelected()==true)
		{
			taBuecherliste.append("\n"+"Jahrgangsstufe TG 13"+"\n");
		}
		
		if(cbChemiebuch.isSelected()==true)
		{
			taBuecherliste.append("\n"+"Chemiebuch");
		}
		if(cbDeutschbuch.isSelected()==true)
		{
			taBuecherliste.append("\n"+"Deutschbuch");
		}
		if(cbEnglischbuch.isSelected()==true)
		{
			taBuecherliste.append("\n"+"Englischbuch");
		}
		if(cbMathebuch.isSelected()==true)
		{
			taBuecherliste.append("\n"+"Mathebuch");
		}
		if(cbPhysikbuch.isSelected()==true)
		{
			taBuecherliste.append("\n"+"Physikbuch");
		}
	}
}