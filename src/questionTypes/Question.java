package questionTypes;

import java.util.ArrayList;

import javax.swing.JPanel;

import interfaceComponents.TestHolder;

/**
 * The interface that all types of question must inherit from so that
 * it can be displayed correctly
 * @author Mark Wiggans
 */
public abstract class Question {
	public static final String DEFAULT_QUESTION = "Enter a question here....";
	
	private int pointsOutOf;
	protected int pointsRecieved;
	protected int questionNumber;
	protected NumberHolder numHolder; 
	private boolean changesMade;
	
	/**
	 * Instantiates some of the fields necessary for a question
	 */
	public Question(int questionNumber, int pointsOutOf) {
		this.questionNumber = questionNumber;
		numHolder = new NumberHolder(questionNumber);
		numHolder.setQuestion(this);
		pointsOutOf = 0;
	}
	
	/**
	 * Gets the question number
	 * @return the question number
	 */
	public int getQuestionNumber() {
		return questionNumber;
	}
	
	/**
	 * Gets how many points the user scored on the question
	 * @return how many points they got
	 */
	public int pointsRecieved() {
		return pointsRecieved;
	}
	
	/**
	 * Gets how many points the question is out of
	 * @return the number of points
	 */
	public int getPointsOutOf() {
		return pointsOutOf;
	}
	
	/**
	 * Sets how many points the question is out of
	 * @param numPoints the points it is out of
	 */
	public void setPointsOutOf(int numPoints) {
		pointsOutOf = numPoints;
	}
	
	/**
	 * Checks if the question needs to be saved
	 */
	public boolean changesMade(){
		return changesMade;
	}
	
	/**
	 * Marks the question as being saved
	 */
	public void saved(){
		changesMade = false;
	}
	
	/**
	 * Marks the question as having changed since its last save
	 * Needs to be package level so that number 
	 */
	void madeChanges(){
		changesMade = true;
	}
	
	public void addActionListener(TestHolder holder){
		numHolder.setTestHolder(holder);
		numHolder.addMouseListener();
	}
	
	public NumberHolder getNumberHolder() {
		return numHolder;
	}
	
	public abstract void setEditable(boolean paneEditable, boolean radioEditable);
	
	public abstract String getQuestionText();
	
	/**
	 * 
	 * @return
	 */
	public abstract ArrayList<String> getAnswerChoices();
	
	
	
	/**
	 * Transfers focus to the given answer choice
	 * @param ans the answer choice to transfer focus to
	 */
	public abstract void transferFocus(AnswerChoice ans);
	
	/**
	 * Adds an answer choice to the end
	 */
	public abstract void addAnswerChoice();
	
	/**
	 * Removes an answer choice from the end 
	 */
	public abstract void removeAnswerChoice();

	public abstract JPanel getPanel();
	
	public abstract void setQuestionNumber(int questionNumber);
	
	public abstract String getAnswerChoice();
	public abstract void setAnswerChoice(String selection);
	
	public abstract void setCorrectAnswer(String correctAnswer, boolean showCorrect);
	
	public abstract void setPercentageGotRight(int answerInt, String string);
}
