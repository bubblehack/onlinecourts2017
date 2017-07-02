package ilt.playground;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.telegram.telegrambots.bots.DefaultAbsSender;

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
		
		template = new DocumentTemplate(dict.generateList(), dict.generateHelp());
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
			preamble = "";
			ItemHelp section = null;
			ItemHelp clause = null;
			for (Clause c : template.clauses) {
				for (ItemHelp help : template.help) {
					if (help.isSection) {
						section = help;
					}
					if (help.itemName.equals(c.name)) {
						clause = help;
						break;
					}
				}
				next = c.getNextDefenseQuestion(defenseAnswers, answers);
				if (next != null) {
					if (section != null) {
						preamble = "*Section: " + section.itemName + "*\n";
					}
					if (clause != null) {
						preamble = preamble + "*Clause: " + clause.itemName + "*\n";
					}
					if (section != null && !section.defenceShown) {
						preamble = preamble + section.defenceHelp + "\n";
						section.defenceShown = true;
					}
					if (clause != null && !clause.defenceShown) {
						preamble = preamble + clause.defenceHelp + "\n";
						clause.defenceShown = true;
					}
					preamble = preamble + "\n";
					return next;
				}
			}
			
			return Variable.simple("Defense complete!", "");
		} 
		
		
		
		if (result != null) {
			Variable v = new Variable();
			v.questionText = new Question(result);
			v.finalMessage = true;
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
		String section = null;
		String clause = null;
		for (ItemHelp help : helps) {
			if (help.isSection) {
				section = help.itemName;
			} else {
				clause = help.itemName;
			}
			if (!help.shown) {
				preamble = preamble + help.helpText + "\n";
				help.shown = true;
			}
		}
		if (clause != null) {
			preamble = "*Clause: " + clause + "*\n" + preamble;
		}
		if (section != null) {
			preamble = "*Section: " + section + "*\n" + preamble;
		}
	}

	public void print(String path) {
		InputStream resourceAsStream = DocumentEngine.class.getResourceAsStream("/ilt/playground/sample.html");
		Scanner s = new Scanner(resourceAsStream);
		String html = s.useDelimiter("\\Z").next();
		s.close();
		
		resourceAsStream = DocumentEngine.class.getResourceAsStream("/ilt/playground/header.html");
		s = new Scanner(resourceAsStream);
		String headerTemplate = s.useDelimiter("\\Z").next();
		s.close();
		

		resourceAsStream = DocumentEngine.class.getResourceAsStream("/ilt/playground/clause.html");
		s = new Scanner(resourceAsStream);
		String clauseTemplate = s.useDelimiter("\\Z").next();
		s.close();
		
//		System.err.print("ANSWERS");
//		for (Variable v : answers.keySet()) {
//			System.err.println(v.scope + " " + (v.questionText != null ? v.questionText.text : "") + " " + v.clause);
//		}
//		System.err.print("DEFENSE");
//		for (Variable v : defenseAnswers.keySet()) {
//			System.err.println(v.scope + " " + (v.questionText != null ? v.questionText.text : "") + " " + v.clause);
//		}
		
		html = html.replace("$court", "The High Court");
		html = html.replace("$claimant", answers.get(Variable.simple("What is your name?", "GLOBAL.Claimant.")));
		html = html.replace("$defendant", answers.get(Variable.simple("who do you want to bring a claim against?", "GLOBAL.Defendant.")));
		html = html.replace("$claimNumber", "1111654");
		
		StringBuffer contents = new StringBuffer();
		

		
		int nextNumber = 1;
		
		
		for (ItemHelp help : template.help) {
			if (help.isSection)
				contents.append(headerTemplate.replace("$headerText", help.itemName));
			else {
				
				Clause clause = null;
				for (Clause c : template.clauses) {
					if (c.name.equals(help.itemName)) {
						clause = c;
						break;
					}
				}
				
				
				String clauseText = clauseTemplate.replace("$number", "" + nextNumber).replace("$clause", clause.resolve(answers));

				String clauseClass = "not-admitted";
				
				if (clause.dispute != null) {
					String clauseResolution = clause.dispute.resolve(defenseAnswers);

					if (clauseResolution != null) {
						System.err.println("Clause resolution: " + clauseResolution);
						clauseText = clauseText.replace("$disputeStatus", clauseResolution);
						if (clauseResolution.equals("Admitted")) {
							clauseClass = "admitted";
						} else {
							clauseClass = "denied";
						}
					} else {
						clauseText = clauseText.replace("$disputeStatus", "todo");
					}
				} else {
					clauseText = clauseText.replace("$disputeStatus", "todo");
				}
				
				clauseText = clauseText.replace("$disputeClass", clauseClass);
				contents.append(clauseText);
				nextNumber++;
			}
		}
		
		html = html.replace("$contents", contents.toString());
		
		
		try {
			BufferedWriter writer = Files.newBufferedWriter(Paths.get(path));
			writer.write(html);
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
