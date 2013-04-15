package jears.cslt.tokenization;

import java.io.IOException;
import java.util.ArrayList;

import jears.cslt.util.fileOperation.CreateDir;
import jears.cslt.util.fileOperation.ReadFile;
import jears.cslt.util.fileOperation.WriteFile;

public class ChineseTokenizer {
	Dict dict;
	
	public ChineseTokenizer(String dictFile, String encode) {
		dict = new Dict(dictFile, "gbk");
		
		System.out.println(dict.getMaxLength());
	}
	
	public ArrayList<String> ptokenize(String query) {
		query = query.toLowerCase();
		ArrayList<String> concepts = new ArrayList<String>();
		String[] words = query.split(" ");
		for (int begin = 0; begin < words.length; begin++) {
			for (int length = min(dict.getMaxLength(), words.length - begin); length > 0; length--) {
				String con = "";
				for (int i = begin; i <= begin + length - 1; i++) {
					con += words[i];
				}
				con = con.trim();
				
				if (dict.hasConcetp(con) && length > 1) {
					concepts.add(con);
					begin = begin + length - 1;
					break;
				} else if (length == 1)
					concepts.add(con);
			}
		}
		return concepts;
	}

	public ArrayList<String> ntokenize(String query) {
		query = query.toLowerCase();
		ArrayList<String> concepts = new ArrayList<String>();
		String[] words = query.split(" ");
		for (int end = words.length-1; end >= 0; end--) {
			for (int length = min(dict.getMaxLength(), end + 1); length > 0; length--) {
				String con = "";
				for (int i = end - length + 1; i <= end; i++) {
					con += words[i];
				}
				con = con.trim();
				
				if (dict.hasConcetp(con)) {
					concepts.add(con);
					end = end -length + 1;
					break;
				} else if (length == 1)
					concepts.add(con);
			}
		}
		
		ArrayList<String> ret = new ArrayList<String>();
		for (int i = concepts.size()-1; i >= 0; i--)
			ret.add(concepts.get(i));
		
		return ret;
	}
	
	public ArrayList<String> tokennize(String query) {
		ArrayList<String> ptemp = ptokenize(query);
		String p = ptemp.toString();
		String n = ntokenize(query).toString();
//		System.out.println("p = " + p);
//		System.out.println("n = " + n);
		if (p.equals(n))
			return ptemp;
		else {
			String[] temp = query.split(" ");
			ArrayList<String> ret = new ArrayList<String>();
			for (int i = 0; i < temp.length; i++) {
				ret.add(temp[i]);
				System.out.println(temp[i] + "*");
			}
			System.out.println("w = " + ret.toString());
			System.out.println("q = " + query + "*");
			System.out.println("p = " + p + "*");
			System.out.println("n = " + n + "*");
			return ret;
		}
	}
	
	public int min(int a, int b) {
		if (a > b)
			return b;
		else
			return a;
	}
	
	public ArrayList<String> addquot(ArrayList<String> concepts) {
		if (concepts == null || concepts.isEmpty())
			return null;
		ArrayList<String> quot = new ArrayList<String>();
		for (int i = 0; i < concepts.size(); i++) {
			quot.add("\"" + concepts.get(i) + "\"");
		}
		return quot;
	}
	
	public String print(ArrayList<String> concepts) {
		if (concepts == null || concepts.isEmpty())
			return null;
		String ret = "";
		for (int i = 0; i < concepts.size()-1; i++) {
			ret += "\"" + concepts.get(i) + "\" ";
		}
		ret += "\"" + concepts.get(concepts.size()-1) + "\"";
		return ret;
	}
	
	public static void main(String[] args) throws IOException {
		
		ChineseTokenizer test = new ChineseTokenizer("dict/dictChinese.txt", "gbk");
		
//		System.out.println(test.ntokenize("閹达拷閻栵拷娴狅拷閺堬拷妤癸拷鐞涳拷));
		
		String source = "NTCIR10";
		tokenizeQuery(source, test);
		source = "NTCIR9";
		tokenizeQuery(source, test);
		
//		QueryTokenizer test = new QueryTokenizer("dict/sample", "gbk");
//		test.tokennize("idaho state flower");
	}

	private static void tokenizeQuery(String source, ChineseTokenizer test)
			throws IOException {
		ReadFile rf = new ReadFile(source + "chtopic.txt", "GBK");
		WriteFile wf = new WriteFile(source + "/" + source + "-Concept.txt", false, "gbk");
		String dir = source + "/" + source + "concept-ambigu-word/";
		CreateDir.createDir(dir);
		String line = "";
		int i = 0;
		while ((line = rf.readLine()) != null) {
			System.out.print(line + "\t");
			line = line.trim();
			i++;
			ArrayList<String> t = test.tokennize(line);
			String temp = test.print(t);
			WriteFile f = new WriteFile(dir + i, false, "gbk");
			
			for (int j = 0; j < t.size(); j++) {
				f.writeLine(t.get(j), "\n");
			}
			

				wf.writeLine(temp, "\n");
				System.out.println(temp);

			f.close();
			
		}
		wf.close();
		rf.close();
	}
}
