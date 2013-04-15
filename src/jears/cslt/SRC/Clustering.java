package jears.cslt.SRC;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import jears.cslt.util.fileOperation.CreateDir;
import jears.cslt.util.fileOperation.ReadFile;
import jears.cslt.util.fileOperation.WriteFile;
import jears.cslt.xmlparser.GoldClustersWriter;

import org.kde9.main.ResultTreeRelationCalculator;
import org.kde9.model.Tree;

import ICTCLAS.I3S.AC.IctWordSeg;

public class Clustering {
	ResultTreeRelationCalculator calculator;
	Map<String, Integer> subtopics;
	Map<String, Tree> clusters;
	Map<String, String> results;
	private IctWordSeg wordSeg;
	private double thredshold = 0.1;

	public Clustering(String wordFile, String weightFile) {
		this.wordSeg = new IctWordSeg("GB2312");
		wordSeg.importUserDict("./ICTCLAS/userdict.txt");
		calculator = new ResultTreeRelationCalculator(wordFile, weightFile,
				"gbk");
		calculator.setParameters(0.01, 0.01, 0.01, 0.01, 0.01);

		subtopics = new TreeMap<String, Integer>();
		Map<Tree, Double> temp = calculator
				.calculate("this is to get the subtrees");
		Integer id = 1;
		for (Tree t : temp.keySet()) {
			subtopics.put(t.getRoot().getWord(), id);
			id++;
		}

		clusters = new TreeMap<String, Tree>();
		results = new TreeMap<String, String>();
	}

	public void tuning(double delta1, double delta2, double theta,
			double sigma1, double sigma2) {
		calculator.setParameters(delta1, delta2, theta, sigma1, sigma2);
	}

	public void clusterQuery(String dir, String query) throws IOException {
		String newDir = dir.replace("SnippetForCarrot", "Cluster/WSI");
		CreateDir.createDir(newDir);
		WriteFile wf = new WriteFile(newDir + query + ".txt", false, "gbk");

		File qDir = new File(dir + query);
		String[] files = qDir.list();
		for (String f : files) {
			ReadFile rf = new ReadFile(dir + query + "/" + f, "gbk");

			String title = rf.readLine();
			title = wordSeg.segSentence(title, 0);
			rf.readLine();
			String snippet = rf.readLine();
			snippet = wordSeg.segSentence(snippet, 0);

			if (getSubtopicNo(title + " " + snippet) != null) {
				clusters.put(f.substring(0, f.indexOf(".")),
						getSubtopicNo(title + " " + snippet));
				results.put(f.substring(0, f.indexOf(".")), title + " "
						+ snippet);
			}

			rf.close();
		}

		for (String key : clusters.keySet()) {
			wf.writeLine(subtopics.get(clusters.get(key).getRoot().getWord())
					+ "\t" + key);
			// System.out.println(key + "\t"
			// + subtopics.get(clusters.get(key).getRoot().getWord())
			// + "\t" + clusters.get(key));
			// System.out.println(key + "\t" + results.get(key));
		}

		wf.close();
	}

	public Tree getSubtopicNo(String searchresult) {
		Map<Tree, Double> result = calculator.calculate(searchresult);

		ArrayList<Clustering.Pair> list = new ArrayList<Clustering.Pair>();

		for (Tree t : result.keySet()) {
			// System.out.println(result.get(t) + "\t" + t);
			list.add(new Pair(t, result.get(t)));
		}

		Collections.sort(list);
		//
		// for (int i = 0 ; i < list.size(); i++) {
		// System.out.println(list.get(i).score + "\t" + list.get(i).t);
		// }
		if (list.get(0).score > thredshold)
			return list.get(0).t;
		else
			return null;
	}

	private class Pair implements Comparable<Pair> {
		Tree t;
		double score;

		Pair(Tree t, double score) {
			this.t = t;
			this.score = score;
		}

		@Override
		public int compareTo(Pair o) {
			// TODO Auto-generated method stub
			if (this.score < o.score)
				return 1;
			else
				return -1;
		}
	}

	public static void main(String[] args) throws IOException {
		String wordFile = "./data/Words/蠕虫.txt.count1";
		String weightFile = "./data/Words/蠕虫.txt.count2";
		Clustering test = new Clustering(wordFile, weightFile);
		test.clusterQuery("./data/SnippetForCarrot/", "蠕虫");
		GoldClustersWriter test2 = new GoldClustersWriter(
				"./data/Cluster/WSI/蠕虫.txt");
		test2.writeXml("蠕虫", "./data/SnippetForCarrot/蠕虫/",
				"./data/Cluster/WSI/" + "蠕虫" + ".xml");
	}

}