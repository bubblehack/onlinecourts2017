package ilt.playground;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Variable {
	public Clause clause;
	public Question questionText;
	public String scope;
	public boolean finalMessage = false;
	
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

	public String resolve(Map<Variable, String> answers, int id,boolean markup) {
		if (clause != null) {
			String result = clause.resolve(answers, id, markup);
			if (result != null) {
				answers.put(this, result);
			}
			return result;
		} else {
			return null;
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((clause == null) ? 0 : clause.hashCode());
		result = prime * result + ((questionText == null) ? 0 : questionText.hashCode());
		result = prime * result + ((scope == null) ? 0 : scope.hashCode());
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
		Variable other = (Variable) obj;
		if (clause == null) {
			if (other.clause != null)
				return false;
		} else if (!clause.equals(other.clause))
			return false;
		if (questionText == null) {
			if (other.questionText != null)
				return false;
		} else if (!questionText.equals(other.questionText))
			return false;
		if (scope == null) {
			if (other.scope != null)
				return false;
		} else if (!scope.equals(other.scope))
			return false;
		return true;
	}

	public Variable create(String path) {
		Variable v = new Variable();
		if (clause != null) 
			v.clause = clause.create(path);
		v.questionText = questionText;
		v.scope = path + "." + scope;
		
		return v;
	}
}
