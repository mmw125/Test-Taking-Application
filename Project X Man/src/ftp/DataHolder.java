package ftp;


import java.io.File;
import java.io.FileNotFoundException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Scanner;

import utilities.PasswordHash;

public class DataHolder {
	private File file;
	private ArrayList<Class> classes;
	private ArrayList<Teacher> teachers;
	private ArrayList<Student> students;
	
	public Class getClass(String teacherUsername, String className){
		for(Class currentClass : classes){
			if(currentClass.getTeacher().equals(teacherUsername)&&currentClass.getClassname().equals(className)){
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
			System.out.println(inString);
			if (inString.startsWith("TEACHER")) {
				System.out.println("Teacher Created");
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

	public boolean correctLoginForTeacher(String username, String password) throws NoSuchAlgorithmException, InvalidKeySpecException{
		for(Teacher teacher : teachers){
			System.out.println(teacher.getUsername());
			if(teacher.getUsername().equals(username)){
				System.out.println("seeing a person named "+username);
				if(PasswordHash.validatePassword(password, teacher.getPassword())){
					return true;
				}
				return false;
			}
		}
		return false;
	}
	
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
	
	public Student getStudentFromUsername(String username){
		for(Student student : students){
			if(student.getUsername().equals(username)){
				return student;
			}
		}
		return null;
	}
	
	public String getTeacherNameFromClassName(String className, String username){
		for(Class studentClass : getClassesStudentIsIn(username)){
			if(studentClass.getClassname().equals(className)){
				return studentClass.getTeacher();
			}
		}
		return null;
	}
	
	public ArrayList<Class> getClassesStudentIsIn(String username){
		System.out.println("Number of Classes: "+classes.size());
		ArrayList<Class> classesUserIsIn = new ArrayList<Class>();
		for(Class currentClass : classes){
			if(currentClass.isStudentInClass(username)){
				classesUserIsIn.add(currentClass);
			}
		}
		return classesUserIsIn;
	}
	
	public class Class {
		private String teacher;
		private String classname;
		private ArrayList<String> studentsInClass; 
		Class(String[] data) {
			studentsInClass = new ArrayList<String>();
			teacher = data[1];
			classname = data[2];
			for(int i = 3; i < data.length; i++){
					studentsInClass.add(data[i]);
			}
		}
		
		public boolean isStudentInClass(String studentUsername){
			for(String username : studentsInClass){
				if(username.equals(studentUsername)){
					return true;
				}
			}
			return false;
		}
		public String getTeacher(){ return teacher; }
		public String getClassname(){ return classname; }
		
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

	public class Teacher {
		private String username;
		private String password;
		private String firstName;
		private String lastName;

		Teacher(String[] data) {
			username = data[1];
			firstName = data[2];
			lastName = data[3];
			password = data[4];
		}

		public String getUsername() {
			return username;
		}

		public String getPassword() {
			return password;
		}
		
		public String getFirstName(){
			return firstName;
		}
		
		public String getLastName(){
			return lastName;
		}
	}
	
	public class Student extends Teacher{
		Student(String[] data) {
			super(data);
		}
	}
	
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
