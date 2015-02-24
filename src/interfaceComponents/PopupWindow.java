package interfaceComponents;
import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

import utilities.Colors;
import utilities.Constants;


public class PopupWindow implements TestHolderHolder{
	private JFrame frame;
	public PopupWindow(){
		frame = new JFrame();
		init();
	}
	
	public PopupWindow(JPanel panel){
		this();
		setPanel(panel);
	}
	
	public void init(){
		frame = new JFrame();
		System.out.println("Created Popup Window");
		ImageIcon icon = new ImageIcon(Constants.LOGO_PATH);
		frame.setIconImage(icon.getImage());
		frame.setMinimumSize(new Dimension(1200, 680));
		frame.setResizable(true);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout());
		((JPanel)frame.getContentPane()).setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		frame.getContentPane().setBackground(Colors.FRAME_BACKGROUND);
	}
	
	public void setPanel(JPanel panel){
		frame.getContentPane().add(panel, BorderLayout.CENTER);
	}

	@Override
	public void setTimer(int time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setAbleToOpenOther(boolean able) {
		// TODO Auto-generated method stub
		
	}
}
