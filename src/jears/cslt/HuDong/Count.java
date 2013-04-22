package jears.cslt.HuDong;

import java.io.IOException;
import java.util.LinkedList;

import jears.cslt.util.fileOperation.ReadFile;
import jears.cslt.util.fileOperation.WriteFile;

import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.queryParser.ParseException;

/**
 * for the co-occurrence graph 
 * @author jj
 *
 */
public class Count {
	Searcher searcher;
	
	public Count(String indexDir) throws CorruptIndexException, IOException {
		searcher = new Searcher(indexDir);
	}
	
	public Count(String[] indexDirs) throws CorruptIndexException, IOException {
		searcher = new Searcher(indexDirs);
	}
	
	public void count(String filename) throws IOException, ParseException {
		WriteFile wf2 = new WriteFile(filename+".count2", false, "gbk");
		WriteFile wf1 = new WriteFile(filename+".count1", false, "gbk");
		ReadFile rf = new ReadFile(filename, "gbk");
				
		LinkedList<String> words = new LinkedList<String>();
		LinkedList<String> source = new LinkedList<String>();
		
		String line = "";
		while((line = rf.readLine()) != null) {
			String[] s = line.split("\t");
			words.add(s[0]);
			source.add(s[1]);
		}

		rf.close();
		
		int c = searcher.searchResultCount("\"" + words.get(0) + "\"");
		System.out.println(0 + "\t" + words.get(0) + "\t" + c + "\t" + source.get(0));
		wf1.writeLine(0 + "\t" + words.get(0) + "\t" + c + "\t" + source.get(0));
				
		/**
		 * for frequency
		 */
		int thredshold1 = 2*(words.size()/3000)*(words.size()/3000) + 1;
		/**
		 * for cooccurence
		 */
		int thredshold2 = words.size()/2000 + 1;
		System.out.println("frequency\t" + thredshold1 + "\tcooccurence\t" + thredshold2);
		
		for (int j = 1; j < words.size(); j++) {
			int c1  = 0;
			int c2 = 0;
			if (words.get(j).trim().length() <= 1) {
				words.remove(j);
				source.remove(j);
				j--;
			} else if ((c1=searcher.searchResultCount("\"" + words.get(j) + "\"")) < thredshold1) {
				words.remove(j);
				source.remove(j);
				j--;
			} else if ((c2=searcher.searchResultCount("\"" + words.get(0) + "\" AND \"" + words.get(j) + "\"")) < thredshold2) {
				words.remove(j);
				source.remove(j);
				j--;
			} else {
				wf1.writeLine(j + "\t" + words.get(j) + "\t" + c1 + "\t" + source.get(j));
				wf2.writeLine(0 + "\t" + j + "\t" + c2 + "\t" + words.get(0) + "\t" + words.get(j));
//				if (j%10 == 0)
//					System.out.print(".");
				if (j % 100 == 0)
					System.out.println(0 + "\t" + j + "\t" + c2 + "\t" + words.get(j));
			}
		}
		wf1.close();
		
		for (int i = 1; i < words.size(); i++) {
			for (int j = i+1; j < words.size(); j++) {
				c = searcher.searchResultCount("\"" + words.get(i) + "\" AND \"" + words.get(j) + "\"");
//				if (j%10 == 0)
//					System.out.print(".");
				if (j % 100 == 0)
					System.out.println(i + "\t" + j + "\t" + c + "\t" + words.get(i) + "\t" + words.get(j));
				wf2.writeLine(i + "\t" + j + "\t" + c + "\t" + words.get(i) + "\t" + words.get(j));
			}
		}
		
		wf2.close();
	}
	
	public static void main(String[] args) throws CorruptIndexException, IOException, ParseException {
		Count test = new Count("D:\\HuDongIndex");
		System.out.println("import index ready!");
		
		ReadFile rf = new ReadFile("./data/testTopicReverse.txt", "GBK");
		String line = "";
		while((line = rf.readLine()) != null) {
			System.out.println(line);
			test.count("./data/Words/" + line + ".txt");
		}
		rf.close();
	}
}
