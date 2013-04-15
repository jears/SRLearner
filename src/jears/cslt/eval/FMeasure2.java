package jears.cslt.eval;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.swing.text.Document;

public class FMeasure2 {
	private Map<Integer, Set<Integer>> map;
	private double precision;
	private double recall;
	private double fmeasure;
	private String query;

	public FMeasure2() {
		map = new TreeMap<Integer, Set<Integer>>();
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
			System.err.println("[precision: denominator = 0]");
			return -1;
		}
	}
	
	private double getP(Clusters gold, Cluster test, int index) {
		TreeSet<Integer> set = new TreeSet<Integer>();
		ArrayList<Cluster> g = gold.getCluster();
		int maxInterSize = 0;
		int maxInterIndex = -1;
		for (int i = 0; i < g.size(); i++) {
			int temp = getInter(g.get(i), test);
			if (temp == 0)
				continue;
			if (temp == maxInterSize) {
				set.add(i);
			}
			if (temp > maxInterSize) {
				set.clear();
				maxInterSize = temp;
				maxInterIndex = i;
				set.add(i);
			}
		}
		map.put(index, set);

		int testSize = test.getDocuments().size();


		return (double) maxInterSize / testSize;
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
			return -1;
		}
	}

	

	private double getR(Cluster gold, Clusters test, int index) {
		double ret = 0;
		ArrayList<String> union = new ArrayList<String>();
		ArrayList<Cluster> t = test.getCluster();

		for (int i = 0; i < t.size(); i++) {
			if (map.get(i).contains(index)) {
				Cluster c = t.get(i);
				ArrayList<String> gDocument = new ArrayList<String>();
				gDocument.addAll(gold.documents);
				 
				gDocument.retainAll(c.documents);
				union.removeAll(gDocument);
				union.addAll(gDocument);
			}
		}

		double r = (double) union.size() / gold.documents.size();

		return r;
	}

	private int getInter(Cluster gold, Cluster test) {
		ArrayList<String> t = test.getDocuments();

		ArrayList<String> g = new ArrayList<String>();
		g.addAll(gold.getDocuments());
		g.retainAll(t);
		int interSize = g.size();

		return interSize;
	}

	public static void main(String[] args) throws IOException {
		Clusters test = new Clusters("./data/Cluster/¹¦·ò.xml");
		Clusters gold = new Clusters("./data/Cluster/±±¶·.xml");
		(new FMeasure2()).getFMeasure(gold, test);
	}
}
