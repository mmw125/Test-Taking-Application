package utilities;

public class JMenu extends javax.swing.JMenu{
	private static final long serialVersionUID = 1L;

	/**
	 * Creates a new JMenu Object for a major bar
	 * @param text	the mane on the menu
	 */
	public JMenu(String text){
		super(text);
		setForeground(Colors.MAJOR_BAR_TEXT);
		setBackground(Colors.MAJOR_BAR_BACKGROUND);
		setFont(Constants.JMENUBAR_FONT);
	}
	
	public JMenu(String text, boolean isMajor){
		super(text);
		if(isMajor){
			setForeground(Colors.MAJOR_BAR_TEXT);
			setBackground(Colors.MAJOR_BAR_BACKGROUND);
			setFont(Constants.JMENUBAR_FONT);
		}else{
			setForeground(Colors.MINOR_BAR_FOREGROUND);
			setBackground(Colors.MINOR_BAR_BACKGROUND);
			setFont(Constants.MINOR_JMENUBAR_FONT);
		}
	}
}
