package org.iyamjeremy.alorarspsbot.api;


public class Player extends Entity {

	public Player(Object instance) {
		super(instance);
	}
	
	public String getName() {
		return (String) Bot.util.callMethod("PLAYER_CLASS", "PLAYER_NAME", new Class<?>[]{int.class}, this.getInstance(), new Object[]{0});
	}

}
