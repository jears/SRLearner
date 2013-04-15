package jears.cslt.eval;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import jears.cslt.util.fileOperation.ReadFile;

public class AmbientMapper extends Clusters{
	Map<String, String> URL2No;
	Map<String, String> No2No;
	
	public AmbientMapper(String currentNo) throws IOException {
		super();
		URL2No = new TreeMap<String, String>();
		No2No = new TreeMap<String, String>();
		ReadFile rf = new ReadFile("./data/ambient/ambient/results.txt");
		
		String line = "";
		while((line = rf.readLine()) != null) {
			String[] temp = line.split("\t");
			String[] no = temp[0].split("\\.");
			String topicNo = no[0];
			String resultNo = no[1];
			if (no[0].equals(currentNo))
				URL2No.put(temp[1], no[1]);
		}
		rf.close();
	}
	
	public void getClusters(String filename) {
		try {
			SAXBuilder builder = new SAXBuilder();
			Document read_doc = builder.build(filename);
			Element revs = read_doc.getRootElement();

			// List list = revs.getChildren();

			query = revs.getChildText("query");
			
			List dList = revs.getChildren("document");
			for (int i = 0; i < dList.size(); i++) {
				Element d = (Element) dList.get(i);
				String id = d.getAttributeValue("id");
				String url = d.getChildText("url");
				String aid = URL2No.get(url);
				No2No.put(id, aid);
			}
			
			List groupList = revs.getChildren("group");

			for (int i = 0; i < groupList.size(); i++) {
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
						cluster.addTitle(titlePhrase);
					}

				}

				List documentList = group.getChildren("document");
				for (int j = 0; j < documentList.size(); j++) {
					Element document = (Element) documentList.get(j);
					String docid = document.getAttributeValue("refid");
					resultCount++;
					results.add(docid);
					cluster.addDocument(No2No.get(docid));
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
	
	public static void main(String[] args) throws IOException {
		ReadFile rf = new ReadFile("./data/ambient/ambient/topics.txt");
		String line = "";
		while((line = rf.readLine()) != null) {
			String[] temp = line.split("\t");
			AmbientMapper test = new AmbientMapper(temp[0]);
			test.getClusters("./data/ambient/stc/" + temp[1].toLowerCase().replace(" ", "-") + ".xml");
			System.out.println(test.getStatistics());
		}
	}
}
