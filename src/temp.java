import java.io.IOException;

import jears.cslt.util.fileOperation.ReadFile;
import jears.cslt.util.fileOperation.WriteFile;


public class temp {
	public static void main(String[] args) throws IOException {
		ReadFile rf = new ReadFile("./data/ambient/STRel.txt");
		
		String line = "";
		while((line=rf.readLine()) != null) {
			System.out.println(line);
			String[] pair = line.split("	");
			System.out.println(pair[0] + " && " + pair[1]);
			String[] subtopic = pair[0].split("\\.");
			System.out.println(subtopic[0] + " && " + subtopic[1]);
			String[] result = pair[1].split("\\.");
			System.out.println(pair[0] + " && " + pair[1]);
			WriteFile wf = new WriteFile("./data/ambient/" + subtopic[0] + ".txt", true);
			wf.writeLine(subtopic[1] + "\t" + result[1]);
			wf.close();
		}
		
		rf.close();
	}
}
