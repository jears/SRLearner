package jears.cslt.jAPCluster;
import com.mathworks.toolbox.javabuilder.MWException;

import cslt.subtopic.apcluster.*;

/**
 * use matlab-generating apcluster.jar
 * transmit parameters by file
 */

public class JAPCluster {	
	/**
	 * s should be a N*N matrix, delimitered by whitespace " "
	 * p should be a N*1 vector
	 * if s(or p) is encoded in utf-8, there should be a blank line in the head of the file
	 * the cluster result is saved in the .exm file, encoded according to the system ("gbk" for windows)
	 * the netsim is saved in the .netsim file
	 * @param sfile
	 * @param pfile
	 * @param encode
	 * @throws MWException
	 */
	public static void apClusterFile(String sfile, String pfile, String encode) throws MWException {
		apcluster.Class1 ap = new apcluster.Class1();
		
		Object[] sname = {sfile};
		Object[] pname = {pfile};
		Object[] code = {encode};
		
		ap.apclusterfile(1, sname, pname, code);
	}
	
	/**
	 * if no pfile, p = median(s/diag(s))
	 * @param sfile
	 * @param encode
	 * @throws MWException
	 */
	public static void apClusterFile(String sfile, String encode) throws MWException {
		apcluster.Class1 ap = new apcluster.Class1();
		
		Object[] sname = {sfile};
		Object[] code = {encode};
		
		ap.apclusterfile(1, sname, 0, code);
	}
	
	public static void main(String[] args) throws MWException {
		apClusterFile("./sAP/46" ,"gbk");
	}
	
}
