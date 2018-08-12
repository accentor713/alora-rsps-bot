package org.iyamjeremy.alorarspsbot.api;


import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Bot {
	
	public static Util util = new Util();
	
	static {
		Hook[] hooks = HookFileParser.parse(Bot.class.getClassLoader().getResourceAsStream("hook-file.txt"));
		for (Hook hook : hooks) {
			util.addHook(hook.getName(), hook.getValue());
		}
	}

	private static Thread botThread;
	private static BotScript currentScript;

	private static HashMap<String, Constructor<? extends BotScript>> botScripts = new HashMap<>();
	
	private static HashMap<String, List<Object>> trackedInstances = new HashMap<>();
	public static void trackInstance(String className, Object instance) {
		if (!trackedInstances.containsKey(className)) {
			trackedInstances.put(className, new ArrayList<>());
		}
		trackedInstances.get(className).add(instance);
	}
	
	private static boolean loadBot(String path, String scriptClass) {
		try {
			ClassLoader loader = URLClassLoader.newInstance(
					new URL[]{new File(path).toURI().toURL()},
					Bot.class.getClassLoader()
					);
			Class<?> clazz = Class.forName(scriptClass, true, loader);
			Class<? extends BotScript> botScript = clazz.asSubclass(BotScript.class);
			BotScriptMetadata metadata = (BotScriptMetadata) botScript.getDeclaredField("metadata").get(null);
			Constructor<? extends BotScript> constructor = botScript.getConstructor(new Class<?>[]{String[].class});
			botScripts.put(metadata.getName(), constructor);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static boolean runCommand(String command) {
		if (command.startsWith(";;")) {
			command = "::" + command.substring(";;".length());
		}
		
		if (command.startsWith("::")) {
			String[] args = command.substring("::".length()).split(" ");
			String cmdName = args[0];
			switch (cmdName) {
				case "test":
					for (String className : trackedInstances.keySet()) {
						System.out.println(className + ": " + trackedInstances.get(className).size());
					}
					break;
				case "npcids":
					for (NPC npc : Bot.getNPCs()) {
						if (!npc.isNull()) {
							Bot.log(npc.getName() + ": " + npc.getId());
						}
					}
					break;
				case "load":
					if (Bot.loadBot(args[1], args[2])) {
						Bot.log("Loaded script successfully");
					}
					else {
						Bot.log("Error: could not load script");
					}
					break;
				case "atk":
					try {
					NPC npc = Bot.findNearestNPC(new int[]{Integer.parseInt(args[1])});
					if (npc != null) {
						npc.doAction("Attack");
					}
					} catch (Exception e) { e.printStackTrace(); }
					break;
				case "scripts":
					Bot.log("Script Count: " + botScripts.size());
					for (String name : botScripts.keySet()) {
						Bot.log(name);
					}
					break;
				case "start":
					String name = args[1];
					if (botScripts.containsKey(name)) {
						String[] botArgs = new String[args.length-1];
						System.arraycopy(args, 1, botArgs, 0, botArgs.length);
						BotScript bot;
						try {
							bot = botScripts.get(name).newInstance(new Object[]{args});
							Bot.startBot(bot);
						} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
								| InvocationTargetException e) {
							e.printStackTrace();
							Bot.log("Error: could not start bot");
						}
					}
					else {
						Bot.log("Error: could not find bot " + name);
					}
					break;
				case "stop":
					Bot.stopBot();
					break;
				case "current_script":
					if (currentScript != null) {
						BotScriptMetadata metadata;
						try {
							metadata = (BotScriptMetadata) currentScript.getClass().getDeclaredField("metadata").get(null);
							Bot.log(metadata.getName());
						} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException
								| SecurityException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					else {
						Bot.log("No script currently running");
					}
					break;
				case "ss":
					for (int i = 0; i < (int)Bot.util.getField("TEST1", "TEST2", null); i++) {
						long l3 = ((long[])Bot.util.getField("TEST3", "TEST4", null))[i];
						int j = (int)l3 & 0x7F;
						int i3 = ((int)l3 & 0x77C3CCF2) >> 29;
						int i4 = (int)(l3 >>> 32) & 0x7FFFFFFF;
						int i5 = 0x7F & (int)l3 >> 7;
						Bot.log(j + ", " + i3 + ", " + i4 + ", " + i5);
						Object gameObjectDef = Bot.util.callMethod("TEST5", "TEST6", new Class<?>[]{int.class}, null, new Object[]{i4});
						String gameObjectName = (String) Bot.util.getField("GAME_OBJECT_DEF_CLASS", "GAME_OBJECT_DEF_NAME", gameObjectDef);
						Bot.log(gameObjectName);
					}
					break;
				case "dd":
					GroundTile[][][] tiles = GroundTile.getTiles();
					for (int x = 0; x < tiles.length; x++) { // TODO check order of x, y, z for array
						for (int y = 0; y < tiles[x].length; y++) {
							for (int z = 0; z < tiles[x][y].length; z++) {
								GroundTile tile = tiles[x][y][z];
								//if (tile.getObjectId() != 0) {
									System.out.println("(" + x + "," + y + "," + z + "):(" + tile.getX() + "," + tile.getY() + "," + tile.getZ() + "): " + tile.getObjectId());
								//}
							}
						}
					}
					/*List<Object> gameObjectInstances = (List<Object>) Bot.util.getField("GAME_OBJECT_CLASS", "GAME_OBJECT_INSTANCES", null);
					for (Object obj : gameObjectInstances) {
						GameObject gameObject = new GameObject(obj);
						Bot.log(gameObject.getName() + ": " + gameObject.getId());
					}*/
//					Object list = Bot.util.getField("TEST7", "TEST8", null);
//					Object startObject = Bot.util.callMethod("TEST9", "TEST10", new Class<?>[]{}, list, new Object[]{});
//					for (Object localGameObject = startObject/*(GameObject)Class3_Sub13_Sub6.G.D()*/; localGameObject != null; localGameObject = Bot.util.callMethod("TEST9", "TEST11", new Class<?>[]{}, list, new Object[]{})/*(GameObject)Class3_Sub13_Sub6.G.B()*/) {
//						System.out.println("-----------------------");
//						try {
//						for (Field f : Bot.util.findClass("GAME_OBJECT_CLASS").getDeclaredFields()) {
//							f.setAccessible(true);
//							if (!Modifier.isStatic(f.getModifiers())) {
//								try {
//								System.out.println(f.getName() + ": " + f.get(localGameObject));
//								} catch (Exception e) {}
//							}
//						}
//						System.out.println("-----------------------");
//						} catch (Exception e) { e.printStackTrace(); }
//					}
					break;
				default:
					Bot.log("Unrecognized command " + cmdName);
					break;
			}
		}
		return false;
	}
	
	public static void startBot(BotScript bot) {
		stopBot();
		currentScript = bot;
		botThread = new Thread(bot);
		botThread.start();
	}
	
	public static void stopBot() {
		if (botThread != null) {
			botThread.interrupt();
			botThread = null;
			currentScript = null;
		}
	}
	
	public static NPC[] getNPCs() {
		Object[] localNpcArray = (Object[]) Bot.util.getField("LOCAL_NPC_CONTAINER_CLASS", "LOCAL_NPC_CONTAINER_FIELD", null);
		List<NPC> npcs = new ArrayList<>();
		
		for (Object obj : localNpcArray) {
			if (obj != null) {
				NPC npc = (NPC)new NPC(obj);
				if (!npc.isNull()) {
					npcs.add(npc);
				}
			}
		}
		
		return npcs.toArray(new NPC[npcs.size()]);
	}
	
	public static void log(String s) {
		Bot.util.callMethod("CHAT_LOG_MESSAGE_CLASS", "CHAT_LOG_MESSAGE_METHOD", new Class<?>[]{String.class, String.class, int.class}, null, new Object[]{null, s, 0});
	}
	
	public static NPC findNearestNPC(int[] ids) {
		NPC nearest = null;
		float nearestDistance = Float.MAX_VALUE;
		Player localPlayer = Bot.getLocalPlayer();
		NPC[] npcs = Bot.getNPCs();
		for (NPC npc : npcs) {
			for (int j = 0; j < ids.length; j++) {
				int id = ids[j];
				if (npc.getId() == id) {
					int dx = npc.getX() - localPlayer.getX();
					int dy = npc.getY() - localPlayer.getY();
					float distance = (float)Math.sqrt((double)(dx*dx + dy*dy));
					if (distance < nearestDistance) {
						nearest = npc;
						nearestDistance = distance;
					}
				}
			}
		}
		return nearest;
	}

	public static Player getLocalPlayer() {
		return new Player(Bot.util.getField("LOCAL_PLAYER_CLASS", "LOCAL_PLAYER_FIELD", null));
	}

	public static void sleep(long ms) {
		try {
			Thread.sleep((long) (ms + 0.25*Math.random()*ms));
		} catch (InterruptedException e) {
			// do nothing, if interrupted it means the bot was supposed to stop
		}
	}
	
}
