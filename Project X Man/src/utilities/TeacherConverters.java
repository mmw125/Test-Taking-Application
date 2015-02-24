package utilities;

import javax.swing.tree.TreePath;

public class TeacherConverters {
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
