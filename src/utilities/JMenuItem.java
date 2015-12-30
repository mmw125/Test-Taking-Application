package utilities;

import javax.swing.border.EmptyBorder;

/**
 * Wrapper class for javax.swing.JMenuItem so that
 * instances of it can be set up in the correct way
 * @author Mark Wiggans
 */
public class JMenuItem extends javax.swing.JMenuItem{
	private static final long serialVersionUID = 1L;

	public JMenuItem(String text){
		this(text, true);
	}
	
	public JMenuItem(String text, boolean isMajor){
		super(text);
		if(isMajor){
			setForeground(Colors.MAJOR_BAR_FOREGROUND);
			setBackground(Colors.MAJOR_BAR_BACKGROUND);
			setFont(Constants.JMENUBAR_FONT);
			setBorder(new EmptyBorder(0, 0, 0, 0));
		}else{
			setForeground(Colors.MINOR_BAR_FOREGROUND);
			//setBackground(Colors.MINOR_BAR_BACKGROUND);
			setFont(Constants.MINOR_JMENUBAR_FONT);
		}
	}
}
