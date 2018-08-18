package org.iyamjeremy.alorarspsbot.api;

import java.util.HashMap;

public class Settings {
	
	private static HashMap<String, Boolean> settings = new HashMap<>();
	
	public static void set(String name, boolean value) {
		settings.put(name, value);
	}
	
	public static boolean get(String name) {
		return settings.containsKey(name) && settings.get(name);
	}

}
