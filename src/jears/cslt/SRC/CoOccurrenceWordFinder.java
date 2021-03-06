package jears.cslt.SRC;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import ICTCLAS.I3S.AC.IctWordSeg;

import jears.cslt.util.fileOperation.CreateDir;
import jears.cslt.util.fileOperation.ReadFile;
import jears.cslt.util.fileOperation.WriteFile;

/**
 * preprocess the search results, including titles and snippets
 * the following steps are executed
 * 1.segment the results sentences or phrases (add the query to user dict)
 * 2.concept recognition using wikipedia dict (undo)
 * 3.remove stop words and query words
 * 4.get the bag of words for each search results
 * @author jj
 *
 */
public class CoOccurrenceWordFinder {
	private String dir;
	private IctWordSeg wordSeg;
	private Set<String> stopWords;
	private String query;
	
	public CoOccurrenceWordFinder() {
		this.wordSeg = new IctWordSeg("GB2312");
		wordSeg.importUserDict("./ICTCLAS/userdict.txt"); 
		
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
		
//		tokenizer = new ChineseTokenizer("./data/list/dictChinese-final.txt", "gbk");
	}
	
	public CoOccurrenceWordFinder(String dir) {
		this();
		this.dir = dir;
	}
	
	public Set<String> segmentSentences(String dir, String query, int windowSize) throws IOException {
		Set<String> sWords = new TreeSet<String>();
		this.query = query;
		ReadFile rf = new ReadFile(dir + query + ".txt", "gbk");
		
		String line = "";
		while((line = rf.readLine()) != null) {
			if (windowSize == 0)
				getWords(line, sWords);
			else {
				getWords(line, sWords, windowSize);
			}
		}
		
		rf.close();
		
		sWords.remove(query);
		
		return sWords;
	}
	
	public Set<String> segment(String query) throws IOException {
		Set<String> qWords = new TreeSet<String>();
		this.query = query;
		
		String newDir = dir.replace("SnippetForCarrot", "Segment");
		CreateDir.createDir(newDir + query);
		
		File qDir = new File(dir + query);
		String[] files = qDir.list();
		for (String f : files) {
			ReadFile rf = new ReadFile(dir + query + "/" + f, "gbk");
			WriteFile wf = new WriteFile(newDir + query + "/" + f, false, "gbk");
			
			String title = rf.readLine();
			getWords(title, qWords);
			title = wordSeg.segSentence(title);
			wf.writeLine(title);
			rf.readLine();
			String snippet = rf.readLine();

			getWords(snippet, qWords);
			snippet = wordSeg.segSentence(snippet);
			wf.writeLine(snippet);
			
			wf.close();
			rf.close();
		}
		
		qWords.remove(query);
		
		return qWords;
	}
	
	public void getWords(String sentence, Set<String> qWords) {
		String segSentence = wordSeg.segSentence(sentence);
		String[] words = segSentence.split(" +");
		for (int i = 0; i < words.length; i++) {
			String w = words[i];
			String[] t = w.split("/");
			if (t.length == 2 && (t[1].contains("n")) && !stopWords.contains(t[0])) {
				if (t[0].startsWith("-")) {
					t[0] = t[0].substring(1);
				}
				
				if (t[0].endsWith(":")) {
					t[0] = t[0].substring(0, t[0].length()-2 );
				}
				
				qWords.add(t[0]);
			}
		}
	}

	private int max(int a, int b) {
		if (a > b)
			return a;
		else
			return b;
	}
	
	private int min(int a, int b) {
		if (a < b)
			return a;
		else
			return b;
	}
	
	public void getWords(String sentence, Set<String> qWords, int windowSize) {
		String seg = wordSeg.segSentence(sentence.trim(), 0);
		String[] ws = seg.split(" +");
		Set<String> filter = new TreeSet<String>();
		int num = ws.length;
		if (num <= windowSize) {
			getWords(sentence, qWords);
			return;
		}
		for (int i = 0; i < num; i++) {
			if (ws[i].equals(query)) {
				int start = max(0, i-windowSize + 1);
				int end = min(i+windowSize-1, num-1);
				for (int j = start; j <= end; j++) {
					filter.add(ws[j]);
				}
			}
		}
		
		String segSentence = wordSeg.segSentence(sentence.trim());
		String[] words = segSentence.split(" +");
		for (int i = 0; i < words.length; i++) {
			String w = words[i];
			String[] t = w.split("/");
			if (t.length == 2 && (t[1].contains("n")) && !stopWords.contains(t[0])) {
				if (t[0].startsWith("-")) {
					t[0] = t[0].substring(1);
				}
				
				if (t[0].endsWith(":")) {
					t[0] = t[0].substring(0, t[0].length()-2 );
				}
				if (filter.contains(t[0]))
					qWords.add(t[0]);
			}
		}
	}

	
	public static void main(String[] args) throws IOException {		
		ReadFile rf = new ReadFile("./data/testTopic.txt", "GBK");
		String line = "";
		while((line = rf.readLine()) != null) {
			System.out.println(line);
			process(line);
		}
		rf.close();
	}

	private static void process(String query) throws IOException {
		CoOccurrenceWordFinder test = new CoOccurrenceWordFinder("./data/SnippetForCarrot/");
		Set<String> qWords = test.segment(query);
		Set<String> sWords = test.segmentSentences("./data/Sentence/", query, 100);
		System.out.println(qWords.size());
		System.out.println(sWords.size());
		sWords.removeAll(qWords);
		System.out.println(sWords.size());
		
		String newDir = "./data/Words/";
		CreateDir.createDir(newDir);
		WriteFile wf = new WriteFile(newDir + query + ".txt" , false, "gbk");
		wf.writeLine(query + "\t0");
		for (String word : qWords) {
			wf.writeLine(word + "\t0");
		}
		for (String word : sWords) {
			wf.writeLine(word + "\t1");
		}
		wf.close();
	}
}
