package jears.cslt.subtopic.model;

import jears.cslt.subtopic.util.Constants;

public class SearchResult {
	private String title;
	private String snippet;
	private String url;

	public SearchResult(String title, String snippet, String url) {
		this.title = title;
		this.snippet = snippet;
		this.url = url;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSnippet() {
		return snippet;
	}

	public void setSnippet(String snippet) {
		this.snippet = snippet;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public String toString() {
		return title + "\n" + snippet + "\n" + url + "\n";
	}
}
