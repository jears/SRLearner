package jears.cslt.util.fileOperation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class MoveFile {
	private File file;
	
	public MoveFile(File file) {
		this.file = file;
	}
	
	public boolean move(String path) 
	throws FileNotFoundException {
		return copyFile(path);
	}
	
	synchronized private boolean copyFile(String newPath) 
	throws FileNotFoundException {
		try {
			int bytesum = 0;
			int byteread = 0;
			InputStream inStream = new FileInputStream(file); // 读入原文�?
			FileOutputStream fs = new FileOutputStream(newPath);
			byte[] buffer = new byte[1024];
			while ((byteread = inStream.read(buffer)) != -1) {
				bytesum += byteread; // 字节�?文件大小
//				System.out.println(bytesum);
				fs.write(buffer, 0, byteread);
			}
			inStream.close();
		} catch (IOException e) {
			System.out.println("复制单个文件操作出错");
			return false;
		}
		return true;
	}
}
