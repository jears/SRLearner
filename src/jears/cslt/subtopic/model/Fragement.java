package jears.cslt.subtopic.model;

import java.util.ArrayList;
import java.util.List;

public class Fragement {
	private String title;
//	private String[] anchor;
	private List<String> bold;
	private List<String> plain;
	private String plainString;
	
	public Fragement() {
		title = "";
		bold = new ArrayList<String>();
		plain = new ArrayList<String>();
		plainString = "";
	}
	
	public Fragement(String title, List<String> bold, List<String> plain, String plainString) {
		this.title = title;
		this.bold = bold;
		this.plain = plain;
		this.plainString = plainString;
	}
	
	public String getTitle() {
		return title;
	}
	
	public String getBold2String() {
		String ret = "";
		for (int i = 0; i < bold.size(); i++)
			ret += bold.get(i) + "\n";
		
		return ret;
	}
	
	public String getPlain2String() {
		String ret = "";
		for (int i = 0; i < plain.size(); i++)
			ret += plain.get(i) + "\n";
		
		return ret;
	}
	
	public String getPlain() {
		return plainString;
	}
	
	public String toString() {
		String ret = "";
		
		return ret;
	}
}
