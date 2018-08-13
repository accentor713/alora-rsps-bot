package org.iyamjeremy.alorarspsbot.api;


public class GameObject {
	
	private GameObjectDefinition definition;
	private long hash;
	
	public GameObject(long hash) {
		this.hash = hash;
		this.definition = new GameObjectDefinition(Bot.util.callMethod("GAME_OBJECT_DEF_CLASS", "GAME_OBJECT_DEF_LOAD_BY_ID", new Class<?>[]{int.class}, null, new Object[]{this.getId()})); 
	}
	
	public GameObjectDefinition getDefinition() {
		return this.definition;
	}
	
	public boolean isNull() {
		return this.getDefinition().getInstance() == null;
	}
	
	public String getName() {
		return this.getDefinition().getName();
	}
	
	public String[] getOptions() {
		return this.getDefinition().getOptions();
	}
	
	public long getHash() {
		return this.hash;
	}
	
	public int getX() {
		return (int) (this.hash & 0x7F);
	}
	
	public int getY() {
		return (int) ((this.hash >> 7) & 0x7F);
	}
	
	public int getId() {
		return (int) (this.hash >>> 32 & (0x7FFFFFFF));
	}
	
	public void doAction(String action) {
		
		int optionIndex = -1;
		for (int i = 0; i < this.getOptions().length; i++) {
			if (this.getOptions()[i] != null && this.getOptions()[i].equals(action)) {
				optionIndex = i;
			}
		}
		if (optionIndex == -1) {
			throw new RuntimeException("Couldn't find action " + action);
		}
		short actionNumber = 0;
        if (optionIndex == 0)
          actionNumber = 42;
        if (-2 == (optionIndex ^ 0xFFFFFFFF))
          actionNumber = 50;
        int i10 = -1;
        if (2 == optionIndex)
          actionNumber = 49;
        /*if (localEG.E == optionIndex)
          i10 = localEG.d;*/
        if (-4 == (optionIndex ^ 0xFFFFFFFF))
          actionNumber = 46;
        /*if (optionIndex == localEG.s)
          i10 = localEG.e;*/
        if (-5 == (optionIndex ^ 0xFFFFFFFF))
          actionNumber = 1001;
        
		Object[] args = new Object[]{this.getX(), this.getY(), actionNumber, this.getHash(), "", action, 297, 244};
		Bot.util.callMethod("DO_ACTION_CLASS", "DO_ACTION_METHOD", new Class<?>[]{int.class, int.class, int.class, long.class, String.class, String.class, int.class, int.class}, null, args);
	}

}
