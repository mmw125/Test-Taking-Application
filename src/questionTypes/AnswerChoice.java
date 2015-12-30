package questionTypes;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;

import utilities.Colors;

/**
 * Allows users to enter in what their answers is
 * @author Mark Wiggans
 */
class AnswerChoice{
	private JRadioButton radio;
	private JPanel answerPanel;
	private JTextArea pane;
	private Question question;
	private AnswerChoice thisAnswerChoice;
	private JLabel percentageLabel;
	
	/**
	 * Creates a new answer choice inside of the given question
	 * @param q the question that will hold this answer choice
	 */
	public AnswerChoice(Question q){
		this.pane = new JTextArea();
		question = q;
		this.init();
	}
	
	/**
	 * Creates a new answer choice with the given text and inside
	 * of the given question
	 * @param inText the text to put in the answer choice
	 * @param q question to put this answer choice in
	 */
	public AnswerChoice(String inText, Question q){
		pane = new JTextArea();
		question = q;
		if(inText.length() != 0){
			if(inText.startsWith(" ")){
				pane.setText(inText.substring(1));
			}else{
				pane.setText(inText);
			}
			
		}
		init();
	}
	
	/**
	 * Gets the internal radio button
	 * @return the radio button
	 */
	public JRadioButton getRadio(){
		return this.radio;
	}
	
	/**
	 * Initilizes the answer choice
	 */
	private void init(){
		pane.setFont(new Font("Ariel", Font.PLAIN, 28));
		pane.setBackground(Colors.ANSWER_BACKGROUND);
		
		answerPanel = new JPanel();
		answerPanel.setBackground(Colors.ANSWER_BACKGROUND);
		answerPanel.setLayout(new BorderLayout());
		answerPanel.setBorder(BorderFactory.createRaisedSoftBevelBorder());
		
		radio = new JRadioButton("");
		radio.setBackground(Colors.ANSWER_BACKGROUND);
		JPanel radioAndPercentageContainer = new JPanel();
		radioAndPercentageContainer.setLayout(new BorderLayout());
		percentageLabel = new JLabel();
		percentageLabel.setBackground(Colors.ANSWER_BACKGROUND);
		percentageLabel.setFont(new Font("Ariel", Font.PLAIN, 20));
		radioAndPercentageContainer.add(percentageLabel, BorderLayout.WEST);
		percentageLabel.setBackground(Colors.ANSWER_BACKGROUND);
		radioAndPercentageContainer.add(radio, BorderLayout.CENTER);
		answerPanel.add(radioAndPercentageContainer, BorderLayout.WEST);
		answerPanel.add(pane, BorderLayout.CENTER);
		thisAnswerChoice = this;
		pane.setLineWrap(true);
		pane.setWrapStyleWord(true);
		pane.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
				if(e.getKeyChar() == KeyEvent.VK_TAB){
					pane.setText(pane.getText().substring(0, pane.getText().length()-1));
					question.transferFocus(thisAnswerChoice);
					e.consume();
				}
				if(e.getKeyCode() == KeyEvent.VK_SEMICOLON && e.getModifiers() == KeyEvent.VK_SHIFT){
					pane.setText(pane.getText().substring(0, pane.getText().length() - 1));
					JOptionPane.showMessageDialog(null, "You cannot use colons in this program");
					e.consume();
				}
				if(e.getKeyChar() == KeyEvent.VK_ENTER){
					pane.setText(pane.getText().substring(0, pane.getText().length() - 1));
					JOptionPane.showMessageDialog(null, "You cannot use colons in this program");
					e.consume();
				}
				question.madeChanges();
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				
			}
			
			@Override
			public void keyPressed(KeyEvent arg0) {
				
			}
		});
	}
	
	public JPanel getPanel(){
		return this.answerPanel;
	}
	
	/**
	 * Allow the pane to be editable
	 * @param editable
	 */
	public void setPaneEditable(Boolean editable){
		pane.setEditable(editable);
	}
	
	/**
	 * Allows the radio to be selected
	 * @param enabled 
	 */
	public void setRadioEnabled(Boolean enabled){
		radio.setEnabled(enabled);
	}
	
	/**
	 * Gets the text of the answer
	 * @return the answer's text
	 */
	public String getAnswerText(){
		return pane.getText();
	}
	
	/**
	 * Sets the text's background color
	 * @param color the new background color for the text
	 */
	public void setTextBackgroundColor(Color color){
		pane.setBackground(color);
	}
	
	/**
	 * Try to give this pane the focus
	 */
	public void setSelected(){
		pane.requestFocus();
	}
	
	/**
	 * Sets what percentage of the users got this question right
	 * This is used in the gradebook class
	 * @param percentage the percentage of students that got it correct
	 */
	public void setPercentageGotRight(String percentage){
		percentageLabel.setText(percentage);
		percentageLabel.setPreferredSize(new Dimension(55, 28));
	}
}
