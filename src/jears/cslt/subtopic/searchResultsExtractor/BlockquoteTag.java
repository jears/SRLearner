package jears.cslt.subtopic.searchResultsExtractor;

import org.htmlparser.tags.CompositeTag;

public class BlockquoteTag extends CompositeTag {
	@Override
	public String[] getIds() {
		return new String[] { "blockquote" };
	}
}
