package ftp;

import java.io.File;
import java.io.FileNotFoundException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Scanner;

import utilities.PasswordHash;

/**
 * This takes the data file on the server, parses it, and allows
 * you to access the data in it easily
 * @author Mark Wiggans
 */
public class DataHolder {
	private File file;
	private ArrayList<Class> classes;
	private ArrayList<Teacher> teachers;
	private ArrayList<Student> students;
	
	/**
	 * Gets the class with the given teacher and class name
	 * @param teacherUsername the class teacher's username
	 * @param className name of the class
	 * @return the class specified by the parameters or null if it does not exist
	 */
	public Class getClass(String teacherUsername, String className){
		for(Class currentClass : classes){
			if(currentClass.getTeacher().equals(teacherUsername) && currentClass.getClassname().equals(className)){
				return currentClass;
			}
		}
		return null;
	}
	
	/**
	 * Imports all of the data from the given file
	 * @param fileIn	the file with data to import
	 */
	public void importDataFile(File fileIn) {
		teachers = new ArrayList<Teacher>();
		classes = new ArrayList<Class>();
		students = new ArrayList<Student>();
		file = fileIn;
		Scanner scanner = null;
		try {
			scanner = new Scanner(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		while(scanner.hasNext()){
			String inString = scanner.nextLine();
			if (inString.startsWith("TEACHER")) {
				teachers.add(new Teacher(inString.split("~")));
			}
			if (inString.startsWith("STUDENT")) {
				students.add(new Student(inString.split("~")));
			}
			if (inString.startsWith("CLASS")) {
				classes.add(new Class(inString.split("~")));
			}
		}
	}

	/**
	 * Checks if the login is correct for a teacher
	 * @param username the entered username
	 * @param password the entered password
	 * @return true if the password is correct. false if not correct
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 */
	public boolean correctLoginForTeacher(String username, String password) throws NoSuchAlgorithmException, InvalidKeySpecException{
		for(Teacher teacher : teachers){
			if(teacher.getUsername().equals(username)){
				if(PasswordHash.validatePassword(password, teacher.getPassword())){
					return true;
				}
				return false;
			}
		}
		return false;
	}
	
	/**
	 * Checks if the username and password are correct
	 * @param username the entered username
	 * @param password the entered password
	 * @return true if it is a correct login. false if not
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 */
	public boolean correctLoginForStudent(String username, String password) throws NoSuchAlgorithmException, InvalidKeySpecException{
		for(Student student : students){
			if(student.getUsername().equals(username)){
				if(PasswordHash.validatePassword(password, student.getPassword())){
					return true;
				}
				return false;
			}
		}
		return false;
	}
	
	/**
	 * Gets the student from the given username
	 * @param username the username to check
	 * @return the student with that username
	 */
	public Student getStudentFromUsername(String username){
		for(Student student : students){
			if(student.getUsername().equals(username)){
				return student;
			}
		}
		return null;
	}
	
	/**
	 * Gets the teacher with the given username
	 * @param username the username to check
	 * @return a teacher with that username
	 */
	public Teacher getTeacherFromUsername(String username) {
		for(Teacher teacher : teachers){
			if(teacher.getUsername().equals(username)){
				return teacher;
			}
		}
		return null;
	}
	
	/**
	 * Gets the teacher of the class
	 * @param className name of the class
	 * @param username student's username
	 * @return the teacher's username
	 */
	public String getTeacherNameFromClassName(String className, String username){
		for(Class cl : classes) {
			if(cl.getClassname().equals(className)) {
				if(cl.isStudentInClass(username)) {
					return cl.getTeacher();
				}
			}
		}
		return null;
	}
	
	/**
	 * Gets what classes the given student's username is in
	 * @param username the student's username to check
	 * @return a list of the classes the given student is in
	 */
	public ArrayList<Class> getClassesStudentIsIn(String username){
		ArrayList<Class> classesUserIsIn = new ArrayList<Class>();
		for(Class currentClass : classes){
			if(currentClass.isStudentInClass(username)){
				classesUserIsIn.add(currentClass);
			}
		}
		return classesUserIsIn;
	}
	
	/**
	 * Represents a class at the school
	 * @author Mark Wiggans
	 */
	public class Class {
		private String teacher;
		private String classname;
		private ArrayList<String> studentsInClass;
		
		/**
		 * Creates a new instance of a class
		 * @param data stuff to create the data with
		 */
		Class(String[] data) {
			studentsInClass = new ArrayList<String>();
			teacher = data[1];
			classname = data[2];
			for(int i = 3; i < data.length; i++){
					studentsInClass.add(data[i]);
			}
		}
		
		/**
		 * Checks if the student is in this class
		 * @param studentUsername the username to check
		 * @return true if in the class false if not
		 */
		public boolean isStudentInClass(String studentUsername){
			for(String username : studentsInClass){
				if(username.equals(studentUsername)){
					return true;
				}
			}
			return false;
		}
		
		/**
		 * Gets the teacher of the class
		 * @return the teacher's username
		 */
		public String getTeacher() {
			return teacher; 
		}
		
		/**
		 * Gets the name of the class
		 * @return the name of the class
		 */
		public String getClassname() {
			return classname; 
		}
		
		/**
		 * Gets an ArrayList with all of the students in the given class
		 * @param classIn	the class to look for
		 * @return	an ArrayList with all of the students in the given class
		 */
		public ArrayList<Student> getStudentsInClass(){
			ArrayList<Student> studentsInClass = new ArrayList<Student>();
			for(Student student : students){
				if(this.isStudentInClass(student.getUsername())){
					studentsInClass.add(student);
				}
			}
			return studentsInClass;
		}
	}

	/**
	 * Represents a Person in the data file
	 * @author Mark Wiggans
	 */
	public class Person {
		private String username;
		private String password;
		private String firstName;
		private String lastName;

		/**
		 * Creates a new instance of the person
		 * @param data the data to create
		 */
		Person(String[] data) {
			username = data[1];
			firstName = data[2];
			lastName = data[3];
			password = data[4];
		}

		/**
		 * Gets the username of the person
		 * @return the username
		 */
		public String getUsername() {
			return username;
		}

		/**
		 * Gets the password of the person
		 * @return the password
		 */
		public String getPassword() {
			return password;
		}
		
		/**
		 * Gets the first name of the person
		 * @return the first name
		 */
		public String getFirstName(){
			return firstName;
		}
		
		/**
		 * Gets the last name of the person
		 * @return the last name
		 */
		public String getLastName(){
			return lastName;
		}
	}
	
	/**
	 * Represents a Student in the data file
	 * @author Mark Wiggans
	 */
	public class Student extends Person{
		Student(String[] data) {
			super(data);
		}
	}
	
	/**
	 * Represents a Teacher in the data file
	 * @author Mark Wiggans
	 */
	public class Teacher extends Person {
		public Teacher(String[] data) {
			super(data);
		}
	}
	
	/**
	 * Gets the first name and last name of the person 
	 * @param username the username to look for
	 * @return the name of the person with that username
	 */
	public String[] getName(String username){
		String[] output = new String[2];
		for(Teacher teacher : teachers){
			if(teacher.getUsername().equals(username)){
				output[0] = teacher.getFirstName();
				output[1] = teacher.getLastName();
				return output;
			}
		}
		
		for(Student student : students){
			if(student.getUsername().equals(username)){
				output[0] = student.getFirstName();
				output[1] = student.getLastName();
				return output;
			}
		}
		return null;
	}
}
