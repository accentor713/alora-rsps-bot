package org.iyamjeremy.alorarspsbot.api;

public class GameObjectDefinition extends InstanceWrapper {
	
	public GameObjectDefinition(Object instance) {
		super(instance);
	}

	public String getName() {
		return (String) Bot.util.getField("GAME_OBJECT_DEF_CLASS", "GAME_OBJECT_DEF_NAME", this.getInstance());
	}
	
	public String[] getOptions() {
		return (String[]) Bot.util.getField("GAME_OBJECT_DEF_CLASS", "GAME_OBJECT_DEF_OPTIONS", this.getInstance());
	}
	
	public int getId() {
		return (int) Bot.util.getField("GAME_OBJECT_DEF_CLASS", "GAME_OBJECT_DEF_ID", this.getInstance());
	}

}
