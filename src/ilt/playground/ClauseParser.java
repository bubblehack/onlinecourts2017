package ilt.playground;

import java.awt.event.ActionEvent;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

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
		
		Set<String> toFind = new HashSet<>();
		
		Pattern p = Pattern.compile("\\$\\w+");
		for (Matcher matcher = p.matcher(t.templateString); matcher.find(); ) {
			toFind.add(matcher.group().substring(1));
		}
		
		for (String key : t.replacements.keySet()) {
			if (!toFind.remove(key)) {
				JOptionPane.showMessageDialog(null, "var/sub $" + key + " not found in template: " + t.templateString);
			}
		}
		
		for (String unfound : toFind) {
			JOptionPane.showMessageDialog(null, "No var/sub for $" + unfound + " in template: " + t.templateString);
		}
		
		System.err.println(toFind);
		
		
		return t;
		
	}
	
	

	public static void main(String[] args) {
		
		final JDialog frame = new JDialog();
		frame.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		frame.setModal(true);
		frame.setSize(500, 500);
		frame.setLocationRelativeTo(null);
		
		final JFileChooser chooser = new JFileChooser();
		
		JButton loadFile = new JButton(new AbstractAction("Load") {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (chooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
					System.err.println(chooser.getSelectedFile());
					
					try {
					FileInputStream in = new FileInputStream(chooser.getSelectedFile());
					
					parseClauses(in);
					in.close();
					
					} catch (IOException ex) {
						JOptionPane.showMessageDialog(frame, "Error loading file: " + ex.getMessage());
					}
				}
			}
		});
		
		frame.getContentPane().add(loadFile);
		
		frame.setVisible(true);
		
	}
	
	public static Map<String, Clause> parseClauses(InputStream in) {
		ClauseParser parser = new ClauseParser(in);
		while (parser.hasNext()) {
			parser.parse();
		}
		return parser.map;
	}
}
