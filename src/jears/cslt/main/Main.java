package jears.cslt.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import cslt.subtopic.apcluster.APMain;

import tfidf.TfIdfCal;

import jears.cslt.preprocess.StopRemover;
import jears.cslt.subtopic.model.SearchResultGroup;
import jears.cslt.subtopic.printer.TestPrinter;
import jears.cslt.subtopic.searchResultsExtractor.GoogleSearchResultDownloader;
import jears.cslt.util.fileOperation.ReadFile;
import jears.cslt.xmlparser.XmlWriter;
import ICTCLAS.I3S.AC.IctWordSeg;

public class Main {
	public static String dirname = "./data/Snippet";
	public static void main(String[] args) throws IOException, InterruptedException {
//		String query = "Н№гу";
//		process(query);
		
		segment();
		
		ReadFile rf = new ReadFile("./data/testTopic.txt", "GBK");
		String line = "";
		while((line = rf.readLine()) != null) {
			System.out.println(line);
			process(line);
		}
		rf.close();
		
	}
	
	private static void segment() {
		IctWordSeg test = new IctWordSeg("GB2312");
		File dir = new File(dirname);
		for (File dir2 : dir.listFiles()) {
			for (File file : dir2.listFiles()) {
				test.segFile(file.getAbsolutePath(), file.getAbsolutePath().replaceAll("Snippet", "Snippet-Seg"), 1);
//				StopRemover.removeStopWordFile(file.getAbsolutePath().replaceAll("Snippet", "Snippet-Seg"), file.getAbsolutePath().replaceAll("Snippet", "Snippet-Remove"));
			}
		}
		System.out.println("--------------------------2.preprocess finished----------------------------");
	}
	
	private static void process(String query) throws InterruptedException, FileNotFoundException, IOException {
		GoogleSearchResultDownloader googleContent = new GoogleSearchResultDownloader(
				12, new TestPrinter());
		googleContent.extractSearchResults(query);
		XmlWriter.writeXml(query, "./data/SnippetForCarrot/" + query, "./data/SnippetForCarrot/" + query + ".xml");
		System.out.println("--------------------------1.download finished----------------------------");
		
		Thread.sleep(1000*60);
//		IctWordSeg test = new IctWordSeg("GB2312");
//		File dir = new File(dirname);
//		for (File dir2 : dir.listFiles()) {
//			for (File file : dir2.listFiles()) {
//				test.segFile(file.getAbsolutePath(), file.getAbsolutePath().replaceAll("Snippet", "Snippet-Seg"), 0);
//				StopRemover.removeStopWordFile(file.getAbsolutePath().replaceAll("Snippet", "Snippet-Seg"), file.getAbsolutePath().replaceAll("Snippet", "Snippet-Remove"));
//			}
//		}
//		System.out.println("--------------------------2.preprocess finished----------------------------");
//		
//		dir = new File(dirname + "-Remove");
//		for (File dir2 : dir.listFiles()) {
//			System.out.println(dir2.getAbsolutePath());
//			TfIdfCal.getTfIdfSim(dir2.getAbsolutePath());
//		}
//		System.out.println("--------------------------3.tfidf finished----------------------------");
//
//		APMain.cluster(dirname + "-matrix");
//		System.out.println("--------------------------4.apcluster finished----------------------------");
	}
}
