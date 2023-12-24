package Game;

import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class Window extends JFrame {

	public JPanel contentPane;
	private JTextField textField;

	/**
	 * Create the frame.
	 */
	public Window() {
		setTitle("2048 [By Sharky]");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 40 + (Main.size * 100), 80 + (Main.size * 100));
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		for(int i = 0;i <= Main.size;i++) {
			JSeparator horizontal = new JSeparator();
			horizontal.setForeground(new Color(0, 0, 0));
			horizontal.setBackground(new Color(0, 0, 0));
			horizontal.setBounds(10, 10 + (i * 100), Main.size * 100, 5);
			contentPane.add(horizontal);
			
			JSeparator vertical = new JSeparator();
			vertical.setOrientation(1);
			vertical.setForeground(new Color(0, 0, 0));
			vertical.setBackground(new Color(0, 0, 0));
			vertical.setBounds(10 + (i * 100), 10, 5, Main.size * 100);
			contentPane.add(vertical);
		}
		
		
	}
}
