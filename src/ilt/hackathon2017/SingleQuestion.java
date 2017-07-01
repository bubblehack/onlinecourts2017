package ilt.hackathon2017;

import java.util.Arrays;
import java.util.List;

public class SingleQuestion implements Question {

	private QuestionTemplate template;

	public SingleQuestion(QuestionTemplate template) {
		this.template = template;
	}

	@Override
	public List<QuestionTemplate> getTemplates() {
		return Arrays.asList(template);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((template == null) ? 0 : template.hashCode());
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
		SingleQuestion other = (SingleQuestion) obj;
		if (template == null) {
			if (other.template != null)
				return false;
		} else if (!template.equals(other.template))
			return false;
		return true;
	}
}
