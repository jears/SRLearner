package jears.cslt.subtopic.searchResultsExtractor;

import java.io.IOException;

import jears.cslt.subtopic.model.SearchResultGroup;
import jears.cslt.subtopic.util.Constants;
import jears.cslt.subtopic.webDownloader.*;
import jears.cslt.util.fileOperation.WriteFile;

public abstract class BasicSearchResultsExtractor {
	protected static final Downloader downloader = new Downloader();
	
	/**
	 * save the search results to file
	 * @param filename
	 * @param content
	 */
	protected void save(String filename, String content) {
		try {
			WriteFile writeFile = new WriteFile(filename, false, Constants.UTF8);
			writeFile.write(content);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public abstract SearchResultGroup extractSearchResults(String keyword);
}
