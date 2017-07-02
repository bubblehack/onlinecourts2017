package ilt.playground;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Template {

	public String templateString;
	public String result = null;
	public Map<String, Variable> replacements = new LinkedHashMap<>();
	
	public List<Variable> getOpenQuestions(Map<Variable, String> answers) {
		List<Variable> result = new ArrayList<>();
		for (Variable v : replacements.values()) {
			if (!answers.containsKey(v)) {
				result.addAll(v.getOpenQuestions(answers));
			}
		}
		return result;
	}

	public String resolve(Map<Variable, String> answers) {
		if (result == null) {
			String tmpResult = templateString;
			for (String key : replacements.keySet()) {
				Variable v = replacements.get(key);
				if (!answers.containsKey(v) && v.resolve(answers) == null) {
					return null;
				}
				String match = "\\$" + key;
				tmpResult = tmpResult.replaceAll(match, answers.get(v));
			}
			result = tmpResult;
		}
		return result;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((replacements == null) ? 0 : replacements.hashCode());
		result = prime * result + ((this.result == null) ? 0 : this.result.hashCode());
		result = prime * result + ((templateString == null) ? 0 : templateString.hashCode());
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
		Template other = (Template) obj;
		if (replacements == null) {
			if (other.replacements != null)
				return false;
		} else if (!replacements.equals(other.replacements))
			return false;
		if (result == null) {
			if (other.result != null)
				return false;
		} else if (!result.equals(other.result))
			return false;
		if (templateString == null) {
			if (other.templateString != null)
				return false;
		} else if (!templateString.equals(other.templateString))
			return false;
		return true;
	}

	public Template create(String path) {
		Template t = new Template();
		t.templateString = templateString;
		for (Entry<String, Variable> var : replacements.entrySet()) {
			String key = var.getKey();
			t.replacements.put(key, var.getValue().create(path));
		}
		
		return t;
	}
}
