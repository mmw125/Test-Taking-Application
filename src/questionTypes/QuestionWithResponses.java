package questionTypes;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

import utilities.Colors;

public abstract class QuestionWithResponses extends Question {
	private JPanel panel;
	private JLabel questionNumberLabel;
	private JTextArea questionText;
	protected String question;
	

	public QuestionWithResponses(String question, int questionNumber, int pointsOutOf) {
		super(questionNumber, pointsOutOf);
		this.question = question;
		init();
	}
	
	public QuestionWithResponses(String question) {
		this(question, 0, 0);
	}
	
	private void init() {
		panel = new JPanel(new BorderLayout());
		
		JPanel headerContainer = new JPanel();
		headerContainer.setLayout(new BorderLayout());
		headerContainer.setBackground(Colors.QUESTION_HEADER_BACKGROUND);
		panel.add(headerContainer, BorderLayout.NORTH);
		
		JPanel questionContainer = new JPanel();
		questionContainer.setLayout(new BorderLayout());
		headerContainer.add(questionContainer, BorderLayout.CENTER);
		
		questionNumberLabel = new JLabel(getQuestionNumber() + "");
		questionNumberLabel.setBackground(null);
		questionContainer.add(questionNumberLabel, BorderLayout.WEST);
		questionContainer.setBackground(Colors.QUESTION_HEADER_BACKGROUND);
		questionNumberLabel.setHorizontalAlignment(SwingConstants.CENTER);
		questionNumberLabel.setFont(new Font("Ariel", Font.PLAIN, 35));
		questionNumberLabel.setForeground(Colors.QUESTION_NUMBER_FOREGROUND);
		questionNumberLabel.setBackground(Colors.TRANSPARENT);
		
		JPanel questionNumberContainer = new JPanel();
		questionNumberContainer.setBackground(Colors.TRANSPARENT);
		questionNumberContainer.setBorder(BorderFactory.createEmptyBorder(0, 4, 0, 6));
		questionContainer.add(questionNumberContainer, BorderLayout.WEST);
		questionNumberContainer.setLayout(new BorderLayout());
		
		JPanel extraSpaceOnRightOfQuestion = new JPanel();
		extraSpaceOnRightOfQuestion.setBackground(Colors.TRANSPARENT);
		extraSpaceOnRightOfQuestion.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
		questionContainer.add(extraSpaceOnRightOfQuestion, BorderLayout.EAST);
		
		questionNumberContainer.add(questionNumberLabel, BorderLayout.CENTER);
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
			public void keyTyped(KeyEvent e) { changesMade(); }
			@Override
			public void keyReleased(KeyEvent e) { }
			@Override
			public void keyPressed(KeyEvent arg0) { }
		});
		
		headerContainer.add(Box.createVerticalStrut(20), BorderLayout.SOUTH);
		headerContainer.add(Box.createVerticalStrut(20), BorderLayout.NORTH);
	}
	

	@Override
	public void setEditable(boolean paneEditable, boolean radioEditable) {
		questionText.setEditable(paneEditable);
		setResponseEditable(radioEditable);
	}
	
	/**
	 * 
	 * @return
	 */
	protected abstract JPanel getAnswersContainer();
	
	/**
	 * 
	 * @param editable
	 */
	protected abstract void setResponseEditable(boolean editable);
	

	@Override
	public String getQuestionText() {
		return questionText.getText();
	}
	
	@Override
	public JPanel getPanel() {
		return panel;
	}
	
	@Override
	public void setQuestionNumber(int questionNumber){
		this.questionNumber = questionNumber;
		numHolder.setQuestionNumber(questionNumber);
		questionNumberLabel.setText(questionNumber+"");
	}
}
