package jears.cslt.HuDong;

import java.io.IOException;
import java.util.LinkedList;

import jears.cslt.util.fileOperation.ReadFile;
import jears.cslt.util.fileOperation.WriteFile;

import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.queryParser.ParseException;

public class Count {
	Searcher searcher;
	
	public Count(String indexDir) throws CorruptIndexException, IOException {
		searcher = new Searcher(indexDir);
	}
	
	public Count(String[] indexDirs) throws CorruptIndexException, IOException {
		searcher = new Searcher(indexDirs);
	}
	
	public void count(String filename) throws IOException, ParseException {
		ReadFile rf = new ReadFile(filename, "gbk");
		WriteFile wf1 = new WriteFile(filename+".count1", false, "gbk");
		WriteFile wf2 = new WriteFile(filename+".count2", false, "gbk");
		LinkedList<String> words = new LinkedList<String>();
		LinkedList<String> source = new LinkedList<String>();
		
		String line = "";
		while((line = rf.readLine()) != null) {
			String[] s = line.split("\t");
			words.add(s[0]);
			source.add(s[1]);
		}
		
		int c = searcher.searchResultCount("\"" + words.get(0) + "\"");
		System.out.println(0 + "\t" + words.get(0) + "\t" + c + "\t" + source.get(0));
		wf1.writeLine(0 + "\t" + words.get(0) + "\t" + c + "\t" + source.get(0));
		int thredshold = words.size()/5000 + 2;
		System.out.println(thredshold);
		for (int j = 1; j < words.size(); j++) {
			if (words.get(j).trim().length() <= 1) {
				words.remove(j);
				source.remove(j);
				j--;
			}else if ((c=searcher.searchResultCount("\"" + words.get(0) + "\" AND \"" + words.get(j) + "\"")) < thredshold) {
				words.remove(j);
				source.remove(j);
				j--;
			} else {
				wf2.writeLine(0 + "\t" + j + "\t" + c + "\t" + words.get(0) + "\t" + words.get(j));
				System.out.print(".");
				if (j % 10 == 0)
					System.out.println(0 + "\t" + j + "\t" + c + "\t" + words.get(j));
			}
		}
		
		for (int i = 1; i < words.size(); i++) {
			c = searcher.searchResultCount("\"" + words.get(i) + "\"");
			System.out.println(i + "\t" + words.get(i) + "\t" + c + "\t" + source.get(i));
			wf1.writeLine(i + "\t" + words.get(i) + "\t" + c + "\t" + source.get(i));
			for (int j = i+1; j < words.size(); j++) {
				c = searcher.searchResultCount("\"" + words.get(i) + "\" AND \"" + words.get(j) + "\"");
				System.out.print(".");
				if (j % 10 == 0)
					System.out.println(i + "\t" + j + "\t" + c + "\t" + words.get(i) + "\t" + words.get(j));
				wf2.writeLine(i + "\t" + j + "\t" + c + "\t" + words.get(i) + "\t" + words.get(j));
			}
		}
		
		wf2.close();
		wf1.close();
		rf.close();
	}
	
	public static void main(String[] args) throws CorruptIndexException, IOException, ParseException {
		Count test = new Count("D:\\graduate\\hudong\\HuDongIndex");
		System.out.println("import index ready!");
		
		ReadFile rf = new ReadFile("./data/testTopic.txt", "GBK");
		String line = "";
		while((line = rf.readLine()) != null) {
			System.out.println(line);
			test.count("./data/Words/" + line + ".txt");
		}
		rf.close();
	}
}
