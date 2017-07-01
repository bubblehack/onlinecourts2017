package ilt.playground;

public class Question {
	public String text;
	
	public Question(String text) {
		super();
		this.text = text;
	}

	public Question canonical() {
		return new Question(text);
	}
}
