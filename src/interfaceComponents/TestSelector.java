package interfaceComponents;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.io.IOException;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreePath;

import mainWindow.MainWindow;
import utilities.Colors;
import utilities.Constants;
import ftp.DataHolder.Student;
import ftp.FTPConnection;

/**
 * Allows you to select what test you want to take o
 * @author Mark Wiggans
 */
public class TestSelector extends JPanel{
	private static final long serialVersionUID = -4675444471114154077L;
	private JTree tree;
	private JScrollPane questionSelectorPane;
	private FTPConnection ftpClient;
	private String username;
	private JLabel pathLabel;
	private Map<String, Boolean> imageLookup;
	
	/**
	 * Creates a new text selector
	 * @param ftp 
	 * @param username the user name of the person using the program
	 */
	public TestSelector(FTPConnection ftp, String username) {
		ftpClient = ftp;
		imageLookup = ftp.getMap();
		this.username = username;
		this.init();
	}

	/**
	 * Creates all of the display components
	 */
	private void init() {
		setLayout(new BorderLayout());
		tree = new JTree(ftpClient.getCachedTree());
		tree.setBackground(Colors.PANEL_BACKGROUND);
		tree.setCellRenderer(new CellRenderer());
		tree.setBorder(BorderFactory.createEmptyBorder());
		questionSelectorPane = new JScrollPane(tree);
		JPanel extraWhiteSpace = new JPanel();
		extraWhiteSpace.setBorder(BorderFactory.createEmptyBorder());
		extraWhiteSpace.setPreferredSize(new Dimension(5, 3));
		extraWhiteSpace.setBackground(Colors.PANEL_BACKGROUND);
		questionSelectorPane.setRowHeaderView(extraWhiteSpace);
		questionSelectorPane.setBorder(BorderFactory.createEmptyBorder());
		add(questionSelectorPane, BorderLayout.CENTER);
		add(Box.createHorizontalStrut(15), BorderLayout.EAST);
		setBackground(Colors.TRANSPARENT);
		setPreferredSize(new Dimension(250, 50));
		
		pathLabel = new JLabel("Assignment Selector");
		pathLabel.setFont(Constants.MINOR_JMENUBAR_FONT);
		pathLabel.setVerticalAlignment(JLabel.CENTER);
		pathLabel.setHorizontalAlignment(JLabel.CENTER);
		pathLabel.setForeground(Colors.MINOR_BAR_FOREGROUND);
		JPanel northContainer = new JPanel();
		northContainer.setBackground(Colors.TRANSPARENT);
		northContainer.setLayout(new BorderLayout());
		northContainer.add(Box.createHorizontalStrut(15), BorderLayout.EAST);
		
		RoundedPanel pathLabelContainer = new RoundedPanel();
		pathLabelContainer.setPreferredSize(new Dimension(250, 30));
		pathLabelContainer.setBackground(Colors.MINOR_BAR_BACKGROUND);
		pathLabelContainer.setLayout(new BorderLayout());
		pathLabelContainer.add(pathLabel, BorderLayout.CENTER);
		
		northContainer.add(pathLabelContainer, BorderLayout.CENTER);
		add(northContainer, BorderLayout.NORTH);
	}
	
	/**
	 * Renders the cell
	 * @author Mark Wiggans
	 */
	private class CellRenderer extends DefaultTreeCellRenderer{
		
		private static final long serialVersionUID = 1L;

		@Override
	    public Color getBackgroundNonSelectionColor() {
	        return (null);
	    }
		
		@Override
		public Color getTextSelectionColor(){
			return Color.BLACK;
		}
		
	    @Override
	    public Color getBackgroundSelectionColor() {
	    	return Colors.QUESTION_SELECTED;
	    }

	    @Override
	    public Color getBackground() {
	        return null;
	    }

	    @Override
	    public Component getTreeCellRendererComponent(final JTree tree, final Object value, final boolean sel, final boolean expanded, final boolean leaf, final int row, final boolean hasFocus) {
	        final Component ret = super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
	        final DefaultMutableTreeNode nodo = (DefaultMutableTreeNode) value;
	        this.setText(value.toString());
	        if(MainWindow.isTeacher){
		        if (tree.getModel().getRoot().equals(nodo)) {
		        	setIcon(null);
		        	this.setFont(Constants.TREE_ROOT_FONT);
		        } else if (nodo.getParent().equals(tree.getModel().getRoot())) {
		        	ImageIcon ic = new ImageIcon(Constants.FOLDER_ICON_PATH);
	        		setIcon(ic);
		        	this.setFont(Constants.TREE_FOLDER_FONT);
		        } else {
		        	setIcon(null);
		        	this.setFont(Constants.TREE_ITEM_FONT);
		        }
	        }else{
	        	//If node is the root
		        if (tree.getModel().getRoot().equals(nodo)) {
		        	setIcon(null);
		        	this.setFont(Constants.TREE_ROOT_FONT);
		        //If the node's parent is the root
		        } else if (nodo.getParent().equals(tree.getModel().getRoot())) {
		        	ImageIcon ic = new ImageIcon(Constants.FOLDER_ICON_PATH);
	        		setIcon(ic);
		        	this.setFont(Constants.TREE_FOLDER_FONT);
		        //If it is not the root or a direct descendant
		        } else {
		        	ImageIcon ic = null;
		        	try{
			        	if(imageLookup.get(ftpClient.getDataHolder().getTeacherNameFromClassName(nodo.getParent().toString(), username)+"/"+nodo.getParent()+"/"+nodo)){
			        		ic = new ImageIcon(Constants.COMPLETE_PATH);
			        	}else{
			        		ic = new ImageIcon(Constants.INCOMPLETE_PATH);
			        	}
		        	}catch(Exception e){
		        		e.printStackTrace();
		        	}
		        	
		        	setIcon(ic);
		        	this.setFont(Constants.TREE_ITEM_FONT);
		        }
	        }
	        
	        this.setBorder(BorderFactory.createEmptyBorder());
	        this.setForeground(Colors.PANEL_TEXT_FOREGROUND);
	        return ret;
	    }
	}

	/**
	 * Repaints the tree
	 * @throws IOException
	 */
	public void setHierarchy() throws IOException {
		tree.repaint();
	}

	/**
	 * 
	 * @throws IOException
	 */
	public void refreshTree() throws IOException {
		ftpClient.cacheHeirarchy(username + "/", username);
		tree = new JTree(ftpClient.getCachedTree());
		tree.setBackground(Colors.PANEL_BACKGROUND);
		CellRenderer renderer = new CellRenderer();
		tree.setCellRenderer(renderer);
		tree.setBorder(BorderFactory.createEmptyBorder());
		questionSelectorPane.setViewportView(tree);
	}
	
	/**
	 * Refreshes the student's tree by going to the server and getting all the data again
	 * @throws IOException
	 */
	public void refreshStudentTree() throws IOException {
		Student student = ftpClient.getDataHolder().getStudentFromUsername(username);
		ftpClient.cacheStudentTree(username, student.getFirstName()+" "+student.getLastName());
		tree = new JTree(ftpClient.getCachedTree());
		tree.setBackground(Colors.PANEL_BACKGROUND);
		CellRenderer renderer = new CellRenderer();
		tree.setCellRenderer(renderer);
		tree.setBorder(BorderFactory.createEmptyBorder());
		questionSelectorPane.setViewportView(tree);
	}

	/**
	 * Checks if the given path is to a test
	 * @param path the path to check
	 * @return true if to a test, false if not
	 */
	public boolean isTest(TreePath path) {
		if(path != null){
			return path.getPathCount() == 3;
		}
		return false;
	}
	
	/**
	 * Sets which element is selected
	 * @param path path to the element to be selected
	 */
	public void setTreeSelection(TreePath path) {
		tree.setSelectionPath(path);
	}

	/**
	 * Gets the path to the selected item in the tree
	 * @return the path
	 */
	public TreePath getTreePath() {
		return tree.getSelectionPath();
	}
	
	/**
	 * Gets the underlying tree
	 * @return the tree
	 */
	public JTree getTree(){
		return tree;
	}
	
	/**
	 * Sets the text in the label above the tree
	 * @param text string to put in the label
	 */
	public void setPathLabel(String text){
		pathLabel.setText(text);
	}
}
