package jears.cslt.xmlparser;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import jears.cslt.util.fileOperation.ReadFile;

import org.jdom.Attribute;
import org.jdom.Comment;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

public class XmlWriter
{
	public static void writeXml(String query, String dirString, String output) throws FileNotFoundException, IOException {
		 //�����ĵ�
        Document root = new Document();
        //������Ԫ��
        Element result = new Element("searchresult");
        //�Ѹ�Ԫ�ؼ��뵽document��
        root.addContent(result); 
        
        //����ע��
//        Comment rootComment = new Comment("�����ݴӳ��������XML�У�");
//        people.addContent(rootComment);
        
        Element queryNode = new Element("query");
        queryNode.setText(query);
        result.addContent(queryNode);

//        //������Ԫ��
//        Element person1 = new Element("person");
//        //��Ԫ�ؼ��뵽��Ԫ����
//        people.addContent(person1);
//        //����person1Ԫ������
//        person1.setAttribute("id", "001");
//        
//        Attribute person1_gender = new Attribute("gender", "male");
//        person1.setAttribute(person1_gender);
//        
//        Element person1_name = new Element("name");
//        person1_name.setText("���»�");
//        person1.addContent(person1_name);
//        
//        Element person1_address = new Element("address");
//        person1_address.setText("���");
//        person1.addContent(person1_address);
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
    	writeXml("����", "./data/SnippetForCarrot/����", "./data/SnippetForCarrot/����.xml");
    }
}

