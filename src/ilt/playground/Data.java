package ilt.playground;

import java.util.HashSet;
import java.util.Set;

public class Data {

	public static DocumentTemplate doc() {

		ClauseDictionary dict = new ClauseDictionary();
		dict.update(Data.class.getResourceAsStream("clauses.text"));
		
		
		Set<String> rootClauses = new HashSet<>();
		rootClauses.add("ContractClaimant");
		
		return new DocumentTemplate(dict.generateList(rootClauses));
	}
}
