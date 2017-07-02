package ilt.playground;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Clause {

	Variable globalQuestion; // null if always true

	String name;

	public Clause dispute;

	Map<String, Template> templates = new HashMap<>(); // key is "" if globalQuestion is null.

	
	private List<Clause> subDisagreements;

	private Template originalTemplate;
	
	private Clause makeDisputeClause(String scope, Template template, Variable variable, Map<Variable, String> originalAnswers) {
		Clause c = new Clause();
		
		Template admitted = new Template();
		admitted.templateString = "Admitted";
		c.templates.put("no", admitted);
		
		Template disputed = new Template();
		disputed.templateString = "I disagree because $reason.";
		Variable disagreementReason = Variable.simple("Why do you disagree with this clause?", name + ".disagree");
		disputed.replacements.put("reason", disagreementReason);
		c.templates.put("yes", disputed);
		
		String originalAnswer;
		
		if (template != null)
			originalAnswer = template.resolve(originalAnswers);
		else
			originalAnswer = originalAnswers.get(variable);

		c.globalQuestion = Variable
				.simple("The claimant answered:\n" + originalAnswer + "\nDo you dispute this?", name);
		c.name = "dispute";
		c.globalQuestion.scope = scope;
		
		return c;
	}

	public Variable getNextDefenseQuestion(Map<Variable, String> answers, Map<Variable, String> originalAnswers) {
		if (dispute == null) {


			String template = "";
			if (globalQuestion != null)
				template = originalAnswers.get(globalQuestion);
			originalTemplate = templates.get(template);
			dispute = makeDisputeClause("", originalTemplate, null, originalAnswers);
			
			Template partial = new Template();
			partial.templateString = "Partial";
			dispute.templates.put("partially", partial);
			
		}

		Variable v = Variable.clause(dispute, name);
		List<Variable> openQuestions = v.getOpenQuestions(answers);
		if (openQuestions.isEmpty() && answers.get(v.clause.globalQuestion).equals("partially")) {
			if (subDisagreements == null) {
				subDisagreements = new ArrayList<>();
				
				for (Entry<String, Variable> subVariable : originalTemplate.replacements.entrySet()) {
					if (subVariable.getValue().clause != null) {
						
						String template = "";
						if (subVariable.getValue().clause.globalQuestion != null)
							template = originalAnswers.get(subVariable.getValue().clause.globalQuestion);
						Template originalSubTemplate = subVariable.getValue().clause.templates.get(template);
						
						subDisagreements.add(makeDisputeClause(name, originalSubTemplate, null, originalAnswers));
					} else {
						subDisagreements.add(makeDisputeClause(name, null, subVariable.getValue(), originalAnswers));
					}
					
				}
				
			}
			
			for (Clause subClause : subDisagreements) {
				Variable subVariable = Variable.clause(subClause, name);
				openQuestions = subVariable.getOpenQuestions(answers);
				if (openQuestions != null && !openQuestions.isEmpty())
					break;
			}
		} 
		return openQuestions.isEmpty() ? null : openQuestions.get(0);
		
		
		
	}

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
