package jears.cslt.util.fileOperation;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 * 数据操作底层
 * <p>
 * 封装了文件写的基本操作，
 * 向上提供了writeLine和write方法�?
 * <p />
 * <strong>
 * 注意：文件写操作结束后要调用close方法关闭流�?
 * </strong>
 */
public class WriteFile
        implements Constants {
    
    private FileOutputStream fos;
    private OutputStreamWriter osw;
    private BufferedWriter bw;

    /**
     * 通过文件名创建文件write流，并使用UTF-8作为编码类型�?
     * @param fileName 文件�?
     * @param append 为true时表示追加，为false时表示覆�?
     * @throws IOException
     */
    public WriteFile(String fileName, boolean append)
            throws IOException {
        fos = new FileOutputStream(fileName, append);
        osw = new OutputStreamWriter(fos, UTF8);
        bw = new BufferedWriter(osw);
    }

    /**
     * 通过文件名创建文件write流，并使用给定的编码类型�?
     * @param fileName 文件�?
     * @param append 为true时表示追加，为false时表示覆盖�?
     * @param charset 编码类型
     * @throws IOException 
     */
    public WriteFile(String fileName, boolean append, String charset)
            throws IOException {
        fos = new FileOutputStream(fileName, append);
        osw = new OutputStreamWriter(fos, charset);
        bw = new BufferedWriter(osw);
    }

    /**
     * 通过文件创建文件write流，并使用给定的编码类型�?
     * @param file 文件
     * @param append 为true时表示追加，为false时表示覆盖�?
     * @param charset 编码类型
     * @throws IOException 
     */
    public WriteFile(File file, boolean append, String charset)
            throws IOException {
        fos = new FileOutputStream(file, append);
        osw = new OutputStreamWriter(fos, charset);
        bw = new BufferedWriter(osw);
    }

    /**
     * 向文件写入内容�?
     * @param str 要写入的内容
     * @throws IOException 
     */
    synchronized public void write(String str)
            throws IOException {
        bw.write(str);
    }

    /**
     * 向文件写入内容并换行�?
     * @param str 要写入的内容
     * @throws IOException 
     */
    synchronized public void writeLine(String str)
            throws IOException {
        bw.write(str + NEW_LINE);
    }
    
    /**
     * 向文件写入内容并换行�?
     * @param str 要写入的内容
     * @throws IOException 
     */
    synchronized public void writeLine(String str, String newline)
            throws IOException {
        bw.write(str + newline);
    }
    
    synchronized public void flush()
            throws IOException {
        bw.flush();
    }
    
    /**
     * 关闭�?
     * @throws IOException 
     */
    synchronized public void close()
            throws IOException {
        bw.close();
        osw.close();
        fos.close();
    }
}
