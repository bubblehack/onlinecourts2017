package ilt.playground;

import java.util.Arrays;

public class Data {

	public DocumentTemplate doc() {
		
		Template employmentEmployed = new Template();
		employmentEmployed.templateString = "is employed at $employer";
		employmentEmployed.localReplacements.put("employer", Variable.simple("Where are you employed?"));

		Template employmentSelfEmployed = new Template();
		employmentEmployed.templateString = "is self employed";

		Template employmentUnemployed = new Template();
		employmentEmployed.templateString = "is unemployed";
		
		Clause employmentClause = new Clause();
		employmentClause.globalQuestion = new Question("What is your employment status?");
		employmentClause.templates.put("self-employed", employmentSelfEmployed);
		employmentClause.templates.put("unemployed", employmentUnemployed);
		employmentClause.templates.put("employed", employmentEmployed);
		
		Template contractClaimantTemplate = new Template();
		contractClaimantTemplate.templateString = "claimant: $name at $address @Employment";
		contractClaimantTemplate.localReplacements.put("name", Variable.simple("What is your name?"));
		contractClaimantTemplate.localReplacements.put("address", Variable.simple("What is your address?"));
		contractClaimantTemplate.localReplacements.put("Employment", Variable.clause(employmentClause));
		
		Clause claimant = new Clause();
		claimant.globalQuestion = null;
		claimant.templates.put("", new Template());
		
		return new DocumentTemplate(Arrays.asList(claimant));
	}
}
