package tfidf;

import java.io.IOException;
import java.util.Iterator;

import jears.cslt.util.fileOperation.CreateDir;
import jears.cslt.util.fileOperation.WriteFile;

public class TfIdfCal {
	public static void main(String[] args) throws IOException{
		//Test code for TfIdf
		String dir = "./data/Snippet-Remove/美国大选";
		getTfIdfSim(dir);
	}

	public static void getTfIdfSim(String dir){
		TfIdf tf = new TfIdf(dir);
		tf.buildAllDocuments();
		String file;
		for (Iterator<String> it = tf.documents.keySet().iterator(); it.hasNext(); ) {
			file = it.next();
			CreateDir.createDir(dir.replaceAll("Remove", "tfidf"));
			WriteFile wf;
			try {
				wf = new WriteFile(dir.replaceAll("Remove", "tfidf") + "/" + file, false, "GB2312");
				wf.writeLine(tf.documents.get(file).toString());
				wf.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		tf.printCosSimMatrix();
	}
}
