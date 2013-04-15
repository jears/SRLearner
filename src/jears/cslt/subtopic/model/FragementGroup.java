package jears.cslt.subtopic.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class FragementGroup {
	private List<Fragement> fragementGroup;

	public FragementGroup() {
		fragementGroup = new ArrayList<Fragement>();
	}

	public void addFragement(Fragement r) {
		fragementGroup.add(r);
	}

	public void addAllFragement(List<Fragement> fs) {
		fragementGroup.addAll(fs);
	}

	public List<Fragement> getFragementGroup() {
		return Collections.unmodifiableList(fragementGroup);
	}

	public Fragement get(int i) {
		return fragementGroup.get(i);
	}

	public int size() {
		return fragementGroup.size();
	}
}
