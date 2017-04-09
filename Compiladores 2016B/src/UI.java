/**
 * @author Hector Eduardo Berrospe Barajas
 * @version 1.0
 */

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JToolBar;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import java.awt.BorderLayout;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import javax.swing.UIManager;
import javax.swing.border.Border;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.Font;

public class UI {

	Consola c = null;
	Border emptyBorder = BorderFactory.createEmptyBorder();
	FileNameExtensionFilter fnexf = new FileNameExtensionFilter("TEXT FILES","txt","cpas");
	private JFileChooser fileChooser;
	private String rutaArchivo="";
	int dotAnterior=0;
	int msg=0;
	private JFrame frmEditcpas;
	private JTextArea  txtrEditorTexto;
	private JLabel lblRutaarchivo;
	private ImageIcon nuevoArchivoIcon=new ImageIcon("imagenes\\nuevoArchivo.png");
	private ImageIcon guardarArchivoIcon=new ImageIcon("imagenes\\guardarArchivo.png");
	private ImageIcon analisisLexicoIcon=new ImageIcon("imagenes\\analisisLexico.png");
	private ImageIcon correrIcon=new ImageIcon("imagenes\\correr.png");
	boolean archivoGuardado=true;
	
	


	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UI window = new UI();
					window.frmEditcpas.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public UI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmEditcpas = new JFrame();
		frmEditcpas.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				if (!archivoGuardado) {
					msg = JOptionPane.showConfirmDialog(null, "¿Deseas guardar archivo?");
					if (msg == JOptionPane.YES_OPTION) {
						guardarArchivo();
					}//Si elige la opcion si
				}//si
			}//windowevent
		});//windowsListener
		frmEditcpas.setTitle("editC-Pas");
		frmEditcpas.setBounds(100, 100, 1024, 768);
		frmEditcpas.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JScrollPane scrollPaneEditorTexto = new JScrollPane();
		frmEditcpas.getContentPane().add(scrollPaneEditorTexto);
		
		lblRutaarchivo = new JLabel("");
		scrollPaneEditorTexto.setColumnHeaderView(lblRutaarchivo);
		
		JTextArea txtrRow = new JTextArea();
		txtrRow.setFont(new Font("Monospaced", Font.PLAIN, 16));
		txtrRow.setBackground(UIManager.getColor("Button.background"));
		txtrRow.setEditable(false);
		txtrRow.setForeground(UIManager.getColor("Button.darkShadow"));
		for (int i = 1; i < 500; i++) {
			txtrRow.append(i+"\n");
		}//for
		scrollPaneEditorTexto.setRowHeaderView(txtrRow);
		
		txtrEditorTexto = new JTextArea();
		txtrEditorTexto.setFont(new Font("Monospaced", Font.PLAIN, 16));
		txtrEditorTexto.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent arg0) {
				archivoGuardado=false;
			}
		});
		scrollPaneEditorTexto.setViewportView(txtrEditorTexto);
		
		JMenuBar menuBar = new JMenuBar();
		frmEditcpas.setJMenuBar(menuBar);
		
		JMenu mnArchivo = new JMenu("Archivo");
		menuBar.add(mnArchivo);
		
		JMenuItem mntmNuevo = new JMenuItem("Nuevo");
		mntmNuevo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!archivoGuardado) {	//Si ya se escribio algo preguntar si desea guardar los cambios
					msg = JOptionPane.showConfirmDialog(null, "¿Deseas guardar cambios?");
					if (msg == JOptionPane.YES_OPTION) {
						guardarArchivo();
						txtrEditorTexto.setText("");
					} else {
						archivoGuardado=true;
						txtrEditorTexto.setText("");
					}//if{}else{}
				}//if
				txtrEditorTexto.setText("");
				lblRutaarchivo.setText(" ");
				rutaArchivo="";
			}//actionPerformed
		});//ActionListener
		mnArchivo.add(mntmNuevo);
		
		JMenuItem mntmAbrir = new JMenuItem("Abrir");
		mntmAbrir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!archivoGuardado){
					msg = JOptionPane.showConfirmDialog(null, "¿Deseas guardar cambios?");
					if (msg == JOptionPane.YES_OPTION) {
						guardarArchivo();
					}//Si elige la opcion si
				}//si el archivo no esta guardado, preguntar si quiere guardarlos
				abrirArchivo();
			}//actionPerformed
		});//ActionListener
		mnArchivo.add(mntmAbrir);
		mnArchivo.addSeparator();
		
		JMenuItem mntmGuardar = new JMenuItem("Guardar");
		mntmGuardar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				guardarArchivo();
			}//actionPerformed
		});//ActionListener
		mnArchivo.add(mntmGuardar);
		
		JMenuItem mntmGuardarComo = new JMenuItem("Guardar como");
		mntmGuardarComo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				guardarArchivoComo();
			}//actionPerformed
		});//ActionListener
		mnArchivo.add(mntmGuardarComo);
		mnArchivo.addSeparator();
		
		JMenu mnArchivosRecientes = new JMenu("Archivos recientes");
		mnArchivo.add(mnArchivosRecientes);
		
		JToolBar toolBar = new JToolBar();
		toolBar.setBounds(0, 0, 448, 16);
		
		JButton btnNuevoArchivo = new JButton(nuevoArchivoIcon);
		btnNuevoArchivo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!archivoGuardado) {	//Si ya se escribio algo preguntar si desea guardar los cambios
					msg = JOptionPane.showConfirmDialog(null, "¿Deseas guardar cambios?");
					if (msg == JOptionPane.YES_OPTION) {
						guardarArchivo();
						txtrEditorTexto.setText("");
					} else {
						archivoGuardado=true;
						txtrEditorTexto.setText("");
					}//if{}else{}
				}//if
				txtrEditorTexto.setText("");
				lblRutaarchivo.setText(" ");
				rutaArchivo="";
			}//actionPerformed
		});//addActionListener
		toolBar.add(btnNuevoArchivo);
		
		JButton btnGuardarArchivo = new JButton(guardarArchivoIcon);
		btnGuardarArchivo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				guardarArchivo();
			}//actionPerformed
		});//addActionListener
		toolBar.add(btnGuardarArchivo);
		
		JButton btnAnalizadorLexico = new JButton(analisisLexicoIcon);
		btnAnalizadorLexico.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (rutaArchivo.equals("")){
					abrirArchivo();
					if (!rutaArchivo.equals("")){
						ejecutarAnalizadorLexico();
					}//if
				} else {
					if (!archivoGuardado) {	//Si ya se escribio algo preguntar si desea guardar los cambios
						msg = JOptionPane.showConfirmDialog(null, "¿Deseas guardar cambios?");
						if (msg == JOptionPane.YES_OPTION) {
							guardarArchivo();
						}//if
					}//if
					ejecutarAnalizadorLexico();
				}//if{}else{}
			}//actionPerformed
		});
		toolBar.add(btnAnalizadorLexico);
		
		JButton btnAnalizadorSintactico = new JButton(correrIcon);
		btnAnalizadorSintactico.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (rutaArchivo.equals("") && archivoGuardado){
					abrirArchivo();
					if (!rutaArchivo.equals("")){
						ejecutarAnalizadorSintactico();
					}//if
				} else {
					if (!archivoGuardado) {	//Si ya se escribio algo preguntar si desea guardar los cambios
						msg = JOptionPane.showConfirmDialog(null, "¿Deseas guardar cambios?");
						if (msg == JOptionPane.YES_OPTION) {
							guardarArchivo();
						}//if
					}//if
					ejecutarAnalizadorSintactico();
				}//if{}else{}
			}//actionPerformed
		});//addActionListener
		toolBar.add(btnAnalizadorSintactico);
		frmEditcpas.getContentPane().add(toolBar, BorderLayout.NORTH);
	}//init
	
	void guardarArchivo() {
		if (rutaArchivo.equals("")) {
			int saveDialog=0;
			fileChooser = new JFileChooser();
			fileChooser.setFileFilter(fnexf);
			saveDialog = fileChooser.showOpenDialog(null);
			rutaArchivo=fileChooser.getSelectedFile().getAbsolutePath();
			lblRutaarchivo.setText(rutaArchivo);
			File file=fileChooser.getSelectedFile();
			try (FileWriter fw = new FileWriter(file);
				 BufferedWriter bw = new BufferedWriter(fw);) {
				if (saveDialog == JFileChooser.APPROVE_OPTION) {
					txtrEditorTexto.write(bw);
				}//if{}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			archivoGuardado=true;
		} else {
			rutaArchivo=fileChooser.getSelectedFile().getAbsolutePath();
			lblRutaarchivo.setText(rutaArchivo);
			File file=fileChooser.getSelectedFile();
			try (FileWriter fw = new FileWriter(file);
				 BufferedWriter bw = new BufferedWriter(fw);) {
				txtrEditorTexto.write(bw);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			archivoGuardado=true;
		}//if{}else{}
	}//guardarArchivo

	void guardarArchivoComo() {
		int saveDialog=0;
		fileChooser = new JFileChooser();
		fileChooser.setFileFilter(fnexf);
		saveDialog = fileChooser.showOpenDialog(null);
		rutaArchivo=fileChooser.getSelectedFile().getAbsolutePath();
		lblRutaarchivo.setText(rutaArchivo);
		File file=fileChooser.getSelectedFile();
		try (FileWriter fw = new FileWriter(file);
			 BufferedWriter bw = new BufferedWriter(fw);) {
			if (saveDialog == JFileChooser.APPROVE_OPTION) {
				txtrEditorTexto.write(bw);
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		archivoGuardado=true;
	}//guardarArchivoComo
	
	void abrirArchivo() {
		fileChooser=new JFileChooser();
		fileChooser.setFileFilter(fnexf);
		if ( fileChooser.showOpenDialog(txtrEditorTexto.getParent()) == JFileChooser.APPROVE_OPTION) {
			fileChooser.getSelectedFile().getName();
			rutaArchivo=fileChooser.getSelectedFile().getAbsolutePath();
			lblRutaarchivo.setText(rutaArchivo);
			File file = fileChooser.getSelectedFile();
			try (FileReader fileReader = new FileReader(file);
				 BufferedReader bufferedReader = new BufferedReader(fileReader);) {
				String ln="";
				txtrEditorTexto.setText("");
				if ((ln = bufferedReader.readLine()) != null) {
					txtrEditorTexto.append(ln);
					txtrEditorTexto.append("\n");
					while ((ln = bufferedReader.readLine()) != null) {
						txtrEditorTexto.append(ln);
						txtrEditorTexto.append("\n");
					}//while
				}//if
			archivoGuardado=true;	
			} catch (IOException e1){
				e1.printStackTrace();
			}//try{}catch{}
		}//if
	}//abrirArchivo
	
	void ejecutarAnalizadorLexico() {
		c = new Consola();
		c.consolaFrame.setVisible(true);
		AnalizadorLexico al = new AnalizadorLexico(rutaArchivo,c);
		c.txtrConsola.append("Token \tLexema\n");
		c.txtrConsola.append("---------\t-----------\n");
		while (al.obtenerIndex() <  al.obtenerTAM_CODIGO_FUENTE()) { 
			String lex=al.escanear();
			c.txtrConsola.append(" "+al.obtenerToken()+" \t"+lex+"\n");
		}//while
	}

	void ejecutarAnalizadorSintactico() {
		Consola c = new Consola();
		c.consolaFrame.setVisible(true);
		AnalizadorSintactico as = new AnalizadorSintactico(rutaArchivo, c);
		as.programa();
	}//ejecutarAnalizadorSintactico
}