package mainWindow;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.JPanel;

import utilities.Constants;


public class LoginBackground extends JPanel 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Image background;
	
	public LoginBackground(LoginFrame lf)
	{
		Toolkit kit = Toolkit.getDefaultToolkit();
		background = kit.getImage(Constants.LOGIN_BACKGROUND);
	}
	
	public void paintComponent(Graphics g)
	{
		Graphics2D g2D = (Graphics2D)g;
		g2D.drawImage(background, 0, 0, this);
	}
}
