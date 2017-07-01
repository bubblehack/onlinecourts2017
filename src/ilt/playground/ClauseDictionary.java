package ilt.playground;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClauseDictionary {
	
	private Map<String, Clause> clauses = new HashMap<>();
	private List<String> rootClauses = new ArrayList<>();
	
	
	public void update(InputStream in) {
		ClauseParser parser = ClauseParser.parseClauses(in);
		clauses.putAll(parser.map);
		for (String s : parser.rootClauses) {
			if (!rootClauses.contains(s)) {
				rootClauses.add(s);
			}
		}
		System.err.println(rootClauses);
	}
	
	public List<Clause> generateList() {
		List<Clause> roots = new ArrayList<>();
		
		for (String rootName : rootClauses) {
			roots.add(clauses.get(rootName).create("GLOBAL"));
		}
		
		return roots;
	}
	

}
