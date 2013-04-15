package cslt.subtopic.apcluster;

public class NoExmCluster extends Cluster {
	public NoExmCluster(String exemplar, String member) {
		super(exemplar, member);
		addExamplar();
	}
	
//	public void sort() {
//		
//		Collections.sort(members);
//	}
}
