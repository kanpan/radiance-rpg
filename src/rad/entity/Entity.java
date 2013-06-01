package rad.entity;

import javax.microedition.lcdui.game.Sprite;
import rad.zone.Zone;

/**
 * This class is the base class for agents like Grace, monsta, etc.
 * @author Ron
 *
 */
public abstract class Entity {
	protected static int code = 0;
	
	/** Heading is up. */
	public final static int GO_UP = code++;
	
	/** Heading is down. */	
	public final static int GO_DOWN = code++;
	
	/** Heading is left. */	
	public final static int GO_LEFT = code++;
	
	/** Heading is right. */	
	public final static int GO_RIGHT = code++;
	
	/** Default heading */
	protected int dir = GO_DOWN;
	
	/** X coordinate of agent */
	protected int x;
	
	/** Y coordinate of agent */
	protected int y;
	
	/** X speed of agent */
	protected int xSpeed;
	
	/** Y speed of agent */
	protected int ySpeed;
	
	/** Agent has move pending if trues */
	protected boolean movePending;
	
	/** Agent can move if true, otherwise it is blockeds */
	protected boolean freeFlying;
	
	/** Agent is PC if trues */
	protected boolean pc;
	
	/** Agent is on this levels */
	protected Zone zone;
	
	/** Gets sprite for this agent. */
	abstract public Sprite getSprite(); 
	
	/** Invoked when agent collides with wall */
	abstract public void collideEvent();
	
	/** Transfer x,y into sprite */
	abstract public void setPosition();
	
	/** Returns RID for entity */
	abstract public int getID();
	
	/** Constructor */
	protected Entity() {
		movePending = freeFlying = false;
		pc = false;
	}
	
	/** Moves agent conditionally */
	public void moveOnCondition() {	
	    // Get person w, h to align on tile boundary
	    int tilew = Zone.ZONE_TILE_WIDTH;
	    int tileh = Zone.ZONE_TILE_HEIGHT;

	    boolean moveToTile = ((freeFlying == true) && (x % tilew != 0 || y % tileh != 0));

	    if(moveToTile || movePending)
	    {
	        move(xSpeed,ySpeed);
	        
	        // Ask level if we collided...
	        if(zone.collide(this) == true) {
	        	// If so move back and tell agent we collided
	            move(-xSpeed,-ySpeed);
	            collideEvent();
	        }
	        else
	            freeFlying = true;
	    }

	    return;		
	}

	/**
	 * Moves agent relatively.
	 * @param xInc X increment
	 * @param yInc Y increment
	 */
	public void move(int xInc,int yInc) {
		if(dir == GO_UP)
            y -= yInc;
		
		else if(dir == GO_DOWN)
            y += yInc;
		
		else if(dir == GO_LEFT)
            x -= xInc;
		
		else if(dir == GO_RIGHT)
            x += xInc;
		
	    setPosition();

	    return;		
	}
	
	/**
	 * Gets agent's direction.
	 * @return Direction
	 */
	public int getDirection() {
		return dir;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public void setY(int y) {
		this.y = y;
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
	
	/** Sets direction heading.
	 * @param dir Heading
	 */
	public void setDirection(int dir) {
		this.dir = dir;
		
	}
	
	/**
	 * Sets the level this agent is on.
	 * @param level Level
	 */
	public void setZone(Zone z) {
		this.zone = z;
	}
	
	/**
	 * Tests if this agent is the PC (or Grace).
	 * @return True if agent is PC.
	 */
	public boolean isPC() {
		return pc;
	}
	
	/**
	 * Tests if the tile id is the PC (or Jodav).
	 * @param rid TID
	 * @return True if the tid is the PC.
	 */
	/*public static boolean isPC(int tid) {
		return tid == Zone.TID_JODAV;
	}*/
	
	/**
	 * Tests if the rid is an enemy.
	 * @param rid RID
	 * @return True if the rid is an enemy.
	 */
	public static boolean isEnemy(int rid) {
		/*if(isMonsta(rid)           ||
		   rid == Entity.RID_HGHOST ||
		   rid == Entity.RID_VGHOST ||
		   rid == Entity.RID_SPIDA)
			return true;*/
		
		return false;
	}
}
