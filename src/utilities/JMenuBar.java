package utilities;

/**
 * Wrapper class for javax.swing.JMenuBar so that instances of it
 * can be set up in the correct way
 * @author Mark Wiggans
 */
public class JMenuBar extends javax.swing.JMenuBar{
	private static final long serialVersionUID = 1L;

	/**
	 * Creates a new JMenuBar and sets it as a major bar
	 */
	public JMenuBar(){
		this(true);
	}
	
	/**
	 * Creates a new JMenuBar
	 * @param isMajor if the menubar is in teh root
	 */
	public JMenuBar(boolean isMajor){
		super();
		if(isMajor){
			setForeground(Colors.MAJOR_BAR_FOREGROUND);
			setBackground(Colors.MAJOR_BAR_BACKGROUND);
		}else{
			setForeground(Colors.MINOR_BAR_FOREGROUND);
			setBackground(Colors.MINOR_BAR_BACKGROUND);
		}
	}
}
