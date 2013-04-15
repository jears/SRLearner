package jears.cslt.tokenization;

import java.util.ArrayList;
import java.util.HashSet;

public class Rule {
	ArrayList<String> order;
	ArrayList<String> addprep;
	HashSet<String> preps;

	public Rule() {
		order = new ArrayList<String>();
		addprep = new ArrayList<String>();
		preps = new HashSet<String>();
		preps.add("of");
		preps.add("in");
		preps.add("at");
		preps.add("for");
	}

	public void changeOrder(String prefix, ArrayList<String> query, int level) {
		if (query.size() == 0) {
			prefix = prefix.trim();
			order.add(prefix);
		} else {
			for (int i = 0; i < query.size(); i++) {
				String prefix2 = prefix + query.get(i) + " ";
				ArrayList<String> query2 = new ArrayList<String>();
				for (int j = 0; j < query.size(); j++) {
					if (j != i)
						query2.add(query.get(j));
				}
				level++;
				changeOrder(prefix2, query2, level);
				level--;
			}
		}
	}

	public void addPrep(ArrayList<String> query) {
		for (String prep : preps)
			for (int i = 0; i < query.size() - 1; i++) {
				String temp = "";
				for (int k = 0; k < query.size(); k++) {
					if (k != i) {
						temp += query.get(k) + " ";
					} else {
						temp += query.get(k) + " " + prep + " ";
					}
				}
				temp = temp.trim();
				addprep.add(temp);
			}
	}

	public String toString() {
		return order.toString();
	}
	
	public String prepToString() {
		return addprep.toString();
	}

	public static void main(String[] args) {
		Rule r = new Rule();
		ArrayList<String> query = new ArrayList<String>();
		query.add("a");
		query.add("b");
		query.add("c");
		query.add("d");
//		r.changeOrder("", query, 0);
//		System.out.println(r.toString());
		r.addPrep(query);
		System.out.println(r.prepToString());
	}
}
