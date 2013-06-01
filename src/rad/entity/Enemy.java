package rad.entity;

import java.util.Vector;
import javax.microedition.lcdui.game.Sprite;
import java.util.Random;

import rad.entity.Jodav;

/**
 * This class manages most of Enemy's functions.
 * Enemies only exist in 3D space
 * @author Aruba
 */
public class Enemy extends Entity {
	/** Directional sprites */
	protected Sprite[] sprites = new Sprite[4];
	
	/** Random number for enemy selection */
	protected Random rnd = new Random();
	private int rndEnemy = rnd.nextInt(1);
	
	/* Enemy's stats */
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
	public Enemy() {
		
		try {
			// don't need Sprite's for enemies				
		}
		catch(Exception e) {
			e.printStackTrace();
		}

		pc = false;
		currLv = Jodav.getCurrLv() - 1;
		currHP = Stats.getMaxHP(currLv);
		currMP = Stats.getMaxMP(currLv);
		currXP = 0;
		String[] abil = Stats.getAvailAbilities(currLv);
		for(int i=0; i<abil.length; i++)
			availAbilities.addElement(abil[i]);
		
		currAbilities.addElement(Stats.DARK_1);
		currAbilities.addElement(Stats.DARK_2);
		currAbilities.addElement(Stats.ASSAULT_1);
	}
	
	public void update(int input) {
		
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
	
	/**
	 * Sets the position.
	 * @param x X coordinate
	 * @param y Y coordinate
	 */
	public void setPosition() {
		sprites[dir].setPosition(x, y);
	}
		
	/**
	 * Sets jodav's direction.
	 * @param newdir Direction
	 */
	public void setDirection(int newdir) {
		
	}
	
	/** Advances jodav's frame	 */
	public void nextFrame() {
		
	}
	
	public int getID() {
		return getDirection();
	}
	
	/**
	 * This is primarily used for MenuScreen Status
	 * @return
	 */
	public static Vector getStats() {
		
		int currATK = Stats.getATK(Enemy.getCurrLv());
		int currDEF = Stats.getDEF(Enemy.getCurrLv());
		int currMA = Stats.getMA(Enemy.getCurrLv());
		
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
	
	public int getEnemy() {
		return rndEnemy;
	}
	
	public static void removeAbil(String abil, int position) {
		currAbilities.removeElementAt(position);
	}
	
	public static void setAbil(String abil, int position) {
		currAbilities.setElementAt(abil, position);
	}
}
