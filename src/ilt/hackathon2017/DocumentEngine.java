package ilt.hackathon2017;

import java.util.Map;
import java.util.Set;

public class DocumentEngine {

	DocumentTemplate template;
	
	Set<SingleQuestion> openQuestions;
	Map<Question, String> answers;
	
	public SingleQuestion getNextQuestion() {
		return new SingleQuestion(new QuestionTemplate("What is your name?"));
	}
	
	public void acceptAnswer(Question question, String answer) {
		
	}
	
	public void refreshOpenQuestions() {
		for (Clause c : template.clauses) {
			
		}
	}
	
}
