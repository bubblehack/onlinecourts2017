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
	public String preamble = "";
	
	public Variable getNextQuestion() {
		if (result != null) {
			Variable v = new Variable();
			v.questionText = new Question(result);
			MakePdf.go(template, answers);
			return v;
		}
		refresh();
		return openQuestions.get(0);
	}
	
	public void acceptAnswer(Variable question, String answer) {
		if (answer.startsWith("/")) {
			if (answer.equals("/reset")) {
				answers.clear();
			}
		} else {
			answers.put(question.canonical(), answer);
		}
		result = template.resolve(answers);
	}
	
	public void refresh() {
		preamble = "";
		List<ItemHelp> helps = new ArrayList<>();
		openQuestions = template.getOpenQuestions(answers, helps);
		for (ItemHelp help : helps) {
			if (!help.shown) {
				preamble = preamble + help.helpText + "\n";
				help.shown = true;
			}
		}
	}
	
}
