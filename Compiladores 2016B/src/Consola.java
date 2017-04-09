/**
 * @author Hector Eduardo Berrospe Barajas
 * @version 1.0
 */

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.Font;

public class Consola {

	public JFrame consolaFrame;
	public JTextArea txtrConsola;

	public Consola() {
		initialize();
	}
	
	private void initialize() {
		consolaFrame = new JFrame();
		consolaFrame.setTitle("Consola");
		consolaFrame.setBounds(100, 100,768 , 300);
		consolaFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		
		JScrollPane scrollPane = new JScrollPane();
		consolaFrame.getContentPane().add(scrollPane, BorderLayout.CENTER);
		
		txtrConsola = new JTextArea();
		txtrConsola.setFont(new Font("Monospaced", Font.PLAIN, 16));
		scrollPane.setViewportView(txtrConsola);
		
	}

}
