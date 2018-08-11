package org.iyamjeremy.alorarspsbot;


import org.iyamjeremy.alorarspsbot.api.Bot;
import org.iyamjeremy.alorarspsbot.api.BotScript;
import org.iyamjeremy.alorarspsbot.api.BotScriptMetadata;
import org.iyamjeremy.alorarspsbot.api.NPC;
import org.iyamjeremy.alorarspsbot.api.Player;

public class Main extends BotScript {
	
	private static int[] BANKER_IDS = {395, 400};
	private static int[] MONSTER_IDS = {3271, 3272};
	
	public static BotScriptMetadata metadata = new BotScriptMetadata("TestBot", "i-yam-jeremy", "A simple test bot", "");

	public Main(String[] args) {
		super(args);
	}

	@Override
	public void run() {
		Player localPlayer = Bot.getLocalPlayer();
		while (true) {
			NPC npc = Bot.findNearestNPC(MONSTER_IDS);
			if (npc != null) {
				npc.doAction("Attack");
				while (npc.getHP() > 0) {
					if (localPlayer.getHP() < 10) {
						bank();
						return;
					}
					Bot.sleep(600);
				}
			}
			Bot.sleep(3000);
		}
	}

	private void bank() {
		NPC banker = Bot.findNearestNPC(BANKER_IDS);
		if (banker != null) {
			banker.doAction("Bank");
		}
		else {
			Bot.log("<col=ff0000>Could not find a banker");
		}
	}
    
}
