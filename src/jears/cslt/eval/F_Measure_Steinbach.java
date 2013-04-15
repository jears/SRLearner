package jears.cslt.eval;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import jears.cslt.util.fileOperation.ReadFile;
import jears.cslt.util.fileOperation.WriteFile;

public class F_Measure_Steinbach {
		private Map<Integer, Integer> map;
		private double precision;
		private double recall;
		private double fmeasure;
		private String query;

		public F_Measure_Steinbach() {
			map = new TreeMap<Integer, Integer>();
			precision = 0;
			recall = 0;
			fmeasure = 0;
			query = "";
		}
		
		public void getFMeasure(Clusters gold, Clusters test) {
			ArrayList<Cluster> testClusters = test.getCluster();
			int documentCount = 0;
			
			for (Cluster testI : testClusters) {
				double[] eva = getEvaI(gold, testI);
				int ni = testI.documents.size();
				precision += ni*eva[0];
				recall += ni*eva[1];
				fmeasure += ni*eva[2];
				documentCount += ni;
				
//				System.out.println("\tiPrecision: " + eva[0]);
//				System.out.println("\tiRecall: " + eva[1]);
//				System.out.println("\tiFMeasure: " + eva[2]);
//				System.out.println();
			}
			
			precision = precision/documentCount;
			recall = recall/documentCount;
			fmeasure = fmeasure/documentCount;

			System.out.println("Precision: " + precision);
			System.out.println("Recall: " + recall);
			System.out.println("FMeasure: " + fmeasure);
		}
		
		private double[] getEvaI(Clusters gold, Cluster testI) {
			ArrayList<Cluster> gCluster = gold.getCluster();
			double precisionI = 0;
			double recallI = 0;
			double fmeasureI = 0;
			
			for (Cluster goldJ : gCluster) {
				double[] eva = getEvaIj(goldJ, testI);
				precisionI = max(precisionI, eva[0]);
				recallI = max(recallI, eva[1]);
				fmeasureI = max(fmeasureI, eva[2]);		
				
//				System.out.println("\t\tijPrecision: " + eva[0]);
//				System.out.println("\t\tijRecall: " + eva[1]);
//				System.out.println("\t\tijFMeasure: " + eva[2]);
//				System.out.println();
			}
			
			double[] ret = {precisionI, recallI, fmeasureI};
			return ret;
		}
		
		private double max(double a, double b) {
			if (a > b)
				return a;
			else
				return b;
		}

		private double[] getEvaIj(Cluster goldJ, Cluster testI) {
			ArrayList<String> tDocument = testI.documents;
			ArrayList<String> gDocument = goldJ.documents;
			ArrayList<String> intersection = new ArrayList<String>();
			intersection.addAll(gDocument);
			intersection.retainAll(tDocument);
			double precisionIj = intersection.size()/(double) tDocument.size();
			double recallIj = intersection.size()/(double) gDocument.size();
			double fmeasureIj = 0;
			if (precisionIj >0 && recallIj > 0) {
				fmeasureIj = 2 * precisionIj * recallIj / (precisionIj + recallIj);
			}
			double[] ret = {precisionIj, recallIj, fmeasureIj};
			return ret;
		}
		
		public String toString() {
			return query + "\t" + precision + "\t" + recall + "\t" + fmeasure;
		}

		public static void main(String[] args) throws IOException {
//			ambient();
//			chineseSL();

			process("»‰≥Ê");
			process("¥Ô∑“∆Ê");
			process("ı¿Õª»™");
		}

		private static void process(String query) throws IOException {
			Clusters gold = new Clusters("./data/annotationXML/" + query + ".xml");
			Clusters test = new Clusters("./data/Cluster/wsi/" + query + ".xml");
			F_Measure_Steinbach f = new F_Measure_Steinbach();
			f.getFMeasure(gold, test);
		}

		private static void ambient() throws IOException {
			WriteFile lingowf = new WriteFile("./data/ambient/A_Lingo_F-measure.txt", false, "gbk");
			WriteFile stcwf = new WriteFile("./data/ambient/A_stc_F-measure.txt", false, "gbk");
			
			ReadFile rf = new ReadFile("./data/ambient/no-topics.txt", "GBK");
			String line = "";
			while((line = rf.readLine()) != null) {
				System.out.println(line);
				String[] pair = line.split("\t");
				Clusters gold = new Clusters("./data/ambient/" +pair[0] + ".xml");
				AmbientMapper test = new AmbientMapper(pair[0]);
				test.getClusters("./data/ambient/lingo/" + pair[1].toLowerCase().replace(" ", "-") + ".xml");
				F_Measure_Steinbach f = new F_Measure_Steinbach();
				f.getFMeasure(gold, test);
				lingowf.writeLine(f.toString());
				
				gold = new Clusters("./data/ambient/" +pair[0] + ".xml");
				test = new AmbientMapper(pair[0]);
				test.getClusters("./data/ambient/stc/" + pair[1].toLowerCase().replace(" ", "-") + ".xml");
				f = new F_Measure_Steinbach();
				f.getFMeasure(gold, test);
				stcwf.writeLine(f.toString());
			}
			rf.close();
			
			stcwf.close();
			lingowf.close();
		}
		
		private static void chineseSL() throws IOException {
			WriteFile lingowf = new WriteFile("./data/Cluster/C_Lingo_F-measure.txt", false, "gbk");
			WriteFile stcwf = new WriteFile("./data/Cluster/C_stc_F-measure.txt", false, "gbk");
			
			
			ReadFile rf = new ReadFile("./data/testTopic.txt", "GBK");
			String line = "";
			while((line = rf.readLine()) != null) {
				System.out.println(line);
				Clusters gold = new Clusters("./data/annotationXML/" + line + ".xml");
				Clusters test = new Clusters("./data/Cluster/lingo/" + line + ".xml");
				F_Measure_Steinbach f = new F_Measure_Steinbach();
				f.getFMeasure(gold, test);
				lingowf.writeLine(f.toString());
				
				gold = new Clusters("./data/annotationXML/" + line + ".xml");
				test = new Clusters("./data/Cluster/stc/" + line + ".xml");
				f = new F_Measure_Steinbach();
				f.getFMeasure(gold, test);
				stcwf.writeLine(f.toString());
			}
			rf.close();
			
			stcwf.close();
			lingowf.close();
		}
		
		private static void chineseWSI() throws IOException {
			WriteFile wsiwf = new WriteFile("./data/Cluster/C_Lingo_F-measure.txt", false, "gbk");
			
			ReadFile rf = new ReadFile("./data/testTopic.txt", "GBK");
			String line = "";
			while((line = rf.readLine()) != null) {
				System.out.println(line);
				Clusters gold = new Clusters("./data/annotationXML/" + line + ".xml");
				Clusters test = new Clusters("./data/Cluster/wsi/" + line + ".xml");
				F_Measure_Steinbach f = new F_Measure_Steinbach();
				f.getFMeasure(gold, test);
				wsiwf.writeLine(f.toString());
			}
			rf.close();
			
			wsiwf.close();
		}
}
