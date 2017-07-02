package ilt.playground;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.sun.research.ws.wadl.Doc;

import ilt.hackathon2017.ILTBot;

public class DocumentEngine {
	
	public boolean locked;

	public int currentClause;
	
	public DocumentTemplate template;
	public Map<Variable, String> answers = new HashMap<>();
	public Map<Variable, String> defenseAnswers = new HashMap<>();
	
	List<Variable> openQuestions;
	String result = null;
	public String preamble = "";
	boolean defense = false;
	
	public DocumentEngine() {
		reset();
	}
	
	public void reset() {
		ClauseDictionary dict = new ClauseDictionary();
		try {
			dict.update(new FileInputStream("/private/tmp/clauses.rtf"));
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		dict.update(ILTBot.class.getResourceAsStream("/ilt/playground/clauses.text"));
		
		template = new DocumentTemplate(dict.generateList());
		answers = new HashMap<>();
		defenseAnswers = new HashMap<>();
		locked = false;
		defense = false;
	}
	
	public void save(String name) {
		Case c = new Case(name, template.clauses, answers);
		c.save();
		reset();
	}
	
	public Variable getNextQuestion() {
		if (defense) {
			Variable next = null;
			
			for (Clause c : template.clauses) {
				next = c.getNextDefenseQuestion(defenseAnswers, answers);
				if (next != null)
					return next;
			}
			
			return Variable.simple("Defense complete!", "");
		} 
		
		
		
		if (result != null) {
			Variable v = new Variable();
			v.questionText = new Question(result);
			return v;
		} else if (locked) {
			return null;
		}
		refresh();
		return openQuestions.get(0);
	}
	
	public void acceptAnswer(Variable question, String answer) {
		if (answer.startsWith("/")) {
			if (answer.equals("/reset")) {
				reset();
			} else if (answer.startsWith("/save")) {
				save(answer.split("\\s+")[1]);
			} else if (answer.startsWith("/load")) {
				Case load = Case.load(answer.split("\\s+")[1]);
				answers = load.answers;
				template.clauses = load.clauses;
				locked = true;
			} else if (answer.startsWith("/continue")) {
				locked = false;
			} else if (answer.startsWith("/defend")) {
				defense = true;
				currentClause = 0;
			}
		} else {
			Map<Variable, String> map = defense ? defenseAnswers : answers;
			map.put(question.canonical(), answer);
		}
		result = template.resolve(answers);
	}
	
	public void refresh() {
		preamble = "";
		List<ItemHelp> helps = new ArrayList<>();
		openQuestions = template.getOpenQuestions(answers, helps);
		for (ItemHelp help : helps) {
			if (!help.shown) {
				preamble = preamble + help.helpText + "\n";
				help.shown = true;
			}
		}
	}

	public void print(String path) {
		InputStream resourceAsStream = DocumentEngine.class.getResourceAsStream("/ilt/playground/sample.html");
		Scanner s = new Scanner(resourceAsStream);
		String template = s.useDelimiter("\\Z").next();
		s.close();
		
		for (Variable v : answers.keySet()) {
			System.err.println(v.scope + " " + v.questionText + " " + v.clause);
		}
		
		template = template.replace("$court", "The High Court");
		Variable claimantName = Variable.simple("What is your name?", "GLOBAL.Claimant.");
		template = template.replace("$claimant", answers.get(claimantName));

		
		try {
			BufferedWriter writer = Files.newBufferedWriter(Paths.get(path));
			writer.write(template);
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
