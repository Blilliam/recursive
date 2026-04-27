import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import javax.swing.JPanel;

/**
 * AppPanel serves as the primary container for the game, managing the rendering
 * surface and the main execution thread.
 */
public class AppPanel extends JPanel implements Runnable {
	// Utility for accessing system-level screen resources
	static Toolkit tk = Toolkit.getDefaultToolkit();

	// Constants defining the fixed resolution of the game panel
	public static final int WIDTH = 1024;
	public static final int HEIGHT = 768;

	public Dimension d = new Dimension(WIDTH, HEIGHT);

	// Main game thread for concurrent execution of the update/render loop
	public Thread t = new Thread(this);

	// Input listeners for user interaction
	MouseInput mouseHandler = new MouseInput();
	KeyboardInput keyHandler = new KeyboardInput();

	// Central game controller managing the state and logic
	GameObject gameObj = new GameObject(mouseHandler, keyHandler);

	/**
	 * Constructor: Initializes component properties, registers listeners, and
	 * starts the game thread.
	 */
	public AppPanel() {
		setPreferredSize(d);
		setFocusable(true);
		addKeyListener(keyHandler); // Register KeyListener for steering logic
		addMouseListener(mouseHandler);
		addMouseMotionListener(mouseHandler);
		t.start(); // Instantiate the 'run' method in a new thread
	}

	/**
	 * Overrides paintComponent to handle custom Graphics2D rendering.
	 * 
	 * @param g The Graphics context provided by the Swing framework.
	 */
	protected void paintComponent(Graphics g) {
		super.paintComponent(g); // Maintain the component paint chain
		Graphics2D g2 = (Graphics2D) g;

		// Configure rendering pipe for high-quality vector output
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		gameObj.draw(g2); // Delegate drawing to the GameObject manager
	}

	/**
	 * Implements the Runnable interface to provide the main game loop logic.
	 */
	@Override
	public void run() {
		// Infinite loop maintaining approximately 60 updates per second
		while (true) {
			// Ensure the panel retains focus for KeyboardInput capture
			requestFocusInWindow();

			gameObj.update(); // Process physics and state logic
			repaint(); // Request a call to paintComponent

			try {
				// Standard frame delay (~60 FPS)
				Thread.sleep(17);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}