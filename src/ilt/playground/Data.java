package ilt.playground;

import java.util.Arrays;

public class Data {

	public static DocumentTemplate doc() {
		
		Template employmentEmployed = new Template();
		employmentEmployed.templateString = "is employed at $employer";
		employmentEmployed.replacements.put("employer", Variable.simple("Where are you employed?", "claimant.employment"));

		Template employmentSelfEmployed = new Template();
		employmentSelfEmployed.templateString = "is self employed";

		Template employmentUnemployed = new Template();
		employmentUnemployed.templateString = "is unemployed";
		
		Clause employmentClause = new Clause();
		employmentClause.globalQuestion = Variable.simple("What is your employment status?", "claimant.employment");
		employmentClause.templates.put("self-employed", employmentSelfEmployed);
		employmentClause.templates.put("unemployed", employmentUnemployed);
		employmentClause.templates.put("employed", employmentEmployed);
		
		Template contractClaimantTemplate = new Template();
		contractClaimantTemplate.templateString = "claimant: $name at $address @Employment";
		contractClaimantTemplate.replacements.put("name", Variable.simple("What is your name?", "claimant"));
		contractClaimantTemplate.replacements.put("address", Variable.simple("What is your address?", "claimant"));
		contractClaimantTemplate.replacements.put("Employment", Variable.clause(employmentClause, "claimant"));
		
		Clause claimant = new Clause();
		claimant.globalQuestion = null;
		claimant.templates.put("", contractClaimantTemplate);
		
		return new DocumentTemplate(Arrays.asList(claimant));
	}
}
