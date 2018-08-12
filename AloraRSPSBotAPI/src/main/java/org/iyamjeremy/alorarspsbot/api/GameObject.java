package org.iyamjeremy.alorarspsbot.api;


public class GameObject extends InstanceWrapper {
	
	private GameObjectDefinition definition;
	
	public GameObject(Object instance) {
		super(instance);
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
	
	public int getId() {
		int id = (int) Bot.util.getField("GAME_OBJECT_CLASS", "GAME_OBJECT_ID_1", this.getInstance());
		if (id == -1) {
			id = (int) Bot.util.getField("GAME_OBJECT_CLASS", "GAME_OBJECT_ID_2", this.getInstance());
		}
		return id;
	}
	
	public void doAction(String action) {
		
		/*int optionIndex = -1;
		for (int i = 0; i < 5; this.getOptions().length; i++) {
			if (this.getOptions()[i].equals(action)) {
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
        if (localEG.E == optionIndex)
          i10 = localEG.d;
        if (-4 == (optionIndex ^ 0xFFFFFFFF))
          actionNumber = 46;
        if (optionIndex == localEG.s)
          i10 = localEG.e;
        if (-5 == (optionIndex ^ 0xFFFFFFFF))
          actionNumber = 1001;
		int i = 0;
		int j = 0;
		int k = (action.equalsIgnoreCase("Attack")) ? 16 : 4; // Examine is 1007
		long l = this.getIndex();
		String str1 = "";//"<col=ffff00>Man<col=00ff00> (level-2)";
		String str2 = action;
		Object[] args = new Object[]{i, j, k, l, str1, str2, 297, 244};
		Bot.util.callMethod("DO_ACTION_CLASS", "DO_ACTION_METHOD", new Class<?>[]{int.class, int.class, int.class, long.class, String.class, String.class, int.class, int.class}, null, args);
		*/
	}

}
