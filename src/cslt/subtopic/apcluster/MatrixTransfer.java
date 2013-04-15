package cslt.subtopic.apcluster;

import java.io.IOException;
import jears.cslt.util.fileOperation.*;

public class MatrixTransfer {
	public static void transferMatrix(String filename, String simdir, String sdir){
		try {
			ReadFile rf = new ReadFile(simdir + filename, "gbk");
			WriteFile wf = new WriteFile(sdir + filename, false, "gbk");
			String line = rf.readLine();
			int size = Integer.parseInt(line);
			for (int i = 0; i < size; i++) {
				line = rf.readLine();
				wf.writeLine(line.replaceAll("\t", " "));
			}
			wf.close();
			rf.close();
		} catch (IOException e) {
			System.out.println("[warning]: " + filename + "sim doesn't exit!");
		}
	}
	public static void main(String[] args) throws IOException {
		for (int i = 1; i <= 50; i++)
			transferMatrix(String.valueOf(i), "sim/", "./sap/");
	}
}
