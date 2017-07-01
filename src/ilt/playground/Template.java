package ilt.playground;

import java.util.HashMap;
import java.util.Map;

public class Template {

	public String templateString;
	public Map<String, Variable> localReplacements = new HashMap<>();
	public Map<String, Variable> globalReplacements = new HashMap<>();
}
