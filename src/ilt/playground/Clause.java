package ilt.playground;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Clause {
	
	Variable globalQuestion; //null if always true
	
	Map<String, Template> templates = new HashMap<>(); //key is "" if globalQuestion is null.
	
	public List<Variable> getOpenQuestions(Map<Variable, String> answers) {
		String key = "";
		if (globalQuestion != null) {
			if (answers.containsKey(globalQuestion)) {
				key = answers.get(globalQuestion);
			} else {
				return Arrays.asList(globalQuestion);
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
}
