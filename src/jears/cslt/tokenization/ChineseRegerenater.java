package jears.cslt.tokenization;

import java.io.IOException;
import java.util.ArrayList;

import jears.cslt.util.fileOperation.ReadFile;

public class ChineseRegerenater {
	public static void main(String[] args) throws InterruptedException {
		String source = "NTCIR10";
		ChineseRegerenater q = new ChineseRegerenater();
		ChineseTokenizer queryTokenizer = new ChineseTokenizer("dict/dictChinese.txt", "gbk");
		q.regenerateQuerySource(source, queryTokenizer);
//		source = "NTCIR10";
//		q.regenerateQuerySource(source, queryTokenizer);
	}

	private void regenerateQuerySource(String source, ChineseTokenizer queryTokenizer) {
		ArrayList<String> topics = getTopics(source + "chtopic.txt", "gbk");
		for (int i = 0; i < topics.size(); i++) {
			System.out.println(i);
			String query = topics.get(i);
			
			ArrayList<String> concepts = queryTokenizer.tokennize(query);
			ArrayList<String> regenerate = regenateQuery(concepts);
			for (int j = 0 ; j < concepts.size(); j++) {
				if (concepts.get(j).length() >= 2) {
					String temp = "\"" + concepts.get(j) + "\"";
					concepts.set(j, temp);
				}
			}
			regenerate.addAll(regenateQuery(concepts));
			
			Printer.print(regenerate, String.valueOf(i + 1), source + "/queryRegenerate", "gbk");
			System.out.println(regenerate);
			
//			ArrayList<String> result = new ArrayList<String>();
//			for (int j = 0; j < regenerate.size(); j++) {
//				String line = regenerate.get(j);
//				System.out.println("\t" + line);
//				ArrayList<String> temp = expandQuery(line);
//				System.out.println("\t" + temp);
//				result.addAll(temp);
//				Printer.print(line, temp, i + ".txt", source + "/candidateQuality");
//				Thread.sleep(1000*2);
//			}
//		
////			Printer.print(result, i + ".txt", source + "/candidateexpansion");
//			Thread.sleep(1000*10);
		}
	}
	
	private ArrayList<String> getTopics(String filename, String encode) {
		ArrayList<String> ret = new ArrayList<String>();
		
		try {
			ReadFile rf = new ReadFile(filename, encode);
			String line = "";
			while ((line = rf.readLine()) != null) {
				line = line.trim();
				ret.add(line);
			}
			rf.close();
		} catch (IOException e) {
			System.err.println("[error: topicFile not found]");
			e.printStackTrace();
		}
		
		return ret;
	}
	

	
	private ArrayList<String> regenateQuery(ArrayList<String> query) {
		ArrayList<String> ret = new ArrayList<String>();
		Rule r = new Rule();
		r.changeOrder("", query, 0);
//		r.addPrep(query);
		ret.addAll(r.order);
//		ret.addAll(r.addprep);
		return ret;
	}
}
