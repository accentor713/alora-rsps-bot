package org.iyamjeremy.alorarspsbot.api;

public class RenderLocation {
	
	private int x;
	private int y;
	private long lastUpdated;
	
	public RenderLocation(int x, int y) {
		this.x = x;
		this.y = y;
		this.lastUpdated = System.currentTimeMillis();
	}
	
	public int getX() {
		return this.x;
	}
	
	public int getY() {
		return this.y;
	}
	
	public void setLocation(int x, int y) {
		this.x = x;
		this.y = y;
		this.lastUpdated = System.currentTimeMillis();
	}

	public boolean isActive() {
		return (System.currentTimeMillis() - this.lastUpdated) < 1000.0/10.0;
	}

}
