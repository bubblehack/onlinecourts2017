package ilt.playground;

import java.awt.event.ActionEvent;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
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

	public Map<String, Clause> map = new HashMap<>();
	public List<String> rootClauses = new ArrayList<>();

	public ClauseParser(InputStream stream) {
		s = new Scanner(stream);
	}

	private String parseString() {
		s.skip("\\s+");
		String x = s.findWithinHorizon("`.*?`", 0);
		if (x == null) {
			JOptionPane.showMessageDialog(null, "Mangled string starting: " + s.next());
		}
		x = x.substring(1, x.length() - 1);
		return x;
	}

	public void parse() {

		String type = s.next();

		if (type.equals("clause")) {

			String name = s.next();
			Clause c = new Clause();
			c.name = name;

			System.err.println("Parsing clause: " + c.name);
			
			for (String command = s.next(); !command.equals("end"); command = s.next().toLowerCase()) {
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
			map.put(name, c);
		} else if (type.equals("document")) {
			System.err.println("document!!!");
			for (String command = s.next().toLowerCase(); !command.equals("end"); command = s.next().toLowerCase()) {
				System.err.println("command: " + command);
				switch (command) {
				case "clause":
					rootClauses.add(s.next());
					break;
				}
			}
		}
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
				String varName = s.next();
				String subName = s.next();
				Clause sub = map.get(subName);
				if (sub == null) {
					JOptionPane.showMessageDialog(null, "Could not find clause with name: " + subName);
				}
				t.replacements.put(varName, Variable.clause(sub, ""));
				break;
			}
			}
		}

		Set<String> toFind = new HashSet<>();

		Pattern p = Pattern.compile("\\$\\w+");
		for (Matcher matcher = p.matcher(t.templateString); matcher.find();) {
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


		return t;

	}

	/*
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
	*/

	public static ClauseParser parseClauses(InputStream in) {
		ClauseParser parser = new ClauseParser(in);
		while (parser.hasNext()) {
			parser.parse();
		}
		return parser;
	}
}
