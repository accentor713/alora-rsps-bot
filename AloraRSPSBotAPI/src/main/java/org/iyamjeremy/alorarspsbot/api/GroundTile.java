package org.iyamjeremy.alorarspsbot.api;

public class GroundTile extends InstanceWrapper {

	public GroundTile(Object instance) {
		super(instance);
	}
	
	public int getX() {
		if (this.getInstance() != null) {
			return (int) Bot.util.getField("GROUND_TILE_CLASS", "GROUND_TILE_X", this.getInstance());
		}
		return -1;
	}
	
	public int getY() {
		if (this.getInstance() != null) {
			return (int) Bot.util.getField("GROUND_TILE_CLASS", "GROUND_TILE_Y", this.getInstance());
		}
		return -1;
	}
	
	public int getZ() {
		if (this.getInstance() != null) {
			return (int) Bot.util.getField("GROUND_TILE_CLASS", "GROUND_TILE_Z", this.getInstance());
		}
		return -1;
	}
	
	public int getObjectId() {
		Object obj = (Object) Bot.util.getField("GROUND_TILE_CLASS", "TEST12", this.getInstance());
		if (obj != null) {
			long l = (long) Bot.util.getField("TEST13", "TEST14", obj);
			//long l = (long) Bot.util.callMethod("GROUND_TILE_CLASS", "GROUND_TILE_GET_LONG_OBJECT_DESCRIPTOR", new Class<?>[]{int.class, int.class, int.class}, null, new Object[]{this.getX(), this.getY(), this.getZ()});
			return (int)(l >>> 32) & 0x7FFFFFFF;
		}
		else {
			return -1;
		}
	}
	
	public static GroundTile[][][] getTiles() {
		Object[][][] instances = (Object[][][]) Bot.util.getField("GROUND_TILE_ARRAY_CLASS", "GROUND_TILE_ARRAY_FIELD", null);
		GroundTile[][][] tiles = new GroundTile[instances.length][instances[0].length][instances[0][0].length];
		for (int z = 0; z < instances.length; z++) {
			for (int x = 0; x < instances[z].length; x++) {
				for (int y = 0; y < instances[z][x].length; y++) {
					tiles[z][x][y] = new GroundTile(instances[z][x][y]);
				}
			}
		}
		return tiles;
	}

}
