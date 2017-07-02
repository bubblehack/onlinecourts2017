package ilt.playground;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Clause {
	
	Variable globalQuestion; //null if always true
	
	String name;
	
	Map<String, Template> templates = new HashMap<>(); //key is "" if globalQuestion is null.

	
	public List<Variable> getOpenQuestions(Map<Variable, String> answers) {
		String key = "";
		if (globalQuestion != null) {
			if (answers.containsKey(globalQuestion)) {
				key = answers.get(globalQuestion);
			} else {
				MultiVariable v = MultiVariable.of(globalQuestion);
				v.options = new ArrayList<>(templates.keySet());
				return Arrays.asList(v);
			}
		}
		return templates.get(key).getOpenQuestions(answers);
	}

	public String resolve(Map<Variable, String> answers) {
		String key = "";
		if (answers.containsKey(globalQuestion)) {
			key = answers.get(globalQuestion);
		}
		if (!templates.containsKey(key)) {
			return null;
		}
		return templates.get(key).resolve(answers);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((globalQuestion == null) ? 0 : globalQuestion.hashCode());
		result = prime * result + ((templates == null) ? 0 : templates.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Clause other = (Clause) obj;
		if (globalQuestion == null) {
			if (other.globalQuestion != null)
				return false;
		} else if (!globalQuestion.equals(other.globalQuestion))
			return false;
		if (templates == null) {
			if (other.templates != null)
				return false;
		} else if (!templates.equals(other.templates))
			return false;
		return true;
	}

	public Clause create(String path) {
		Clause c = new Clause();
		c.name = name;
		
		if (globalQuestion != null) {
			c.globalQuestion = globalQuestion.create(path + "." + name);
		}
		
		for (Entry<String, Template> t : templates.entrySet()) {
			c.templates.put(t.getKey(), t.getValue().create(path + "." + name));
		}
		
		return c;
	}
}
