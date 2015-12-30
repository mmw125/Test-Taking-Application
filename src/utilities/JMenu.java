package utilities;

/**
 * Wrapper for JMenu so new instances of it can be set up in the correct way
 * 
 * @author Mark Wiggans
 */
public class JMenu extends javax.swing.JMenu {
	private static final long serialVersionUID = 1L;

	/**
	 * Creates a new JMenu Object for a major bar
	 * 
	 * @param text
	 *            the mane on the menu
	 */
	public JMenu(String text) {
		this(text, true, false);
	}

	/**
	 * Creates a new JMenu object
	 * @param text string to display in the menu
	 * @param isMajor if the menu is in the upper JMenuBar
	 * @param isInternal if the menu will be inside of another jmenu
	 */
	public JMenu(String text, boolean isMajor, boolean isInternal) {
		super(text);
		if (isInternal) {
			if(isMajor){
				setForeground(Colors.MAJOR_BAR_FOREGROUND);
				setBackground(Colors.MAJOR_BAR_BACKGROUND);
				setFont(Constants.JMENUBAR_FONT);
			}else{
				setForeground(Colors.MINOR_BAR_FOREGROUND);
				setBackground(Colors.MINOR_BAR_BACKGROUND);
				setFont(Constants.MINOR_JMENUBAR_FONT);
			}
		} else {
			if (isMajor) {
				setForeground(Colors.MAJOR_BAR_FOREGROUND);
				setBackground(Colors.MAJOR_BAR_BACKGROUND);
				setFont(Constants.JMENUBAR_FONT);
			} else {
				setForeground(Colors.MINOR_BAR_FOREGROUND);
				setBackground(Colors.MINOR_BAR_BACKGROUND);
				setFont(Constants.MINOR_JMENUBAR_FONT);
			}
		}
	}
}
