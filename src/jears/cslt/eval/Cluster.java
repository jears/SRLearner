package jears.cslt.eval;

import java.util.ArrayList;

public class Cluster{	
	String id;
	String score;
	String size;
	
	ArrayList<String> title;
	ArrayList<String> documents;
	
	public Cluster(String id, String score, String size) {
		this.id = id;
		this.score = score;
		this.size = size;
		
		title = new ArrayList<String>();
		documents = new ArrayList<String>();
	}
	
	public void addTitle(String tid) {
		title.add(tid);
	}
	
	public void addDocument(String id) {
		documents.add(id);
	}
	
	public ArrayList<String> getDocuments() {
		return documents;
	}

	public String toString() {
		String ret = "";
		
		ret += id + "\t" + score + "\t" + size + "\n";
		
		for (int i = 0; i < title.size(); i++) {
			ret += title.get(i) + "\t*\t";
		}
		
		ret += "\n";
		
		for (int i = 0; i < documents.size(); i++) {
			ret += documents.get(i) + "\n";
		}
		
		return ret;
	}
}
