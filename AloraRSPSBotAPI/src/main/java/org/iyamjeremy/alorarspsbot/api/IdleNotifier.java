package org.iyamjeremy.alorarspsbot.api;

public class IdleNotifier {
	
	public static final String SETTING_NAME = "idle-notifier";
	
	private static long timeAtLastNonIdleAnimation = 0L;
	
	public static void tick() {
		int id = Bot.getLocalPlayer().getAnimation();
		if (id != -193 && id != -194 && id != -195) {
			timeAtLastNonIdleAnimation = System.currentTimeMillis();
		}
	}
	
	public static boolean isIdle() {
		return (System.currentTimeMillis() - timeAtLastNonIdleAnimation) > 3000L;
	}

}
