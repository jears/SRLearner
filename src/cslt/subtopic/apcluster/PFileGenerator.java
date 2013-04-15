package cslt.subtopic.apcluster;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import jears.cslt.util.fileOperation.*;

public class PFileGenerator {
	public enum Type {
		THU, MAX, THUNORM
	}
	public void getPfile(Type type, String qsdir, String simdir, String pdir) throws IOException {
		switch (type) {
		case THU:
			getTHU(qsdir, pdir);
			break;
		case MAX:
			getMAX(simdir, pdir);
			break;
		case THUNORM:
			getTHUNORM(qsdir, pdir);
			break;
		}
	}
	
	public void getTHU(String qsdir, String pdir) throws IOException {
		File dir = new File(qsdir);
		String[] files = dir.list();
		for (String f : files) {
			ReadFile rf = new ReadFile(qsdir + f);
			WriteFile wf = new WriteFile(pdir + f, false, "gbk");
			
			String line = "";
			while((line = rf.readLine()) != null) {
				if (line.length() > 5) {
					String[] element = line.split("\t");
					wf.writeLine(element[element.length-1], "\n");
				}
			}
			
			wf.close();
			rf.close();
		}
	}
	
	public void getTHUNORM(String qsdir, String pdir) throws IOException {
		File dir = new File(qsdir);
		String[] files = dir.list();
		for (String f : files) {
			ReadFile rf = new ReadFile(qsdir + f);
			WriteFile wf = new WriteFile(pdir + f, false, "gbk");
			ArrayList<Double> pvec = new ArrayList<Double>();
			
			String line = "";
			while((line = rf.readLine()) != null) {
				if (line.length() > 5) {
					String[] element = line.split("\t");
					pvec.add(Double.parseDouble(element[element.length-1]));
				}
			}
			
			Collections.sort(pvec);
			double max = pvec.get(pvec.size()-1);
			
			rf.close();
			rf = new ReadFile(qsdir + f);
			
			while((line = rf.readLine()) != null) {
				if (line.length() > 5) {
					String[] element = line.split("\t");
					wf.writeLine(String.valueOf((Double.parseDouble(element[element.length-1])/max)), "\n");
				}
			}
			
			wf.close();
			rf.close();
		}
	}
	
	public void getMAX(String simdir, String pdir) throws IOException {
		File dir = new File(simdir);
		String[] files = dir.list();
		for (String f : files) {
			ReadFile rf = new ReadFile(simdir + f);
			WriteFile wf = new WriteFile(pdir + f, false, "gbk");
			
			String line = rf.readLine();
			int candidateNum = Integer.parseInt(line);
			for (int j = 0; j < candidateNum; j++) {
				wf.writeLine("1", "\n");
			}
			
			wf.close();
			rf.close();
		}
	}
	
	public static void main(String[] args) throws IOException {
		CreateDir.createDir("./pap/");
		CreateDir.createDir("./maxpap/");
		CreateDir.createDir("./normpap/");
		
		(new PFileGenerator()).getTHU("./qsap/", "./normpap/");

	}
}
