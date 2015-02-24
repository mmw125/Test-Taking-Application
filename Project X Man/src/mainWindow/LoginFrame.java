package mainWindow;

import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.ImageIcon;
import javax.swing.JFrame;


public class LoginFrame extends JFrame 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	Login loginPanel;
	
	LoginBackground bg;
	
	public LoginFrame(String title){
		super(title);
		int windowWidth = 850;
		int windowHeight = 700;
		
		ImageIcon icon = new ImageIcon("logo.png");
		setIconImage(icon.getImage());
		
		setSize(windowWidth,windowHeight);
		
		setLayout(new BorderLayout());
		
		loginPanel = new Login(this);
		bg = new LoginBackground(this);
		bg.add(loginPanel);
		
		Container content = getContentPane();
		setContentPane(content);
		content.add(bg);
	}
}
