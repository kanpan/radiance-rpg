package rad.entity;

import java.util.Vector;

import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.GameCanvas;
import javax.microedition.lcdui.game.Sprite;
import javax.microedition.lcdui.game.TiledLayer;

import rad.zone.Zone;
import rad.util.Point;
import rad.Game;

/**
 * This class manages most of Grace's functions.
 * @author Ron
 *
 */
public class Jodav extends Entity {
	/** Grace's motion speed in pixels per step */
	public final static int SPEED = 4;
	
	/** Directional sprites */
	protected Sprite[] sprites = new Sprite[4];
	
	/* Jodav's stats */
	protected static int currHP;
	protected static int currMP;
	protected static int currXP;
	protected static int currLv;
	protected static Vector currAbilities = new Vector();
	
	protected int maxHP;
	protected int maxMP;
	protected int maxXP;
	protected int maxLv;
	protected Vector availAbilities = new Vector();

	/** Constructor */
	public Jodav() {
		
		try {
			// Load in the sprites
			sprites[GO_UP] =
				new Sprite(Image.createImage("/jodav_up.png"),32,32);
			
			sprites[GO_DOWN] =
				new Sprite(Image.createImage("/jodav_down.png"),32,32);
				
			sprites[GO_LEFT] =
				new Sprite(Image.createImage("/jodav_left.png"),32,32);	
			
			sprites[GO_RIGHT] =
				new Sprite(Image.createImage("/jodav_right.png"),32,32);				
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		pc = true;
		currLv = 10;
		currHP = Stats.getMaxHP(currLv);
		currMP = Stats.getMaxMP(currLv);
		currXP = 0;
		String[] abil = Stats.getAvailAbilities(currLv);
		for(int i=0; i<abil.length; i++)
			availAbilities.addElement(abil[i]);
		
		currAbilities.addElement(Stats.RESTORE_1);
		currAbilities.addElement(Stats.THRUST_1);
		currAbilities.addElement(Stats.FROST_1);
		
		xSpeed = ySpeed = SPEED;
	}
	
	public void update(int input) {
		int oldX = x;
		int oldY = y;
		
		moveOnCondition();
		
		if(oldX != x || oldY != y) {
			nextFrame();
		}
		
		if(isMovement(input))
			movePending = freeFlying = true;
		else
			movePending = false;
	}
	
	public void collideEvent() {
		freeFlying = false;
	}

	/**
	 * Gets the current directional sprite.
	 * @return
	 */
	public Sprite getSprite() {
		return sprites[dir];
	}
	
	public void setPosition() {
		sprites[dir].setPosition(x, y);
	}
	
	/**
	 * Sets the position.
	 * @param x X coordinate
	 * @param y Y coordinate
	 */
	public void setPosition(int x,int y) {
		setX(x);
		setY(y);
		
		for(int j=0; j < sprites.length; j++)
			sprites[j].setPosition(x,y);
	}
		
	/**
	 * Sets jodav's direction.
	 * @param newdir Direction
	 */
	public void setDirection(int newdir) {
		int oldX = sprites[dir].getX();
		
		int oldY = sprites[dir].getY();
		
		sprites[dir].setVisible(false);
		
		sprites[newdir].setPosition(oldX,oldY);
		
		dir = newdir;
		
		sprites[dir].setVisible(true);
	}
	
	/** Advances jodav's frame	 */
	public void nextFrame() {
		sprites[dir].nextFrame();
	}
	
	/**
	 * Tests if jodav is colliding.
	 * @param sprite Other sprite
	 * @param pixelLevel Pixel level collision detection flag
	 * @return True if colliding, false otherwise
	 */
	public boolean collidesWith(Sprite sprite,boolean pixelLevel) {
		return sprites[dir].collidesWith(sprite, pixelLevel);
	}

	/**
	 * Tests if jodav is colliding
	 * @param tlayer Tiled layer
	 * @param pixelLevel Pixel level collision detection flag
	 * @return True if colliding, false otherwise
	 */
	public boolean collidesWith(TiledLayer tlayer,boolean pixelLevel) {
		return sprites[dir].collidesWith(tlayer, pixelLevel);
	}
	
	/**
	 * Gets the array of sprites.
	 * @return Sprites.
	 */
	public Sprite[] getSprites() {
		return sprites;
	}
	
	protected boolean isMovement(int input) {
	    int tilew = Zone.ZONE_TILE_WIDTH;
	    int tileh = Zone.ZONE_TILE_HEIGHT;
	    
		int moveFlag = input & GameCanvas.LEFT_PRESSED |
		               input & GameCanvas.RIGHT_PRESSED |
		               input & GameCanvas.UP_PRESSED |
		               input & GameCanvas.DOWN_PRESSED;
		
		if(moveFlag == 0)
			return false;
		
	    boolean onTile = ((x % tilew == 0 && y % tileh == 0));
	    
	    // Do not change Grace's direction orthogonally if
	    // Grace is not on a tile boundary
		if ((input & GameCanvas.LEFT_PRESSED) != 0) {
			if(!onTile && (dir == GO_UP || dir == GO_DOWN))
				return false;
			setDirection(GO_LEFT);
		}
		
		else if ((input & GameCanvas.RIGHT_PRESSED) != 0) {
			if(!onTile && (dir == GO_UP || dir == GO_DOWN))
				return false;
			setDirection(GO_RIGHT);
		}

		else if ((input & GameCanvas.UP_PRESSED) != 0) {
			if(!onTile && (dir == GO_LEFT || dir == GO_RIGHT))
				return false;			
			setDirection(GO_UP);
		}
		
		else if ((input & GameCanvas.DOWN_PRESSED) != 0) {
			if(!onTile && (dir == GO_LEFT || dir == GO_RIGHT))
				return false;			
			setDirection(GO_DOWN);
		}
		
		return true;
	}
	
	/** Resets jodav */
	public void reset() {
		dir = GO_DOWN;
		
		for(int j=0; j < 4; j++)
			sprites[j].setVisible(false);
		
		sprites[dir].setVisible(true);
		
		// Ask zone where we are and go there
		Point pt = Game.getJodavPos();
		
		setPosition(pt.getX(),pt.getY());
	}
	
	public int getID() {
		return getDirection();
	}
	
	public void setMovePending(boolean tf) {
		super.movePending = tf;
	}
	
	/**
	 * This is primarily used for MenuScreen Status
	 * @return
	 */
	public static Vector getStats() {
		
		int currATK = Stats.getATK(Jodav.getCurrLv());
		int currDEF = Stats.getDEF(Jodav.getCurrLv());
		int currMA = Stats.getMA(Jodav.getCurrLv());
		
		for(int i=0; i<currAbilities.size(); i++) {
			String abil = currAbilities.elementAt(i).toString();
			currATK += Stats.getModATK(abil);
			currDEF += Stats.getModDEF(abil);
			currMA += Stats.getModMA(abil);
		}
		Vector stats = new Vector();
		
		stats.addElement("Level: " + currLv);
		stats.addElement("HP: " + currHP + " / " + Stats.getMaxHP(currLv));
		stats.addElement("MP: " + currMP + " / " + Stats.getMaxMP(currLv));
		stats.addElement("EXP: " + currXP + " / " + Stats.getMaxXP(currLv));
		stats.addElement("ATK: " + currATK);
		stats.addElement("DEF: " + currDEF);
		stats.addElement("MA: " + currMA);
		
		return stats;
	}
	
	public static Vector getCurrAbil() {
		return currAbilities;
	}
	
	public static int getCurrLv() {
		return currLv;
	}
	
	public void setCurrHP(int newHP) {
		this.currHP = newHP;
	}
	
	public void setCurrMP(int newMP) {
		this.currMP = newMP;
	}
	
	public static int getCurrHP() {
		return currHP;
	}
	
	public static int getCurrMP() {
		return currMP;
	}
	
	public static void removeAbil(String abil, int position) {
		currAbilities.removeElementAt(position);
	}
	
	public static void setAbil(String abil, int position) {
		currAbilities.setElementAt(abil, position);
	}
}
