package rad.zone;

import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.TiledLayer;

import java.util.Vector;

import rad.Game;
import rad.entity.Entity;
import rad.door.Door;
import rad.door.TownDoor;
import rad.door.DungeonDoorSide;
import rad.door.DungeonDoorFront;
import rad.npc.NPC;
import rad.npc.GuardFront;
import rad.npc.GuardBack;
import rad.npc.GuardLeft;
import rad.npc.OldMan;
import rad.btsqr.BTSQR;
import rad.util.Point;

public class Zone {
	
	public static final int WORLD_WIDTH = 512;
	public static final int WORLD_HEIGHT = 512;

	public static final int BG_TILE_HEIGHT = 32;
	public static final int BG_TILE_WIDTH = 32;
	
	public static final int ZONE_TILE_HEIGHT = 32;
	public static final int ZONE_TILE_WIDTH = 32;
	
	public static final int ZONE_ROWS = WORLD_HEIGHT / ZONE_TILE_HEIGHT;
	public static final int ZONE_COLS = WORLD_WIDTH / ZONE_TILE_WIDTH;

	public static final int BG_ROWS = WORLD_HEIGHT / BG_TILE_HEIGHT;
	public static final int BG_COLS = WORLD_WIDTH / BG_TILE_WIDTH;
	
	/* Tile Numbers */
	/* Background tiles */
	public static final int TID_NO_TILE = 0;
	public static final int TID_GRASS = 1;
	public static final int TID_DRIEDGRASS = 2;
	public static final int TID_SNOW = 3;
	
	/* forced combat tile */
	public static final int TID_COMBAT = 4;
	
	/* "free" tiles */
	public static final int TID_PATH = 5;
	public static final int TID_PATH_RIGHT = 6;
	public static final int TID_PATH_LEFT = 7;

	public static final int TID_WATER = 10;
	public static final int TID_WATER_UP = 23;
	public static final int TID_WATER_DOWN = 24;
	public static final int TID_WATER_LEFT = 25;
	public static final int TID_WATER_RIGHT = 26;
	public static final int TID_BRICK = 31;
	
	/* Door tiles */
	public static final int TID_ENTRANCE_SIDE = 8;
	public static final int TID_ENTRANCE_FRONT = 9;
	public static final int TID_DOOR_TOWN = 32;
	
	/* Wall Tiles 1 */
	public static final int TID_CLIFF_HORIZ_UP = 11;
	public static final int TID_CLIFF_HORIZ_DOWN = 12;
	public static final int TID_CLIFF_INCLINE_LEFT = 13;
	public static final int TID_CLIFF_INCLINE_RIGHT = 14;
	public static final int TID_CLIFF_DIA_FWD_UP = 15;
	public static final int TID_CLIFF_VERT_RT = 16;
	public static final int TID_CLIFF_DIA_BCK_UP = 17;
	public static final int TID_CLIFF_DIA_FWD_DOWN = 18;
	public static final int TID_CLIFF_DIA_BCK_DOWN = 19;
	public static final int TID_CLIFF_REV_INCLINE_LF = 20;
	public static final int TID_CLIFF_REV_INCLINE_RT = 21;
	public static final int TID_CLIFF_VERT_LF = 22;
	
	/* Wall Tiles 2 */
	public static final int TID_CLIFF_WATER_BCK_UP = 27;
	public static final int TID_CLIFF_WATER_FWD_UP = 28;
	public static final int TID_CLIFF_WATER_FWD_DOWN = 29;
	public static final int TID_CLIFF_WATER_BCK_DOWN = 30;
	
	/* Wall Tiles 3 */
	public static final int TID_HOUSE_PART_BRICK = 33;
	public static final int TID_HOUSE_NO_BRICK = 34;
	public static final int TID_TREE = 35;
	public static final int TID_DEAD_TREE = 36;
	public static final int TID_CLIFF_CORNER_SW = 37;
	public static final int TID_CLIFF_CORNER_SE = 38;
	public static final int TID_CLIFF_CORNER_NE = 39;
	public static final int TID_CLIFF_CORNER_NW = 40;
	
	/* NPCs */
	public static final int TID_GUARD_FRONT = 41;
	public static final int TID_GUARD_BACK = 42;
	public static final int TID_GUARD_LEFT = 43;
	public static final int TID_OLDMAN = 44;
	
	protected boolean caught;
	protected boolean inDoor;
	protected boolean inNPC;
	protected boolean inBTSQR;

	protected TiledLayer wallLayer;
	protected TiledLayer freeLayer;
	protected TiledLayer backgroundLayer;
	
	protected Vector doors;
	protected Vector npcs;
	protected Vector btsqrs;
	protected Door recentDoor;
	protected NPC recentNPC;
	
	private int zoneNum;
	private int[] layout;
	
	protected Zone(int zoneNumber) {
		
		zoneNum = zoneNumber;
		inDoor = false;
		inNPC = false;
		inBTSQR = false;
		
		try {
			layout = ZoneLayout.populateLayout(zoneNum);
			Image img = Image.createImage("/tiles.png");
			createBackground(img);
			createZone(img);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	protected void createBackground(Image img) throws Exception {
		backgroundLayer = new TiledLayer(BG_COLS,BG_ROWS,img,BG_TILE_WIDTH,BG_TILE_HEIGHT);
		
		//Image img = Image.createImage("/tiles.png");
		
		//backgroundLayer = new TiledLayer(ZONE_COLS,ZONE_ROWS,img,ZONE_TILE_WIDTH,ZONE_TILE_HEIGHT);
		
		// Depending on which zone we're in, change the grass type
		int bg_tile;
		if(zoneNum <= 5 || zoneNum == 12 || zoneNum == 13)
			bg_tile = TID_SNOW;
		else if (zoneNum == 10 || zoneNum == 11 || 
				(zoneNum >= 19 && zoneNum <= 23) ||
				(zoneNum >= 28 && zoneNum <= 31) ||
				zoneNum == 38 || zoneNum == 39)
			bg_tile = TID_DRIEDGRASS;
		else
			bg_tile = TID_GRASS;
		
		// Setup the background tiled layer map
		for (int row = 0; row < BG_ROWS; row++) {
			for (int col = 0; col < BG_COLS; col++) {
				backgroundLayer.setCell(col, row, bg_tile);
			}
		}		
	}
	
	/** Creates the map. */
	protected void createZone(Image img) throws Exception {
		//Image img = Image.createImage("tiles.png");
		
		wallLayer = new TiledLayer(ZONE_COLS,ZONE_ROWS,img,ZONE_TILE_WIDTH,ZONE_TILE_HEIGHT);
		freeLayer = new TiledLayer(ZONE_COLS,ZONE_ROWS,img,ZONE_TILE_WIDTH,ZONE_TILE_HEIGHT);
		
		doors = new Vector();
		int door_id = 1;
		
		npcs = new Vector();
		int npc_id = 1;
		
		btsqrs = new Vector();
		int btsqr_id = 1;
		
	    // Populate the map
		for (int row = 0; row < ZONE_ROWS; row++) {
			for (int col = 0; col < ZONE_COLS; col++) {
				// Extract the tile
				int tile = getCell(col,row);
				
				// If wall-type, add to wallLayer
				if ( (tile <= TID_CLIFF_VERT_LF && tile >= TID_CLIFF_HORIZ_UP)
						|| (tile >= TID_CLIFF_WATER_BCK_UP && tile <= TID_CLIFF_WATER_BCK_DOWN)
						|| (tile >= TID_HOUSE_PART_BRICK && tile <= TID_CLIFF_CORNER_NW))
					wallLayer.setCell(col, row, tile);
				
				// If free space, add to freeLayer
				if ( (tile <= TID_PATH_LEFT && tile >= TID_PATH) 
						|| tile == TID_WATER
						|| tile == TID_HOUSE_NO_BRICK 
						|| tile == TID_HOUSE_PART_BRICK
						|| (tile <= TID_WATER_RIGHT && tile >= TID_WATER_UP))
					freeLayer.setCell(col, row, tile);
				
				// Add doors to Vector, numbering them in the order they
				// are added: from top left to bottom right
				if(Door.isDoor(tile)) {
					
					Door door = null;
					if(Door.isDungeonSide(tile))
						door = new DungeonDoorSide(door_id++,col,row);

					else if(Door.isDungeonFront(tile)) 
						door = new DungeonDoorFront(door_id++,col,row);
					
					else if(Door.isTown(tile)) 
						door = new TownDoor(door_id++,col,row);
					
					door.setZone(this);
					doors.addElement(door);
				}
				
				// Add NPCs to Vector, numbering them in the order they
				// are added: from top left to bottom right
				if(NPC.isNPC(tile)) {
					
					NPC npc = null;
					if(NPC.isGuardFront(tile))
						npc = new GuardFront(npc_id++,col,row);

					else if(NPC.isGuardBack(tile))
						npc = new GuardBack(npc_id++,col,row);
					
					else if(NPC.isGuardLeft(tile))
						npc = new GuardLeft(npc_id++,col,row);
					
					else if(NPC.isOldMan(tile))
						npc = new OldMan(npc_id++,col,row);
					
					npc.setZone(this);
					npcs.addElement(npc);
				}
				
				// Add BTSQRs - battlesquares to Vector
				if(BTSQR.isBTSQR(tile)) {
					
					BTSQR btsqr = null;
					
					btsqr = new BTSQR(btsqr_id++,col,row);
					
					btsqr.setZone(this);
					btsqrs.addElement(btsqr);
				}
			}
		}
	}
	
	public void update() {
		if(isInBTSQR())
			setCaught(true);
	}
	
	/**
	 * Checks for collision with walls, doors, NPCs, and BTSQRs
	 * @param agent
	 * @return True if there's a collision with a wall or a BTSQR
	 */
	public boolean collide(Entity agent) {
		
		// Checks for collision with doors
		if(doors != null) {
			Door door;
			for (int i = 0; i < doors.size(); i++) {
				door = (Door) doors.elementAt(i);
				if (agent.getSprite().collidesWith(door.getSprite(), false)) {
					recentDoor = door;
					inDoor = true;
					return false;
				}
			}
		}
		
		// Checks for collision with NPCs
		inNPC = false;
		if(npcs != null) {
			NPC npc;
			for (int i = 0; i < npcs.size(); i++) {
				npc = (NPC) npcs.elementAt(i);
				if (agent.getSprite().collidesWith(npc.getSprite(), false)) {
					recentNPC = npc;
					inNPC = true;
					return false;
				}
			}	
		}
		
		// Checks for collision with BTSQRs
		inBTSQR = false;
		if(btsqrs != null) {
			BTSQR btsqr;
			for (int i = 0; i < btsqrs.size(); i++) {
				btsqr = (BTSQR) btsqrs.elementAt(i);
				if (agent.getSprite().collidesWith(btsqr.getSprite(), false)) {
					inBTSQR = true;
					btsqrs.removeElementAt(i);
					btsqr.getSprite().setVisible(false);
					//removeBattleTile(btsqr.getX(), btsqr.getY());
					return false;
				}
			}	
		}
		
		// Otherwise check for wall collision -- don't use pixel detection!
		return agent.getSprite().collidesWith(wallLayer, false);
	}
	
	/**
	 * @return the door most recently collided with
	 */
	public Door getRecentDoor() {
		return recentDoor;
	}
	
	/**
	 * @return the NPC most recently collided with
	 */
	public NPC getRecentNPC() {
		return recentNPC;
	}
	
	public int getCell(int col,int row) {
		if(row < 0 || row >= ZONE_ROWS || col < 0 || col >= ZONE_COLS)
			return TID_NO_TILE;
		
		int tile = layout[row * ZONE_COLS + col];
		
		return tile;
	}
	
	public void setCell(int col,int row, int tile) {
		if(row < 0 || row >= ZONE_ROWS || col < 0 || col >= ZONE_COLS)
			return;
		
		layout[row * ZONE_COLS + col] = tile;
	}
	
	private void removeBattleTile(Point p) {
		int x = p.getX()/ZONE_TILE_WIDTH;
		int y = p.getY()/ZONE_TILE_HEIGHT;
		setCell(x, y, TID_NO_TILE);
	}
	
	private void removeBattleTile(int x, int y) {
		x = x/ZONE_TILE_WIDTH;
		y = y/ZONE_TILE_HEIGHT;
		setCell(x, y, TID_NO_TILE);
	}
	
	public boolean isCaught() {
		return caught;
	}
	
	public boolean isInDoor() {
		return inDoor;
	}
	
	public boolean isInNPC() {
		return inNPC;
	}
	
	public boolean isInBTSQR() {
		return inBTSQR;
	}
	
	public void setCaught(boolean tf) {
		caught = tf;
	}
	
	public void setInDoor(boolean tf) {
		inDoor = tf;
	}
	
	public void setInNPC(boolean tf) {
		inNPC = tf;
	}
	
	public int getWidth() {
		return backgroundLayer.getWidth();
	}
	
	public int getHeight() {
		return backgroundLayer.getHeight();
	}
	
	public TiledLayer getBackgroundLayer() {
		return backgroundLayer;
	}
	
	public TiledLayer getWallLayer() {
		return wallLayer;
	}
	
	public TiledLayer getFreeLayer() {
		return freeLayer;
	}
	
	public Vector getDoors() {
		return doors;
	}
	
	public Vector getNPCs() {
		return npcs;
	}
	
	public Vector getBTSQRs() {
		return btsqrs;
	}

}
