package jears.cslt.xmlparser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import jears.cslt.util.fileOperation.ReadFile;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

public class GoldClustersWriter {
	private TreeMap<String, TreeSet<String>> clusters;
	
	public GoldClustersWriter() {
		clusters = new TreeMap<String, TreeSet<String>>();
	}
	
	public GoldClustersWriter(String clusterfile) throws IOException {
		this();
		ReadFile rf = new ReadFile(clusterfile);
		
		String line = "";
		while((line=rf.readLine())!=null) {
			String[] c = line.split("\t");
			if (c.length < 2)
				break;
			if (clusters.containsKey(c[0]))
				clusters.get(c[0]).add(c[1]);
			else {
				TreeSet<String> s = new TreeSet<String>();
				s.add(c[1]);
				clusters.put(c[0], s);
			}
		}
		
//		System.out.println(clusters);
		
		rf.close();
	}
	
	public void writeGroupXml(String query, String output) throws FileNotFoundException, IOException {
		 //�����ĵ�
      Document root = new Document();
      //������Ԫ��
      Element result = new Element("searchresult");
      //�Ѹ�Ԫ�ؼ��뵽document��
      root.addContent(result); 
      
      //����ע��
//      Comment rootComment = new Comment("�����ݴӳ��������XML�У�");
//      people.addContent(rootComment);
      
      Element queryNode = new Element("query");
      queryNode.setText(query);
      result.addContent(queryNode);

//      //������Ԫ��
//      Element person1 = new Element("person");
//      //��Ԫ�ؼ��뵽��Ԫ����
//      people.addContent(person1);
//      //����person1Ԫ������
//      person1.setAttribute("id", "001");
//      
//      Attribute person1_gender = new Attribute("gender", "male");
//      person1.setAttribute(person1_gender);
//      
//      Element person1_name = new Element("name");
//      person1_name.setText("���»�");
//      person1.addContent(person1_name);
//      
//      Element person1_address = new Element("address");
//      person1_address.setText("���");
//      person1.addContent(person1_address);
//      
      
      for (String intentNo : clusters.keySet()) {
   	   Element group = new Element("group");
          result.addContent(group);
          group.setAttribute("id", intentNo);//������ԣ�����һ����Ӷ������
          
          Element title = new Element("title");
          Element phrase = new Element("phrase");
          phrase.setText("");
          title.addContent(phrase);
          group.addContent(title);

          for (String resultNo : clusters.get(intentNo)) {
       	   Element documentId = new Element("document");
       	   documentId.setAttribute("refid", resultNo);
       	   group.addContent(documentId);
          }
      }
      
      //����xml�����ʽ
      Format format = Format.getPrettyFormat();
      format.setEncoding("utf-8");//���ñ���
      format.setIndent("    ");//��������
      
      
      //�õ�xml�����
      XMLOutputter out = new XMLOutputter(format);
      //�����������xml��
      out.output(root, new FileOutputStream(output));//����FileWriter
      
	}
	
	public void writeXml(String query, String dirString, String output) throws FileNotFoundException, IOException {
		 //�����ĵ�
       Document root = new Document();
       //������Ԫ��
       Element result = new Element("searchresult");
       //�Ѹ�Ԫ�ؼ��뵽document��
       root.addContent(result); 
       
       //����ע��
//       Comment rootComment = new Comment("�����ݴӳ��������XML�У�");
//       people.addContent(rootComment);
       
       Element queryNode = new Element("query");
       queryNode.setText(query);
       result.addContent(queryNode);

//       //������Ԫ��
//       Element person1 = new Element("person");
//       //��Ԫ�ؼ��뵽��Ԫ����
//       people.addContent(person1);
//       //����person1Ԫ������
//       person1.setAttribute("id", "001");
//       
//       Attribute person1_gender = new Attribute("gender", "male");
//       person1.setAttribute(person1_gender);
//       
//       Element person1_name = new Element("name");
//       person1_name.setText("���»�");
//       person1.addContent(person1_name);
//       
//       Element person1_address = new Element("address");
//       person1_address.setText("���");
//       person1.addContent(person1_address);
//       
       
       File dir = new File(dirString);
       File[] files = dir.listFiles();
       for (File f : files) {
       	if (Integer.valueOf(f.getName().substring(0, f.getName().indexOf("."))) >= 100){
       		f.delete();
       		continue;
       	}
       	ReadFile rf = new ReadFile(f.getAbsolutePath(), "gbk");
       	String line = "";
       	line = rf.readLine();
       	if (line != null) {
       		Element document = new Element("document");
               result.addContent(document);
               document.setAttribute("id", f.getName().substring(0, f.getName().indexOf(".")));//������ԣ�����һ����Ӷ������
               
               Element title = new Element("title");
               title.setText(line);
               document.addContent(title);
               
               line = rf.readLine();
               if (line != null) {
               	Element url = new Element("url");
               	url.setText(line);
               	document.addContent(url);
               }
               
               line = rf.readLine();
               if (line != null) {
               	Element snippet = new Element("snippet");
               	snippet.setText(line);
               	document.addContent(snippet);
               }
       	}
       	
       }
       
       for (String intentNo : clusters.keySet()) {
    	   Element group = new Element("group");
           result.addContent(group);
           group.setAttribute("id", intentNo);//������ԣ�����һ����Ӷ������
           
           Element title = new Element("title");
           Element phrase = new Element("phrase");
           phrase.setText("");
           title.addContent(phrase);
           group.addContent(title);

           for (String resultNo : clusters.get(intentNo)) {
        	   Element documentId = new Element("document");
        	   documentId.setAttribute("refid", resultNo);
        	   group.addContent(documentId);
           }
       }
       
       //����xml�����ʽ
       Format format = Format.getPrettyFormat();
       format.setEncoding("utf-8");//���ñ���
       format.setIndent("    ");//��������
       
       
       //�õ�xml�����
       XMLOutputter out = new XMLOutputter(format);
       //�����������xml��
       out.output(root, new FileOutputStream(output));//����FileWriter
       
	}
   public static void main(String[] args) throws IOException
   {
	   ReadFile rf = new ReadFile("./data/testTopic.txt", "gbk");
	   String line = "";
	   while((line = rf.readLine()) != null) {
		   GoldClustersWriter test = new GoldClustersWriter("./data/annotation/" + line + ".txt");
		   test.writeXml(line, "./data/SnippetForCarrot/" + line, "./data/annotationXML/" + line +".xml");
	   }
	}
}
