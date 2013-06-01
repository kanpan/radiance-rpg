package rad.btsqr;

import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.Sprite;

import rad.zone.Zone;

public class BTSQR {
	
	protected int x;
	protected int y;
	protected int btsqr_id;
	protected Zone zone;
	protected Sprite sprite;
	
	public BTSQR(int btsqr_id) 
	{
		x=y=0;
		this.btsqr_id = btsqr_id;
	}
	
	public BTSQR(int btsqr_id, int col, int row) 
	{
		this.btsqr_id = btsqr_id;
		
		try {
	    	Image img = Image.createImage("/tiles.png");
	    	
	    	sprite = new Sprite(img,Zone.ZONE_TILE_WIDTH,Zone.ZONE_TILE_HEIGHT);
	    	
	    	sprite.setFrame(Zone.TID_COMBAT-1);
	    }
	    catch(Exception e) 
	    {
	    	e.printStackTrace();
	    	System.exit(1);
	    }
	    
		x = Zone.ZONE_TILE_WIDTH * col;
		y = Zone.ZONE_TILE_HEIGHT * row;
		setPosition();
	}
	
	/** Gets sprite for this BTSQR */
	public Sprite getSprite() 
	{
		return sprite;
	}

	/** Returns BTSQR ID where IDs are numbered from top left
	 * down to bottom right */
	public int getID() 
	{
		return btsqr_id;
	}
	
	public void setX(int x) 
	{
		this.x = x;
	}
	
	public void setY(int y) 
	{
		this.y = y;
	}
	
	public void setZone(Zone z) 
	{
		zone = z;
	}
	
	public Zone getZone() 
	{
		return zone;
	}
	
	/**
	 * Gets X position
	 * @return X position
	 */
	public int getX() 
	{
		return x;
	}

	/**
	 * Gets Y position
	 * @return Y position
	 */
	public int getY() 
	{
		return y;
	}
	
	public void setPosition() 
	{
		sprite.setPosition(x, y);
	}
	
	public void setPosition(int x, int y) 
	{
		setX(x);
		setY(y);
	}
	
	public static boolean isBTSQR(int TID) 
	{
		if(TID == Zone.TID_COMBAT)
			return true;
		return false;
	}
}