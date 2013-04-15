package jears.cslt.SRC;

import java.io.IOException;
import java.util.Set;
import java.util.TreeSet;

import jears.cslt.util.fileOperation.ReadFile;

public class SogouResultProcesser {
	private String dir;
	private SnippetProcess processer;
	
	public SogouResultProcesser(String dir) {
		this.dir = dir;
		processer = new SnippetProcess();
	}
	
	public Set<String> process(String query) throws IOException {
		Set<String> qWords = new TreeSet<String>();
		
		ReadFile rf = new ReadFile(dir + query + ".txt");
		String line = "";
		while((line = rf.readLine()) != null) {
			processer.getWords(line, qWords);
		}
		
		rf.close();
		
		return qWords;
	}
	
	public static void main(String[] args) throws IOException {
		SogouResultProcesser test = new SogouResultProcesser("./data/SogouSentence");
		System.out.println(test.process("ָה³ז"));
	}
}
