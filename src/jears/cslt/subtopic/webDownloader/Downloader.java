package jears.cslt.subtopic.webDownloader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import jears.cslt.subtopic.util.Constants;
import jears.cslt.util.fileOperation.WriteFile;

/**
 * connect to and download the web page by url
 * @author jj
 *
 */
public class Downloader {
	/**
	 * connect to the web page by url
	 * save the content to a string and return it
	 * only accept media type "text/html"
	 * @param urlStr	the url linked to the web page
	 * @param encode	the encode used by the web page
	 * @return	a string containing the content of the web page
	 * @throws IOException
	 * @throws NotHtmlException 
	 */
	public String getUrlContent(String urlStr, String encode)
			throws NotHtmlException, IOException {
		setProxy("127.0.0.1", "8087");
		URL url = new URL(urlStr);
		URLConnection connection = url.openConnection();
		
		connection.setRequestProperty("User-agent","Mozilla/5.0");

		//actually connect to the url
		InputStream is = connection.getInputStream();
		
		
//		System.out.println(connection.getContentType());
		
		String type = connection.getContentType();
		if (type != null && (type.toLowerCase().indexOf("html") != -1 || type.toLowerCase().indexOf("json") != -1
				|| type.toLowerCase().indexOf("text") != -1 || type.toLowerCase().indexOf("javascript") != -1)) {
			BufferedReader br = new BufferedReader(new InputStreamReader(
					is, encode));
			
			StringBuilder builder = new StringBuilder();
			String s = "";
			while ((s = br.readLine()) != null) {
				builder.append(s);
			}
			br.close();
			is.close();
			return builder.toString();
		} else 
			throw new NotHtmlException();
	}
	
	/**
	 * to cheat google
	 * @param urlStr
	 * @param encode
	 * @param user
	 * @return
	 * @throws IOException
	 * @throws NotHtmlException 
	 */
	public String getUrlContent(String urlStr, String encode, String user)
			throws IOException, NotHtmlException {
		setProxy("127.0.0.1", "8087");
		URL url = new URL(urlStr);
		URLConnection connection = url.openConnection();
		connection.setRequestProperty("User-agent",user);
		//actually connect to the url
		InputStream is = connection.getInputStream();
		
		String type = connection.getContentType().toLowerCase();
		if (type.indexOf("html") != -1 || type.indexOf("json") != -1) {
			BufferedReader br = new BufferedReader(new InputStreamReader(
					is, encode));
			
			StringBuilder builder = new StringBuilder();
			String s = "";
			while ((s = br.readLine()) != null) {
				builder.append(s);
			}
			br.close();
			is.close();
			return builder.toString();
		} else 
			throw new NotHtmlException();
	}
	
	public String getUrlContent(String urlStr, String encode, boolean google)
			throws NotHtmlException, IOException {
		setProxy("127.0.0.1", "8087");
		URL url = new URL(urlStr);
		URLConnection connection = url.openConnection();
		
		connection.setRequestProperty("User-agent","Mozilla/5.0");
		String myCookie = "NID=61=wAm2FtbJ_LKqoCRWrYKd3N1vzWb3-8ZrvtZrqWZ3PLRpjgk92BZ6fL1XFL_IDhs5j2N0lHF22q4gY8PXwT5yaBsJ8_aJfkQMBPiYNckGuXJBOrLxO2-kQnRf3f5W5Qfu; PREF=ID=04c90e85276b5378:U=2b23e2e990896069:FF=0:LD=en:NW=1:CR=2:TM=1341418863:LM=1341418868:S=Ai0IWKrqpcjANcHw";
		connection.setRequestProperty("Cookie", myCookie);
		connection.setConnectTimeout(1000*60);
		connection.setReadTimeout(1000*60);

		//actually connect to the url
		InputStream is = connection.getInputStream();
		
		
//		System.out.println(connection.getContentType());
		
		String type = connection.getContentType();
		if (type != null && (type.toLowerCase().indexOf("html") != -1 || type.toLowerCase().indexOf("json") != -1)) {
			BufferedReader br = new BufferedReader(new InputStreamReader(
					is, encode));
			
			StringBuilder builder = new StringBuilder();
			String s = "";
			while ((s = br.readLine()) != null) {
				builder.append(s);
			}
			br.close();
			is.close();
			return builder.toString();
		} else 
			throw new NotHtmlException();
	}
	
	
	private static void setProxy(String host, String port) {
		System.setProperty("proxySet", "true");
		System.setProperty("proxyHost", host);
		System.setProperty("proxyPort", port);
	}
	
	public static void main(String[] args) throws IOException, NotHtmlException {
		String keyword = "apple";
		keyword = URLEncoder.encode(keyword, Constants.UTF8);
		String googleUrlEn = "http://www.google.com/search?hl=en&q="
				+ keyword + "&start=0&pws=0&lr=lang_en";

		Downloader downloader = new Downloader();
		System.out.println(googleUrlEn);

		String url = "http://www.computerhope.com/shortcut.htm";
		System.out.println(downloader.getUrlContent(url, "utf-8", true));
		
		WriteFile wf = new WriteFile("googlehk.html", false);
		wf.write(downloader.getUrlContent(url, "utf-8"));
		wf.close();
//		downloader.getUrlContent(baiduUrl, Constants.GBK);
//		downloader.getUrlContent("http://www.cros-portal.eu/sites/default/files/S1P2.pdf", Constants.UTF8);
//		System.out.println(downloader.getUrlContent("http://www.cros-portal.eu/sites/default/files/S1P2.pdf", Constants.UTF8));
//		int i = 0;
//		while(i < 10) {
//			System.out.println(downloader.getUrlContent(googleUrlEn, "utf-8", "Chrome"));
//			i++;
//		}
//		
		System.out.print("*");
	}
}
