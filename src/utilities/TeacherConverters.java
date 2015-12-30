package utilities;

import javax.swing.tree.TreePath;

/**
 * Converters useful for teachers
 * @author Mark Wiggans
 */
public class TeacherConverters {
	
	/**
	 * Generates the base string for the other converters
	 * @param path test's location in the tree
	 * @param username teacher's username
	 * @return the base string
	 */
	private static String genBaseString(TreePath path, String username) {
		String pathString = username+"/";
		Object[] obj = path.getPath();
		for (int i = 1; i < obj.length; i++) {
			pathString = pathString + obj[i].toString() + "/";
		}
		return pathString;
	}
	
	/**
	 * Generates the location of the temporary key
	 * @param path selected item in tree
	 * @param username teacher's username
	 * @return location of temp key
	 */
	public static String treePathToTempKeyPath(TreePath path, String username) {
		return genBaseString(path, username) + "tempkey.key";
	}
	
	/**
	 * Generates the test path
	 * @param path test's location in the tree
	 * @param username teacher's username
	 * @return path to the test
	 */
	public static String treePathToTestPath(TreePath path, String username) {
		return genBaseString(path, username) + path.getLastPathComponent()+".tst";
	}
	
	/**
	 * Generates the temp test location
	 * @param path test's location in tree
	 * @param username teacher's username
	 * @return temp test path
	 */
	public static String treePathToTempTestPath(TreePath path, String username) {
		return genBaseString(path, username) + "temp" + path.getLastPathComponent()+".tst";
	}
	
	/**
	 * Generates string for location of test folder
	 * @param path location of test in tree
	 * @param username teacher's username
	 * @return test's folder
	 */
	public static String treePathToString(TreePath path, String username){
		String pathString = genBaseString(path, username);
		pathString = pathString.substring(0, pathString.length() - 1);
		return pathString;
	}

	/**
	 * Generates the path to the key
	 * @param path test's location in tree
	 * @param username teacher's username
	 * @return key path
	 */
	public static String treePathToKeyPath(TreePath path, String username) {
		return genBaseString(path, username) + "key.key";
	}
}
