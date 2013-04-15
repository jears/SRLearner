package jears.cslt.subtopic.searchResultsExtractor;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import jears.cslt.subtopic.model.SearchResult;
import jears.cslt.subtopic.model.SearchResultGroup;
import jears.cslt.subtopic.printer.TestPrinter;
import jears.cslt.subtopic.util.Constants;
import jears.cslt.subtopic.util.RandomGenerator;
import jears.cslt.subtopic.util.StringHelper;
import jears.cslt.subtopic.webDownloader.NotHtmlException;
import jears.cslt.util.fileOperation.ReadFile;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.Tag;
import org.htmlparser.filters.AndFilter;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.HasChildFilter;
import org.htmlparser.filters.HasParentFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

public class GoogleSearchResultDownloader extends BasicSearchResultsExtractor {
	private TestPrinter printer;

	private static final String googleUrlEn = "http://www.google.com.hk/search?hl=zh-CN&q="
			+ Constants.PLACEHOLDER1
			+ "&start="
			+ Constants.PLACEHOLDER2
			+ "&pws=0&lr=lang_zh-CN";

	private int total = 10;

	public GoogleSearchResultDownloader(TestPrinter printer) {
		this.printer = printer;
	}

	public GoogleSearchResultDownloader(int total, TestPrinter printer) {
		this.total = total;
		this.printer = printer;
	}

	private String getGoogleUrlEn(String keyword, int start)
			throws UnsupportedEncodingException {
		return googleUrlEn.replaceAll(Constants.PLACEHOLDER1,
				URLEncoder.encode(keyword, Constants.UTF8)).replaceAll(
				Constants.PLACEHOLDER2, String.valueOf(start));
	}

	private String getHtml(String url) throws IOException {
		while (true) {
			try {
				Thread.sleep(1000 * RandomGenerator.getNextRandomNum());
				String htmlString = downloader.getUrlContent(url,
						Constants.UTF8, true);
				return htmlString;
			} catch (java.net.SocketException e) {
				System.out.print(".");
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NotHtmlException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				try {
					Thread.sleep(1000 * 60 * 5 * RandomGenerator.getNextRandomNum());
				} catch (InterruptedException e1) {
				}
			}
		}
	}

	@Override
	public SearchResultGroup extractSearchResults(String keyword) {
		SearchResultGroup ret = new SearchResultGroup(keyword);
		for (int i = 0; i < total; ++i) {
			try {
				String urlen = getGoogleUrlEn(keyword, i * 10);
				System.out.println("SERP: " + i + " \t" + urlen);
				String htmlEn = getHtml(urlen);
				printer.printSERP(htmlEn, i, keyword);
				List<SearchResult> temp = extractSearchResultTitle(htmlEn);
				ret.addAllResult(temp);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ParserException e) {
				e.printStackTrace();
			}
		}

		try {
			printer.printSnippet(ret, keyword);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}
	
	public SearchResultGroup extractSearchResultsFromFile(String dirname) {
		int index = dirname.lastIndexOf('/');
		if (index < 0)
			index = 0;
		if(dirname.lastIndexOf('\\') > index)
			index = dirname.lastIndexOf('\\');
		String keyword = dirname.substring(index);
		SearchResultGroup ret = new SearchResultGroup(keyword);
		for (int i = 0; i < 100; ++i) {
			try {
				ReadFile rf = new ReadFile(dirname + "/" + i + ".txt");
				String htmlEn = rf.readLine();
				printer.printSERP(htmlEn, i, keyword);
				List<SearchResult> temp = extractSearchResultTitle(htmlEn);
				ret.addAllResult(temp);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ParserException e) {
				e.printStackTrace();
			}
		}

		try {
			printer.printSnippet(ret, keyword);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}

	private List<SearchResult> extractSearchResultTitle(String html)
			throws ParserException {
		Parser parser = new Parser(html);
		NodeFilter filter = getSearchResultTitleFilter();
		NodeList list = parser.extractAllNodesThatMatch(filter);

		Parser fParser = new Parser(html);
		NodeFilter fFilter = getTitleFatherFilter();
		NodeList fList = fParser.extractAllNodesThatMatch(fFilter);

		List<SearchResult> ret = new ArrayList<SearchResult>();
		if (list.size() != fList.size())
			return ret;

		for (int i = 0; i < list.size(); ++i) {
			Node currentNode = list.elementAt(i);
			String title = StringHelper.unescapeHTML(getNodeValue(currentNode));
			NodeList tagANode = currentNode.getChildren();
			tagANode.keepAllNodesThatMatch(new TagNameFilter("a"));
			if (tagANode.size() > 0) {
				currentNode = tagANode.elementAt(0);
				String url = StringHelper.unescapeHTML(((Tag) currentNode)
						.getAttribute("href"));

				if (!url.startsWith("http://"))
					url = "http://www.google.com" + url;

				String snippet = "";
				Node fNode = fList.elementAt(i);
				NodeList sNodeList = new NodeList();
				fNode.collectInto(sNodeList, getSnippetFilter());
				for (int j = 0; j < sNodeList.size(); j++) {
					Node sNode = sNodeList.elementAt(j);
					snippet += StringHelper.unescapeHTML(getNodeValue(sNode));
				}
				snippet = snippet.replaceAll(
						"^[0-9]{1,2}\\s.{3,20}\\s\\.\\.\\.\\s", "");
				ret.add(new SearchResult(title, snippet, url));
			}
		}
		return ret;
	}

	private NodeFilter getSearchResultTitleFilter() {
		NodeFilter pFilter = new HasParentFilter(new HasAttributeFilter("id",
				"ires"), true);
		NodeFilter liFilter = new TagNameFilter("li");
		NodeFilter fFilter = new AndFilter(pFilter, liFilter);

		NodeFilter cFilter = new HasChildFilter(new TagNameFilter("a"));
		NodeFilter fcFilter = new AndFilter(new HasParentFilter(fFilter),
				cFilter);

		NodeFilter filter = new TagNameFilter("h3");
		NodeFilter titleFilter = new AndFilter(fcFilter, filter);
		return titleFilter;
	}

	private NodeFilter getTitleFatherFilter() {
		NodeFilter filter = new HasChildFilter(getSearchResultTitleFilter());
		return filter;
	}

	private NodeFilter getSnippetFilter() {
		NodeFilter filter = new AndFilter(
				new HasAttributeFilter("class", "st"),
				new TagNameFilter("span"));
		return filter;
	}

	private String getNodeValue(Node node) {
		return node.toPlainTextString();
	}

	public static void main(String[] args) {
		GoogleSearchResultDownloader googleContent = new GoogleSearchResultDownloader(
				1, new TestPrinter());
		SearchResultGroup resultGroup = googleContent
				.extractSearchResults("美国大选");
		System.out.println();
		System.err.println(resultGroup);
	}
}
