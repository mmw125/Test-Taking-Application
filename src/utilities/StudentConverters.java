package utilities;

import javax.swing.tree.TreePath;

import ftp.DataHolder;

/**
 * A few very important methods to convert from a TreePath to a path
 * which is where the file will be located on the server
 * @author Mark Wiggans
 */
public class StudentConverters {
	
	/**
	 * Generates the location of the test on the server
	 * @param path the selected path on the tree
	 * @param username student's username
	 * @param holder the holder of the data
	 * @return test location on the server
	 */
	public static String treePathToString(TreePath path, String username, DataHolder holder) {
		String pathString = "";
		Object[] obj = path.getPath();
		for (int i = 1; i < path.getPath().length; i++) {
			if (i == 0) {
				// do nothing
			} else if (i == 1) {
				pathString += holder.getTeacherNameFromClassName(obj[1].toString(), username)+"/";
				pathString += obj[1].toString() + "/";
			} else {
				pathString = pathString + obj[i].toString() + "/";
			}

		}
		pathString = pathString + path.getLastPathComponent() + ".tst";
		return pathString;
	}
	
	/**
	 * Generates the location of the test folder on the server
	 * @param path the currently selected item on the tree
	 * @param username student's username
	 * @param holder where the data is held
	 * @return location of test folder
	 */
	public static String treePathToMap(TreePath path, String username, DataHolder holder) {
		String pathString = "";
		Object[] obj = path.getPath();
		for (int i = 0; i < path.getPath().length; i++) {
			if (i == 0) {
				// don't need to use name of student
			} else if (i == 1) {
				pathString += holder.getTeacherNameFromClassName(obj[1].toString(), username)+"/";
				pathString += obj[1].toString() + "/";
			} else {
				pathString = pathString + obj[i].toString();
			}
		}
		return pathString;
	}
	
	/**
	 * Generates the submission path on the server
	 * @param path test's location on tree
	 * @param username student's username
	 * @param holder holder of the data
	 * @return submission path
	 */
	public static String createSubmissionPath(TreePath path, String username, DataHolder holder) {
		String[] splitOutput = treePathToString(path, username, holder).split("/");
		splitOutput[splitOutput.length - 1] = username + ".sub";
		String output = "";
		for (String string : splitOutput) {
			output += string + "/";
		}
		output = output.substring(0, output.length() - 1);
		return output;
	}
	
	/**
	 * Gets the location of the key on the server
	 * @param path test's location on server
	 * @param username the user's username
	 * @param holder holder of the data
	 * @return the key path
	 */
	public static String keyPathToKey(TreePath path, String username, DataHolder holder){
		String[] splitOutput = treePathToString(path, username, holder).split("/");
		splitOutput[splitOutput.length - 1] = "key.key";
		String output = "";
		for (String string : splitOutput) {
			output += string + "/";
		}
		output = output.substring(0, output.length() - 1);
		return output;
	}
}
