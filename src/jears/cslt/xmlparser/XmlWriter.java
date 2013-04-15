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
		 //创建文档
        Document root = new Document();
        //创建根元素
        Element result = new Element("searchresult");
        //把根元素加入到document中
        root.addContent(result); 
        
        //创建注释
//        Comment rootComment = new Comment("将数据从程序输出到XML中！");
//        people.addContent(rootComment);
        
        Element queryNode = new Element("query");
        queryNode.setText(query);
        result.addContent(queryNode);

//        //创建父元素
//        Element person1 = new Element("person");
//        //把元素加入到根元素中
//        people.addContent(person1);
//        //设置person1元素属性
//        person1.setAttribute("id", "001");
//        
//        Attribute person1_gender = new Attribute("gender", "male");
//        person1.setAttribute(person1_gender);
//        
//        Element person1_name = new Element("name");
//        person1_name.setText("刘德华");
//        person1.addContent(person1_name);
//        
//        Element person1_address = new Element("address");
//        person1_address.setText("香港");
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
                document.setAttribute("id", f.getName().substring(0, f.getName().indexOf(".")));//添加属性，可以一次添加多个属性
                
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
        
        //设置xml输出格式
        Format format = Format.getPrettyFormat();
        format.setEncoding("utf-8");//设置编码
        format.setIndent("    ");//设置缩进
        
        
        //得到xml输出流
        XMLOutputter out = new XMLOutputter(format);
        //把数据输出到xml中
        out.output(root, new FileOutputStream(output));//或者FileWriter
        
	}
    public static void main(String[] args) throws IOException
    {
    	writeXml("功夫", "./data/SnippetForCarrot/功夫", "./data/SnippetForCarrot/功夫.xml");
    }
}

