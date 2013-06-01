package rad;

import java.io.InputStream;
import java.util.Random;
import java.util.Vector;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.LayerManager;
import javax.microedition.lcdui.game.Sprite;
import javax.microedition.media.Manager;
import javax.microedition.media.Player;

import javax.microedition.rms.*;

import rad.door.Door;
import rad.npc.NPC;
import rad.npc.Speech;
import rad.btsqr.BTSQR;

import rad.zone.ZoneManager;
import rad.zone.Zone;
import rad.entity.Jodav;
import rad.entity.Enemy;
import rad.entity.Entity;
import rad.screen.GameScreen;
import rad.util.Point;
import rad.util.Trace;

import javax.microedition.m3g.*;

/**
 * This class manages the game world, including game states,
 * levels, input, 3D, and the radar scope.<br>
 * MIDI and WAV sound sources:<br>
 * 		http://www.aganazzar.com/midi.html<br>
 * 		http://www.midi4u.com/games/<br>
 * 		http://frogstar.com/wav/effects.asp<br>
 * 
 * @author Aruba, adapted from MazingGrace
 *
 */
public class Game {
	
	protected RadianceHUD hud;
	
	/** Sound toggle */
	protected static boolean soundsEnabled = false;
	
	/* Game states */
	protected static int code = 0;
	protected final static int GAME_STATE_START = code++;
	protected final static int GAME_STATE_NEW_ZONE = code++;
	protected final static int GAME_STATE_NONCOMBAT = code++;
	protected final static int GAME_STATE_CAUGHT = code++;
	protected final static int GAME_STATE_COMBAT = code++;
	protected final static int GAME_STATE_OVER = code++;
	
	/** Zone states */
	protected static int zoneCode = 0;
	protected final static int GAME_ZONE_FIELD = zoneCode++;
	protected final static int GAME_ZONE_DUNGEON = zoneCode++;
	protected final static int GAME_ZONE_TOWN = zoneCode++;
	
	/** Story states */
	protected static int storyCode = 0;
	public final static int GAME_STORY_NEW = storyCode++;
	public final static int GAME_STORY_ENROUTE_RINJ = storyCode++;
	public final static int GAME_STORY_DEFEATED_RINJ = storyCode++;
	public final static int GAME_STORY_ENROUTE_JAIHA = storyCode++;
	public final static int GAME_STORY_ENROUTE_FALSAR = storyCode++;
	public final static int GAME_STORY_RECEIVED_TOME = storyCode++;
	public final static int GAME_STORY_ENROUTE_EBEVET = storyCode++;
	public final static int GAME_STORY_ENROUTE_NORTH = storyCode++;
	public final static int GAME_STORY_END = storyCode++;
	
	/** Generic delay of game */
	protected final static int GAME_DELAY_CAUGHT = 30;
	
	/** 3D World stuff */
    private Graphics3D g3d;
    private World world;
    private Camera camera;
    private Light light;
   
    /** 3D Character jodav */
    private World jodavWorld;
    private Mesh jodavMesh;
    private Transform jodavTransform;
    
    /** 3D Character calte */
    private World calteWorld;
    private Mesh calteMesh;
    private Transform calteTransform;
    
    /** 3D Character enemy */
    private World enemyWorld;
    private Mesh enemyMesh;
    private Transform enemyTransform;
    
    /** In combat turn */
	protected static int turnCode = 0;
	public final static int JODAV_TURN = turnCode++;
	public final static int CALTE_TURN = turnCode++;
	public final static int ENEMY_TURN = turnCode++;
	
	/** Size of world objects on scope */
	protected final static int RADAR_SCALE = 4;
	
	/* Cycle time for Jodav / goal refresh on scope */
	protected final static int RADAR_GRACE_CYCLE = 4;
	protected final static int RADAR_GOAL_CYCLE = 2;
	
	/* Scope coordinates on physical screen */
	protected final static int RADAR_X = 0;
	protected final static int RADAR_Y = 0;
	
	/* Scope  colors*/
	protected final static int RADAR_BG_COLOR = 0xffff00;
	protected final static int RADAR_FG_COLOR = 0x191919;
	protected final static int RADAR_GOAL_COLOR = 0x00f000;
	protected final static int RADAR_REWARD_COLOR = 0x00ffff;
	protected final static int RADAR_GRACE_COLOR = 0x0000ff;
	protected final static int RADAR_ENEMY_COLOR = 0xff0000;
	protected final static int RADAR_BOULDER_COLOR = 0x669900;
	protected final static int RADAR_OBSTACLE_COLOR = 0x666666;
	
	/** Flag for game state management */
	protected final static int FIRST_TIME = -1;
	
	protected final static int MAGIC_X = 100;
	protected final static int MAGIC_Y = 100;
	
	/** Start state when world begins */
	protected static int state = GAME_STATE_START;
	protected static int story = GAME_STORY_NEW;
	protected static int zoneState = GAME_ZONE_TOWN;
	protected boolean newbie = true;
	
	/** Steps through loop where a step is about 33 ms */
	protected int steps = 0;
	
	protected int stepsStart = FIRST_TIME;
	
	/** Jodav in world */
	protected Jodav jodav;
	protected static Point jodavPos;
	
	/** Enemy in 3D */
	protected Enemy enemy;
	
	/** Scope (transparent) background image */
	protected Image scopeImage;
	
	/* Jodav / goal radar state */
	protected boolean jodavSymbolOn = true;
	protected boolean goalSymbolOn = true;
	
	/** Radar scope flag */
	protected boolean radarOn = false;
	
	/** Global layers contains every visible object */
	protected LayerManager layers;
	
	/* Screen dimensions */
	protected int screenw;
	protected int screenh;
	
	/* Tile / world dimensions */
	protected int tilew = Zone.ZONE_TILE_WIDTH;
	protected int tileh = Zone.ZONE_TILE_HEIGHT;
	protected int worldRows = Zone.ZONE_ROWS;
	protected int worldCols = Zone.ZONE_COLS;
	
	protected GameScreen gs;
	
	protected Random random = new Random();
	
	/* X, Y coordinate of view window in world */
	protected int xView;
	protected int yView;
	
	/** Graphics context for writing to game screen */
	protected Graphics graphics;
	
	/* Sounds, text for opening trap door(s) and enemy-player collision */
	protected static boolean soundsLoaded = false;
	protected static Player fieldSound;
	protected static Player townSound;
	protected static Player dungeonSound;
	protected static Player combatSound;
	protected static Player selectSound;
	
    protected int choice = 0;
	
	private static final String RSNAME = "RadRPG_RS";
	
	protected static Zone zone;
	protected static int zoneNum;
	protected static int zoneType;
	protected static int zoneSystem;
	
	protected Vector doors;
	protected Vector npcs;
	protected Vector btsqrs;
	
	public int textPosition = 0;

	/**
	 * Constructor
	 * @param gs Game screen
	 */
	public Game(GameScreen gs) {
		this.gs = gs;
		
		this.graphics = gs.getGraphics();
		
		this.screenw = gs.getWidth();
		
		this.screenh = gs.getHeight();
			
		ZoneManager.newGame();
		jodav = new Jodav();
		jodavPos = new Point(320,384);
		
		// This moves Jodav into the NPC to
		// initiate the intro speech
		jodav.setMovePending(true);
			

		// Load images and sounds
		/*try {
			life = new Sprite(Image.createImage("/life.png"),16,16);
			
			scopeImage = Image.createImage("/rscope2.png");
			
			loadSounds();
		}
		catch(Exception e) {
			e.printStackTrace();
		}*/
		
		
		//ZoneManager.setZone(1);
		//ZoneManager.setZoneSystem(ZoneManager.FIELD);
		
		random.setSeed(System.currentTimeMillis());		
	}
	
	/**
	 * Runs one cycle of game loop.
	 * @param input Player input
	 */
	public void loop(int input) throws Exception {
		steps++;

		if(state == GAME_STATE_START || state == GAME_STATE_NEW_ZONE)
			openZone();
		
		else if(state == GAME_STATE_OVER)
			gameOver();
		
		else if(state == GAME_STATE_NONCOMBAT)
			runZone(input);
			
		else if(state == GAME_STATE_CAUGHT)
			playerCaught();
		
		else if(state == GAME_STATE_COMBAT)
			runCombat(input);
	}
	
	/** Opens a new zone */
	protected void openZone() throws Exception {
		// Set up the new zone
		zone = ZoneManager.getZone();
		zoneType = ZoneManager.getZoneType();
		zoneSystem = ZoneManager.getZoneSystem();
		zoneNum = ZoneManager.getZoneNum();
		
		jodav.setZone(zone);
		
		if(state == GAME_STATE_START) {
			jodav.reset();
		}
		
		buildLayers();
		
		//zone.setJodav(jodav);
		
		if(state == GAME_STATE_START) {
			xView = 228;
			yView = Zone.WORLD_HEIGHT-screenh;
			//zone.setInNPC(true); // This initiates the intro speech
		}
		
		// Transition to noncombat state
		state = GAME_STATE_NONCOMBAT;
	}
	
	/** Ends Game */
	protected void gameOver() {
		gs.terminate();
		gs.gameOver();
	}
	
	/** Handles player getting caught by enemy */
	protected void playerCaught() {	
		state = GAME_STATE_COMBAT;
		gs.removeCommand(gs.menuCmd);
		gs.addCommand(gs.combatSelectCmd);
		gs.addCommand(gs.combatBackCmd);
		

		hud = new RadianceHUD(graphics);

		g3d = Graphics3D.getInstance();
		world = new World();
		jodavWorld = new World();
		calteWorld = new World();

		try {
			Object3D[] buffer;

			buffer = Loader.load("/battle_box.m3g");

			// find the world node
			for(int i = 0; i < buffer.length; i++) {
				if(buffer[i] instanceof World) {
					world = (World)buffer[i];
					break;
				}
			}
			
			buffer = Loader.load("/jodav.m3g");
			
			for(int i = 0; i < buffer.length; i++) {
				if(buffer[i] instanceof World) {
					jodavWorld = (World)buffer[i];
					break;
				}
			}
			
			for(int i=0; i<jodavWorld.getChildCount(); i++) {
				if(jodavWorld.getChild(i) instanceof Mesh) {
					System.out.println("Found JODAV");
					jodavMesh = (Mesh)jodavWorld.getChild(i);
					jodavWorld.removeChild(jodavMesh);
					world.addChild(jodavMesh);
					break;
				}
			}
		
			buffer = Loader.load("/calte.m3g");
			
			for(int i = 0; i < buffer.length; i++) {
				if(buffer[i] instanceof World) {
					calteWorld = (World)buffer[i];
					break;
				}
			}
			
			for(int i=0; i<calteWorld.getChildCount(); i++) {
				if(calteWorld.getChild(i) instanceof Mesh) {
					System.out.println("Found CALTE");
					calteMesh = (Mesh)calteWorld.getChild(i);
					calteWorld.removeChild(calteMesh);
					world.addChild(calteMesh);
					break;
				}
			}
			
			buffer = Loader.load("/cloaked.m3g");
			
			for(int i = 0; i < buffer.length; i++) {
				if(buffer[i] instanceof World) {
					enemyWorld = (World)buffer[i];
					break;
				}
			}
			
			for(int i=0; i<enemyWorld.getChildCount(); i++) {
				if(enemyWorld.getChild(i) instanceof Mesh) {
					System.out.println("Found ENEMY");
					enemyMesh = (Mesh)enemyWorld.getChild(i);
					enemyWorld.removeChild(enemyMesh);
					world.addChild(enemyMesh);
					break;
				}
			}

			jodavTransform = new Transform();
			jodavMesh.getTransform(jodavTransform);
			calteTransform = new Transform();
			calteMesh.getTransform(jodavTransform);
			enemyTransform = new Transform();
			enemyMesh.getTransform(enemyTransform);
			
			//rotate jodav
			jodavTransform.postRotate(-240, 0, 0, 1);
			jodavMesh.setTransform(jodavTransform);
			
			//move jodav to the right a bit
			jodavTransform.postTranslate(-3, 0, 0);
			jodavMesh.setTransform(jodavTransform);

			//rotate calte
			calteTransform.postRotate(-240, 0, 0, 1);
			calteMesh.setTransform(calteTransform);
			
			//rotate enemy
			//enemyTransform.postRotate(-240, 0, 0, 1);
			//enemyMesh.setTransform(enemyTransform);
			
			//move enemy forward a little
			enemyTransform.postTranslate(0, -1, -5);
			enemyMesh.setTransform(enemyTransform);
			
			camera = world.getActiveCamera();

			light = new Light();
			light.setMode(Light.AMBIENT);
			light.setIntensity(3.0f);

			// clean objects
			buffer = null;
		}
		catch(Exception e) {
			System.out.println("Loading error!");
			e.printStackTrace();
		}
	}

	public void addPaused() {
		
		Font f = Font.getFont(Font.FACE_PROPORTIONAL, 
				  Font.STYLE_BOLD,
				  Font.SIZE_LARGE);
		graphics.setFont(f);

		graphics.setColor(255,255,0);

		int x = MAGIC_X;
		int y = MAGIC_Y;

		graphics.drawString("PAUSED",x, y, Graphics.TOP|Graphics.LEFT);
	}
	
	/**
	 * Runs a "game running" cycle.
	 * @param input Player input
	 */
	protected void runZone(int input) {
			update(input);
			render();
	}

	/**
	 * Updates the world.
	 * @param input Player input
	 */
	protected void update(int input) {
		
		// Get Jodav's last known location.
		int xOld = jodav.getX();
		int yOld = jodav.getY();
		
		// Update Jodav
		jodav.update(input);

		// Update the view based on Jodav's current / old location
		scrollView(xOld,yOld);
		
		layers.setViewWindow(xView, yView, screenw, screenh);
		
		// Update everything in the zone
		zone.update();
		
		// Check to see if combat should occur
		if(zone.isInBTSQR()) {
			state = GAME_STATE_CAUGHT;
			return;
		}
		// Load new zones if reached a boundary
		// or we collide with a doorway
		handleZoneTransitions();
	}

	/** Renders the world */
	protected void render() {
		layers.paint(graphics, 0, 0);
		
		// If we collide with an NPC, get their speech, and
		// display a scrolling chat window with a "Scroll" soft button
		if(zone.isInNPC()) {
			paintSpeech(Speech.getSpeech(zoneNum,zone.getRecentNPC().getID(),story));
		}
		else {
			gs.removeCommand(gs.textCmd);
			textPosition = 0;
			if(newbie == false && story == GAME_STORY_NEW )
				// Once player reads through intro speech, change story state.
				story = GAME_STORY_ENROUTE_RINJ;
		}
		if(radarOn)
			showScope(RADAR_SCALE);
	}
	
	/** Handle zone transitions */
	protected void handleZoneTransitions() {
		int xOld = jodav.getX();
		int yOld = jodav.getY();
		
		// if Jodav is in a town
		if(zoneSystem == ZoneManager.TOWN) {
			//Town town = (Town)zone;
			// if we reach a zone boundary
			if( xOld < 0 || xOld > Zone.WORLD_WIDTH-tilew ||
					yOld < 0 || yOld > Zone.WORLD_HEIGHT-tileh ) {
				if(zoneNum == 15) {
					// leaving Rinj
					ZoneManager.setZone(16);
					jodav.setPosition(160,288);
					centerView();
				}
				else if(zoneNum == 59) {
					// leaving Jaiha
					ZoneManager.setZone(60);
					jodav.setPosition(96,128);
					xView = jodav.getX()/2;
					yView = 0;
				}
				else if(zoneNum == 5) {
					// leaving Ebevet
					ZoneManager.setZone(1);
					jodav.setPosition(128,288);
					centerView();
				}

				ZoneManager.setZoneSystem(ZoneManager.FIELD);
				state = GAME_STATE_NEW_ZONE;
			}
		}
		
		// if Jodav is in a doorway
		else if(zone.isInDoor()) {
			zone.setInDoor(false);
			state = GAME_STATE_NEW_ZONE;
			int door_id = zone.getRecentDoor().getID();
			
			if(zoneNum == 16) {
				// enter Rinj town
				ZoneManager.setZone(15);
				ZoneManager.setZoneSystem(ZoneManager.TOWN);
				jodav.setPosition(256, 32);
				xView=192;
				yView=0;
			} else if(zoneNum == 17 && door_id == 1) {
				// enter Rinj Woods from side
				ZoneManager.setZone(18);
				ZoneManager.setZoneSystem(ZoneManager.DUNGEON);
				jodav.setPosition(32, 160);
				xView=0;
				yView=jodav.getY()/2;
			} else if(zoneNum == 17 && door_id == 2) {
				// enter Rinj Woods from bottom
				ZoneManager.setZone(18);
				ZoneManager.setZoneSystem(ZoneManager.DUNGEON);
				jodav.setPosition(128, Zone.WORLD_HEIGHT-64);
				xView=jodav.getX()/2;
				yView=Zone.WORLD_HEIGHT-screenh;
			} else if(zoneNum == 18 && door_id == 1) {
				// emerge from west side of Rinj Woods
				ZoneManager.setZone(17);
				ZoneManager.setZoneSystem(ZoneManager.FIELD);
				jodav.setPosition(64, 224);
				xView=0;
				yView=jodav.getY()/2;
			} else if(zoneNum == 18 && door_id == 2) {
				// emerge from south side of Rinj Woods
				ZoneManager.setZone(17);
				ZoneManager.setZoneSystem(ZoneManager.FIELD);
				jodav.setPosition(192, 416);
				xView=jodav.getX()/2;
				yView=Zone.WORLD_HEIGHT-screenh;
			}
			else if(zoneNum == 60) {
				// Town: Jaiha
				ZoneManager.setZone(59);
				ZoneManager.setZoneSystem(ZoneManager.TOWN);
				jodav.setPosition(160, 32);
				xView=jodav.getX()/2;
				yView=0;
			}
			else if(zoneNum == 1) {
				// Town: Ebevet
				ZoneManager.setZone(5);
				ZoneManager.setZoneSystem(ZoneManager.TOWN);
				jodav.setPosition(416, 448);
				xView=Zone.WORLD_WIDTH-screenw;
				yView=Zone.WORLD_HEIGHT-screenh;
			}
			else if(zoneNum == 2) {
				// Dungeon: North
				ZoneManager.setZone(12);
				ZoneManager.setZoneSystem(ZoneManager.DUNGEON);
				jodav.setPosition(64, 448);
				xView=0;
				yView=Zone.WORLD_HEIGHT-screenh;
			}
			else if(zoneNum == 12) {
				// emerge from North Dungeon
				ZoneManager.setZone(2);
				ZoneManager.setZoneSystem(ZoneManager.FIELD);
				jodav.setPosition(352, 64);
				xView=228;
				yView=0;
			}
		} // inDoor
		
		/* if Jodav intersects with a world boundary, then 
		 * load a new zone
		 * reposition Jodav to the opposite side of the zone
		 * reposition the view window accordingly
		 * flag GAME_STATE_NEW_ZONE to build the new zone
		 */
		else if(zoneSystem <= ZoneManager.DUNGEON ) {
			if( xOld < 0 ) {
				ZoneManager.loadWest();
				jodav.setX(Zone.WORLD_WIDTH-tilew);
				xView = Zone.WORLD_WIDTH-screenw;
				state = GAME_STATE_NEW_ZONE;
			}
			else if ( xOld > Zone.WORLD_WIDTH-tilew ) {
				ZoneManager.loadEast();
				jodav.setX(0);
				xView = 0;
				state = GAME_STATE_NEW_ZONE;
			}
			else if ( yOld < 0 ) {
				ZoneManager.loadNorth();
				jodav.setY(Zone.WORLD_HEIGHT-tileh);
				yView = Zone.WORLD_HEIGHT-screenh;
				state = GAME_STATE_NEW_ZONE;
			}
			else if ( yOld > Zone.WORLD_HEIGHT-tileh) {
				ZoneManager.loadSouth();
				jodav.setY(0);
				yView = 0;
				state = GAME_STATE_NEW_ZONE;
			}
		}
	}
	
	protected void paintSpeech(Vector msg) {
		int widthCanvas = gs.getWidth();
		int heightCanvas = gs.getHeight();
		Graphics g = gs.getGraphics();
		
		// Get a font
		Font f =
			Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_PLAIN, Font.SIZE_LARGE);
	    g.setFont(f);

	    int fontHeight = f.getHeight();

	    // navy blue
		g.setColor(0, 78, 101);
		g.fillRect(0, heightCanvas-fontHeight*4, widthCanvas, heightCanvas);
		
		// Set the drawing color to white
		g.setColor(255,255,255);
		
		// Display the currently visible text, and
		// enable player to scroll the text
		if(msg != null && textPosition < msg.size()-4 ) {
			gs.addCommand(gs.textCmd);
			for(int i=textPosition; i<textPosition+4; i++)
				g.drawString(msg.elementAt(i).toString(),2,
						heightCanvas-fontHeight*(textPosition+4-i),Graphics.LEFT | Graphics.TOP);
		} 
		// If player scrolled beyond Vector size, 
		// restrict what is displayed, and remove
		// the Scroll button
		else if(textPosition >= msg.size()-4) {
			for(int i=msg.size()-4; i<msg.size(); i++)
				g.drawString(msg.elementAt(i).toString(),2,
						heightCanvas-fontHeight*(msg.size()-i),Graphics.LEFT | Graphics.TOP);
			newbie = false;
			gs.removeCommand(gs.textCmd);
		}			
	}
	
	protected void runCombat(int input) {
			update3D(input);
			render3D();
	}
	
	protected void update3D(int input) {
		hud.update();
		hud.updateSelection(input);
		
		if (hud.getjodavCurrHP() > 0 && hud.getenemyCurrHP() > 0) {
			state = GAME_STATE_COMBAT;
			moveCamera(turnCode);
		}
		else if (hud.getjodavCurrHP() > 0 && hud.getenemyCurrHP() < 0) {
			state = GAME_STATE_NONCOMBAT;
		}
		else if (hud.getjodavCurrHP() < 0 && hud.getenemyCurrHP() > 0) {
			state = GAME_STATE_OVER;
		}
		
		//jodav.setCurrHP(newHP);
		//jodav.setCurrMP(newMP);
	}

	/** Renders the 3D world */
	protected void render3D() {
		try {           
			g3d = Graphics3D.getInstance();
			g3d.bindTarget(graphics, true, 0);
			g3d.render(world);
		}
		catch(Exception e) { 
			System.out.println("draw error: " + e.getMessage());
			e.printStackTrace();
		}
		finally {
			g3d.releaseTarget();
		}

		hud.draw(gs.getWidth(), gs.getHeight());

		gs.flushGraphics();
	}
	
	/** Rotates camera to whomever's turn it is */
	protected void moveCamera(int turn)
	{
		if (turn == 0)
			camera.postRotate(0, 0, 0, 0);
		else if (turn == 1)
			camera.postRotate(1, 1, 1, 1);
		else if (turn == 2)
			camera.postRotate(2, 2, 2, 2);
	}

	/**
	 * Scroll the view parameters.
	 * @param xOld Jodav's last known x
	 * @param yOld Jodav's last known y
	 */
	protected void scrollView(int xOld,int yOld) {
		// Get Jodav's delta position
		int x = jodav.getX();
		int y = jodav.getY();
		
		int dx = x - xOld;
		int dy = y - yOld;
		
		// Get Jodav's screen coordinates
		int xScr = x - xView;
		int yScr = y - yView;
		
		// Get the center of the screen
		int xCenter = screenw / 2;
		int yCenter = screenh / 2;
		
		int dir = jodav.getDirection();
		
		// If Jodav within a tile of screen center, scroll the view
		if(xScr > xCenter && xScr < (xCenter + tilew))
			xView += dx;
		
		// Otherwise, if Jodav is off center and headed away from
		// center, move Jodav and view in same direction
		else if((xScr < xCenter && dir == Entity.GO_LEFT) ||
				(xScr > (xCenter + tilew) && dir == Entity.GO_RIGHT))
		{
            // Commented statement below causes world to 
			// shift w/ Jodav standing still -- not what we want
			// jodav.setX(x - dx);
			xView += dx;
		}
			
		// ...ditto for y axis
		if(yScr > yCenter && yScr < (yCenter + tileh))
			yView += dy;
		else if((yScr < yCenter && dir == Entity.GO_UP) ||
				(yScr > (yCenter + tileh) && dir == Entity.GO_DOWN))
		{
			yView += dy;
		}
		
		// Check view is not off the world
		if(xView < 0 || (xView+screenw) > zone.getWidth())
			xView -= dx;

		if(yView < 0 || (yView+screenh) > zone.getHeight())
			yView -= dy;
		
		// Check view is not off the world
		/*if(xView < 0 )
			xView =0;
		else if ((xView+screenw) > zone.getWidth())
			xView = zone.getWidth();

		if(yView < 0 )
			yView = 0;
		
		else if((yView+screenh) > zone.getHeight())
			yView = zone.getHeight();*/
	}
	
	/**
	 * Gets and initializes zone n.
	 * @return Level
	 */
	protected Zone getZone() {
		return zone;
	}
	
	/** Centers the view on Jodav
	 * 
	 */
	protected void centerView() {
		xView = jodav.getX() - screenw/2;
		yView = jodav.getY() - screenh/2;
		layers.setViewWindow(xView,yView,screenw,screenh);
	}
	
	/**
	 * Sets view so that Jodav is at bottom of view, one tile
	 * right of view's left edge and one tile above view's
	 * bottom edge.
	 */
	protected void resetView() {
		//xView = zone.getJodavPos().getX() - tilew;
		xView = jodav.getX() - tilew;

		//yView = zone.getJodavPos().getY() - screenh + 2 * tileh;
		yView = jodav.getY() - screenh + 2 * tileh;
		
		layers.setViewWindow(xView,yView,screenw,screenh);		
	}
	
	/**
	 * Builds the layer manager layers.
	 *
	 */
	protected void buildLayers() {
		// Create the layer stack
		layers = new LayerManager();
		
		// Add jodav's sprites
		Sprite[] jodavSprites = jodav.getSprites();
		for(int j=0; j < jodavSprites.length; j++)
			layers.append(jodavSprites[j]);
		
		// Add the doors
		doors = zone.getDoors();
		if(doors != null ) {
			for(int j=0; j < doors.size(); j++) {
				Door door = (Door) doors.elementAt(j);
				layers.append(door.getSprite());
			}
		}
		
		// Add NPCs
		npcs = zone.getNPCs();
		if(npcs != null) {
			for(int i=0; i < npcs.size(); i++) {
				NPC npc = (NPC) npcs.elementAt(i);
				layers.append(npc.getSprite());
			}
		}
		
		// Add Combat squares
		btsqrs = zone.getBTSQRs();
		if(btsqrs != null) {
			for(int i=0; i < btsqrs.size(); i++) {
				BTSQR btsqr = (BTSQR) btsqrs.elementAt(i);
				layers.append(btsqr.getSprite());
			}
		}

		layers.append(zone.getWallLayer());
		layers.append(zone.getFreeLayer());
		layers.append(zone.getBackgroundLayer());
	}
	
	/**
	 * Enables the radar on or off.
	 * @param tf True v. false.
	 */
	public void enableRadar(boolean tf) {
		radarOn = tf;
	}
	
	/**
	 * Shows the radar scope
	 * @param scale Rendering scale.
	 */
	protected void showScope(int scale) {
		// Paint the scope window
		if(scopeImage != null) {
			graphics.drawImage(scopeImage, RADAR_X, RADAR_Y,  Graphics.TOP|Graphics.LEFT);
		}
		else {
			graphics.setColor(RADAR_BG_COLOR);
			graphics.fillRect(RADAR_X, RADAR_Y, worldCols*scale, worldRows*scale);
		}
		
		// Show objects in world
		showWalls(scale);

		showScopeJodav(scale);
	}
	
	/**
	 * Shows the walls of the world.
	 * @param scale Rendering scale.
	 */
	protected void showWalls(int scale) {
		// Paint maze walls and rewards
		for(int row=0; row < worldRows; row++) {
			for(int col=0; col < worldCols; col++) {
				int tile = zone.getCell(col, row);
				
				if(tile == Zone.TID_NO_TILE)
					continue;
				
				if((tile <= Zone.TID_CLIFF_VERT_LF && tile >= Zone.TID_CLIFF_HORIZ_UP)
						|| (tile >= Zone.TID_CLIFF_WATER_BCK_UP && tile <= Zone.TID_CLIFF_WATER_BCK_DOWN)
						|| (tile >= Zone.TID_TREE && tile <= Zone.TID_CLIFF_CORNER_NW)) {
					graphics.setColor(RADAR_FG_COLOR);					
					graphics.fillRect(RADAR_X+col*scale,RADAR_Y+row*scale,scale,scale);
				}
			}
		}		
	}
	
	/**
	 * Show Jodav on the scope.
	 * @param scale Object scale
	 */
	protected void showScopeJodav(int scale) {
		// Locate and render Jodav on scope
		if((steps % RADAR_GRACE_CYCLE) == 0) {
			if(jodavSymbolOn)
				jodavSymbolOn = false;
			else
				jodavSymbolOn = true;
		}
		
		if(jodavSymbolOn)	{		
			int tileX = jodav.getX() / tilew;
			int tileY = jodav.getY() / tileh;
		
			graphics.setColor(RADAR_GRACE_COLOR);
			graphics.fillRect(RADAR_X+tileX*scale,RADAR_Y+tileY*scale,scale,scale);
		}		
	}
	
	
	/**
	 * Gets the zone number.
	 * @return Zone number.
	 */
	public static int getZoneNum() {
		return ZoneManager.getZoneNum();
	}
	
	/**
	 * Sets the zone number.
	 * @param zoneNum Zone number
	 */
	public static void setZoneNum(int zoneNum) {
		Game.zoneNum = zoneNum;
	}
	
	/** Resets the game state. */
	public static void reset() {
		Game.zoneNum = ZoneManager.getZoneNum();
	}
	
	public static Point getJodavPos() {
		return jodavPos;
	}
	
	public static void restoreGameState() {
		
		try {
			RecordStore rs = RecordStore.openRecordStore(RSNAME, false);
			byte[] record;// = new byte[rs.getRecordSize(1)];
			
			System.out.println("TRACE--RecordStore opened");
			
			/*String testString = "test";
			byte[] testByte = testString.getBytes();
			System.out.println("testString = " + testString +
					" | testByte = " + testByte +
					" | testByte parsed = " + new String(testByte));*/
			
			//record = new byte[rs.getRecordSize(1)];
			System.out.println(rs.getNumRecords());
			
			record = new byte[rs.getRecordSize(1)];
			record = rs.getRecord(1);
			System.out.println("TRACE--past getRecord()");
			int zoneNum = Integer.parseInt(new String(record));
			Game.zoneNum = zoneNum;
			
			System.out.println("TRACE--Game set zone");
			
			record = new byte[rs.getRecordSize(2)];
			record = rs.getRecord(2);
			int RSscore = Integer.parseInt(new String(record));
			//Game.score = RSscore;
			
			System.out.println("TRACE--Game set score");
			
			record = new byte[rs.getRecordSize(3)];
			record = rs.getRecord(3);
			int RSlives = Integer.parseInt(new String(record));
			//Game.lives = RSlives;
			
			System.out.println("TRACE--Restore " + zoneNum + RSscore + RSlives);
			
			/*rs.deleteRecord(1);
			rs.deleteRecord(2);
			rs.deleteRecord(3);*/
			rs.closeRecordStore();
			
			//System.out.println(RecordStore.listRecordStores());
			//RecordStore.deleteRecordStore(RSNAME);
			
		} catch (Exception e) {
			//e = new RecordStoreException();
			System.err.println(e.toString());
		}
	}
	
	/** Saves the game. */
	public static void saveGame() {

		try {
			RecordStore rs = null;
			String s;
			byte[] record;
			// open a RecordStore
			// making sure there is not one already created
			if(!emptyRS())
			{
				rs = RecordStore.openRecordStore(RSNAME,false);
				
				//need to make sure store is closed before we can delete it
				try{
					while(true)
					{
						rs.closeRecordStore();
					}
				}
				catch(RecordStoreNotOpenException e)
				{
				//	e.printStackTrace();
				}
				RecordStore.deleteRecordStore(RSNAME);
			}
			rs = RecordStore.openRecordStore(RSNAME, true);
			
			int zoneNum = Game.zoneNum;
			s = "" + zoneNum;
			record = s.getBytes();
			rs.addRecord(record,0,record.length);
			
			rs.closeRecordStore();
			
			System.out.println("TRACE--SAVING " + zoneNum);
			
			
			
		} catch (Exception e) {
			e = new RecordStoreException();
		}
	}
	
	public static boolean emptyRS()
	{
		String [] stores = RecordStore.listRecordStores();
		if(stores != null)
		{

			for(int i = 0; i < stores.length; i++)
			{
				if(stores[i].equals(RSNAME))
				{
					return false;
				}
			}
		}
		return true;
	}
	
	public static void setSound(boolean tf) {
		Game.soundsEnabled = tf;
	}
	
	public static boolean isSound() {
		return Game.soundsEnabled;
	}
	
	protected void loadSounds() throws Exception {
		if(!Game.soundsEnabled)
			return;
		
		/*if(Game.soundsLoaded) {
			if(zoneNum == 0 && Game.soundsEnabled)
				themeSound.start();
			return;
		}
		
		//InputStream is = getClass().getResourceAsStream("/trap.wav");
		InputStream is = getClass().getResourceAsStream("/gong.wav");
		trapSound = Manager.createPlayer(is, "audio/X-wav");
		trapSound.prefetch();
		
		is = getClass().getResourceAsStream("/ouch.wav");
		ouchSound = Manager.createPlayer(is, "audio/X-wav");
		ouchSound.prefetch();
		
		// Source: http://frogstar.com/wav/effects.asp
		is = getClass().getResourceAsStream("/evil_laf.wav");
		lafSound = Manager.createPlayer(is, "audio/X-wav");
		lafSound.prefetch();			
		
		
		// Source: http://frogstar.com/wav/effects.asp
		is = getClass().getResourceAsStream("/shazam2.wav");
		successSound = Manager.createPlayer(is, "audio/X-wav");
		successSound.prefetch();
		
		// Source: http://www.midi4u.com/games/
	    is = getClass().getResourceAsStream("/Legend_Of_Zelda.mid");
	    themeSound = Manager.createPlayer(is, "audio/midi");
	    themeSound.prefetch();
	    themeSound.setLoopCount(-1);
	    themeSound.start();*/
	    
	    Game.soundsLoaded = true;
	}
	
}
