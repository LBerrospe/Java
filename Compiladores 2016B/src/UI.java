import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JToolBar;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import javax.swing.border.CompoundBorder;
import java.awt.Dimension;
import javax.swing.UIManager;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class UI {

	private JFrame frmEditcpas;
	public JTextArea txtrConsola;
	private ImageIcon nuevoArchivoIcon=new ImageIcon("imagenes\\nuevoArchivo.png");
	private ImageIcon guardarArchivoIcon=new ImageIcon("imagenes\\guardarArchivo.png");
	private ImageIcon guardarArchivosIcon=new ImageIcon("imagenes\\guardarArchivos.png");
	private ImageIcon analisisLexicoIcon=new ImageIcon("imagenes\\analisisLexico.png");
	private ImageIcon analisisSintacticoIcon=new ImageIcon("imagenes\\analisisSintactico.png");
	private ImageIcon analisisSemanticoIcon=new ImageIcon("imagenes\\analisisSemantico.png");
	private ImageIcon generadorCodigoIcon=new ImageIcon("imagenes\\generadorCodigo.png");
	private ImageIcon correrIcon=new ImageIcon("imagenes\\correr.png");

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UI window = new UI();
					String ruta="documentos\\NoTeTruena.txt";
					AnalizadorSintactico as = new AnalizadorSintactico(ruta, window);
					as.programa();
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
		frmEditcpas.setTitle("editC-Pas");
		frmEditcpas.setBounds(100, 100, 630, 369);
		frmEditcpas.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JMenuBar menuBar = new JMenuBar();
		frmEditcpas.setJMenuBar(menuBar);
		
		JMenu mnArchivo = new JMenu("Archivo");
		menuBar.add(mnArchivo);
		
		JMenuItem mntmNuevo = new JMenuItem("Nuevo");
		mntmNuevo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}//actionPerformed
		});//ActionListener
		mnArchivo.add(mntmNuevo);
		
		JMenuItem mntmAbrir = new JMenuItem("Abrir");
		mntmAbrir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}//actionPerformed
		});//ActionListener
		mnArchivo.add(mntmAbrir);
		mnArchivo.addSeparator();
		
		JMenuItem mntmCerrar = new JMenuItem("Cerrar");
		mntmCerrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}//actionPerformed
		});//ActionListener
		mnArchivo.add(mntmCerrar);
		
		JMenuItem mntmCerrarTodos = new JMenuItem("Cerrar todos");
		mntmCerrarTodos.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}//actionPerformed
		});//ActionListener
		mnArchivo.add(mntmCerrarTodos);
		mnArchivo.addSeparator();
		
		JMenuItem mntmGuardar = new JMenuItem("Guardar");
		mntmGuardar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}//actionPerformed
		});//ActionListener
		mnArchivo.add(mntmGuardar);
		
		JMenuItem mntmGuardarComo = new JMenuItem("Guardar como");
		mntmGuardarComo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}//actionPerformed
		});//ActionListener
		mnArchivo.add(mntmGuardarComo);
		
		JMenuItem mntmGuardarTodos = new JMenuItem("Guardar todos");
		mntmGuardarTodos.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}//actionPerformed
		});//ActionListener
		mnArchivo.add(mntmGuardarTodos);
		mnArchivo.addSeparator();
		
		JMenu mnArchivosRecientes = new JMenu("Archivos recientes");
		mnArchivo.add(mnArchivosRecientes);
		
		JToolBar toolBar = new JToolBar();
		toolBar.setBounds(0, 0, 448, 16);
		
		JButton btnNuevoArchivo = new JButton(nuevoArchivoIcon);
		btnNuevoArchivo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}//actionPerformed
		});//addActionListener
		toolBar.add(btnNuevoArchivo);
		
		JButton btnGuardarArchivo = new JButton(guardarArchivoIcon);
		btnGuardarArchivo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}//actionPerformed
		});//addActionListener
		toolBar.add(btnGuardarArchivo);
		
		JButton btnGuardarArchivos = new JButton(guardarArchivosIcon);
		btnGuardarArchivos.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}//actionPerformed
		});//addActionListener
		toolBar.add(btnGuardarArchivos);
		
		JButton btnAnalizadorLexico = new JButton(analisisLexicoIcon);
		btnAnalizadorLexico.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}//actionPerformed
		});
		toolBar.add(btnAnalizadorLexico);
		
		JButton btnAnalizadorSintactico = new JButton(analisisSintacticoIcon);
		btnAnalizadorSintactico.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}//actionPerformed
		});//addActionListener
		toolBar.add(btnAnalizadorSintactico);
		
		JButton btnAnalizadorSemantico = new JButton(analisisSemanticoIcon);
		btnAnalizadorSemantico.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}//actionPerformed
		});//addActionListener
		toolBar.add(btnAnalizadorSemantico);
		
		JButton btnGeneracionCodigo = new JButton(generadorCodigoIcon);
		btnGeneracionCodigo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}//actionPerformed
		});//addActionListener
		toolBar.add(btnGeneracionCodigo);
		
		JButton btnCorrer = new JButton(correrIcon);
		btnCorrer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}//actionPerformed
		});//addActionListener
		btnCorrer.setHorizontalAlignment(SwingConstants.LEFT);
		toolBar.add(btnCorrer);
		frmEditcpas.getContentPane().add(toolBar, BorderLayout.NORTH);
		
		txtrConsola = new JTextArea();
		txtrConsola.setPreferredSize(new Dimension(4, 150));
		txtrConsola.setBorder(new CompoundBorder());
		JScrollPane scrollPaneConsola = new JScrollPane(txtrConsola);
		frmEditcpas.getContentPane().add(scrollPaneConsola, BorderLayout.SOUTH);
		
		JScrollPane scrollPaneEditorTexto = new JScrollPane();
		frmEditcpas.getContentPane().add(scrollPaneEditorTexto);
		
		JLabel lblRutaarchivo = new JLabel("");
		scrollPaneEditorTexto.setColumnHeaderView(lblRutaarchivo);
		
		JTextArea txtrRow = new JTextArea();
		txtrRow.setBackground(UIManager.getColor("Button.background"));
		txtrRow.setEditable(false);
		txtrRow.setForeground(UIManager.getColor("Button.darkShadow"));
		for (int i = 1; i < 500; i++) {
			txtrRow.append(i+"\n");
		}//for
		scrollPaneEditorTexto.setRowHeaderView(txtrRow);
		
		JTextArea txtrEditorTexto = new JTextArea();
		scrollPaneEditorTexto.setViewportView(txtrEditorTexto);
	}
}