package jears.cslt.preprocess;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import jears.cslt.util.fileOperation.CreateDir;
import jears.cslt.util.fileOperation.ReadFile;
import jears.cslt.util.fileOperation.WriteFile;

public class StopRemover {
	private static Set<String> stopWords;
	static {
		stopWords = new HashSet<String>();
		ReadFile rf;
		try {
			rf = new ReadFile("./data/list/stopword_CH.txt", "GB2312");
			
			String line = "";
			while((line = rf.readLine()) != null) {
				stopWords.add(line);
			}
			
			rf.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static String removeStopWord(String sentence) {
		String[] words = sentence.split(" ");
		String ret = "";
		for (int i = 0; i < words.length; i++) {
			if (stopWords.contains(words[i]))
				continue;
			else
				ret += words[i] + " ";
		}
		
		return ret.trim();
	}
	
	public static void removeStopWordFile(String inputFilename, String outputFilename) {
		CreateDir.createDirForFile(outputFilename);
		try {
			ReadFile rf = new ReadFile(inputFilename, "GB2312");
			WriteFile wf = new WriteFile(outputFilename, false, "GB2312");
			
			String line = "";
			while((line = rf.readLine()) != null) {
				wf.writeLine(removeStopWord(line));
			}
			
			wf.close();
			rf.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		removeStopWordFile("data/list/dictChinese.txt", "data/list/dictCh.txt");
	}
}
