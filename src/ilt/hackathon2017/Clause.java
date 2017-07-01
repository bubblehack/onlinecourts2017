package ilt.hackathon2017;

import java.util.HashMap;
import java.util.Map;

public class Clause {

	public final String type;
	public final String template;
	public final Map<String, Question> variables;
	
	public String render(Map<Question, String> answers) {
		return "";
	}

	public Clause(String type, String template) {
		super();
		this.type = type;
		this.template = template;
		this.variables = new HashMap<>();
		
	}
	
	
	
}
