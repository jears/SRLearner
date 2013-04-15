package jears.cslt.eval;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import jears.cslt.util.fileOperation.ReadFile;
import jears.cslt.util.fileOperation.WriteFile;
import jears.cslt.xmlparser.GoldClustersWriter;

public class Clusters {
	protected ArrayList<Cluster> clusters;
	protected String query;
	protected Set<String> results;
	protected int resultCount;
	protected int resultToken;

	public Clusters() {
		clusters = new ArrayList<Cluster>();
		results = new TreeSet<String>();
	}
	
	public Clusters(String filename) throws IOException {
		this();
		try {
			SAXBuilder builder = new SAXBuilder();
			Document read_doc = builder.build(filename);
			Element revs = read_doc.getRootElement();

			// List list = revs.getChildren();

			query = revs.getChildText("query");
			List groupList = revs.getChildren("group");

			outer: for (int i = 0; i < groupList.size(); i++) {
				Element group = (Element) groupList.get(i);
				
				String id = group.getAttributeValue("id");
				String score = group.getAttributeValue("score");
				String size = group.getAttributeValue("size");
				Cluster cluster = new Cluster(id, score, size);

				List titleList = group.getChildren("title");
				for (int j = 0; j < titleList.size(); j++) {
					Element title = (Element) titleList.get(j);
					List phraseList = title.getChildren("phrase");
					for (int k = 0; k < phraseList.size(); k++) {
						Element phrase = (Element) phraseList.get(k);
						String titlePhrase = phrase.getText();
//						if (titlePhrase.equals("Other Topics"))
//							break outer;
						cluster.addTitle(titlePhrase);
					}

				}

				List documentList = group.getChildren("document");
				for (int j = 0; j < documentList.size(); j++) {
					Element document = (Element) documentList.get(j);
					String docid = document.getAttributeValue("refid");
					resultCount++;
					results.add(docid);
					cluster.addDocument(docid);
				}

				clusters.add(cluster);

				// System.out.println();
				// System.out.print(thisTopic);
			}
			resultToken = results.size();

		} catch (IOException ex) {
			ex.printStackTrace();
		} catch (JDOMException ex) {
			ex.printStackTrace();
		}
	}

	public ArrayList<Cluster> getCluster() {
		return clusters;
	}
	
	public String getQuery() {
		return query;
	}
	
	public int getResultCount() {
		return resultCount;
	}
	
	public int getResultToken() {
		return resultToken;
	}

	public String getStatistics() {
		return query + "\t" + clusters.size() + "\t" + resultToken + "\t" + resultCount;
	}
	
	public String toString() {
		String ret = "";

		for (int i = 0; i < clusters.size(); i++) {
			ret += clusters.get(i) + "\n";
		}
		return ret;
	}

	public static void main(String[] args) throws IOException {
		Clusters test = new Clusters("./data/ambient/stc/out-of-control.xml");

		System.out.println(test.toString());
	
//		WriteFile lingowf = new WriteFile("./data/ambient/statistics.txt", false, "gbk");
//			
//		ReadFile rf = new ReadFile("./data/ambient/topics.txt", "GBK");
//		String line = "";
//		while((line = rf.readLine()) != null) {
//			System.out.println(line);
//			GoldClustersWriter test = new GoldClustersWriter("./data/ambient/" + line + ".txt");
//			   test.writeGroupXml(line, "./data/ambient/" + line +".xml");
//			Clusters gold = new Clusters("./data/ambient/" +line + ".xml");
//			lingowf.writeLine(gold.getStatistics());
//		}
//		rf.close();
//		
//		lingowf.close();
	}
}
