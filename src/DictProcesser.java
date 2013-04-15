import java.io.IOException;
import java.security.spec.EncodedKeySpec;
import java.util.HashSet;
import java.util.Set;


import jears.cslt.subtopic.util.EncodeProcesser;
import jears.cslt.util.fileOperation.CreateDir;
import jears.cslt.util.fileOperation.ReadFile;
import jears.cslt.util.fileOperation.WriteFile;

/**
 * 删掉所有包含空格、英文字符、停用词的串
 * 日语部分手动删除了
 * @author jears
 *
 */

public class DictProcesser {
	private static Set<String> stopWords;
	static {
		stopWords = new HashSet<String>();
		ReadFile rf;
		try {
			rf = new ReadFile("./data/list/stopword_CH.txt", "GB2312");
			
			String line = "";
			while((line = rf.readLine()) != null) {
				stopWords.add(line);
			}
			
			rf.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static String removeStopWord(String sentence) {
		String[] words = sentence.split(" ");
		String ret = "";
		if (EncodeProcesser.hasAsicii(sentence) || words.length > 1)
			return ret;
		
		for (int i = 0; i < words.length; i++) {
			if (words[i].length() == 1 || words[i].length() > 4 || hasStopword(words[i]))
				continue;
			else
				ret += words[i] + " ";
		}
		
		return ret.trim();
	}
	
	private static boolean hasStopword(String word) {
		for (String s : stopWords) {
			if (word.indexOf(s) != -1)
				return true;
		}
		return false;
	}
	
	public static void removeStopWordFile(String inputFilename, String outputFilename) {
		CreateDir.createDirForFile(outputFilename);
		Set<String> words = new HashSet<String>();
		try {
			ReadFile rf = new ReadFile(inputFilename, "GB2312");
			WriteFile wf = new WriteFile(outputFilename, false, "GB2312");
			
			String line = "";
			while((line = rf.readLine()) != null) {
				String res = removeStopWord(line);
				if (res.length() > 1 && !words.contains(res)) {
					words.add(res);
					wf.writeLine(res);
				}
			}
			
			wf.close();
			rf.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		removeStopWordFile("data/list/dictChinese-remove.txt", "data/list/dictChinese-final.txt");
	}
}
