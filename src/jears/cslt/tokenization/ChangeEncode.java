package jears.cslt.tokenization;

import java.io.File;
import java.io.IOException;

import jears.cslt.util.fileOperation.CreateDir;
import jears.cslt.util.fileOperation.ReadFile;
import jears.cslt.util.fileOperation.WriteFile;

public class ChangeEncode {
	public static void main(String[] args) throws IOException {
		String dir = "NTCIR9/NTCIR9candidateexpansion/";
		String newdir = "NTCIR9/NTCIR9candidateexpansiongbk/";
		CreateDir.createDir(newdir);
		for (int i = 1; i <= 100; i++) {
			WriteFile wf = new WriteFile(newdir+i, false, "gbk");
			ReadFile rf = new ReadFile(dir + i);
			
			String line = "";
			while((line = rf.readLine()) != null) {
				wf.writeLine(line.trim(), "\n");
			}
			
			rf.close();
			wf.close();
		}
	}
}
