package ilt.playground;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClauseParser {
	Scanner s;

	private Map<String, Clause> map = new HashMap<>();
	
	public ClauseParser(InputStream stream) {
		s = new Scanner(stream);
	}

	private String parseString() {
		s.skip("\\s+");
		String x = s.findWithinHorizon("`.*?`", 0);
		x = x.substring(1, x.length() - 1);
		return x;
	}

	public void parse() {

		String type = s.next();
		String name = s.next();
		Clause c = new Clause();
		c.name = name;

		if (type.equals("clause")) {
			for (String command = s.next(); !command.equals("end"); command = s.next()) {
				switch (command) {
				case "question": {
					c.globalQuestion = Variable.simple(parseString(), "");
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
		map.put(name, c);
	}
	
	public boolean hasNext() {
		return s.hasNext();
	}

	private Template parseTemplate() {
		Template t = new Template();
		t.templateString = parseString();
		for (String command = s.next(); !command.equals("end"); command = s.next()) {
			switch (command) {
			case "var": {
				t.replacements.put(s.next(), Variable.simple(parseString(), ""));
				break;
			}
			case "sub": {
				t.replacements.put(s.next(), Variable.clause(map.get(s.next()), ""));
				break;
			}
			}
		}
		
		return t;
	}
	
	

	public static void main(String[] args) {
		System.out.println(parseClauses(ClauseParser.class.getResourceAsStream("/ilt/playground/clauses.text")));
	}
	
	public static Map<String, Clause> parseClauses(InputStream in) {
		ClauseParser parser = new ClauseParser(in);
		while (parser.hasNext()) {
			parser.parse();
		}
		return parser.map;
	}
}
