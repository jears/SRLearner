package jears.cslt.HuDong;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import jears.cslt.util.fileOperation.ReadFile;
import jears.cslt.util.fileOperation.WriteFile;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.WhitespaceAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.TopDocsCollector;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class HuDongSearcher {
	private IndexSearcher indexSearcher;
	private static int totalResultCount = 100000000;
	
	public HuDongSearcher(String indexDir) throws CorruptIndexException, IOException {
		indexSearcher = new IndexSearcher(FSDirectory.open(new File(indexDir)));
	}
	
	public int searchResultCount(String queryStr) throws ParseException, IOException {
		QueryParser queryParser = new QueryParser(Version.LUCENE_31, "sentence", new StandardAnalyzer(Version.LUCENE_31));
		Query query = queryParser.parse(queryStr);
		TopDocs topDocs = indexSearcher.search(query, totalResultCount);
		return topDocs.totalHits;
	}
	
//	public TopDocs search(String queryStr) throws ParseException, IOException {
//		QueryParser queryParser = new QueryParser(Version.LUCENE_31, "sentence", new StandardAnalyzer(Version.LUCENE_31));
//		Query query = queryParser.parse(queryStr);
//		TopDocs topDocs = indexSearcher.search(query, totalResultCount);
//		return topDocs;
//	}
	
	public void searchReaults(String queryStr) throws ParseException, IOException {
		int hitPerPage = 100;
		QueryParser queryParser = new QueryParser(Version.LUCENE_31, "sentence", new StandardAnalyzer(Version.LUCENE_31));
		try {
			Query query = queryParser.parse(queryStr);
			ScoreDoc lastDoc = new ScoreDoc(0, 0);
			for (int pageNo = 0; pageNo < 10000000; pageNo++) {
//				System.out.println("PAGE " + pageNo);
				TopDocsCollector collector;
				if (pageNo == 0) {
					collector = TopScoreDocCollector.create(hitPerPage, true);
				}
				else {
					collector = TopScoreDocCollector.create(hitPerPage, lastDoc, true);
				}
				
				indexSearcher.search(query, collector);
				ScoreDoc[] hits = collector.topDocs().scoreDocs;
				int resultNum = hits.length;
//				System.out.println("Search resultNum for " + queryStr + " is " + resultNum);
				
				for(int i=0;i<resultNum;i++){
					int docId = hits[i].doc;
					Document doc = indexSearcher.doc(docId);
//					System.out.println(doc.get("sentence"));
				}
				
				if (resultNum < hitPerPage)
					break;
				else {
					lastDoc = hits[resultNum-1];
//					System.out.println("LASTDOC " + indexSearcher.doc(lastDoc.doc).get("sentence"));
				}
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CorruptIndexException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
	
	public void searchReaultsToFile(String queryStr, String outputfile) throws ParseException, IOException {
		WriteFile wf = new WriteFile(outputfile, false, "gbk");
		int hitPerPage = 100;
		QueryParser queryParser = new QueryParser(Version.LUCENE_31, "sentence", new StandardAnalyzer(Version.LUCENE_31));
		try {
			Query query = queryParser.parse(queryStr);
			ScoreDoc lastDoc = new ScoreDoc(0, 0);
			for (int pageNo = 0; pageNo < 1000000; pageNo++) {
//				System.out.println("PAGE " + pageNo);
				TopDocsCollector collector;
				if (pageNo == 0) {
					collector = TopScoreDocCollector.create(hitPerPage, true);
				}
				else {
					collector = TopScoreDocCollector.create(hitPerPage, lastDoc, true);
				}
				
				indexSearcher.search(query, collector);
				ScoreDoc[] hits = collector.topDocs().scoreDocs;
				int resultNum = hits.length;
//				System.out.println("Search resultNum for " + queryStr + " is " + resultNum);
				
				for(int i=0;i<resultNum;i++){
					int docId = hits[i].doc;
					Document doc = indexSearcher.doc(docId);
					wf.writeLine(doc.get("sentence"));
				}
				
				if (resultNum < hitPerPage)
					break;
				else {
					lastDoc = hits[resultNum-1];
//					System.out.println("LASTDOC " + indexSearcher.doc(lastDoc.doc).get("sentence"));
				}
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CorruptIndexException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		wf.close();
	}
	
	public static void main(String[] args) throws CorruptIndexException, IOException, ParseException {
		HuDongSearcher searcher= new HuDongSearcher("d:/HuDongIndex");
//		int count = searcher.searchResultCount("\"Ä¾Âí\"");
//		System.out.println(count);
		ReadFile rf = new ReadFile("./data/testTopic.txt", "GBK");
		String line = "";
		while((line = rf.readLine()) != null) {
			System.out.println(line);
			searcher.searchReaultsToFile("\"" + line + "\"", "./data/Sentence/" + line + ".txt");
		}
		rf.close();
//		searcher.searchReaultsToFile("\"Èä³æ\"", "./data/Sentence/Èä³æ.txt");
//		long end = new Date().getTime();
	}
}
