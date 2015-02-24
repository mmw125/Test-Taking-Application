package mainWindow;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.Timer;

import utilities.Colors;
import utilities.Constants;
import utilities.JMenu;
import utilities.JMenuBar;
import utilities.JMenuItem;
import ftp.DataHolder.Student;
import ftp.FTPConnection;

public class LoginOld extends JPanel {
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
	private JButton createUserButton;
	private JButton extraButton;
	private FTPConnection ftpConnetion;
	private LoginFrame loginFrame;
	private JLabel connectionStatus;
	private Timer timer;

	public LoginOld() {		
		loginFrame = new LoginFrame("Login Screen");
		loginFrame.setVisible(true);
		loginFrame.add(this);
		
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
		//add(pleaseLogin);
		
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
		
		//getRootPane().setDefaultButton(loginButton);
		loginButton = new JButton("Login");
		loginButton.setFont (loginButton.getFont ().deriveFont (24.0f));
		loginButton.setLayout(null);
		loginButton.setBounds(loginButtonPositionx, passwordLabelPositiony+80, loginButtonWidth, 40);
		ActionListener LoginButtonListener = new LoginButtonListener();
		loginButton.addActionListener(LoginButtonListener);
		add(loginButton);
		
		createUserButton = new JButton("Create New Student");
		createUserButton.setFont (createUserButton.getFont ().deriveFont (24.0f));
		createUserButton.setLayout(null);
		createUserButton.setBounds(loginButtonPositionx, passwordLabelPositiony+130, loginButtonWidth, 40);
//		ActionListener CreateUserButtonListener = new CreateUserButtonListener();
//		createUserButton.addActionListener(CreateUserButtonListener);
		//add(createUserButton);
		
		extraButton = new JButton("Testing Login");
		extraButton.setFont (extraButton.getFont ().deriveFont (24.0f));
		extraButton.setLayout(null);
		extraButton.setBounds(loginButtonPositionx, passwordLabelPositiony+180, loginButtonWidth, 40);
		ActionListener PrintUsersButtonListener = new PrintUsersButtonListener();
		extraButton.addActionListener(PrintUsersButtonListener);
		//add(extraButton);

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
				ftpConnetion.updateConnectionStatus(connectionStatus);
			}			
		};
		ftpConnetion.updateConnectionStatus(connectionStatus);
		timer = new Timer(5000, listener);
		//Set update to run every 5 seconds
		timer.setDelay(5000);
		timer.start();
		
		setVisible(true);

		addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					loginButton.doClick();
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {

			}

			@Override
			public void keyPressed(KeyEvent arg0) {

			}
		});
		revalidate();
	}
	
	public class MenuBar{
		private JMenuBar bar;
		MenuBar(){
			bar = new JMenuBar();
			JMenu options = new JMenu("Options");
			bar.add(options);
			JMenuItem serverAddress = new JMenuItem("Set Server Address");
			
		}
		public JMenuBar getBar(){
			return bar;
		}
	}

	public class LoginButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == loginButton) {
				setCursor (Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				if (ftpConnetion.isConnected()) {
					try {
						//Checks if the entered username and password is correct for any of the teachers
						if (ftpConnetion.getDataHolder().correctLoginForTeacher(usernameField.getText(),new String(passwordField.getPassword()))) {
							ftpConnetion.cacheHeirarchy(usernameField.getText() + "/", usernameField.getText());
							//MainTeacherWindow window = new MainTeacherWindow(usernameField.getText(), ftpConnetion);
							MainWindow window = new MainWindow(usernameField.getText(), ftpConnetion, true);
							window.getFrame().setVisible(true);
							timer.stop();
							loginFrame.dispose();
						} else if (ftpConnetion.getDataHolder().correctLoginForStudent(usernameField.getText(), new String(passwordField.getPassword()))) {
							Student student = ftpConnetion.getDataHolder().getStudentFromUsername(usernameField.getText());
							ftpConnetion.cacheStudentTree(usernameField.getText(), student.getFirstName() + " " + student.getLastName());
							//MainStudentWindow window = new MainStudentWindow(usernameField.getText(), ftpConnetion);
							MainWindow window = new MainWindow(usernameField.getText(), ftpConnetion, false);
							loginFrame.setVisible(false);
							window.setVisible(true);
							timer.stop();
							loginFrame.dispose();
						} else {
							System.out.println("No exception");
							passwordField.setText("");
							
							JOptionPane.showMessageDialog(loginFrame, "Incorrect Username or Password");
						}
					} catch (Exception ex) {
						ex.printStackTrace();
						passwordField.setText("");
						//					JOptionPane.showMessageDialog(loginFrame, "Something went wrong. Please try again later");
					}
				}else{
					JOptionPane.showMessageDialog(null, "Could not connect to the server");
				}
				setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			} // End if source = loginButton
		} // End actionPerformed
	} // End LoginButtonListener

//	public class CreateUserButtonListener implements ActionListener {
//		public void actionPerformed(ActionEvent e) {
//			if (e.getSource() == createUserButton) {
//				if (createCount == 0) {
//					enterUsername_Password1.setText("Please enter a username and password for the new user.");
//					enterUsername_Password2.setText("When finished, click Save User to store new user.");
//					createUserButton.setText("Save User");
//					usernameField.setText("");
//					passwordField.setText("");
//					loginUnsuccessful.setText("");
//					loginSuccessful.setText("");
//					pleaseLogin.setText("");
//					userSaved.setText("");
//
//					createCount++;
//				} else if (createCount == 1) {
//					users.add(usernameField.getText());
//					try {
//						String hashedAndSaltedPassword = PasswordHash.createHash(passwordField.getPassword());
//						System.out.println(hashedAndSaltedPassword);
//						passwords.add(hashedAndSaltedPassword);
//					} catch (NoSuchAlgorithmException e2) {
//						e2.printStackTrace();
//					} catch (InvalidKeySpecException e2) {
//						e2.printStackTrace();
//					}
//					// passwords.add(passwordField.getText());
//
//					createUserButton.setText("Create New User");
//					usernameField.setText("");
//					passwordField.setText("");
//					enterUsername_Password1.setText("");
//					enterUsername_Password2.setText("");
//					loginUnsuccessful.setText("");
//					pleaseLogin.setText("Please log in below");
//					userSaved.setText("User Saved");
//
//					createCount = 0;
//
//					try {
//						if (!usersTeachers_File.exists()) {
//							usersTeachers_File.createNewFile();
//						}
//						ftpConnetion.download(users_Teachers);
//
//						PrintWriter pw = new PrintWriter(usersTeachers_File);
//
//						// for (int i = 0; i < users.size(); i++)
//						for (int i = 0; i < passwords.size(); i++) {
//							pw.write(users.get(i) + " " + passwords.get(i)
//									+ "\n");
//						}
//
//						pw.close();
//
//						ftpConnetion.upload(users_Teachers);
//
//					} catch (IOException e1) {
//						e1.printStackTrace();
//					}
//					System.out.println("Users Printed");
//				}
//			} // End if source = CreateNewUserButton
//		} // End actionPerformed
//	} // End CreateUserButtonListener

	public class PrintUsersButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			usernameField.setText("mark");
			passwordField.setText("wiggans");
			loginButton.doClick();
		}
	}

	public static void main(String[] args) 
	{
		new LoginOld();
	}
	
	public class LoginFrame extends JFrame 
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
		LoginBackground bg;
		
		public LoginFrame(String title){
			super(title);
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
			setLocationRelativeTo(null);
			setLocation(dim.width/2-getSize().width/2, dim.height/2-getSize().height/2);
			setResizable(false);
			int windowWidth = 850;
			int windowHeight = 700;
			
			ImageIcon icon = new ImageIcon(Constants.LOGO_PATH);
			setIconImage(icon.getImage());
			
			setSize(windowWidth,windowHeight);
			
			setLayout(new BorderLayout());
			
			bg = new LoginBackground(this);
			getContentPane().add(bg);
		}
	}
	
	public class LoginBackground extends JPanel 
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		Image background;
		
		public LoginBackground(LoginFrame lf)
		{
			Toolkit kit = Toolkit.getDefaultToolkit();
			background = kit.getImage(Constants.LOGIN_BACKGROUND);
		}
		
		public void paintComponent(Graphics g)
		{
			Graphics2D g2D = (Graphics2D)g;
			g2D.drawImage(background, 0, 0, null);
		}
	}
}
