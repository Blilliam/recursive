import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;

/**
 * RunState manages the primary gameplay loop, including the recursive DVD
 * animation, the zooming camera logic, and the user interface.
 */
public class RunState extends State {
	private Dvd rootDvd;
	private boolean isZooming = false;
	private boolean isAuto = false;
	private double zoomProgress = 0; // Ranges from 0.0 to 1.0 during animation
	private double zoomSpeed = 0.04;

	// Accumulator to track the number of successful recursive transitions
	private int depthCount = 0;

	// Static UI constant for consistent font rendering
	private static final Font UI_FONT = new Font("Arial", Font.BOLD, 30);

	/**
	 * Constructor: Initializes the simulation with a root Dvd object.
	 * 
	 * @param gameObj Reference to the parent controller.
	 */
	public RunState(GameObject gameObj) {
		super(gameObj);
		// Initialize root with screen dimensions and promote to root status
		rootDvd = new Dvd(AppPanel.WIDTH, AppPanel.HEIGHT, 3.0, 5, AppPanel.WIDTH, AppPanel.HEIGHT);
		rootDvd.promoteToRoot(AppPanel.WIDTH, AppPanel.HEIGHT);
	}

	/**
	 * Update Tick: Handles state transitions, animation progress, and recursive
	 * logic.
	 */
	@Override
	public void update() {
		// Delegate physics and steering updates to the Dvd hierarchy
		rootDvd.update(AppPanel.WIDTH, AppPanel.HEIGHT, false, gameObj.getKeyHandler(), false);

		// Toggle automated zooming state via mouse input event
		if (MouseInput.rightClicked) {
			isAuto = !isAuto;
			MouseInput.rightClicked = false;
		}

		// Trigger zoom sequence if automation is active or a manual click is detected
		if ((MouseInput.mousePressed || isAuto) && !isZooming) {
			isZooming = true;
			zoomProgress = 0;
		}

		// Increment animation progress and handle transition upon completion
		if (isZooming) {
			zoomProgress += zoomSpeed;

			if (zoomProgress >= 1.0) {
				Dvd child = rootDvd.getChild();
				if (child != null) {
					// Update the hierarchy: promote child to root for the next iteration
					rootDvd = child;
					rootDvd.promoteToRoot(AppPanel.WIDTH, AppPanel.HEIGHT);
					depthCount++;
				}

				// Reset state variables for next animation cycle
				isZooming = false;
				zoomProgress = 0;
				MouseInput.mousePressed = false;
			}
		}
	}

	/**
	 * Render Tick: Handles AffineTransform camera operations and UI overlays.
	 * 
	 * @param g The Graphics context provided by the Panel.
	 */
	@Override
	public void draw(Graphics2D g) {
		// Cache the original transformation matrix to prevent state bleeding
		AffineTransform original = g.getTransform();

		// Perform camera zoom calculations if an animation is in progress
		if (isZooming && rootDvd.getChild() != null) {
			Dvd target = rootDvd.getChild();
			double currentScale = 1.0 + zoomProgress;

			// Calculate translation offsets to center the camera on the target Dvd
			double tx = -target.getPos().getX() * zoomProgress;
			double ty = -target.getPos().getY() * zoomProgress;

			// Update transformation matrix: Translate then Scale
			g.translate(tx * currentScale, ty * currentScale);
			g.scale(currentScale, currentScale);
		}

		// Render the DVD object hierarchy
		rootDvd.draw(g);

		// Revert to original transformation matrix for static UI rendering
		g.setTransform(original);

		// Render Heads-Up Display (HUD)
		g.setFont(UI_FONT);

		// Render current recursion depth
		g.setColor(Color.WHITE);
		g.drawString("DEPTH: " + depthCount, 20, 45);

		// Render Auto Mode status using conditional coloring
		if (isAuto) {
			g.setColor(Color.GREEN);
			g.drawString("AUTO RUN: ACTIVE", 20, 85);
		} else {
			g.setColor(new Color(255, 255, 255, 100)); // Render at reduced opacity
			g.drawString("AUTO RUN: OFF", 20, 85);
		}
	}
}