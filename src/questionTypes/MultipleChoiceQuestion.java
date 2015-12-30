package questionTypes;

import java.awt.BorderLayout;
import java.awt.Component;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;

import utilities.Colors;

/**
 * Creates a Multiple Choice Question
 * @author Mark Wiggans
 */
public class MultipleChoiceQuestion extends QuestionWithResponses {
	private ArrayList<AnswerChoice> answers;
	
	private ButtonGroup group;
	private JPanel centerContainer;
	private JPanel answersContainer;
	private ArrayList<Component> answerSpacers;
	
	/**
	 * Creates a new MultipleChoiceQuestion
	 */
	public MultipleChoiceQuestion(){
		super(DEFAULT_QUESTION);
		answers = new ArrayList<AnswerChoice>();
		for(int i = 0; i < 4; i ++){
			answers.add(new AnswerChoice(this));
		}
		this.init();
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
		super(questionData.get(0));
		answers = new ArrayList<AnswerChoice>();	
		
		if(questionData.size() != 0){
			question = questionData.get(0);
			for(int i = 1; i < questionData.size( ); i++){
				answers.add(new AnswerChoice(questionData.get(i), this));
			}
		}else{
			question = "";
		}
		this.init();
	}

	/**
	 * Initializes the question
	 */
	private void init(){
		centerContainer = new JPanel();
		centerContainer.setBackground(Colors.QUESTION_BACKGROUND);
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
		
		getPanel().add(centerContainer, BorderLayout.CENTER);
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

	@Override
	protected void setResponseEditable(boolean editable) {
		for(AnswerChoice ans : answers) {
			ans.setRadioEnabled(editable);
		}
	}
	
	/**
	 * Gets which radio is selected
	 * @return the number of the selected radio
	 */
	private int getSelectedRadio() {
		for(int i = 0; i < group.getButtonCount(); i++){
			if(answers.get(i).getRadio().getSelectedObjects() != null){
				return i;
			}
		}
		return -1;
	}

	@Override
	public String getAnswerChoice() {
		int i = getSelectedRadio();
		if(i == -1) {
			return null;
		}
		return i + "";
	}

	@Override
	protected JPanel getAnswersContainer() {
		return centerContainer;
	}
	
	@Override
	public void setAnswerChoice(String answerChoice) {
		Integer intAns = Integer.parseInt(answerChoice);
		if(intAns >= 0 && intAns < answers.size()){
			group.setSelected(answers.get(intAns).getRadio().getModel(), true);
		}
	}

	@Override
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

	@Override
	public void setCorrectAnswer(String correctAnswer, boolean showCorrect) {
		numHolder.questionLabel.setVisible(true);
		if(correctAnswer.equals(getAnswerChoice())) {
			if(showCorrect) {
				numHolder.questionLabel.setText("Correct");
			}
			pointsRecieved = getPointsOutOf();
		} else {
			if(showCorrect) {
				numHolder.questionLabel.setText("Incorrect");
			}
			pointsRecieved = 0;
		}
		if(showCorrect) {
			answers.get(getSelectedRadio()).setTextBackgroundColor(Colors.INCORRECT_ANSWER);
			answers.get(Integer.parseInt(correctAnswer)).setTextBackgroundColor(Colors.CORRECT_ANSWER);
		}
		
	}

	@Override
	public void setPercentageGotRight(int answerInt, String string) {
		answers.get(answerInt).setPercentageGotRight(string);
	}
}