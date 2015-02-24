package interfaceComponents;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
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

public class TestSelector extends JPanel{
	private static final long serialVersionUID = -4675444471114154077L;
	private JTree tree;
	private JScrollPane questionSelectorPane;
	private FTPConnection ftpClient;
	private String username;
	private JLabel pathLabel;
	private Map<String, Boolean> imageLookup;
	
	public TestSelector(FTPConnection ftp, String username) {
		ftpClient = ftp;
		imageLookup = ftp.getMap();
		this.username = username;
		this.init();
	}

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
//		JPanel pathLabelContainer = new JPanel();
		pathLabelContainer.setPreferredSize(new Dimension(250, 30));
		pathLabelContainer.setBackground(Colors.MINOR_BAR_BACKGROUND);
		pathLabelContainer.setLayout(new BorderLayout());
		pathLabelContainer.add(pathLabel, BorderLayout.CENTER);
		
		northContainer.add(pathLabelContainer, BorderLayout.CENTER);
		add(northContainer, BorderLayout.NORTH);
	}
	
	private class CellRenderer extends DefaultTreeCellRenderer{
		/**
		 * 
		 */
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
	        //return Colors.SELECTED;
	    }

	    @Override
	    public Color getBackground() {
	        return (null);
	    }

	    @Override
	    public Component getTreeCellRendererComponent(final JTree tree, final Object value, final boolean sel, final boolean expanded, final boolean leaf, final int row, final boolean hasFocus) {
	        final Component ret = super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
	        final DefaultMutableTreeNode nodo = (DefaultMutableTreeNode) value;
	        this.setText(value.toString());
	        if(MainWindow.isTeacher){
		        if (tree.getModel().getRoot().equals(nodo)) {
		        	setIcon(null);
		        	this.setFont(new Font(Font.DIALOG, Font.PLAIN, 24));
		        } else if (nodo.getParent().equals(tree.getModel().getRoot())) {
		        	ImageIcon ic = new ImageIcon("pics/folderIcon.png", "a folder");
	        		setIcon(ic);
		        	this.setFont(new Font(Font.DIALOG, Font.PLAIN, 24));
		        } else {
		        	setIcon(null);
		        	this.setFont(new Font(Font.DIALOG, Font.PLAIN, 21));
		        }
	        }else{
	        	//If node is the root
		        if (tree.getModel().getRoot().equals(nodo)) {
		        	setIcon(null);
		        	this.setFont(new Font(Font.DIALOG, Font.PLAIN, 24));
		        //If the node's parent is the root
		        } else if (nodo.getParent().equals(tree.getModel().getRoot())) {
		        	ImageIcon ic = new ImageIcon("pics/folderIcon.png", "a folder");
	        		setIcon(ic);
		        	this.setFont(new Font(Font.DIALOG, Font.PLAIN, 24));
		        //If it is not the root or a direct descendant
		        } else {
//		        	String[] split = nodo.getParent().toString().split(" ");
//					String pathString = split[0] + "/";
//					pathString += nodo.getParent().toString().substring(split[0].length() + 1);
//					pathString += "/";
//					System.out.println(pathString+nodo.toString());
//					System.out.println("Looking here for an image: "+imageLookup.get(pathString+nodo.toString()));
		        	ImageIcon ic = null;
		        	//if(imageLookup.get(FTPConnection.holder.fwpathString+nodo.toString())){
		        	try{
			        	if(imageLookup.get(ftpClient.getDataHolder().getTeacherNameFromClassName(nodo.getParent().toString(), username)+"/"+nodo.getParent()+"/"+nodo)){
			        		ic = new ImageIcon("pics/complete24.png", "Check Mark");
			        	}else{
			        		ic = new ImageIcon("pics/incomplete24.png", "Circle");
			        	}
		        	}catch(Exception e){
		        		e.printStackTrace();
		        	}
		        	
		        	setIcon(ic);
		        	
		        	this.setFont(new Font(Font.DIALOG, Font.PLAIN, 21));
		        }
	        }
	        
	        
	        
	        this.setBorder(BorderFactory.createEmptyBorder());
	        this.setFont(new Font(Font.DIALOG, Font.PLAIN, 21));
	        this.setForeground(Colors.PANEL_TEXT_FOREGROUND);
	        return ret;
	    }
	}

	public void setHierarchy() throws IOException {
		tree.repaint();
	}

	public void refreshTree() throws IOException {
		ftpClient.cacheHeirarchy(username + "/", username);
		tree = new JTree(ftpClient.getCachedTree());
		tree.setBackground(Colors.PANEL_BACKGROUND);
		CellRenderer renderer = new CellRenderer();
		tree.setCellRenderer(renderer);
		tree.setBorder(BorderFactory.createEmptyBorder());
		questionSelectorPane.setViewportView(tree);
	}
	
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

	public boolean isTest(TreePath path) {
		if(path != null){
			return path.getPathCount() == 3;
		}
		return false;
	}

	public boolean isClass(TreePath pathIn) {
		if (pathIn != null && pathIn.getPathCount() == 2) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isRoot(TreePath path) {
		if (path != null){
			return path.getPathCount() == 1;
		}else{
			return false;
		}
	}
	
	public void setTreeSelection(TreePath path) {
		tree.setSelectionPath(path);
	}

	public TreePath getTreePath() {
		return tree.getSelectionPath();
	}
	
	public JTree getTree(){
		return tree;
	}
	
	public void setPathLabel(String text){
		pathLabel.setText(text);
	}
}
