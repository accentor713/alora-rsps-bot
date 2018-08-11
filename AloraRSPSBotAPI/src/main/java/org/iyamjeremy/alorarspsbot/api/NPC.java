package org.iyamjeremy.alorarspsbot.api;


public class NPC extends Entity {
	
	private NPCDefinition npcDefinition;
	
	public NPC(Object instance) {
		super(instance);
		this.npcDefinition = new NPCDefinition(Bot.util.getField("NPC_CLASS", "NPC_DEF", this.getInstance()));
	}
	
	public NPCDefinition getNPCDefinition() {
		return npcDefinition;
	}
	
	public boolean isNull() {
		return this.getNPCDefinition().getInstance() == null;
	}
	
	public String getName() {
		return this.getNPCDefinition().getName();
	}
	
	public String[] getOptions() {
		return this.getNPCDefinition().getOptions();
	}
	
	public int getId() {
		return this.getNPCDefinition().getId();
	}
	
	public int getCombatLevel() {
		return this.getNPCDefinition().getCombatLevel();
	}
	
	public int getIndex() {
		Object[] npcs = (Object[]) Bot.util.getField("LOCAL_NPC_CONTAINER_CLASS", "LOCAL_NPC_CONTAINER_FIELD", null);
		for (int i = 0; i < npcs.length; i++) {
			if (npcs[i] == this.getInstance()) {
				return i;
			}
		}
		return -1;
	}
	
	public void doAction(String action) {
		int i = 0;
		int j = 0;
		int k = (action.equalsIgnoreCase("Attack")) ? 16 : 4; // Examine is 1007
		long l = this.getIndex();
		String str1 = "";//"<col=ffff00>Man<col=00ff00> (level-2)";
		String str2 = action;
		Object[] args = new Object[]{i, j, k, l, str1, str2, 297, 244};
		Bot.util.callMethod("DO_ACTION_CLASS", "DO_ACTION_METHOD", new Class<?>[]{int.class, int.class, int.class, long.class, String.class, String.class, int.class, int.class}, null, args);
	}

}
