package ilt.playground;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Variable {
	public Clause clause;
	public Question questionText;
	public String scope;
	
	public static Variable simple(String questionText, String scope) {
		Variable v = new Variable();
		v.questionText = new Question(questionText);
		v.scope = scope;
		return v;
	}

	public static Variable clause(Clause clause, String scope) {
		Variable v = new Variable();
		v.clause = clause;
		v.scope = scope;
		return v;
	}
	
	public List<Variable> getOpenQuestions(Map<Variable, String> answers) {
		if (clause != null) {
			return clause.getOpenQuestions(answers);
		}
		return Arrays.asList(this);
	}
	
	public Variable canonical() {
		if (this instanceof MultiVariable) {
			Variable v = new Variable();
			v.clause = clause;
			v.questionText = questionText;
			v.scope = scope;
		return v;
		} else {
			return this;
		}
	}

	public String resolve(Map<Variable, String> answers) {
		if (clause != null) {
			String result = clause.resolve(answers);
			if (result != null) {
				answers.put(this, result);
			}
			return result;
		} else {
			return null;
		}
	}
}
