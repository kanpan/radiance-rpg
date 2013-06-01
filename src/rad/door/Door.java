package rad.door;

import javax.microedition.lcdui.game.Sprite;

import rad.zone.Zone;

public abstract class Door {
	
	protected int x;
	protected int y;
	protected int door_id;
	protected Zone zone;
	protected Sprite sprite;
	
	public Door(int door_id) {
		x=y=0;
		this.door_id = door_id;
	}
	
	public Door(int door_id, int x, int y) {
		this.x=x;
		this.y=y;
		this.door_id = door_id;
	}
	
	/** Gets sprite for this door. */
	public Sprite getSprite() {
		return sprite;
	}

	/** Returns door ID where IDs are numbered from top left
	 * down to bottom right */
	public int getID() {
		return door_id;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
	public void setZone(Zone z) {
		zone = z;
	}
	
	public Zone getZone() {
		return zone;
	}
	
	/**
	 * Gets X position
	 * @return X position
	 */
	public int getX() {
		return x;
	}

	/**
	 * Gets Y position
	 * @return Y position
	 */
	public int getY() {
		return y;
	}
	
	public void setPosition() {
		sprite.setPosition(x, y);
	}
	
	public void setPosition(int x, int y) {
		setX(x);
		setY(y);
	}
	
	public static boolean isDoor(int TID) {
		if(TID == Zone.TID_ENTRANCE_SIDE ||
			TID == Zone.TID_ENTRANCE_FRONT ||
			TID == Zone.TID_DOOR_TOWN)
			return true;
		return false;
	}
	
	public static boolean isTown(int TID) {
		if(TID == Zone.TID_DOOR_TOWN)
			return true;
		return false;
	}
	
	public static boolean isDungeonSide(int TID) {
		if(TID == Zone.TID_ENTRANCE_SIDE)
			return true;
		return false;
	}

	public static boolean isDungeonFront(int TID) {
		if(TID == Zone.TID_ENTRANCE_FRONT)
			return true;
		return false;
	}
}
