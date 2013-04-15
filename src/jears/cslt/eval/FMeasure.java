package jears.cslt.eval;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import jears.cslt.util.fileOperation.WriteFile;

public class FMeasure {
	private Map<Integer, Integer> map;
	private double precision;
	private double recall;
	private double fmeasure;
	private String query;

	public FMeasure() {
		map = new TreeMap<Integer, Integer>();
		precision = 0;
		recall = 0;
		fmeasure = 0;
		query = "";
	}
	
	public double getFMeasure(Clusters gold, Clusters test) {
		precision = getPrecision(gold, test);
//		System.out.println("*****************************************************");
//		System.out.println(map);
//		System.out.println("*****************************************************");
		recall = getRecall(gold, test);
		fmeasure = 2 * precision * recall / (precision + recall);

		System.out.println("FMeasure: " + fmeasure);

		return fmeasure;
	}

	public double getPrecision(Clusters gold, Clusters test) {
		query = gold.getQuery();
		ArrayList<Cluster> t = test.getCluster();
		double sum = 0;
		double denominator = 0;
		for (int i = 0; i < t.size(); i++) {
			sum += getP(gold, t.get(i), i) * t.get(i).documents.size();
			denominator += t.get(i).documents.size();
		}

		if (denominator > 0) {
			System.out.println("Precision: " + sum / denominator);
			return sum / denominator;
		} else {
			System.out.println("[precision: denominator = 0] !!!!!");
			return -1;
		}
	}

	public double getRecall(Clusters gold, Clusters test) {
		ArrayList<Cluster> g = gold.getCluster();
		double sum = 0;
		double denominator = 0;
		for (int i = 0; i < g.size(); i++) {
			sum += getR(g.get(i), test, i) * g.get(i).documents.size();
			denominator += g.get(i).documents.size();
		}

		if (denominator > 0) {
			System.out.println("Recall: " + sum / denominator);
			return sum / denominator;
		} else {
			System.out.println("[Recall: denominator = 0] !!!!!");
			return -1;
		}
	}

	private double getP(Clusters gold, Cluster test, int index) {
		ArrayList<Cluster> g = gold.getCluster();
		int maxInterSize = 0;
		int maxInterIndex = -1;
		for (int i = 0; i < g.size(); i++) {
			int temp = getInter(g.get(i), test);
			if (temp > maxInterSize) {
				maxInterSize = temp;
				maxInterIndex = i;
			}
		}

		map.put(index, maxInterIndex);

		int testSize = test.getDocuments().size();

//		System.out.println("p: " + (double) maxInterSize / testSize);

		return (double) maxInterSize / testSize;
	}

	private double getR(Cluster gold, Clusters test, int index) {
		double ret = 0;
		ArrayList<String> union = new ArrayList<String>();
		ArrayList<Cluster> t = test.getCluster();

		for (int i = 0; i < t.size(); i++) {
			if (map.get(i) == index) {
				Cluster c = t.get(i);
				ArrayList<String> gDocument = new ArrayList<String>();
				gDocument.addAll(gold.documents);
//				System.out.println("           g: " + gDocument);
//				System.out.println("           t: " + c.documents);
				 
				gDocument.retainAll(c.documents);
//				System.out.println("   i: " + gDocument);
//				System.out.println("   union1: " + union);
				union.removeAll(gDocument);
				union.addAll(gDocument);
//				System.out.println("   union2: " + union);
			}
		}

		double r = (double) union.size() / gold.documents.size();
//		System.out.println(index + "r: " + r);

		return r;
	}

	private int getInter(Cluster gold, Cluster test) {
		ArrayList<String> t = test.getDocuments();

		ArrayList<String> g = new ArrayList<String>();
		g.addAll(gold.getDocuments());
//		 System.out.println("           g: " + g);
//		 System.out.println("           t: " + t);
		g.retainAll(t);
//		 System.out.println("           i: " + g);
		// System.out.println("goldAfterRetain: " + gold.getDocuments());
		int interSize = g.size();

//		System.out.println("inter: " + interSize);
		return interSize;
	}
	
	public String toString() {
		return query + "\t" + precision + "\t" + recall + "\t" + fmeasure;
	}

	public static void main(String[] args) throws IOException {
//		WriteFile lingowf = new WriteFile("./data/ambient/Lingo.txt", false, "gbk");
//		WriteFile stcwf = new WriteFile("./data/ambient/stc.txt", false, "gbk");
//		
//		ReadFile rf = new ReadFile("./data/ambient/no-topics.txt", "GBK");
//		String line = "";
//		while((line = rf.readLine()) != null) {
//			System.out.println(line);
//			String[] pair = line.split("\t");
//			Clusters gold = new Clusters("./data/ambient/" +pair[0] + ".xml");
//			Clusters test = new Clusters("./data/ambient/Lingo/" +pair[1].toLowerCase().replace(" ", "-") + ".xml");
//			FMeasure f = new FMeasure();
//			f.getFMeasure(gold, test);
//			lingowf.writeLine(f.toString());
//			test = new Clusters("./data/ambient/stc/" +pair[1].toLowerCase().replace(" ", "-") + ".xml");
//			f = new FMeasure();
//			f.getFMeasure(gold, test);
//			stcwf.writeLine(f.toString());
//		}
//		rf.close();
//		
//		stcwf.close();
//		lingowf.close();
		WriteFile stcwf = new WriteFile("./data/Cluster/wsi.txt", false, "gbk");
		
			Clusters gold = new Clusters("./data/annotationXMl/" + "ָה³ז" + ".xml");
			Clusters test = new Clusters("./data/Cluster/WSI/" + "ָה³ז" + ".xml");
			FMeasure f = new FMeasure();
			f.getFMeasure(gold, test);
			stcwf.writeLine(f.toString());
		
		stcwf.close();
	}
}
