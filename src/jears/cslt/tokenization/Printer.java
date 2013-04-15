package jears.cslt.tokenization;

import java.io.IOException;
import java.util.ArrayList;

import jears.cslt.util.fileOperation.CreateDir;
import jears.cslt.util.fileOperation.WriteFile;

public class Printer {
	public static void print(ArrayList<String> ret, String filename, String dir) {
		try {
			CreateDir.createDir(dir);
			WriteFile wf = new WriteFile(dir + "/" + filename, false);
			
			for (int i = 0; i < ret.size(); i++)
				wf.writeLine(ret.get(i), "\n");
			
			wf.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void print(ArrayList<String> ret, String filename, String dir, String encode) {
		try {
			CreateDir.createDir(dir);
			WriteFile wf = new WriteFile(dir + "/" + filename, false, encode);
			
			for (int i = 0; i < ret.size(); i++)
				wf.writeLine(ret.get(i), "\n");
			
			wf.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void print(String query, ArrayList<String> ret, String filename, String dir) {
		try {
			CreateDir.createDir(dir);
			WriteFile wf = new WriteFile(dir + "/" + filename, true);
			
			wf.writeLine(query, "\n");
			
			for (int i = 0; i < ret.size(); i++) {
				wf.writeLine("\t" + ret.get(i), "\n");
			}
			
			wf.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void print(String query, ArrayList<String> ret, String filename, String dir, String encode) {
		try {
			CreateDir.createDir(dir);
			WriteFile wf = new WriteFile(dir + "/" + filename, true, encode);
			
			wf.writeLine(query, "\n");
			
			for (int i = 0; i < ret.size(); i++) {
				wf.writeLine("\t" + ret.get(i), "\n");
			}
			
			wf.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
}
