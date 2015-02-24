package interfaceComponents;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.tree.TreePath;

import mainWindow.MainWindow;
import ftp.FTPConnection;
import questionTypes.MultipleChoiceQuestion;
import questionTypes.Question;
import utilities.Colors;
import utilities.Constants;
import utilities.JMenu;
import utilities.StudentConverters;
import utilities.TeacherConverters;

public class TestHolder extends JPanel{
	private static final long serialVersionUID = 1L;
	
	private ProblemHolder holder;
	private ProblemSelector selector;
	private TestHolder testHolder;
	private TestHolderHolder window;
	
	public TestHolder(TestHolderHolder window, FTPConnection ftp){
		this.window = window;
		setLayout(new BorderLayout());
		holder = new ProblemHolder();
		selector = new ProblemSelector();
		JPanel holderHolder = new JPanel();
		holderHolder.setLayout(new BorderLayout());

		int strutLength = 15;
		holderHolder.add(Box.createHorizontalStrut(strutLength), BorderLayout.WEST);
		holderHolder.setBackground(Colors.FRAME_BACKGROUND);
		add(holderHolder, BorderLayout.CENTER);
		holderHolder.add(holder, BorderLayout.CENTER);
		add(selector, BorderLayout.WEST);
		setBackground(Colors.FRAME_BACKGROUND);
		testHolder = this;
	} 
	
	public void importTest(TreePath path, FTPConnection ftp, String username) throws IOException{
		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		remove(selector);
		selector = new ProblemSelector();
		add(selector, BorderLayout.WEST);
		holder.removeQuestion();
		File file = new File("import.tst");
		file.createNewFile();
		if(MainWindow.isTeacher){
			ftp.getFile(TeacherConverters.treePathToTempTestPath(path, username), "import.tst");
		}else{
			ftp.getFile(StudentConverters.treePathToString(path, username, ftp.getDataHolder()),"import.tst");
		}
		Scanner scanner = new Scanner(file);
		while (scanner.hasNext()) {
			ArrayList<String> questionData = new ArrayList<String>();
			String string = scanner.nextLine();
			String[] splitString = string.split(":");
			if (splitString[0].equals("MUL")) {
				for (int i = 1; i < splitString.length; i++) {
					questionData.add(splitString[i]);
				}
				selector.newMultCh(questionData);
			}else if(splitString[0].equals("TIMELIMIT")){
				window.setTimer(Integer.parseInt(splitString[1]));
			}else if(splitString[0].equals("ABLETOOPENOTHER")){
				if(splitString[1].equals("TRUE")){
					window.setAbleToOpenOther(true);
				}else{
					window.setAbleToOpenOther(false);
				}
				
			}
			
		}
		scanner.close();
		file.delete();
		if(MainWindow.isTeacher){
			importKey(path, ftp, username);
		}else{
			selector.setEditable(false, true);
		}
		selector.saved();
		selector.refreshNumberHolderText();
		revalidate();
		setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}
	
	public void importKey(TreePath path, FTPConnection ftp, String username) throws IOException{
		File file = new File("key.key");
		file = new File("key.key");
		file.createNewFile();
		if(MainWindow.isTeacher){
			ftp.getFile(TeacherConverters.treePathToTempKeyPath(path, username), "key.key");
		}else{
			ftp.getFile(StudentConverters.keyPathToKey(path, username, ftp.getDataHolder()), "key.key");
		}
		
		Scanner scanner = new Scanner(file);
		while (scanner.hasNext()) {
			String string = scanner.nextLine();
			String[] splitString = string.split(" ");
			try{
				if(MainWindow.isTeacher){
					selector.getQuestions().get(Integer.parseInt(splitString[0])).setSelected(Integer.parseInt(splitString[1]));
				}else{
					selector.getQuestions().get(Integer.parseInt(splitString[0])).setCorrectAnswer(Integer.parseInt(splitString[1]), true);
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		file.createNewFile();
		scanner.close();
	}
	
	public void importSubmission(TreePath path, FTPConnection ftp, String username) throws IOException {
		File file = new File("key.key");
		file.createNewFile();
		ftp.getFile(StudentConverters.createSubmissionPath(
				path, username, ftp.getDataHolder()), "key.key");
		Scanner scanner = new Scanner(file);
		while (scanner.hasNext()) {
			String string = scanner.nextLine();
			String[] splitString = string.split(" ");

			try {
				selector.getQuestions()
						.get(Integer.parseInt(splitString[0]) - 1)
						.setSelected(Integer.parseInt(splitString[1]));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		scanner.close();
		file.delete();
	}
	
	/**
	 * Holds a question for the user to edit
	 * @author Mark
	 */
	public class ProblemHolder extends JPanel{
		private static final long serialVersionUID = 1L;
		private JScrollPane scrollProblemHolder;
		private MenuBar bar;
		private Question question;
		private JPanel tempPanel;

		ProblemHolder() {
			question = null;
			problemHolderInit();
			if(!MainWindow.isTeacher){
				removeEditJMenu();
			}
		}
		
		/**
		 * Removes the question from view
		 */
		public void removeQuestion() {
			scrollProblemHolder.setViewportView(tempPanel);
		}
		
		/**
		 * Creates the panel
		 */
		public void problemHolderInit() {
			setBorder(BorderFactory.createEmptyBorder());
			setLayout(new BorderLayout());
			bar = new MenuBar();
			
			add(bar.getMenuBar(), BorderLayout.NORTH);
			scrollProblemHolder = new JScrollPane();
			scrollProblemHolder.addComponentListener(new ComponentListener() {
				
				@Override
				public void componentShown(ComponentEvent arg0) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void componentResized(ComponentEvent arg0) {
					System.out.println("resized");
					if(question != null){
						//question.getPanel().setPreferredSize(scrollProblemHolder.getBounds().getSize());
						question.getPanel().setPreferredSize(new Dimension(scrollProblemHolder.getBounds().getSize().width-20, question.getPanel().getHeight()));
					}
				}
				
				@Override
				public void componentMoved(ComponentEvent arg0) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void componentHidden(ComponentEvent arg0) {
					// TODO Auto-generated method stub
					
				}
			});
			scrollProblemHolder.setBorder(BorderFactory.createEmptyBorder());
			add(scrollProblemHolder, BorderLayout.CENTER);
			tempPanel = new JPanel();
			scrollProblemHolder.setViewportView(tempPanel);
			tempPanel.setBackground(Colors.PANEL_BACKGROUND);
		}

		/**
		 * Sets the question to be visible
		 * @param question	the question to be seen
		 */
		public void setQuestion(Question question){
			System.out.println("Question Displayed");
			this.question = question;
			scrollProblemHolder.setVisible(true);
			scrollProblemHolder.setViewportView(question.getPanel());
			revalidate();
			repaint();
		}

		public JScrollPane getPane() {
			return this.scrollProblemHolder;
		}
		
		public Question getQuestion(){
			return question;
		}
		
		/**
		 * Removes the menu options that allow changing the number of answers
		 */
		public void removeEditJMenu(){
			bar.removeEdit();
			bar.getMenuBar().revalidate();
		}

		class MenuBar {
			private JMenuBar problemHolderBar;
			private JMenu edit;

			MenuBar() {
				problemHolderBar = new JMenuBar();
				problemHolderBar.setPreferredSize(new Dimension(1, Constants.MINOR_JMENUBAR_HEIGHT));
				problemHolderBar.setBackground(Colors.MINOR_BAR_BACKGROUND);
				problemHolderBar.setBorder(BorderFactory.createEmptyBorder());
				this.init();
			}

			public void init() {
				edit = new JMenu("Edit");
				edit.setForeground(Colors.MINOR_BAR_FOREGROUND);
				edit.setBackground(null);
				problemHolderBar.add(edit);
				edit.setFont(Constants.MINOR_JMENUBAR_FONT);
				
				JMenuItem add = new JMenuItem("Add Answer Choice");
				edit.add(add);
				add.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if(question != null){
							question.addAnswerChoice();
							holder.revalidate();
						}
						
					}
				});
				
				JMenuItem remove = new JMenuItem("Remove Answer Choice");
				edit.add(remove);
				remove.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if(question != null){
							question.removeAnswerChoice();
							holder.revalidate();
						}
						
					}
				});
			}

			public JMenuBar getMenuBar() {
				return this.problemHolderBar;
			}
			
			public void removeEdit(){
				problemHolderBar.remove(edit);
			}
		}
	}
	
	/**
	 * A JPanel that allows the user to choose a question to edit 
	 * @author Mark
	 */
	public class ProblemSelector extends JPanel {
		private static final long serialVersionUID = 1L;
		private JPanel multChoicePanel = new JPanel(new GridLayout(0, 1, 5, 5));
		private JPanel questionHolder = new JPanel(new BorderLayout());
		private ArrayList<Question> questions;
		private ProblemMenuBar probBar;

		ProblemSelector() {
			questions = new ArrayList<Question>();
			this.init();
			if(!MainWindow.isTeacher){
				removeAdd();
			}
		}
		
		public boolean anyChanges(){
			for(Question question : questions){
				if(question.changesMade()){
					return true;
				}
			}
			return false;
		}
		
		public ArrayList<Question> getQuestions(){
			return questions;
		}

		private void newMultCh() {
			MultipleChoiceQuestion question = new MultipleChoiceQuestion();
			questions.add(question);
			multChoicePanel.add(question.getNumberHolder().getPanel());
			this.reassignProblemNumbers();
			question.addActionListener(testHolder);
			revalidate();
		}
		
		public void newMultCh(ArrayList<String> questionData) {
			MultipleChoiceQuestion question = new MultipleChoiceQuestion(questionData);
			questions.add(question);
			multChoicePanel.add(question.getNumberHolder().getPanel());
			reassignProblemNumbers();
			question.addActionListener(testHolder);
			revalidate();
		}

		private void init() {
			setLayout(new BorderLayout());
			setPreferredSize(new Dimension(250, 250));
			JScrollPane selectorScroll = new JScrollPane(multChoicePanel);
			selectorScroll.setBackground(Colors.PANEL_BACKGROUND);
			probBar = new ProblemMenuBar();
			add(probBar.getBar(), BorderLayout.NORTH);
			add(selectorScroll, BorderLayout.CENTER);
			selectorScroll.setViewportView(questionHolder);
			questionHolder.add(multChoicePanel, BorderLayout.NORTH);
			questionHolder.setBackground(Colors.PANEL_BACKGROUND);
			multChoicePanel.setPreferredSize(new Dimension(10, 0));
			multChoicePanel.setBackground(Colors.PANEL_BACKGROUND);
			setBorder(BorderFactory.createEmptyBorder());
			selectorScroll.setBorder(BorderFactory.createEmptyBorder());
		}

		public void reassignProblemNumbers() {
			for (int i = 0; i < questions.size(); i++) {
				questions.get(i).setQuestionNumber(i + 1);
			}
			multChoicePanel.setPreferredSize(new Dimension(10, (questions.size() * (Constants.PROBLEM_SELECTOR_HEIGHT+5)) - 5));
		}

		public void setSelected(int questionNumber) {
			for (int i = 0; i < questions.size(); i++) {
				if (questionNumber == questions.get(i).getQuestionNumber()) {
					questions.get(i).getNumberHolder().setSelected(true);
					holder.setQuestion(questions.get(i));
				} else {
					questions.get(i).getNumberHolder().setSelected(false);
				}
			}
			refreshNumberHolderText();
			this.repaint();
		}
		
		public void refreshNumberHolderText(){
			for(Question question : questions) {
				question.getNumberHolder().refreshText();
			}
			this.repaint();
		}

		public void removeQuestion(Question question) {
			questions.remove(question);
			multChoicePanel.remove(question.getNumberHolder().getPanel());
			reassignProblemNumbers();
		}
		
		public class ProblemMenuBar {
			private JMenuBar problemBar;
			JMenu newItem;
			ProblemMenuBar() {
				problemBar = new JMenuBar();
				problemBar.setBackground(Colors.MINOR_BAR_BACKGROUND);
				problemBar.setBorder(BorderFactory.createEmptyBorder());
				problemBar.setPreferredSize(new Dimension(42, 30));
				loadProblemBar();
			}

			private void loadProblemBar() {
				newItem = new JMenu("New");
				this.problemBar.add(newItem);
				JMenuItem multCh = new JMenuItem("Multiple Choice Question");
				multCh.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M, KeyEvent.CTRL_MASK));
				newItem.add(multCh);
				multCh.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent arg0) {
						newMultCh();
					}
				});
				
				JMenuItem trueFalse = new JMenuItem("True/False Question");
				trueFalse.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, KeyEvent.CTRL_MASK));
				newItem.add(trueFalse);
				trueFalse.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent arg0) {
						ArrayList<String> question = new ArrayList<String>();
						question.add("Enter a question here....");
						question.add("True");
						question.add("False");
						newMultCh(question);
					}
				});
				trueFalse.setForeground(Colors.MINOR_BAR_FOREGROUND);
			}

			public JMenuBar getBar() {
				return this.problemBar;
			}
		}

		public void setEditable(boolean paneEditable, boolean radioEditable) {
			for(Question question : questions){
				question.setEditable(paneEditable, radioEditable);
			}
		}
		
		public void removeAdd(){
			probBar.newItem.setVisible(false);
		}

		public void saved() {
			for(Question question : questions){
				question.saved();
			}
		}
	}

	public ProblemSelector getSelector(){
		return selector;
	}
	
	public ProblemHolder getHolder(){
		return holder;
	}
}
