package ilt.playground;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DocumentEngine {

	public DocumentTemplate template;
	public Map<Variable, String> answers = new HashMap<>();
	
	List<Variable> openQuestions;
	String result = null;
	
	public Variable getNextQuestion() {
		if (result != null) {
			Variable v = new Variable();
			v.questionText = new Question(result);
			return v;
		}
		refresh();
		return openQuestions.get(0);
		
	}
	
	public void acceptAnswer(Variable question, String answer) {
		answers.put(question.canonical(), answer);
		result = template.resolve(answers);
	}
	
	public void refresh() {
		openQuestions = template.getOpenQuestions(answers);
	}
	
}
