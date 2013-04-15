package jears.cslt.HuDong;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.htmlparser.util.NodeList;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

public class HuDongParser {
	public HuDongParser(String xmlfile){
		        SAXBuilder builder=new SAXBuilder(false);
		        try {
		            Document doc=builder.build(new File(xmlfile));
		            Element root=doc.getRootElement();
		            
		            System.out.println(root.getChildTextTrim("Backup"));
		            Element cNode = root.getChild("Content");
		            List subNodes = cNode.getChildren();
		            for (Iterator iter = subNodes.iterator(); iter.hasNext();) {
		                Element sNode = (Element) iter.next();
//		                System.out.println(sNode.getChildTextTrim("SubContent"));
		            }
//		            
		            XMLOutputter outputter=new XMLOutputter();
		            outputter.output(doc,new FileOutputStream(xmlfile + ".txt"));
		            
		        } catch (JDOMException e) {
		            e.printStackTrace();
		        } catch (IOException e) {
		            e.printStackTrace();
		        }
		    }
	
	public static void main(String[] args) {
		new HuDongParser("D:\\hudong\\xmls\\½ú½­.xml");
	}
}
