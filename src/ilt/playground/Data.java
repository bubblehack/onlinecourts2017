package ilt.playground;

import java.util.ArrayList;
import java.util.List;

public class Data {

	public static DocumentTemplate doc() {

		ClauseDictionary dict = new ClauseDictionary();
		dict.update(Data.class.getResourceAsStream("clauses.text"));
		
		
		List<String> rootClauses = new ArrayList<>();
		//rootClauses.add("ContractClaimant");
		rootClauses.add("Loss");
		
		return new DocumentTemplate(dict.generateList(rootClauses));
	}
}
