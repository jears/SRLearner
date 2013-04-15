package jears.cslt.util.fileOperation;

import java.io.File;

/**
 * 数据操作底层
 * <p>
 * 封装了文件删除的基本操作�?
 * 向上提供了delete方法�?
 */
public class DeleteFile {
	String fileName;
	File file;

	/**
	 * 创建文件删除
	 * @param fileName 要删除的文件�?
	 */
	public DeleteFile(String fileName) {
		this.fileName = fileName;
		file = new File(fileName);
	}
	
	public DeleteFile(File file) {
		this.fileName = file.getPath();
		this.file = file;
	}

	/**
	 * 删除文件
	 */
	synchronized public void delete() {
		file.delete();
	}
}