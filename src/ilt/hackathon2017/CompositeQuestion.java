package ilt.hackathon2017;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CompositeQuestion implements Question {
	
	List<Question> questions;

	public CompositeQuestion(Question ... questions) {
		this.questions = Arrays.asList(questions);
	}
	
	@Override
	public List<QuestionTemplate> getTemplates() {
		List<QuestionTemplate> results = new ArrayList<>();
		for (Question q : questions) {
			results.addAll(q.getTemplates());
		}
		return results;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((questions == null) ? 0 : questions.hashCode());
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
		CompositeQuestion other = (CompositeQuestion) obj;
		if (questions == null) {
			if (other.questions != null)
				return false;
		} else if (!questions.equals(other.questions))
			return false;
		return true;
	}

}
