package jears.cslt.util.fileOperation;

import java.io.File;

public class CreateDir {
	public static void createDir(String dirName) {
		File dir = new File(dirName);
		if (!dir.exists()) 
			dir.mkdirs();
	}
	
	public static void createDirForFile(String fileName) {
		int index1 = fileName.lastIndexOf("/");
		int index2 = fileName.lastIndexOf("\\");
		if (index1 < index2)
			index1 = index2;
		if (index1 == -1)
			return;
		String dirName = fileName.substring(0, index1);
		File dir = new File(dirName);
		if (!dir.exists()) 
			dir.mkdirs();
	}
}
