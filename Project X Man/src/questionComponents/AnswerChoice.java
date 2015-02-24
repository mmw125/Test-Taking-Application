package questionComponents;

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

import questionTypes.Question;
import utilities.Colors;

public class AnswerChoice{
	private JRadioButton radio;
	private JPanel answerPanel;
	private JTextArea pane;
	private Question question;
	private AnswerChoice thisAnswerChoice;
	private JLabel percentageLabel;
	
	public AnswerChoice(Question q){
		this.pane = new JTextArea();
		question = q;
		this.init();
	}
	
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
	
	public JRadioButton getRadio(){
		return this.radio;
	}	
	
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
					pane.setText(pane.getText().substring(0, pane.getText().length()-1));
					JOptionPane.showMessageDialog(null, "You cannot use colons in this program");
					e.consume();
				}
				if(e.getKeyChar() == KeyEvent.VK_ENTER){
					pane.setText(pane.getText().substring(0, pane.getText().length()-1));
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
	
	public void setPaneEditable(Boolean editable){
		pane.setEditable(editable);
	}
	
	public void setRadioEnabled(Boolean enabled){
		radio.setEnabled(enabled);
	}
	
	public String getAnswerText(){
		return pane.getText();
	}
	
	public void setTextSize(int size) {
		Font font = pane.getFont();
		pane.setFont(new Font(font.getFontName(), font.getStyle(), size));
	}
	
	public void setTextBackgroundColor(Color color){
		pane.setBackground(color);
	}
	
	public void setSelected(){
		pane.requestFocus();
	}
	
	public void setPercentageGotRight(String percentage){
		percentageLabel.setText(percentage);
		percentageLabel.setPreferredSize(new Dimension(55, 28));
	}
}
