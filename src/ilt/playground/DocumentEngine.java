package ilt.playground;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DocumentEngine {

	DocumentTemplate template;
	Map<Question, String> answers;
	List<Question> openQuestions;
	
	public Question getNextQuestion() {
		refreshQuestions();
		return openQuestions.get(0);
	}
	
	public void acceptAnswer(Question question, String answer) {
		answers.put(question.canonical(), answer);
	}
	
	public void refreshQuestions() {
		openQuestions = new ArrayList<>();
		for (Clause c : template.clauses) {
			openQuestions.addAll(c.getOpenQuestions(answers));
		}
	}
	
}
