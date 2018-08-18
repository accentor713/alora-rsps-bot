package org.iyamjeremy.alorarspsbot.api;


public class Player extends Entity {

	public Player(Object instance) {
		super(instance);
	}
	
	public String getName() {
		return (String) Bot.util.callMethod("PLAYER_CLASS", "PLAYER_NAME", new Class<?>[]{int.class}, this.getInstance(), new Object[]{0});
	}
	
	public int getAnimation() {
		return (int) Bot.util.callMethod("PLAYER_CLASS", "PLAYER_GET_ANIMATION_METHOD", new Class<?>[]{}, this.getInstance(), new Object[]{});
	}

}
