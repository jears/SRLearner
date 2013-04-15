package cslt.subtopic.apcluster;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jears.cslt.util.fileOperation.*;


public class FileSplitter {
	public void main(String[] args) throws IOException {
		splitFile("NTCIR10filterJoinCandidate-2.txt", "./qAP/");
		splitFile("NTCIR10THUweight_0723.txt", "./vAP/");
		CreateDir.createDir("./qsAp/");
		int topicNum = 50;
		for (int i = 1; i <= topicNum; i++) {
			System.out.println(i);
			mapFile("./qAP/"+i, "./vAP/"+i, "./qsAp/"+i);
		}
	}
	
	public void mapFile(String file, String oldfile, String newfile) throws IOException {
		try {
			Map<String, String> map = new HashMap<String, String>();
			ReadFile rf2 = new ReadFile(oldfile);
			String line = "";
			while((line = rf2.readLine()) != null) {
				String[] elements = line.split("\t");
				map.put(elements[3], line);
			}
			rf2.close();
			
			ReadFile rf1 = new ReadFile(file);
			WriteFile wf1 = new WriteFile(newfile, false);
			while((line = rf1.readLine()) != null) {
				String[] elements = line.split("\t");
				wf1.writeLine(map.get(elements[3]), "\n");
			}
			wf1.close();
			rf1.close();
		} catch (IOException e) {
			System.out.println("[warning]:" + file + "vote dosen't exit!");
		}
	}
	
	public void splitFile(String candidateFile, String dir) throws IOException {
		List<Topic> topics = new ArrayList<Topic>();
		
		ReadFile rf = new ReadFile(candidateFile);
		
		String line = "";
		while ((line = rf.readLine()) != null) {
//			System.out.println(line);
			String[] elements = line.split("\t");
			Topic t = new Topic(Integer.parseInt(elements[3]), line, elements[1]);
			topics.add(t);
		}
		
		rf.close();
		
		String topicno = "";
		WriteFile wf = new WriteFile("hehehe", false);
		for (int i = 0; i < topics.size(); i++) {
			if (!topicno.equals(topics.get(i).getSource())) {
				CreateDir.createDir(dir);
				wf.close();
				wf = new WriteFile(dir+topics.get(i).getSource(), false);
				topicno = topics.get(i).getSource();
//				System.out.println(topicno + "\t" + i);
			}
			wf.writeLine(topics.get(i).getQuery(), "\n");
		}
//		System.out.println("\t" + (topics.size()-1));
		wf.close();
	}
}
