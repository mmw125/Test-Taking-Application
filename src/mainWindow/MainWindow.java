package mainWindow;

import interfaceComponents.Grader;
import interfaceComponents.PopupWindow;
import interfaceComponents.TestHolder;
import interfaceComponents.TestHolderHolder;
import interfaceComponents.TestSelector;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.Timer;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;

import questionTypes.Question;
import utilities.Colors;
import utilities.Constants;
import utilities.JMenu;
import utilities.JMenuBar;
import utilities.JMenuItem;
import utilities.StudentConverters;
import utilities.TeacherConverters;
import ftp.FTPConnection;
import ftp.DataHolder.Student;

public class MainWindow extends JPanel implements TestHolderHolder{

	public static boolean isTeacher;
	private static final long serialVersionUID = 1L;
	private FTPConnection ftpClient;
	private JFrame frame;
	private String username;
	private TreePath currentTestPath;
	private MenuBar menuBar;
	private TestHolder holder;
	private TestSelector selector;
	private Question copiedQuestion;
	private String[] name;
	private boolean inAssignment = false;
	private boolean ableToOpenOtherAssignments = false;

	public MainWindow(String user, FTPConnection ftpConnetion, boolean isTeacher) {
		username = user;
		ftpClient = ftpConnetion;
		name = ftpClient.getDataHolder().getName(user);
		MainWindow.isTeacher = isTeacher;
		init();
	}

	public JFrame getFrame() {
		return frame;
	}

	public void windowClosingEvent(){
		if(MainWindow.isTeacher){
			if (JOptionPane.showConfirmDialog(frame,
					"Are you sure you want to close the program?", "Confirm Exit",
					JOptionPane.YES_NO_OPTION,
					JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
		if(holder != null && holder.getSelector().anyChanges()){
			int output = JOptionPane.showConfirmDialog(frame,
					"You have unsaved changes. Would you like to save them?", "Save changes?",
					JOptionPane.YES_NO_OPTION,
					JOptionPane.QUESTION_MESSAGE);
			if(output == JOptionPane.YES_OPTION){
				try {
					saveTest(false);
				} catch (IOException e) {
					e.printStackTrace();
				}
				System.exit(0);
			}else if(output == JOptionPane.NO_OPTION){
				System.exit(0);
			}
		}else{
			System.exit(0);
		}
	}
		}else{ //if student
			if(inAssignment){
				JOptionPane.showMessageDialog(frame, "You mush finish and submit the assignment before closing the program.");
				
			}else if (JOptionPane
					.showConfirmDialog(frame,
							"Are you sure you want to exit the program?", "Confirm Exit",
							JOptionPane.YES_NO_OPTION,
							JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
				System.exit(0);
			}
		}
	}
	
	private void init() {
		frame = new JFrame();
		System.out.println("Created Main Window");
		ImageIcon icon = new ImageIcon(Constants.LOGO_PATH);
		frame.setIconImage(icon.getImage());
		frame.setMinimumSize(new Dimension(1200, 680));
		frame.setResizable(true);
		frame.setVisible(true);
		frame.setExtendedState(frame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		name = ftpClient.getDataHolder().getName(username);
		frame.setTitle(name[0]+"'s Console");
		frame.getContentPane().setLayout(new BorderLayout());
		frame.addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(java.awt.event.WindowEvent windowEvent) {
				windowClosingEvent();
			}
		});

		final int gap = 10;
		frame.getContentPane().add(this, BorderLayout.CENTER);
		frame.getContentPane().add(Box.createVerticalStrut(gap),
				BorderLayout.NORTH);
		frame.getContentPane().add(Box.createVerticalStrut(gap),
				BorderLayout.SOUTH);
		frame.getContentPane().add(Box.createHorizontalStrut(gap),
				BorderLayout.EAST);
		frame.getContentPane().add(Box.createHorizontalStrut(gap),
				BorderLayout.WEST);

		setLayout(new BorderLayout());
		setBackground(Colors.TRANSPARENT);

		menuBar = new MenuBar();
		frame.setJMenuBar(menuBar.getBar());

		frame.setBackground(Colors.FRAME_BACKGROUND);
		frame.getContentPane().setBackground(Colors.TRANSPARENT);

		selector = new TestSelector(ftpClient, username);
		add(selector, BorderLayout.WEST);
		addTreeSelectionListener(selector.getTree());

	}

	public void submitTest(boolean checkForPossibleMistakes) throws IOException {
		setCursor (Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		File output = new File("output.txt");
		output.createNewFile();
		PrintWriter pw = new PrintWriter(output);
		boolean problem = false;
		for (Question question : holder.getSelector().getQuestions()) {
			if (question.getAnswerChoice() == -1 && !problem && checkForPossibleMistakes) {
				JOptionPane.showMessageDialog(frame, "Please select an answer for Question "+ question.getQuestionNumber());
				problem = true;
			}
			pw.println(question.getQuestionNumber() + " "
					+ question.getAnswerChoice());
			System.out.println(question.getQuestionNumber() + " "
					+ question.getAnswerChoice());
		}
		pw.close();
		if (!problem) {
			ftpClient.uploadFile(StudentConverters.createSubmissionPath(
					currentTestPath, username, ftpClient.getDataHolder()), new FileInputStream(output));
			gradeTest();
		}
		setCursor (Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}
	
	public void gradeTest() {
		try {
			holder.importKey(currentTestPath, ftpClient, username);
		} catch (IOException e) {
			e.printStackTrace();
		}
		double numberOfQuestions = holder.getSelector().getQuestions().size();
		double correctAnswers = 0;
		for (Question question : holder.getSelector().getQuestions()) {
			question.setEditable(false, false);
			if (question.isCorrect()) {
				correctAnswers++;
			}
		}
		double grade = correctAnswers * 100.0f / numberOfQuestions;
		String text = (int) correctAnswers + "/" + (int) numberOfQuestions
				+ " | " + (int) grade + "%";
		// frame.setTitle(text);
		menuBar.setWhiteSpaceText(" Grade: " + text);
		menuBar.stopAllowingSubmissions();
		if(inAssignment){
			try {
				selector.refreshStudentTree();
			} catch (IOException e) {
				e.printStackTrace();
			}
			addTreeSelectionListener(selector.getTree());
			frame.revalidate();
		}
		inAssignment = false;
	}
	
	
	
	public void selectTest(TreePath path, boolean takenBefore)
			throws IOException {
		System.out.println("Method selected");
		currentTestPath = path;
		Object[] splitPath = currentTestPath.getPath();
		
		selector.setPathLabel(splitPath[1].toString() + " | " + splitPath[2].toString());
		importTest(currentTestPath);
		if (takenBefore) {
			holder.importSubmission(path, ftpClient, username);
			gradeTest();
			inAssignment = false;
		} else {
			inAssignment = true;
		}
	};
	
	public void treeSelected(TreeSelectionEvent e){
		if(MainWindow.isTeacher){
			if (selector.isTest(e.getNewLeadSelectionPath())) {
				if(holder != null && holder.getSelector().anyChanges()){
					int option = JOptionPane.showConfirmDialog(frame, "Do you want to save your changes before working on a different assignment?");
					if(option == JOptionPane.OK_OPTION){
						try {
							saveTest(false);
						} catch (IOException e1) {
							e1.printStackTrace();
						}
						try {
							importTest(e.getNewLeadSelectionPath());
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}else if(option == JOptionPane.NO_OPTION){
						try {
							importTest(e.getNewLeadSelectionPath());
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
				}else{
					try {
						if(holder == null){
							holder = new TestHolder(this, ftpClient);
							add(holder, BorderLayout.CENTER);
						}
						currentTestPath = e.getNewLeadSelectionPath();
						if(MainWindow.isTeacher){
							setCursor (Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
						}
						importTest(e.getNewLeadSelectionPath());
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					menuBar.save.setEnabled(true);
				}
			}
		}else{
			if (selector.isTest(e.getNewLeadSelectionPath())) {
				// If student has already taken test
				if (inAssignment) {
					if(ableToOpenOtherAssignments && ftpClient.getMap().get(
							StudentConverters.treePathToMap(e.getNewLeadSelectionPath(), username, ftpClient.getDataHolder()))){
						PopupWindow win = new PopupWindow();
						TestHolder hol = new TestHolder(win, ftpClient);
						win.setPanel(hol);
						try {
							hol.importTest(e.getNewLeadSelectionPath(), ftpClient, username);
							hol.importSubmission(e.getNewLeadSelectionPath(), ftpClient, username);
							hol.importKey(e.getNewLeadSelectionPath(), ftpClient, username);
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}else{
						JOptionPane.showMessageDialog(frame, "You must finish and submit the current assingment before switching assingments.");
					}
				} else {
					if (ftpClient.getMap().get(StudentConverters.treePathToMap(e.getNewLeadSelectionPath(), username, ftpClient.getDataHolder()))) {
						try {
							selectTest(e.getNewLeadSelectionPath(), true);
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					} else {

						if (JOptionPane.showConfirmDialog(frame,
										"Are you sure you want to begin this assingment?",
										"Confirm",
										JOptionPane.YES_NO_OPTION,
										JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
							try {
								selectTest(e.getNewLeadSelectionPath(),false);
							} catch (IOException e1) {
								e1.printStackTrace();
							}
						}
					}
				}
			}
		}
//		// Don't really have any reason for these to be here
//		// Am leaving in just in case
//		if (selector.isClass(e.getNewLeadSelectionPath())) {
//
//		}
//		if (selector.isRoot(e.getNewLeadSelectionPath())) {
//
//		}
	}
	
	public void addTreeSelectionListener(JTree treeIn) {
		treeIn.getSelectionModel().addTreeSelectionListener(
				new TreeSelectionListener() {
					public void valueChanged(TreeSelectionEvent e) {
						treeSelected(e);
					}
			});
	}

	public void saveTest(boolean submitting) throws IOException {
		if(ftpClient.isConnected()){
			File file = new File("export.tst");
			if (file.exists()) {
				file.createNewFile();
			}
			boolean completedSuccessfully = true;
			PrintWriter pw = new PrintWriter(file);
			if(holder.getSelector().getQuestions().size() == 0 && submitting){
				JOptionPane.showMessageDialog(null, "Please add questions before saving", "Warning", JOptionPane.ERROR_MESSAGE);
				completedSuccessfully = false;
			}
			
			pw.println("TIMELIMIT:" + menuBar.getTimeLimit()+"");
			System.out.println(menuBar.getTimeLimit());
			if(menuBar.ableToOpenOtherAssignments()){
				pw.println("ABLETOOPENOTHER:TRUE");
				System.out.println("ABLETOOPENOTHER:TRUE");
			}else{
				pw.println("ABLETOOPENOTHER:FALSE");
				System.out.println("ABLETOOPENOTHER:FALSE");
			}
			
			
			for (Question question : holder.getSelector().getQuestions()) {
				ArrayList<String> answerChoices = question.getAnswerChoices();
				if(submitting && (question.getQuestionText().equals("")||question.getQuestionText().equals("Enter a question here...."))){
					JOptionPane.showMessageDialog(null, "Please enter a question for question "+question.getQuestionNumber(), "Warning", JOptionPane.ERROR_MESSAGE);
					completedSuccessfully = false;
				}
				pw.print("MUL:" + question.getQuestionText() + ": ");
				for (int i = 0; i < answerChoices.size(); i++) {
					if (i == answerChoices.size() - 1) {
						if(submitting && answerChoices.get(i).equals("")){
							if(completedSuccessfully){
								int number = i + 1;
								JOptionPane.showMessageDialog(null, "Please fill out answer choice "+number+" in question "+question.getQuestionNumber(), "Warning", JOptionPane.ERROR_MESSAGE);
								completedSuccessfully = false;
							}
						}else{
							pw.println(answerChoices.get(i));
						}
					} else {
						if(submitting && answerChoices.get(i).equals("")){
							if(completedSuccessfully){
								int number = i + 1;
								JOptionPane.showMessageDialog(null, "Please fill out answer choice "+number+" in question "+question.getQuestionNumber(), "Warning", JOptionPane.ERROR_MESSAGE);
								completedSuccessfully = false;
							}
						}else{
							pw.print(answerChoices.get(i) + ": ");
						}
					}
				}
			}
			pw.close();

			File key = new File("key.key");
			key.createNewFile();
			PrintWriter printWriter = new PrintWriter(key);
			for (Question question : holder.getSelector().getQuestions()) {
				if (question.getAnswerChoice() < 0  && submitting && completedSuccessfully) {
					completedSuccessfully = false;
					JOptionPane.showMessageDialog(null, "Please add questions before saving", "Warning", JOptionPane.ERROR_MESSAGE);

				} else if(completedSuccessfully){
					printWriter.println(question.getQuestionNumber()-1 + " " + question.getAnswerChoice());
				}
			}
			printWriter.close();
			if (completedSuccessfully){
				if(submitting){
					ftpClient.uploadFile(TeacherConverters.treePathToKeyPath(currentTestPath, username), new FileInputStream(key));
					ftpClient.uploadFile(TeacherConverters.treePathToTempKeyPath(currentTestPath, username), new FileInputStream(key));
					ftpClient.uploadFile(TeacherConverters.treePathToTestPath(currentTestPath, username), new FileInputStream(file));
					ftpClient.uploadFile(TeacherConverters.treePathToTempTestPath(currentTestPath, username), new FileInputStream(file));
					JOptionPane.showMessageDialog(null, "Assignment published successfully");
				}else{
					ftpClient.uploadFile(TeacherConverters.treePathToTempKeyPath(currentTestPath, username), new FileInputStream(key));
					ftpClient.uploadFile(TeacherConverters.treePathToTempTestPath(currentTestPath, username), new FileInputStream(file));
				}
				holder.getSelector().saved();
			}

			file.delete();
			key.delete();
		}else{
			JOptionPane.showMessageDialog(null, "Could not connect to the server");
		}
	}
	
//	public static JMenu defaultJMenu(String name){
//		JMenu output = new JMenu(name);
//		output.setBackground(null);
//		output.setFont(Constants.JMENUBAR_FONT);
//		output.setForeground(Colors.MAJOR_BAR_TEXT);
//		return output;
//	}
	
	public static JMenuItem defaultJMenuItem(String name){
		JMenuItem output = new JMenuItem(name);
		return output;
	}

	public void createNewTest(String string) throws IOException {
		if(ftpClient.isConnected()){
			setCursor (Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			ftpClient.createDirectory(TeacherConverters.treePathToString(selector.getTreePath(), username)
					+ "/" + string);
			File file = new File(string + ".tst");
			file.createNewFile();
			ftpClient.uploadFile(TeacherConverters.treePathToTestPath(selector.getTreePath(), username) + "/"
					+ string + "/" + string + ".tst",
					new FileInputStream(file));
			selector.refreshTree();
			addTreeSelectionListener(selector.getTree());
			selector.revalidate();
			file.delete();
			setCursor (Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}else{
			JOptionPane.showMessageDialog(null, "Could not connect to the server");
		}
	}

	public void importTest(File file) throws IOException{
		
	}
	
	public void importTest(TreePath testPath) throws IOException {
		if(ftpClient.isConnected()){
			if(holder == null){
				holder = new TestHolder(this, ftpClient);
				add(holder, BorderLayout.CENTER);
			}
			setCursor (Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			holder.importTest(testPath, ftpClient, username);
			if(MainWindow.isTeacher){
				menuBar.addExtraMenus();
				holder.getSelector().saved();
				currentTestPath = testPath;
				
				revalidate();
			}else{
				menuBar.startAllowingSubmissions();
			}
			setCursor (Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}else{
			JOptionPane.showMessageDialog(null, "Could not connect to the server");
		}
	}

	public class MenuBar {
		private JMenuItem save;
		private JMenuBar jMenuBar;
		private boolean alreadyAdded;
		private JMenu file;
		private JMenuItem grade;
		private JMenuItem cut;
		private JMenuItem copy;
		private JMenuItem paste;
		private JMenuItem delete;
		private JMenuItem publish;
		private JMenu view;
		private JMenu options;
		private JCheckBoxMenuItem ableToOpenOtherAssignments;
		private JLabel connectionStatus;
		private JMenuItem deleteAssignment;
		private JMenu timeLimit;
		private ButtonGroup timeLimitGroup;
		private ArrayList<TimeLimitButton> timeLimitButtons;
		private CountDownTimer countDownTimer;
		
		private JMenuItem submit;
		private JLabel gradeLabel;
		
		MenuBar() {
			if(MainWindow.isTeacher){
				jMenuBar = new JMenuBar();
				jMenuBar.setPreferredSize(new Dimension(1, 50));
				jMenuBar.setBackground(Colors.MAJOR_BAR_BACKGROUND);
				jMenuBar.setBorder(BorderFactory.createEmptyBorder());
				alreadyAdded = false;
				this.addFileMenu();
				this.addEditMenu();
				this.addNewMenu();
				this.addViewMenu();
				this.addTestOptionMenu();
				jMenuBar.add(Box.createGlue());
			}else{
				jMenuBar = new JMenuBar();
				jMenuBar.setBackground(Colors.MAJOR_BAR_BACKGROUND);
				jMenuBar.setBorder(BorderFactory.createEmptyBorder());
				jMenuBar.setPreferredSize(new Dimension(100, 50));
				JMenu file = new JMenu(" File ");
				file.setBackground(null);
				file.setForeground(Colors.MAJOR_BAR_TEXT);
				file.setFont(Constants.JMENUBAR_FONT);
				this.jMenuBar.add(file);
				
				JMenuItem logOut = new JMenuItem("Log Out");
				logOut.setFont(Constants.JMENUBAR_FONT);
				file.add(logOut);
				logOut.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						frame.setVisible(false);
						Login.main(null);
						frame.dispose();
					}
				});

				JMenuItem exit = new JMenuItem("Exit");
				file.add(exit);
				exit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E,
						KeyEvent.CTRL_MASK));
				exit.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent arg0) {
						System.exit(0);
					}
				});

				JMenu view = new JMenu(" View ");
				jMenuBar.add(view);
				view.setBackground(null);
				view.setForeground(Colors.MAJOR_BAR_TEXT);
				view.setFont(Constants.JMENUBAR_FONT);
				JMenu setTextSize = new JMenu("Set Text Size");
				view.add(setTextSize);

				for (int i = 8; i <= 46; i += 2) {
					JMenuItem setSize = new JMenuItem(i + "");
					setTextSize.add(setSize);
					final int temp = i;
					setSize.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent arg0) {
							for (Question question : holder.getSelector()
									.getQuestions())
								question.setTextSize(temp);
						}
					});
				}

				final JCheckBoxMenuItem set = new JCheckBoxMenuItem(
						"Show Test Selector");
				set.setSelected(true);
				set.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent arg0) {
						selector.setVisible(set.isSelected());

					}
				});

				view.add(set);

				submit = new JMenuItem("Submit");
				jMenuBar.add(submit);
				submit.setVisible(false);
				submit.setBackground(null);
				submit.setForeground(Colors.MAJOR_BAR_TEXT);
				submit.setEnabled(false);
				submit.setFont(Constants.JMENUBAR_FONT);
				submit.setMaximumSize(new Dimension(95, Constants.JMENUBAR_HEIGHT));
				submit.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent arg0) {
						if(JOptionPane.showConfirmDialog(frame, "Are you sure you want to submit this assignment? This action cannot be undone.")==JOptionPane.OK_OPTION){
							try {
								submitTest(true);
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
				});

				gradeLabel = new JLabel();
				jMenuBar.add(gradeLabel);
				gradeLabel.setFont(Constants.JMENUBAR_FONT);
				gradeLabel.setForeground(Colors.MAJOR_BAR_TEXT);
				gradeLabel.setBackground(Colors.MAJOR_BAR_BACKGROUND);
				
				jMenuBar.add(Box.createGlue());
				
				countDownTimer = new CountDownTimer();
				jMenuBar.add(countDownTimer.getLabel());
				
			}
			
			addConnectionStatus();
			addUsername();
		}

		public void addFileMenu(){
			file = new JMenu(" File ");
			file.setBackground(null);
			file.setForeground(Colors.MAJOR_BAR_TEXT);
			file.setFont(Constants.JMENUBAR_FONT);
			file.setVerticalAlignment(JMenu.CENTER);
			this.jMenuBar.add(file);

			save = new JMenuItem("Save");
			file.add(save);
			save.setEnabled(false);
			save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
					KeyEvent.CTRL_MASK));
			save.setToolTipText("Saves the test without allowing students to take it");
			save.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					try {
						saveTest(false);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			});
			
			publish = new JMenuItem("Publish");
			file.add(publish);
			publish.setEnabled(false);
			publish.setToolTipText("Allows students to see this version of the test");
			publish.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					try {
						saveTest(true);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			});
			
			grade = new JMenuItem("Grade");
			file.add(grade);
			grade.setToolTipText("Opens a grading window for the selected assignment");
			grade.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G, KeyEvent.CTRL_MASK));
			grade.setEnabled(false);
			grade.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					setCursor (Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
					ftp.DataHolder.Class classIn = ftpClient.getDataHolder().getClass(username, currentTestPath.getPathComponent(1).toString());
					ArrayList<Student> studentsInClass =  classIn.getStudentsInClass();
					Grader grader = new Grader(currentTestPath, studentsInClass, ftpClient, username);
					grader.setVisible(true);
					setCursor (Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
				}
			});
			
//			JMenuItem exportTest = new JMenuItem("Export Test");
//			file.add(exportTest);
//			exportTest.setToolTipText("Exports the tests to the file system. Not currently implemented");
//			exportTest.setEnabled(false);
//			
//			JMenuItem importTest = new JMenuItem("Import Test");
//			file.add(importTest);
//			importTest.setEnabled(false);
//			final JFileChooser chooser = new JFileChooser();
//			chooser.setFileFilter(new FileFilter() {
//				
//				@Override
//				public String getDescription() {
//					return "Test Files";
//				}
//				
//				@Override
//				public boolean accept(File f) {
//					if(f.getName().endsWith(".tst")){
//						return true;
//					}
//					return false;
//				}
//			});
//			importTest.addActionListener(new ActionListener() {
//				
//				@Override
//				public void actionPerformed(ActionEvent e) {
//					int returnVal = chooser.showOpenDialog(frame);
//					if(returnVal == JFileChooser.APPROVE_OPTION){
//						try {
//							importTest(chooser.getSelectedFile());
//						} catch (IOException e1) {
//							e1.printStackTrace();
//						}
//					}
//				}
//			});
//			importTest.setToolTipText("Imports a test from the file system to the server. Not currently implemented and will need quite a bit of backend to work");
			
			JMenuItem logOut = new JMenuItem("Log Out");
			file.add(logOut);
			logOut.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					frame.setVisible(false);
					Login.main(null);
					frame.dispose();
				}
			});
			
			JMenuItem exit = new JMenuItem("Exit");
			file.add(exit);
			exit.setToolTipText("Exits the program");
			exit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E,
					KeyEvent.CTRL_MASK));
			exit.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					System.exit(0);
				}
			});
		}
		
		public void addEditMenu(){
			JMenu edit = new JMenu(" Edit ");
			jMenuBar.add(edit);
			edit.setBackground(null);
			edit.setForeground(Colors.MAJOR_BAR_TEXT);
			edit.setFont(Constants.JMENUBAR_FONT);
			edit.setVerticalAlignment(JMenu.CENTER);
			
			cut = defaultJMenuItem("Cut Question");
			edit.add(cut);
			cut.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, KeyEvent.SHIFT_MASK));
			cut.setToolTipText("Adds the copied question to the current test");
			cut.setEnabled(false);
			cut.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					try{
						copiedQuestion = holder.getHolder().getQuestion();
						holder.getSelector().removeQuestion(copiedQuestion);
						holder.getHolder().removeQuestion();
						holder.getHolder().revalidate();
					}catch(NullPointerException ex){
						ex.printStackTrace();
					}
					
				}
			});
			
			copy = defaultJMenuItem("Copy Question");
			edit.add(copy);
			copy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C,
					KeyEvent.SHIFT_MASK));
			copy.setToolTipText("Holds the current selected question");
			copy.setEnabled(false);
			copy.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					copiedQuestion = holder.getHolder().getQuestion();
				}
			});
			
			paste = defaultJMenuItem("Paste Question");
			edit.add(paste);
			paste.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, KeyEvent.SHIFT_MASK));
			paste.setToolTipText("Adds the copied question to the current test");
			paste.setEnabled(false);
			paste.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					ArrayList<String> questionData = new ArrayList<String>();
					if(copiedQuestion != null){
						questionData.add(copiedQuestion.getQuestionText());
						for(String s : copiedQuestion.getAnswerChoices()){
							questionData.add(s);
						}
						holder.getSelector().newMultCh(questionData);
						holder.getSelector().getQuestions().get(holder.getSelector().getQuestions().size()-1).getNumberHolder().refreshText();
						holder.revalidate();
					}
					
				}
			});
			
			delete = new JMenuItem("Delete Question");
			edit.add(delete);
			delete.setToolTipText("Deletes the currently selected question");
			delete.setEnabled(false);
			delete.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					holder.getSelector().removeQuestion(holder.getHolder().getQuestion());
					holder.getHolder().removeQuestion();
					holder.getHolder().revalidate();
				}
			});
			
			deleteAssignment = defaultJMenuItem("Delete Assignment");
			edit.add(deleteAssignment);
			deleteAssignment.setToolTipText("Delets the currently Assignment");
			deleteAssignment.setEnabled(false);
			deleteAssignment.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (selector.getTreePath() != null){
						if(selector.getTreePath().getPathCount() == 3) {
							if(JOptionPane.showConfirmDialog(frame,
									"You have unsaved changes. Would you like to save them?", "Save changes?",
									JOptionPane.YES_NO_OPTION,
									JOptionPane.QUESTION_MESSAGE) == JOptionPane.OK_OPTION){
								//deleteAssignment(selector.getTreePath());
							}
						}
					}else{
						JOptionPane.showMessageDialog(frame, "Please select the class that you want to add the test to.", "Warning", JOptionPane.ERROR_MESSAGE);
					}
				}
			});
		}
		
		public void addNewMenu(){
			JMenu newMenu = new JMenu(" New ");
			jMenuBar.add(newMenu);
			
			JMenuItem newAssignment = new JMenuItem("Assignment");
			newMenu.add(newAssignment);
			newAssignment.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_MASK));
			newAssignment.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					if (selector.getTreePath() != null){
						if(selector.getTreePath().getPathCount() == 2) {
							String inputValue = JOptionPane.showInputDialog("Enter a name for this assignment:");
							if(inputValue != null){
								try {
									createNewTest(inputValue);
								} catch (IOException e) {
									e.printStackTrace();
								}
							}
						}else{
							JOptionPane.showMessageDialog(frame, "Please select the class that you want to add the test to.", "Warning", JOptionPane.ERROR_MESSAGE);
						}
					}else{
						JOptionPane.showMessageDialog(frame, "Please select the class that you want to add the test to.", "Warning", JOptionPane.ERROR_MESSAGE);
					}
				}
			});
		}

		public void addViewMenu(){
			view = new JMenu(" View ");
			jMenuBar.add(view);
			view.setVisible(false);

			JMenu setTextSize = new JMenu("Set Text Size");
			view.add(setTextSize);

			for (int i = 8; i <= 46; i += 2) {
				JMenuItem setSize = defaultJMenuItem(i + "");
				setTextSize.add(setSize);
				final int temp = i;
				setSize.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent arg0) {
						for(Question question : holder.getSelector().getQuestions())
							question.setTextSize(temp);
					}
				});
			}
		}
		
		public void addTestOptionMenu(){
			options = new JMenu(" Test Options ");
			jMenuBar.add(options);
			options.setVisible(false);
			options.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
						System.out.println("Pressed");
				}
			});
			
			timeLimit = new JMenu("Time Limit");
			options.add(timeLimit);
			
			timeLimitGroup = new ButtonGroup();
			timeLimitButtons = new ArrayList<TimeLimitButton>();
			
			TimeLimitButton noTimeLimit = new TimeLimitButton("No time Limit", true, 0);
			timeLimit.add(noTimeLimit);
			timeLimitGroup.add(noTimeLimit);
			timeLimitButtons.add(noTimeLimit);
			
			for (int i = 5; i <= 55; i += 5) {
				TimeLimitButton setSize = new TimeLimitButton(i + " minutes", i);
				timeLimit.add(setSize);
				timeLimitGroup.add(setSize);
				timeLimitButtons.add(setSize);
			}
			
			TimeLimitButton hour = new TimeLimitButton("1 hour", 60);
			timeLimit.add(hour);
			timeLimitGroup.add(hour);
			timeLimitButtons.add(hour);
			
			for (int i = 5; i <= 55; i += 5) {
				TimeLimitButton setSize = new TimeLimitButton("1 hour, "+ i + " minutes", i+60);
				timeLimit.add(setSize);
				timeLimitGroup.add(setSize);
				timeLimitButtons.add(setSize);
			}
			
			ableToOpenOtherAssignments = new JCheckBoxMenuItem("Able to open other assignments");
			ableToOpenOtherAssignments.setToolTipText("Allows the student to open other assignments while taking this assignment");
			options.add(ableToOpenOtherAssignments);
		}
		
		public void startAllowingSubmissions() {
			submit.setEnabled(true);
			submit.setVisible(true);
		}

		public void stopAllowingSubmissions() {
			submit.setEnabled(false);
			submit.setVisible(false);
		}

		public void setWhiteSpaceText(String text) {
			gradeLabel.setText(text);
		}
		
		public boolean ableToOpenOtherAssignments(){
			return ableToOpenOtherAssignments.isSelected();
		}
	
		public void addConnectionStatus(){
			connectionStatus = new JLabel(" Connected ");
			connectionStatus.setForeground(Colors.GOOD_CONNECTION);
			connectionStatus.setBackground(Colors.MAJOR_BAR_BACKGROUND);
			connectionStatus.setFont(Constants.JMENUBAR_FONT);
			jMenuBar.add(connectionStatus);
			ActionListener listener = new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					System.out.println("Main teacher window timer triggered");
					ftpClient.updateConnectionStatus(connectionStatus);
				}			
			};
			Timer timer = new Timer(1000, listener);
			//Set update to run every 10 seconds
			timer.setDelay(10000);
			timer.start();
		}
		
		class CountDownTimer{
			JLabel timeLabel;
			int timeRemaining;
			Timer timer;
			public CountDownTimer(){
				timeLabel = new JLabel();
				timeLabel.setFont(Constants.JMENUBAR_FONT);
				timeLabel.setForeground(Colors.MAJOR_BAR_TEXT);
				timer = new Timer(60000, new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent arg0) {
						decrementTime();
					}
				});
			}
			
			public JLabel getLabel(){
				return timeLabel;
			}
			
			public void setTime(int time){
				timeRemaining = time;
				refreshTimeLabel();
			}
			
			public void startTimer(){
				timer.start();
				refreshTimeLabel();
			}
			
			public void stopTimer(){
				timer.stop();
				timeLabel.setText("");
			}
			
			public void decrementTime(){
				timeRemaining--;
				refreshTimeLabel();
				if(getTime() <= 0){
					stopTimer();
					try {
						submitTest(false);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			
			public int getTime(){
				return timeRemaining;
			}
			
			public void refreshTimeLabel(){
				String outputText = "Time Remaining ";
				if(timeRemaining >= 60){
					int hours = timeRemaining-(timeRemaining%60/60);
					outputText+=hours+":";
				}else{
					outputText += "0:";
				}
				if(timeRemaining % 60 < 10){
					outputText += "0"+timeRemaining % 60;
				}else{
					outputText += timeRemaining % 60;
				}
				timeLabel.setText(outputText);
			}
		}
		
		class TimeLimitButton extends JRadioButtonMenuItem{
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;
			private int time;
			TimeLimitButton(String menuText, int time){
				super(menuText);
				this.time = time;
			}
			
			TimeLimitButton(String menuText, boolean startSelected, int time){
				super(menuText, startSelected);
				this.time = time;
			}
			public int getTime(){
				return time;
			}
		}
		
		public void addUsername() {
			JLabel username = new JLabel(" "+name[0]+" "+name[1]+" ");
			username.setForeground(Colors.MAJOR_BAR_TEXT);
			username.setBackground(Colors.MAJOR_BAR_BACKGROUND);
			username.setFont(Constants.JMENUBAR_FONT);
			jMenuBar.add(username);
		}
		
		public int getTimeLimit(){
			for(TimeLimitButton button : timeLimitButtons){
				if(button.isSelected()){
					return button.getTime();
				}
			}
			return 0;
		}

		public void setTimeLimit(int timeLimit){
			if(MainWindow.isTeacher){
				//Resets the time limit
				for(TimeLimitButton button : timeLimitButtons){
					if(button.getTime() == 0){
						button.setSelected(true);
					}
				}
				
				//Sets the time limit
				for(TimeLimitButton button : timeLimitButtons){
					if(button.getTime() == timeLimit){
						button.setSelected(true);
					}
				}
			}else{
				countDownTimer.setTime(timeLimit);
				countDownTimer.startTimer();
			}
		}
		
		public JMenuBar getBar() {
			return jMenuBar;
		}
		
		public void addExtraMenus(){
			if(!alreadyAdded){
				grade.setEnabled(true);
				save.setEnabled(true);
				copy.setEnabled(true);
				paste.setEnabled(true);
				cut.setEnabled(true);
				delete.setEnabled(true);
				view.setVisible(true);
				options.setVisible(true);
				publish.setEnabled(true);
				alreadyAdded = true;
			}
		}
		
		public void removeExtraMenus(){
			if(alreadyAdded){
				grade.setEnabled(false);
				save.setEnabled(false);
				copy.setEnabled(false);
				paste.setEnabled(false);
				cut.setEnabled(false);
				delete.setEnabled(false);
				view.setVisible(false);
				options.setVisible(false);
				publish.setEnabled(false);
				alreadyAdded = false;
			}
		}

		public void setAbleToOpenOther(boolean able) {
			ableToOpenOtherAssignments.setSelected(able);
		}
	}
	
	public MenuBar getMenuBar(){
		return menuBar;
	}
	
	public void setTimer(int time){
		menuBar.setTimeLimit(time);
	}

	public void setAbleToOpenOther(boolean able) {
		ableToOpenOtherAssignments = able;
		if(MainWindow.isTeacher){
			menuBar.setAbleToOpenOther(able);
		}
	}
}
