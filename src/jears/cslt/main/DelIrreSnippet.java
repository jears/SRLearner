package jears.cslt.main;

import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.TreeSet;

import jears.cslt.util.fileOperation.CreateDir;
import jears.cslt.util.fileOperation.MoveFile;
import jears.cslt.util.fileOperation.ReadFile;
import jears.cslt.xmlparser.XmlWriter;

public class DelIrreSnippet {
	public static void main(String[] args) throws IOException {
		ReadFile rf = new ReadFile("./data/testTopic.txt", "GBK");
		String line = "";
		while((line = rf.readLine()) != null) {
			System.out.println(line);
			process(line);
		}
		rf.close();
	}
	
	public static void process(String query) throws IOException {
		CreateDir.createDir("./data/SnippetForSRC/" + query);
		Set<String> rel = new TreeSet<String>();
		ReadFile rf = new ReadFile("./data/annotation/" + query + ".txt");
		String line = "";
		while((line = rf.readLine()) != null) {
			String[] temp = line.split("\t");
			rel.add(temp[1]);
		}
		rf.close();
		
		File dir = new File("./data/SnippetForCarrot/" + query);
		String[] files = dir.list();
		for (String f : files) {
			String temp[] = f.split("\\.");
			if (rel.contains(temp[0])) {
				MoveFile mf = new MoveFile(new File("./data/SnippetForCarrot/" + query + "/" + f));
				mf.move("./data/SnippetForSRC/" + query + "/" + f);
			}
		}
		
		XmlWriter.writeXml(query, "./data/SnippetForSRC/" + query, "./data/SnippetForSRC/" + query + ".xml");
		
	}
}
