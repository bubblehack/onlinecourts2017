package ilt.playground;

public class Variable {
	Clause clause;
	
	Question questionText;
	String answer;
	
	
	
	public static Variable simple(String questionText) {
		Variable v = new Variable();
		v.questionText = new Question(questionText);
		return v;
	}



	public static Variable clause(Clause clause) {
		Variable v = new Variable();
		v.clause = clause;
		return v;
	}
}
