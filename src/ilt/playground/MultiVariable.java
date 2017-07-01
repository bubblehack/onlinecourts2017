package ilt.playground;

import java.util.ArrayList;
import java.util.List;

public class MultiVariable extends Variable {
	public List<String> options = new ArrayList<>();
	
	public MultiVariable(Variable v) {
		questionText = v.questionText;
		scope = v.scope;
	}
	
	public static MultiVariable of(Variable v) {
		return new MultiVariable(v);
	}
}
