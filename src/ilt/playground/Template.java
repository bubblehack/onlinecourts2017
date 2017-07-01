package ilt.playground;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Template {

	public String templateString;
	public String result = null;
	public Map<String, Variable> replacements = new HashMap<>();
	
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
				tmpResult = tmpResult.replaceAll(key, answers.get(v));
			}
			result = tmpResult;
		}
		return result;
	}
}
