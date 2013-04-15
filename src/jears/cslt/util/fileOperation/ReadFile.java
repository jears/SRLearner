package jears.cslt.util.fileOperation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;


/**
 * 数据操作底层
 * <p>
 * 封装了文件读的基本操作，
 * 向上提供了readLine方法�?
 * <p />
 * <strong>
 * 注意：文件读操作结束后要调用close方法关闭流�?
 * </strong>
 */
public class ReadFile
        implements Constants {

    private FileInputStream fis;
    private InputStreamReader isr;
    private BufferedReader br;

    /**
     * 通过文件名构造一个文件read流，并使用UTF-8作为编码类型�?
     * @param fileName 文件�?
     * @throws IOException
     */
    public ReadFile(String fileName)
            throws IOException {
        fis = new FileInputStream(fileName);
        isr = new InputStreamReader(fis, UTF8);
        br = new BufferedReader(isr);
    }

    /**
     * 通过文件名构造一个文件read流，并使用给定的编码类型�?
     * @param fileName 文件�?
     * @param charset 编码类型
     * @throws IOException 
     */
    public ReadFile(String fileName, String charset)
            throws IOException {
        fis = new FileInputStream(fileName);
        isr = new InputStreamReader(fis, charset);
        br = new BufferedReader(isr);
    }
    
    /**
     * 通过文件名构造一个文件read流，并使用给定的编码类型�?
     * @param fileName 文件�?
     * @param charset 编码类型
     * @throws IOException 
     */
    public ReadFile(String fileName, String charset, int size)
            throws IOException {
        fis = new FileInputStream(fileName);
        isr = new InputStreamReader(fis, charset);
        br = new BufferedReader(isr, size);
    }

    /**
     * 通过文件构�?�?��文件read流，并使用给定的编码类型�?
     * @param file 文件
     * @param charset 编码类型
     * @throws IOException 
     */
    public ReadFile(File file, String charset)
            throws IOException {
        fis = new FileInputStream(file);
        isr = new InputStreamReader(fis, charset);
        br = new BufferedReader(isr);
    }

    /**
     * 从文件中读取�?��
     * @return 包含要读取行内容的String
     * @throws IOException
     */
    synchronized public String readLine()
            throws IOException {
        return br.readLine();
    }
    
    /**
     * 关闭�?
     * @throws IOException
     */
    synchronized public void close()
            throws IOException {
        br.close();
        isr.close();
        fis.close();
    }
}
