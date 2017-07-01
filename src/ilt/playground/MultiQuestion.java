package ilt.playground;

import java.util.ArrayList;
import java.util.List;

public class MultiQuestion extends Question {
	public List<String> options = new ArrayList<>();
	
	public MultiQuestion(String text) {
		super(text);
	}
	
	public static MultiQuestion of(Question q) {
		return new MultiQuestion(q.text);
	}
}
