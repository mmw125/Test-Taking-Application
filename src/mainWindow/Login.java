package mainWindow;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.Timer;

import utilities.Colors;
import ftp.DataHolder.Student;
import ftp.FTPConnection;

/**
 * The login window
 * @author Andrew Storms
 */
public class Login extends JPanel {
	private static final long serialVersionUID = 1L;
	private JLabel usernameLabel;
	private JTextField usernameField;
	private JLabel passwordLabel;
	private JPasswordField passwordField;
	private JLabel enterUsername_Password1;
	private JLabel enterUsername_Password2;
	private JLabel loginSuccessful;
	private JLabel loginUnsuccessful;
	private JLabel pleaseLogin;
	private JLabel userSaved;
	private JButton loginButton;
	private FTPConnection ftpConnetion;
	private LoginFrame loginFrame;
	private JLabel connectionStatus;
	private Timer connectedTimer;

	/**
	 * Creates a new Login object and puts it inside of the given frame
	 * @param loginFrame the frame to put this inside of
	 */
	public Login(LoginFrame loginFrame) {

		this.loginFrame = loginFrame;
		int windowWidth = 850;
		int windowHeight = 700;
		
		Dimension size = getPreferredSize();
		size.width = 850;
		size.height = 700;
		setPreferredSize(new Dimension(windowWidth, windowHeight));
				
		setLayout(null);
		setBackground(Colors.TRANSPARENT);

		int loginButtonWidth = 330;
		int usernameLabelPositionx = (windowWidth - 330) / 2;
		int loginButtonPositionx = (windowWidth - loginButtonWidth) / 2;
		int enterUsername_Password1Positionx = (windowWidth - 390) / 2;
		int enterUsername_Password2Positionx = (windowWidth - 334) / 2;
		int pleaseLoginPositionx = (windowWidth - 133) / 2;
		int loginSuccessfulPositionx = (windowWidth - 119) / 2;
		int loginUnsuccessfulPositionx = (windowWidth - 218) / 2;
		int userSavedPositionx = (windowWidth - 77) / 2;
		int usernameLabelPositiony = 150;
		int usernameFieldPositionx = usernameLabelPositionx + 130;
		int usernameFieldPositiony = usernameLabelPositiony;
		int passwordLabelPositionx = usernameLabelPositionx + 6;
		int passwordLabelPositiony = usernameLabelPositiony + 50;
		int passwordFieldPositionx = usernameFieldPositionx;
		int passwordFieldPositiony = usernameLabelPositiony + 50;
		
		usernameLabel = new JLabel("Username:");
		usernameLabel.setFont (usernameLabel.getFont ().deriveFont (24.0f));
		usernameLabel.setForeground(Color.WHITE);
		usernameLabel.setLayout(null);
		usernameLabel.setBounds(usernameLabelPositionx, usernameLabelPositiony, 130, 40);
		add(usernameLabel);
		
		usernameField = new JTextField(20);
		usernameField.setFont (usernameField.getFont ().deriveFont (24.0f));
		usernameField.setLayout(null);
		usernameField.setBounds(usernameFieldPositionx, usernameFieldPositiony, 200, 40);
		usernameField.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
		add(usernameField);
		
		passwordLabel = new JLabel("Password:");
		passwordLabel.setFont (passwordLabel.getFont ().deriveFont (24.0f));
		passwordLabel.setForeground(Color.WHITE);
		passwordLabel.setLayout(null);
		passwordLabel.setBounds(passwordLabelPositionx, passwordLabelPositiony, 130, 40);
		add(passwordLabel);
		
		passwordField = new JPasswordField(20);
		passwordField.setFont (passwordField.getFont ().deriveFont (24.0f));
		passwordField.setLayout(null);
		passwordField.setBounds(passwordFieldPositionx, passwordFieldPositiony, 200, 40);
		passwordField.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
		//Allows you to hit enter to press the login button
		passwordField.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				loginButton.doClick();
			}
		});
		add(passwordField);
		
		enterUsername_Password1 = new JLabel("");
		enterUsername_Password1.setFont (enterUsername_Password1.getFont ().deriveFont (14.0f));
		enterUsername_Password1.setForeground(Color.WHITE);
		enterUsername_Password1.setLayout(null);
		enterUsername_Password1.setBounds(enterUsername_Password1Positionx, 90, 600, 40);
		add(enterUsername_Password1);
		
		enterUsername_Password2 = new JLabel("");
		enterUsername_Password2.setFont (enterUsername_Password2.getFont ().deriveFont (14.0f));
		enterUsername_Password2.setForeground(Color.WHITE);
		enterUsername_Password2.setLayout(null);
		enterUsername_Password2.setBounds(enterUsername_Password2Positionx, 110, 600, 40);
		add(enterUsername_Password2);
		
		pleaseLogin = new JLabel("Please log in below");
		pleaseLogin.setFont (pleaseLogin.getFont ().deriveFont (14.0f));
		pleaseLogin.setForeground(Color.WHITE);
		pleaseLogin.setLayout(null);
		pleaseLogin.setBounds(pleaseLoginPositionx, 40, 600, 40);
		
		loginSuccessful = new JLabel("");
		loginSuccessful.setFont (loginSuccessful.getFont ().deriveFont (14.0f));
		loginSuccessful.setLayout(null);
		loginSuccessful.setBounds(loginSuccessfulPositionx, 190, 600, 40);
		add(loginSuccessful);
		
		loginUnsuccessful = new JLabel("");
		loginUnsuccessful.setFont (loginUnsuccessful.getFont ().deriveFont (14.0f));
		loginUnsuccessful.setForeground(Color.WHITE);
		loginUnsuccessful.setLayout(null);
		loginUnsuccessful.setBackground(Color.BLACK);
		loginUnsuccessful.setBounds(loginUnsuccessfulPositionx, 240, 600, 40);
		add(loginUnsuccessful);
		
		userSaved = new JLabel("");
		userSaved.setFont (userSaved.getFont ().deriveFont (14.0f));
		userSaved.setForeground(Color.WHITE);
		userSaved.setLayout(null);
		userSaved.setBounds(userSavedPositionx, 190, 600, 40);
		add(userSaved);
		
		loginButton = new JButton("Login");
		loginButton.setFont (loginButton.getFont ().deriveFont (24.0f));
		loginButton.setLayout(null);
		loginButton.setBounds(loginButtonPositionx, passwordLabelPositiony+80, loginButtonWidth, 40);
		ActionListener LoginButtonListener = new LoginButtonListener();
		loginButton.addActionListener(LoginButtonListener);
		add(loginButton);

		ftpConnetion = new FTPConnection();
		
		connectionStatus = new JLabel();
		connectionStatus.setFont(new Font(Font.DIALOG, Font.PLAIN, 20));
		connectionStatus.setForeground(Color.WHITE);
		connectionStatus.setBackground(Color.BLACK);
		connectionStatus.setBounds(10, 638, 200, 20);
		add(connectionStatus);
		ActionListener listener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				updateConnectionStatus();
			}			
		};
		updateConnectionStatus();
		connectedTimer = new Timer(5000, listener);
		//Set update to run every 5 seconds
		connectedTimer.setDelay(5000);
		connectedTimer.start();
		
		setVisible(true);

		addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					loginButton.doClick();
				}

			}

			@Override
			public void keyReleased(KeyEvent e) { }

			@Override
			public void keyPressed(KeyEvent arg0) { }
		});
	}
	
	public void updateConnectionStatus(){
		System.out.println("Checking if connected");
		if(ftpConnetion.isConnected()){
			connectionStatus.setText(" Connected ");
			System.out.println("Connected");
			connectionStatus.setForeground(Color.GREEN);
			loginFrame.revalidate();
		}else if(ftpConnetion.connectToServer()){
			connectionStatus.setText(" Connected ");
			System.out.println("Connected when updated");
			connectionStatus.setForeground(Color.GREEN);
			loginFrame.revalidate();
		}else{
			connectionStatus.setText(" Not Connected ");
			connectionStatus.setForeground(Color.RED);
			loginFrame.revalidate();
		}
	}

	public class LoginButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == loginButton) {
				setCursor (Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				if (ftpConnetion.isConnected()) {
					try {
						if (ftpConnetion.getDataHolder().correctLoginForStudent(usernameField.getText(), new String(passwordField.getPassword()))) {
							Student student = ftpConnetion.getDataHolder().getStudentFromUsername(usernameField.getText());
							ftpConnetion.cacheStudentTree(usernameField.getText(), student.getFirstName() + " " + student.getLastName());
							MainWindow window = new MainWindow(usernameField.getText(), ftpConnetion, false);
							loginFrame.setVisible(false);
							window.setVisible(true);
							connectedTimer.stop();
							setVisible(false);
						} else if (ftpConnetion.getDataHolder().correctLoginForTeacher(usernameField.getText(),new String(passwordField.getPassword()))) {
							ftpConnetion.cacheHeirarchy(usernameField.getText() + "/", usernameField.getText());
							MainWindow window = new MainWindow(usernameField.getText(), ftpConnetion, true);
							window.getFrame().setVisible(true);
							loginFrame.setVisible(false);
							setVisible(false);
							connectedTimer.stop();
						} else {
							passwordField.setText("");
							JOptionPane.showMessageDialog(loginFrame, "Incorrect Username or Password");
						}
					} catch (Exception ex) {
						ex.printStackTrace();
						passwordField.setText("");
					}
				}else{
					JOptionPane.showMessageDialog(null, "Could not connect to the server");
				}
				setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			} // End if source = loginButton
		} // End actionPerformed
	} // End LoginButtonListener

	public static void main(String[] args) {
		System.setProperty("java.net.preferIPv4Stack" , "true");
		LoginFrame loginFrame = new LoginFrame("Login Screen");
		loginFrame.setVisible(true);
		loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		loginFrame.setLocationRelativeTo(null);
		loginFrame.setLocation(dim.width/2-loginFrame.getSize().width/2, dim.height/2-loginFrame.getSize().height/2);
		loginFrame.setResizable(false);
	}
}
