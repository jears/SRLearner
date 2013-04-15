package jears.cslt.subtopic.printer;

import java.io.IOException;

import jears.cslt.subtopic.model.Fragement;
import jears.cslt.subtopic.model.FragementGroup;
import jears.cslt.subtopic.model.SearchResultGroup;
import jears.cslt.subtopic.model.Topic;
import jears.cslt.subtopic.util.Constants;
import jears.cslt.util.fileOperation.*;
import jears.cslt.util.fileOperation.ReadFile;
import jears.cslt.util.fileOperation.WriteFile;

public class Printer implements PrinterInterface {
	Topic topic;
	
	public Printer(Topic topic) {
		this.topic = topic;
	}
	
	//save html pages linked to the urls of search results
	/* (non-Javadoc)
	 * @see cslt.subtopic.printer.PrinterInterface#printHtml(java.lang.String, int)
	 */
	@Override
	public void printHtml(String htmlString, int resultNo) throws IOException {
		String dirname = "data/" + topic.getSource() + "/html/" + Format.get0X(topic.getNo());
		CreateDir.createDir(dirname);
		WriteFile wf = new WriteFile(dirname + "/" + Format.get0X(resultNo), false);
		wf.write(htmlString);
		wf.close();
	}
	
	/* (non-Javadoc)
	 * @see cslt.subtopic.printer.PrinterInterface#printSERP(java.lang.String, int)
	 */
	@Override
	public void printSERP(String SERPString, int pageNo) throws IOException {
		String dirname = "data/" + topic.getSource() + "/SERP/" + Format.get0X(topic.getNo());
		CreateDir.createDir(dirname);
		WriteFile wf = new WriteFile(dirname + "/" + Format.get0X(pageNo), false);
		wf.write(SERPString);
		wf.close();
	}
	
	/* (non-Javadoc)
	 * @see cslt.subtopic.printer.PrinterInterface#printTitleAndUrl(cslt.subtopic.model.SearchResultGroup)
	 */
	@Override
	public void printTitleAndUrl(SearchResultGroup searchResultGroup) throws IOException {
//		String dirname = "data/" + topic.getSource() + "/titleAndUrl/";
//		CreateDir.createDir(dirname);
//		WriteFile wf = new WriteFile(dirname + "/" + Format.get0X(topic.getNo()), false);
//		wf.write(utf82Ascii(searchResultGroup.toString()));
//		wf.close();
		
//		gbkTitle(searchResultGroup);
	}
	
	public void printSnippet(SearchResultGroup searchResultGroup) throws IOException {
//		String dirname = "data/" + topic.getSource() + "/Snippet/";
//		CreateDir.createDir(dirname);
//		WriteFile wf = new WriteFile(dirname + "/" + Format.get0X(topic.getNo()), false);
//		wf.write(utf82Ascii(searchResultGroup.toString()));
//		wf.close();
		
//		gbkTitle(searchResultGroup);
	}
	
	/* (non-Javadoc)
	 * @see cslt.subtopic.printer.PrinterInterface#printFragement(cslt.subtopic.fragementExtractor.fragement.FragementGroup)
	 */
	@Override
	public void printFragement(FragementGroup fragementGroup) throws IOException {
		String dirname1 = "data/" + topic.getSource() + "/fragement/title/";
		String dirname2 = "data/" + topic.getSource() + "/fragement/bold/";
		String dirname3 = "data/" + topic.getSource() + "/fragement/plain/";
		String dirname4 = "data/" + topic.getSource() + "/fragement/plainString/";
		CreateDir.createDir(dirname1);
		CreateDir.createDir(dirname2);
		CreateDir.createDir(dirname3);
		CreateDir.createDir(dirname4);
		WriteFile wf1 = new WriteFile(dirname1 + "/" + Format.get0X(topic.getNo()), false);
		WriteFile wf2 = new WriteFile(dirname2 + "/" + Format.get0X(topic.getNo()), false);
		WriteFile wf3 = new WriteFile(dirname3 + "/" + Format.get0X(topic.getNo()), false);
		WriteFile wf4 = new WriteFile(dirname4 + "/" + Format.get0X(topic.getNo()), false);
		
		for (int i = 0; i < fragementGroup.size(); i++) {
			wf1.writeLine(fragementGroup.get(i).getTitle(), "\n");
			wf2.write(fragementGroup.get(i).getBold2String());
			wf3.write(fragementGroup.get(i).getPlain2String());
			wf4.writeLine(fragementGroup.get(i).getPlain(), "\n");
		}
		
		wf4.close();
		wf3.close();
		wf2.close();
		wf1.close();
//		gbkPrintFragement(fragementGroup);
//		asciiPrintFragement(fragementGroup);
	}
	
	public void printFragement2(FragementGroup fragementGroup) throws IOException {
		String dirname1 = "data/" + topic.getSource() + "/fragement2/title/";
		String dirname2 = "data/" + topic.getSource() + "/fragement2/bold/";
		String dirname3 = "data/" + topic.getSource() + "/fragement2/plain/";
		String dirname4 = "data/" + topic.getSource() + "/fragement2/plainString/";
		CreateDir.createDir(dirname1);
		CreateDir.createDir(dirname2);
		CreateDir.createDir(dirname3);
		CreateDir.createDir(dirname4);
		WriteFile wf1 = new WriteFile(dirname1 + "/" + Format.get0X(topic.getNo()), false);
		WriteFile wf2 = new WriteFile(dirname2 + "/" + Format.get0X(topic.getNo()), false);
		WriteFile wf3 = new WriteFile(dirname3 + "/" + Format.get0X(topic.getNo()), false);
		WriteFile wf4 = new WriteFile(dirname4 + "/" + Format.get0X(topic.getNo()), false);
		
		for (int i = 0; i < fragementGroup.size(); i++) {
			wf1.writeLine(fragementGroup.get(i).getTitle(), "\n");
			wf2.write(fragementGroup.get(i).getBold2String());
			wf3.write(fragementGroup.get(i).getPlain2String());
			wf4.writeLine(fragementGroup.get(i).getPlain(), "\n");
		}
		
		wf4.close();
		wf3.close();
		wf2.close();
		wf1.close();
//		gbkPrintFragement(fragementGroup);
//		asciiPrintFragement(fragementGroup);
	}
	
	private void gbkPrintFragement(FragementGroup fragementGroup) throws IOException {
		String dirname1 = "data/gbk/fragement/title/" + topic.getSource();
		String dirname2 = "data/gbk/fragement/bold/" + topic.getSource();
		String dirname3 = "data/gbk/fragement/plain/" + topic.getSource();
		String dirname4 = "data/gbk/fragement/plainString/" + topic.getSource();
		CreateDir.createDir(dirname1);
		CreateDir.createDir(dirname2);
		CreateDir.createDir(dirname3);
		CreateDir.createDir(dirname4);
		WriteFile wf1 = new WriteFile(dirname1 + "/" + Format.get0X(topic.getNo()), false, Constants.GBK);
		WriteFile wf2 = new WriteFile(dirname2 + "/" + Format.get0X(topic.getNo()), false, Constants.GBK);
		WriteFile wf3 = new WriteFile(dirname3 + "/" + Format.get0X(topic.getNo()), false, Constants.GBK);
		WriteFile wf4 = new WriteFile(dirname4 + "/" + Format.get0X(topic.getNo()), false, Constants.GBK);
		
		for (int i = 0; i < fragementGroup.size(); i++) {
//			System.out.println(i);
			wf1.writeLine(fragementGroup.get(i).getTitle().replaceAll("\u00A0", "\u0020"), "\n");
			wf2.write(fragementGroup.get(i).getBold2String().replaceAll("\u00A0", "\u0020"));
			wf3.write(fragementGroup.get(i).getPlain2String().replaceAll("\u00A0", "\u0020"));	
			Fragement fragement = fragementGroup.get(i);
			String plain = fragement.getPlain();
//			System.out.println(plain);
			plain = plain.replaceAll("\u00A0", "\u0020");
			wf4.writeLine(plain, "\n");
		}
		
		wf4.close();
		wf3.close();
		wf2.close();
		wf1.close();
	}
	
	private void asciiPrintFragement(FragementGroup fragementGroup) throws IOException {
		String dirname1 = "data/ascii/fragement/title/" + topic.getSource();
		String dirname2 = "data/ascii/fragement/bold/" + topic.getSource();
		String dirname3 = "data/ascii/fragement/plain/" + topic.getSource();
		String dirname4 = "data/ascii/fragement/plainString/" + topic.getSource();
		CreateDir.createDir(dirname1);
		CreateDir.createDir(dirname2);
		CreateDir.createDir(dirname3);
		CreateDir.createDir(dirname4);
		WriteFile wf1 = new WriteFile(dirname1 + "/" + Format.get0X(topic.getNo()), false, Constants.UTF8);
		WriteFile wf2 = new WriteFile(dirname2 + "/" + Format.get0X(topic.getNo()), false, Constants.UTF8);
		WriteFile wf3 = new WriteFile(dirname3 + "/" + Format.get0X(topic.getNo()), false, Constants.UTF8);
		WriteFile wf4 = new WriteFile(dirname4 + "/" + Format.get0X(topic.getNo()), false, Constants.UTF8);
		
		for (int i = 0; i < fragementGroup.size(); i++) {
			System.out.println("   ??   ??  ?? "+i);
			wf1.writeLine(utf82Ascii(fragementGroup.get(i).getTitle()));
			wf2.write(utf82Ascii(fragementGroup.get(i).getBold2String()));
			wf3.write(utf82Ascii(fragementGroup.get(i).getPlain2String()));	
			Fragement fragement = fragementGroup.get(i);
			wf4.writeLine(utf82Ascii(fragementGroup.get(i).getPlain()), "\n");
		}
		
		wf4.close();
		wf3.close();
		wf2.close();
		wf1.close();
	}
	
	public static String utf82Ascii(String in) {
//		System.out.println(in.length());
		String out = "";
		
		char[] c = in.toCharArray();
//		System.out.println(c.length);
		
		for (int i = 0; i < c.length; ++i) {
//			System.out.print(i +c[i]);
//			System.out.println((int)c[i] + " " + c[i]);
			if (c[i] > 127 || c[i] <0) {
				c[i] = ' ';
				if (i > 0 && c[i-1] == ' ') {
					
				} else {
					out += ' ';
				}
			} else
			{
				out += c[i];
			}
		}
		
		return out.trim();
	}
	
	public static void main(String[] args) throws IOException {
//		System.out.println(utf82Ascii(" Bloodthirsty Liberal 绂�the Obama Family Tree;59;44.729213817395475;RunTag"));
	
		ReadFile rf = new ReadFile("gbk2.txt", "gbk");
		String s = rf.readLine();
		WriteFile wf = new WriteFile("gbk22utf8.txt", false, "utf8");
		wf.write(s);
		
		wf.close();
		rf.close();
		
	
	}
}
