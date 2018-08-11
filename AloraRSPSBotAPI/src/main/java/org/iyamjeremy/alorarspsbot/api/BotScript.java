package org.iyamjeremy.alorarspsbot.api;


public abstract class BotScript implements Runnable {
		
	private final String[] args;
	
	public BotScript(String[] args) {
		this.args = args;
	}
	
	public final String[] getArgs() {
		return this.args;
	}

}
