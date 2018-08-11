package org.iyamjeremy.alorarspsbot.api;

public class BotScriptMetadata {
	
	private String name;
	private String author;
	private String desc;
	private String repoUrl;
	
	public BotScriptMetadata(String name, String author, String desc, String repoUrl) {
		this.name = name;
		this.author = author;
		this.desc = desc;
		this.repoUrl = repoUrl;
	}
	
	public String getName() {
		return name;
	}
	
	public String getAuthor() {
		return author;
	}
	
	public String getDesc() {
		return desc;
	}
	
	public String getRepoUrl() {
		return repoUrl;
	}
	

	
}
