package jears.cslt.HuDong;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.WhitespaceAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.MultiReader;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Collector;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.TopDocsCollector;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.search.TotalHitCountCollector;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class Searcher {
	private IndexSearcher indexSearcher;
	private static int totalResultCount = 10000000;
	
	public Searcher(String indexDir) throws CorruptIndexException, IOException {
		indexSearcher = new IndexSearcher(FSDirectory.open(new File(indexDir)));
	}
	
	public Searcher(String[] indexDirs) {
		int indexNum = indexDirs.length;
		IndexReader[] iReaders = new IndexReader[indexNum];
		for (int i = 0; i < indexNum; i++) {
			try {
				iReaders[i] = IndexReader.open(FSDirectory.open(new File(indexDirs[i])));
			} catch (CorruptIndexException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		MultiReader mr = new MultiReader(iReaders);
		indexSearcher = new IndexSearcher(mr);
	}
	
	public int searchResultCount(String queryStr) throws ParseException, IOException {
		QueryParser queryParser = new QueryParser(Version.LUCENE_31, "sentence", new StandardAnalyzer(Version.LUCENE_31));
		Query query = queryParser.parse(queryStr);
		TotalHitCountCollector collector = new TotalHitCountCollector(); 
	    indexSearcher.search(query, collector);
		return collector.getTotalHits();
	}
	
	public TopDocs search(String queryStr) throws ParseException, IOException {
		QueryParser queryParser = new QueryParser(Version.LUCENE_31, "sentence", new StandardAnalyzer(Version.LUCENE_31));
		Query query = queryParser.parse(queryStr);
		TopDocs topDocs = indexSearcher.search(query, totalResultCount);
		ScoreDoc[] docs = topDocs.scoreDocs;
		return topDocs;
	}
	
	public void searchReaults(String queryStr) throws ParseException, IOException {
		TopDocs topDocs = search(queryStr);
		ScoreDoc[] docs = topDocs.scoreDocs;
		System.out.println(docs.length);
		for (int i = 0; i < docs.length; i++) {
			Document doc = indexSearcher.doc(docs[i].doc);
			System.out.println(doc.get("sentence"));
		}
	}
	
	public static void main(String[] args) throws CorruptIndexException, IOException, ParseException {
		Searcher searcher= new Searcher("G:\\WangJunjun\\SogouTIndex\\SogouTIndex311");
		int count = searcher.searchResultCount("\"Å®º¢\"");
		System.out.println(count);
//		searcher.searchReaults("\"ÃÀ\"");
//		long end = new Date().getTime();
	}
}
