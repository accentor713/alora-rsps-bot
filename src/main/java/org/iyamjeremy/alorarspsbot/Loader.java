package org.iyamjeremy.alorarspsbot;

import java.awt.GridBagLayout;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Loader {

	private JFrame frame;
	
	private List<Hook> hooks;
	
	public void start() {
		startUI();
		loadHookFile();
		ClientTransformer.transformClient(hooks);
		frame.dispose();
		ClientTransformer.startGame();
	}
	
	private void loadHookFile() {
		Scanner hookFileReader;
		try {
			hookFileReader = new Scanner(new URL("http://localhost:8080/hook-file.txt").openStream());
			//hookFileReader = new Scanner(new URL("https://rawgit.com/i-yam-jeremy/alora-rsps-bot/master/hook-file.txt").openStream());
			String hookFileContents = hookFileReader.useDelimiter("\\z").next();
			this.hooks = HookFileParser.parseFile(hookFileContents);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void startUI() {
		this.frame = new JFrame();
		this.frame.setSize(300, 50);
		this.frame.setUndecorated(true);
		
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		panel.add(new JLabel("Loading...."));
		
		this.frame.add(panel);
		
		this.frame.setLocationRelativeTo(null);
		this.frame.setVisible(true);
	}
	
}
