package ftp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.tree.DefaultMutableTreeNode;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import utilities.Colors;

/**
 * The class that handles the connection to the server
 * @author Mark
 */
public class FTPConnection {
	private FTPClient ftpClient;
	private boolean login;
	private DefaultMutableTreeNode heirarchy;
	private DataHolder holder;
	private Map<String, Boolean> imageLookup;

	/**
	 * Attempts to connect to the server
	 */
	public FTPConnection() {
		ftpClient = new FTPClient();
		if(connectToServer()){
			imageLookup = new HashMap<String, Boolean>();
			holder = new DataHolder();
			try {
				refreshDataHolder();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void updateConnectionStatus(JLabel connectionStatus){
		System.out.println("Checking if connected");
		if(isConnected()){
			connectionStatus.setText(" Connected ");
			System.out.println("Connected");
			connectionStatus.setForeground(Colors.GOOD_CONNECTION);
		}else if(connectToServer()){
			connectionStatus.setText(" Connected ");
			connectionStatus.setForeground(Colors.GOOD_CONNECTION);
		}else{
			connectionStatus.setText(" Not Connected ");
			connectionStatus.setForeground(Colors.BAD_CONNECTION);
		}
		connectionStatus.revalidate();
	}
	
	public Map<String, Boolean> getMap(){
		return imageLookup;
	}
	
	public void refreshDataHolder() throws IOException{
		File data = new File("data.txt");
		data.createNewFile();
		download("data.txt");
		holder.importDataFile(data);
		data.delete();
	}
	
	public DataHolder getDataHolder(){
		return holder;
	}
	
	/**
	 * Attempts to connect to the server
	 * @return says if it successfully connected
	 */
	public boolean connectToServer() {
		try {
			ftpClient.connect("");
			login = false;
			try {
				login = ftpClient.login("", "");
				if (login) {
					System.out.println("Connection established...");
				} else {
					System.out.println("Connection fail...");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			return login;
		} catch (SocketException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return false;
	}

	/**
	 * Gets the entire hierarchy under the root and stores it locally 
	 * THIS PROCESS TAKES SEVERAL SECONDS
	 * 
	 * @param rootDirectory the root directory
	 * @param name what to name the node
	 * @throws IOException
	 */
	public void cacheHeirarchy(String rootDirectory, String username) throws IOException {
		String[] name = holder.getName(username);
		DefaultMutableTreeNode node = new DefaultMutableTreeNode(name[0]+" "+name[1]);
		for (FTPFile file : ftpClient.listFiles(rootDirectory)) {
			node.add(getDirectory(rootDirectory + "/" + file.getName() + "/", file.getName()));
		}
		heirarchy = node;
	}

	public boolean isConnected(){
		try{
			download("emptyFile");
			return true;
		}catch(Exception e){
			return false;
		}
	}
	
	
	/**
	 * Gets a Node of the passed directory with all of the folders and files
	 * added to it
	 * 
	 * @param fileDirectory where the node is located
	 * @param name what the node should be named
	 * @return a node with all of the files/folders under it added to it
	 * @throws IOException
	 */
	private DefaultMutableTreeNode getDirectory(String fileDirectory, String name) throws IOException {
		DefaultMutableTreeNode node = new DefaultMutableTreeNode(name);
		for (FTPFile file : ftpClient.listFiles(fileDirectory)) {
			if(file.getName().endsWith(".tst")){
				return new DefaultMutableTreeNode(name);
			}else{
				node.add(getDirectory(fileDirectory + "/" + file.getName() + "/", file.getName()));
			}
			
		}
		return node;
	}
	
	/**
	 * Uploads the file at the fileDirectory to the server at that directory
	 * 
	 * @param fileDirectory
	 *            where the file is and should be saved
	 * @throws IOException
	 */
	public void upload(String fileDirectory) throws IOException {
		FileInputStream upload = new FileInputStream(fileDirectory);
		ftpClient.storeFile(fileDirectory, upload);
	}

	/**
	 * Creates a Folder at the given directory
	 * @param directory
	 *            Where the folder should be located
	 * @throws IOException
	 */
	public void createDirectory(String directory) throws IOException {
		ftpClient.makeDirectory(directory);
	}

	/**
	 * Uploads the data from the input stream to a file at the given directory
	 * 
	 * @param fileDirectory
	 *            Where the data should be stored on the server
	 * @param upload
	 *            The data to be uploaded
	 * @throws IOException
	 */
	public void uploadFile(String fileDirectory, FileInputStream upload)
			throws IOException {
		System.out.println(fileDirectory);
		ftpClient.storeFile(fileDirectory, upload);
	}

	public void download(String fileDirectory) throws IOException {
		FileOutputStream download = new FileOutputStream(fileDirectory);
		ftpClient.retrieveFile(fileDirectory, download);
	}

	public void getFile(String serverFileDirectory, String localFileDirectory) throws IOException {
		ftpClient.retrieveFile(serverFileDirectory, new FileOutputStream(localFileDirectory));
	}
	
	public DefaultMutableTreeNode getCachedTree(){
		return heirarchy;
	}
	
	public void cacheStudentTree(String username, String studentName) throws IOException{
		heirarchy = getStudentTree(username, studentName);
	}
	
	public DefaultMutableTreeNode getStudentTree(String username, String name) throws IOException{
		DefaultMutableTreeNode node = new DefaultMutableTreeNode(name);
		for(DataHolder.Class studentClass : holder.getClassesStudentIsIn(username)){
			node.add(getClass(studentClass.getTeacher(), studentClass.getClassname(), username));
		}
		return node;
	}
	
	public DefaultMutableTreeNode getClass(String teacher, String className, String username) throws IOException{
		//DefaultMutableTreeNode node = new DefaultMutableTreeNode(teacher + " " + className);
		DefaultMutableTreeNode node = new DefaultMutableTreeNode(className);
		for(FTPFile file : ftpClient.listFiles(teacher+"/"+className)){
			if(checkTest(teacher, className, file.getName(), username)){
				node.add(new DefaultMutableTreeNode(file.getName()));
			}
		}
		return node;
	}
	
	/**
	 * Checks if the student can take the test
	 * First, it checks if there is a submission file
	 * Next, it checks if the test can be taken
	 * 		between the dates in the test file
	 * @param teacher	The teacher's username
	 * @param className	The class name
	 * @param testName	The test name
	 * @param username	The user's name
	 * @return			Whether the student can take the specified test or not
	 * @throws IOException 
	 */
	public boolean checkTest(String teacher, String className, String testName, String username) throws IOException{
		boolean submission = false;
		boolean canTakeTest = false;
		//Check if the user has a submission
		for(FTPFile file : ftpClient.listFiles(teacher+"/"+className+"/"+testName)){
			if(file.getName().equals("key.key")){
				imageLookup.put(teacher+"/"+className+"/"+testName, true);
				canTakeTest = true;
			}
			
			if(file.getName().equals(username+".sub")){
				imageLookup.put(teacher+"/"+className+"/"+testName, true);
				submission = true;
			}
		}
		imageLookup.put(teacher+"/"+className+"/"+testName, submission);
		return canTakeTest;
	}
	
	/**
     * Removes a non-empty directory by delete all its sub files and
     * sub directories recursively. And finally remove the directory.
     */
    public void removeDirectory(FTPClient ftpClient, String parentDir,
            String currentDir) throws IOException {
        String dirToList = parentDir;
        if (!currentDir.equals("")) {
            dirToList += "/" + currentDir;
        }
 
        FTPFile[] subFiles = ftpClient.listFiles(dirToList);
 
        if (subFiles != null && subFiles.length > 0) {
            for (FTPFile aFile : subFiles) {
                String currentFileName = aFile.getName();
                if (currentFileName.equals(".") || currentFileName.equals("..")) {
                    // skip parent directory and the directory itself
                    continue;
                }
                String filePath = parentDir + "/" + currentDir + "/"
                        + currentFileName;
                if (currentDir.equals("")) {
                    filePath = parentDir + "/" + currentFileName;
                }
 
                if (aFile.isDirectory()) {
                    // remove the sub directory
                    removeDirectory(ftpClient, dirToList, currentFileName);
                } else {
                    // delete the file
                    boolean deleted = ftpClient.deleteFile(filePath);
                    if (deleted) {
                        System.out.println("DELETED the file: " + filePath);
                    } else {
                        System.out.println("CANNOT delete the file: "
                                + filePath);
                    }
                }
            }
 
            // finally, remove the directory itself
            boolean removed = ftpClient.removeDirectory(dirToList);
            if (removed) {
                System.out.println("REMOVED the directory: " + dirToList);
            } else {
                System.out.println("CANNOT remove the directory: " + dirToList);
            }
        }
    }
    public FTPClient getClient(){
    	return ftpClient;
    }
}
