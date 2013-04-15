package cslt.subtopic.apcluster;

import java.io.IOException;

import jears.cslt.util.fileOperation.*;

public class FileMerger {
	private int topicNum = 50;
	public FileMerger(int topicNum) {
		this.topicNum = topicNum;
	}
	
	public void mergeResult(String filename, String rankdir) throws IOException {
			WriteFile wf = new WriteFile(filename, false);
			for (int i = 1; i <= topicNum; i++) {
				try {
					ReadFile rf = new ReadFile(rankdir+i);
					
					String line = "";
					int subtopicNo = 1;
					while((line = rf.readLine()) != null) {
						if (line.length() < 5)
							continue;
						wf.writeLine(format(i, subtopicNo, line), "\n");
						subtopicNo++;
					}
					
					rf.close();
				} catch (Exception e) {
//					wf.writeLine(format(i, 1, "thisisdummydata"), "\n");
				}
			}
			
			wf.close();
	}
	
	private String format(int topicNo, int subtopicNo, String subtopic) {
		return Format.get000X(topicNo) + ";0;" + subtopic + ";" + Format.get0X(subtopicNo) + ";0;run3;";
	}
	
	public static void main(String[] args) throws IOException {
//		mergeResult("run3.txt", "./rankap/");
	}
}
