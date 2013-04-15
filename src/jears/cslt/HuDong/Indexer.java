package jears.cslt.HuDong;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

import jears.cslt.util.fileOperation.CreateDir;
import jears.cslt.util.fileOperation.ReadFile;
import jears.cslt.util.fileOperation.WriteFile;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.htmlparser.Parser;
import org.htmlparser.beans.StringBean;
import org.htmlparser.util.ParserException;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

/**
 * Index SogouT, including the following steps 1.segement the big SogouT file
 * into small parts, one web page per part. 2.transform the charset of the web
 * into UTF-8 3.extract the text in web pages 4.segment the text into sentenses
 * 5.index the sentenses with <sentense, docNo>
 * 
 * @author jears
 * 
 */
public class Indexer {
	/**
	 * the input SogouT file, genereted from TextFilter
	 */
	private String docPath;
	/**
	 * the path where index is saved
	 */
	private String indexPath;

	/**
	 * the dir where the input SogouT file to be indexed is saved
	 */
	private String dirPath;

	Directory dir;
	private IndexWriter indexWriter;
	Analyzer analyzer;
	IndexWriterConfig iwc;

	/**
	 * create index in indexPath for SogouT file docPath
	 * 
	 * @param docPath
	 * @param indexPath
	 */
	public Indexer(String docPath, String indexPath) {
		try {
			this.docPath = docPath;
			this.indexPath = indexPath;
			CreateDir.createDir(indexPath);
			writerConfig();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * configure IndexWriter
	 */
	private void writerConfig() {
		try {
			this.dir = FSDirectory.open(new File(indexPath));
			this.analyzer = new StandardAnalyzer(Version.LUCENE_31);
			// Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_31);
			this.iwc = new IndexWriterConfig(Version.LUCENE_31, analyzer);
			iwc.setOpenMode(OpenMode.CREATE_OR_APPEND);
			this.indexWriter = new IndexWriter(dir, iwc);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * for HuDong
	 * 
	 * @param hdDir
	 */
	public void indexHuDong(String hdDir) {
		File dir = new File(hdDir);
		String[] files = dir.list();

		SAXBuilder builder = new SAXBuilder(false);
		for (String f : files) {
			try {
				CreateDir.createDir(hdDir.replace("xml", "txt"));
				org.jdom.Document doc = builder.build(new File(hdDir + f));
				WriteFile wf = new WriteFile((hdDir + f).replace("xml", "txt"),
						false, "gbk");

				Element root = doc.getRootElement();
				String title = root.getChildTextTrim("Title");
				String abstract_ = root.getChildTextTrim("Abstract");
				String backup = root.getChildTextTrim("Backup");
				System.out.println(title);
				wf.writeLine(title);
				wf.writeLine(abstract_);
				wf.writeLine(backup);

				indexSentence(title, f);
				indexSentence(abstract_, f);
				indexText(backup, f);

				Element cNode = root.getChild("Content");
				List subNodes = cNode.getChildren();
				for (Iterator iter = subNodes.iterator(); iter.hasNext();) {
					Element sNode = (Element) iter.next();
					
					String stitle = sNode.getChildTextTrim("SubTitle");
					wf.writeLine(stitle);
					indexSentence(stitle, f);
					
					String scontent = sNode.getChildTextTrim("SubContent");
					wf.writeLine(scontent);
					indexText(scontent, f);
				}
				wf.close();
			} catch (JDOMException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void indexText(String str, String docNo) throws IOException {
		String[] sentences = str.split("\r\n|\\?|\\.|!|\r|\n|。|！|？|…�?|…|~|\\|");
		for (String s : sentences) {
			if (s.trim().length() > 4)
				indexSentence(s.trim(), docNo);
		}
	}

	private void indexSentence(String str, String docNo) {
		Document indexDoc = new Document();
		indexDoc.add(new Field("docNo", docNo, Field.Store.YES,
				Field.Index.NOT_ANALYZED));
		indexDoc.add(new Field("sentence", str, Field.Store.YES,
				Field.Index.ANALYZED));

		try {
			indexWriter.addDocument(indexDoc);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void close() {
		try {
			indexWriter.close();
			analyzer.close();
			dir.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String args[]) {
		String docPath = "G:\\WangJunjun\\HuDong\\hudong\\txts\\";
		String xmlPath = "G:\\WangJunjun\\HuDong\\hudong\\xmls\\";
		String indexPath = "G:\\WangJunjun\\HuDong\\hudong\\HuDongIndex\\";

		long start = new Date().getTime();

		Indexer test = new Indexer(docPath, indexPath);
		test.indexHuDong(xmlPath);
		test.close();

		long end = new Date().getTime();

		System.out.println("indexing " + docPath + " file took "
				+ (end - start) / 1000 + " seconds");
		//
	}

}
