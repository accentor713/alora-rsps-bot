package org.iyamjeremy.alorarspsbot.api;


public class NPCDefinition extends InstanceWrapper {

	public NPCDefinition(Object instance) {
		super(instance);
	}
	
	public String getName() {
		return (String) Bot.util.getField("NPC_DEF_CLASS", "NPC_DEF_NAME", this.getInstance());
	}
	
	public String[] getOptions() {
		return (String[]) Bot.util.getField("NPC_DEF_CLASS", "NPC_DEF_OPTIONS", this.getInstance());
	}
	
	public int getId() {
		return (int) Bot.util.getField("NPC_DEF_CLASS", "NPC_DEF_ID", this.getInstance());
	}
	
	public int getCombatLevel() {
		return (int) Bot.util.getField("NPC_DEF_CLASS", "NPC_DEF_COMBAT_LEVEL", this.getInstance());
	}
	
}
