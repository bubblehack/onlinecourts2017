package ilt.playground;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DocumentTemplate {

	List<Clause> clauses;
	List<ItemHelp> help;
	
	public DocumentTemplate(List<Clause> clauses, List<ItemHelp> help) {
		this.clauses = clauses;
		this.help = help;
	}
	
	public List<Variable> getOpenQuestions(Map<Variable, String> answers, List<ItemHelp> helps) {
		List<Variable> result = new ArrayList<>();
		ItemHelp section = null;
		ItemHelp clause = null;
		for (Clause c : clauses) {
			if (c.name != null) {
				for (ItemHelp i : help) {
					if (i.isSection) {
						section = i;
					}
					if (i.itemName.equals(c.name)) {
						clause = i;
						break;
					}
				}
			}
			result.addAll(c.getOpenQuestions(answers));
			if (!result.isEmpty()) {
				if (section != null) helps.add(section);
				if (clause != null) helps.add(clause);
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
