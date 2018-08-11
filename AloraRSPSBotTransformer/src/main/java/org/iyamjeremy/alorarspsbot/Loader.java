package org.iyamjeremy.alorarspsbot;

import java.awt.GridBagLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Loader {

	private JFrame frame;
	
	public void start() {
		startUI();
		ClientTransformer.transformClient();
		frame.dispose();
		ClientTransformer.startGame();
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
