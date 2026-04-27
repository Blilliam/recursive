import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.Random;

/**
 * The Dvd class represents a recursive, bouncing graphical object. It supports
 * keyboard steering for the outermost child and hierarchical transformations
 * for nested "child" Dvd objects.
 */
public class Dvd {
	private double w, h;
	private Vec2 pos;
	private Vec2 vel;
	private Color color;
	private Random rand = new Random();
	private Dvd child;
	private boolean isRoot = false;
	private static final int MAX_DEPTH = 6;

	// Base fonts initialized at size 1 for scalable derivation
	private static final Font LOGO_FONT_BOLD = new Font("Arial", Font.BOLD, 1);
	private static final Font LOGO_FONT_ITALIC = new Font("Arial", Font.ITALIC, 1);

	/**
	 * Constructor: Initializes dimensions, random velocity, and recursive child
	 * generation.
	 * 
	 * @param depth The remaining recursion depth for child objects.
	 */
	public Dvd(double width, double height, double speed, int depth, double parentW, double parentH) {
		this.w = width;
		this.h = height;
		this.color = randomColor();

		// Initial center-alignment relative to parent container
		this.pos = new Vec2((parentW - w) / 2.0, (parentH - h) / 2.0);

		// Randomize initial direction vectors
		double dx = rand.nextBoolean() ? speed : -speed;
		double dy = rand.nextBoolean() ? speed : -speed;
		this.vel = new Vec2(dx, dy);

		if (depth > 1) {
			spawnChild(depth - 1);
		}
	}

	/**
	 * Updates position and handles collision detection.
	 * 
	 * @param isSteerable Determines if this specific instance responds to user
	 *                    input.
	 */
	public void update(double parentW, double parentH, boolean shouldMove, KeyboardInput input, boolean isSteerable) {
		if (isRoot) {
			// Root objects act as static containers; pass update call to child
			if (child != null) {
				child.update(this.w, this.h, true, input, true);
			}
			return;
		}

		if (shouldMove) {
			// Process directional input if steering is enabled for this instance
			if (isSteerable) {
				double steerSpeed = 3.0;
				if (input.up)
					vel.setY(-steerSpeed);
				if (input.down)
					vel.setY(steerSpeed);
				if (input.left)
					vel.setX(-steerSpeed);
				if (input.right)
					vel.setX(steerSpeed);
			}

			// Integrate velocity into position
			pos.setX(pos.getX() + vel.getX());
			pos.setY(pos.getY() + vel.getY());

			// Boundary collision logic: Reflect velocity and change color
			if (pos.getX() <= 0 && vel.getX() < 0) {
				vel.setX(Math.abs(vel.getX()));
				color = randomColor();
			} else if (pos.getX() + w >= parentW && vel.getX() > 0) {
				vel.setX(-Math.abs(vel.getX()));
				color = randomColor();
			}

			if (pos.getY() <= 0 && vel.getY() < 0) {
				vel.setY(Math.abs(vel.getY()));
				color = randomColor();
			} else if (pos.getY() + h >= parentH && vel.getY() > 0) {
				vel.setY(-Math.abs(vel.getY()));
				color = randomColor();
			}
		}

		// Recursive update call; only the first child of a root is steerable
		if (child != null) {
			child.update(this.w, this.h, true, input, false);
		}
	}

	/**
	 * Handles the recursive drawing of the DVD object and its hierarchy.
	 */
	public void draw(Graphics2D g) {
		AffineTransform old = g.getTransform();

		// Apply local translation based on position
		if (isRoot) {
			g.translate(0, 0);
		} else {
			g.translate(pos.getX(), pos.getY());
		}

		renderDVDLogo(g, (int) Math.ceil(w), (int) Math.ceil(h));

		// Recursive draw call
		if (child != null) {
			child.draw(g);
		}

		// Restore previous transformation state
		g.setTransform(old);
	}

	/**
	 * Internal rendering method for the DVD branding and shapes.
	 */
	private void renderDVDLogo(Graphics2D g, int width, int height) {
		g.setColor(color);
		if (this.isRoot) {
			g.fillRect(0, 0, width, height); // Background fill
		} else {
			g.fillOval(0, 0, width, height); // Primary disc shape

			g.setColor(Color.BLACK);
			float scale = (float) width / 100f;

			// Scalable font rendering for "DVD" text
			Font bold = LOGO_FONT_BOLD.deriveFont(32f * scale);
			g.setFont(bold);
			FontMetrics fm = g.getFontMetrics(bold);
			String txt = "DVD";
			g.drawString(txt, (width - fm.stringWidth(txt)) / 2, (int) (height * 0.52));

			// Render decorative inner ring
			int ringW = (int) (width * 0.75);
			int ringH = (int) (height * 0.22);
			g.drawOval((width - ringW) / 2, (int) (height * 0.58), ringW, ringH);

			// Scalable font rendering for "VIDEO" text
			Font italic = LOGO_FONT_ITALIC.deriveFont(9f * scale);
			g.setFont(italic);
			String sub = "VIDEO";
			g.drawString(sub, (width - g.getFontMetrics().stringWidth(sub)) / 2, (int) (height * 0.74));
		}
	}

	/**
	 * Converts the current instance into a static root container for the hierarchy.
	 */
	public void promoteToRoot(double screenW, double screenH) {
		this.isRoot = true;
		this.w = screenW;
		this.h = screenH;
		this.pos = new Vec2(0, 0);
		this.vel = new Vec2(0, 0);
		rebuildChildQueue(MAX_DEPTH);
	}

	private void spawnChild(int remainingDepth) {
		this.child = new Dvd(w * 0.5, h * 0.5, Math.abs(vel.getX()), remainingDepth, this.w, this.h);
	}

	/**
	 * Recursively reconfigures child dimensions based on updated root size.
	 */
	private void rebuildChildQueue(int currentDepthLevel) {
		if (currentDepthLevel <= 1)
			return;
		if (this.child == null) {
			spawnChild(currentDepthLevel - 1);
		} else {
			child.w = this.w * 0.5;
			child.h = this.h * 0.5;
			child.isRoot = false;
			child.rebuildChildQueue(currentDepthLevel - 1);
		}
	}

	/**
	 * Helper method for RGB color randomization.
	 */
	private Color randomColor() {
		return new Color(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256));
	}

	public Dvd getChild() {
		return child;
	}

	public Vec2 getPos() {
		return pos;
	}
}