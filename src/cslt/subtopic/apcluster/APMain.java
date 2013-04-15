package cslt.subtopic.apcluster;

import java.io.File;
import java.io.IOException;

import jears.cslt.jAPCluster.JAPCluster;
import jears.cslt.jAPCluster.JAPClusterResult;

import com.mathworks.toolbox.javabuilder.MWException;

import jears.cslt.util.fileOperation.*;

public class APMain {

	public static void main(String[] args) throws IOException, MWException {

		String dir = "./data/Snippet-matrix";

		cluster(dir);

	}

	public static void cluster(String dir) {
		System.out.println("clustering begin");
		try {
			File f = new File(dir);
			File[] fs = f.listFiles();
			for (File file : fs) {
				String path = file.getAbsolutePath();
				if (path.endsWith(".txt")) {

					JAPCluster.apClusterFile(path, "gbk");

					CreateDir.createDirForFile(path.replaceAll("matrix",
							"result"));
					new JAPClusterResult(path + ".exm", path.replaceAll(
							"Snippet-matrix", "Title"), path.replaceAll(
							"matrix", "result"), "./data/test");
				}
			}
		} catch (MWException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("clustering end");
	}
	
	public static void clusterP(String dir) {
		System.out.println("clustering begin");
		try {
			File f = new File(dir);
			File[] fs = f.listFiles();
			for (File file : fs) {
				String path = file.getAbsolutePath();
				if (path.endsWith(".txt")) {

					JAPCluster.apClusterFile(path, "gbk");

					CreateDir.createDirForFile(path.replaceAll("matrix",
							"result"));
					new JAPClusterResult(path + ".exm", path.replaceAll(
							"Snippet-matrix", "Title"), path.replaceAll(
							"matrix", "result"), "./data/test");
				}
			}
		} catch (MWException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("clustering end");
	}
}
