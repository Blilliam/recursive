/**
 * Vec2 is a utility class representing a 2D vector (x, y). It provides
 * essential geometric and algebraic operations used for positioning, physics
 * calculations, and coordinate transformations.
 */
public class Vec2 {
	// Encapsulated fields to protect data integrity
	private double x, y;

	/**
	 * Constructor: Initializes the vector with specific coordinate values.
	 * 
	 * @param x The horizontal component.
	 * @param y The vertical component.
	 */
	public Vec2(double x, double y) {
		this.x = x;
		this.y = y;
	}

	// --- Accessors (Getters) and Mutators (Setters) ---

	public double getX() {
		return x;
	}

	/**
	 * Helper method to return the x-coordinate as an integer. Useful for AWT/Swing
	 * methods that require pixel-based parameters.
	 */
	public int getIntX() {
		return (int) x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	/**
	 * Helper method to return the y-coordinate as an integer.
	 */
	public int getIntY() {
		return (int) y;
	}

	public void setY(double y) {
		this.y = y;
	}

	// --- Vector Operations ---

	/**
	 * Performs vector addition.
	 * 
	 * @param o The vector to add.
	 * @return A new Vec2 representing the sum.
	 */
	public Vec2 add(Vec2 o) {
		return new Vec2(x + o.x, y + o.y);
	}

	/**
	 * Performs vector subtraction.
	 */
	public Vec2 sub(Vec2 o) {
		return new Vec2(x - o.x, y - o.y);
	}

	/**
	 * Scales the vector by a scalar value.
	 */
	public Vec2 scale(double s) {
		return new Vec2(x * s, y * s);
	}

	/**
	 * Calculates the dot product of two vectors.
	 */
	public double dot(Vec2 o) {
		return x * o.x + y * o.y;
	}

	/**
	 * Calculates the magnitude (length) of the vector using the Pythagorean
	 * theorem.
	 */
	public double length() {
		return Math.sqrt(x * x + y * y);
	}

	/**
	 * Converts the vector into a unit vector (length of 1).
	 */
	public Vec2 normalize() {
		double len = length();
		// Avoid division by zero errors
		return len == 0 ? new Vec2(0, 0) : scale(1.0 / len);
	}

	/**
	 * Rotates the vector 90 degrees counter-clockwise.
	 */
	public Vec2 perpendicular() {
		return new Vec2(-y, x);
	}

	/**
	 * Calculates the angle between this vector and another using arctangent.
	 */
	public double angleTo(Vec2 o) {
		return Math.atan2(o.y - y, o.x - x);
	}

	/**
	 * Static factory method to create a unit vector from a given angle.
	 * 
	 * @param radians The angle in radians.
	 * @return A unit Vec2.
	 */
	public static Vec2 fromAngle(double radians) {
		return new Vec2(Math.cos(radians), Math.sin(radians));
	}

	/**
	 * Overrides Object.toString to provide a readable coordinate format.
	 */
	@Override
	public String toString() {
		return "(" + x + ", " + y + ")";
	}
}