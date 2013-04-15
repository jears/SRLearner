package jears.cslt.jAPCluster;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cslt.subtopic.apcluster.Cluster;
import jears.cslt.util.fileOperation.*;

public class JAPClusterResult {
	private int clusterNum;
	private Map<String, Cluster> resultMap;
	private List<Cluster> result;

	public JAPClusterResult(String exmfile, String objfile, String resfile, String rankfile)
			throws IOException {
		resultMap = new HashMap<String, Cluster>();
		result = new ArrayList<Cluster>();
		getClusters(exmfile, objfile);
		System.out.println(result.size());
		print(resfile);
//		rank(rankfile);
	}

	private void getClusters(String exmfile, String objfile) throws IOException {
		Map<Integer, Integer> clusterres = getClusterRes(exmfile);
		;
		Map<Integer, String> objectno = getObj(objfile);

		for (Integer i : clusterres.keySet()) {
			String member = objectno.get(i);
			if (member == null || member.length() < 5)
				continue;
			String exemplar = objectno.get(clusterres.get(i));
			if (resultMap.containsKey(exemplar)) {
				resultMap.get(exemplar).addMember(member);
			} else {
				Cluster c = new Cluster(exemplar, member);
				resultMap.put(exemplar, c);
			}
		}

		for (String exm : resultMap.keySet()) {
			Cluster c = resultMap.get(exm);
			result.add(c);
		}
		clusterNum = resultMap.size();
	}

	private static Map<Integer, Integer> getClusterRes(String exmfile)
			throws IOException {
		ReadFile exm = new ReadFile(exmfile, "gbk");
		Map<Integer, Integer> clusterres = new HashMap<Integer, Integer>();
		String line = "";
		int i = 0;
		while ((line = exm.readLine()) != null) {
			if (line.length() == 0)
				break;
			i++;
			clusterres.put(i, Integer.parseInt(line));
		}

		exm.close();

		return clusterres;
	}

	private static Map<Integer, String> getObj(String objfile)
			throws IOException {
		ReadFile obj = new ReadFile(objfile, "GB2312");
		Map<Integer, String> objno = new HashMap<Integer, String>();
		String line = "";
		int i = 0;
		while ((line = obj.readLine()) != null) {
			if (line.length() == 0)
				break;
			// TODO:parse input file to get obj&no
			i++;
			objno.put(i, line);
		}

		obj.close();

		return objno;
	}

	private void print(String resfile) throws IOException {
		WriteFile res = new WriteFile(resfile, false, "GB2312");
		res.write(toString());
		res.close();
	}

	public String toString() {
		String res = "";
		res += resultMap.keySet().size() + "\n";
		for (Cluster c : result) {
			res += c;
		}

		return res;
	}

	private void sortClusters() {
		Collections.sort(result);
	}

	private int resultSize() {
		int ret = 0;
		for (Cluster c : result) {
			ret += c.getMemberSize();
		}

		return ret;
	}
	
	public void rank(String filename) throws IOException {
		WriteFile wf = new WriteFile(filename, false);
		
		sortClusters();
		if (clusterNum >= 50) {
			int count = 0;
			for (Cluster c : result) {
				wf.writeLine(c.getExmplar());
				count++;
				if (count >= 50)
					break;
			}
		} else {
			for (Cluster c : result) {
				wf.writeLine(c.getExmplar());
				c.sort();
			}

			int index = 0;
			int bound = 50;
			int totalCandidate = resultSize();
			if (bound > totalCandidate)
				bound = totalCandidate;
			bound = bound  - clusterNum;
			while (true) {
				if (bound <= 0)
					break;
				String next = result.get(index).next();
				if (next != null) {
					wf.writeLine(next);
					bound--;
				}
				index = (index + 1) % clusterNum;
			}
		}
		
		wf.close();
	}

	public static void main(String[] args) throws IOException {
		CreateDir.createDir("./sap/");
		CreateDir.createDir("./qsap/");
		CreateDir.createDir("./resap/");
		CreateDir.createDir("./rankap/");
		JAPClusterResult test = new JAPClusterResult("./sap/46.exm", "./qsap/46",
				"resap/46", "./rankap/46");
		System.out.println(test.clusterNum);
	}
}
