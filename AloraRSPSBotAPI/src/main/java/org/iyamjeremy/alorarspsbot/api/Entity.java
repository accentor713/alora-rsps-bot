package org.iyamjeremy.alorarspsbot.api;


public class Entity extends InstanceWrapper {
	
	public Entity(Object instance) {
		super(instance);
	}
	
	public int getX() {
		return (int) Bot.util.getField("ENTITY_CLASS", "ENTITY_X", this.getInstance());
	}
	
	public int getY() {
		return (int) Bot.util.getField("ENTITY_CLASS", "ENTITY_Y", this.getInstance());
	}
	
	public int getHP() {
		return (int) Bot.util.getField("ENTITY_CLASS", "ENTITY_HP", this.getInstance());
	}
	
	public int getMaxHP() {
		return (int) Bot.util.getField("ENTITY_CLASS", "ENTITY_MAX_HP", this.getInstance());
	}

}
