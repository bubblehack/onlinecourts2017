package ilt.playground;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ClauseDictionary {
	
	private Map<String, Clause> clauses = new HashMap<>();
	
	public void update(InputStream in) {
		clauses.putAll(ClauseParser.parseClauses(in));
	}
	
	public List<Clause> generateList(Set<String> rootClauses) {
		List<Clause> roots = new ArrayList<>();
		
		for (String rootName : rootClauses) {
			roots.add(clauses.get(rootName).create("GLOBAL"));
		}
		
		return roots;
	}
	

}
