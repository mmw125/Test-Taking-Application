package questionTypes;

import interfaceComponents.TestHolder;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

import questionComponents.AnswerChoice;
import questionComponents.NumberHolder;
import utilities.Colors;

/**
 * Creates a Multiple Choice Question
 * @author Mark Wiggans
 */
public class MultipleChoiceQuestion implements Question{
	private int questionNumberInt;
	private JPanel panel;
	private String question;
	private ArrayList<AnswerChoice> answers;
	private JLabel questionNumber;
	private NumberHolder numberHolder;
	private JTextArea questionText;
	private ButtonGroup group;
	private JPanel answersContainer;
	private boolean isCorrect;
	private boolean changesMade = true;
	private ArrayList<Component> answerSpacers;
	
	public MultipleChoiceQuestion(){
		super();
		question = "Enter a question here....";
		questionNumber = new JLabel("0");
		answers = new ArrayList<AnswerChoice>();
		for(int i = 0; i < 4; i ++){
			answers.add(new AnswerChoice(this));
		}
		this.init();
	}
	
	protected void setQuestionText(String text){
		
	}
	
	/**
	 * Creates a multiple choice question with the given parameters
	 * @param questionData	the question data in the form
	 * 		Question
	 * 		Answer Choice 1
	 * 		Answer Choice 2
	 * 		and so on
	 */
	public MultipleChoiceQuestion(ArrayList<String> questionData) {
		answers = new ArrayList<AnswerChoice>();
		if(questionData.size() != 0){
			question = questionData.get(0);
			for(int i = 1; i < questionData.size( ); i++){
				answers.add(new AnswerChoice(questionData.get(i), this));
			}
			questionNumber = new JLabel("0");
			this.init();
		}else{
			question = "";
			questionNumber = new JLabel("0");
			this.init();
		}
	}

	/**
	 * Initializes the question
	 */
	private void init(){
		numberHolder = new NumberHolder(Integer.parseInt(questionNumber.getText()));
		numberHolder.setQuestion(this);
		questionNumber.setBackground(null);
		panel = new JPanel();
		panel.setLayout(new BorderLayout());
		
		JPanel headerContainer = new JPanel();
		headerContainer.setLayout(new BorderLayout());
		headerContainer.setBackground(Colors.QUESTION_HEADER_BACKGROUND);
		panel.add(headerContainer, BorderLayout.NORTH);
		
		JPanel questionContainer = new JPanel();
		questionContainer.setLayout(new BorderLayout());
		headerContainer.add(questionContainer, BorderLayout.CENTER);
		
		questionContainer.add(questionNumber, BorderLayout.WEST);
		questionContainer.setBackground(Colors.QUESTION_HEADER_BACKGROUND);
		questionNumber.setHorizontalAlignment(SwingConstants.CENTER);
		questionNumber.setFont(new Font("Ariel", Font.PLAIN, 35));
		questionNumber.setForeground(Colors.QUESTION_NUMBER_FOREGROUND);
		questionNumber.setBackground(Colors.TRANSPARENT);
		
		JPanel questionNumberContainer = new JPanel();
		questionNumberContainer.setBackground(Colors.TRANSPARENT);
		questionNumberContainer.setBorder(BorderFactory.createEmptyBorder(0, 4, 0, 6));
		questionContainer.add(questionNumberContainer, BorderLayout.WEST);
		questionNumberContainer.setLayout(new BorderLayout());
		
		JPanel extraSpaceOnRightOfQuestion = new JPanel();
		extraSpaceOnRightOfQuestion.setBackground(Colors.TRANSPARENT);
		extraSpaceOnRightOfQuestion.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
		questionContainer.add(extraSpaceOnRightOfQuestion, BorderLayout.EAST);
		
		questionNumberContainer.add(questionNumber, BorderLayout.CENTER);
		questionNumberContainer.add(Box.createHorizontalStrut(5), BorderLayout.WEST);
		questionNumberContainer.add(Box.createHorizontalStrut(5), BorderLayout.EAST);
		
		questionText = new JTextArea();
		questionText.setLineWrap(true);
		questionText.setWrapStyleWord(true);
		questionContainer.add(questionText, BorderLayout.CENTER);
		questionText.setEditable(true);
		questionText.setText(question);
		questionText.setBackground(Colors.QUESTION_BACKGROUND);
		questionText.setBorder(BorderFactory.createEmptyBorder(4, 5, 5, 4));
		questionText.setFont(new Font("Ariel", Font.PLAIN, 35));
		questionText.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
				changesMade();
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				
			}
			
			@Override
			public void keyPressed(KeyEvent arg0) {
				
			}
		});
		
		headerContainer.add(Box.createVerticalStrut(20), BorderLayout.SOUTH);
		headerContainer.add(Box.createVerticalStrut(20), BorderLayout.NORTH);
		
		JPanel centerContainer = new JPanel();
		centerContainer.setBackground(Colors.QUESTION_BACKGROUND);
		panel.add(centerContainer, BorderLayout.CENTER);
		centerContainer.setLayout(new BorderLayout());
		
		JPanel wholeAnswersContainer = new JPanel();
		centerContainer.add(wholeAnswersContainer, BorderLayout.NORTH);
		wholeAnswersContainer.setLayout(new BorderLayout());
		wholeAnswersContainer.setBackground(Colors.QUESTION_BACKGROUND);
		wholeAnswersContainer.add(Box.createHorizontalStrut(50), BorderLayout.WEST);
		wholeAnswersContainer.add(Box.createHorizontalStrut(50), BorderLayout.EAST);
		wholeAnswersContainer.setBorder(BorderFactory.createEmptyBorder(35, 0, 0, 0));
		
		wholeAnswersContainer.add(Box.createHorizontalStrut(50), BorderLayout.WEST);
		
		answersContainer = new JPanel();
		wholeAnswersContainer.add(answersContainer);
		answersContainer.setLayout(new BoxLayout(answersContainer, BoxLayout.Y_AXIS));
		answersContainer.setBackground(Colors.QUESTION_BACKGROUND);
		
		answerSpacers = new ArrayList<Component>();
		
		group = new ButtonGroup();
		for(int i = 0; i < answers.size(); i ++){
			if(i!=0){
				Component c = Box.createVerticalStrut(20);
				answersContainer.add(c);
				answerSpacers.add(c);
			}
			answersContainer.add(answers.get(i).getPanel());
			group.add(answers.get(i).getRadio());
		}		
	}
	
	/**
	 * Sets the question number
	 * @param questionNumber the new number for the question
	 */
	public void setQuestionNumber(int questionNumber){
		questionNumberInt = questionNumber;
		this.questionNumber.setText(questionNumber+"");
		numberHolder.setQuestionNumber(questionNumber);
	}
	
	/**
	 * Adds or removes the ability to edit questions
	 * @param editable should the question be editable
	 */
	public void setEditable(boolean paneEditable, boolean radioEditable){
		for(AnswerChoice answer : answers){
			answer.setPaneEditable(paneEditable);
			answer.setRadioEnabled(radioEditable);
		}
		questionText.setEditable(paneEditable);
	}
	
	/**
	 * Sets the correct answer for the question
	 * @param correctAnswer the correct answer for the question
	 */
	public void setCorrectAnswer(int correctAnswer, boolean showCorrect){
		numberHolder.questionLabel.setVisible(true);
		if(correctAnswer == getAnswerChoice()){
			if(showCorrect){
				numberHolder.questionLabel.setText("Correct");
			}
			
			isCorrect = true;
		}else{
			if(showCorrect){
				numberHolder.questionLabel.setText("Incorrect");
			}
			
			isCorrect = false;
		}
		for(int i = 0; i < answers.size(); i++){
			if(i == correctAnswer){
				answers.get(i).setTextBackgroundColor(Colors.CORRECT_ANSWER);
			}else if(i == getAnswerChoice()){
				answers.get(i).setTextBackgroundColor(Colors.INCORRECT_ANSWER);
			}
		}
	}
	
	/**
	 * Gets the question panel
	 * @return the question panel
	 */
	public JPanel getPanel(){
		return this.panel;
	}
	
	/**
	 * Gets the question number
	 * @return the question number
	 */
	public int getQuestionNumber(){
		return this.questionNumberInt;
	}
	
	/**
	 * Gets the number holder
	 */
	public NumberHolder getNumberHolder(){
		return numberHolder;
	}
	
	public void addActionListener(TestHolder holder){
		numberHolder.setTestHolder(holder);
		numberHolder.addMouseListener();
	}
	
	public String getQuestionText(){
		return questionText.getText();
	}
	
	/**
	 * Gets the possible answer choices for this question
	 * @return the answer choices
	 */
	public ArrayList<String> getAnswerChoices(){
		ArrayList<String> answerHolder = new ArrayList<String>();
		for(int i = 0; i < answers.size(); i++){
			answerHolder.add(answers.get(i).getAnswerText());
		}
		return answerHolder;
	}

	/**
	 * Adds an answer choice to the question
	 */
	public void addAnswerChoice() {
		Component c = Box.createVerticalStrut(20);
		answerSpacers.add(c);
		answersContainer.add(c);
		answers.add(new AnswerChoice(this));
		answersContainer.add(answers.get(answers.size()-1).getPanel());
		group.add(answers.get(answers.size()-1).getRadio());
	}

	/**
	 * 
	 */
	public void setTextSize(int size) {
		Font font = questionText.getFont();
		questionText.setFont(new Font(font.getName(), font.getStyle(), size));
		for(AnswerChoice choice : answers){
			choice.setTextSize(size);
		}	
	}
	
	public int getAnswerChoice(){
		for(int i = 0; i < group.getButtonCount(); i++){
			if(answers.get(i).getRadio().getSelectedObjects() != null){
				return i;
			}
		}
		return -1;
	}

//	public void setWidth(int width) {
//		questionText.setSize(width-500, 1);
//		questionText.setPreferredSize(questionText.getSize());
//		for(AnswerChoice answer : answers){
//			answer.resize(width);
//		}
//	}

	@Override
	public void setSelected(int correctAnswer) {
		if(correctAnswer >= 0 && correctAnswer < answers.size()){
			group.setSelected(answers.get(correctAnswer).getRadio().getModel(), true);
		}
	}

	@Override
	public boolean isCorrect() {
		return isCorrect;
	}
	
	public void removeAnswerChoice(){
		try{
			answersContainer.remove(answers.get(answers.size()-1).getPanel());
			answers.remove(answers.size()-1);
			answersContainer.remove(answerSpacers.get(answerSpacers.size()-1));
			answerSpacers.remove(answerSpacers.size()-1);
		}catch(Exception e){
			System.out.println("Exception Thrown");
		}
	}

	@Override
	public void transferFocus(AnswerChoice ans) {
		if(answers.indexOf(ans) < answers.size()-1){
			answers.get(answers.indexOf(ans)+1).setSelected();
		}
	}
	
	public boolean changesMade(){
		return changesMade;
	}
	
	public void saved(){
		changesMade = false;
	}
	
	public void madeChanges(){
		System.out.println("Changes made");
		changesMade = true;
	}

	@Override
	public ArrayList<AnswerChoice> getAnswers() {
		return answers;
	}
}