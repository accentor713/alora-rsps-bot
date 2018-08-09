package org.iyamjeremy.alorarspsbot;

import java.util.ArrayList;
import java.util.List;

public class HookFileParser {

	public static List<Hook> parseFile(String contents) {
		List<Hook> hooks = new ArrayList<Hook>();
		
		String[] currentAttributes = null;
		String currentSourceBody = null;
		for (String line : contents.split("\n")) {
			if (line.startsWith("::")) {
				if (currentAttributes != null && currentSourceBody != null) {
					hooks.add(new Hook(currentAttributes, currentSourceBody));
				}
				currentAttributes = line.substring("::".length()).split(":");
				currentSourceBody = "";
			}
			else {
				currentSourceBody += line + "\n";
			}
		}
		if (currentAttributes != null && currentSourceBody != null) {
			hooks.add(new Hook(currentAttributes, currentSourceBody));
		}
		
		return hooks;
	}

}
