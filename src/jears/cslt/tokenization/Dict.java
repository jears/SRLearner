package jears.cslt.tokenization;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import jears.cslt.util.fileOperation.ReadFile;

public class Dict {
	Set<String> dict;
	int maxLength;
	
	public Dict(String dictfile, String encode) {
		dict = new HashSet<String>();
		maxLength = 0;
		try {
			System.out.println("Loading dict...");
			ReadFile rf = new ReadFile(dictfile, encode, 1024*1024*64);
			
			String line = "";
			int lineNo = 0;
			while((line = rf.readLine()) != null) {
				lineNo++;
				if (lineNo %10000 == 0)
					System.out.println(lineNo + ":" + line);
				String[] words = line.split(" ");
				if (words.length > maxLength)
					maxLength = words.length;
				line = line.toLowerCase();
				line = line.replaceAll("-", " ");
				dict.add(line);
			}
			
			rf.close();
			System.out.println("Dict loaded");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public int getMaxLength() {
		return maxLength;
	}
 	
	public boolean hasConcetp(String concept) {
		return dict.contains(concept);
	}
	
	public static void main(String[] args) {
		Dict dict = new Dict("dict/getTitles.txt", "gbk");
		System.out.println(dict.hasConcetp("yassin"));
	}
}
