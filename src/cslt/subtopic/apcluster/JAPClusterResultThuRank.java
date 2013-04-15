package cslt.subtopic.apcluster;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import jears.cslt.util.fileOperation.*;
public class JAPClusterResultThuRank {
	private int clusterNum;
	private Map<String, NoExmCluster> resultMap;
	private List<NoExmCluster> result;

	public JAPClusterResultThuRank(String exmfile, String objfile,
			String resfile, String rankfile) throws IOException {
		resultMap = new HashMap<String, NoExmCluster>();
		result = new ArrayList<NoExmCluster>();
		getClusters(exmfile, objfile);
		print(resfile);
		rank(rankfile);
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
				NoExmCluster c = new NoExmCluster(exemplar, member);
				resultMap.put(exemplar, c);
			}
		}

		for (String exm : resultMap.keySet()) {
			NoExmCluster c = resultMap.get(exm);
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
		ReadFile obj = new ReadFile(objfile, "utf-8");
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
		WriteFile res = new WriteFile(resfile, false);
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
		for (Cluster c : result) {
			c.sort();
		}

		int index = 0;
		int bound = 50;
		int totalCandidate = resultSize();
		if (bound > totalCandidate)
			bound = totalCandidate;
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

		wf.close();
	}

	public static void main(String[] args) throws IOException {
		CreateDir.createDir("./sap/");
		CreateDir.createDir("./qsap/");
		CreateDir.createDir("./resap/");
		CreateDir.createDir("./rankap/");
		JAPClusterResultThuRank test = new JAPClusterResultThuRank("./sap/46.exm",
				"./qsap/46", "resap/46", "./rankap/46");
		System.out.println(test.clusterNum);
	}
}
