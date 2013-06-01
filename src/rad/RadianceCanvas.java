package rad;
import javax.microedition.lcdui.game.GameCanvas;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Graphics;

/**
 * @author Aruba
 *
 */
public class RadianceCanvas extends GameCanvas implements Runnable {
	
	private Display display;
	
	protected final static int DELAY_FRAME = 33;
	protected double dur = DELAY_FRAME;
	protected double alpha = 2. / (DELAY_FRAME + 1);
	
	public RadianceCanvas(Display d) {
		super(true);
		display=d;
	}
	
	public void startCanvas() {
		display.setCurrent(this);
		gameInitialize();
		Thread thread = new Thread(this);
		thread.start();
	}
	
	public void gameInitialize() {
		setFullScreenMode(true);
	}
	
	public void gameUpdate() {
		
	}
	
	public void gameDraw(Graphics g) {
		flushGraphics();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		Graphics g = getGraphics();
		
		try {
			// game loop
			while( true ) {
				
				long t0 = System.currentTimeMillis();
				
				gameUpdate();
				gameDraw(g);
				
				long t1 = System.currentTimeMillis();

				long latency = t1 - t0;

				long delay = smooth(latency);

				Thread.sleep(delay);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}

	}
	
	/**
	 * Smooth latency using EMA.<p>
	 * See http://en.wikipedia.org/wiki/Moving_average.
	 * @param latency
	 * @return Smoothed latency
	 */
	protected long smooth(long latency) {
		long delay = DELAY_FRAME - latency;

		if(delay < 0)
			delay = 0;
		
		dur = alpha * delay + (1 - alpha) * dur;
		
		return (long) dur;
	}

}
