package utilities;

public class JMenuBar extends javax.swing.JMenuBar{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public JMenuBar(){
		super();
		setBackground(Colors.MAJOR_BAR_BACKGROUND);
		setForeground(Colors.MINOR_BAR_FOREGROUND);
	}
	
	public JMenuBar(boolean isMajor){
		super();
		if(isMajor){
			setForeground(Colors.MAJOR_BAR_TEXT);
			setBackground(Colors.MAJOR_BAR_BACKGROUND);
		}else{
			setForeground(Colors.MINOR_BAR_FOREGROUND);
			setBackground(Colors.MINOR_BAR_BACKGROUND);
		}
	}
}
