/**
 * 
 */
package rad.door;

import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.Sprite;
import rad.zone.Zone;

/**
 * @author Joe
 *
 */
public class TownDoor extends Door {

	/**
	 * @param door ID
	 */
	public TownDoor(int ID) {
		super(ID);
		// TODO Auto-generated constructor stub
	}

	public TownDoor(int ID,int col,int row) {
		super(ID);
		try {
	    	Image img = Image.createImage("/tiles.png");
	    	
	    	sprite = new Sprite(img,Zone.ZONE_TILE_WIDTH,Zone.ZONE_TILE_HEIGHT);
	    	
	    	sprite.setFrame(Zone.TID_DOOR_TOWN-1);
	    }
	    catch(Exception e) {
	    	e.printStackTrace();
	    	System.exit(1);
	    }
		
		x = Zone.ZONE_TILE_WIDTH * col;
		y = Zone.ZONE_TILE_HEIGHT * row;
		setPosition();
	}

}
