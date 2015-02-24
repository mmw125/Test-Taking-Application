package questionTypes;

import java.util.ArrayList;

import javax.swing.JPanel;

import questionComponents.AnswerChoice;
import questionComponents.NumberHolder;

public interface Question {
	public void setQuestionNumber(int questionNumber);
	public void setCorrectAnswer(int correctAnswer, boolean showCorrect);
	public void setEditable(boolean editable, boolean editable2);
	public JPanel getPanel();
	public int getQuestionNumber();
	public NumberHolder getNumberHolder();
	public String getQuestionText();
	public int getAnswerChoice();
	public ArrayList<String> getAnswerChoices();
	public void addAnswerChoice();
	public void setSelected(int correctAnswer);
	public boolean isCorrect();
	public void setTextSize(int textSize);
	public void removeAnswerChoice();
	public void transferFocus(AnswerChoice ans);
	public boolean changesMade();
	public void saved();
	public void madeChanges();
	public ArrayList<AnswerChoice> getAnswers();
}
