package Installer;


import java.awt.EventQueue;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.SwingConstants;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

public class TODO_Installer {

	private JFrame frmTodoInstaller;
	private BufferedImage logo;
	private JTextField tFSchluessel;
	private JTextField tFIPAdress;
	private JTextField tFPort;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TODO_Installer window = new TODO_Installer();
					window.frmTodoInstaller.setVisible(true);
					window.Laden();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public TODO_Installer() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		frmTodoInstaller = new JFrame();
		frmTodoInstaller.setResizable(false);
		frmTodoInstaller.setTitle("TODO Installer");
		frmTodoInstaller.setBounds(100, 100, 489, 375);
		frmTodoInstaller.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmTodoInstaller.getContentPane().setLayout(null);
		
		
		//Fenster mit Handbuch
		
		//Darstellung des Hilfe Fensters
				JFrame FensterHilfe = new JFrame("Hilfe");
				FensterHilfe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				FensterHilfe.setResizable(false);
				FensterHilfe.setBounds(10, 0, 768, 550);
				FensterHilfe.getContentPane().setLayout(null);
				FensterHilfe.setLocationRelativeTo(frmTodoInstaller);
				
				JLabel lblNewLabel = new JLabel("Handbuch");
				lblNewLabel.setBounds(10, 0, 736, 57);
				lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
				lblNewLabel.setFont(new Font("Monotype Corsiva", Font.PLAIN, 50));
				FensterHilfe.getContentPane().add(lblNewLabel);
				
				JScrollPane scrollPane_3 = new JScrollPane();
				scrollPane_3.setBounds(10, 53, 736, 409);
				FensterHilfe.getContentPane().add(scrollPane_3);
				
				//Darstellung der Html Datei in einer EditorPane
				JEditorPane editorPane = new JEditorPane();
				editorPane.setEditable(false);
				   try {
			       File Handbuch =  new File("Handbuch/index.html");
					   editorPane.setPage(Handbuch.toURI().toURL());
			        } 
			        catch (IOException ioe) {
			            // HTML wird als Texttyp vorgegeben.
			            editorPane.setContentType("text/html");
			 
			            // Text für Fehlermeldung wird
			            // im HTML-Format übergeben.
			            editorPane.setText("<html> <center>"
			                    + "<h1>Page not found.</h1>"
			                    + "</center> </html>.");
			        }
				scrollPane_3.setViewportView(editorPane);
				
				JButton btnSchliessen_1 = new JButton("Schlie\u00DFen");
				btnSchliessen_1.setEnabled(true);
				btnSchliessen_1.addActionListener(new ActionListener() {
					/**
					 * FensterHilfe im GUI schliessen wenn der Button "Schliessen" betaetigt wird
					 * @param e Wenn der Button schliessen betaetigt wird
					 */
					public void actionPerformed(ActionEvent e) {
						FensterHilfe.setVisible(false);
					}
				});
				//Darstellung Button Schließen
				btnSchliessen_1.setFont(new Font("Tahoma", Font.PLAIN, 15));
				btnSchliessen_1.setBounds(303, 472, 150, 30);
				FensterHilfe.getContentPane().add(btnSchliessen_1);
				FensterHilfe.setVisible(false);
				
				
		//Hauptfenster
		
		JLabel lblTODO_Installer = new JLabel("TODO.net Installer");
		lblTODO_Installer.setHorizontalAlignment(SwingConstants.CENTER);
		lblTODO_Installer.setFont(new Font("Monotype Corsiva", Font.PLAIN, 40));
		lblTODO_Installer.setBounds(0, 10, 475, 55);
		frmTodoInstaller.getContentPane().add(lblTODO_Installer);
			
		try {															
			JLabel lblLogo = new JLabel();								
			lblLogo.setHorizontalAlignment(SwingConstants.RIGHT);
			logo = ImageIO.read(new File("Bilder/to-do-logo.png"));
			lblLogo.setIcon(new ImageIcon(logo));
			lblLogo.setBounds(10, 75, 139, 185);
			frmTodoInstaller.getContentPane().add(lblLogo);	
			
			tFSchluessel = new JTextField();
			tFSchluessel.setBounds(183, 92, 282, 30);
			frmTodoInstaller.getContentPane().add(tFSchluessel);
			tFSchluessel.setColumns(10);
			
			tFIPAdress = new JTextField();
			tFIPAdress.setColumns(10);
			tFIPAdress.setBounds(183, 158, 282, 30);
			frmTodoInstaller.getContentPane().add(tFIPAdress);
			
			tFPort = new JTextField();
			tFPort.setColumns(10);
			tFPort.setBounds(183, 230, 282, 30);
			frmTodoInstaller.getContentPane().add(tFPort);
			
			JButton btnNewButton = new JButton("Speichern");
			btnNewButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					
					if (tFIPAdress.getText().equals("")||tFPort.getText().equals("")||tFSchluessel.getText().equals(""))
					{
						JOptionPane.showMessageDialog(frmTodoInstaller , "Bitte alle Felder ausfüllen." , "Achtung",
								JOptionPane.WARNING_MESSAGE);
					}
					else
					{
					DateiSchreiben();
					ImageIcon icon = new ImageIcon("Bilder/diskette.png");
					JOptionPane.showMessageDialog(frmTodoInstaller , "Einstellungen wurden erfolgreich gespeichert." , "Gespeichert",
							JOptionPane.INFORMATION_MESSAGE,icon );
					try {
						Laden();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					}
				}
			});
			btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 13));
			btnNewButton.setBounds(331, 281, 134, 25);
			frmTodoInstaller.getContentPane().add(btnNewButton);
			
			JButton btnAbbrechen = new JButton("Abbrechen");
			btnAbbrechen.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					System.exit(0);
				}
			});
			btnAbbrechen.setFont(new Font("Tahoma", Font.PLAIN, 13));
			btnAbbrechen.setBounds(10, 281, 134, 25);
			frmTodoInstaller.getContentPane().add(btnAbbrechen);
			
			JLabel lblSchluessel = new JLabel("Schl\u00FCssel");
			lblSchluessel.setFont(new Font("Tahoma", Font.PLAIN, 13));
			lblSchluessel.setBounds(183, 69, 123, 16);
			frmTodoInstaller.getContentPane().add(lblSchluessel);
			
			JLabel lblIPAdresse = new JLabel("IP Adresse");
			lblIPAdresse.setFont(new Font("Tahoma", Font.PLAIN, 13));
			lblIPAdresse.setBounds(183, 132, 123, 16);
			frmTodoInstaller.getContentPane().add(lblIPAdresse);
			
			JLabel lblPort = new JLabel("Port");
			lblPort.setFont(new Font("Tahoma", Font.PLAIN, 13));
			lblPort.setBounds(183, 204, 123, 16);
			frmTodoInstaller.getContentPane().add(lblPort);
		} catch (IOException e1) {
			e1.printStackTrace();
		}		
		
		JMenuBar menuBar = new JMenuBar();
		frmTodoInstaller.setJMenuBar(menuBar);
		
		JMenu mnHilfe = new JMenu("Hilfe");
		menuBar.add(mnHilfe);
		
		JMenuItem mntmHandbuch = new JMenuItem("Handbuch");
		mntmHandbuch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FensterHilfe.setVisible(true);
			}
		});
		mnHilfe.add(mntmHandbuch);
	}
	
	public void DateiSchreiben()
	{
		FileWriter writerSchluessel;
		FileWriter writerIP;
		FileWriter writerPort;
		
		File dateiSchluessel = new File("AuthKey.txt");
		File dateiIP = new File("IPaddress.txt");
		File dateiPort = new File("Port.txt");
		
		try {
			writerSchluessel = new FileWriter(dateiSchluessel);
			String result = tFSchluessel.getText().replace(" ", "");
			writerSchluessel.write(result);
			writerSchluessel.flush();
			writerSchluessel.close();
			
			writerIP = new FileWriter(dateiIP);
			String result2 = tFIPAdress.getText().replace(" ", "");
			writerIP.write(result2);
			writerIP.flush();
			writerIP.close();
			
			writerPort = new FileWriter(dateiPort);
			String result3 = tFPort.getText().replace(" ", "");
			writerPort.write(result3);
			writerPort.flush();
			writerPort.close();
		}catch(IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public void Laden() throws IOException
	{
		
		    FileReader readerSchluessel;
		    FileReader readerIP;
		    FileReader readerPort;
			try {
	
				readerSchluessel = new FileReader("AuthKey.txt");
				readerIP = new FileReader("IPaddress.txt");
				readerPort = new FileReader("Port.txt");
		
				BufferedReader Key = new BufferedReader(readerSchluessel);
				String strKey = Key.readLine();
				tFSchluessel.setText(strKey);
				Key.close();
			
				BufferedReader IP = new BufferedReader(readerIP);
				String strIP = IP.readLine();
				tFIPAdress.setText(strIP);
				IP.close();
				
				BufferedReader Port = new BufferedReader(readerPort);
				String strPort = Port.readLine();
				tFPort.setText(strPort);
				Port.close();

			} catch (FileNotFoundException e) {
				tFIPAdress.setText("");
				tFPort.setText("");
				tFSchluessel.setText("");
			}
	}
	
}


