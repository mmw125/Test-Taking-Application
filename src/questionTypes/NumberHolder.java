package questionTypes;

import interfaceComponents.TestHolder;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import utilities.Colors;

public class NumberHolder{
	private JLabel numberLabel;
	private JPanel teacherNumberPanel;
	public JLabel questionLabel;
	private TestHolder holder;
	private int questionNumber;
	private MouseListener currentMouseListener;
	private Question question;
	
	public NumberHolder(){
		this.numberLabel = new JLabel(" 1.");
		questionNumber = 1;
		initNumber();
	}
	public NumberHolder(int number){
		questionNumber = number;
		this.numberLabel = new JLabel(" "+number+".");
		initNumber();
	}
	
	private void initNumber(){
		teacherNumberPanel = new JPanel(new BorderLayout());
		teacherNumberPanel.setBackground(Colors.SELECTED);
		teacherNumberPanel.setSize(40, 40);
		numberLabel.setFont(new Font("Ariel", Font.PLAIN, 20));
		numberLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 7));
		numberLabel.setForeground(Colors.PANEL_TEXT_FOREGROUND);
		teacherNumberPanel.add(numberLabel, BorderLayout.WEST);
		questionLabel = new JLabel();
		questionLabel.setFont(new Font("Ariel", Font.PLAIN, 20));
		questionLabel.setForeground(Colors.PANEL_TEXT_FOREGROUND);
		teacherNumberPanel.add(questionLabel, BorderLayout.CENTER);
	}
	
	public void setQuestionNumber(int number){
		numberLabel.setText(" "+number+".");
		questionNumber = number;
		addMouseListener();
	}
	
	public void setQuestion(Question question){
		this.question = question;
	}
	
	public void setTestHolder(TestHolder holder){
		this.holder = holder;
	}
	
	public void setDimension(Dimension d){
		teacherNumberPanel.setPreferredSize(d);
	}
	
	public JPanel getPanel(){
		return teacherNumberPanel;
	}
	
	public void refreshText(){
		if (question.getQuestionText() != null && question.getQuestionText().equals("Enter a question here....")
				|| questionLabel.getText().equals("Correct") || questionLabel.getText().equals("Incorrect")){
		}else{
			questionLabel.setText(question.getQuestionText());
		}
	}
	
	public void setSelected(boolean selected){
		if(selected){
			teacherNumberPanel.setBackground(Colors.QUESTION_SELECTED);
			questionLabel.setForeground(Colors.SELECTED_TEXT_FOREGROUND);
			numberLabel.setForeground(Colors.SELECTED_TEXT_FOREGROUND);
		}else{
			teacherNumberPanel.setBackground(Colors.SELECTED);
			questionLabel.setForeground(Colors.PANEL_TEXT_FOREGROUND);
			numberLabel.setForeground(Colors.PANEL_TEXT_FOREGROUND);
		}
	}
	
	public void addMouseListener(){
		if(currentMouseListener != null){
			teacherNumberPanel.removeMouseListener(currentMouseListener);
		}
		currentMouseListener = createMouseListener(questionNumber);
		teacherNumberPanel.addMouseListener(currentMouseListener);
		
	}
	
	private MouseListener createMouseListener(final int questionNumber){
		return new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent arg0) {
				try{
					holder.getSelector().setSelected(questionNumber);
				}catch (Exception e){
					e.printStackTrace();
				}
			}
			
			@Override
			public void mousePressed(MouseEvent e) { }
			
			@Override
			public void mouseExited(MouseEvent e) { }
			
			@Override
			public void mouseEntered(MouseEvent e) { }
			
			@Override
			public void mouseClicked(MouseEvent e) { }
		};
	}
}
