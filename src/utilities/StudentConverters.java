package utilities;

import javax.swing.tree.TreePath;

import ftp.DataHolder;
import ftp.FTPConnection;

public class StudentConverters {
	public static String treePathToString(TreePath path, String username, DataHolder holder) {
		String pathString = "";
		Object[] obj = path.getPath();
		for (int i = 1; i < path.getPath().length; i++) {
			if (i == 0) {
				// do nothing
			} else if (i == 1) {
				pathString += holder.getTeacherNameFromClassName(obj[1].toString(), username)+"/";
				pathString += obj[1].toString() + "/";
//				String[] split = obj[1].toString().split(" ");
//				pathString += split[0] + "/";
//				pathString += obj[1].toString()
//						.substring(split[0].length() + 1);
//				pathString += "/";
			} else {
				pathString = pathString + obj[i].toString() + "/";
			}

		}
		pathString = pathString + path.getLastPathComponent() + ".tst";
		return pathString;
	}
	
	public static String treePathToMap(TreePath path, String username, DataHolder holder) {
		String pathString = "";
		Object[] obj = path.getPath();
		for (int i = 0; i < path.getPath().length; i++) {
			if (i == 0) {
				// do nothing
			} else if (i == 1) {
				pathString += holder.getTeacherNameFromClassName(obj[1].toString(), username)+"/";
				pathString += obj[1].toString() + "/";
//				String[] split = obj[1].toString().split(" ");
//				pathString += split[0] + "/";
//				pathString += obj[1].toString().substring(split[0].length() + 1);
//				pathString += "/";
			} else {
				pathString = pathString + obj[i].toString() + "/";
			}

		}
		pathString = pathString.substring(0, pathString.length() - 1);
		return pathString;
	}
	
	public static String createSubmissionPath(TreePath path, String username, DataHolder holder) {
		String[] splitOutput = treePathToString(path, username, holder).split("/");
		splitOutput[splitOutput.length - 1] = username + ".sub";
		String output = "";
		for (String string : splitOutput) {
			output += string + "/";
		}
		output = output.substring(0, output.length() - 1);
		System.out.println(output);
		return output;
	}
	
	public static String keyPathToKey(TreePath path, String username, DataHolder holder){
		String[] splitOutput = treePathToString(path, username, holder).split("/");
		splitOutput[splitOutput.length - 1] = "key.key";
		String output = "";
		for (String string : splitOutput) {
			output += string + "/";
		}
		output = output.substring(0, output.length() - 1);
		System.out.println(output);
		return output;
	}
}
