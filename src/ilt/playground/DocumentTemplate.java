package ilt.playground;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DocumentTemplate {

	List<Clause> clauses;
	
	public DocumentTemplate(List<Clause> clauses) {
		this.clauses = clauses;
	}
	
	public String render() {
		return "";
	}
	
	public List<Variable> getOpenQuestions(Map<Variable, String> answers) {
		List<Variable> result = new ArrayList<>();
		for (Clause c : clauses) {
			result.addAll(c.getOpenQuestions(answers));
		}
		return result;
	}

	public String resolve(Map<Variable, String> answers) {
		StringBuilder result = new StringBuilder();
		for (Clause c : clauses) {
			String next = c.resolve(answers);
			if (next == null) {
				return null;
			}
			result.append(next);
		}
		return result.toString();
	}
	
}
