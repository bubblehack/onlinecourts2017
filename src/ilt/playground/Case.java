package ilt.playground;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Case {
	
	private static Map<String, Case> cases = new HashMap<>();
	private static Object lock = new Object();
	
	public Map<Variable, String> answers = new HashMap<>();
	public List<Clause> clauses = new ArrayList<Clause>();
	private String name;
	
	public Case(String name, List<Clause> clauses, Map<Variable, String> answers) {
		this.name = name;
		this.clauses = clauses;
		this.answers = answers;
	}
	
	public void save() {
		synchronized (lock) {
			cases.put(this.name, this);
		}
	}

	public static Case load(String name) {
		synchronized (lock) {
			return cases.get(name);
		}
	}

	public static List<String> list() {
		List<String> l =  new ArrayList<>();
		l.addAll(cases.keySet());
		return l;
	}
}
