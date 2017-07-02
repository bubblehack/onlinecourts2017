package ilt.playground;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DocumentTemplate {

	List<Clause> clauses;
	
	public DocumentTemplate(List<Clause> clauses) {
		this.clauses = clauses;
	}
	
	public List<Variable> getOpenQuestions(Map<Variable, String> answers) {
		List<Variable> result = new ArrayList<>();
		for (Clause c : clauses) {
			result.addAll(c.getOpenQuestions(answers));
			if (!result.isEmpty()) {
				break;
			}
		}
		return result;
	}

	public String resolve(Map<Variable, String> answers) {
		StringBuilder result = new StringBuilder();
		int index = 0;
		for (Clause c : clauses) {
			String next = c.resolve(answers);
			if (next == null) {
				return null;
			}
			result.append(++index + ". " + next + "\n\n");
		}
		return result.toString();
	}
	
	public List<String> resolveClauses(Map<Variable, String> answers) {
		List<String> result = new ArrayList<>();
		int index = 0;
		for (Clause c : clauses) {
			result.add(++index + ". " + c.resolve(answers));
		}
		return result;
	}
	
}
