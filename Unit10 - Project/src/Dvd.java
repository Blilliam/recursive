import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.Random;

public class Dvd {
	private double w, h;
	private Vec2 pos;
	private Vec2 vel;
	private Color color;
	private Random rand = new Random();
	private Dvd child;
	private static final int MAX_DEPTH = 6;

	public Dvd(double width, double height, double speed, int depth, double parentW, double parentH) {
		this.w = width;
		this.h = height;
		this.color = randomColor();

		// Initial setup: start in the center
		this.pos = new Vec2((parentW - w) / 2.0, (parentH - h) / 2.0);

		// Removed ternary operators and replaced with if-else logic
		double dx = speed;
		if (!rand.nextBoolean()) {
			dx = -speed;
		}

		double dy = speed;
		if (!rand.nextBoolean()) {
			dy = -speed;
		}

		this.vel = new Vec2(dx, dy);

		if (depth > 1) {
			spawnChild(depth - 1);
		}
	}

	private void spawnChild(int remainingDepth) {
		this.child = new Dvd(w * 0.5, h * 0.5, Math.abs(vel.getX()), remainingDepth, this.w, this.h);
	}

	public void promoteToRoot(double screenW, double screenH) {
		this.w = screenW;
		this.h = screenH;
		this.pos.setX(0);
		this.pos.setY(0);

		rebuildChildQueue(MAX_DEPTH);
	}

	private void rebuildChildQueue(int currentDepthLevel) {
		if (currentDepthLevel <= 1)
			return;

		if (this.child == null) {
			spawnChild(currentDepthLevel - 1);
		} else {
			child.w = this.w * 0.5;
			child.h = this.h * 0.5;
			child.rebuildChildQueue(currentDepthLevel - 1);
		}
	}

	public void update(double parentW, double parentH, boolean shouldMove) {
		if (shouldMove) {
			pos.setX(pos.getX() + vel.getX());
			pos.setY(pos.getY() + vel.getY());

			if ((pos.getX() <= 0 && vel.getX() < 0) || (pos.getX() + w >= parentW && vel.getX() > 0)) {
				vel.setX(-vel.getX());
				//color = randomColor();
			}
			if ((pos.getY() <= 0 && vel.getY() < 0) || (pos.getY() + h >= parentH && vel.getY() > 0)) {
				vel.setY(-vel.getY());
				//color = randomColor();
			}
		}

		if (child != null) {
			child.update(this.w, this.h, true);
		}
	}

	public void draw(Graphics2D g) {
		g.setColor(color);
		g.fillRect((int) pos.getX(), (int) pos.getY(), (int) Math.ceil(w), (int) Math.ceil(h));

		if (child != null) {
			AffineTransform old = g.getTransform();
			g.translate(pos.getX(), pos.getY());
			child.draw(g);
			g.setTransform(old);
		}
	}

	private Color randomColor() {
		return new Color(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256));
	}

	public Dvd getChild() {
		return child;
	}

	public Vec2 getPos() {
		return pos;
	}

	public double getW() {
		return w;
	}

	public double getH() {
		return h;
	}
}