package org.iyamjeremy.alorarspsbot.api;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class HookFileParser {

	public static Hook[] parse(InputStream in) {
		List<Hook> hooks = new ArrayList<>();
		Scanner scanner = new Scanner(in);
		
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			String[] data = line.split("=");
			if (data.length == 2) {
				hooks.add(new Hook(data[0], data[1]));
			}
			else {
				System.err.println("Unparsed line: " + line);
			}
		}
		
		scanner.close();
		
		return hooks.toArray(new Hook[hooks.size()]);
	}

}
