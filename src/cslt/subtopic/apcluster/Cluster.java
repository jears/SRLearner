package cslt.subtopic.apcluster;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Cluster implements Comparable<Cluster> {
	Member exemplar;
	/**
	 * exemplar is one of members
	 */
	List<Member> members;

	double clusterScore;
	private int index = 0;

	static class Member implements Comparable<Member> {
		double score;
		String subtopic;

		public Member(String member) {
			subtopic = member;
			score = 1; 
		}
		
		public String toString() {
			return subtopic + "\t" + score;
		}
		
		public int compareTo(Member o) {
			// TODO Auto-generated method stub
			return -(new Double(this.score).compareTo(o.score));
		}
	}

	public int getMemberSize() {
		return members.size();
	}
	public Cluster(String exemplar, String member) {
		Member e = new Member(exemplar);
		this.exemplar = e;
		members = new ArrayList<Cluster.Member>();
		clusterScore = e.score;
		if (!member.equals(exemplar))	
			addMember(member);
	}

	public void addMember(String member) {
		Member m = new Member(member);
		if (m.subtopic.equals(exemplar.subtopic) && m.score==exemplar.score)
			return;
		members.add(m);
		clusterScore += m.score;
	}
	
	protected void addExamplar() {
		members.add(this.exemplar);
	}
	
	public String getExmplar() {
		return exemplar.subtopic;
	}
	
	public void sort() {
		Collections.sort(members);
	}
	
	public String toString() {
		String res = exemplar + "\t" + clusterScore + "\n";

		for (int i = 0; i < members.size(); ++i) {
			res += "\t" + members.get(i) + "\n";
		}

		return res;
	}
	
	public String next() {
		if (index < members.size())
			return members.get(index++).subtopic;
		else
			return null;
	}

	@Override
	public int compareTo(Cluster o) {
		// TODO Auto-generated method stub
		return -(new Double(this.clusterScore).compareTo(o.clusterScore));
	}
}
