package jears.cslt.subtopic.printer;

import java.io.IOException;

import jears.cslt.subtopic.model.FragementGroup;
import jears.cslt.subtopic.model.SearchResultGroup;


public interface PrinterInterface {

	//save html pages linked to the urls of search results
	public abstract void printHtml(String htmlString, int resultNo)
			throws IOException;

	public abstract void printSERP(String SERPString, int pageNo)
			throws IOException;

	public abstract void printTitleAndUrl(SearchResultGroup searchResultGroup)
			throws IOException;
	
	public abstract void printSnippet(SearchResultGroup searchResultGroup)
			throws IOException;

	public abstract void printFragement(FragementGroup fragementGroup)
			throws IOException;

}