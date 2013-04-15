package jears.cslt.subtopic.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.transform.Result;

import jears.cslt.subtopic.util.Constants;


public class RecommendationResult {
	private String query;
	private List<String> relatedSearches;

	public RecommendationResult(String query) {
		this.query = query;
		relatedSearches = new ArrayList<String>();
	}

	public void addRelatedSearch(String str) {
		relatedSearches.add(str);
	}

	public void addAllRelatedSearch(List<String> strs) {
		relatedSearches.addAll(strs);
	}

	public String getQuery() {
		return query;
	}

	public List<String> getRelatedSearches() {
		return Collections.unmodifiableList(relatedSearches);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
//		builder.append(query);
//		builder.append(Constants.NEW_LINE);
//		builder.append(relatedSearches.size());
//		builder.append(Constants.NEW_LINE);
		for (String related : relatedSearches) {
			builder.append(related);
			builder.append("\n");
		}
//		builder.append(Constants.NEW_LINE);
		return builder.toString();
	}
}
