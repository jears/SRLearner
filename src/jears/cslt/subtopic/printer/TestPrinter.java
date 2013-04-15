package jears.cslt.subtopic.printer;

import java.io.IOException;

import jears.cslt.subtopic.model.FragementGroup;
import jears.cslt.subtopic.model.SearchResultGroup;
import jears.cslt.util.fileOperation.CreateDir;
import jears.cslt.util.fileOperation.WriteFile;


public class TestPrinter{

	public void printHtml(String htmlString, int resultNo) throws IOException {
		// TODO Auto-generated method stub
		
	}

	public void printSERP(String SERPString, int pageNo, String query) throws IOException {
		// TODO Auto-generated method stub
		CreateDir.createDirForFile("./data/SERP/" + query + "/" + pageNo);
		WriteFile wf = new WriteFile("./data/SERP/" + query + "/" + pageNo, false, "GBK");
		
		wf.write(SERPString);
		wf.close();
	}

	public void printTitleAndUrl(SearchResultGroup searchResultGroup)
			throws IOException {
		// TODO Auto-generated method stub
		
	}

	public void printFragement(FragementGroup fragementGroup)
			throws IOException {
		// TODO Auto-generated method stub
		
	}

	public void printSnippet(SearchResultGroup searchResultGroup, String query)
			throws IOException {
		// TODO Auto-generated method stub
		CreateDir.createDir("./data/Snippet/" + query);
		CreateDir.createDirForFile("./data/Title/" + query + ".txt");
		CreateDir.createDir ("./data/SnippetForCarrot/" + query);
		for (int i = 0; i < searchResultGroup.size(); i++) {
			WriteFile wf = new WriteFile("./data/Snippet/" + query + "/" + i + ".txt", false, "GBK");
			wf.writeLine(searchResultGroup.get(i).getTitle());
			wf.writeLine(searchResultGroup.get(i).getSnippet());
			wf.close();
			
			wf = new WriteFile("./data/SnippetForCarrot/" + query + "/" + i + ".txt", false, "GBK");
			wf.writeLine(searchResultGroup.get(i).getTitle());
			wf.writeLine(searchResultGroup.get(i).getUrl());
			wf.writeLine(searchResultGroup.get(i).getSnippet());
			wf.close();
			
			wf = new WriteFile("./data/Title/" + query + ".txt", true, "GBK");
			wf.writeLine(searchResultGroup.get(i).getTitle());
			wf.close();
		}
	}
}
