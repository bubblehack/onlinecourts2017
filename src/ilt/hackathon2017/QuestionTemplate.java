package ilt.hackathon2017;

import java.util.ArrayList;
import java.util.List;

public class QuestionTemplate {

	public final String questionText;
	public final List<String> options;
	
	public QuestionTemplate(String questionText) {
		this(questionText, new ArrayList<>());
	}
	
	public QuestionTemplate(String questionText, List<String> options) {
		this.questionText = questionText; 
		this.options = options;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((options == null) ? 0 : options.hashCode());
		result = prime * result + ((questionText == null) ? 0 : questionText.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		QuestionTemplate other = (QuestionTemplate) obj;
		if (options == null) {
			if (other.options != null)
				return false;
		} else if (!options.equals(other.options))
			return false;
		if (questionText == null) {
			if (other.questionText != null)
				return false;
		} else if (!questionText.equals(other.questionText))
			return false;
		return true;
	}
	
}
