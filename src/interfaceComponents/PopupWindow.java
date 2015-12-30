package interfaceComponents;
import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

import utilities.Colors;
import utilities.Constants;

/**
 * Popup window that holds a test so that teachers can 
 * allow users can refer back to old assignments when taking assingments
 * @author Mark Wiggans
 */
public class PopupWindow implements TestHolderHolder{
	private JFrame frame;
	private TestHolder holder;
	
	public PopupWindow() {
		init();
	}
	
	/**
	 * Creates a new Popup window with the given panel
	 * @param panel JPanel to create popup with
	 */
	public PopupWindow(TestHolder holder){
		this();
		this.holder = holder;
		setHolder(holder);
	}
	
	
	public void init(){
		frame = new JFrame();
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
	
	/**
	 * Sets the panel inside of the frame
	 * @param panel new panel
	 */
	public void setHolder(JPanel panel){
		frame.getContentPane().add(panel, BorderLayout.CENTER);
	}

	@Override
	public void setTimer(int time) {
		// Not needed as popups are not timed
	}

	@Override
	public void setAbleToOpenOther(boolean able) {
		// Not needed as popups 
	}
	
	public TestHolder getTestHolder() {
		return holder;
	}
}
