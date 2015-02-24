package interfaceComponents;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.tree.TreePath;

import mainWindow.MainWindow;
import questionTypes.Question;
import utilities.Colors;
import utilities.Constants;
import utilities.JMenu;
import utilities.JMenuBar;
import utilities.JMenuItem;
import utilities.TeacherConverters;
import ftp.DataHolder.Student;
import ftp.FTPConnection;

/**
 * This class grades the student's submissions
 * @author Mark
 */
public class Grader implements TestHolderHolder{
	
	private TreePath path;
	private ArrayList<Student> students;
	private JFrame frame;
	private FTPConnection ftp;
	private ArrayList<Integer> answers;
	private String username;
	private GradeDisplay display;
	private TestHolder holder;
	private JPanel background;
	private ArrayList<GradedStudent> gradedStudents;
	
	/**
	 * Sets the visibility of the frame
	 * @param visible
	 */
	public void setVisible(boolean visible){
		frame.setVisible(visible);
	}
	
	/**
	 * Creates a grader with the given parameters
	 * @param path		Where the test is located
	 * @param students	The students in the class
	 * @param ftp		The ftpClient
	 * @param username	The teacher's username
	 */
	public Grader(TreePath path, ArrayList<Student> students, FTPConnection ftp, String username){
		frame = new JFrame();
		this.path = path;
		this.students = sortByLastName(students);
		this.ftp = ftp;
		this.username = username;
		display = new GradeDisplay();
		answers = new ArrayList<Integer>();
		init();
	}
	
	/**
	 * Sorts an array of students by their last name
	 * @param studentArray	the array to be sorted
	 * @return	the sorted array
	 */
	public ArrayList<Student> sortByLastName(ArrayList<Student> studentArray){
		ArrayList<Student> outputArray = new ArrayList<Student>();
		while(studentArray.size() > 0){
			Student heldStudent = studentArray.get(0);
			for(Student currentStudent : studentArray){
				if(heldStudent.getLastName().compareTo(currentStudent.getLastName()) > 0){
					heldStudent = currentStudent;
				}
			}
			outputArray.add(heldStudent);
			studentArray.remove(heldStudent);
		}
		return outputArray;
	}
	
	private void createAndAddMenuBar(){
		JMenuBar bar = new JMenuBar();
		bar.setSize(1, Constants.JMENUBAR_HEIGHT);
		bar.setBackground(Colors.MAJOR_BAR_BACKGROUND);
		frame.setJMenuBar(bar);
		
		JMenu export = new JMenu("Export");
		bar.add(export);
		JMenuItem exportToCSV = MainWindow.defaultJMenuItem("to CSV");
		export.add(exportToCSV);
		exportToCSV.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				int returnVal = chooser.showSaveDialog(frame);
				if(returnVal == JFileChooser.APPROVE_OPTION){
					generateCsvFile(chooser.getSelectedFile());
				}
			}
		});
		System.out.println("Added MenuBar");
	}
	
	private void generateCsvFile(File f){
		try
		{
			f.createNewFile();
		    FileWriter writer = new FileWriter(f);
	 
		    for(GradedStudent graded : gradedStudents){
		    	writer.append(graded.getUsername()+",");
		    	writer.append(""+graded.getGrade());
		    	writer.append('\n');
		    }	 
		    writer.flush();
		    writer.close();
		    System.out.println("Saved to "+f.getAbsolutePath());
		}
		catch(IOException e)
		{
		     e.printStackTrace();
		} 
	}
	
	/**
	 * Creates the window
	 */
	private void init(){
		ImageIcon icon = new ImageIcon("logo.png");
		frame.setIconImage(icon.getImage());
		frame.setMinimumSize(new Dimension(1200, 680));
		frame.setResizable(true);
		frame.setVisible(true);
		frame.setExtendedState(frame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout());
		int gap = 25;
		frame.getContentPane().setBackground(Colors.FRAME_BACKGROUND);
		frame.getContentPane().add(Box.createVerticalStrut(gap),
				BorderLayout.NORTH);
		frame.getContentPane().add(Box.createVerticalStrut(gap),
				BorderLayout.SOUTH);
		frame.getContentPane().add(Box.createHorizontalStrut(gap),
				BorderLayout.EAST);
		frame.getContentPane().add(Box.createHorizontalStrut(gap),
				BorderLayout.WEST);
		background = new JPanel();
		background.setBackground(Colors.FRAME_BACKGROUND);
		frame.getContentPane().add(background, BorderLayout.CENTER);
		JPanel displayContainer = new JPanel();
		displayContainer.setLayout(new BorderLayout());
		displayContainer.setBackground(Colors.FRAME_BACKGROUND);
		displayContainer.add(Box.createHorizontalStrut(15), BorderLayout.EAST);
		displayContainer.add(display, BorderLayout.CENTER);
		background.setLayout(new BorderLayout());
		background.add(displayContainer, BorderLayout.WEST);
		try {
			importTest(path);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		try {
			gradeTests();
		} catch (IOException e) {
			e.printStackTrace();
		}
		createAndAddMenuBar();
	}
	
	public void importTest(TreePath testPath) throws IOException {
		if(holder != null){
			background.remove(holder);
		}
		holder = new TestHolder(this, ftp);
		background.add(holder, BorderLayout.CENTER);
		holder.importTest(testPath, ftp, username);
		
		Object[] splitPath = testPath.getPath();
		display.setPathLabel("Results: "+splitPath[1]+" | "+splitPath[2]);
		frame.setTitle("Results: "+splitPath[1]+" | "+splitPath[2]);
		
		holder.importKey(testPath, ftp, username);
		holder.getSelector().setEditable(false, false);
		holder.getHolder().removeEditJMenu();
		holder.getSelector().refreshNumberHolderText();
		frame.revalidate();
		System.out.println("Imported test");
	}
	
	/**
	 * Grades the tests
	 * @throws IOException
	 */
	private void gradeTests() throws IOException{
		File file = new File("key.key");
		file.createNewFile();
		ftp.getFile(TeacherConverters.treePathToKeyPath(path, username), "key.key");
		Scanner scanner = new Scanner(file);
		while (scanner.hasNext()) {
			String string = scanner.nextLine();
			String[] splitString = string.split(" ");
			answers.add(Integer.parseInt(splitString[1]));
		}
		scanner.close();
		frame.revalidate();
		file.delete();
		int longestQuestion = 0;
		for(Question question : holder.getSelector().getQuestions()){
			if(question.getAnswerChoices().size() > longestQuestion){
				longestQuestion = question.getAnswerChoices().size();
			}
		}
		int[][] studentResponses = new int[answers.size()][longestQuestion];
		for(int x = 0; x < studentResponses.length; x++){
			for(int y = 0; y < studentResponses[0].length; y++){
				studentResponses[x][y] = 0;
			}
		}
		gradedStudents = new ArrayList<GradedStudent>();
		for(Student student : students){
			GradedStudent gradedStudent = new GradedStudent(student, answers.size());
			String subPath = TeacherConverters.treePathToString(path, username)+"/"+student.getUsername()+".sub";
			System.out.println(subPath);
			File studentSub = new File("submission.sub");
			studentSub.createNewFile();
			ftp.getFile(subPath, "submission.sub");
			scanner = new Scanner(studentSub);
			while (scanner.hasNext()) {
				String string = scanner.nextLine();
				String[] splitString = string.split(" ");
				studentResponses[Integer.parseInt(splitString[0])-1][Integer.parseInt(splitString[1])]++;
				gradedStudent.setAnswer(Integer.parseInt(splitString[0])-1, Integer.parseInt(splitString[1]));
			}
			gradedStudent.refreshGrade();
			display.addGradedStudent(gradedStudent, answers.size());
			gradedStudents.add(gradedStudent);
		}
		for(int questionInt = 0; questionInt < studentResponses.length; questionInt++){
			for(int answerInt = 0; answerInt < holder.getSelector().getQuestions().get(questionInt).getAnswerChoices().size(); answerInt++){
				int percentage = (int)((float)studentResponses[questionInt][answerInt]*100.0f / (float)students.size());
				holder.getSelector().getQuestions().get(questionInt).getAnswers().get(answerInt).setPercentageGotRight(percentage+"%");;
			}
		}
		
		if(display.getGradedStudents().size() != 0){
			double totalGrade = 0;
			for(GradedStudent graded : display.getGradedStudents()){
				totalGrade += graded.getGrade();
			}
			totalGrade /= (float)display.getGradedStudents().size();
			display.getAverageScore().setText((int)totalGrade+"%");
		}
		System.out.println("Graded Tests");
		frame.revalidate();
	}
	
	/**
	 * Displays all of the student's grades. Partially 
	 * duplicates some of the code from testSelector
	 * @author Mark
	 */
	public class GradeDisplay extends JPanel {
		private static final long serialVersionUID = 1L;
		private JPanel multChoicePanel = new JPanel(new GridLayout(0, 1, 0, 0));
		private JPanel questionHolder = new JPanel(new BorderLayout());
		private ArrayList<GradedStudent> gradedStudents;
		private JLabel pathLabel;
		private AverageScore averageScore;

		public ArrayList<GradedStudent> getGradedStudents(){
			return gradedStudents;
		}
		
		GradeDisplay() {
			gradedStudents = new ArrayList<GradedStudent>();
			this.init();
		}
		
		public void setPathLabel(String string) {
			pathLabel.setText(string);
		}

		public void addGradedStudent(GradedStudent graded, int testLength){
			gradedStudents.add(graded);
			multChoicePanel.add(graded.getHolder().getPanel());
			multChoicePanel.setPreferredSize(new Dimension(10, (gradedStudents.size()+1) * 30));
		}
		
		private void init() {
			setLayout(new BorderLayout());
			JPanel northContainer = new JPanel();
			northContainer.setBackground(Colors.TRANSPARENT);
			northContainer.setLayout(new BorderLayout());
			add(northContainer, BorderLayout.NORTH);
			
			JPanel pathLabelContainer = new JPanel();
			pathLabelContainer.setPreferredSize(new Dimension(250, 30));
			pathLabelContainer.setBackground(Colors.MINOR_BAR_BACKGROUND);
			pathLabelContainer.setLayout(new BorderLayout());
			//northContainer.add(pathLabelContainer, BorderLayout.CENTER);
			add(pathLabelContainer, BorderLayout.NORTH);
			
			pathLabel = new JLabel("Please Select A Test");
			pathLabel.setFont(Constants.MINOR_JMENUBAR_FONT);
			pathLabel.setVerticalAlignment(JLabel.CENTER);
			pathLabel.setHorizontalAlignment(JLabel.CENTER);
			pathLabel.setForeground(Colors.MINOR_BAR_FOREGROUND);
			pathLabelContainer.add(pathLabel, BorderLayout.CENTER);
			
			averageScore = new AverageScore();
			multChoicePanel.add(averageScore.getPanel());
			
			setPreferredSize(new Dimension(250, 250));
			JScrollPane selectorScroll = new JScrollPane(multChoicePanel);
			selectorScroll.setBackground(Colors.PANEL_BACKGROUND);
			add(selectorScroll, BorderLayout.CENTER);
			selectorScroll.setViewportView(questionHolder);
			multChoicePanel.setBackground(Colors.PANEL_BACKGROUND);
			questionHolder.add(multChoicePanel, BorderLayout.NORTH);
			questionHolder.setBackground(Colors.PANEL_BACKGROUND);
			multChoicePanel.setPreferredSize(new Dimension(10, 0));
			multChoicePanel.setBackground(Colors.PANEL_BACKGROUND);
			setBorder(BorderFactory.createEmptyBorder());
			selectorScroll.setBorder(BorderFactory.createEmptyBorder());
		}

		public void reassignGrades() {
			for (GradedStudent gradedStudent : gradedStudents) {
				gradedStudent.refreshGrade();
			}
		}
		
		public AverageScore getAverageScore(){
			return averageScore;
		}
		
		public void refreshNumberHolderText(){
			for(GradedStudent gradedStudent : gradedStudents) {
				gradedStudent.refreshGrade();
			}
			this.repaint();
		}
	}

	/**
	 * Holds the average score for all the students
	 * @author Mark
	 */
	public class AverageScore{
		private JLabel usernameLabel;
		private JPanel teacherNumberPanel;
		public JLabel gradeLabel;
		
		public AverageScore(){
			this.usernameLabel = new JLabel(" Average Grade");
			initHolder();
		}
		
//		public void setTextColor(Color color) {
//			gradeLabel.setForeground(color);
//		}

		public void initHolder(){
			teacherNumberPanel = new JPanel(new BorderLayout());
			teacherNumberPanel.setBackground(Colors.PANEL_BACKGROUND);
			teacherNumberPanel.setSize(40, 30);
			teacherNumberPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Colors.GRADER_SEPERATOR_LINE));
			usernameLabel.setFont(new Font("Ariel", Font.PLAIN, 16));
			usernameLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 7));
			usernameLabel.setForeground(Colors.PANEL_TEXT_FOREGROUND);
			teacherNumberPanel.add(usernameLabel, BorderLayout.WEST);
			JPanel gradeContainer = new JPanel();
			gradeContainer.setPreferredSize(new Dimension(75, 20));
			teacherNumberPanel.add(gradeContainer, BorderLayout.EAST);
			gradeContainer.setBackground(Colors.PANEL_BACKGROUND);
			gradeLabel = new JLabel();
			gradeLabel.setFont(new Font("Ariel", Font.PLAIN, 16));
			gradeContainer.setPreferredSize(new Dimension(65, 20));
			gradeLabel.setForeground(Colors.PANEL_TEXT_FOREGROUND);
			gradeLabel.setVerticalAlignment(JLabel.CENTER);
			gradeContainer.add(gradeLabel, BorderLayout.EAST);
			gradeContainer.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, Colors.GRADER_SEPERATOR_LINE));
		}
		
		public void setDimension(Dimension d){
			teacherNumberPanel.setPreferredSize(d);
		}
		
		public void setText(String grade){
			gradeLabel.setText(grade);
		}
		
		public JPanel getPanel(){
			return teacherNumberPanel;
		}		
	}
	
	/**
	 * Keeps a student and the student's responses together
	 * @author Mark
	 */
	public class GradedStudent{
		private Student student;
		private int[] responses;
		private GradeHolder holder;
		private int correctAnswers;
		private int grade;
		
		/**
		 * Creates a new GradedStudent object
		 * @param student		The student it should be associated with
		 * @param testLength	How many questions are in the test
		 */
		public GradedStudent(Student student, int testLength){
			this.student = student;
			responses = new int[testLength];
			for(int i = 0; i < testLength; i++){
				responses[i] = -1;
			}
			holder = new GradeHolder();
		}
		
		public int getGrade(){
			return grade;
		}
		
		public String getUsername(){
			return student.getUsername();
		}
		
		/**
		 * Sets the students answer for a question
		 * @param questionNumber	the question number
		 * @param answer	the student's response for that question
		 */
		public void setAnswer(int questionNumber, int answer){
			responses[questionNumber] = answer;
		}
		
		public GradeHolder getHolder(){
			return holder;
		}
		
		/**
		 * Calculates the student's grade and refreshes the gradeHolder
		 */
		public void refreshGrade(){
			correctAnswers = 0;
			boolean noResponses = true;
			for(int i = 0; i < responses.length; i++){
				if(responses[i] == answers.get(i)){
					correctAnswers++;
				}
				if(responses[i] != -1){
					noResponses = false;
				}
			}
			System.out.println("Number of correct Answers "+correctAnswers);
			float grade = (float)correctAnswers * 100.0f / (float)responses.length;
			holder.setText((int)grade + "%");
			this.grade = (int)grade;
			if(noResponses){
				grade = -1;
				holder.setText("--");
			}
			if(grade >= 60){
				holder.setTextColor(Colors.PASSING_SCORE);
			}else{
				holder.setTextColor(Colors.FAILING_SCORE);
			}
		}
		
		/**
		 * Holds the student's grade 
		 * @author Mark
		 */
		public class GradeHolder{
			private JLabel usernameLabel;
			private JPanel teacherNumberPanel;
			public JLabel gradeLabel;
			
			public GradeHolder(){
				this.usernameLabel = new JLabel(" "+student.getLastName()+", "+student.getFirstName()+" ");
				initHolder();
			}
			
			public void setTextColor(Color color) {
				gradeLabel.setForeground(color);
			}

			public void initHolder(){
				teacherNumberPanel = new JPanel(new BorderLayout());
				teacherNumberPanel.setBackground(Colors.PANEL_BACKGROUND);
				teacherNumberPanel.setSize(40, 30);
				usernameLabel.setFont(new Font("Ariel", Font.PLAIN, 16));
				usernameLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 7));
				usernameLabel.setForeground(Colors.PANEL_TEXT_FOREGROUND);
				teacherNumberPanel.add(usernameLabel, BorderLayout.WEST);
				JPanel gradeContainer = new JPanel();
				gradeContainer.setPreferredSize(new Dimension(75, 20));
				teacherNumberPanel.add(gradeContainer, BorderLayout.EAST);
				gradeContainer.setBackground(Colors.PANEL_BACKGROUND);
				gradeLabel = new JLabel();
				gradeLabel.setFont(new Font("Ariel", Font.PLAIN, 16));
				gradeContainer.setPreferredSize(new Dimension(65, 20));
				gradeLabel.setForeground(Colors.PANEL_TEXT_FOREGROUND);
				gradeLabel.setVerticalAlignment(JLabel.CENTER);
				gradeContainer.add(gradeLabel, BorderLayout.EAST);
				gradeContainer.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, Colors.GRADER_SEPERATOR_LINE));
			}
			
			public void setDimension(Dimension d){
				teacherNumberPanel.setPreferredSize(d);
			}
			
			public void setText(String grade){
				gradeLabel.setText(grade);
			}
			
			public JPanel getPanel(){
				return teacherNumberPanel;
			}		
		}
	}
	
	public void setTimer(int time){
		
	}

	@Override
	public void setAbleToOpenOther(boolean able) {
		// TODO Auto-generated method stub
		
	}
}
