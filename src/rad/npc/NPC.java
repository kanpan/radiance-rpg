package rad.npc;

import javax.microedition.lcdui.game.Sprite;

import rad.zone.Zone;

public abstract class NPC {
	
	protected int x;
	protected int y;
	protected int npc_id;
	protected Zone zone;
	protected Sprite sprite;
	
	public NPC(int npc_id) {
		x=y=0;
		this.npc_id = npc_id;
	}
	
	public NPC(int npc_id, int x, int y) {
		this.x=x;
		this.y=y;
		this.npc_id = npc_id;
	}
	
	/** Gets sprite for this NPC. */
	public Sprite getSprite() {
		return sprite;
	}

	/** Returns NPC ID where IDs are numbered from top left
	 * down to bottom right */
	public int getID() {
		return npc_id;
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
	
	public static boolean isNPC(int TID) {
		if(TID == Zone.TID_GUARD_FRONT ||
			TID == Zone.TID_GUARD_BACK ||
			TID == Zone.TID_GUARD_LEFT ||
			TID == Zone.TID_OLDMAN)
			return true;
		return false;
	}
	
	public static boolean isGuardFront(int TID) {
		if(TID == Zone.TID_GUARD_FRONT)
			return true;
		return false;
	}
	
	public static boolean isGuardBack(int TID) {
		if(TID == Zone.TID_GUARD_BACK)
			return true;
		return false;
	}

	public static boolean isGuardLeft(int TID) {
		if(TID == Zone.TID_GUARD_LEFT)
			return true;
		return false;
	}
	
	public static boolean isOldMan(int TID) {
		if(TID == Zone.TID_OLDMAN)
			return true;
		return false;
	}
}
