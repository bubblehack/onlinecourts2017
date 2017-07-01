package ilt.playground;

import java.io.InputStream;
import java.util.Scanner;
import java.util.regex.Pattern;

public class ClauseParser {
	Scanner s;
	public ClauseParser(InputStream stream) {
		s = new Scanner(stream);
	}
	
	private String parseString() {
		s.skip("\\s+");
		Pattern delimiter = s.delimiter();
		s.useDelimiter("`");
		String x = s.next();
		s.useDelimiter(delimiter);
		System.exit(0);
		s.skip("\\s+");
		return x;
	}
	
	
		
	public Clause parse() {

		String type = s.next();
		Clause c = new Clause();
		
		
		if (type.equals("clause")) {
			for (String command = s.next(); !command.equals("end"); command = s.next()) {
				switch (command) {
				case "question": {
					c.globalQuestion = new Question(parseString());
					break;
				}
				case "template": {
					c.templates.put("", parseTemplate());
					break;
				}
				case "if": {
					c.templates.put(parseString(), parseTemplate());
					break;
				}
				}
			}
		}
		return null;
	}

	
	private Template parseTemplate() {
		Template t = new Template();
		t.templateString = parseString();
		for (String command = s.next(); !command.equals("end"); command = s.next()) {
			switch (command) {
			case "var": {
				t.localReplacements.put(s.next(), Variable.simple(parseString()));
				break;
			}
			}
		}
		return t;
	}

	public static void main(String[] args) {
		ClauseParser parser = new ClauseParser(ClauseParser.class.getResourceAsStream("/ilt/playground/clauses.text"));
		parser.parse();
	}
}
