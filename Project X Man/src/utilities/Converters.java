package utilities;

import javax.swing.tree.TreePath;

public class Converters {
	public static String treePathToString(TreePath path) {
		String pathString = "";
		Object[] obj = path.getPath();
		for (int i = 1; i < path.getPath().length; i++) {
			if (i == 0) {
				// do nothing
			} else if (i == 1) {
				String[] split = obj[1].toString().split(" ");
				pathString += split[0] + "/";
				pathString += obj[1].toString()
						.substring(split[0].length() + 1);
				pathString += "/";
			} else {
				pathString = pathString + obj[i].toString() + "/";
			}

		}
		pathString = pathString + path.getLastPathComponent() + ".tst";
		return pathString;
	}
	
	public static String treePathToMap(TreePath path) {
		String pathString = "";
		Object[] obj = path.getPath();
		for (int i = 0; i < path.getPath().length; i++) {
			if (i == 0) {
				// do nothing
			} else if (i == 1) {
				String[] split = obj[1].toString().split(" ");
				pathString += split[0] + "/";
				pathString += obj[1].toString()
						.substring(split[0].length() + 1);
				pathString += "/";
			} else {
				pathString = pathString + obj[i].toString() + "/";
			}

		}
		pathString = pathString.substring(0, pathString.length() - 1);
		return pathString;
	}
	
	public static String createSubmissionPath(TreePath path, String username) {
		String[] splitOutput = treePathToString(path).split("/");
		splitOutput[splitOutput.length - 1] = username + ".sub";
		String output = "";
		for (String string : splitOutput) {
			output += string + "/";
		}
		output = output.substring(0, output.length() - 1);
		System.out.println(output);
		return output;
	}
	
	public static String keyPathToKey(TreePath path){
		String[] splitOutput = treePathToString(path).split("/");
		splitOutput[splitOutput.length - 1] = "key.key";
		String output = "";
		for (String string : splitOutput) {
			output += string + "/";
		}
		output = output.substring(0, output.length() - 1);
		System.out.println(output);
		return output;
	}
	
	public static String treePathToTempKeyPath(TreePath path, String username) {
		String pathString = username+"/";
		Object[] obj = path.getPath();
		for (int i = 1; i < obj.length; i++) {
			pathString = pathString + obj[i].toString() + "/";
		}
		pathString += "tempkey.key";
		return pathString;
	}
	
	public static String treePathToTestPath(TreePath path, String username) {
		String pathString = username+"/";
		Object[] obj = path.getPath();
		for (int i = 1; i < obj.length; i++) {
			pathString = pathString + obj[i].toString() + "/";
		}
		pathString += path.getLastPathComponent()+".tst";
		return pathString;
	}
	
	public static String treePathToTempTestPath(TreePath path, String username) {
		String pathString = username+"/";
		Object[] obj = path.getPath();
		for (int i = 1; i < obj.length; i++) {
			pathString = pathString + obj[i].toString() + "/";
		}
		pathString += "temp"+path.getLastPathComponent()+".tst";
		return pathString;
	}
	
	public static String treePathToString(TreePath path, String username){
		String pathString = username+"/";
		Object[] obj = path.getPath();
		for (int i = 1; i < obj.length; i++) {
			pathString = pathString + obj[i].toString() + "/";
		}
		if(pathString.endsWith("/")){
			pathString = pathString.substring(0, pathString.length()-1);
		}
		return pathString;
	}
	
	public static String treePathToParent(TreePath path, String username){
		String pathString = username+"/";
		Object[] obj = path.getPath();
		for (int i = 1; i < obj.length-1; i++) {
			pathString = pathString + obj[i].toString() + "/";
		}
		if(pathString.endsWith("/")){
			pathString = pathString.substring(0, pathString.length()-1);
		}
		return pathString;
	}

	public static String treePathToKeyPath(TreePath path, String username) {
		String pathString = username+"/";
		Object[] obj = path.getPath();
		for (int i = 1; i < obj.length; i++) {
			pathString = pathString + obj[i].toString() + "/";
		}
		pathString += "key.key";
		System.out.println(pathString);
		return pathString;
	}
}
