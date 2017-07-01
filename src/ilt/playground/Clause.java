package ilt.playground;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Clause {
	
	Question globalQuestion; //null if always true
	
	Map<String, Template> templates; //key is "" if globalQuestion is null.
	
	public List<Question> getOpenQuestions(Map<Question, String> answers) {
		return new ArrayList<>();
	}

}
