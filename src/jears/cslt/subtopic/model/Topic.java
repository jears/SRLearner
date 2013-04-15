package jears.cslt.subtopic.model;

/**
 * topic class
 * @author jj
 *
 */

public class Topic {
	private int no;
	private String query;
	private String source;
	
	public Topic(int n, String q) {	
		no = n;
		query = q;
	}
	
	public Topic(int n, String q, String source) {
		this.no = n;
		this.query = q;
		this.source = source;
	}
	
	public void setSource(String source) {
		this.source = source;
	}
	
	public String getQuery() {
		return query;
	}

	public int getNo() {
		return no;
	}
	
	public String getSource() {
		return source;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return query + "\t" + source + "\t" + no;
	}
}
